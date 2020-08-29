package com.example.commonlibrary.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * <p/>
 * corePoolSize 核心线程数，指保留的线程池大小（不超过maximumPoolSize值时，线程池中最多有corePoolSize个线程工作）。
 * maximumPoolSize 指的是线程池的最大大小（线程池中最大有maximumPoolSize个线程可运行）。
 * keepAliveTime 指的是空闲线程结束的超时时间（当一个线程不工作时，过keepAliveTime 长时间将停止该线程）。
 * unit 是一个枚举，表示 keepAliveTime 的单位（
 * 有NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS，7个可选值）。
 * workQueue 表示存放任务的队列（存放需要被线程池执行的线程队列）
 * <p/>
 * 线程池的线程增长逻辑是：
 * 1.当池子大小小于corePoolSize就新建线程，并处理请求
 * 2.当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去从workQueue中取任务并处理
 * 3.当workQueue放不下新入的任务时(block queue是个有界队列)，新建线程入池，并处理请求，
 * 如果池子大小撑到了maximumPoolSize就用RejectedExecutionHandler来做拒绝处理
 */
public final class TaskScheduler {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MSG_TYPE_SUBMIT_TASK = 0;//任务提交事件
    private static final int MSG_TYPE_TASK_OVER = 1;//任务结束事件
    private static final int MSG_TYPE_CHANGE_PRIORITY = 2;//调整任务优先级事件
    private final int CORE_POOL_SIZE = CPU_COUNT * 2 + 1;//常驻最大线程数
    private final int MAXIMUM_POOL_SIZE = CPU_COUNT * 16 + 1;//最大线程数
    private final int QUEUE_SIZE = CORE_POOL_SIZE + 1;//队列大小
    private final int KEEP_ALIVE = 1;//空闲线程存活超时时间
    private final Handler handler;
    private final PriorityThreadPoolExecutor executor;
    private final TaskPriorityManager taskPriorityManager;
    public static TaskScheduler INSTANCE = instance();

    private TaskScheduler() {
        /**
         * 用于消息调度的线程
         */
        HandlerThread handlerThread = new HandlerThread("task-handler-thread");
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper(), new HandlerCallback());

        /**
         * task必须实现Comparable,否则要使用构造函数PriorityBlockingQueue(int, Comparator)
         * @see AbstractTask#compareTo(IPriorityTask)
         */
        BlockingQueue<Runnable> poolWorkQueue = new PriorityBlockingQueue<Runnable>(QUEUE_SIZE);
        this.executor = new PriorityThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE, TimeUnit.SECONDS,
                poolWorkQueue,
                Process.THREAD_PRIORITY_BACKGROUND,
                new ExecuteInterceptor());
        this.taskPriorityManager = new TaskPriorityManager(poolWorkQueue);
    }

    public static TaskScheduler instance() {
        if (INSTANCE == null) {
            synchronized (TaskScheduler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TaskScheduler();
                }
            }
        }
        return INSTANCE;
    }

    public void clearAllTask() {

    }

    /**
     * 提交新任务
     *
     * @param task
     */
    public void submit(AbstractTask<?> task) {
        handler.sendMessage(handler.obtainMessage(MSG_TYPE_SUBMIT_TASK, task));
    }

    public void submit(Runnable runnable) {
        this.submit(new AsyncTask<Void>(runnable));
    }

    private void doSubmitTask(AbstractTask<?> task) {
        //1.默认分组的任务直接提交到线程池
        if (task.groupName == IGroupedTask.DEFAULT_GROUP_NAME) {
            submitReadyTask(task);
        } else {
            //2.任务去重
            AbstractTask<?> oldTask = taskPriorityManager.getOldTask(task);
            if (oldTask != null) {
                int dualPolicy = task.dualPolicy();
                if (dualPolicy == IGroupedTask.DISCARD_NEW) {//2.如果同一个组的相同任务已经被提交，则丢弃该任务
                    if (TPE.isDebug()) {
                        Log.e(TPE.TAG, "任务被重复提交，新任务被丢弃，taskName=" + task.taskName());
                    }
                    return;
                } else if (dualPolicy == IGroupedTask.CANCEL_OLD) {
                    oldTask.stop();
                    if (TPE.isDebug()) {
                        Log.e(TPE.TAG, "任务被重复提交，老任务被取消，taskName=" + task.taskName());
                    }
                } else if (dualPolicy == IGroupedTask.FORCE_SUBMIT) {
                    task.taskName = task.taskName + "_1";
                    if (TPE.isDebug()) {
                        Log.e(TPE.TAG, "任务被重复提交，新任务被重命名，taskName=" + task.taskName());
                    }
                }
            }

            //3.首先添加任务到任务分组队列
            taskPriorityManager.addTask(task);
            //4.同时检测下任务是否可提交，不可提交则设置任务为等待状态
            if (canSubmit(task)) {
                submitReadyTask(task);
            } else {
                task.setStatus(ITask.STATUS_WAIT);
            }
        }
        //4.触发afterSubmit事件
        task.afterSubmit();
    }

    /**
     * 直接提交任务到线程池
     *
     * @param task
     */
   /*package*/ void submitReadyTask(AbstractTask<?> task) {
        task.setStatus(ITask.STATUS_READY);
        executor.submit(task);
    }

    /**
     * 判断当前任务是否可提交到线程池
     *
     * @param task
     * @return
     */
   /*package*/ boolean canSubmit(AbstractTask<?> task) {
        //1.线程池队列已满，则禁止新任务提交
        if (executor.getQueue().size() >= QUEUE_SIZE) {
            return false;
        }
        //2.如果是并行任务则可以直接提交
        if (!task.serialExecute) return true;
        //3.如果是串行任务，则要先判断下当前组里是否有正在执行的任务，有则禁止提交
        List<AbstractTask<?>> tasks = taskPriorityManager.taskGroups.get(task.groupName);
        if (tasks.isEmpty()) return true;
        for (AbstractTask t : tasks) {
            if (t.status == ITask.STATUS_RUNNING) return false;
        }
        return true;
    }

    /**
     * 移除执行结束的任务
     *
     * @param task
     */
   /*package*/ void removeEndTask(AbstractTask<?> task) {
        taskPriorityManager.removeTask(task);
    }

    /**
     * 提交等待中的任务
     */
    private void submitWaitTasks() {
        List<AbstractTask<?>> tasks = taskPriorityManager.getNextTaskGroup();
        if (tasks == null) return;
        for (AbstractTask<?> t : tasks) {
            if (t.status == ITask.STATUS_WAIT) {
                if (canSubmit(t)) submitReadyTask(t);
                break;
            }
        }
    }

    private void doTaskOver(AbstractTask<?> task) {
        removeEndTask(task);//从分组删除执行完毕的任务
        submitWaitTasks();//提交下一个等待中的任务
    }

    public void scheduleTask(int status, String groupName) {
        handler.sendMessage(handler.obtainMessage(MSG_TYPE_CHANGE_PRIORITY, status, 0, groupName));
    }

    private static final class ExecuteInterceptor implements PriorityThreadPoolExecutor.Interceptor {

        @Override
        public void before(Runnable r) {
            ((AbstractTask<?>) r).setStatus(ITask.STATUS_RUNNING);
        }

        @Override
        public void after(Runnable r, Throwable t) {
            ((AbstractTask) r).setStatus(ITask.STATUS_OVER);
            INSTANCE.handler.sendMessage(INSTANCE.handler.obtainMessage(MSG_TYPE_TASK_OVER, r));
        }
    }

    private static final class HandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TYPE_SUBMIT_TASK:
                    INSTANCE.doSubmitTask((AbstractTask<?>) msg.obj);
                    break;
                case MSG_TYPE_TASK_OVER:
                    INSTANCE.doTaskOver((AbstractTask<?>) msg.obj);
                    break;
                case MSG_TYPE_CHANGE_PRIORITY:
                    INSTANCE.taskPriorityManager.changeTaskPriority(msg.arg1, (String) msg.obj);
                    break;
            }
            return true;
        }
    }
}
