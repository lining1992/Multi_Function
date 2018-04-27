/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.widget;

import java.util.List;

import com.fast.framework.R;
import com.fast.framework.data.RecyclerViewAdapter;
import com.fast.framework.view.pulltorefresh.PtrClassicFrameLayout;
import com.fast.framework.view.pulltorefresh.PtrFrameLayout;
import com.fast.framework.view.videolist.visibility.calculator.DefaultSingleItemCalculatorCallback;
import com.fast.framework.view.videolist.visibility.calculator.SingleListViewItemActiveCalculator;
import com.fast.framework.view.videolist.visibility.scroll.ItemsPositionGetter;
import com.fast.framework.view.videolist.visibility.scroll.RecyclerViewItemPositionGetter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by lishicong on 2016/10/27.
 */

public class PullToRefreshRecyclerView<T> extends RelativeLayout {

    // 分页起始页数
    public static final int START_PAGE = 1;
    // 每页数量
    public static final int PAGE_SIZE = 20;

    private Context mContext;

    // 下拉刷新
    private PtrClassicFrameLayout mPtrFrame;
    // 列表
    private RecyclerView mRecyclerView;

    /**
     * 下拉刷新视图数据适配器
     */
    private RecyclerViewAdapter mRecyclerViewAdapter;
    /**
     * 下拉刷新视图事件监听
     */
    private RecyclerViewListener mRecyclerViewListener;
    /**
     * 滚动状态
     */
    private int mScrollState;
    /**
     * 分页显示时，当前数据是否为最后一页
     */
    private boolean isLastPage = false;

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        this.mContext = context;
        this.init();
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.init();
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.init();
    }

    public PtrClassicFrameLayout getPtrFrame() {
        return mPtrFrame;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 返回分页的下一页数
     *
     * @return
     */
    public int getNextPage() {
        int count = mRecyclerViewAdapter.getItemCount();
        int nextPage = count / PAGE_SIZE + 1;
        return nextPage;
    }

    /**
     * 设置为已经是最后一页状态
     */
    public void isLastPage() {
        this.isLastPage = true;
    }

    /**
     * 设置下拉刷新视图数据构造器
     *
     * @param adapter
     */
    public void setRecyclerViewAdapter(RecyclerViewAdapter adapter) {
        this.mRecyclerViewAdapter = adapter;
    }

    /**
     * 设置下拉刷新视图回调监听
     *
     * @param listener
     */
    public void setRecyclerViewListener(RecyclerViewListener listener) {
        this.mRecyclerViewListener = listener;
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fast_view_pulltorefresh_recycler, null);

        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.pull_to_refresh_ptr_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pull_to_refresh_recycler_view);

        // header
        //        final MaterialHeader header = new MaterialHeader(getContext());
        //        int[] colors = getResources().getIntArray(R.array.google_colors);
        //        header.setColorSchemeColors(colors);
        //        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        //        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        //        header.setPtrFrameLayout(mPtrFrame);
        //        mPtrFrame.setLoadingMinTime(1000);
        //        mPtrFrame.setDurationToCloseHeader(1500);
        //        mPtrFrame.setHeaderView(header);
        //        mPtrFrame.addPtrUIHandler(header);

        mPtrFrame.setLastUpdateTimeRelateObject(mContext);
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh(false);
            }
        }, 100);

        this.addView(view);
    }

    /**
     * 界面初使化完成后初使化下拉刷新列表
     */
    public void onActivityCreated() {

        getPtrFrame().setPtrHandler(new RecyclerViewPtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isLastPage = false; // 每次刷新时更新为有更多数据状态
                mRecyclerViewListener.recyclerRefreshData();
            }
        });

        getRecyclerView().setOnScrollListener(new RecyclerOnScrollListener() {

            @Override
            public void onScrollBottom() {
                if (!isLastPage) {
                    mRecyclerViewListener.recyclerMoreData();
                }
            }
        });

        final SingleListViewItemActiveCalculator mCalculator = new SingleListViewItemActiveCalculator(
                new DefaultSingleItemCalculatorCallback(), mRecyclerViewAdapter.getObjects());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        getRecyclerView().setLayoutManager(layoutManager);
        getRecyclerView().setAdapter(mRecyclerViewAdapter);

        final ItemsPositionGetter mItemsPositionGetter = new RecyclerViewItemPositionGetter(layoutManager,
                                                                                            getRecyclerView());
        getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mScrollState = newState;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !mRecyclerViewAdapter.getObjects().isEmpty()) {
                    mCalculator.onScrollStateIdle(mItemsPositionGetter);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mCalculator.onScrolled(mItemsPositionGetter, mScrollState);
            }
        });
    }

    /**
     * 下拉列表刷新数据
     *
     * @param list
     */
    public void refreshObjects(List<T> list) {
        mRecyclerViewAdapter.refreshObjects(list);
    }

    /**
     * 下拉列表添加更多数据
     *
     * @param list
     * @param total
     */
    public void addObjects(List<T> list, int total) {

        mRecyclerViewAdapter.addObjects(list);

        if (mRecyclerViewAdapter.getItemCount() >= total) {
            this.isLastPage();
        }
    }

    /**
     * 下拉刷新视图滚动事件监听
     */
    abstract class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {

        /**
         * 滑动到底部还有5条数据时加载更多
         */
        private static final int NOT_SHOW_NUMBER = 5;
        /**
         * 最后可见行
         */
        private int mLastVisbleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                int[] i = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPositions(
                        null);
                mLastVisbleItem = i[0]; // 此处逻辑按照单列数据来处理，如果需要多列瀑布流，需要取值大的 Math.max(i[0], i[1])
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                mLastVisbleItem =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            }
            mRecyclerViewListener.onRecyclerScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.getAdapter() != null
                    && mLastVisbleItem + NOT_SHOW_NUMBER >= recyclerView.getAdapter().getItemCount()) {
                onScrollBottom();
            }
        }

        /**
         * 滚动到底部事件
         */
        public abstract void onScrollBottom();
    }

    /**
     * 下拉刷新视图回调事件
     */
    public interface RecyclerViewListener {

        /**
         * 获取刷新数据
         */
        public void recyclerRefreshData();

        /**
         * 获取更多数据
         */
        public void recyclerMoreData();

        /**
         * 列表滚动事件
         *
         * @param recyclerView
         * @param dx
         * @param dy
         */
        public void onRecyclerScrolled(RecyclerView recyclerView, int dx, int dy);

        /**
         * 滚动到底部事件
         */
        public void onScrollBottom();
    }

}
