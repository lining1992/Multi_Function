/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist;

import com.fast.framework.base.BaseBean;
import com.fast.framework.support.L;
import com.fast.framework.view.videolist.visibility.items.ListItem;
import com.fast.framework.view.videolist.widget.TextureVideoView;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by lishicong on 2016/10/27.
 */

public class VideoListItem extends BaseBean implements ListItem {

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;

    private int mState = STATE_IDLE;

    private String mCoverUrl;
    private String mVideoUrl;
    private String mVideoPath;
    private ImageView mVideoCover;
    private TextureVideoView mVideoView;
    private final Rect mCurrentViewRect = new Rect();

    public VideoListItem() {
    }

    public VideoListItem(String videoUrl, String coverUrl) {
        mCoverUrl = coverUrl;
        mVideoUrl = videoUrl;
    }

    public void setCoverUrl(String mCoverUrl) {
        this.mCoverUrl = mCoverUrl;
    }

    public void setVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    public String getCoverUrl() {
        return mCoverUrl;
    }

    public void bindView(TextureVideoView videoView, ImageView videoCover) {
        mVideoView = videoView;
        mVideoCover = videoCover;
    }

    public void setVideoPath(String videoPath) {
        mVideoPath = videoPath;
        if (videoPath != null) {
            mVideoView.setVideoPath(videoPath);
            if (mState == STATE_ACTIVED) {
                mVideoView.start();
            }
        }
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }

    @Override
    public int getVisibilityPercents(View currentView) {
        int percents = 100;

        currentView.getLocalVisibleRect(mCurrentViewRect);

        int height = currentView.getHeight();

        if (viewIsPartiallyHiddenTop()) {
            // view is partially hidden behind the top edge
            percents = (height - mCurrentViewRect.top) * 100 / height;
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = mCurrentViewRect.bottom * 100 / height;
        }

        return percents;
    }

    @Override
    public void setActive(View currentView, int newActiveViewPosition) {
        L.i("setActive " + newActiveViewPosition + " path " + mVideoPath);
        mState = STATE_ACTIVED;
        if (mVideoPath != null) {
            mVideoView.setVideoPath(mVideoPath);
            mVideoView.start();
        }
    }

    @Override
    public void deactivate(View currentView, int position) {
        L.i("deactivate " + position);
        mState = STATE_DEACTIVED;
        if (mVideoPath != null) {
            mVideoView.stop();
            mVideoCover.setVisibility(View.VISIBLE);
            mVideoCover.setAlpha(1.f);
        }
    }
}
