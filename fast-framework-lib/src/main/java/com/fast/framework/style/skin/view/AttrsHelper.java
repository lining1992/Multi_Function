/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.skin.view;

import android.content.res.TypedArray;
import android.util.SparseIntArray;

public class AttrsHelper {
    private SparseIntArray mResourceMap;
    private static final int VALUE_NOT_FOUND = -1;

    public AttrsHelper() {
        this.mResourceMap = new SparseIntArray();
    }

    public void storeAttributeResource(TypedArray a, int[] styleable) {
        int size = a.getIndexCount();
        for (int index = 0; index < size; index++) {
            int resourceId = a.getResourceId(index, VALUE_NOT_FOUND);
            int key = styleable[index];
            if (resourceId != VALUE_NOT_FOUND) {
                mResourceMap.put(key, resourceId);
            }
        }
    }

    public int getAttributeResource(int attr) {
        return mResourceMap.get(attr, VALUE_NOT_FOUND);
    }

}
