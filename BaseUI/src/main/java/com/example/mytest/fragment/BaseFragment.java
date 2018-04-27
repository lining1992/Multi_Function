package com.example.mytest.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytest.utils.LayoutInflater;
import com.example.mytest.utils.ToastUtil;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.example.mytest.interfaces.BaseView;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;

/**
 * 创建人：v_lining05
 * 创建时间：2018/1/27
 */
public abstract class BaseFragment extends RxFragment implements BaseView<FragmentEvent> {

    private Activity mActicity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActicity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData(savedInstanceState);
        initTitleView();
        initContentView();
        ButterKnife.bind(this,mActicity);
        return inflateLayout(getLayoutResId(),container,false);
    }

    public abstract int getLayoutResId();

    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    protected void initTitleView() {
    }

    protected void initContentView() {
    }

    @Override
    public boolean isFinishing() {
        return mActicity == null || mActicity.isFinishing();
    }

    @Override
    public void finish() {
        if (mActicity != null) {
            mActicity.finish();
        }
    }

    @Override
    public void showView(View v) {

    }

    @Override
    public void hideView(View v) {

    }

    @Override
    public void goneView(View v) {

    }

    @Override
    public void showImageView(ImageView iv, int resId) {

    }

    @Override
    public void showImageView(ImageView iv, Drawable drawable) {

    }

    @Override
    public void hideImageView(ImageView iv) {

    }

    @Override
    public void goneImageView(ImageView iv) {

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
        showToast(getString(resId, formatArgs));
    }

    @Override
    public void showSnackbar(@NonNull CharSequence text) {

    }

    @Override
    public void showSnackbar(@NonNull CharSequence text, int duration) {

    }

    @Override
    public void showSnackbar(@NonNull CharSequence text, int duration, int textColor) {

    }

    @Override
    public void showSnackbar(@NonNull CharSequence text, int duration, int bgColor, int textColor) {

    }

    @Override
    public <T extends View> T inflateLayout(int layoutResId) {
        return LayoutInflater.inflate(mActicity,layoutResId);
    }

    @Override
    public <T extends View> T inflateLayout(int layoutResId, @Nullable ViewGroup root) {
        return LayoutInflater.inflate(mActicity,layoutResId,root);
    }

    @Override
    public <T extends View> T inflateLayout(int layoutResId, @Nullable ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.inflate(mActicity,layoutResId,root,attachToRoot);
    }

}
