package com.baidu.lining.displayadapter.api;

import com.baidu.lining.displayadapter.App;
import com.baidu.lining.displayadapter.utils.CommonUtil;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * Retrofit帮助类
 */
public class RetrofitHelper {

  private static OkHttpClient mOkHttpClient;


  static {
    initOkHttpClient();
  }

  public static BangumiService getBangumiAPI() {

    return createApi(BangumiService.class, ApiConstants.BANGUMI_BASE_URL);
  }



  /**
   * 根据传入的baseUrl，和api创建retrofit
   */
  private static <T> T createApi(Class<T> clazz, String baseUrl) {

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(mOkHttpClient)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    return retrofit.create(clazz);
  }


  /**
   * 初始化OKHttpClient,设置缓存,设置超时时间,设置打印日志,设置UA拦截器
   */
  private static void initOkHttpClient() {

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    if (mOkHttpClient == null) {
      synchronized (RetrofitHelper.class) {
        if (mOkHttpClient == null) {
          //设置Http缓存
          Cache cache = new Cache(new File(App.getInstance()
              .getCacheDir(), "HttpCache"), 1024 * 1024 * 10);

          mOkHttpClient = new OkHttpClient.Builder()
              .cache(cache)
              .addInterceptor(interceptor)
              .addNetworkInterceptor(new CacheInterceptor())
              .retryOnConnectionFailure(true)
              .connectTimeout(30, TimeUnit.SECONDS)
              .writeTimeout(20, TimeUnit.SECONDS)
              .readTimeout(20, TimeUnit.SECONDS)
              .addInterceptor(new UserAgentInterceptor())
              .build();
        }
      }
    }
  }


  /**
   * 添加UA拦截器，B站请求API需要加上UA才能正常使用
   */
  private static class UserAgentInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

      Request originalRequest = chain.request();
      Request requestWithUserAgent = originalRequest.newBuilder()
          .removeHeader("User-Agent")
          .addHeader("User-Agent", ApiConstants.COMMON_UA_STR)
          .build();
      return chain.proceed(requestWithUserAgent);
    }
  }

  /**
   * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
   */
  private static class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

      // 有网络时 设置缓存超时时间1个小时
      int maxAge = 60 * 60;
      // 无网络时，设置超时为1天
      int maxStale = 60 * 60 * 24;
      Request request = chain.request();
      if (CommonUtil.isNetworkAvailable(App.getInstance())) {
        //有网络时只从网络获取
        request = request.newBuilder()
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build();
      } else {
        //无网络时只从缓存中读取
        request = request.newBuilder()
            .cacheControl(CacheControl.FORCE_CACHE)
            .build();
      }
      Response response = chain.proceed(request);
      if (CommonUtil.isNetworkAvailable(App.getInstance())) {
        response = response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", "public, max-age=" + maxAge)
            .build();
      } else {
        response = response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
            .build();
      }
      return response;
    }
  }
}
