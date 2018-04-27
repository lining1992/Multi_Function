/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support;

import com.fast.framework.ndk.AESHelper;
import com.fast.framework.ndk.SampleHelper;

import android.content.Context;

/**
 * 输出相关状态信息
 * <p>
 * Created by lishicong on 2017/3/2.
 */

public class InfoHelper {

    /**
     * 打印应用信息
     *
     * @param context
     */
    public static void print(Context context) throws Exception {
        // jni
        L.i("apk jni load start");
        L.i("apk jni load:" + SampleHelper.getHelloJni());
        L.i("apk jni load success");
        // 签名值
        L.i("apk sign value:[" + Sign.getSignature(context) + "]");

        // AES key
        L.i("apk aes key:" + AESHelper.getAESKey());
        String message = "com.baidu.adu.smartop.driver:v1111";
        L.i("apk aes message source:" + message);
        String encrypt = AESCrypt.encrypt(AESHelper.getAESKey(), message);
        L.i("apk aes message encrypt:" + encrypt);
        String decrypt = AESCrypt.decrypt(AESHelper.getAESKey(), encrypt);
        L.i("apk aes message decrypt:" + decrypt);
    }

    // ---------------------------------------------------------------------------
    // View->ToolWindows->Terminal 生成native .h文件
    // javah -jni com.fast.framework.ndk.AESHelper
    // ----------------------------------------------------------------------------
}
