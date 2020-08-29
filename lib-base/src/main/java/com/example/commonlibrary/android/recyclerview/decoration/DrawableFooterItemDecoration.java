package com.example.commonlibrary.android.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author      : leixing
 * Date        : 2017-04-06
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : Drawable分割线
 */

public class DrawableFooterItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDrawable;
    private final Rect mOffset;

    public DrawableFooterItemDecoration(Drawable drawable) {
        this(drawable, new Rect());
    }

    public DrawableFooterItemDecoration(Drawable drawable, Rect offset) {
        super();
        mDrawable = drawable;
        mOffset = offset;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }
        View child = parent.getChildAt(childCount - 1);
        int position = parent.getChildLayoutPosition(child);
        int itemCount = state.getItemCount();
        if (position != itemCount - 1) {
            return;
        }

        Rect rect = new Rect();
        int parentWidth = parent.getWidth();
        int paddingLeft = parent.getPaddingLeft();
        int paddingRight = parent.getPaddingRight();
        int drawableWidth = mDrawable.getIntrinsicWidth();
        int drawableHeight = mDrawable.getIntrinsicHeight();

        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        rect.left = (parentWidth + paddingLeft - paddingRight - drawableWidth) / 2;
        rect.right = (parentWidth + paddingLeft + drawableWidth - paddingRight) / 2;
        rect.top = child.getBottom() + params.bottomMargin + mOffset.top;
        rect.bottom = rect.top + drawableHeight;

        mDrawable.setBounds(rect);
        mDrawable.draw(c);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        int itemCount = state.getItemCount();
        if (position == itemCount - 1) {
            outRect.bottom += mDrawable.getIntrinsicHeight();
        }
    }
}
