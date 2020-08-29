package com.example.commonlibrary.android.coverflow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.example.base.BuildConfig;
import com.example.commonlibrary.utils.LogUtil;


/**
 * Cover Flow布局类
 * <p>通过重写LayoutManger布局方法{@link #onLayoutChildren(RecyclerView.Recycler, RecyclerView.State)}
 * 对Item进行布局，并对超出屏幕的Item进行回收
 * <p>通过重写LayoutManger中的{@link #scrollHorizontallyBy(int, RecyclerView.Recycler, RecyclerView.State)}
 * 进行水平滚动处理
 *
 * @author Chen Xiaoping (562818444@qq.com)
 * @version V1.0
 */

public class CoverFlowLayoutManger extends RecyclerView.LayoutManager {
    private static final String TAG = CoverFlowLayoutManger.class.getSimpleName();

    /**
     * 最大存储item信息存储数量，
     * 超过设置数量，则动态计算来获取
     */
    private static final int MAX_RECT_COUNT = 100;

    /**
     * 滑动状态
     */
    public static final int STATE_IDLE = 0;
    public static final int STATE_SCROLLING = 1;
    public static final int STATE_ANIMATION = 2;

    /**
     * 滑动总偏移量
     */
    private int mOffsetAll = 0;

    /**
     * Item宽
     */
    private int mDecoratedChildWidth = 0;

    /**
     * Item高
     */
    private int mDecoratedChildHeight = 0;

    /**
     * Item间隔与item宽的比例
     */
    private float mIntervalRatio = 1.0f;

    /**
     * 起始ItemX坐标
     */
    private int mStartX = 0;

    /**
     * 起始Item Y坐标
     */
    private int mStartY = 0;

    /**
     * 保存所有的Item的上下左右的偏移量信息
     */
    private SparseArray<Rect> mAllItemFrames = new SparseArray<>();

    /**
     * 记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
     */
    private SparseBooleanArray mItemAttachedFlags = new SparseBooleanArray();

    /**
     * RecyclerView的Item回收器
     */
    private RecyclerView.Recycler mRecycle;

    /**
     * RecyclerView的状态器
     */
    private RecyclerView.State mState;

    /**
     * 滚动动画
     */
    private ValueAnimator mAnimation;

    /**
     * 正显示在中间的Item
     */
    private int mSelectPosition = 0;

    /**
     * 滑动的方向：左
     */
    private static int SCROLL_LEFT = 1;

    /**
     * 滑动的方向：右
     */
    private static int SCROLL_RIGHT = 2;

    /**
     * 选中监听
     */
    private ItemSelectListener mItemSelectListener;

    /**
     * 是否启动Item灰度值渐变
     */
    private boolean mItemGradualGrey = false;

    /**
     * 是否启动Item半透渐变
     */
    private boolean mItemGradualAlpha = false;

    private float mScaleX = 0.6f;
    private float mScaleY = 0.6f;
    private float mTranslationY;
    private float mTranslationX;
    private float mGrayFrom = 0.9f;
    private float mGrayTo = 0.6f;
    private float mItemSwitchRatio = 0.5f;
    private ScrollStateListener mScrollStateListener;
    private int mScrollState;
    private Animator.AnimatorListener mAnimationListener;
    private int mViewScrollState;
    private boolean mAnimationRunning;

    public CoverFlowLayoutManger() {
        mScrollState = RecyclerView.SCROLL_STATE_IDLE;
        mViewScrollState = RecyclerView.SCROLL_STATE_IDLE;
        mAnimationRunning = false;
    }

    public void setTranslationY(float translationY) {
        mTranslationY = translationY;
    }

    public void setTranslationX(float translationX) {
        mTranslationX = translationX;
    }

    public void setScaleX(float scaleX) {
        mScaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        mScaleY = scaleY;
    }

    public void setItemSwitchRatio(float itemSwitchRatio) {
        mItemSwitchRatio = itemSwitchRatio;
    }

    public void setScrollStateListener(ScrollStateListener listener) {
        mScrollStateListener = listener;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            mOffsetAll = 0;
            return;
        }
        mAllItemFrames.clear();
        mItemAttachedFlags.clear();
        if (mDecoratedChildWidth == 0 || mDecoratedChildHeight == 0) {
            View scrap = recycler.getViewForPosition(0);
            measureChildWithMargins(scrap, 0, 0);
            mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap);
            mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap);
        }

        int horizontalSpace = getHorizontalSpace();
        LogUtil.d(TAG + "===mDecoratedChildWidth===" + mDecoratedChildWidth
                + "===mDecoratedChildHeight===" + mDecoratedChildHeight
                + "===horizontalSpace===" + horizontalSpace);
        mStartX = Math.round((horizontalSpace - mDecoratedChildWidth) * 1.0f / 2);
        mStartY = Math.round((getVerticalSpace() - mDecoratedChildHeight) * 1.0f / 2);
        LogUtil.d(TAG + "===mStartX===" + mScaleX + "===mStartY===" + mScaleY);

        for (int i = 0; i < getItemCount() && i < MAX_RECT_COUNT; i++) {
            Rect frame = mAllItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            float offset = mStartX + getIntervalDistance() * i;
            frame.set(Math.round(offset),
                    mStartY,
                    Math.round(offset + mDecoratedChildWidth),
                    mStartY + mDecoratedChildHeight);
            mAllItemFrames.put(i, frame);
            mItemAttachedFlags.put(i, false);
        }

        detachAndScrapAttachedViews(recycler);
        boolean b = (mRecycle == null || mState == null) && mSelectPosition != 0;
        if (b) {
            mOffsetAll = calculateOffsetForPosition(mSelectPosition);
        }

        layoutItems(recycler, state, SCROLL_RIGHT);

        mRecycle = recycler;
        mState = state;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler,
                                    RecyclerView.State state) {
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.cancel();
            mAnimationRunning = false;
        }
        int travel = dx;
        if (dx + mOffsetAll < 0) {
            travel = -mOffsetAll;
        } else if (dx + mOffsetAll > getMaxOffset()) {
            travel = (int) (getMaxOffset() - mOffsetAll);
        }

        mOffsetAll += travel;
        layoutItems(recycler, state, dx > 0 ? SCROLL_RIGHT : SCROLL_LEFT);
        return travel;
    }

    /**
     * 布局Item
     * <p>注意：1，先清除已经超出屏幕的item
     * <p>     2，再绘制可以显示在屏幕里面的item
     */
    private void layoutItems(RecyclerView.Recycler recycler,
                             RecyclerView.State state, int scrollDirection) {
        if (recycler == null || state == null) {
            return;
        }
        if (state.isPreLayout()) {
            return;
        }
        try {
            Rect displayFrame = new Rect(mOffsetAll,
                    0,
                    mOffsetAll + getHorizontalSpace(),
                    getVerticalSpace());

            int position = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                position = getPosition(child);

                Rect rect = getFrame(position);
                Rect adjustRect = adjust(rect, displayFrame);
                if (!Rect.intersects(displayFrame, adjustRect)) {
                    removeAndRecycleView(child, recycler);
                    mItemAttachedFlags.delete(position);
                } else {
                    layoutItem(child, adjustRect, adjustRect != rect, displayFrame);
                    mItemAttachedFlags.put(position, true);
                }
            }

            int itemCount = state.getItemCount();

            LogUtil.d(TAG + "===itemCount===" + itemCount);
            for (int i = 0; i < itemCount; i++) {
                Rect rect = getFrame(i);
                Rect adjustRect = adjust(rect, displayFrame);
                if (Rect.intersects(displayFrame, adjustRect) && !mItemAttachedFlags.get(i)) {
                    View scrap = recycler.getViewForPosition(i);
                    measureChildWithMargins(scrap, 0, 0);
                    if (scrollDirection == SCROLL_LEFT) {
                        addView(scrap, 0);
                    } else {
                        addView(scrap);
                    }
                    layoutItem(scrap, adjustRect, adjustRect != rect, displayFrame);
                    mItemAttachedFlags.put(i, true);
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                throw e;
            }
        }
    }

    private Rect adjust(Rect rect, Rect displayFrame) {
        int left = rect.left;
        int right = rect.right;
        int width = right - left;
        int halfWidth = width >> 1;
        int frameLeft = displayFrame.left;
        int frameRight = displayFrame.right;
        int halfFrameWidth = (frameRight - frameLeft) >> 1;

        if (left < frameLeft && left >= (frameLeft - halfFrameWidth + halfWidth)) {
            Rect result = new Rect(rect);
            result.left = (frameLeft << 1) - left;
            result.right = result.left + width;
            return result;
        } else if (right > frameRight && right <= frameRight + halfFrameWidth - halfWidth) {
            Rect result = new Rect(rect);
            result.right = (frameRight << 1) - right;
            result.left = result.right - width;
            return result;
        } else {
            return rect;
        }
    }

    /**
     * 布局Item位置
     *
     * @param child 要布局的Item
     * @param frame 位置信息
     */
    private void layoutItem(View child, Rect frame, boolean isAdjust, Rect displayFrame) {
        LogUtil.d(TAG + "===frame.left===" + frame.left + "===frame.right==="
                + frame.right + "===isAdjust===" + isAdjust + "===mOffsetAll===" + mOffsetAll);
        layoutDecorated(child,
                frame.left - mOffsetAll,
                frame.top,
                frame.right - mOffsetAll,
                frame.bottom);
        if (isAdjust) {
            child.setScaleX(mScaleX);
            child.setScaleY(mScaleY);
            child.setTranslationY(mTranslationY);
            if (mItemGradualGrey) {
                greyItem(child, mGrayTo);
            }
        } else {
            float ratio = calcRatio(displayFrame, frame);
            child.setScaleX(calcValue(1, mScaleX, ratio));
            child.setScaleY(calcValue(1, mScaleY, ratio));
            child.setTranslationY(calcValue(0, mTranslationY, ratio));
            if (mItemGradualGrey) {
                greyItem(child, calcValue(mGrayFrom, mGrayTo, ratio));
            }
        }

        if (mItemGradualAlpha) {
            child.setAlpha(computeAlpha(frame.left - mOffsetAll));
        }
    }

    private float calcValue(float from, float to, float ratio) {
        return from * (1 - ratio) + to * ratio;
    }

    private float calcRatio(Rect outerRect, Rect innerRect) {
        int outCenter = (outerRect.right + outerRect.left) >> 1;
        int innerCenter = (innerRect.right + innerRect.left) >> 1;
        int offset = Math.abs(outCenter - innerCenter);

        int outerWidth = (outerRect.right - outerRect.left) >> 1;
        int innerWidth = (innerRect.right - innerRect.left) >> 1;
        int offsetMax = Math.abs(outerWidth - innerWidth);

        return offset * 1.0f / offsetMax;
    }

    /**
     * 动态获取Item的位置信息
     *
     * @param index item位置
     * @return item的Rect信息
     */
    private Rect getFrame(int index) {
        Rect frame = mAllItemFrames.get(index);
        if (frame == null) {
            frame = new Rect();
            float offset = mStartX + getIntervalDistance() * index;
            LogUtil.d(TAG + "===offset===" + offset);
            frame.set(Math.round(offset),
                    mStartY,
                    Math.round(offset + mDecoratedChildWidth),
                    mStartY + mDecoratedChildHeight);
        }

        return frame;
    }

    /**
     * 变化Item的灰度值
     *
     * @param child 需要设置灰度值的Item
     */
    private void greyItem(View child, float value) {
        ColorMatrix cm = new ColorMatrix(new float[]{
                value, 0, 0, 0, 0,
                0, value, 0, 0, 0,
                0, 0, value, 0, 0,
                0, 0, 0, 1, 0,
        });
        Paint greyPaint = new Paint();
        greyPaint.setColorFilter(new ColorMatrixColorFilter(cm));

        child.setLayerType(View.LAYER_TYPE_HARDWARE, greyPaint);
        if (value >= 1) {
            child.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        mScrollState = state;
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                fixOffsetWhenFinishScroll();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
            case RecyclerView.SCROLL_STATE_SETTLING:
                updateScrollState();
                break;
            default:
        }
    }

    @Override
    public void scrollToPosition(int position) {
        if (position < 0 || position > getItemCount() - 1) {
            return;
        }
        mOffsetAll = calculateOffsetForPosition(position);
        int direction = position > mSelectPosition ? SCROLL_RIGHT : SCROLL_LEFT;
        updateSelectPosition(position, SelectTrigger.SCROLL_TO);
        layoutItems(mRecycle, mState, direction);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        updateSelectPosition(position, SelectTrigger.SCROLL_TO);
        int finalOffset = calculateOffsetForPosition(position);
        if (mRecycle == null || mState == null) {
            updateScrollState();
            return;
        }
        startScroll(mOffsetAll, finalOffset, SelectTrigger.SCROLL_TO);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        removeAllViews();
        mRecycle = null;
        mState = null;
        mOffsetAll = 0;
        mItemAttachedFlags.clear();
        mAllItemFrames.clear();
        updateSelectPosition(0, SelectTrigger.DATA);
    }

    /**
     * 获取整个布局的水平空间大小
     */
    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    /**
     * 获取整个布局的垂直空间大小
     */
    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    /**
     * 获取最大偏移量
     */
    private float getMaxOffset() {
        return (getItemCount() - 1) * getIntervalDistance();
    }

    /**
     * 计算Item半透值
     *
     * @param x Item的偏移量
     * @return 缩放系数
     */
    private float computeAlpha(int x) {
        float alpha = 1 - Math.abs(x - mStartX) * 1.0f / Math.abs(mStartX + mDecoratedChildWidth / mIntervalRatio);
        if (alpha < 0.3f) {
            alpha = 0.3f;
        }
        if (alpha > 1) {
            alpha = 1.0f;
        }
        return alpha;
    }

    /**
     * 计算Item所在的位置偏移
     *
     * @param position 要计算Item位置
     */
    private int calculateOffsetForPosition(int position) {
        return Math.round(getIntervalDistance() * position);
    }

    /**
     * 修正停止滚动后，Item滚动到中间位置
     */
    private void fixOffsetWhenFinishScroll() {
        float intervalDistance = getIntervalDistance();
        int selectPosition = (int) (mOffsetAll * 1.0f / intervalDistance);
        float offset = (mOffsetAll % intervalDistance);
        if (selectPosition >= mSelectPosition) {
            if (offset > (intervalDistance * mItemSwitchRatio)) {
                selectPosition++;
            }
        } else {
            if (offset > intervalDistance * (1 - mItemSwitchRatio)) {
                selectPosition++;
            }
        }

        int finalOffsetAll = Math.round(selectPosition * intervalDistance);
        updateSelectPosition(selectPosition, SelectTrigger.USER_TOUCH);
        startScroll(mOffsetAll, finalOffsetAll, SelectTrigger.USER_TOUCH);
    }

    /**
     * 滚动到指定X轴位置
     *
     * @param from X轴方向起始点的偏移量
     * @param to   X轴方向终点的偏移量
     */
    private void startScroll(int from, int to, final SelectTrigger selectTrigger) {
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.cancel();
            mAnimationRunning = false;
        }
        if (from == to) {
            updateScrollState();
            return;
        }
        final int direction = from < to ? SCROLL_RIGHT : SCROLL_LEFT;
        mAnimation = ValueAnimator.ofFloat(from, to);
        mAnimation.setDuration(500);
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetAll = Math.round((float) animation.getAnimatedValue());
                layoutItems(mRecycle, mState, direction);
            }
        });
        mAnimationListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationRunning = true;
                updateScrollState();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationRunning = false;
                notifySelectPositionUpdate(selectTrigger);
                updateScrollState();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimationRunning = false;
                mAnimation.removeListener(mAnimationListener);
                updateScrollState();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        mAnimation.addListener(mAnimationListener);
        mAnimation.start();
    }

    /**
     * 获取Item间隔
     */
    private float getIntervalDistance() {
        return mDecoratedChildWidth * mIntervalRatio;
    }

    /**
     * 获取第一个可见的Item位置
     * <p>Note:该Item为绘制在可见区域的第一个Item，有可能被第二个Item遮挡
     */
    public int getFirstVisiblePosition() {
        Rect displayFrame = new Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace());
        int cur = getCenterPosition();
        for (int i = cur - 1; i >= 0; i--) {
            Rect rect = getFrame(i);
            if (!Rect.intersects(displayFrame, rect)) {
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * 获取最后一个可见的Item位置
     * <p>Note:该Item为绘制在可见区域的最后一个Item，有可能被倒数第二个Item遮挡
     */
    public int getLastVisiblePosition() {
        Rect displayFrame = new Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace());
        int cur = getCenterPosition();
        for (int i = cur + 1; i < getItemCount(); i++) {
            Rect rect = getFrame(i);
            if (!Rect.intersects(displayFrame, rect)) {
                return i - 1;
            }
        }
        return cur;
    }

    /**
     * 获取可见范围内最大的显示Item个数
     */
    public int getMaxVisibleCount() {
        int oneSide = (int) ((getHorizontalSpace() - mStartX) / (getIntervalDistance()));
        return oneSide * 2 + 1;
    }

    /**
     * 获取中间位置
     * <p>Note:该方法主要用于{@link CoverFlowRecyclerView#getChildDrawingOrder(int, int)}判断中间位置
     * <p>如果需要获取被选中的Item位置，调用{@link #getSelectedPosition()}
     */
    public int getCenterPosition() {
        int pos = (int) (mOffsetAll / getIntervalDistance());
        int more = (int) (mOffsetAll % getIntervalDistance());
        if (more > getIntervalDistance() * 0.5f) {
            pos++;
        }
        return pos;
    }

    /**
     * 设置选中监听
     *
     * @param l 监听接口
     */
    public void setOnSelectedListener(ItemSelectListener l) {
        mItemSelectListener = l;
    }

    /**
     * 获取被选中Item位置
     */
    public int getSelectedPosition() {
        return mSelectPosition;
    }

    private void updateSelectPosition(int position, SelectTrigger selectTrigger) {
        if (mSelectPosition == position) {
            return;
        }
        mSelectPosition = position;
        notifySelectPositionUpdate(selectTrigger);
    }

    /**
     * 计算当前选中位置，并回调
     */
    private void notifySelectPositionUpdate(SelectTrigger selectTrigger) {
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemSelected(mSelectPosition, selectTrigger);
        }
    }

    private void updateScrollState() {
        int state = mAnimationRunning
                ? STATE_ANIMATION :
                mScrollState == RecyclerView.SCROLL_STATE_IDLE
                        ? STATE_IDLE :
                        STATE_SCROLLING;

        if (mViewScrollState != state) {
            mViewScrollState = state;
            if (mScrollStateListener != null) {
                mScrollStateListener.onScrollStateUpdate(mViewScrollState);
            }
        }
    }

    public int getScrollState() {
        return mViewScrollState;
    }

    /**
     * 选中监听接口
     */
    public interface ItemSelectListener {
        /**
         * 监听选中回调
         *
         * @param position 显示在中间的Item的位置
         */
        void onItemSelected(int position, SelectTrigger selectTrigger);
    }

    public enum SelectTrigger {

        /**
         * 用户滑动卡片选中Item
         */
        USER_TOUCH,

        /**
         * 程序调用{@link RecyclerView#scrollToPosition(int)}
         * 或者 {@link RecyclerView#smoothScrollToPosition(int)}后选中Item
         */
        SCROLL_TO,

        /**
         * 数据更新导致选中位置变化
         */
        DATA,
    }
}