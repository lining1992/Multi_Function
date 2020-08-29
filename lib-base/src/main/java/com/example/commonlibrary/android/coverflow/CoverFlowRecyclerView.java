package com.example.commonlibrary.android.coverflow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 继承RecyclerView重写{@link #getChildDrawingOrder(int, int)}对Item的绘制顺序进行控制
 *
 * @author Chen Xiaoping (562818444@qq.com)
 * @version V1.0
 */

public class CoverFlowRecyclerView extends RecyclerView {
    /**
     * 按下的X轴坐标
     */
    private float mDownX;

    private boolean mTouchEnable = true;


    public CoverFlowRecyclerView(Context context) {
        super(context);
        init();
    }

    public CoverFlowRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoverFlowRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setChildrenDrawingOrderEnabled(true);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setItemAnimator(null);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof CoverFlowLayoutManger)) {
            throw new IllegalArgumentException("The layout manager must be CoverFlowLayoutManger");
        }
        super.setLayoutManager(layout);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverFlowLayout().getCenterPosition()
                - getCoverFlowLayout().getFirstVisiblePosition();
        // 计算正在显示的所有Item的中间位置
        if (center < 0) {
            center = 0;
        } else if (center > childCount) {
            center = childCount;
        }
        int order;
        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;
    }

    /**
     * 获取LayoutManger，并强制转换为CoverFlowLayoutManger
     */
    private CoverFlowLayoutManger getCoverFlowLayout() {
        return ((CoverFlowLayoutManger) getLayoutManager());
    }

    /**
     * 获取被选中的Item位置
     */
    public int getSelectedPosition() {
        return getCoverFlowLayout().getSelectedPosition();
    }

    /**
     * 设置选中监听
     *
     * @param l 监听接口
     */
    public void setOnItemSelectedListener(CoverFlowLayoutManger.ItemSelectListener l) {
        getCoverFlowLayout().setOnSelectedListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mTouchEnable) {
            return true;
        }
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int centerPosition = getCoverFlowLayout().getCenterPosition();
                int itemCount = getCoverFlowLayout().getItemCount();
                boolean allow = (x > mDownX && centerPosition == 0)
                        || (x < mDownX && centerPosition == itemCount - 1);
                getParent().requestDisallowInterceptTouchEvent(!allow);
                break;
            default:
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        return false;
    }

    public void setTouchEnable(boolean enable) {
        mTouchEnable = enable;
    }
}
