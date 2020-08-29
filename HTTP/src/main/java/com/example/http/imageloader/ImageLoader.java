//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.http.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public final class ImageLoader {
    public ImageLoader() {
    }

    public static void load(Context context, ImageView imageView, String url) {
        load(context, imageView, url, 0);
    }

    public static void load(Context context, ImageView imageView, String url, int placeHolder) {
//        RequestOptions options = (new RequestOptions()).placeholder(placeHolder).error(placeHolder).dontTransform();
//        Glide.with(context.getApplicationContext()).load(url).apply(options).into(imageView);
        Glide.with(context.getApplicationContext()).load(url)
                .placeholder(placeHolder).error(placeHolder).dontTransform().into(imageView);
    }

    public static void load(Context context, ImageView imageView, String url, BitmapTransformation transformation) {
        load(context, imageView, url, 0, transformation);
    }

    public static void load(Context context, ImageView imageView, String url, int placeHolder, BitmapTransformation transformation) {
//        (new RequestOptions()).placeholder(placeHolder).error(placeHolder);
//        RequestOptions options = RequestOptions.bitmapTransform(transformation);
//        Glide.with(context.getApplicationContext()).load(url).apply(options).into(imageView);
        Glide.with(context.getApplicationContext()).load(url).placeholder(placeHolder)
                .error(placeHolder).bitmapTransform(transformation).into(imageView);
    }

    public static void loadCircle(Context context, ImageView imageView, String url) {
        loadCircle(context, imageView, url, 0);
    }

    public static void loadCircle(Context context, ImageView imageView, String url, int placeHolder) {
//        (new RequestOptions()).placeholder(placeHolder).error(placeHolder);
//        RequestOptions options = RequestOptions.bitmapTransform(new CircleCrop());
//        Glide.with(context.getApplicationContext()).load(url).apply(options).into(imageView);
        Glide.with(context.getApplicationContext()).load(url)
                .placeholder(placeHolder).error(placeHolder)
                .bitmapTransform(new GlideCircleTransform(context)).into(imageView);
    }

    public static void loadRoundedCorner(Context context, ImageView imageView, String url, int roundingRadius) {
        loadRoundedCorner(context, imageView, url, 0, roundingRadius);
    }

    public static void loadRoundedCorner(Context context, ImageView imageView, String url, int placeHolder, int roundingRadius) {
//        (new RequestOptions()).placeholder(placeHolder).error(placeHolder);
//        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(roundingRadius));
//        Glide.with(context.getApplicationContext()).load(url).apply(options).into(imageView);
        Glide.with(context.getApplicationContext()).load(url)
                .placeholder(placeHolder).error(placeHolder)
                .bitmapTransform(new RoundedCornersTransformation(context, roundingRadius, 0))
                .into(imageView);
    }
}
