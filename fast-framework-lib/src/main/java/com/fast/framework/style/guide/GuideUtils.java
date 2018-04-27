/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

public class GuideUtils {

    private static final String TAG = GuideUtils.class.getName();

    public static boolean setTypeface(TextView textView, String typefacePath, Context context) {
        if (typefacePath != null && !typefacePath.equals("")) {
            try {
                textView.setTypeface(Typeface.createFromAsset(context.getAssets(), typefacePath));
                return true;
            } catch (Exception e) {
                Log.w(TAG, "Error setting typeface");
            }
        }
        return false;
    }

    public static boolean isIndexBeforeLastPage(int index, int lastPageIndex) {
        return index < lastPageIndex;
    }

}
