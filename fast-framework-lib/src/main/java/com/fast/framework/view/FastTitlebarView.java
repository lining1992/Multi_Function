/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view;

import com.fast.framework.R;
import com.fast.framework.style.skin.SkinnableActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 标题栏视图
 * <p>
 * Created by lishicong on 2016/10/25.
 */
public class FastTitlebarView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private View mView;

    public ImageView mImageViewBack;
    public TextView mTextViewTitle;
    public CheckBox mCheckBoxNight;

    public FastTitlebarView(Context context) {
        super(context);
        this.mContext = context;
        this.init();
    }

    public FastTitlebarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.init();
    }

    public FastTitlebarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.init();
    }

    @Override
    public void setBackgroundColor(int color) {
        mView.setBackgroundColor(color);
    }

    private void init() {

        LayoutInflater factory = LayoutInflater.from(mContext);
        mView = factory.inflate(R.layout.fast_view_titlebar, this, false);

        mImageViewBack = (ImageView) mView.findViewById(R.id.view_title_bar_l_iv);
        mTextViewTitle = (TextView) mView.findViewById(R.id.view_title_bar_m_tv);
        mCheckBoxNight = (CheckBox) mView.findViewById(R.id.view_title_bar_r_cb);

        mImageViewBack.setOnClickListener(this);
        mCheckBoxNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        ((SkinnableActivity) mContext).setDayNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        // Night mode is not active, we're in day time
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        ((SkinnableActivity) mContext).setDayNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        // Night mode is active, we're at night!
                        break;

                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        // We don't know what mode we're in, assume notnight
                        break;
                    default:
                        break;
                }
            }
        });

        this.addView(mView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.view_title_bar_l_iv) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        }
    }

}
