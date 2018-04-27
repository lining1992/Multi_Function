/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by lishicong on 2016/12/16.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected Set<View> mSkinnableViewsCache = new HashSet<>();
    private List<T> mObjects = new ArrayList<T>();

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return false;
                }
            });
        }
    }

    public T getObject(int position) {
        return mObjects.get(position);
    }

    public List<T> getObjects() {
        return mObjects;
    }

    public void refreshObjects(List<T> list) {
        this.mObjects.clear();
        this.mObjects.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addObjectsByIndex(List<T> list, int index) {
        int start = 0;
        this.mObjects.addAll(index, list);
        int end = this.mObjects.size();
        this.notifyItemRangeChanged(start, end);
    }

    public void addObjects(List<T> list) {
        int start = this.mObjects.size();
        this.mObjects.addAll(list);
        int end = this.mObjects.size();
        this.notifyItemRangeChanged(start, end);
    }

    public void removeObject(int index) {
        this.mObjects.remove(index);
        this.notifyItemRemoved(index);
    }

    public interface OnItemClickListener {

        public void onItemClick(View v, int position);

        public void onItemLongClick(View v, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
