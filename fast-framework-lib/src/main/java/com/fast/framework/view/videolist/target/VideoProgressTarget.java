/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.target;

import java.io.File;

import com.fast.framework.view.CircularProgressBar;

import android.view.View;

public class VideoProgressTarget extends ProgressTarget<String, File> {
    private final CircularProgressBar progress;

    public VideoProgressTarget(VideoLoadTarget target, CircularProgressBar progress) {
        super(target);
        this.progress = progress;
    }

    @Override
    public float getGranualityPercentage() {
        return 0.1f; // this matches the format string for #text below
    }

    @Override
    protected void onConnecting() {
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(0);
    }

    @Override
    protected void onDownloading(long bytesRead, long expectedLength) {
        progress.setProgress((int) (100 * bytesRead / expectedLength));
    }

    @Override
    protected void onDownloaded() {
    }

    @Override
    protected void onDelivered() {
        progress.setVisibility(View.GONE);
    }
}
