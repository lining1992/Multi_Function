/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

import com.fast.framework.base.BaseBean;

import android.net.Uri;

/**
 * 文件上传类
 * <p>
 * Created by lishicong on 2016/12/13.
 */
public class FileBean extends BaseBean {

    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
