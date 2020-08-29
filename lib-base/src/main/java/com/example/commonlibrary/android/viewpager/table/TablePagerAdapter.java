package com.example.commonlibrary.android.viewpager.table;

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
 *         email : leixing@baidu.com
 * @date : 2018/8/13 15:28
 */
@SuppressWarnings("unused")
public class TablePagerAdapter<T> extends PagerAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;

    private int mLayoutId;
    private BaseTableViewHolderFactory<T> mFactory;
    private TitleProvider mTitleProvider;
    private TableItemClickListener<T> mTableItemClickListener;
    private int mRow = 1;
    private int mColumn = 1;
    private TableMultiTypeSupport mTableMultiTypeSupport;
    private final List<List<List<T>>> mItems;
    private SparseArray<Stack<BaseTableViewHolder<T>>> mHolderPool;
    private SparseArray<SparseArray<SparseArray<BaseTableViewHolder<T>>>> mHolderCache;

    public TablePagerAdapter(@NonNull Context context) {
        mItems = new ArrayList<>();
        mHolderPool = new SparseArray<>();
        mHolderCache = new SparseArray<>();
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public TablePagerAdapter<T> itemLayoutId(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
        return this;
    }

    public TablePagerAdapter<T> viewHolderFactory(BaseTableViewHolderFactory<T> factory) {
        mFactory = factory;
        return this;
    }

    public TablePagerAdapter<T> multiTypeSupport(TableMultiTypeSupport support) {
        mTableMultiTypeSupport = support;
        return this;
    }

    public TablePagerAdapter<T> itemClickListener(TableItemClickListener<T> listener) {
        mTableItemClickListener = listener;
        return this;
    }

    public TablePagerAdapter<T> titleProvider(TitleProvider provider) {
        mTitleProvider = provider;
        return this;
    }

    public TablePagerAdapter<T> row(int row) {
        mRow = row;
        return this;
    }

    public TablePagerAdapter<T> column(int column) {
        mColumn = column;
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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final int pageIndex = position;
        List<List<T>> lists = mItems.get(pageIndex);
        final ViewGroup parent = createContainer();
        container.addView(parent);

        int row = -1;
        for (List<T> list : lists) {
            row++;
            int column = -1;
            for (final T t : list) {
                column++;

                final int r = row;
                final int c = column;

                int layoutId;
                int viewType = 0;
                if (mTableMultiTypeSupport == null) {
                    layoutId = mLayoutId;
                } else {
                    viewType = mTableMultiTypeSupport.getViewType(pageIndex, row, column);
                    layoutId = mTableMultiTypeSupport.getLayoutId(viewType);
                }
                BaseTableViewHolder<T> viewHolder = getHolderFromPool(viewType);
                if (viewHolder == null) {
                    View itemView = mInflater.inflate(layoutId, parent, false);
                    viewHolder = mFactory.createViewHolder(itemView, viewType);
                }
                putHolderInCache(viewHolder, pageIndex, row, column);
                View itemView = viewHolder.getItemView();
                viewHolder.onBindItemView(itemView, t, pageIndex, row, column);
                addItemView(parent, itemView, row, column);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTableItemClickListener != null) {
                            mTableItemClickListener.onItemClick(parent, t, pageIndex, r, c);
                        }
                    }
                });
            }
        }

        return parent;
    }


    @SuppressWarnings("WeakerAccess")
    protected ViewGroup createContainer() {
        android.support.v7.widget.GridLayout layout = new android.support.v7.widget.GridLayout(mContext);
        layout.setRowCount(mRow);
        layout.setColumnCount(mColumn);
        return layout;
    }

    @SuppressWarnings("WeakerAccess")
    protected void addItemView(ViewGroup container, View view, int row, int column) {
        android.support.v7.widget.GridLayout layout = (android.support.v7.widget.GridLayout) container;
        android.support.v7.widget.GridLayout.LayoutParams layoutParams = new android.support.v7.widget.GridLayout.LayoutParams();
        layoutParams.height = 0;
        layoutParams.width = 0;
        layoutParams.columnSpec = android.support.v7.widget.GridLayout.spec(column, 1f);
        layoutParams.rowSpec = android.support.v7.widget.GridLayout.spec(row, 1f);
        layout.addView(view, layoutParams);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        SparseArray<SparseArray<BaseTableViewHolder<T>>> pageHolders = mHolderCache.get(position);

        for (int row = 0; row < pageHolders.size(); row++) {
            SparseArray<BaseTableViewHolder<T>> rowHolders = pageHolders.get(row);
            if (rowHolders == null) {
                continue;
            }
            for (int column = 0; column < rowHolders.size(); column++) {
                BaseTableViewHolder<T> holder = rowHolders.get(column);
                int viewType = getViewType(position, row, column);
                Util.departFromParent(holder.getItemView());
                putHolderInPool(holder, viewType);
            }
        }
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
            mItems.addAll(Util.to3D(list, mRow, mColumn));
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

        List<T> list = Util.from3D(mItems);
        list.add(index, item);
        List<List<List<T>>> lists = Util.to3D(list, mRow, mColumn);
        mItems.clear();
        mItems.addAll(lists);

        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        addAll(mItems.size(), list);
    }

    public void addAll(int start, List<T> items) {
        if (items == null) {
            return;
        }

        List<T> list = Util.from3D(mItems);
        list.addAll(start, items);
        List<List<List<T>>> lists = Util.to3D(list, mRow, mColumn);
        mItems.clear();
        mItems.addAll(lists);

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

        List<T> list = Util.from3D(mItems);

        ListIterator<T> iterator = list.listIterator(position);
        int countToRemove = count;
        while (iterator.hasNext() && countToRemove > 0) {
            iterator.next();
            iterator.remove();
            countToRemove--;
        }

        List<List<List<T>>> lists = Util.to3D(list, mRow, mColumn);
        mItems.clear();
        mItems.addAll(lists);

        notifyDataSetChanged();
    }

    public void moveItem(int from, int to) {
        List<T> list = Util.from3D(mItems);
        Util.move(list, from, to);
        List<List<List<T>>> lists = Util.to3D(list, mRow, mColumn);
        mItems.clear();
        mItems.addAll(lists);

        notifyDataSetChanged();
    }

    public void updateItem(int pageIndex, int row, int column, Object payload) {
        BaseTableViewHolder<T> holder = getHolderFromCache(pageIndex, row, column);
        if (holder == null) {
            return;
        }
        holder.onRefreshItemView(payload);
    }

    private int getViewType(int position, int row, int column) {
        int viewType = 0;
        if (mTableMultiTypeSupport != null) {
            viewType = mTableMultiTypeSupport.getViewType(position, row, column);
        }
        return viewType;
    }

    private void putHolderInPool(BaseTableViewHolder<T> holder, int viewType) {
        Stack<BaseTableViewHolder<T>> holders = mHolderPool.get(viewType);
        if (holders == null) {
            holders = new Stack<>();
            mHolderPool.put(viewType, holders);
        }
        holders.push(holder);
    }

    private BaseTableViewHolder<T> getHolderFromPool(int viewType) {
        Stack<BaseTableViewHolder<T>> holders = mHolderPool.get(viewType);
        if (holders != null && !holders.empty()) {
            return holders.pop();
        }
        return null;
    }

    private void putHolderInCache(BaseTableViewHolder<T> viewHolder, int pageIndex, int row, int column) {
        SparseArray<SparseArray<BaseTableViewHolder<T>>> pageHolders = mHolderCache.get(pageIndex);
        if (pageHolders == null) {
            pageHolders = new SparseArray<>();
            mHolderCache.put(pageIndex, pageHolders);
        }
        SparseArray<BaseTableViewHolder<T>> rowHolders = pageHolders.get(row);
        if (rowHolders == null) {
            rowHolders = new SparseArray<>();
            pageHolders.put(row, rowHolders);
        }
        rowHolders.put(column, viewHolder);
    }

    private BaseTableViewHolder<T> getHolderFromCache(int pageIndex, int row, int column) {
        SparseArray<SparseArray<BaseTableViewHolder<T>>> pageHolders = mHolderCache.get(pageIndex);
        if (pageHolders == null) {
            return null;
        }
        SparseArray<BaseTableViewHolder<T>> rowHolders = pageHolders.get(row);
        if (rowHolders == null) {
            return null;
        }
        return rowHolders.get(column);
    }
}
