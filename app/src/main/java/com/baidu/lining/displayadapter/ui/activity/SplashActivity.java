package com.baidu.lining.displayadapter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.baidu.lining.displayadapter.R;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/14.
 */
public class SplashActivity extends AutoLayoutActivity{

    @BindView(R.id.splash_iv)
    ImageView splash_iv;
    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        animation();
    }

    private void init() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
