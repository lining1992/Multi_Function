package com.example.mytest.adapter;

import android.view.View;

public interface OnItemClickListener<T> {

    void onItemClick(int position, View v, T t);
}
