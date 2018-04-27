/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import java.util.Random;

/**
 * Created by lishicong on 2017/6/29.
 */

public class RandomUtil {

    private RandomUtil() {
    }

    /**
     * 生成指定范围的随机数
     *
     * @param min
     * @param max
     *
     * @return
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
