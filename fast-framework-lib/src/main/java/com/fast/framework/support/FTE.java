/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support;

import com.fast.framework.base.BaseFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * FragmentTransactionExtended
 * <p>
 * Created by lishicong on 2016/12/8.
 */

public class FTE {

    private Context mContext;
    private int resourceId;
    private BaseFragment mContent;

    public FTE(Context context, int pager) {
        this.mContext = context;
        this.resourceId = pager;
    }

    public BaseFragment getContent() {
        return mContent;
    }

    public void switchContent(BaseFragment to) {

        Fragment from = mContent;

        if (mContent != to) {

            mContent = to;

            FragmentTransaction transaction =
                    ((AppCompatActivity) this.mContext).getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            if (!to.isAdded()) { // 先判断是否被add过

                if (from != null) { // 隐藏当前的fragment，add下一个到Activity中
                    transaction.hide(from).add(resourceId, to).commit();
                } else {
                    transaction.add(resourceId, to).commit();
                }
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
