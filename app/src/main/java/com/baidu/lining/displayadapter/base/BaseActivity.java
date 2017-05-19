package com.baidu.lining.displayadapter.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/5/18.
 */
public abstract class BaseActivity extends AutoLayoutActivity{

    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        bind = ButterKnife.bind(this);
        initView();
        initData();
    }

    public void initData(){

    }

    public void initView(){

    }

    public abstract int getLayoutRes();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
