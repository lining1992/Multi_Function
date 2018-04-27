/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.widget;

import com.fast.framework.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * 图片在文字顶部，图片和文字居中显示
 * <p>
 * Created by lishicong on 2016/12/8.
 */
public class FastRadioButton extends RadioButton {

    private static final int DEFAULT_WIDTH_HEIGHT = 20;

    private Drawable drawableTop;
    private Drawable drawableBottom;
    private Drawable drawableLeft;
    private Drawable drawableRight;
    private int mLeftWith;
    private int mLeftHeight;
    private int mTopWith;
    private int mTopHeight;
    private int mRightWith;
    private int mRightHeight;
    private int mBottomWith;
    private int mBottomHeight;
    private int mMarginLeft;
    private int mMarginTop;
    private int mMarginRight;
    private int mMarginBottom;

    public FastRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public FastRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FastRadioButton(Context context) {
        super(context);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FastRadioButton);
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.FastRadioButton_fast_drawableBottom) {
                    drawableBottom = a.getDrawable(attr);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableTop) {
                    drawableTop = a.getDrawable(attr);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableLeft) {
                    drawableLeft = a.getDrawable(attr);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableRight) {
                    drawableRight = a.getDrawable(attr);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableTopWith) {
                    mTopWith = (int) (a.getDimension(attr, DEFAULT_WIDTH_HEIGHT) + 0.5f);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableTopHeight) {
                    mTopHeight = (int) (a.getDimension(attr, DEFAULT_WIDTH_HEIGHT) + 0.5f);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableBottomWith) {
                    mBottomWith = (int) (a.getDimension(attr, DEFAULT_WIDTH_HEIGHT) + 0.5f);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableBottomHeight) {
                    mBottomHeight = (int) (a.getDimension(attr, DEFAULT_WIDTH_HEIGHT) + 0.5f);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableRightWith) {
                    mRightWith = (int) (a.getDimension(attr, DEFAULT_WIDTH_HEIGHT) + 0.5f);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableRightHeight) {
                    mRightHeight = (int) (a.getDimension(attr, DEFAULT_WIDTH_HEIGHT) + 0.5f);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableLeftWith) {
                    mLeftWith = (int) (a.getDimension(attr, DEFAULT_WIDTH_HEIGHT) + 0.5f);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableLeftHeight) {
                    mLeftHeight = (int) (a.getDimension(attr, DEFAULT_WIDTH_HEIGHT) + 0.5f);

                } else if (attr == R.styleable.FastRadioButton_fast_drawableMarginLeft) {
                    mMarginLeft = (int) (a.getDimension(attr, 0));

                } else if (attr == R.styleable.FastRadioButton_fast_drawableMarginTop) {
                    mMarginTop = (int) (a.getDimension(attr, 0));

                } else if (attr == R.styleable.FastRadioButton_fast_drawableMarginRight) {
                    mMarginRight = (int) (a.getDimension(attr, 0));

                } else if (attr == R.styleable.FastRadioButton_fast_drawableMarginBottom) {
                    mMarginBottom = (int) (a.getDimension(attr, 0));

                }
            }
            a.recycle();
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
        }
    }

    /**
     * 设置Drawable定义的大小
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, mLeftWith <= 0 ? left.getIntrinsicWidth() : mLeftWith,
                           mLeftHeight <= 0 ? left.getMinimumHeight() : mLeftHeight);
        }
        if (right != null) {
            right.setBounds(0, 0, mRightWith <= 0 ? right.getIntrinsicWidth() : mRightWith,
                            mRightHeight <= 0 ? right.getMinimumHeight() : mRightHeight);
        }
        if (top != null) {
            top.setBounds(0, mMarginTop, mTopWith <= 0 ? top.getIntrinsicWidth() : mTopWith,
                          mTopHeight <= 0 ? top.getMinimumHeight() : mTopHeight + mMarginTop);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mBottomWith <= 0 ? bottom.getIntrinsicWidth() : mBottomWith,
                             mBottomHeight <= 0 ? bottom.getMinimumHeight() : mBottomHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

}

/**
 * by use
 * <com.fast.framework.widget.FastRadioButton
 * android:id="@+id/f_home_bottom_radio"
 * android:layout_width="match_parent"
 * android:layout_height="match_parent"
 * android:layout_weight="1.0"
 * android:background="@color/fast_framework_color_fff9f9f9"
 * android:button="@null"
 * android:gravity="center"
 * android:text="@string/app_home_tab_msg"
 * android:textColor="@drawable/f_home_bottom_font_selector"
 * android:textSize="@dimen/fast_framework_sp_note"
 * button:fast_drawableMarginTop="6dp"
 * button:fast_drawableTop="@drawable/f_home_tab_msg_selector"
 * button:fast_drawableTopHeight="24dp"
 * button:fast_drawableTopWith="24dp" />
 */