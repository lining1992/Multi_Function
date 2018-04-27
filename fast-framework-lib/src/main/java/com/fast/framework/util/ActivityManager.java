/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * Created by yushengjin on 2017/11/23.
 */

public final class ActivityManager {

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        if (activity != null && !activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public static void removeActivity(Activity activity) {
        if (activity != null && activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public static void exitApp(boolean isKill) {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
        if (isKill) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
