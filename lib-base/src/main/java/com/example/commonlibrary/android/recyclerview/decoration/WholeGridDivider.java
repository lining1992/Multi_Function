package com.example.commonlibrary.android.recyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 整体的GridLayoutManager分割线 使用一个完整的Drawable对象绘制横向或纵向的分割线 而不是每个Item分别绘制分割线 Created by kevin.bai on 16/12/8.
 */

public class WholeGridDivider extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_WIDTH = 1; // PX
    Drawable mHDivider;
    Drawable mVDivider;

    private int mHWidth = DEFAULT_WIDTH;
    private int mVWidth = DEFAULT_WIDTH;

    public WholeGridDivider(Context context, int hDrawableResId, int vDrawableResId) {
        mHDivider = ContextCompat.getDrawable(context, hDrawableResId);
        mVDivider = ContextCompat.getDrawable(context, vDrawableResId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);
        for (int i = 0; i < childCount; i += spanCount) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = parent.getLeft();
            final int right = parent.getRight();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mHWidth;
            mHDivider.setBounds(left, top, right, bottom);
            mHDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        for (int i = 0; i < spanCount; i++) {
            final View child = parent.getChildAt(i);

            if (child == null) {
                continue;

            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = parent.getTop();
            final int bottom = parent.getBottom();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mVWidth;

            mVDivider.setBounds(left, top, right, bottom);
            mVDivider.draw(c);
        }
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return (pos + 1) % spanCount == 0;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                return (pos + 1) % spanCount == 0;
            } else {
                childCount = childCount - childCount % spanCount;
                return pos >= childCount;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            // 如果是最后一行，则不需要绘制底部
            return pos >= childCount;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                return pos >= childCount;
            } else {
                // StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                return (pos + 1) % spanCount == 0;
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
            // 如果是最后一行，则不需要绘制底部
            outRect.set(0, 0, mVWidth, 0);
        } else if (isLastColum(parent, itemPosition, spanCount, childCount)) {
            // 如果是最后一列，则不需要绘制右边
            outRect.set(0, 0, 0, mHWidth);
        } else {
            outRect.set(0, 0, mVWidth, mHWidth);
        }
    }
}
