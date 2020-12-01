package com.baidu.iov.dueros.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author v_lining05
 * @date 2020/9/11
 */
class CostomView extends View {

    private final Paint paint2;
    private Canvas canvas;
    private Paint paint;
    private float x;
    private float y;
    private Path path;

    public CostomView(Context context) {
        this(context, null);
    }

    public CostomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CostomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(12);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
//        canvas.drawLine(getX(), getY(), getWidth(), getHeight(), paint);
//        canvas.drawLine(getY(), getX(), getHeight(), getWidth(), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("debugli", "getRawX=" + event.getRawX() + "==getRawY==" + event.getRawY());
        Log.d("debugli", "getX=" + event.getX() + "==getY==" + event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = getX();
                y = getY();
//                path.lineTo(x, y);
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (canvas != null) {
//                    path.moveTo(event.getRawX(), event.getRawY());
                    path.lineTo(event.getRawX(), event.getRawY());
                    path.setLastPoint(event.getRawX(), event.getRawY());
                    canvas.drawPoint(event.getRawX(), event.getRawY(), paint2);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
