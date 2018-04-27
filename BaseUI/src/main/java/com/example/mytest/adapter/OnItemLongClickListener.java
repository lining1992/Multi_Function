package com.example.mytest.adapter;

import android.view.View;

public interface OnItemLongClickListener<T> {

    void onItemLongClick(int position, View v, T t);
}
