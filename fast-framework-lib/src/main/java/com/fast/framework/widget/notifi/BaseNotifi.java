package com.fast.framework.widget.notifi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by lishicong on 2017/6/20.
 */

public abstract class BaseNotifi {

    protected static final int NOTIFI_ID_WIDGET = 1001;
    protected static final int NOTIFI_ID_PROGRESS = 1002;

    protected Context mContext;

    protected NotificationManager mNotificationManager;
    protected NotificationCompat.Builder mNotifiBuilder;

    /**
     * @param context
     * @param ticker  (第一次提示消息的时候显示在通知栏上)
     */
    protected BaseNotifi(Context context, String ticker) {
        this.mContext = context;

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifiBuilder = new NotificationCompat.Builder(context);

        PendingIntent pendingIntent = getDefalutIntent(context, Notification.FLAG_ONGOING_EVENT);

        mNotifiBuilder.setContentIntent(pendingIntent);
        mNotifiBuilder.setWhen(System.currentTimeMillis());
        mNotifiBuilder.setTicker(ticker);
        mNotifiBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mNotifiBuilder.setOngoing(true);
        // mNotifiBuilder.setSmallIcon(R.mipmap.ic_share_friends); // 通知栏的小图标
    }

    protected void showNotifi(int notifiId) {
        Notification notify = mNotifiBuilder.build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(notifiId, notify);
    }

    protected void clearNotifi(int notifiId) {
        mNotificationManager.cancel(notifiId);
    }

    private PendingIntent getDefalutIntent(Context context, int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 收起系统通知栏
     *
     * @param context
     */
    public static void closeStatusBar(Context context) {
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }
}
