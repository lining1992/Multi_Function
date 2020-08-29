package com.example.commonlibrary.android.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author      : leixing
 * Date        : 2017-04-06
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : 线条分割线
 */

public class LineDividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mHeight;
    private int mColor;
    private final Paint mPaint;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mHeaderCount;
    private int mFooterCount;
    private DecorStrategy mDecorStrategy;

    private static final DecorStrategy DEFAULT_DECOR_STRATEGY = new DecorStrategy() {
        @Override
        public boolean hasDecor(int position, int itemCount, int headerCount, int footerCount) {
            return position >= headerCount && position <= itemCount - 1 - footerCount;
        }
    };

    public LineDividerItemDecoration() {
        this(1, 0xffe6e8ea);
    }

    public LineDividerItemDecoration(int height, int color) {
        super();
        mHeight = height;
        mColor = color;

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);

        mPaddingLeft = 0;
        mPaddingRight = 0;
        mHeaderCount = 0;

    }

    public LineDividerItemDecoration decorStrategy(DecorStrategy strategy) {
        mDecorStrategy = strategy;
        return this;
    }

    public LineDividerItemDecoration paddingLeft(int paddingLeft) {
        mPaddingLeft = paddingLeft;
        return this;
    }

    public LineDividerItemDecoration paddingRight(int paddingRight) {
        mPaddingRight = paddingRight;
        return this;
    }

    public LineDividerItemDecoration headerCount(int headerCount) {
        mHeaderCount = headerCount;
        return this;
    }

    public LineDividerItemDecoration footerCount(int footerCount) {
        mFooterCount = footerCount;
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
        int itemCount = state.getItemCount();
        int startPosition = parent.getChildLayoutPosition(parent.getChildAt(0));

        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft() + mPaddingLeft;
        rect.right = parent.getWidth() - parent.getPaddingRight() - mPaddingRight;

        DecorStrategy strategy = mDecorStrategy == null ? DEFAULT_DECOR_STRATEGY : mDecorStrategy;

        for (int i = 0; i < childCount; i++) {
            if (!strategy.hasDecor(i + startPosition, itemCount, mHeaderCount, mFooterCount)) {
                continue;
            }
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
        DecorStrategy strategy = mDecorStrategy == null ? DEFAULT_DECOR_STRATEGY : mDecorStrategy;
        int position = parent.getChildLayoutPosition(view);
        int childCount = parent.getChildCount();
        if (strategy.hasDecor(position, childCount, mHeaderCount, mFooterCount)) {
            outRect.bottom += mHeight;
        }
    }
}
