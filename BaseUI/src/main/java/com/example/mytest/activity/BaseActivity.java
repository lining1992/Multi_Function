package com.example.mytest.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mytest.R;
import com.example.mytest.interfaces.BaseView;
import com.example.mytest.utils.LayoutInflater;
import com.example.mytest.utils.SnackbarUtil;
import com.example.mytest.utils.ToastUtil;
import com.example.mytest.utils.ViewUtil;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * 创建人：v_lining05
 * 创建时间：2018/1/27
 */
public abstract class BaseActivity extends RxAppCompatActivity implements BaseView<ActivityEvent>{

    View mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());
        ButterKnife.bind(this);

        initData(savedInstanceState);
        initTitleView();
        initContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setContentView(inflateLayout(getLayoutResId()));
        mContentView = view;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    protected void initTitleView() {
    }

    protected void initContentView() {
    }

    public abstract int getLayoutResId();

    protected final View getContentView() {
        return mContentView;
    }

    @Override
    public void showView(View v) {
        ViewUtil.showView(v);
    }

    @Override
    public void hideView(View v) {
        ViewUtil.hideView(v);
    }

    @Override
    public void goneView(View v) {
        ViewUtil.goneView(v);
    }

    @Override
    public void showImageView(ImageView iv, int resId) {
        ViewUtil.showImageView(iv,resId);
    }

    @Override
    public void showImageView(ImageView iv, Drawable drawable) {
        ViewUtil.showImageView(iv, drawable);
    }

    @Override
    public void hideImageView(ImageView iv) {
        ViewUtil.hideImageView(iv);
    }

    @Override
    public void goneImageView(ImageView iv) {
        ViewUtil.goneImageView(iv);
    }

    @Override
    public void showToast(String text) {
        ToastUtil.showToast(text);
    }

    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(int resId, Object... formatArgs) {
        showToast(getString(resId,formatArgs));
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void showSnackbar(@NonNull CharSequence text) {
        showSnackbar(text, LENGTH_SHORT);
    }

    @Override
    public void showSnackbar(@NonNull CharSequence text, int duration) {
        showSnackbar(text, duration, -1);
    }

    @Override
    public void showSnackbar(@NonNull CharSequence text, int duration, int textColor) {
        showSnackbar(text, duration, -1, textColor);
    }

    @Override
    public void showSnackbar(@NonNull CharSequence text, int duration, int bgColor, int textColor) {
        if (textColor == -1){
            textColor = getResources().getColor(R.color.color_text_primary);
        }
        SnackbarUtil.showSnackbar(getContentView(), text, duration, bgColor, textColor);
    }

    @Override
    public <T extends View> T inflateLayout(int layoutResId) {
        return LayoutInflater.inflate(this,layoutResId);
    }

    @Override
    public <T extends View> T inflateLayout(int layoutResId, @Nullable ViewGroup root) {
        return LayoutInflater.inflate(this,layoutResId,root);
    }

    @Override
    public <T extends View> T inflateLayout(int layoutResId, @Nullable ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.inflate(this,layoutResId,root,attachToRoot);
    }


    /**
     * fragment activity part
     */
    public final void addFragment(Fragment f, String tag) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().add(f, tag).commitAllowingStateLoss();
        }
    }

    public final void addFragment(int frameId, Fragment f) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().add(frameId, f).commitAllowingStateLoss();
        }
    }

    public final void addFragment(int frameId, Fragment f, String tag) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().add(frameId, f, tag).commitAllowingStateLoss();
        }
    }

    public final void replaceFragment(int frameId, Fragment f) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().replace(frameId, f).commitAllowingStateLoss();
        }
    }

    public final void replaceFragment(int frameId, Fragment f, String tag) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().replace(frameId, f, tag).commitAllowingStateLoss();
        }
    }

    public final void removeFragment(Fragment f) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
    }

}
