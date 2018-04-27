/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.widget;

import com.fast.framework.R;
import com.fast.framework.view.CircularProgressBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by lishicong on 2016/10/31.
 */

public class PullToRefreshVideoView extends RelativeLayout {

    private Context mContext;

    private TextureVideoView mTextureVideoView;
    private ImageView mImageView;
    private CircularProgressBar mCircularProgressBar;

    public PullToRefreshVideoView(Context context) {
        super(context);
        this.mContext = context;
        this.init();
    }

    public PullToRefreshVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.init();
    }

    public PullToRefreshVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.init();
    }

    public TextureVideoView getVideoView() {
        return mTextureVideoView;
    }

    public ImageView getVideoCover() {
        return mImageView;
    }

    public CircularProgressBar getProgressBar() {
        return mCircularProgressBar;
    }

    private void init() {
        LayoutInflater factory = LayoutInflater.from(mContext);
        final View view = factory.inflate(R.layout.fast_view_pulltorefresh_video, this, false);

        mTextureVideoView = (TextureVideoView) view.findViewById(R.id.pull_to_refresh_video_view);
        mImageView = (ImageView) view.findViewById(R.id.pull_to_refresh_video_cover);
        mCircularProgressBar = (CircularProgressBar) view.findViewById(R.id.pull_to_refresh_video_progress);

        this.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

}
