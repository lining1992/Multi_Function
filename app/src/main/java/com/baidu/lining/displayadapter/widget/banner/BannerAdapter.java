package com.baidu.lining.displayadapter.widget.banner;

import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by hcc on 16/8/7 21:18
 * 100332338@qq.com
 * <p/>
 * Banner适配器
 */
public class BannerAdapter extends PagerAdapter {

  private List<ImageView> mList;

  private int pos;

  private ViewPagerOnItemClickListener mViewPagerOnItemClickListener;
  private ViewPagerOnTouchListener mViewPagerOnTouchListener;

  public void setmViewPagerOnItemClickListener(ViewPagerOnItemClickListener mViewPagerOnItemClickListener) {

    this.mViewPagerOnItemClickListener = mViewPagerOnItemClickListener;
  }

  public void setViewPagerOnTouchListener(ViewPagerOnTouchListener mViewPagerOnTouchListener){
    this.mViewPagerOnTouchListener = mViewPagerOnTouchListener;
  }


  public BannerAdapter(List<ImageView> list) {
    this.mList = list;
  }


  @Override
  public int getCount() {

    return Integer.MAX_VALUE;
  }


  @Override
  public boolean isViewFromObject(View arg0, Object arg1) {

    return arg0 == arg1;
  }


  @Override
  public Object instantiateItem(ViewGroup container, int position) {

    //对ViewPager页号求模取出View列表中要显示的项
    position %= mList.size();
    if (position < 0) {
      position = mList.size() + position;
    }
    ImageView v = mList.get(position);
    pos = position;
    v.setScaleType(ImageView.ScaleType.CENTER);
    //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
    ViewParent vp = v.getParent();
    if (vp != null) {
      ViewGroup parent = (ViewGroup) vp;
      parent.removeView(v);
    }
    v.setOnClickListener(v1 -> {

      if (mViewPagerOnItemClickListener != null) {
        mViewPagerOnItemClickListener.onItemClick();
      }
    });
    // 按下状态不让viewPager滑动
    v.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
          case MotionEvent.ACTION_DOWN:
            mViewPagerOnTouchListener.onTouchDown();
            break;
          case MotionEvent.ACTION_UP:
            mViewPagerOnTouchListener.onTouchUp();
            break;
        }
        return false;
      }
    });

    container.addView(v);
    return v;
  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {

  }


  public interface ViewPagerOnItemClickListener {

    void onItemClick();
  }

  public interface ViewPagerOnTouchListener{
    void onTouchDown();
    void onTouchUp();
  }
}
