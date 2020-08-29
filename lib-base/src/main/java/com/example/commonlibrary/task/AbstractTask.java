package com.example.commonlibrary.task;

import com.example.commonlibrary.utils.LogUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;



/*package*/ abstract class AbstractTask<Result> extends FutureTask<Result> implements ITask, IGroupedTask, IPriorityTask {
    private static final String TAG = AbstractTask.class.getSimpleName();
    
    protected String taskName = DEFAULT_TASK_NAME;
    protected String groupName = DEFAULT_GROUP_NAME;
    protected ITaskCallback<Result> callback;
    protected boolean serialExecute;
    protected int dualPolicy = DISCARD_NEW;
    protected int priority = PRIOR_NORMAL;
    int status = STATUS_NEW;
    private long afterSubmit, beginExecute, endExecute;

    public AbstractTask(Callable<Result> callable, ITaskCallback<Result> callback) {
        super(callable);
        this.callback = callback;
    }

    public AbstractTask(Runnable runnable) {
        super(runnable, null);
    }

    void afterSubmit() {
        afterSubmit = System.currentTimeMillis();
        if (callback != null) {
            Handlers.postMain(new Runnable() {
                @Override
                public void run() {
                    try {
                        callback.onBeforeCall();
                    } catch (Exception e) {
                        LogUtil.e(TAG, "do apply onBeforeCall ex, task=" + this, e);
                    }
                }
            });
        }
    }

    void afterExecute() {
        try {
            callback.onAfterCall();
        } catch (Exception e) {
            LogUtil.e(TAG, "do apply onAfterCall ex, task=" + this, e);
        }
    }

    @Override
    public void run() {
        this.beginExecute = System.currentTimeMillis();
        super.run();
    }

    /**
     * 任务执行结束时将会调用此方法
     * 不论是正常结束，异常结束，还是被取消此方法都将被调用
     */
    @Override
    protected void done() {
        this.endExecute = System.currentTimeMillis();
        if (callback != null) {
            Handlers.postMain(new Runnable() {
                @Override
                public void run() {
                    try {
                        afterExecute();
                        /**
                         * 如果任务被取消则调用onCancelled
                         */
                        if (isCancelled()) {
                            callback.onCancelled();
                        } else {
                            /**
                             * 如果任务正常结束则调用onComplete
                             * 异常结束get()将抛出异常会调用onException
                             */
                            callback.onComplete(get());
                        }
                    } catch (Throwable e) {
                        LogUtil.e(TAG, "call callback ex task="+AbstractTask.this, e);
                        callback.onException(e);
                    }
                }
            });
        }
        if (TPE.isDebug()) {
            LogUtil.e(TAG, "task execute end, task=" + this);
        }
    }

    @Override
    public void start() {
        TaskScheduler.INSTANCE.submit(this);
    }

    @Override
    public void stop() {
        this.cancel(true);//手动取消也会会触发一次done()
        this.setStatus(STATUS_CANCEL);
        TaskScheduler.INSTANCE.removeEndTask(this);
        if(TPE.isDebug()) {
            LogUtil.e(TAG, "task was canceled, \ntask=" + this);
        }
    }

    @Override
    protected void setException(Throwable t) {
        super.setException(t);
        this.endExecute = System.currentTimeMillis();//为了更好的打印日志，此时任务还未结束此字段为0

        LogUtil.e(TAG, "execute task exception, \ntask=" + this, t);
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String taskName() {
        return taskName;
    }

    @Override
    public String groupName() {
        return groupName;
    }

    /**
     * 依次和PriorityBlockingQueue中的对象比较，
     * priority值越大越早执行
     *
     * @param another
     * @return 返回负数表示优先级较高
     */
    @Override
    public int compareTo(IPriorityTask another) {
        return another.getPriority() - priority;
    }

    @Override
    public boolean serialExecute() {
        return serialExecute;
    }

    @Override
    public int dualPolicy() {
        return dualPolicy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTask that = (AbstractTask) o;

        if (!taskName.equals(that.taskName)) return false;
        return groupName.equals(that.groupName);

    }

    @Override
    public int hashCode() {
        int result = taskName.hashCode();
        result = 31 * result + groupName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "waitTime=" + (beginExecute - afterSubmit) +
                ", runTime=" + (endExecute - beginExecute) +
                ", totalTime=" + (endExecute - afterSubmit) +
                ", \ntaskName='" + taskName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", \nserialExecute=" + serialExecute +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}
