/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support.permissionslib;

import com.fast.framework.support.L;

import android.Manifest;
import android.content.Context;

public class Constants {

    public static final String GRANT = "grant";
    public static final String DENIED = "denied";

    /**
     * 6.0申请权限示例
     *
     * @param context
     */
    void requestPermissionExample(Context context) {

        new PermissionCompat.Builder(context).addPermissions(new String[] {Manifest.permission.RECORD_AUDIO})
                .addPermissionRationale("需要录音权限").addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
            @Override
            public void onGrant() {
                // 授权
            }

            @Override
            public void onDenied(String permission) {
                // 否认
                L.e(permission + "Denied");
            }

        }).build().request();
    }

}
