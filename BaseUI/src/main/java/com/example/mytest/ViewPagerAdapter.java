package com.example.mytest;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lijuan on 2016/9/12.
 */
public abstract class ViewPagerAdapter<T> extends PagerAdapter {
    private List<T> mViewList;

    public ViewPagerAdapter(List<T> mViewList) {
        this.mViewList = mViewList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getItemView(position);
        container.addView(view);
        return view;
    }

    protected abstract View getItemView(int position);

    @Override
    public int getCount() {
        if (mViewList == null)
            return 0;
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}