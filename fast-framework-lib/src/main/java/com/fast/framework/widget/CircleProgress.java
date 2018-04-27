/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.widget;

import com.fast.framework.util.DensityUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形进度条
 * <p>
 * ps: 圆环会自动根据宽度高度取小边的大小的比例 你也可以自己设置圆环的大小@see #scrollBy(int, int)
 */
public class CircleProgress extends View {

    private int width; // 控件的宽度
    private int height; // 控件的高度
    private int radius; // 圆形的半径

    private Paint mPaint = new Paint();
    private RectF mRectF = new RectF();

    private int progressColor;
    private int progressColorComplete;
    private int progressColorCenter;
    private float progressWidth;

    private int startAngle = 270;
    private float paddingScale = 0.9f; // 控件内偏距占空间本身的比例

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressColor = Color.parseColor("#3e414e"); // 进度条未完成的颜色
        progressColorComplete = Color.parseColor("#90008cff"); // 进度条已完成的颜色
        progressColorCenter = Color.parseColor("#00000000"); // 圆中间的背景颜色
        progressWidth = DensityUtil.dp2px(context, 2); // 圆环进度条的宽度
    }

    @Override
    protected void onDraw(Canvas canvas) {

        width = getWidth();

        int size = height = getHeight();
        if (height > width) {
            size = width;
        }

        radius = (int) (size * paddingScale / 2f);
        mPaint.setAntiAlias(true);

        mPaint.setStrokeWidth(progressWidth); // 线宽
        mPaint.setStyle(Paint.Style.STROKE);

        // 绘制进度的环
        mPaint.setColor(progressColor);

        canvas.drawCircle(width / 2, height / 2, radius, mPaint);

        // 绘制当前进度
        mRectF.set((width - radius * 2) / 2f, (height - radius * 2) / 2f, ((width - radius * 2) / 2f) + (2 * radius),
                   ((height - radius * 2) / 2f) + (2 * radius));
        mPaint.setColor(progressColorComplete);
        // canvas.drawArc(rectf, startAngle, value * 3.6f, true, paint);
        canvas.drawArc(mRectF, startAngle, currentPosition, false, mPaint);

        // 绘制中间部分
        mPaint.setColor(progressColorCenter);
        canvas.drawCircle(width / 2, height / 2, radius - progressWidth, mPaint);

        if (bitmap != null) {
            // 绘制中间的图片
            int width2 = (int) (mRectF.width() * scale);
            int height2 = (int) (mRectF.height() * scale);
            mRectF.set(mRectF.left + width2, mRectF.top + height2, mRectF.right - width2, mRectF.bottom - height2);
            canvas.drawBitmap(bitmap, null, mRectF, null);
        }

        super.onDraw(canvas);
    }

    /**
     * 设置圆环进度条的宽度 px
     */
    public void setProdressWidth(float progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置开始的位置
     *
     * @param startAngle 0~360
     *                   <p>
     *                   ps 0代表在最右边 90 最下方 按照然后顺时针旋转
     */
    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    /**
     * 设置进度条之前的颜色
     *
     * @param precolor
     */
    public void setProgressColor(int precolor) {
        this.progressColor = precolor;
    }

    /**
     * 设置进度颜色
     *
     * @param color
     */
    public void setProgressColorComplete(int color) {
        this.progressColorComplete = color;
    }

    /**
     * 设置圆心中间的背景颜色
     *
     * @param color
     *
     * @return
     */
    public void setProgressColorCenter(int color) {
        this.progressColorCenter = color;
    }

    // ------------------------------ 显示进度 ------------------------------
    private float currentPosition = 0;
    private int duration;

    /**
     * 设置进度
     *
     * @param currentPosition
     */
    public void setCurrentDuration(int currentPosition) {
        if (duration > 0) {
            if (currentPosition > duration) {
                return;
            }
            this.currentPosition = currentPosition * 360f / duration;
            invalidate();
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // ------------------------------ 中间图片 ------------------------------
    private Bitmap bitmap; // 中间图片
    private float scale = 0.15f; // 中间背景图片相对圆环的大小的比例

    /**
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * @param paddingScale
     */
    public void setPaddingScale(float paddingScale) {
        this.paddingScale = paddingScale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}
