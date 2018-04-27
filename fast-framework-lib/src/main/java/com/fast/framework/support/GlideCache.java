/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support;

import java.io.File;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

import android.content.Context;
import android.os.Environment;

/**
 * Created by lishicong on 2017/5/2.
 */

public class GlideCache implements GlideModule {

    private static final int GLIDE_CACHE_SIZE = 100 * 1000 * 1000; // 设置缓存的大小为100M

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        // 设置磁盘缓存目录（和创建的缓存目录相同）
        File storageDirectory = Environment.getExternalStorageDirectory();
        String downloadDirectoryPath = storageDirectory + "/GlideCache";

        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, GLIDE_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
