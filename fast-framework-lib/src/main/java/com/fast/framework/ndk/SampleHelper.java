/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.ndk;

/**
 * Created by lishicong on 2017/2/27.
 */

public class SampleHelper {

    static {
        System.loadLibrary("FastFrameworkJniLib");
    }

    public static native String getHelloJni();
}
