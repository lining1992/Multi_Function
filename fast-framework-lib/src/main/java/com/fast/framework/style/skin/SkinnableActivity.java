package com.fast.framework.style.skin;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.fast.framework.style.skin.view.Skinnable;

/**
 * 夜间模式的基类
 */
public class SkinnableActivity extends AppCompatActivity implements LayoutInflaterFactory {

    private SkinnableViewInflater mSkinnableViewInflater;
    private SkinnableCallback mSkinnableCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (mSkinnableViewInflater == null) {
            mSkinnableViewInflater = new SkinnableViewInflater();
        }
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;
        final boolean inheritContext = isPre21 && shouldInheritContext((ViewParent) parent);
        return mSkinnableViewInflater.createView(parent, name, context, attrs, inheritContext, isPre21, true,
                                                 VectorEnabledTintResources.shouldBeUsed());
    }

    public void setSkinnableCallback(SkinnableCallback skinnableCallback) {
        mSkinnableCallback = skinnableCallback;
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View) || ViewCompat.isAttachedToWindow(
                    (View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    public void setDayNightMode(@AppCompatDelegate.NightMode int nightMode) {
        final boolean isPost21 = Build.VERSION.SDK_INT >= 21;

        if (mSkinnableCallback != null) {
            mSkinnableCallback.beforeApplyDayNight();
        }

        getDelegate().setLocalNightMode(nightMode);

        if (isPost21) {
            applyDayNightForStatusBar();
            applyDayNightForActionBar();
        }

        View decorView = getWindow().getDecorView();
        applyDayNightForView(decorView);

        if (mSkinnableCallback != null) {
            mSkinnableCallback.onApplyDayNight();
        }
    }

    private void applyDayNightForView(View view) {
        if (view instanceof Skinnable) {
            Skinnable skinnable = (Skinnable) view;
            if (skinnable.isSkinnable()) {
                skinnable.applyDayNight();
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                applyDayNightForView(parent.getChildAt(i));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyDayNightForStatusBar() {
        TypedArray a = getTheme().obtainStyledAttributes(0, new int[] {android.R.attr.statusBarColor});
        int color = a.getColor(0, 0);
        getWindow().setStatusBarColor(color);
        a.recycle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void applyDayNightForActionBar() {
        TypedArray a = getTheme().obtainStyledAttributes(0, new int[] {android.R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    public interface SkinnableCallback {
        void beforeApplyDayNight();

        void onApplyDayNight();
    }
}
