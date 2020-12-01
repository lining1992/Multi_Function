package com.baidu.iov.dueros.test.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author v_lining05
 * @date 2020/8/30
 */
public class CustomConstraintLayout extends ConstraintLayout {

    private static final String TAG = "CustomConstraintLayout";

    public CustomConstraintLayout(Context context) {
        this(context, null);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
