/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by lishicong on 2016/12/13.
 */

public interface FileUploadService {

    @Multipart
    @POST("/upload/file")
    Observable<NetworkModel<FileBean>> uploadFile(@Part MultipartBody.Part file);

    @Multipart
    @POST("/upload/files")
    Observable<NetworkModel<ArrayList<FileBean>>> uploadFiles(@Part List<MultipartBody.Part> files);

}
