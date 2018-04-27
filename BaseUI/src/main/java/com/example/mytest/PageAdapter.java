package com.example.mytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mytest.adapter.ExPagerAdapter;
import com.example.mytest.view.banner.indicator.IndicatorAdapter;

import java.util.List;

/**
 * 创建人：v_lining05
 * 创建时间：2018/1/29
 */
public class PageAdapter extends ExPagerAdapter<Model> implements IndicatorAdapter {
    static final int PER_PAGE_CAPACITY = 6;

    private int pageSize = 5;
    private OnItemClickListener<Model> mOnItemClickListener;
    private Context context;

    public PageAdapter(List<Model> list){
        super(list);
    }

    @Override
    public int getCount() {
        int total = super.getCount();
        return total % PER_PAGE_CAPACITY > 0 ? total / PER_PAGE_CAPACITY + 1 : total / PER_PAGE_CAPACITY;
    }

    @Override
    public int getIndicatorCount() {
        return getCount();
    }

    @Override
    protected View getView(ViewGroup container,int position) {
        context = container.getContext();
        GridView gridView = (GridView) LayoutInflater.from(context).inflate(R.layout.dot, container, false);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getData(), position, pageSize);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Model model = getData().get((int) id);
                callbackOnItemClickListener(position,id,view,model);
            }
        });
        gridView.setAdapter(gridViewAdapter);
        return gridView;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position,long id, View v, T t);
    }

    public void setOnItemClickListener(OnItemClickListener<Model> lisn) {
        mOnItemClickListener = lisn;
    }


    public void callbackOnItemClickListener(int position, long id,View view, Model searchResult) {
        if (mOnItemClickListener != null && searchResult != null) {
            mOnItemClickListener.onItemClick(position, id, view, searchResult);
        }
    }

}
