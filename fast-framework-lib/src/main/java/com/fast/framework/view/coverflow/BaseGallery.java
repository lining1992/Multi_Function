/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.coverflow;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import com.fast.framework.support.L;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Scroller;

/**
 * Created by lishicong on 2017/6/6.
 */

public abstract class BaseGallery extends AdapterView<Adapter> {
    /**
     * Children added with this layout mode will be added after the last child
     */
    protected static final int LAYOUT_MODE_AFTER = 0;

    /**
     * Children added with this layout mode will be added before the first child
     */
    protected static final int LAYOUT_MODE_TO_BEFORE = 1;

    protected static final int SCROLLING_DURATION = 1500;

    /**
     * The adapter providing data for container
     */
    protected Adapter mAdapter;

    /**
     * The adaptor position of the first visible item
     */
    protected int mFirstItemPosition;

    /**
     * The adaptor position of the last visible item
     */
    protected int mLastItemPosition;

    /**
     * The adaptor position of selected item
     */
    protected int mSelectedPosition = INVALID_POSITION;

    /**
     * Left of current most left child
     */
    protected int mLeftChildEdge;

    /**
     * User is not touching the list
     */
    protected static final int TOUCH_STATE_RESTING = 1;

    /**
     * User is scrolling the list
     */
    protected static final int TOUCH_STATE_SCROLLING = 2;

    /**
     * Fling gesture in progress
     */
    protected static final int TOUCH_STATE_FLING = 3;

    /**
     * Aligning in progress
     */
    protected static final int TOUCH_STATE_ALIGN = 4;

    protected static final int TOUCH_STATE_DISTANCE_SCROLL = 5;

    /**
     * A list of cached (re-usable) item views
     */
    protected final LinkedList<WeakReference<View>> mCachedItemViews = new LinkedList<>();

    /**
     * If there is not enough items to fill adapter, this value is set to true and scrolling is disabled. Since all
     * items from adapter are on screen
     */
    protected boolean isSrollingDisabled = true;

    /**
     * Position to scroll adapter only if is in endless mode. This is done after layout if we find out we are endless,
     * we must relayout
     */
    protected int mScrollPositionIfEndless = -1;

    protected int mTouchState = TOUCH_STATE_RESTING;

    protected final Scroller mScroller = new Scroller(getContext());
    private VelocityTracker mVelocityTracker;
    private boolean mDataChanged;

    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private boolean mAllowLongPress;
    private float mLastMotionX;
    private float mLastMotionY;

    private final Point mDown = new Point();
    private boolean mHandleSelectionOnActionUp = false;
    private boolean mInterceptTouchEvents;

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemSelectedListener mOnItemSelectedListener;

    public BaseGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

    }

    public BaseGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BaseGallery(Context context) {
        this(context, null);
    }

    private final DataSetObserver fDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            synchronized (this) {
                mDataChanged = true;
            }
            invalidate();
        }

        @Override
        public void onInvalidated() {
            mAdapter = null;
        }
    };

    /**
     * Params describing position of child view in container
     * in HORIZONTAL mode TOP,CENTER,BOTTOM are active in VERTICAL mode LEFT,CENTER,RIGHT are active
     *
     * @author Martin Appl
     */
    public static class LoopLayoutParams extends MarginLayoutParams {
        public static final int CENTER = 1;

        public int position;

        public LoopLayoutParams(int w, int h) {
            super(w, h);
            position = CENTER;
        }

    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(fDataObserver);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(fDataObserver);

        reset();
        refill();
        invalidate();
    }

    @Override
    public View getSelectedView() {
        // throw new UnsupportedOperationException("Not supported");
        return null;
    }

    /**
     * Position index must be in range of adapter values (0 - getCount()-1) or -1 to unselect
     */
    @Override
    public void setSelection(int position) {
        throw new UnsupportedOperationException("Not supported");
    }

    private void reset() {
        scrollTo(0, 0);
        removeAllViewsInLayout();
        mFirstItemPosition = 0;
        mLastItemPosition = -1;
        mLeftChildEdge = 0;
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

        if (mScroller.computeScrollOffset()) {
            if (mScroller.getFinalX() == mScroller.getCurrX()) {
                mScroller.abortAnimation();
                mTouchState = TOUCH_STATE_RESTING;
                if (!checkScrollPosition()) {
                    L.d("checkScrollPosition");
                }
                return;
            }

            int x = mScroller.getCurrX();
            scrollTo(x, 0);

            postInvalidate();
        } else if (mTouchState == TOUCH_STATE_FLING || mTouchState == TOUCH_STATE_DISTANCE_SCROLL) {
            mTouchState = TOUCH_STATE_RESTING;
            if (!checkScrollPosition()) {
                L.d("checkScrollPosition");
            }
        }

        if (mDataChanged) {
            removeAllViewsInLayout();
            refillOnChange(mFirstItemPosition);
            return;
        }

        relayout();
        removeNonVisibleViews();
        refillRight();
        refillLeft();

    }

    /**
     * @param velocityY The initial velocity in the Y direction. Positive
     *                  numbers mean that the finger/cursor is moving down the screen,
     *                  which means we want to scroll towards the top.
     * @param velocityX The initial velocity in the X direction. Positive
     *                  numbers mean that the finger/cursor is moving right the screen,
     *                  which means we want to scroll towards the top.
     */
    public void fling(int velocityX, int velocityY) {
        mTouchState = TOUCH_STATE_FLING;
        final int x = getScrollX();
        final int y = getScrollY();

        mScroller.fling(x, y, velocityX, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE,
                        Integer.MAX_VALUE);

        invalidate();
    }

    /**
     * Scroll widget by given distance in pixels
     *
     * @param dx
     */
    public void scroll(int dx) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, SCROLLING_DURATION);
        mTouchState = TOUCH_STATE_DISTANCE_SCROLL;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //  if we don't have an adapter, we don't need to do anything
        if (mAdapter == null) {
            return;
        }

        refillInternal(mLastItemPosition, mFirstItemPosition);
    }

    /**
     * Method for actualizing content after data change in adapter. It is expected container was emptied before
     *
     * @param firstItemPosition
     */
    protected void refillOnChange(int firstItemPosition) {
        refillInternal(firstItemPosition - 1, firstItemPosition);
    }

    protected void refillInternal(final int lastItemPos, final int firstItemPos) {
        //  if we don't have an adapter, we don't need to do anything
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getCount() == 0) {
            return;
        }

        if (getChildCount() == 0) {
            fillFirstTime(lastItemPos, firstItemPos);
        } else {
            relayout();
            removeNonVisibleViews();
            refillRight();
            refillLeft();
        }
    }

    /**
     * Check if container visible area is filled and refill empty areas
     */
    private void refill() {
        scrollTo(0, 0);
        refillInternal(-1, 0);
    }

    protected void measureChild(View child) {
        final int pwms = MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY);
        final int phms = MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY);
        measureChild(child, pwms, phms);
    }

    private void relayout() {
        final int c = getChildCount();
        int left = mLeftChildEdge;

        View child;
        LoopLayoutParams lp;
        for (int i = 0; i < c; i++) {
            child = getChildAt(i);
            lp = (LoopLayoutParams) child.getLayoutParams();
            measureChild(child);

            left = layoutChildHorizontal(child, left, lp);
        }

    }

    protected abstract void fillFirstTime(final int lastItemPos, final int firstItemPos);

    /**
     * Checks and refills empty area on the right
     */
    protected abstract void refillRight();

    /**
     * Checks and refills empty area on the left
     */
    protected abstract void refillLeft();

    /**
     * Removes view that are outside of the visible part of the list. Will not
     * remove all views.
     */
    protected abstract void removeNonVisibleViews();

    /**
     * Layouts children from left to right
     *
     * @param left positon for left edge in parent container
     * @param lp   layout params
     *
     * @return new left
     */
    protected abstract int layoutChildHorizontal(View v, int left, LoopLayoutParams lp);

    /**
     * Allows to make scroll alignments
     *
     * @return true if invalidate() was issued, and container is going to scroll
     */
    protected abstract boolean checkScrollPosition();

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */

        /*
         * Shortcut the most recurring case: the user is in the dragging
         * state and he is moving his finger.  We want to intercept this
         * motion.
         */
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState == TOUCH_STATE_SCROLLING)) {
            return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_MOVE:

                /*
                 * not dragging, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                 * Locally do absolute value. mLastMotionX is set to the x value
                 * of the down event.
                 */
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                final int yDiff = (int) Math.abs(y - mLastMotionY);

                final int touchSlop = mTouchSlop;
                final boolean xMoved = xDiff > touchSlop;
                final boolean yMoved = yDiff > touchSlop;

                if (xMoved) {

                    //  Scroll if the user moved far enough along the X axis
                    mTouchState = TOUCH_STATE_SCROLLING;
                    mHandleSelectionOnActionUp = false;
                    //                     enableChildrenCache();

                    //  Either way, cancel any pending longpress
                    if (mAllowLongPress) {
                        mAllowLongPress = false;
                        //  Try canceling the long press. It could also have been scheduled
                        //  by a distant descendant, so use the mAllowLongPress flag to block
                        //  everything
                        cancelLongPress();
                    }
                }
                if (yMoved) {
                    mHandleSelectionOnActionUp = false;
                    if (mAllowLongPress) {
                        mAllowLongPress = false;
                        cancelLongPress();
                    }
                }
                break;

            case MotionEvent.ACTION_DOWN:
                //  Remember location of down touch
                mLastMotionX = x;
                mLastMotionY = y;
                mAllowLongPress = true;
                //                 mCancelInIntercept = false;

                mDown.x = (int) x;
                mDown.y = (int) y;

                /*
                 * If being flinged and user touches the screen, initiate drag;
                 * otherwise don't.  mScroller.isFinished should be false when
                 * being flinged.
                 */
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTING : TOUCH_STATE_SCROLLING;
                // if he had normal click in rested state, remember for action up check
                if (mTouchState == TOUCH_STATE_RESTING) {
                    mHandleSelectionOnActionUp = true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                mDown.x = -1;
                mDown.y = -1;
                break;
            case MotionEvent.ACTION_UP:
                // if we had normal down click and we haven't moved enough to initiate drag, take action as a click on
                //  down coordinates
                if (mHandleSelectionOnActionUp && mTouchState == TOUCH_STATE_RESTING) {
                    final float d = ToolBox.getLineLength(mDown.x, mDown.y, x, y);
                    if ((ev.getEventTime() - ev.getDownTime()) < ViewConfiguration.getLongPressTimeout()
                            && d < mTouchSlop) {
                        handleClick(mDown);
                    }
                }
                //  Release the drag
                mAllowLongPress = false;
                mHandleSelectionOnActionUp = false;
                mDown.x = -1;
                mDown.y = -1;
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    if (checkScrollPosition()) {
                        break;
                    }
                }
                mTouchState = TOUCH_STATE_RESTING;
                break;
            default:
                break;
        }

        mInterceptTouchEvents = mTouchState == TOUCH_STATE_SCROLLING;
        return mInterceptTouchEvents;

    }

    protected abstract void handleClick(Point p);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //  if we don't have an adapter, we don't need to do anything
        if (mAdapter == null) {
            return false;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                super.onTouchEvent(event);
            /*
             * If being flinged and user touches, stop the fling. isFinished
             * will be false if being flinged.
             */
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }

                //  Remember where the motion event started
                mLastMotionX = x;
                mLastMotionY = y;

                break;
            case MotionEvent.ACTION_MOVE:

                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    //  Scroll to follow the motion event
                    final int deltaX = (int) (mLastMotionX - x);
                    mLastMotionX = x;
                    mLastMotionY = y;

                    int sx = getScrollX() + deltaX;
                    scrollTo(sx, 0);

                } else {
                    final int xDiff = (int) Math.abs(x - mLastMotionX);

                    final int touchSlop = mTouchSlop;
                    final boolean xMoved = xDiff > touchSlop;

                    if (xMoved) {

                        //  Scroll if the user moved far enough along the X axis
                        mTouchState = TOUCH_STATE_SCROLLING;
                        //                         enableChildrenCache();

                        //  Either way, cancel any pending longpress
                        if (mAllowLongPress) {
                            mAllowLongPress = false;
                            //  Try canceling the long press. It could also have been scheduled
                            //  by a distant descendant, so use the mAllowLongPress flag to block
                            //  everything
                            cancelLongPress();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                // this must be here, in case no child view returns true,
                // events will propagate back here and on intercept touch event wont be called again
                // in case of no parent it propagates here, in case of parent it usualy propagates to on cancel
                if (mHandleSelectionOnActionUp && mTouchState == TOUCH_STATE_RESTING) {
                    final float d = ToolBox.getLineLength(mDown.x, mDown.y, x, y);
                    if ((event.getEventTime() - event.getDownTime()) < ViewConfiguration.getLongPressTimeout()
                            && d < mTouchSlop) {
                        handleClick(mDown);
                    }
                    mHandleSelectionOnActionUp = false;
                }

                // if we had normal down click and we haven't moved enough to initiate drag, take action as a click on
                //  down coordinates
                if (mTouchState == TOUCH_STATE_SCROLLING) {

                    //  这里减慢滑动速度 20170602 lishicong
                    //  mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    mVelocityTracker.computeCurrentVelocity(mMinimumVelocity, mMaximumVelocity);

                    int initialXVelocity = (int) mVelocityTracker.getXVelocity();
                    int initialYVelocity = (int) mVelocityTracker.getYVelocity();

                    if (Math.abs(initialXVelocity) + Math.abs(initialYVelocity) > mMinimumVelocity) {
                        fling(-initialXVelocity, -initialYVelocity);
                    } else {
                        //  Release the drag
                        mTouchState = TOUCH_STATE_RESTING;
                        checkScrollPosition();
                        mAllowLongPress = false;

                        mDown.x = -1;
                        mDown.y = -1;
                    }

                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }

                    break;
                }

                mTouchState = TOUCH_STATE_RESTING;
                mAllowLongPress = false;

                mDown.x = -1;
                mDown.y = -1;

                break;
            case MotionEvent.ACTION_CANCEL:

                mAllowLongPress = false;

                mDown.x = -1;
                mDown.y = -1;

                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    if (checkScrollPosition()) {
                        break;
                    }
                }

                mTouchState = TOUCH_STATE_RESTING;
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Check if list of weak references has any view still in memory to offer for recyclation
     *
     * @return cached view
     */
    protected View getCachedView() {
        if (mCachedItemViews.size() != 0) {
            View v;
            do {
                v = mCachedItemViews.removeFirst().get();
            } while (v == null && mCachedItemViews.size() != 0);
            return v;
        }
        return null;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    @Override
    @ViewDebug.CapturedViewProperty
    public int getCount() {
        if (mAdapter != null) {
            return mAdapter.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public int getPositionForView(View view) {
        final int c = getChildCount();
        View v;
        for (int i = 0; i < c; i++) {
            v = getChildAt(i);
            if (v == view) {
                return mFirstItemPosition + i;
            }
        }
        return INVALID_POSITION;
    }

    @Override
    public int getFirstVisiblePosition() {
        return mFirstItemPosition;
    }

    @Override
    public int getLastVisiblePosition() {
        return mLastItemPosition;
    }

    @Override
    public Object getItemAtPosition(int position) {
        final int index;
        if (mFirstItemPosition > position) {
            index = position + mAdapter.getCount() - mFirstItemPosition;
        } else {
            index = position - mFirstItemPosition;
        }
        if (index < 0 || index >= getChildCount()) {
            return null;
        }

        return getChildAt(index);
    }

    @Override
    public long getItemIdAtPosition(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public boolean performItemClick(View view, int position, long id) {
        throw new UnsupportedOperationException();
    }

}
