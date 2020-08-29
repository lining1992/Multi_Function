package com.example.commonlibrary.task;


import android.content.Context;

public abstract class ThreadPoolExecutor {


    public abstract void submitUiTaskBeforeCheck(Runnable runnable, Context context);
    public abstract void submitUiTask(Runnable runnable);
    
    public abstract void submitTaskSimple(Runnable runnable);
    
    /**
     * 默认串行 即顺序执行的线程队列 
     * 默认组名TPE.ORDERED_TASKS ="Ordered queue" 可以根据这个进行组名进行取消 添加等操作
     * 默认每任务名字 ORDERED_TASKS + System.currentTimeMillis()
     * 默认提交规则 不去重 AsyncTask.FORCE_SUBMIT
     * 默认优先级为IPriorityTask.PRIOR_NORMAL = 1
     * @param callback
     * @return
     */
    public abstract <Result> AsyncTask<Result> submitDefaultOrderedTasks(Callback<Result> callback);
    
    /**
     * 默认并行 即并发执行的线程队列 
     * 默认组名CONCURRENT_TASKS = "Concurrent queue" 可以根据这个进行组名进行取消 添加等操作
     * 默认每任务名字 CONCURRENT_TASKS + System.currentTimeMillis()
     * 默认提交规则 不去重 AsyncTask.FORCE_SUBMIT
     * 默认优先级为IPriorityTask.PRIOR_NORMAL = 1
     * @param callback
     * @return
     */
    public abstract <Result> AsyncTask<Result> submitDefaultConcurrentTasks(Callback<Result> callback);
    
    
    public  abstract <Result> AsyncTask<Result> submitTask(String taskName, String groupName, Callback<Result> callback);

    public  abstract <Result> AsyncTask<Result> submitTask(String taskName, String groupName, int dualPolicy, Callback<Result> callback);
    
    public  abstract <Result> AsyncTask<Result> submitDefaultTask(String taskName, String groupName, int dualPolicy, Callback<Result> callback);
    
    /**
     * 
     * @param taskName  任务名称
     * @param groupName  分组名称
     * @param priority  优先级 (IPriorityTask.PRIOR_NORMAL = 1, IPriorityTask.PRIOR_UI = 32, IPriorityTask.PRIOR_HIGH = 256)
     * @param dualPolicy  去重规则 (丢弃新任务:AsyncTask.DISCARD_NEW;取消老任务:AsyncTask.CANCEL_OLD;强制提交，任务可重复:AsyncTask.FORCE_SUBMIT;)
     * @param serialExecute 串行 还是并行   (serialExecute true 串行)
     * @param callback 回调
     * @return 线程任务
     */
    public abstract <Result> AsyncTask<Result> submitTask(String taskName, String groupName, int priority, int dualPolicy, boolean serialExecute, Callback<Result> callback) ;

    /**
     * 按组名取消整组线程队列
     * */
    public abstract void cancelTaskByGroupName(String groupName);
}
