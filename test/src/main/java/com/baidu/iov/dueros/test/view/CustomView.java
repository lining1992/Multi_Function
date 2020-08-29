package com.baidu.iov.dueros.test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.baidu.iov.dueros.test.R;

import static android.icu.lang.UCharacterEnums.ECharacterDirection.LEFT_TO_RIGHT;
import static android.icu.lang.UCharacterEnums.ECharacterDirection.RIGHT_TO_LEFT;

/**
 * @author v_lining05
 * @date 2020/8/26
 */
public class CustomView extends View {

    private String text;
    private Paint paint, paintText, arcPaint, arcPaint2, changePaint;
    private int textSize;
    private int textColor;
    private RectF rectF;
    private RectF rectF2;
    private int progress;
    private int maxSetp;
    private Rect rect;
    private Direction direction = Direction.LEFT_TO_RIGHT;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        textSize = typedArray.getDimensionPixelSize(R.styleable.CustomView_customTextSize, textSize);
        textColor = typedArray.getColor(R.styleable.CustomView_customTextColor, textColor);
        text = typedArray.getString(R.styleable.CustomView_customText);
        typedArray.recycle();

        changePaint = new Paint();
        changePaint.setAntiAlias(true);
        changePaint.setTextSize(textSize);
        changePaint.setColor(textColor);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(Color.BLUE);

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setTextSize(textSize);
        paintText.setColor(textColor);

        arcPaint = new Paint();
        arcPaint.setColor(Color.RED);
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth(10);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        Log.d("debugli", "getWidth==" + getWidth() + "==getHeight==" + getHeight());


        arcPaint2 = new Paint();
        arcPaint2.setColor(Color.BLUE);
        arcPaint2.setAntiAlias(true);
        arcPaint2.setStrokeWidth(10);
        arcPaint2.setStyle(Paint.Style.STROKE);
        arcPaint2.setStrokeCap(Paint.Cap.ROUND);

        rect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        Log.d("debugli", "==width==" + width + "==height==" + height);

        rectF = new RectF(80, height / 2, width - 80, height);

        setMeasuredDimension(width > height ? height : width,
                width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, 100, 100, paint);

        canvas.drawArc(rectF, 135, 270, false, arcPaint);
        if (maxSetp == 0) return;
        float sweepAngle = (float) progress / maxSetp;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, arcPaint2);
        String currentStepStr = progress + "";
        paintText.getTextBounds(currentStepStr, 0, currentStepStr.length(), rect);
        int dx = (int) (((rectF.right + rectF.left) / 2) - (rect.width() / 2));
        Paint.FontMetricsInt fontMetricsInt = paintText.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = (int) (((rectF.top + rectF.bottom) / 2) + dy);
        canvas.drawText(progress + "", dx, baseLine, paintText);

    }

    public void setProgress(int progress) {
        this.progress = progress;
        Log.d("debugli", "progress==" + progress);
        invalidate();
    }

    public void setMaxStep(int maxStep) {
        this.maxSetp = maxStep;
    }

    public enum Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT;
    }

}
