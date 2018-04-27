/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.skin.view;

import com.fast.framework.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class SkinnableButton extends AppCompatButton implements Skinnable {

    private AttrsHelper mAttrsHelper;

    public SkinnableButton(Context context) {
        this(context, null);
    }

    public SkinnableButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public SkinnableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrsHelper = new AttrsHelper();

        TypedArray a;

        a = context.obtainStyledAttributes(attrs, R.styleable.SkinnableView, defStyleAttr, 0);
        mAttrsHelper.storeAttributeResource(a, R.styleable.SkinnableView);
        a.recycle();

        a = context.obtainStyledAttributes(attrs, R.styleable.SkinnableButton, defStyleAttr, 0);
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
            setBackgroundDrawable(background);
        }

        key = R.styleable.SkinnableView[R.styleable.SkinnableView_backgroundTint];
        int backgroundTintResource = mAttrsHelper.getAttributeResource(key);
        if (backgroundTintResource > 0) {
            ColorStateList backgroundTint = ContextCompat.getColorStateList(context, backgroundTintResource);
            setSupportBackgroundTintList(backgroundTint);
        }

        key = R.styleable.SkinnableTextView[R.styleable.SkinnableButton_android_textColor];
        int textColorResource = mAttrsHelper.getAttributeResource(key);
        if (textColorResource > 0) {
            ColorStateList color = ContextCompat.getColorStateList(context, textColorResource);
            setTextColor(color);
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
