package com.example.mytest.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Daisw on 16/8/4.
 */

public class LayoutInflater {

    public static <T extends View> T inflate(@NonNull Context context, @LayoutRes int layoutResId) {

        return (T) android.view.LayoutInflater.from(context).inflate(layoutResId, null);
    }

    public static <T extends View> T inflate(@NonNull Context context, @LayoutRes int layoutResId, @Nullable ViewGroup root) {

        return (T) android.view.LayoutInflater.from(context).inflate(layoutResId, root);
    }

    public static <T extends View> T inflate(@NonNull Context context, @LayoutRes int layoutResId, @Nullable ViewGroup root, boolean attachToRoot) {

        return (T) android.view.LayoutInflater.from(context).inflate(layoutResId, root, attachToRoot);
    }
}
