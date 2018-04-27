/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support.permissionslib;

public interface OnRequestPermissionsCallBack {

    void onGrant();

    void onDenied(String permission);
}
