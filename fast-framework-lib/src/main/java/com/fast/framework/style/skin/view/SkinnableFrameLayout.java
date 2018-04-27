/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.skin.view;

import com.fast.framework.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SkinnableFrameLayout extends FrameLayout implements Skinnable {

    private AttrsHelper mAttrsHelper;

    public SkinnableFrameLayout(Context context) {
        this(context, null);
    }

    public SkinnableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mAttrsHelper = new AttrsHelper();

        TypedArray a;

        a = context.obtainStyledAttributes(attrs, R.styleable.SkinnableView, defStyleAttr, 0);
        mAttrsHelper.storeAttributeResource(a, R.styleable.SkinnableView);
        a.recycle();
    }

    @Override
    public void applyDayNight() {
        Context context = getContext();
        int key;

        key = R.styleable.SkinnableView[R.styleable.SkinnableView_android_background];
        int backgroundResource = mAttrsHelper.getAttributeResource(key);
        if (backgroundResource > 0) {
            Drawable background = ContextCompat.getDrawable(context, backgroundResource);
            if (Build.VERSION.SDK_INT < 16) {
                setBackgroundDrawable(background);
            } else {
                setBackground(background);
            }
        }
    }

    @Override
    public boolean isSkinnable() {
        return true;
    }

    @Override
    public void setSkinnable(boolean skinnable) {
    }
}
