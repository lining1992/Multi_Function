package com.example.commonlibrary.android.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/9/22 17:42
 */
public class FragmentLoader {

    private final FragmentManager mFragmentManager;
    private final SparseArray<Factory<? extends Fragment>> mFragmentFactories;
    private final SparseIntArray mContainerIds;
    private final SparseArray<String> mTags;
    private final SparseArray<Fragment> mFragments;
    private final SparseArray<Fragment> mLoadedFragments;

    public FragmentLoader(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mFragmentFactories = new SparseArray<>();
        mContainerIds = new SparseIntArray();
        mTags = new SparseArray<>();
        mFragments = new SparseArray<>();
        mLoadedFragments = new SparseArray<>();
    }

    public <F extends Fragment> FragmentLoader add(int index, int containerId, Class<F> cls, Factory<F> factory) {
        mFragmentFactories.put(index, factory);
        mContainerIds.put(index, containerId);
        mTags.put(index, createTag(index, cls));
        return this;
    }

    public <F extends Fragment> F load(int index) {
        // noinspection unchecked
        F f = (F) mFragments.get(index);
        int containerId = mContainerIds.get(index);
        Fragment loadedFragment = mLoadedFragments.get(containerId);

        if (f != null && loadedFragment == f) {
            return f;
        }


        String tag = mTags.get(index);

        if (f != null) {
            if (loadedFragment == null) {
                mFragmentManager
                        .beginTransaction()
                        .show(f)
                        .commit();
            } else {
                mFragmentManager
                        .beginTransaction()
                        .hide(loadedFragment)
                        .show(f)
                        .commit();
            }

            mLoadedFragments.put(containerId, f);
            return f;
        }

        Fragment fragment = mFragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            Factory<? extends Fragment> factory = mFragmentFactories.get(index);
            // noinspection unchecked
            f = (F) factory.create();
            mTags.put(index, tag);
            mFragments.put(index, f);
            if (loadedFragment == null) {
                mFragmentManager
                        .beginTransaction()
                        .add(containerId, f, tag)
                        .commit();
            } else {
                mFragmentManager
                        .beginTransaction()
                        .hide(loadedFragment)
                        .add(containerId, f, tag)
                        .commit();
            }

            mLoadedFragments.put(containerId, f);
            return f;
        }

        // noinspection unchecked
        f = (F) fragment;
        if (f != loadedFragment) {
            if (loadedFragment == null) {
                mFragmentManager
                        .beginTransaction()
                        .show(f)
                        .commit();
                mLoadedFragments.put(containerId, f);
            } else {
                mFragmentManager
                        .beginTransaction()
                        .hide(loadedFragment)
                        .show(f)
                        .commit();
                mLoadedFragments.put(containerId, f);
            }
        }

        return f;
    }

    public void unload(int index) {
        int containerId = mContainerIds.get(index);
        Fragment loadedFragment = mLoadedFragments.get(containerId);
        if (loadedFragment == null) {
            return;
        }

        mFragmentManager.beginTransaction()
                .hide(loadedFragment)
                .commit();
        mLoadedFragments.delete(containerId);
    }

    private String createTag(int index, Class<? extends Fragment> cls) {
        return "tag_" + cls.getCanonicalName() + "_" + index;
    }
}
