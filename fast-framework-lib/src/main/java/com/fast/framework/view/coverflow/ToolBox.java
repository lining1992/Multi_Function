/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.coverflow;

public abstract class ToolBox {

    /**
     * Get length of line between points A and B.
     *
     * @param ax point A x coordinate
     * @param ay point A y coordinate
     * @param bx point B x coordinate
     * @param by point B y coordinate
     *
     * @return length
     */
    public static float getLineLength(float ax, float ay, float bx, float by) {
        float vx = bx - ax;
        float vy = by - ay;
        return (float) Math.sqrt(vx * vx + vy * vy);
    }
}