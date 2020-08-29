package com.example.commonlibrary.task;


/*
 * 
 * TPE: ThreadPoolExecutor
 * */

import android.app.Activity;
import android.content.Context;


public class TPE extends ThreadPoolExecutor {
    public static final String TAG = "TPE";
    
    public static final String ORDERED_TASKS ="Ordered queue";
    public static final String CONCURRENT_TASKS = "Concurrent queue";
    
    private static  boolean DEBUG = false;
    
    private static TPE mTpe;
    
    private TPE() {}
    
    public static TPE get() {
        if (mTpe == null) {
            mTpe = new TPE();
        }
        return mTpe;
    }
    
    public static boolean isDebug() {
        return DEBUG;
    }
    
    public static void setDebug(boolean bool) {
        DEBUG = bool;
    }

    @Override
    public void submitUiTaskBeforeCheck(Runnable runnable, Context context) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        Handlers.postMain(runnable);
    }

    @Override
    public void submitUiTask(Runnable runnable) {
        Handlers.postMain(runnable);
    }

    @Override
    public void submitTaskSimple(Runnable runnable) {
        AsyncTask.start(runnable);
    }
    
    @Override
    public <Result> AsyncTask<Result> submitDefaultOrderedTasks(Callback<Result> callback) {
        return submitTask(ORDERED_TASKS + System.currentTimeMillis(), ORDERED_TASKS, AsyncTask.FORCE_SUBMIT, callback);
    }
    @Override
    public <Result> AsyncTask<Result> submitDefaultConcurrentTasks(Callback<Result> callback) {
        return submitDefaultTask(CONCURRENT_TASKS + System.currentTimeMillis(), CONCURRENT_TASKS, AsyncTask.FORCE_SUBMIT, callback);
    }
    
    @Override
    public  <Result> AsyncTask<Result> submitTask(String taskName, String groupName, Callback<Result> callback) {
        return submitDefaultTask(taskName, groupName, AsyncTask.DISCARD_NEW, callback);
    }
    
    @Override
    public  <Result> AsyncTask<Result> submitTask(String taskName, String groupName, int dualPolicy, Callback<Result> callback) {
        return submitTask(taskName, groupName, IPriorityTask.PRIOR_NORMAL, dualPolicy, true, callback);
    }
    
    @Override
    public  <Result> AsyncTask<Result> submitDefaultTask(String taskName, String groupName, int dualPolicy, Callback<Result> callback) {
        return submitTask(taskName, groupName, IPriorityTask.PRIOR_NORMAL, dualPolicy, false, callback);
    }
    
    @Override
    public synchronized <Result> AsyncTask<Result> submitTask(String taskName, String groupName, int priority, int dualPolicy, boolean serialExecute, Callback<Result> callback) {
        AsyncTask<Result> task = new AsyncTask<Result>(callback, callback)
                .taskName(taskName)
                .priority(priority)
                .dualPolicy(dualPolicy)
                .groupName(groupName)
                .serialExecute(serialExecute);
        task.start();
        return task;
    }
    
    @Override
    public void cancelTaskByGroupName(String groupName){
        TaskScheduler.INSTANCE.scheduleTask(UIChangeEvent.STATUS_DESTROY, groupName);
    }

}
