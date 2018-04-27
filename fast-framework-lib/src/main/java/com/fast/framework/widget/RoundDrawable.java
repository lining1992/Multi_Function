package com.fast.framework.widget;

import com.fast.framework.util.DensityUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;

/**
 * 绘制一个圆形进度的Drawable，中间可以包含图片
 * <p>
 * Created by lishicong on 2017/6/21.
 */

public class RoundDrawable extends Drawable {

    private Paint mPaint;
    private RectF mRectF;

    private int progressColor;
    private int progressColorComplete;
    private float progressWidth;

    private Bitmap mBitmap; // 中间图片
    private float currentPosition = 0; // 当前进度
    private float scale = 0.05f; // 图片距进度距离
    private int startAngle = 270;
    private float paddingScale = 0.9f; //  控件内偏距占空间本身的比例

    private int width; // 控件的宽度
    private int height; // 控件的高度

    public RoundDrawable(Context context) {
        initProgressTool(context);
    }

    private void initProgressTool(Context context) {
        mPaint = new Paint();
        progressColor = Color.parseColor("#3e414e"); // 进度条未完成的颜色
        progressColorComplete = Color.parseColor("#90008cff"); // 进度条已完成的颜色
        progressWidth = DensityUtil.dp2px(context, 6); // 圆环进度条的宽度
        mRectF = new RectF();
        width = (int) DensityUtil.dp2px(context, 280);
        height = (int) DensityUtil.dp2px(context, 280);
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        if (currentPosition == 0) {
            invalidateSelf();
        }
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        invalidateSelf();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawProgressCircle(canvas);
        drawCenterBg(canvas);
    }

    private void drawCenterBg(Canvas canvas) {
        if (mBitmap != null) {
            // 绘制中间的图片
            int width2 = (int) (mRectF.width() * scale);
            int height2 = (int) (mRectF.height() * scale);
            mRectF.set(mRectF.left + width2, mRectF.top + height2, mRectF.right - width2,
                    mRectF.bottom - height2);
            canvas.drawBitmap(mBitmap, null, mRectF, null);
        }
    }

    private void drawProgressCircle(Canvas canvas) {
        int size = height;
        int radius = (int) (size * paddingScale / 2f);
        mPaint.setAntiAlias(true);
        // 线宽
        mPaint.setStrokeWidth(progressWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 绘制进度的环
        mPaint.setColor(progressColor);
        canvas.drawCircle(width / 2, height / 2, radius, mPaint);
        // 绘制当前进度
        mRectF.set((width - radius * 2) / 2f, (height - radius * 2) / 2f,
                ((width - radius * 2) / 2f) + (2 * radius),
                ((height - radius * 2) / 2f) + (2 * radius));
        mPaint.setColor(progressColorComplete);
        canvas.drawArc(mRectF, startAngle, currentPosition, false, mPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }
}
