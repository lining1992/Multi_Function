/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.ndk;

/**
 * Created by lishicong on 2017/2/24.
 */

public class AESHelper {

    static {
        System.loadLibrary("FastFrameworkJniLib");
    }

    private AESHelper() {
    }

    /**
     * @return
     */
    public static native String getAESKey();
}
