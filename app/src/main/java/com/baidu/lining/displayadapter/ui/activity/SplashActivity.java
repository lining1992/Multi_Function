package com.baidu.lining.displayadapter.ui.activity;

import android.content.Intent;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/14.
 */
public class SplashActivity extends BaseActivity{

    @BindView(R.id.splash_iv)
    ImageView splash_iv;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash_app;
    }

    @Override
    public void initData() {
        super.initData();
        animation();
    }

    private void init() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    this.overridePendingTransition(0,0);
                    finish();
                });
    }

    private void animation() {
        // 去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AlphaAnimation animation = new AlphaAnimation(0f,1.0f);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                init();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash_iv.startAnimation(animation);
    }
}
