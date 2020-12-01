package com.baidu.iov.dueros.test.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baidu.iov.dueros.test.R;

/**
 * @author v_lining05
 * @date 2020/8/29
 */
@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {

    public int mTextColor;
    public int mTextChangeColor;
    public Paint mTextPaint, mTextChangePaint;
    private Direction direction = Direction.LEFT_TO_RIGHT;
    private float mCurrentTextProgress = 0.0f;
    private String mText;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        mTextColor = typedArray.getColor(R.styleable.CustomTextView_customTextViewColor, getCurrentTextColor());
        mTextChangeColor = typedArray.getColor(R.styleable.CustomTextView_customTextViewChangeColor, getCurrentTextColor());
        typedArray.recycle();

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(getTextSize());

        mTextChangePaint = new Paint();
        mTextChangePaint.setAntiAlias(true);
        mTextChangePaint.setColor(Color.RED);
        mTextChangePaint.setTextSize(getTextSize());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int progress = (int) (mCurrentTextProgress * getWidth());
        if (direction == Direction.LEFT_TO_RIGHT) {
            drawText(canvas, mTextChangePaint, 0, progress);
            drawText(canvas, mTextPaint, progress, getWidth());
        } else {
            drawText(canvas, mTextChangePaint, getWidth() - progress, getWidth());
            drawText(canvas, mTextPaint, 0, getWidth() - progress);
        }

    }
     private void drawText(Canvas canvas, Paint paint, int start, int end) {
         canvas.save();
         Rect rect = new Rect(start, 0, end, getHeight());
         canvas.clipRect(rect);
         paint.getTextBounds(mText, 0, mText.length(), rect);
         int dx = getWidth() / 2 - rect.width() / 2;
         Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
         int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
         int baseLine = getHeight() / 2 + dy;
         canvas.drawText(mText, dx, baseLine, paint);
         canvas.restore();
     }

     public void setCurrentTextProgress(float currentTextProgress) {
        this.mCurrentTextProgress = currentTextProgress;
        invalidate();
     }

     public void setDirection(Direction direction) {
        this.direction = direction;
     }

     public CustomTextView setTextChangeColor(int color) {
        this.mTextChangeColor = color;
        return this;
     }

     public CustomTextView setText(String text) {
        this.mText = text;
        return this;
     }

    public enum Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT;
    }
}
