package com.example.commonlibrary.android.recyclerview;

import android.view.ViewGroup;

public interface OnItemLongClickListener<T> {
    void onItemLongClick(ViewGroup parent, int position, T t);

}
