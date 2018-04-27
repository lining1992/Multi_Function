/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by lishicong on 2016/12/2.
 */

public class FastManager {

    public static final String ACTIVITY_FULL_SCREEN = "activity_full_screen";

    /**
     * 获取要打开的Fragment的Intent.
     *
     * @param context
     * @param clazz
     *
     * @return
     */
    public static Intent getIntent(Context context, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(context, FastActivity.class);
        intent.setData(Uri.parse(clazz.getName()));
        return intent;
    }

    /**
     * 获取要打开的Fragment的Intent，并设置为全屏模式
     *
     * @param context
     * @param clazz
     *
     * @return
     */
    public static Intent getIntentFullScreen(Context context, Class<?> clazz) {
        Intent intent = getIntent(context, clazz);
        intent.putExtra(ACTIVITY_FULL_SCREEN, true);
        return intent;
    }

    /**
     * 通过URI分发给相关的Fragment来处理.
     *
     * @param context
     * @param intent  要打开的Fragment的Intent
     * @param clazz   未找到指定的Fragment时，使用一个默认的Fragment.
     *
     * @return
     */
    public static FastFragment dispatch(FragmentActivity context, Intent intent, Class<?> clazz) {

        FastFragment fragment;
        String fragmentName;

        if (intent != null && intent.getData() != null) {

            Uri uri = intent.getData();
            fragmentName = uri.toString();
        } else {
            fragmentName = clazz.getName();
        }

        fragment = (FastFragment) Fragment.instantiate(context, fragmentName, intent.getExtras());
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fast_framelayout, fragment, fragmentName)
                .commit();

        return fragment;
    }
}
