package com.baidu.iov.dueros.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

/**
 * @author v_lining05
 * @date 2020/8/30
 */
public class CustomTextView2 extends CustomTextView {

    private static final String TAG = "CustomTextView2";

    public CustomTextView2(Context context) {
        this(context, null);
    }

    public CustomTextView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        int dx = getWidth() / 2 - rect.width() / 2;
        int dy = getHeight() / 2 + rect.height() / 2;
        canvas.drawText(text, dx, dy, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
}
