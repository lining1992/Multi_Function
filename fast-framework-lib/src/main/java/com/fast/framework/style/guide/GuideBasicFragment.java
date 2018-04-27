/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import com.fast.framework.R;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple fragment that shows an image, a heading, and a description.
 */
public class GuideBasicFragment extends Fragment implements GuidePage.OnChangeListener {

    public static final String KEY_DRAWABLE_ID = "drawable_id";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SHOW_ANIM = "show_anim";
    public static final String KEY_HEADER_TYPEFACE_PATH = "header_typeface";
    public static final String KEY_DESCRIPTION_TYPEFACE_PATH = "description_typeface";

    private ImageView imageView = null;
    private TextView titleView = null;
    private TextView descriptionView = null;
    private boolean showParallaxAnim = true;

    public static GuideBasicFragment newInstance(@DrawableRes int drawableId, String title, String description,
                                                 boolean showParallaxAnim, String headerTypefacePath,
                                                 String descriptionTypefacePath) {
        Bundle args = new Bundle();
        args.putInt(KEY_DRAWABLE_ID, drawableId);
        args.putString(KEY_TITLE, title);
        args.putString(KEY_DESCRIPTION, description);
        args.putBoolean(KEY_SHOW_ANIM, showParallaxAnim);
        args.putString(KEY_HEADER_TYPEFACE_PATH, headerTypefacePath);
        args.putString(KEY_DESCRIPTION_TYPEFACE_PATH, descriptionTypefacePath);
        GuideBasicFragment fragment = new GuideBasicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fast_fragment_guide_basic, container, false);

        Bundle args = getArguments();

        imageView = (ImageView) view.findViewById(R.id.f_guide_image);
        titleView = (TextView) view.findViewById(R.id.f_guide_title);
        descriptionView = (TextView) view.findViewById(R.id.f_guide_description);

        if (args == null) {
            return view;
        }

        showParallaxAnim = args.getBoolean(KEY_SHOW_ANIM, showParallaxAnim);

        imageView.setImageResource(args.getInt(KEY_DRAWABLE_ID));

        if (args.getString(KEY_TITLE) != null) {
            titleView.setText(args.getString(KEY_TITLE));
        }

        if (args.getString(KEY_DESCRIPTION) != null) {
            descriptionView.setText(args.getString(KEY_DESCRIPTION));
        }

        GuideUtils.setTypeface(titleView, args.getString(KEY_HEADER_TYPEFACE_PATH), getActivity());
        GuideUtils.setTypeface(descriptionView, args.getString(KEY_DESCRIPTION_TYPEFACE_PATH), getActivity());

        return view;
    }

    @Override
    public void onWelcomeScreenPageScrolled(int pageIndex, float offset, int offsetPixels) {
        if (showParallaxAnim && Build.VERSION.SDK_INT >= 11 && imageView != null) {
            imageView.setTranslationX(-offsetPixels * 0.8f);
        }
    }

    @Override
    public void onWelcomeScreenPageSelected(int pageIndex, int selectedPageIndex) {
        // Not used
    }

    @Override
    public void onWelcomeScreenPageScrollStateChanged(int pageIndex, int state) {
        // Not used
    }
}
