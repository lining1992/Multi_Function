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
import com.baidu.lining.displayadapter.widget.banner.BannerUtils;
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

    //默认轮播时间，10s
    private int delayTime = 10;

    private BannerUtils bannerUtils;

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

        bannerUtils = new BannerUtils(activity,vp,points);
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

                    bannerUtils.delayTime(5);
                    Observable.from(banners)
                            .forEach(bannersBean -> bannerList.add(new BannerEntity(
                                    bannersBean.getLink(), bannersBean.getTitle(), bannersBean.getImg())));
                    bannerUtils.build(bannerList);
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
            position = video.getCurrentPosition();
            video.pause();
            Toast.makeText(getActivity(), "当前进度:" + position, Toast.LENGTH_SHORT).show();
        }else{
            video.seekTo(position);
            video.start();
        }
    }

    @Override
    public void onItemClick() {

    }
}
