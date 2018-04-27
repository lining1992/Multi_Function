package com.fast.framework.widget.notifi;

import com.fast.framework.R;
import com.fast.framework.support.L;

import android.content.Context;
import android.widget.RemoteViews;

/**
 * 自定义下载进度通知栏
 * <p>
 * Created by lishicong on 2017/6/20.
 */

public class NotifiProgress extends BaseNotifi {

    private LooperThread mLooperThread;
    private Builder mBuilder;

    private boolean isPause = false; // 是否暂停
    private int mProgress = 0; // 进度

    public NotifiProgress(Context context, String ticker) {
        super(context, ticker);
    }

    public void setBuilder(Builder builder) {
        this.mBuilder = builder;
    }

    public void show() {
        updateStatus(mBuilder.defautlStatus);
    }

    private void updateStatus(String status) {
        RemoteViews remoteViews = getRemoeteViews(status);
        mNotifiBuilder.setContent(remoteViews);
        showNotifi(NOTIFI_ID_PROGRESS);
    }

    private RemoteViews getRemoeteViews(String status) {
        RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.fast_notifi_progress);
        mRemoteViews.setImageViewResource(R.id.fast_notifi_progress_icon, R.mipmap.ic_share_friends);
        mRemoteViews.setTextViewText(R.id.fast_notifi_progress_title, mBuilder.title);
        mRemoteViews.setTextViewText(R.id.fast_notifi_progress_status, status);
        mRemoteViews.setProgressBar(R.id.fast_notifi_progressbar, 100, mProgress, false); // true为不定长的进度
        return mRemoteViews;
    }

    /**
     * 更新下载进度线程
     */
    private class LooperThread extends Thread {

        @Override
        public void run() {
            while (mProgress <= 100) {

                if (!isPause) {
                    updateStatus(mBuilder.startStatus);
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开始下载
     */
    public void start() {
        isPause = false;
        if (mLooperThread != null && mLooperThread.isAlive()) {
            // undo
            L.d("isAlive");
        } else {
            mLooperThread = new LooperThread();
            mLooperThread.start();
        }
    }

    /**
     * 暂停下载
     */
    public void pause() {
        this.isPause = true;
        updateStatus(mBuilder.pauseStatus);
    }

    /**
     * 停止下载
     */
    public void stop() {
        if (mLooperThread != null) {
            mLooperThread.interrupt();
        }
        mLooperThread = null;

        isPause = false;
        updateStatus(mBuilder.stopStatus);
    }

    /**
     * 异常
     */
    public void error() {
        this.isPause = true;
        updateStatus(mBuilder.errorStatus);
    }

    /**
     * 刷新进度
     *
     * @param progress
     */
    public void refresh(int progress) {
        this.mProgress = progress;
    }

    public static class Builder {

        /**
         * 下载标题
         */
        private String title;
        /**
         * 默认状态
         */
        private String defautlStatus;
        /**
         * 开始状态
         */
        private String startStatus;
        /**
         * 暂停状态
         */
        private String pauseStatus;
        /**
         * 停止状态
         */
        private String stopStatus;
        /**
         * 错误状态
         */
        private String errorStatus;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDefautlStatus(String defautlStatus) {
            this.defautlStatus = defautlStatus;
        }

        public void setStartStatus(String startStatus) {
            this.startStatus = startStatus;
        }

        public void setPauseStatus(String pauseStatus) {
            this.pauseStatus = pauseStatus;
        }

        public void setStopStatus(String stopStatus) {
            this.stopStatus = stopStatus;
        }

        public void setErrorStatus(String errorStatus) {
            this.errorStatus = errorStatus;
        }

        public Builder build() {
            return this;
        }
    }

}
