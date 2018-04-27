/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.base;

import com.fast.framework.R;
import com.fast.framework.FastFragment;
import com.fast.framework.style.guide.button.DoneButton;
import com.fast.framework.style.guide.GuideConfiguration;
import com.fast.framework.style.guide.GuideItemList;
import com.fast.framework.style.guide.GuideViewHider;
import com.fast.framework.style.guide.GuideViewPagerIndicator;
import com.fast.framework.style.guide.button.GuideViewWrapper;
import com.fast.framework.style.guide.button.NextButton;
import com.fast.framework.style.guide.button.PreviousButton;
import com.fast.framework.style.guide.button.SkipButton;
import com.fast.framework.style.guide.color.GuideBackgroundView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by lishicong on 2016/12/6.
 */

public abstract class GuideFragment extends FastFragment {

    private ViewPager viewPager;
    private GuideFragmentPagerAdapter adapter;
    private GuideConfiguration configuration;
    private GuideItemList responsiveItems = new GuideItemList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configuration = configuration();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fast_fragment_guide, container, false);

        adapter = new GuideFragmentPagerAdapter(getFragmentManager());

        viewPager = (ViewPager) view.findViewById(R.id.f_guide_view_pager);
        viewPager.setAdapter(adapter);

        // -- Inflate the bottom layout -- //

        FrameLayout bottomFrame = (FrameLayout) view.findViewById(R.id.f_guide_bottom_frame);
        View.inflate(mActivity, configuration.getBottomLayoutResId(), bottomFrame);

        // -- Add buttons to responsiveItems -- //
        // addViewWrapper() will filter out any that aren't in the current layout

        SkipButton skip = new SkipButton(view.findViewById(R.id.f_guide_bottom_btn_skip));
        addViewWrapper(skip, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeWelcomeScreen();
            }
        });

        PreviousButton prev = new PreviousButton(view.findViewById(R.id.f_guide_bottom_btn_prev));
        addViewWrapper(prev, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToPreviousPage();
            }
        });

        NextButton next = new NextButton(view.findViewById(R.id.f_guide_bottom_btn_next));
        addViewWrapper(next, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToNextPage();
            }
        });

        DoneButton done = new DoneButton(view.findViewById(R.id.f_guide_bottom_btn_done));
        addViewWrapper(done, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeWelcomeScreen();
            }
        });

        GuideViewPagerIndicator indicator = (GuideViewPagerIndicator) view.findViewById(
                R.id.f_guide_bottom_pager_indicator);
        if (indicator != null) {
            responsiveItems.add(indicator);
        }

        GuideBackgroundView background = (GuideBackgroundView) view.findViewById(R.id.f_guide_background_view);

        GuideViewHider hider = new GuideViewHider(view.findViewById(R.id.f_guide_root));
        hider.setOnViewHiddenListener(new GuideViewHider.OnViewHiddenListener() {
            @Override
            public void onViewHidden() {
                completeWelcomeScreen();
            }
        });

        responsiveItems.addAll(background, hider, configuration.getPages());

        responsiveItems.setup(configuration);

        viewPager.addOnPageChangeListener(responsiveItems);
        viewPager.setCurrentItem(configuration.firstPageIndex());

        responsiveItems.onPageSelected(viewPager.getCurrentItem());

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewPager != null) {
            viewPager.clearOnPageChangeListeners();
        }
    }

    /**
     * Helper method to add button to list,
     * checks for null first
     */
    private void addViewWrapper(GuideViewWrapper wrapper, View.OnClickListener onClickListener) {
        if (wrapper.getView() != null) {
            wrapper.setOnClickListener(onClickListener);
            responsiveItems.add(wrapper);
        }
    }

    private boolean scrollToNextPage() {
        if (!canScrollToNextPage()) {
            return false;
        }
        viewPager.setCurrentItem(getNextPageIndex());
        return true;
    }

    private boolean scrollToPreviousPage() {
        if (!canScrollToPreviousPage()) {
            return false;
        }
        viewPager.setCurrentItem(getPreviousPageIndex());
        return true;
    }

    private int getNextPageIndex() {
        return viewPager.getCurrentItem() + 1;
    }

    private int getPreviousPageIndex() {
        return viewPager.getCurrentItem() + -1;
    }

    private boolean canScrollToNextPage() {
        return getNextPageIndex() <= configuration.lastViewablePageIndex();
    }

    private boolean canScrollToPreviousPage() {
        return getPreviousPageIndex() >= configuration.firstPageIndex();
    }

    /**
     * Closes the activity and saves it as completed.
     * A subsequent call to WelcomeScreenHelper.show() would not show this again,
     * unless the key is changed.
     */
    protected void completeWelcomeScreen() {
        setWelcomeScreenResult(Activity.RESULT_OK);
        mActivity.finish();
        if (configuration.getExitAnimation() != GuideConfiguration.NO_ANIMATION_SET) {
            mActivity.overridePendingTransition(R.anim.fast_guide_none, configuration.getExitAnimation());
        }
    }

    /**
     * Closes the activity, doesn't save as completed.
     * A subsequent call to WelcomeScreenHelper.show() would show this again.
     */
    protected void cancelWelcomeScreen() {
        setWelcomeScreenResult(Activity.RESULT_CANCELED);
        mActivity.finish();
    }

    @Override
    public void onBackPressed() {

        // Scroll to previous page and return if back button navigates
        if (configuration.getBackButtonNavigatesPages() && scrollToPreviousPage()) {
            return;
        }

        if (configuration.getCanSkip() && configuration.getBackButtonSkips()) {
            completeWelcomeScreen();
        } else {
            cancelWelcomeScreen();
        }

    }

    private void setWelcomeScreenResult(int resultCode) {
        Intent intent = mActivity.getIntent();
        mActivity.setResult(resultCode, intent);
    }

    protected abstract GuideConfiguration configuration();

    private class GuideFragmentPagerAdapter extends FragmentPagerAdapter {

        public GuideFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return configuration.createFragment(position);
        }

        @Override
        public int getCount() {
            return configuration.pageCount();
        }
    }
}
