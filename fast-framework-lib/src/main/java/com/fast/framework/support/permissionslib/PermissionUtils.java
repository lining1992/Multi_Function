/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support.permissionslib;

import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionUtils {

    /**
     * 检查系统版本
     *
     * @return
     */
    public static boolean canMakeSmores() {

        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

    }

    static int verifyPermissions(int[] grantResults) {

        if (grantResults.length < 1) {
            return -1;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return i;
            }
        }

        return -1;
    }
}
