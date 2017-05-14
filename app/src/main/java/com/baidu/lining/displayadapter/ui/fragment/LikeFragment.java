package com.baidu.lining.displayadapter.ui.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.api.BangumiAppIndexInfo;
import com.baidu.lining.displayadapter.api.BangumiRecommendInfo;
import com.baidu.lining.displayadapter.api.RetrofitHelper;
import com.baidu.lining.displayadapter.base.BaseFragment;
import com.baidu.lining.displayadapter.utils.DisplayUtil;
import com.baidu.lining.displayadapter.widget.banner.BannerAdapter;
import com.baidu.lining.displayadapter.widget.banner.BannerEntity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/5/10.
 */
public class LikeFragment extends BaseFragment implements MediaPlayer.OnCompletionListener,BannerAdapter.ViewPagerOnItemClickListener {

   public final String videoUrl = "http://128.1.226.186/file/MP4/mp413/%E5%A6%B2%E5%B7%B1-%E5%85%A8%E4%B8%96%E7%95%8C%E6%88%91%E5%8F%AA%E5%96%9C%E6%AC%A2%E4%BD%A0[68mtv.com].mp4";

    private int position;

    @BindView(R.id.like_video)
    public VideoView video;
    @BindView(R.id.like_vp)
    public ViewPager vp;
    @BindView(R.id.layout_banner_points_group)
    LinearLayout points;
    @BindView(R.id.main_title)
    public TextView title;

    List<BannerEntity> bannerList = new ArrayList<BannerEntity>();
    private List<BangumiAppIndexInfo.ResultBean.AdBean.HeadBean> banners = new ArrayList<>();
    private boolean isStopScroll = false;
    //默认轮播时间，10s
    private int delayTime = 10;
    //选中显示Indicator
    private int selectRes = R.drawable.shape_dots_select;
    //非选中显示Indicator
    private int unSelcetRes = R.drawable.shape_dots_default;
    //当前页的下标
    private int currrentPos;
    private CompositeSubscription compositeSubscription;
    private List<ImageView> imageViewList = new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_like;
    }


    @Override
    public void initView() {
        title.setText("Like");

        MediaController controller = new MediaController(activity);
        controller.setVisibility(View.INVISIBLE);
        video.setMediaController(controller);
        Uri uri = Uri.parse(videoUrl);
        video.setVideoURI(uri);
        video.setSystemUiVisibility(View.GONE);
        video.setOnCompletionListener(this);
        video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        video.clearFocus();
        video.start();
    }

    /**
     * 设置轮播间隔时间
     *
     * @param time 轮播间隔时间，单位秒
     */
    public LikeFragment delayTime(int time) {

        this.delayTime = time;
        return this;
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

    /**
     * 图片轮播需要传入参数
     */
    public void build(List<BannerEntity> list) {

        destory();

        if (list.size() == 0) {
        //    this.setVisibility(GONE);
            return;
        }

        bannerList = new ArrayList<>();
        bannerList.addAll(list);
        final int pointSize;
        pointSize = bannerList.size();

        if (pointSize == 2) {
            bannerList.addAll(list);
        }
        //判断是否清空 指示器点
        if (points.getChildCount() != 0) {
            points.removeAllViewsInLayout();
        }

        //初始化与个数相同的指示器点
        for (int i = 0; i < pointSize; i++) {
            View dot = new View(activity);
            dot.setBackgroundResource(unSelcetRes);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DisplayUtil.dp2px(activity, 6),
                    DisplayUtil.dp2px(activity, 6));
            params.leftMargin = 13;
            dot.setLayoutParams(params);
            dot.setEnabled(false);
            // 添加轮播图三个点view
            points.addView(dot);
        }

        points.getChildAt(0).setBackgroundResource(selectRes);

        for (int i = 0; i < bannerList.size(); i++) {
            ImageView mImageView = new ImageView(activity);

            Glide.with(getContext())
                    .load(bannerList.get(i).img)
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
                        if (isStopScroll) {
                            startScroll();
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
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

        //图片开始轮播
        startScroll();
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

    public void destory() {

        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void initData() {
        super.initData();
        RetrofitHelper.getBangumiAPI()
                .getBangumiAppIndex()
                .flatMap(new Func1<BangumiAppIndexInfo, Observable<BangumiRecommendInfo>>() {

                    @Override
                    public Observable<BangumiRecommendInfo> call(BangumiAppIndexInfo bangumiAppIndexInfo) {

                        banners.addAll(bangumiAppIndexInfo.getResult().getAd().getHead());

                        return RetrofitHelper.getBangumiAPI().getBangumiRecommended();
                    }
                })
                .map(new Func1<BangumiRecommendInfo, List<BangumiRecommendInfo.ResultBean>>() {
                    @Override
                    public List<BangumiRecommendInfo.ResultBean> call(BangumiRecommendInfo bangumiRecommendInfo) {
                        return bangumiRecommendInfo.getResult();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {

                    delayTime(5);
                    Observable.from(banners)
                            .forEach(bannersBean -> bannerList.add(new BannerEntity(
                                    bannersBean.getLink(), bannersBean.getTitle(), bannersBean.getImg())));
                    build(bannerList);
                }, throwable -> {
        });
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        video.start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            video.pause();
            position = video.getCurrentPosition();
            Toast.makeText(getActivity(), "当前进度:" + position, Toast.LENGTH_SHORT).show();
            Log.d("debugli","第一次退出");
        }else{
            video.seekTo(position);
            video.start();
            Log.d("debugli","第一次进来");
        }
    }

    @Override
    public void onItemClick() {

    }
}
