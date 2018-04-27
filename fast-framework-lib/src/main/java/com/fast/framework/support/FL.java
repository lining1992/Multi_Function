package com.fast.framework.support;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 屏幕上输出log.需要继承自BaseActivity.
 * <p>
 * Created by lishicong on 2017/5/18.
 */

public class FL {

    private static final boolean LOG_TRACE_ENABLED = true;

    private static final int NUMBER_OF_LINES = 20;

    private FL() {
    }

    private static FL instance;
    private TextView mTextView;

    public static FL getInstance() {
        if (instance == null) {
            synchronized (FL.class) {
                if (instance == null) {
                    instance = new FL();
                }
            }
        }
        return instance;
    }

    public void setTextView(TextView textView) {
        this.mTextView = textView;
    }

    public void log(String message) {
        mLines.add(message);

        if (mLines.size() > NUMBER_OF_LINES) {
            mLines.poll();
        }

        if (LOG_TRACE_ENABLED) {
            redraw(mLines);
        }
    }

    private final Queue<String> mLines = new ArrayDeque<>();

    private void redraw(Collection<String> texts) {

        Spannable spannable = new SpannableString(TextUtils.join("\n", texts));

        mTextView.setText(spannable);
    }
}
