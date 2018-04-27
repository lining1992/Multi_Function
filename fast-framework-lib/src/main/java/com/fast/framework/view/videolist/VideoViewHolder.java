/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.fast.framework.view.videolist.target.VideoLoadTarget;
import com.fast.framework.view.videolist.target.VideoProgressTarget;
import com.fast.framework.view.videolist.widget.PullToRefreshVideoView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 所有ViewHolder的基类，支持列表滚动视频播放和夜音模式时的列表view刷新
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {

    /**
     * 切换夜间模式时，RecyclerView只能更新可见部分，无法刷新整个列表
     * 此处缓存需要进行切换的列表视图组件
     */
    protected Set<View> mSkinnableViewsCache;

    private PullToRefreshVideoView mVideoView;
    private VideoProgressTarget mProgressTarget;
    private VideoLoadTarget mVideoTarget;

    public VideoViewHolder(View itemView) {
        super(itemView);
    }

    protected void initVideoView(PullToRefreshVideoView videoView) {
        this.mVideoView = videoView;
        mVideoTarget = new VideoLoadTarget(videoView.getVideoView(), videoView.getVideoCover());
        mProgressTarget = new VideoProgressTarget(mVideoTarget, videoView.getProgressBar());
    }

    public void bind(int position, VideoListItem item) {

        if (mVideoView != null) {
            item.bindView(mVideoView.getVideoView(), mVideoView.getVideoCover());
            mVideoTarget.bind(item);
            mProgressTarget.setModel(item.getVideoUrl());
            Glide.with(itemView.getContext()).using(VideoListGlideModule.getOkHttpUrlLoader(), InputStream.class).load(
                    new GlideUrl(item.getVideoUrl())).as(File.class).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(
                    mProgressTarget);
        }
    }
}
