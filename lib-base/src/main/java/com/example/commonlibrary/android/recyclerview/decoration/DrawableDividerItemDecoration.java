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

public class DrawableDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDrawable;

    public DrawableDividerItemDecoration(Drawable drawable) {
        super();
        mDrawable = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int itemCount = state.getItemCount();
        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < itemCount - 1; i++) {
            View child = parent.getChildAt(i);
            rect.top = child.getBottom();
            rect.bottom = rect.top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(rect);
            mDrawable.draw(c);
        }
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
        if (position < itemCount - 1) {
            outRect.bottom += mDrawable.getIntrinsicHeight();
        }
    }
}
