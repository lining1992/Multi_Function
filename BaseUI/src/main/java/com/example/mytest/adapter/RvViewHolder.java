package com.example.mytest.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joy.ui.adapter.ExRvViewHolder;
import com.joy.ui.extension.view.fresco.FrescoImage;

/**
 * Created by Daisw on 2017/11/6.
 */

public class RvViewHolder<T> extends ExRvViewHolder<T> {

    private SparseArray<View> views;

    public RvViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    @Override
    public void invalidateItemView(int position, T t) {
    }

    public <V extends View> V getView(@IdRes int resId) {
        View v = views.get(resId);
        if (v != null) {
            return (V) v;
        } else {
            v = getItemView().findViewById(resId);
            views.put(resId, v);
            return (V) v;
        }
    }

    public void setText(@IdRes int textResId, @StringRes int resId, Object... formatArgs) {
        try {
            setText(textResId, getItemView().getContext().getString(resId, formatArgs));
        } catch (Resources.NotFoundException e) {
            setText(textResId, Integer.toString(resId));
        }
    }

    public void setText(@IdRes int textResId, CharSequence text) {
        TextView tv = getView(textResId);
        tv.setText(text);
    }

    public void setImage(@IdRes int imageResId, String url) {
        View iv = getView(imageResId);
        if (iv instanceof FrescoImage) {
            FrescoImage frescoImage = (FrescoImage) iv;
            frescoImage.setImageURI(url);
        }
    }

    public void setImage(@IdRes int imageResId, String url, int w, int h) {
        View iv = getView(imageResId);
        if (iv instanceof FrescoImage) {
            FrescoImage frescoImage = (FrescoImage) iv;
            frescoImage.resize(url, w, h);
        }
    }

    public void setImageDrawable(@IdRes int imageResId, Drawable drawable){
        View iv = getView(imageResId);
        if(iv instanceof ImageView){
            ((ImageView)iv).setImageDrawable(drawable);
        }
    }

    public void setBackgroundResource(@IdRes int vId, @DrawableRes int resId) {
        getView(vId).setBackgroundResource(resId);
    }

    public void show(@IdRes int... resIds) {
        for (int resId : resIds) {
            showView(getView(resId));
        }
    }

    public void hide(@IdRes int... resIds) {
        for (int resId : resIds) {
            hideView(getView(resId));
        }
    }

    public void gone(@IdRes int... resIds) {
        for (int resId : resIds) {
            goneView(getView(resId));
        }
    }
}
