package com.example.commonlibrary.android.viewpager;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/13 14:22
 */
@SuppressWarnings("unused")
public class ViewPagerAdapter<T> extends PagerAdapter {

    private final LayoutInflater mInflater;

    private int mLayoutId;
    private BaseViewHolderFactory<T> mFactory;
    private TitleProvider mTitleProvider;
    private ItemClickListener<T> mItemClickListener;
    private MultiTypeSupport mMultiTypeSupport;
    private final List<T> mItems;
    private SparseArray<Stack<BaseViewHolder<T>>> mHolderPool;
    private SparseArray<BaseViewHolder<T>> mHolderCache;

    public ViewPagerAdapter(@NonNull Context context) {
        mItems = new ArrayList<>();
        mHolderPool = new SparseArray<>();
        mHolderCache = new SparseArray<>();
        mInflater = LayoutInflater.from(context);
    }

    public ViewPagerAdapter<T> itemLayoutId(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
        return this;
    }

    public ViewPagerAdapter<T> viewHolderFactory(BaseViewHolderFactory<T> factory) {
        mFactory = factory;
        return this;
    }

    public ViewPagerAdapter<T> multiTypeSupport(MultiTypeSupport support) {
        mMultiTypeSupport = support;
        return this;
    }

    public ViewPagerAdapter<T> itemClickListener(ItemClickListener<T> listener) {
        mItemClickListener = listener;
        return this;
    }

    public ViewPagerAdapter<T> titleProvider(TitleProvider provider) {
        mTitleProvider = provider;
        return this;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        final T t = mItems.get(position);
        int layoutId;
        int viewType = 0;
        if (mMultiTypeSupport == null) {
            layoutId = mLayoutId;
        } else {
            viewType = mMultiTypeSupport.getViewType(position);
            layoutId = mMultiTypeSupport.getLayoutId(viewType);
        }
        BaseViewHolder<T> viewHolder = getHolderFromPool(viewType);
        if (viewHolder == null) {
            View itemView = mInflater.inflate(layoutId, container, false);
            viewHolder = mFactory.createViewHolder(itemView, viewType);
        }
        putHolderInCache(viewHolder, position);
        View itemView = viewHolder.getItemView();
        viewHolder.onBindItemView(itemView, t, position);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(container, t, position);
                }
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        BaseViewHolder<T> holder = mHolderCache.get(position);
        int viewType = getViewType(position);
        Util.departFromParent(holder.getItemView());
        putHolderInPool(holder, viewType);
        mHolderCache.remove(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleProvider != null) {
            return mTitleProvider.getPageTitle(position);
        }
        return super.getPageTitle(position);
    }

    public void update(List<T> list) {
        mItems.clear();
        if (list != null) {
            mItems.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void add(T item) {
        add(mItems.size(), item);
    }

    public void add(int index, T item) {
        if (item == null) {
            return;
        }
        mItems.add(index, item);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        addAll(mItems.size(), list);
    }

    @SuppressWarnings("WeakerAccess")
    public void addAll(int start, List<T> items) {
        if (items == null) {
            return;
        }
        mItems.addAll(start, items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }

    public void remove(int position, int count) {
        if (position < 0 || position >= mItems.size()) {
            return;
        }

        ListIterator<T> iterator = mItems.listIterator(position);
        int countToRemove = count;
        while (iterator.hasNext() && countToRemove > 0) {
            iterator.next();
            iterator.remove();
            countToRemove--;
        }

        notifyDataSetChanged();
    }

    public void moveItem(int from, int to) {
        Util.move(mItems, from, to);
        notifyDataSetChanged();
    }

    public void updateItem(int position, Object payload) {
        BaseViewHolder<T> holder = getHolderFromCache(position);
        if (holder == null) {
            return;
        }
        holder.onRefreshItemView(payload);
    }

    public List<T> getDatas() {
        return mItems;
    }

    private int getViewType(int position) {
        int viewType = 0;
        if (mMultiTypeSupport != null) {
            viewType = mMultiTypeSupport.getViewType(position);
        }
        return viewType;
    }

    private void putHolderInPool(BaseViewHolder<T> holder, int viewType) {
        Stack<BaseViewHolder<T>> holders = mHolderPool.get(viewType);
        if (holders == null) {
            holders = new Stack<>();
            mHolderPool.put(viewType, holders);
        }
        holders.push(holder);
    }

    private BaseViewHolder<T> getHolderFromPool(int viewType) {
        Stack<BaseViewHolder<T>> holders = mHolderPool.get(viewType);
        if (holders != null && !holders.empty()) {
            return holders.pop();
        }
        return null;
    }

    private void putHolderInCache(BaseViewHolder<T> viewHolder, int position) {
        mHolderCache.put(position, viewHolder);
    }

    private BaseViewHolder<T> getHolderFromCache(int position) {
        return mHolderCache.get(position);
    }
}
