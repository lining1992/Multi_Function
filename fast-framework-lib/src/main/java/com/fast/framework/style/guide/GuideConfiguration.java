/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import com.fast.framework.R;
import com.fast.framework.style.guide.color.BackgroundColor;
import com.fast.framework.style.guide.color.ColorHelper;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

/**
 * Created by lishicong on 2016/12/5.
 */
public class GuideConfiguration {

    public static final int NO_ANIMATION_SET = -1;

    private Builder builder;

    private GuidePageList pages;

    public GuideConfiguration(Builder builder) {
        this.builder = builder;

        this.pages = new GuidePageList();
        pages.addAll(builder.pages);

        if (pageCount() == 0) {
            throw new IllegalStateException("0 pages; at least one page must be added");
        }

        if (getSwipeToDismiss()) {
            pages.add(new FragmentGuidePage() {
                @Override
                protected Fragment fragment() {
                    return new Fragment();
                }
            }.background(pages.getBackgroundColor(getContext(), pageCount() - 1)));
        }

    }

    /**
     * Get context
     *
     * @return The context the builder was initialized with
     */
    public Context getContext() {
        return builder.context;
    }

    /**
     * Creates the fragment at the specified index
     *
     * @param index the index of the fragment
     *
     * @return fragment
     */
    public Fragment createFragment(int index) {
        return pages.get(index).createFragment();
    }

    /**
     * Get the background colors for the pages
     *
     * @return array of background colors
     */
    public BackgroundColor[] getBackgroundColors() {
        return pages.getBackgroundColors(getContext());
    }

    /**
     * Get the total number of pages, will be +1 if swipeToDismiss is enabled
     *
     * @return total number of pages
     */
    public int pageCount() {
        return pages.size();
    }

    /**
     * Get the number of viewable pages, not affected by swipeToDismiss
     *
     * @return number of viewable pages
     */
    public int viewablePageCount() {
        return getSwipeToDismiss() ? pageCount() - 1 : pageCount();
    }

    /**
     * @return list of pages
     */
    public GuidePageList getPages() {
        return pages;
    }

    /**
     * Whether or not back button should skip
     *
     * @return backButtonSkips
     */
    public boolean getBackButtonSkips() {
        return builder.backButtonSkips;
    }

    /**
     * Whether or not back button should navigate through pages
     *
     * @return backButtonNavigatesPages
     */
    public boolean getBackButtonNavigatesPages() {
        return builder.backButtonNavigatesPages;
    }

    /**
     * Typeface path for skip button
     *
     * @return path to typeface in assets folder
     */
    public String getSkipButtonTypefacePath() {
        return builder.skipButtonTypefacePath;
    }

    /**
     * Typeface path for done button
     *
     * @return path to typeface in assets folder
     */
    public String getDoneButtonTypefacePath() {
        return builder.doneButtonTypefacePath;
    }

    /**
     * Default typeface path for titles
     *
     * @return path to typeface in assets folder
     */
    public String getDefaultTitleTypefacePath() {
        return builder.defaultTitleTypefacePath;
    }

    /**
     * Default typeface path for descriptions
     *
     * @return path to typeface in assets folder
     */
    public String getDefaultDescriptionTypefacePath() {
        return builder.defaultDescriptionTypefacePath;
    }

    /**
     * True if can skip, false if not
     *
     * @return canSkip
     */
    public boolean getCanSkip() {
        return builder.canSkip;
    }

    /**
     * Check if swipeToDismiss is enabled
     * Returns false if SDK_INT is less than 11
     *
     * @return swipeToDismiss
     */
    public boolean getSwipeToDismiss() {
        return builder.swipeToDismiss && Build.VERSION.SDK_INT >= 11;
    }

    /**
     * Get the index of the first page, dependent
     * on if the layout is RTL
     *
     * @return first page index
     */
    public int firstPageIndex() {
        return 0;
    }

    /**
     * Get the index of the last page, dependent
     * on if the layout is RTL
     *
     * @return last page index
     */
    public int lastPageIndex() {
        return pages.size() - 1;
    }

    /**
     * Get the index of the last viewable page,
     * dependent on if the layout is RTL
     *
     * @return last viewable page index
     */
    public int lastViewablePageIndex() {
        return getSwipeToDismiss() ? Math.abs(lastPageIndex() - 1) : lastPageIndex();
    }

    /**
     * Get the resource id of the exit animation
     *
     * @return animation resource id
     */

    @AnimRes
    public int getExitAnimation() {
        return builder.exitAnimationResId;
    }

    /**
     * Whether or not buttons should animate
     *
     * @return animate buttons
     */
    public boolean getAnimateButtons() {
        return builder.animateButtons;
    }

    /**
     * Whether or not the next button should be shown
     *
     * @return show next button
     */
    public boolean getShowNextButton() {
        return builder.showNextButton;
    }

    /**
     * Whether or not the previous button should be shown
     *
     * @return show previous button
     */
    public boolean getShowPrevButton() {
        return builder.showPrevButton;
    }

    /**
     * Get the layout res to be used for bottom of the welcome screen
     *
     * @return layout resource id
     */
    @LayoutRes
    public int getBottomLayoutResId() {
        return R.layout.fast_fragment_guide_bottom;
    }

    public static class Builder {

        private Context context;

        private GuidePageList pages = new GuidePageList();
        private boolean canSkip = true;
        private boolean backButtonSkips = true;
        private boolean backButtonNavigatesPages = true;
        private BackgroundColor defaultBackgroundColor;
        private boolean swipeToDismiss = false;
        private int exitAnimationResId = NO_ANIMATION_SET;
        private String skipButtonTypefacePath = null;
        private String doneButtonTypefacePath = null;
        private String defaultTitleTypefacePath = null;
        private String defaultDescriptionTypefacePath = null;
        private boolean animateButtons = true;
        private boolean showNextButton = true;
        private boolean showPrevButton = true;

        /**
         * Creates a new Builder
         *
         * @param context Context object
         */
        public Builder(Context context) {
            this.context = context;
            initDefaultBackgroundColor(this.context);
        }

        private void initDefaultBackgroundColor(Context context) {
            // Default background color
            final int standardBackgroundColor = ColorHelper.getColor(context, R.color.fast_framework_white);

            // AppCompat colorPrimary
            int defaultBackgroundColor = ColorHelper.resolveColorAttribute(context, R.attr.colorPrimary,
                                                                           standardBackgroundColor);

            // Android system colorPrimary
            if (defaultBackgroundColor == standardBackgroundColor
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                defaultBackgroundColor = ColorHelper.resolveColorAttribute(context, android.R.attr.colorPrimary,
                                                                           defaultBackgroundColor);
            }

            this.defaultBackgroundColor = new BackgroundColor(defaultBackgroundColor, standardBackgroundColor);
        }

        /**
         * Builds the configuration
         *
         * @return A {@link GuideConfiguration} with the parameters set on this object
         */
        public GuideConfiguration build() {
            return new GuideConfiguration(this);
        }

        /**
         * Enables or disables swipe to dismiss (disabled by default)
         *
         * @param swipeToDismiss True to enable swipe to dismiss, false to disable it
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder swipeToDismiss(boolean swipeToDismiss) {
            this.swipeToDismiss = swipeToDismiss;
            return this;
        }

        /**
         * Sets whether or not the welcome screen can be skipped.
         * Skipping is allowed by default
         *
         * @param canSkip True to allow skipping, false to disable it
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder canSkip(boolean canSkip) {
            this.canSkip = canSkip;
            return this;
        }

        /**
         * Only applies if skipping is allowed. Sets whether or not the back button can skip the welcome screen.
         * This is enabled by default.
         *
         * @param backButtonSkips True to allow the back button to skip the welcome screen, false to disable it
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder backButtonSkips(boolean backButtonSkips) {
            this.backButtonSkips = backButtonSkips;
            return this;
        }

        /**
         * Set whether or not pressing the back button will move to the previous page in the welcome screen.
         * This is true by default
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder backButtonNavigatesPages(boolean navigatesPages) {
            this.backButtonNavigatesPages = navigatesPages;
            return this;
        }

        /**
         * Set whether or not the buttons fade out/in when changing visibilty
         *
         * @param animateButtons True to animate buttons, false to not
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder animateButtons(boolean animateButtons) {
            this.animateButtons = animateButtons;
            return this;
        }

        /**
         * Set the visibility of the next button
         *
         * @param showNextButton Whether or not to show the next button
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder showNextButton(boolean showNextButton) {
            this.showNextButton = showNextButton;
            return this;
        }

        /**
         * Sets the visibility of the previous button (navigates back through the pages).
         * This shows in the same spot as the skip button. If this welcome screen can be skipped,
         * setting this to true will hide the skip button on all but the first page.
         *
         * @param showPrevButton Whether or not to show the previous button
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder showPrevButton(boolean showPrevButton) {
            this.showPrevButton = showPrevButton;
            return this;
        }

        /**
         * Set the path to a typeface (in assets) to be used for the skip button
         *
         * @param typefacePath The path to a typeface file in assets folder
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder skipButtonTypefacePath(String typefacePath) {
            this.skipButtonTypefacePath = typefacePath;
            return this;
        }

        /**
         * Set the path to a typeface (in assets) to be used for the done button
         *
         * @param typefacePath The path to a typeface file in assets folder
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder doneButtonTypefacePath(String typefacePath) {
            this.doneButtonTypefacePath = typefacePath;
            return this;
        }

        /**
         * Set the path to a typeface (in assets) to be used by default for titles
         *
         * @param typefacePath The path to a typeface file in assets folder
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder defaultTitleTypefacePath(String typefacePath) {
            this.defaultTitleTypefacePath = typefacePath;
            return this;
        }

        /**
         * Set the path to a typeface (in assets) to be used by default for descriptions
         *
         * @param typefacePath The path to a typeface file in assets folder
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder defaultDescriptionTypefacePath(String typefacePath) {
            this.defaultDescriptionTypefacePath = typefacePath;
            return this;
        }

        /**
         * Set the animation that is used when the welcome screen closes
         *
         * @param exitAnimationResId The resource id of the animation to use
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder exitAnimation(@AnimRes int exitAnimationResId) {
            this.exitAnimationResId = exitAnimationResId;
            return this;
        }

        /**
         * Adds a page, uses the default background color
         *
         * @param page The page to add
         *
         * @return this Builder object to allow method calls to be chained
         */
        public Builder page(GuidePage page) {
            page.setIndex(pages.size());
            if (!page.backgroundIsSet()) {
                page.background(defaultBackgroundColor);
            }
            pages.add(page);
            return this;
        }

    }
}
