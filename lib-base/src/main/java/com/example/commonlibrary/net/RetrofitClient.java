package com.example.commonlibrary.net;

import android.content.Context;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.bean.NetworkListModel;
import com.example.commonlibrary.bean.NetworkModel;
import com.example.commonlibrary.interceptor.CacheInterceptor;
import com.example.commonlibrary.manager.ApplicationManager;
import com.example.commonlibrary.utils.LogUtil;
import com.example.commonlibrary.utils.TrustAllCertsUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class RetrofitClient {

    private static final Object OBJECT_EMPTY = new Object();
    private static String BASE_URL = "https://www.baidu.com";
    private static Retrofit mRetrofit;
    private static String AGENT = "agent";

    public static synchronized void init(Context context) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(getClient(context))
                .build();
    }

    private static OkHttpClient getClient(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //设置Http缓存
        Cache cache = new Cache(new File(ApplicationManager.getInstance().getContext()
                .getCacheDir(), "HttpCache"), 1024 * 1024 * 10);
        return new OkHttpClient.Builder()
                .cache(cache)
                .sslSocketFactory(TrustAllCertsUtil.createSSLSocketFactory(),
                        TrustAllCertsUtil.createX509TrustManager())
                .hostnameVerifier(new TrustAllCertsUtil.TrustAllHostnameVerifier(BASE_URL))
                .addNetworkInterceptor(new CacheInterceptor())
                // 连接超时设置，单位毫秒
                .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .cookieJar(new CookieManager(context))
                .addInterceptor(new UserAgentInterceptor(AGENT))
                .build();
    }

    public static <T> T createService(final Class<T> service) {
        try {
            return mRetrofit.create(service);
        } catch (Exception e) {
            LogUtil.e(e);
            e.printStackTrace();
        }
        return null;
    }

    public static <T> ObservableTransformer<NetworkModel<T>, T> handleResultSync() {
        return new ObservableTransformer<NetworkModel<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<NetworkModel<T>> upstream) {
                return upstream.flatMap(new Function<NetworkModel<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(NetworkModel<T> networkModel) throws Exception {
                        LogUtil.d("debugli", "===networkModel.getCode===" + networkModel.getCode());
                        if (networkModel.getCode() != ExceptionHandle.SUCCESS_CODE) {
                            return Observable.error(
                                    new NetworkException(networkModel.getMsg(), networkModel.getCode()));
                        } else {
                            if (networkModel.getResult() != null) {
                                if (networkModel.getResult() instanceof BaseBean) {
                                    ((BaseBean) networkModel.getResult()).code = networkModel.getCode();
                                    ((BaseBean) networkModel.getResult()).msg = networkModel.getMsg();
                                }
                            }
                            return createData(networkModel.getResult());
                        }
                    }
                });
            }
        };
    }

    private static <T> Observable<T> createData(final T result) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) {
                try {
                    emitter.onNext(result);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    LogUtil.e(e);
                }
            }
        });
    }

    public static <T> ObservableTransformer<NetworkListModel<T>, List<T>> handleListResultSync() {
        return new ObservableTransformer<NetworkListModel<T>, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(Observable<NetworkListModel<T>> upstream) {
                return upstream.flatMap(new Function<NetworkListModel<T>, ObservableSource<List<T>>>() {
                    @Override
                    public ObservableSource<List<T>> apply(NetworkListModel<T> networkModel) {
                        if (networkModel.getCode() != ExceptionHandle.SUCCESS_CODE) {
                            return Observable.error(
                                    new NetworkException(networkModel.getMsg(), networkModel.getCode()));
                        } else {
                            return createLisData(networkModel.getResult());
                        }
                    }
                });
            }
        };
    }

    private static <T> Observable<List<T>> createLisData(final NetworkListModel.Data<T> result) {
        return Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> emitter) throws Exception {
                try {
                    emitter.onNext(result.getList());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    LogUtil.e(e);
                }
            }
        });
    }

    public static ObservableTransformer<NetworkModel<Object>, Object> handleEmptyResultSync() {
        return new ObservableTransformer<NetworkModel<Object>, Object>() {
            @Override
            public ObservableSource<Object> apply(Observable<NetworkModel<Object>> upstream) {
                return upstream.flatMap(new Function<NetworkModel<Object>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(NetworkModel<Object> networkModel) {
//                        if (networkModel.getCode() != ExceptionHandle.SUCCESS_CODE) {
//                            return Observable.error(
//                                    new NetworkException(networkModel.getMsg(), networkModel.getCode()));
//                        } else {
//                            return createEmptyData();
//                        }
                        return createEmptyData();
                    }
                });
            }
        };
    }

    private static Observable<Object> createEmptyData() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                try {
                    emitter.onNext(OBJECT_EMPTY);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    LogUtil.e(e);
                }
            }
        });
    }
}
