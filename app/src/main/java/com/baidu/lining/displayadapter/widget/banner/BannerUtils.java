package com.baidu.lining.displayadapter.widget.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.api.BangumiAppIndexInfo;
import com.baidu.lining.displayadapter.utils.DisplayUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/5/15.
 */
public class BannerUtils implements BannerAdapter.ViewPagerOnItemClickListener, BannerAdapter.ViewPagerOnTouchListener{


    List<BannerEntity> bannerList;
    List<BangumiAppIndexInfo.ResultBean.AdBean.HeadBean> banners = new ArrayList<>();
    List<ImageView> imageViewList = new ArrayList<>();

    private final Context context;
    private final ViewPager vp;
    private int delayTime;
    private CompositeSubscription compositeSubscription;
    private final LinearLayout points;
    //选中显示Indicator
    private int selectRes = R.drawable.shape_dots_select;
    //非选中显示Indicator
    private int unSelcetRes = R.drawable.shape_dots_default;
    //当前页的下标
    private int currrentPos;
    private boolean isStopScroll = false;

    /**
     *
     * @param context 上下文
     * @param vp viewpager
     * @param points 指示器
     */

    public BannerUtils(Context context, ViewPager vp, LinearLayout points){
        this.context = context;
        this.vp = vp;
        this.points = points;
    }

    /**
     * 图片轮播需要传入参数
     */
    public void build(List<BannerEntity> list) {

        destory();

        if (list.size() == 0) {
            //    this.setVisibility(GONE);
            return;
        }


        final int pointSize;
        pointSize = list.size();

        //判断是否清空 指示器点
        if (points.getChildCount() != 0) {
            points.removeAllViewsInLayout();
        }

        //初始化与个数相同的指示器点
        for (int i = 0; i < pointSize; i++) {
            View dot = new View(context);
            dot.setBackgroundResource(unSelcetRes);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DisplayUtil.dp2px(context, 6),
                    DisplayUtil.dp2px(context, 6));
            params.leftMargin = 13;
            dot.setLayoutParams(params);
            dot.setEnabled(false);
            // 添加轮播图三个点view
            points.addView(dot);
        }

        points.getChildAt(0).setBackgroundResource(selectRes);

        for (int i = 0; i < list.size(); i++) {
            ImageView mImageView = new ImageView(context);

            Glide.with(context)
                    .load(list.get(i).img)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bili_default_image_tv)
                    .dontAnimate()
                    .into(mImageView);
            imageViewList.add(mImageView);
        }

        //监听图片轮播，改变指示器状态
        vp.clearOnPageChangeListeners();
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {

                pos = pos % pointSize;
                currrentPos = pos;
                for (int i = 0; i < points.getChildCount(); i++) {
                    points.getChildAt(i).setBackgroundResource(unSelcetRes);
                }
                points.getChildAt(pos).setBackgroundResource(selectRes);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        Log.d("debugli","viewPager空闲状态");
                        if (isStopScroll) {
                            startScroll();
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        Log.d("debugli","viewPager拖到状态");
                        stopScroll();
                        compositeSubscription.unsubscribe();
                        break;
                }
            }
        });

        BannerAdapter bannerAdapter = new BannerAdapter(imageViewList);
        vp.setAdapter(bannerAdapter);
        bannerAdapter.notifyDataSetChanged();
        bannerAdapter.setmViewPagerOnItemClickListener(this);
        bannerAdapter.setViewPagerOnTouchListener(this);
        // 图片开始轮播
        startScroll();
    }

    /**
     * 设置轮播间隔时间
     *
     * @param time 轮播间隔时间，单位秒
     */
    public BannerUtils delayTime(int time) {
        this.delayTime = time;
        return this;
    }

    /**
     * 图片开始轮播
     */
    private void startScroll() {
        compositeSubscription = new CompositeSubscription();
        isStopScroll = false;

        Subscription subscription = Observable.timer(delayTime, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {

                    if (isStopScroll) {
                        return;
                    }
                    isStopScroll = true;
                    vp.setCurrentItem(vp.getCurrentItem() + 1);
                });
        compositeSubscription.add(subscription);
    }

    /**
     * 图片停止轮播
     */
    private void stopScroll() {
        isStopScroll = true;
    }

    /**
     * 取消订阅
     */
    public void destory() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    /**
     * 设置Points资源 Res
     *
     * @param selectRes 选中状态
     * @param unselcetRes 非选中状态
     */
    public void setPointsRes(int selectRes, int unselcetRes) {
        this.selectRes = selectRes;
        this.unSelcetRes = unselcetRes;
    }

    @Override
    public void onItemClick() {

    }


    @Override
    public void onTouchDown() {
        Log.d("debugli","viewPager按下状态");
        stopScroll();
        compositeSubscription.unsubscribe();
    }

    @Override
    public void onTouchUp() {
        Log.d("debugli","viewPager抬起状态");
        if (isStopScroll) {
            startScroll();
        }
    }
}
