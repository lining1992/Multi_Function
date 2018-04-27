/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fast.framework.base.BaseBean;
import com.fast.framework.support.L;

import android.content.Context;
import android.net.Uri;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lishicong on 2016/12/12.
 */

public class RetrofitFactory {

    public static final String BASE_URL = NetworkConfig.getInstance().getHttpServerUrl();

    private static String AGENT;

    private static Context mContext;
    private static Retrofit mRetrofit;

    public static synchronized void init(Context context, String agent) {

        AGENT = agent;
        mContext = context;

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(StringConverterFactory.create());
        builder.addConverterFactory(FastJsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        builder.client(getClient(context));
        mRetrofit = builder.build();
    }

    private static OkHttpClient getClient(Context context) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 连接超时设置，单位毫秒
        builder.connectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        builder.readTimeout(10 * 1000, TimeUnit.MILLISECONDS);

        builder.cookieJar(new CookieManager(context));
        builder.addInterceptor(new UserAgentInterceptor(AGENT));
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }

    public static <T> T createService(final Class<T> service) {
        try {
            return mRetrofit.create(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> Observable.Transformer<NetworkModel<T>, T> handleResult() {

        L.e("hm + errmsg111 = 我运行到这里了");
        return new Observable.Transformer<NetworkModel<T>, T>() {
            @Override
            public Observable<T> call(Observable<NetworkModel<T>> baseModelObservable) {
                return baseModelObservable.flatMap(new Func1<NetworkModel<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(NetworkModel<T> networkModel) {

                        // 网络延时3秒，用于测试dialog样式显示
                        // try {
                        //      Thread.sleep(3000L);
                        // } catch (InterruptedException e) {
                        //      e.printStackTrace();
                        // }

                        L.e("hm + errmsg111 = " + networkModel.getCode());

                        if (networkModel.getCode() != ExceptionHandle.SUCCESS_CODE) {

                            L.e("hm + errmsg111 = " + networkModel.getCode());

                            return Observable.error(
                                    new NetworkException(networkModel.getMsg(), networkModel.getCode()));
                        } else {

                            L.e("hm + errmsg111 = " + networkModel.getCode());

                            if (networkModel.getResult() != null) {


                                L.e("hm + errmsg111 = " + networkModel.getCode());

                                ((BaseBean) networkModel.getResult()).code = networkModel.getCode();
                                ((BaseBean) networkModel.getResult()).msg = networkModel.getMsg();
                            }

                            L.e("hm + errmsg111 = " + networkModel.getResult());
                            return createData(networkModel.getResult());
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable<T> createData(final T result) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    L.e("hm + errmsg222 = " + result);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 压缩并上传单张图片
     *
     * @param context
     * @param uri
     *
     * @return
     */
    public static Observable<FileBean> uploadImgAfterCompress(Context context, final Uri uri) {
        return BitmapUtil.compressImgToFile(context, FileUtil.getPath(context, uri)).flatMap(
                new Func1<File, Observable<NetworkModel<FileBean>>>() {
                    @Override
                    public Observable<NetworkModel<FileBean>> call(File file) {
                        return uploadFile(file);
                    }
                }).doOnNext(new Action1<NetworkModel<FileBean>>() {
            @Override
            public void call(NetworkModel<FileBean> fileBeanNetworkModel) {
                // 加入uri作为TAG
                fileBeanNetworkModel.getResult().setUri(uri);
            }
        }).compose(RetrofitFactory.<FileBean>handleResult());
    }

    /**
     * 压缩并上传多张图片
     */
    public static Observable<ArrayList<FileBean>> uploadImgsAfterCompress(final Context context,
                                                                          final ArrayList<Uri> uris) {
        return Observable.from(uris).flatMap(new Func1<Uri, Observable<File>>() {
            @Override
            public Observable<File> call(Uri uri) {
                return BitmapUtil.compressImgToFile(context, FileUtil.getPath(context, uri));
            }
        }).flatMap(new Func1<File, Observable<ArrayList<File>>>() {
            ArrayList<File> files = new ArrayList<>();

            @Override
            public Observable<ArrayList<File>> call(File file) {
                files.add(file);
                if (files.size() != uris.size()) {
                    return null;
                }
                return Observable.just(files);
            }
        }).flatMap(new Func1<ArrayList<File>, Observable<NetworkModel<ArrayList<FileBean>>>>() {
            @Override
            public Observable<NetworkModel<ArrayList<FileBean>>> call(ArrayList<File> files) {
                return uploadFiles(files);
            }
        }).compose(RetrofitFactory.<ArrayList<FileBean>>handleResult());
    }

    /**
     * 上传单个文件
     */
    public static Observable<NetworkModel<FileBean>> uploadFile(File file) {
        // create upload service client
        FileUploadService service = RetrofitFactory.createService(FileUploadService.class);

        MultipartBody.Part body = prepareFilePart("upload", file);

        return service.uploadFile(body);
    }

    /**
     * 上传多个文件
     */
    public static Observable<NetworkModel<ArrayList<FileBean>>> uploadFiles(List<File> files) {

        FileUploadService service = RetrofitFactory.createService(FileUploadService.class);

        ArrayList<MultipartBody.Part> parts = new ArrayList<>();

        for (File file : files) {
            parts.add(prepareFilePart("upload", file));
        }

        return service.uploadFiles(parts);
    }

    /**
     * 获得文件的RequestBody
     */
    public static MultipartBody.Part prepareFilePart(String partName, File file) {

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

        return body;
    }

}
