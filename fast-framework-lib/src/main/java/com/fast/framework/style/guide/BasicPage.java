/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

/**
 * A page with a large image, header, and description.
 */
public class BasicPage extends GuidePage<BasicPage> {

    private int drawableResId;
    private String title;
    private String description;
    private boolean showParallax = true;
    private String titleTypefacePath = null;
    private String descriptionTypefacePath = null;

    /**
     * A page with a large image, header, and description
     *
     * @param drawableResId Resource id of drawable to show
     * @param title         Title, shown in large font
     * @param description   Description, shown beneath title
     */
    public BasicPage(@DrawableRes int drawableResId, String title, String description) {
        this.drawableResId = drawableResId;
        this.title = title;
        this.description = description;
    }

    /**
     * Set the typeface of the header
     *
     * @param typefacePath The path to a typeface in the assets folder
     *
     * @return This BasicPage object to allow method calls to be chained
     */
    public BasicPage titleTypeface(String typefacePath) {
        this.titleTypefacePath = typefacePath;
        return this;
    }

    /**
     * Set the typeface of the description
     *
     * @param typefacePath The path to a typeface in the assets folder
     *
     * @return This BasicPage object to allow method calls to be chained
     */
    public BasicPage descriptionTypeface(String typefacePath) {
        this.descriptionTypefacePath = typefacePath;
        return this;
    }

    @Override
    public void setup(GuideConfiguration config) {
        super.setup(config);

        if (this.titleTypefacePath == null) {
            titleTypeface(config.getDefaultTitleTypefacePath());
        }

        if (this.descriptionTypefacePath == null) {
            descriptionTypeface(config.getDefaultDescriptionTypefacePath());
        }

    }

    @Override
    public Fragment fragment() {
        return GuideBasicFragment.newInstance(drawableResId, title, description, showParallax, titleTypefacePath,
                                              descriptionTypefacePath);
    }

}
