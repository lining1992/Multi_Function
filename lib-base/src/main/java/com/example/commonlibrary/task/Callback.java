package com.example.commonlibrary.task;


import com.example.commonlibrary.utils.LogUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;



/**
 * 应用通用callback
 * <p>
 *在Task里使用:
 * ----1.任务执行时调用doBackground
 * ----2.任务执行成功调用onComplete
 * ----3.任务执行异常调用onException
 * ----4.任务被取消会调用onCancelled
 * <p>
 * 如果要更新进度条请在doBackground手动调用progress方法更新进度
 * <p>
 */
public abstract class Callback<Result> implements Callable<Result>, ITaskCallback<Result> {

    /**
     * callback被执行前调用
     */
    @Override
    public void onBeforeCall() {

    }

    /**
     * callback被执行后调用，
     * 无论执行成功或失败此方法都将被调用
     */
    @Override
    public void onAfterCall() {

    }

    /**
     * Callable.call的别名，表明该方法在后台线程中执行
     *
     * @return
     * @throws Exception
     */
    public Result doBackground() throws Exception {
        return null;
    }

    /**
     * 任务执行完毕或Api调用成功的回调方法，在主线程里执行
     *
     * @param t
     */
    @Override
    public void onComplete(Result t) {

    }

    public void onDownload(int msgSize) {

    }


    /**
     * 任务执行进度的回调方法，在主线程里执行
     *
     * @param progress
     */
    @Override
    public void onProgress(long total, long current, Object... progress) {

    }

    /**
     * 任务执行失败的回调方法，在主线程里执行
     *
     * @param t
     */
    @Override
    public void onException(Throwable t) {
        StringWriter writer = new StringWriter(256);
        t.printStackTrace(new PrintWriter(writer));
        LogUtil.e(TPE.TAG, writer.toString());
    }

    /**
     * 任务执行被取消的回调方法，在主线程里执行
     *
     * @param
     */
    @Override
    public void onCancelled() {
        if (TPE.isDebug())
            LogUtil.d(TPE.TAG, "task cancelled");
    }


    /**
     * 如果要更新进度条请在doBackground调用此方法
     *
     * @param progress
     * @throws Exception
     */
    public void progress(final long total, final long current, final Object... progress) {
        Handlers.postMain(new Runnable() {
            @Override
            public void run() {
                onProgress(total, current, progress);
            }
        });
    }


    /**
     * 此方法不允许调用，可能产生死循环
     *
     * @return
     * @throws Exception
     */
    @Override
    @Deprecated
    public final Result call() throws Exception {
        return doBackground();
    }
}
