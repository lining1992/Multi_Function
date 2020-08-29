package com.example.commonlibrary.android.handlercounter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/9 14:17
 */
class CounterHandler extends Handler {

    static final int MSG_COUNT = 100;

    CounterHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_COUNT:
                handleCountMessage(msg);
                break;
            default:
        }
    }

    private void handleCountMessage(Message msg) {
        if (!(msg.obj instanceof WeakReference)) {
            return;
        }
        WeakReference reference = (WeakReference) msg.obj;
        Object o = reference.get();
        if (o == null || !(o instanceof HandlerCounter)) {
            return;
        }

        HandlerCounter counter = (HandlerCounter) o;
        counter.onCount();
    }
}