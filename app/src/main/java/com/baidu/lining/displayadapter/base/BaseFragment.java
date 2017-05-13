package com.baidu.lining.displayadapter.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.lining.displayadapter.ui.activity.MainActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/5/12.
 */
public abstract class BaseFragment extends Fragment{

    public MainActivity activity;
    private Unbinder bind;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(getLayoutRes(), container, false);
        bind = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }



    public void initData(){
    }

    public void initView(){

    }

    public abstract int getLayoutRes();


    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
