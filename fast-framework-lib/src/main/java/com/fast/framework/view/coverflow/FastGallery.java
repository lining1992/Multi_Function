/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.coverflow;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import com.fast.framework.R;
import com.fast.framework.support.L;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by lishicong on 2017/6/6.
 */

public class FastGallery extends BaseGallery implements ViewTreeObserver.OnPreDrawListener {
    public static final int DEFAULT_MAX_CACHE_SIZE = 32;

    /**
     * Graphics Camera used for generating transformation matrices;
     */
    private final Camera mCamera = new Camera();
    /**
     * 容器中视图的相对间距值. 如果小于1将重叠, 如果大于1视图将在它们之间有间距（支持layout设定）
     */
    private float mSpacing = 0.5f;

    /**
     * Index of view in center of screen, which is most in foreground
     */
    private int mReverseOrderIndex = -1;

    private int mLastCenterItemIndex = -1;

    /**
     * Distance from center as fraction of half of widget size where covers start to rotate into center
     * 1 means rotation starts on edge of widget, 0 means only center rotated
     */
    private float mRotationThreshold = 0.3f;
    /**
     * 从中心的距离为一半的图片大小，其中封面开始放大，1缩放开始边缘的图片，0只中心缩放
     */
    private float mScalingThreshold = 0.3f;

    /**
     * Distance from center as fraction of half of widget size,
     * where covers start enlarge their spacing to allow for smooth passing each other without jumping over each other
     * 1 means edge of widget, 0 means only center
     */
    private float mAdjustPositionThreshold = 0.5f;  //  lishicong

    /**
     * By enlarging this value, you can enlarge spacing in center of widget done by position adjustment
     */
    private float mAdjustPositionMultiplier = 1.0f;

    /**
     * Absolute value of rotation angle of cover at edge of widget in degrees
     */
    private float mMaxRotationAngle = 70.0f;
    /**
     * Scale factor of item in center
     */
    private float mMaxScaleFactor = 1.2f;

    /**
     * Radius of circle path which covers follow. Range of screen is -1 to 1, minimal radius is therefore 1
     */
    private float mRadius = 2f;

    /**
     * Radius of circle path which covers follow in coordinate space of matrix transformation. Used to scale offset
     */
    private float mRadiusInMatrixSpace = 1000f;

    /**
     * Size of reflection as a fraction of original image (0-1)
     */
    private float mReflectionHeight = 0.5f;

    /**
     * Gap between reflection and original image in pixels
     */
    private int mReflectionGap = 2;

    /**
     * 图像倒影的透明度
     */
    private int mReflectionOpacity = 0x30;

    /**
     * Widget size on which was tuning of parameters done. This value is used to scale parameters on when widgets has
     * different size
     */
    private int mTuningWidgetSize = 1280;

    /**
     * How long will alignment animation take
     */
    private int mAlignTime = 350;

    /**
     * 图片倒影的背景色
     */
    private int mReflectionBackgroundColor = Color.TRANSPARENT;

    /**
     * A list of cached (re-usable) cover frames
     */
    protected final LinkedList<WeakReference<CoverFrame>> mRecycledCoverFrames = new LinkedList<>();

    /**
     * A listener for center item position
     */
    private OnScrollPositionListener mOnScrollPositionListener;

    private int mLastTouchState = -1;
    private int mlastCenterItemPosition = -1;

    public interface OnScrollPositionListener {
        public void onScrolledToPosition(int position);

        public void onScrolling();
    }

    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;

    private int mCenterItemOffset;
    private final Scroller mAlignScroller = new Scroller(getContext(), new DecelerateInterpolator());

    private final MyCache mCachedFrames;

    private int mCoverWidth = 160;
    private int mCoverHeight = 240;

    private final Matrix mMatrix = new Matrix();
    private final Matrix mTemp = new Matrix();
    private final Matrix mTempHit = new Matrix();
    private final Rect mTempRect = new Rect();
    private final RectF mTouchRect = new RectF();

    private View mMotionTarget;
    private float mTargetLeft;
    private float mTargetTop;

    // reflection
    private final Matrix mReflectionMatrix = new Matrix();
    private final Paint mPaint = new Paint();
    private final Paint mReflectionPaint = new Paint();
    private final PorterDuffXfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private final Canvas mReflectionCanvas = new Canvas();

    private int mScrollToPositionOnNextInvalidate = -1;

    private boolean mInvalidated = false;

    private class MyCache extends LruCache<Integer, CoverFrame> {

        public MyCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected void entryRemoved(boolean evicted, Integer key, CoverFrame oldValue, CoverFrame newValue) {
            if (evicted) {
                if (oldValue.getChildCount() == 1) {
                    mCachedItemViews.addLast(new WeakReference<>(oldValue.getChildAt(0)));
                    recycleCoverFrame(oldValue);  //  removes children, must be after caching children
                }
            }
        }

    }

    public FastGallery(Context context, AttributeSet attrs, int defStyle, int cacheSize) {
        super(context, attrs, defStyle);

        if (cacheSize <= 0) {
            cacheSize = DEFAULT_MAX_CACHE_SIZE;
        }
        mCachedFrames = new MyCache(cacheSize);

        setChildrenDrawingOrderEnabled(true);
        setChildrenDrawingCacheEnabled(true);
        setChildrenDrawnWithCacheEnabled(true);

        mReflectionMatrix.preScale(1.0f, -1.0f);

        // init params from xml
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FeatureCoverFlow, defStyle, 0);

            mCoverWidth = a.getDimensionPixelSize(R.styleable.FeatureCoverFlow_coverWidth, mCoverWidth);
            if (mCoverWidth % 2 == 1) {
                mCoverWidth--;
            }
            mCoverHeight = a.getDimensionPixelSize(R.styleable.FeatureCoverFlow_coverHeight, mCoverHeight);
            mSpacing = a.getFloat(R.styleable.FeatureCoverFlow_spacing, mSpacing);
            mRotationThreshold = a.getFloat(R.styleable.FeatureCoverFlow_rotationThreshold, mRotationThreshold);
            mScalingThreshold = a.getFloat(R.styleable.FeatureCoverFlow_scalingThreshold, mScalingThreshold);
            mAdjustPositionThreshold = a.getFloat(R.styleable.FeatureCoverFlow_adjustPositionThreshold,
                                                  mAdjustPositionThreshold);
            mAdjustPositionMultiplier = a.getFloat(R.styleable.FeatureCoverFlow_adjustPositionMultiplier,
                                                   mAdjustPositionMultiplier);
            mMaxRotationAngle = a.getFloat(R.styleable.FeatureCoverFlow_maxRotationAngle, mMaxRotationAngle);
            mMaxScaleFactor = a.getFloat(R.styleable.FeatureCoverFlow_maxScaleFactor, mMaxScaleFactor);
            mRadius = a.getFloat(R.styleable.FeatureCoverFlow_circlePathRadius, mRadius);
            mRadiusInMatrixSpace = a.getFloat(R.styleable.FeatureCoverFlow_circlePathRadiusInMatrixSpace,
                                              mRadiusInMatrixSpace);
            mReflectionHeight = a.getFloat(R.styleable.FeatureCoverFlow_reflectionHeight, mReflectionHeight);
            mReflectionGap = a.getDimensionPixelSize(R.styleable.FeatureCoverFlow_reflectionGap, mReflectionGap);
            mReflectionOpacity = a.getInteger(R.styleable.FeatureCoverFlow_reflectionOpacity, mReflectionOpacity);
            mTuningWidgetSize = a.getDimensionPixelSize(R.styleable.FeatureCoverFlow_tunningWidgetSize,
                                                        mTuningWidgetSize);
            mAlignTime = a.getInteger(R.styleable.FeatureCoverFlow_alignAnimationTime, mAlignTime);
            mPaddingTop = a.getDimensionPixelSize(R.styleable.FeatureCoverFlow_verticalPaddingTop, mPaddingTop);
            mPaddingBottom = a.getDimensionPixelSize(R.styleable.FeatureCoverFlow_verticalPaddingBottom,
                                                     mPaddingBottom);
            mReflectionBackgroundColor = a.getColor(R.styleable.FeatureCoverFlow_reflectionBackroundColor,
                                                    Color.TRANSPARENT);

            a.recycle();
        }
    }

    public FastGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastGallery(Context context) {
        this(context, null);
    }

    public FastGallery(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, DEFAULT_MAX_CACHE_SIZE);
    }

    private class CoverFrame extends FrameLayout {
        private Bitmap mReflectionCache;
        private boolean mReflectionCacheInvalid = true;

        public CoverFrame(Context context, View cover) {
            super(context);
            setCover(cover);
        }

        public void setCover(View cover) {
            if (cover.getLayoutParams() != null) {
                setLayoutParams(cover.getLayoutParams());
            }

            final LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.leftMargin = 1;
            lp.topMargin = 1;
            lp.rightMargin = 1;
            lp.bottomMargin = 1;

            if (cover.getParent() != null && cover.getParent() instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) cover.getParent();
                parent.removeView(cover);
            }

            // register observer to catch cover redraws
            cover.getViewTreeObserver().addOnPreDrawListener(FastGallery.this);

            addView(cover, lp);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            mReflectionCacheInvalid = true;
        }

        @Override
        public Bitmap getDrawingCache(boolean autoScale) {
            final Bitmap b = super.getDrawingCache(autoScale);

            if (mReflectionCacheInvalid) {
                if ((mTouchState != TOUCH_STATE_FLING && mTouchState != TOUCH_STATE_ALIGN)
                        || mReflectionCache == null) {
                    try {
                        mReflectionCache = createReflectionBitmap(b);
                        mReflectionCacheInvalid = false;
                    } catch (NullPointerException e) {
                        Log.e(VIEW_LOG_TAG, "Null pointer in createReflectionBitmap. Bitmap b=" + b, e);
                    }
                }
            }
            return b;
        }

        public void recycle() {
            if (mReflectionCache != null) {
                mReflectionCache.recycle();
                mReflectionCache = null;
            }

            mReflectionCacheInvalid = true;
            removeAllViewsInLayout();
        }

    }

    /**
     * 图片偏移中心的距离
     *
     * @return
     */
    private float getWidgetSizeMultiplier() {
        return ((float) mTuningWidgetSize) / ((float) getWidth());
    }

    /**
     * Adds a view as a child view and takes care of measuring it
     *
     * @param child      The view to add
     * @param layoutMode Either LAYOUT_MODE_LEFT or LAYOUT_MODE_RIGHT
     *
     * @return child which was actually added to container, subclasses can override to introduce frame views
     */
    protected View addAndMeasureChildHorizontal(View child, int layoutMode) {
        final int index = layoutMode == LAYOUT_MODE_TO_BEFORE ? 0 : -1;
        final LoopLayoutParams lp = new LoopLayoutParams(mCoverWidth, mCoverHeight);

        if (child != null && child instanceof CoverFrame) {
            addViewInLayout(child, index, lp, true);
            measureChild(child);
            return child;
        }

        CoverFrame frame = getRecycledCoverFrame();
        if (frame == null) {
            frame = new CoverFrame(getContext(), child);
        } else {
            frame.setCover(child);
        }

        // to enable drawing cache
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            frame.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        frame.setDrawingCacheEnabled(true);

        addViewInLayout(frame, index, lp, true);
        measureChild(frame);
        return frame;
    }

    @Override
    protected int layoutChildHorizontal(View v, int left, LoopLayoutParams lp) {
        int l;
        int t;
        int r;
        int b;
        l = left;
        r = l + v.getMeasuredWidth();
        final int x = ((getHeight() - mPaddingTop - mPaddingBottom) - v.getMeasuredHeight()) / 2
                + mPaddingTop;  //  - (int)((lp.actualHeight*mReflectionHeight)/2)
        t = x;
        b = t + v.getMeasuredHeight();
        v.layout(l, t, r, b);
        return l + (int) (v.getMeasuredWidth() * mSpacing);
    }

    /**
     * Layout children from right to left
     */
    protected int layoutChildHorizontalToBefore(View v, int right, LoopLayoutParams lp) {
        int left = right - v.getMeasuredWidth();

        left = layoutChildHorizontal(v, left, lp);

        return left;
    }

    /**
     * 获取中心点
     *
     * @param v
     *
     * @return
     */
    private int getChildsCenter(View v) {
        final int w = v.getRight() - v.getLeft();
        return v.getLeft() + w / 2;
    }

    private int getChildsCenter(int i) {
        return getChildsCenter(getChildAt(i));
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        final int screenCenter = getWidth() / 2 + getScrollX();
        final int myCenter = getChildsCenter(i);
        final int d = myCenter - screenCenter;

        final View v = getChildAt(i);
        final int sz = (int) (mSpacing * v.getWidth() / 2f);

        if (mReverseOrderIndex == -1 && (Math.abs(d) < sz || d >= 0)) {
            mReverseOrderIndex = i;
            mCenterItemOffset = d;
            mLastCenterItemIndex = i;
            return childCount - 1;
        }

        if (mReverseOrderIndex == -1) {
            return i;
        } else {
            if (i == childCount - 1) {
                final int x = mReverseOrderIndex;
                mReverseOrderIndex = -1;
                return x;
            }
            return childCount - 1 - (i - mReverseOrderIndex);
        }
    }

    @Override
    protected void refillInternal(int lastItemPos, int firstItemPos) {
        super.refillInternal(lastItemPos, firstItemPos);

        final int c = getChildCount();
        for (int i = 0; i < c; i++) {
            getChildDrawingOrder(c, i);  // go through children to fill center item offset
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }

        mInvalidated = false;  // last invalidate which marked redrawInProgress, caused this dispatchDraw. Clear flag to
        //  prevent creating loop

        mReverseOrderIndex = -1;

        canvas.getClipBounds(mTempRect);
        mTempRect.top = 0;
        mTempRect.bottom = getHeight();
        canvas.clipRect(mTempRect);

        super.dispatchDraw(canvas);

        if (mScrollToPositionOnNextInvalidate != -1 && mAdapter != null && mAdapter.getCount() > 0) {
            final int lastCenterItemPosition = (mFirstItemPosition + mLastCenterItemIndex) % mAdapter.getCount();
            final int di = lastCenterItemPosition - mScrollToPositionOnNextInvalidate;
            mScrollToPositionOnNextInvalidate = -1;
            if (di != 0) {
                final int dst = (int) (di * mCoverWidth * mSpacing) - mCenterItemOffset;
                scrollBy(-dst, 0);
                postInvalidate();
                return;
            }
        }

        if (mTouchState == TOUCH_STATE_RESTING) {

            final int lastCenterItemPosition = (mFirstItemPosition + mLastCenterItemIndex) % mAdapter.getCount();
            if (mLastTouchState != TOUCH_STATE_RESTING || mlastCenterItemPosition != lastCenterItemPosition) {
                mLastTouchState = TOUCH_STATE_RESTING;
                mlastCenterItemPosition = lastCenterItemPosition;
                if (mOnScrollPositionListener != null) {
                    mOnScrollPositionListener.onScrolledToPosition(lastCenterItemPosition);
                }
            }
        }

        if (mTouchState == TOUCH_STATE_SCROLLING && mLastTouchState != TOUCH_STATE_SCROLLING) {
            mLastTouchState = TOUCH_STATE_SCROLLING;
            if (mOnScrollPositionListener != null) {
                mOnScrollPositionListener.onScrolling();
            }
        }
        if (mTouchState == TOUCH_STATE_FLING && mLastTouchState != TOUCH_STATE_FLING) {
            mLastTouchState = TOUCH_STATE_FLING;
            if (mOnScrollPositionListener != null) {
                mOnScrollPositionListener.onScrolling();
            }
        }

        // make sure we never stay unaligned after last draw in resting state
        if (mTouchState == TOUCH_STATE_RESTING && mCenterItemOffset != 0) {
            scrollBy(mCenterItemOffset, 0);
            postInvalidate();
        }

        try {
            View v = getChildAt(mLastCenterItemIndex);
            if (v != null) {
                v.requestFocus(FOCUS_FORWARD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void fillFirstTime(final int lastItemPos, final int firstItemPos) {
        final int leftScreenEdge = 0;
        final int rightScreenEdge = leftScreenEdge + getWidth();

        int right;
        int left;
        View child;

        boolean isRepeatingNow = false;

        // scrolling is enabled until we find out we don't have enough items
        isSrollingDisabled = false;

        mLastItemPosition = lastItemPos;
        mFirstItemPosition = firstItemPos;
        mLeftChildEdge = (int) (-mCoverWidth * mSpacing);
        right = 0;
        left = mLeftChildEdge;

        while (right < rightScreenEdge) {
            mLastItemPosition++;

            if (isRepeatingNow && mLastItemPosition >= firstItemPos) {
                return;
            }

            if (mLastItemPosition >= mAdapter.getCount()) {
                if (firstItemPos == 0) {
                    mLastItemPosition = 0;
                } else {
                    if (firstItemPos > 0) {
                        mLastItemPosition = 0;
                        isRepeatingNow = true;
                    }

                }
            }

            if (mLastItemPosition >= mAdapter.getCount()) {
                Log.wtf("EndlessLoop", "mLastItemPosition > mAdapter.getCount()");
                return;
            }

            child = mAdapter.getView(mLastItemPosition, getCachedView(), this);
            child = addAndMeasureChildHorizontal(child, LAYOUT_MODE_AFTER);
            left = layoutChildHorizontal(child, left, (LoopLayoutParams) child.getLayoutParams());
            right = child.getRight();

            // if selected view is going to screen, set selected state on him
            if (mLastItemPosition == mSelectedPosition) {
                child.setSelected(true);
            }

        }

        if (mScrollPositionIfEndless > 0) {
            final int p = mScrollPositionIfEndless;
            mScrollPositionIfEndless = -1;
            removeAllViewsInLayout();
            refillOnChange(p);
        }
    }

    /**
     * Checks and refills empty area on the right
     */
    @Override
    protected void refillRight() {
        //  this branch
        if (getChildCount() == 0) {
            return;
        }

        final int leftScreenEdge = getScrollX();
        final int rightScreenEdge = leftScreenEdge + getWidth();

        View child = getChildAt(getChildCount() - 1);
        int currLayoutLeft = child.getLeft() + (int) (child.getWidth() * mSpacing);
        while (currLayoutLeft < rightScreenEdge) {
            mLastItemPosition++;
            if (mLastItemPosition >= mAdapter.getCount()) {
                mLastItemPosition = 0;
            }

            child = getViewAtPosition(mLastItemPosition);
            child = addAndMeasureChildHorizontal(child, LAYOUT_MODE_AFTER);
            currLayoutLeft = layoutChildHorizontal(child, currLayoutLeft, (LoopLayoutParams) child.getLayoutParams());

            // if selected view is going to screen, set selected state on him
            if (mLastItemPosition == mSelectedPosition) {
                child.setSelected(true);
            }
        }
    }

    private boolean containsView(View v) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == v) {
                return true;
            }
        }
        return false;
    }

    private View getViewAtPosition(int position) {
        View v = mCachedFrames.remove(position);
        if (v == null) {
            v = mAdapter.getView(position, getCachedView(), this);
            return v;
        }

        if (!containsView(v)) {
            return v;
        } else {
            v = mAdapter.getView(position, getCachedView(), this);
            return v;
        }
    }

    /**
     * Checks and refills empty area on the left
     */
    @Override
    protected void refillLeft() {
        //  this branch
        if (getChildCount() == 0) {
            return;
        }

        final int leftScreenEdge = getScrollX();

        View child = getChildAt(0);
        int currLayoutRight = child.getRight() - (int) (child.getWidth() * mSpacing);
        while (currLayoutRight > leftScreenEdge) {
            mFirstItemPosition--;
            if (mFirstItemPosition < 0) {
                mFirstItemPosition = mAdapter.getCount() - 1;
            }

            child = getViewAtPosition(mFirstItemPosition);
            if (child == getChildAt(getChildCount() - 1)) {
                removeViewInLayout(child);
            }
            child = addAndMeasureChildHorizontal(child, LAYOUT_MODE_TO_BEFORE);
            currLayoutRight = layoutChildHorizontalToBefore(child, currLayoutRight,
                                                            (LoopLayoutParams) child.getLayoutParams());

            // update left edge of children in container
            mLeftChildEdge = child.getLeft();

            // if selected view is going to screen, set selected state on him
            if (mFirstItemPosition == mSelectedPosition) {
                child.setSelected(true);
            }
        }
    }

    /**
     * Removes view that are outside of the visible part of the list. Will not
     * remove all views.
     */
    @Override
    protected void removeNonVisibleViews() {
        if (getChildCount() == 0) {
            return;
        }

        final int leftScreenEdge = getScrollX();
        final int rightScreenEdge = leftScreenEdge + getWidth();

        //  check if we should remove any views in the left
        View firstChild = getChildAt(0);
        final int leftedge = firstChild.getLeft();
        if (leftedge != mLeftChildEdge) {
            Log.e("feature component",
                  "firstChild.getLeft() != mLeftChildEdge, leftedge:" + leftedge + " ftChildEdge:" + mLeftChildEdge);
            View v = getChildAt(0);
            removeAllViewsInLayout();
            addAndMeasureChildHorizontal(v, LAYOUT_MODE_TO_BEFORE);
            layoutChildHorizontal(v, mLeftChildEdge, (LoopLayoutParams) v.getLayoutParams());
            return;
        }
        while (firstChild != null && firstChild.getRight() < leftScreenEdge) {
            // if selected view is going off screen, remove selected state
            firstChild.setSelected(false);

            //  remove view
            removeViewInLayout(firstChild);

            mCachedFrames.put(mFirstItemPosition, (CoverFrame) firstChild);

            mFirstItemPosition++;
            if (mFirstItemPosition >= mAdapter.getCount()) {
                mFirstItemPosition = 0;
            }

            //  update left item position
            mLeftChildEdge = getChildAt(0).getLeft();

            //  Continue to check the next child only if we have more than
            //  one child left
            if (getChildCount() > 1) {
                firstChild = getChildAt(0);
            } else {
                firstChild = null;
            }
        }

        //  check if we should remove any views in the right
        View lastChild = getChildAt(getChildCount() - 1);
        while (lastChild != null && lastChild.getLeft() > rightScreenEdge) {
            // if selected view is going off screen, remove selected state
            lastChild.setSelected(false);

            //  remove the right view
            removeViewInLayout(lastChild);

            mCachedFrames.put(mLastItemPosition, (CoverFrame) lastChild);

            mLastItemPosition--;
            if (mLastItemPosition < 0) {
                mLastItemPosition = mAdapter.getCount() - 1;
            }

            //  Continue to check the next child only if we have more than
            //  one child left
            if (getChildCount() > 1) {
                lastChild = getChildAt(getChildCount() - 1);
            } else {
                lastChild = null;
            }
        }
    }

    /**
     * 绘制图片
     *
     * @param canvas
     * @param child
     * @param drawingTime
     *
     * @return
     */
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        canvas.save();

        // set matrix to child's transformation
        setChildTransformation(child, mMatrix);

        // Generate child bitmap
        Bitmap bitmap = child.getDrawingCache();

        // initialize canvas state. Child 0,0 coordinates will match canvas 0,0
        canvas.translate(child.getLeft(), child.getTop());
        // set child transformation on canvas
        canvas.concat(mMatrix);

        final Bitmap rfCache = ((CoverFrame) child).mReflectionCache;

        if (mReflectionBackgroundColor != Color.TRANSPARENT) {
            //  画图片倒影的背景色
            final int top = bitmap.getHeight() + mReflectionGap - 2;
            final float frame = 1.0f;
            mReflectionPaint.setColor(mReflectionBackgroundColor);
            canvas.drawRect(frame, top + frame, rfCache.getWidth() - frame, top + rfCache.getHeight() - frame,
                            mReflectionPaint);
        }

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);

        //  画图片
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);

        //  画图片倒影
        //  canvas.drawBitmap(rfCache, 0.0f, bitmap.getHeight() - 2 + mReflectionGap, mPaint);

        canvas.restore();
        return false;
    }

    private Bitmap createReflectionBitmap(Bitmap original) {
        final int w = original.getWidth();
        final int h = original.getHeight();
        final int rh = (int) (h * mReflectionHeight);
        final int gradientColor = Color.argb(mReflectionOpacity, 0xff, 0xff, 0xff);

        final Bitmap reflection = Bitmap.createBitmap(original, 0, rh, w, rh, mReflectionMatrix, false);

        final LinearGradient shader = new LinearGradient(0, 0, 0, reflection.getHeight(), gradientColor, 0x00ffffff,
                                                         Shader.TileMode.CLAMP);
        mPaint.reset();
        mPaint.setShader(shader);
        mPaint.setXfermode(mXfermode);

        mReflectionCanvas.setBitmap(reflection);
        mReflectionCanvas.drawRect(0, 0, reflection.getWidth(), reflection.getHeight(), mPaint);

        return reflection;
    }

    /**
     * Fill outRect with transformed child hit rectangle. Rectangle is not moved to its position on screen, neither
     * getSroolX is accounted for
     *
     * @param child
     * @param outRect
     */
    protected void transformChildHitRectangle(View child, RectF outRect) {
        outRect.left = 0;
        outRect.top = 0;
        outRect.right = child.getWidth();
        outRect.bottom = child.getHeight();

        setChildTransformation(child, mTempHit);
        mTempHit.mapRect(outRect);
    }

    /**
     * 设置每张图片的变化
     *
     * @param child
     * @param m
     */
    private void setChildTransformation(View child, Matrix m) {
        m.reset();

        //  addChildRotation(child, m);
        addChildScale(child, m);
        addChildCircularPathZOffset(child, m);
        addChildAdjustPosition(child, m);

        // set coordinate system origin to center of child
        m.preTranslate(-child.getWidth() / 2f, -child.getHeight() / 2f);
        // move back
        m.postTranslate(child.getWidth() / 2f, child.getHeight() / 2f);

    }

    /**
     * 设置图片旋转
     *
     * @param v
     * @param m
     */
    private void addChildRotation(View v, Matrix m) {
        mCamera.save();

        final int c = getChildsCenter(v);
        mCamera.rotateY(getRotationAngle(c) - getAngleOnCircle(c));

        mCamera.getMatrix(mTemp);
        m.postConcat(mTemp);

        mCamera.restore();
    }

    private float getRotationAngle(int childCenter) {
        return -mMaxRotationAngle * getClampedRelativePosition(getRelativePosition(childCenter),
                                                               mRotationThreshold * getWidgetSizeMultiplier());
    }

    private float getAngleOnCircle(int childCenter) {
        float x = getRelativePosition(childCenter) / mRadius;
        if (x < -1.0f) {
            x = -1.0f;
        }
        if (x > 1.0f) {
            x = 1.0f;
        }

        return (float) (Math.acos(x) / Math.PI * 180.0f - 90.0f);
    }

    /**
     * 添加中间图片放大效果
     *
     * @param v
     * @param m
     */
    private void addChildScale(View v, Matrix m) {
        final float f = getScaleFactor(getChildsCenter(v));
        m.postScale(f, f);
    }

    private void addChildCircularPathZOffset(View child, Matrix m) {
        mCamera.save();

        final float v = getOffsetOnCircle(getChildsCenter(child));
        final float z = mRadiusInMatrixSpace * v;

        mCamera.translate(0.0f, 0.0f, z);

        mCamera.getMatrix(mTemp);
        m.postConcat(mTemp);

        mCamera.restore();
    }

    /**
     * 调整图片位置的
     *
     * @param child
     * @param m
     */
    private void addChildAdjustPosition(View child, Matrix m) {
        final int c = getChildsCenter(child);
        final float crp = getClampedRelativePosition(getRelativePosition(c),
                                                     mAdjustPositionThreshold * getWidgetSizeMultiplier());
        final float d = mCoverWidth * mAdjustPositionMultiplier * mSpacing * crp * getSpacingMultiplierOnCirlce(c);

        m.postTranslate(d, 0f);
    }

    /**
     * Calculates relative position on screen in range -1 to 1, widgets out of screen can have values ove 1 or -1
     *
     * @param pixexPos Absolute position in pixels including scroll offset
     *
     * @return relative position
     */
    private float getRelativePosition(int pixexPos) {
        final int half = getWidth() / 2;
        final int centerPos = getScrollX() + half;

        return (pixexPos - centerPos) / ((float) half);
    }

    /**
     * Clamps relative position by threshold, and produces values in range -1 to 1 directly usable for transformation
     * computation
     *
     * @param position  value int range -1 to 1
     * @param threshold always positive value of threshold distance from center in range 0-1
     *
     * @return
     */
    private float getClampedRelativePosition(float position, float threshold) {
        if (position < 0) {
            if (position < -threshold) {
                return -1f;
            } else {
                return position / threshold;
            }
        } else {
            if (position > threshold) {
                return 1;
            } else {
                return position / threshold;
            }
        }
    }

    private float getScaleFactor(int childCenter) {
        float factor = 1 + (mMaxScaleFactor - 1) * (1 - Math.abs(
                getClampedRelativePosition(getRelativePosition(childCenter),
                                           mScalingThreshold * getWidgetSizeMultiplier())));
        return factor;
    }

    /**
     * Compute offset following path on circle
     *
     * @param childCenter
     *
     * @return offset from position on unitary circle
     */
    private float getOffsetOnCircle(int childCenter) {
        float x = getRelativePosition(childCenter) / mRadius;
        if (x < -1.0f) {
            x = -1.0f;
        }
        if (x > 1.0f) {
            x = 1.0f;
        }

        return (float) (1 - Math.sin(Math.acos(x)));
    }

    private float getSpacingMultiplierOnCirlce(int childCenter) {
        float x = getRelativePosition(childCenter) / mRadius;
        return (float) Math.sin(Math.acos(x));
    }

    @Override
    protected void handleClick(Point p) {
        final int c = getChildCount();
        View v;
        final RectF r = new RectF();
        final int[] childOrder = new int[c];

        for (int i = 0; i < c; i++) {
            childOrder[i] = getChildDrawingOrder(c, i);
        }

        for (int i = c - 1; i >= 0; i--) {
            v = getChildAt(childOrder[i]);  // we need reverse drawing order. Check children drawn last first
            getScrolledTransformedChildRectangle(v, r);
            if (r.contains(p.x, p.y)) {

                int position = mFirstItemPosition + childOrder[i];
                if (position >= mAdapter.getCount()) {
                    position = position - mAdapter.getCount();
                }

                mSelectedPosition = position;
                v.setSelected(true);

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(this, v, position, getItemIdAtPosition(position));
                }
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(this, v, position, getItemIdAtPosition(position));
                }

                break;
            }
        }
    }

    @Override
    public void computeScroll() {
        //  if we don't have an adapter, we don't need to do anything
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getCount() == 0) {
            return;
        }

        if (getChildCount()
                == 0) {  // release memory resources was probably called before, and onLayout didn't get called to fill
            //  container again
            requestLayout();
        }

        if (mTouchState == TOUCH_STATE_ALIGN) {
            if (mAlignScroller.computeScrollOffset()) {
                if (mAlignScroller.getFinalX() == mAlignScroller.getCurrX()) {
                    mAlignScroller.abortAnimation();
                    mTouchState = TOUCH_STATE_RESTING;
                    return;
                }

                int x = mAlignScroller.getCurrX();
                scrollTo(x, 0);

                postInvalidate();
                return;
            } else {
                mTouchState = TOUCH_STATE_RESTING;
                return;
            }
        }

        super.computeScroll();
    }

    @Override
    protected boolean checkScrollPosition() {
        if (mCenterItemOffset != 0) {
            mAlignScroller.startScroll(getScrollX(), 0, mCenterItemOffset, 0, mAlignTime);
            mTouchState = TOUCH_STATE_ALIGN;
            invalidate();
            return true;
        }
        return false;
    }

    private void getScrolledTransformedChildRectangle(View child, RectF r) {
        transformChildHitRectangle(child, r);
        final int offset = child.getLeft() - getScrollX();
        r.offset(offset, child.getTop());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float xf = ev.getX();
        final float yf = ev.getY();
        final RectF frame = mTouchRect;

        if (action == MotionEvent.ACTION_DOWN) {
            if (mMotionTarget != null) {
                //  this is weird, we got a pen down, but we thought it was
                //  already down!
                //  We should probably send an ACTION_UP to the current
                //  target.
                mMotionTarget = null;
            }
            //  If we're disallowing intercept or if we're allowing and we didn't
            //  intercept
            if (!onInterceptTouchEvent(ev)) {
                //  reset this event's action (just to protect ourselves)
                ev.setAction(MotionEvent.ACTION_DOWN);
                //  We know we want to dispatch the event down, find a child
                //  who can handle it, start with the front-most child.

                final int count = getChildCount();
                final int[] childOrder = new int[count];

                for (int i = 0; i < count; i++) {
                    childOrder[i] = getChildDrawingOrder(count, i);
                }

                for (int i = count - 1; i >= 0; i--) {
                    final View child = getChildAt(childOrder[i]);
                    if (child.getVisibility() == VISIBLE || child.getAnimation() != null) {

                        getScrolledTransformedChildRectangle(child, frame);

                        if (frame.contains(xf, yf)) {
                            //  offset the event to the view's coordinate system
                            final float xc = xf - frame.left;
                            final float yc = yf - frame.top;
                            ev.setLocation(xc, yc);
                            if (child.dispatchTouchEvent(ev)) {
                                //  Event handled, we have a target now.
                                mMotionTarget = child;
                                mTargetTop = frame.top;
                                mTargetLeft = frame.left;
                                return true;
                            }

                            break;
                        }
                    }
                }
            }
        }

        boolean isUpOrCancel = (action == MotionEvent.ACTION_UP) || (action == MotionEvent.ACTION_CANCEL);

        //  The event wasn't an ACTION_DOWN, dispatch it to our target if
        //  we have one.
        final View target = mMotionTarget;
        if (target == null) {
            //  We don't have a target, this means we're handling the
            //  event as a regular view.
            ev.setLocation(xf, yf);
            return onTouchEvent(ev);
        }

        //  if have a target, see if we're allowed to and want to intercept its
        //  events
        if (onInterceptTouchEvent(ev)) {
            final float xc = xf - mTargetLeft;
            final float yc = yf - mTargetTop;
            ev.setAction(MotionEvent.ACTION_CANCEL);
            ev.setLocation(xc, yc);
            if (!target.dispatchTouchEvent(ev)) {
                //  target didn't handle ACTION_CANCEL. not much we can do
                //  but they should have.
                L.d("target.dispatchTouchEvent");
            }
            //  clear the target
            mMotionTarget = null;
            //  Don't dispatch this event to our own view, because we already
            //  saw it when intercepting; we just want to give the following
            //  event to the normal onTouchEvent().
            return true;
        }

        if (isUpOrCancel) {
            mMotionTarget = null;
            mTargetTop = -1;
            mTargetLeft = -1;
        }

        //  finally offset the event to the target's coordinate system and
        //  dispatch the event.
        final float xc = xf - mTargetLeft;
        final float yc = yf - mTargetTop;
        ev.setLocation(xc, yc);

        return target.dispatchTouchEvent(ev);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int h;
        int w;
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            h = heightSpecSize;
        } else {
            h = (int) ((mCoverHeight + mCoverHeight * mReflectionHeight + mReflectionGap) * mMaxScaleFactor
                               + mPaddingTop + mPaddingBottom);
            h = resolveSize(h, heightMeasureSpec);
        }

        if (widthSpecMode == MeasureSpec.EXACTLY) {
            w = widthSpecSize;
        } else {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            w = display.getWidth();
            w = resolveSize(w, widthMeasureSpec);
        }

        setMeasuredDimension(w, h);
    }

    /**
     * Set new center item position
     */
    public void scrollToPosition(int position) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            throw new IllegalStateException(
                    "You are trying to scroll " + "container with no adapter set. Set adapter first.");
        }

        if (mLastCenterItemIndex != -1) {
            final int lastCenterItemPosition = (mFirstItemPosition + mLastCenterItemIndex) % mAdapter.getCount();
            final int di = lastCenterItemPosition - position;

            //  点击卡片切换无滑动效果 lishicong
            //              final int dst = (int) (di * mCoverWidth * mSpacing);
            //              mScrollToPositionOnNextInvalidate = -1;
            //              scrollBy(-dst, 0);

            //  TODO 点击卡片切换有滑动效果，显示超过5张卡片此算法需要优化
            int di2 = di;
            int m = 1;
            int n = 2;
            if (di == -mAdapter.getCount() + m) {
                di2 = m;
            } else if (di == -mAdapter.getCount() + n) {
                di2 = n;
            } else if (di == mAdapter.getCount() - m) {
                di2 = -m;
            } else if (di == mAdapter.getCount() - n) {
                di2 = -n;
            }
            final int dst = (int) (di2 * mCoverWidth * mSpacing);
            mScrollToPositionOnNextInvalidate = -1;
            scroll(-dst);
        } else {
            mScrollToPositionOnNextInvalidate = position;
        }

        invalidate();
    }

    /**
     * sets listener for center item position
     *
     * @param onScrollPositionListener
     */
    public void setOnScrollPositionListener(OnScrollPositionListener onScrollPositionListener) {
        mOnScrollPositionListener = onScrollPositionListener;
    }

    /**
     * removes children, must be after caching children
     *
     * @param cf
     */
    private void recycleCoverFrame(CoverFrame cf) {
        cf.recycle();
        WeakReference<CoverFrame> ref = new WeakReference<>(cf);
        mRecycledCoverFrames.addLast(ref);
    }

    protected CoverFrame getRecycledCoverFrame() {
        if (!mRecycledCoverFrames.isEmpty()) {
            CoverFrame v;
            do {
                v = mRecycledCoverFrames.removeFirst().get();
            } while (v == null && !mRecycledCoverFrames.isEmpty());
            return v;
        }
        return null;
    }

    @Override
    public boolean onPreDraw() {  // when child view is about to be drawn we invalidate whole container

        if (!mInvalidated) {  // this is hack, no idea now is possible that this works, but fixes problem where not all
            //  area was redrawn
            mInvalidated = true;
            invalidate();
            return false;
        }

        return true;

    }

}