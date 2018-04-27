/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.skin.view;

public interface Skinnable {
    /**
     * called by activity when UiMode changed
     */
    void applyDayNight();

    /**
     * indicate that if it can be changed by {@link com.fast.framework.style.skin.SkinnableActivity}
     *
     * @return true if it works
     */
    boolean isSkinnable();

    void setSkinnable(boolean skinnable);
}
