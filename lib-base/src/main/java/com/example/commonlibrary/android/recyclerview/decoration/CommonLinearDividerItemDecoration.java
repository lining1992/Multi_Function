package com.example.commonlibrary.android.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wangbaozeng on 2018/1/29.
 */

public class CommonLinearDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int mHeight;
    private int mColor;
    private final Paint mPaint;
    private int mPaddingLeft;
    private int mPaddingRight;


    public CommonLinearDividerItemDecoration() {
        this(1, 0xffe6e8ea);
    }

    public CommonLinearDividerItemDecoration(int height, int color) {
        super();
        mHeight = height;
        mColor = color;

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);

        mPaddingLeft = 0;
        mPaddingRight = 0;

    }



    public CommonLinearDividerItemDecoration paddingLeft(int paddingLeft) {
        mPaddingLeft = paddingLeft;
        return this;
    }

    public CommonLinearDividerItemDecoration paddingRight(int paddingRight) {
        mPaddingRight = paddingRight;
        return this;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }

        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft() + mPaddingLeft;
        rect.right = parent.getWidth() - parent.getPaddingRight() - mPaddingRight;


        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child == null) {
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            rect.top = child.getBottom() + params.bottomMargin;
            rect.bottom = rect.top + mHeight;
            c.drawRect(rect, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom += mHeight;
    }
}
