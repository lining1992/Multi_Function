package com.example.commonlibrary.android.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * 持平滑滑动
 *
 * @author v_liupeng04
 * @date on 2018/12/10
 * @describe 支
 */
public class SmoothScrollGridLayoutManager extends GridLayoutManager {

    private LinearSmoothScroller mSmoothScroller;

    public SmoothScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SmoothScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public SmoothScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        // 返回：滑过1px时经历的时间(ms)。
        ensureSmoothScrollerExist(recyclerView);
        mSmoothScroller.setTargetPosition(position);
        startSmoothScroll(mSmoothScroller);
    }

    private void ensureSmoothScrollerExist(final RecyclerView recyclerView) {
        if (mSmoothScroller != null) {
            return;
        }
        mSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            // 返回：滑过1px时经历的时间(ms)。
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 30f / displayMetrics.densityDpi;
            }
        };
    }
}
