/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.glide;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Glide图片处理工具类
 * <p>
 * Created by lishicong on 2017/5/3.
 */

public class GlideImgManager {

    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        // 原生 API
        Glide.with(context).load(url).crossFade(1000).placeholder(emptyImg).error(erroImg).into(iv);
    }

    /**
     * 圆角图片
     *
     * @param context
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param radius   圆角
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int radius) {
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(
                new GlideRoundTransform(context, radius)).into(iv);
    }

    /**
     * 交叉淡入淡出、模糊度、缩放的图片
     *
     * @param context
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param duration 交叉淡入淡出时间
     * @param radius   模糊程度
     * @param sampling 图片缩放比例,默认“1”
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int duration,
                                   int radius, int sampling) {
        Glide.with(context).load(url).crossFade(duration).bitmapTransform(
                new BlurTransformation(context, radius, sampling)).into(iv);
    }

    /**
     * 圆形图片
     *
     * @param context
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader2(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context))
                .into(iv);
    }

    /**
     * 圆形、灰度处理
     *
     * @param context
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader3(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).bitmapTransform(
                new GlideCircleTransform(context), new GrayscaleTransformation(context)).into(iv);
    }

    /**
     * 通过url下载图片，返回圆形的bitmap
     *
     * @param context
     * @param url
     *
     * @return
     */
    public static Bitmap glideDownload(Context context, String url) {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context).load(url).asBitmap().transform(new GlideCircleTransform(context)).into(100,
                                                                                                                100)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
