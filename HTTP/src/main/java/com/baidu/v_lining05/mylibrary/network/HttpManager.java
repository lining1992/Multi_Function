//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.network;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.baidu.v_lining05.mylibrary.network.callback.DownloadCallback;
import com.baidu.v_lining05.mylibrary.network.callback.HttpCallback;
import com.baidu.v_lining05.mylibrary.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

public final class HttpManager {
    private static final String TAG = "codriver_network";
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private CookieJarImpl mCookieJarImpl;

    private static HttpManager getInstance() {
        return HttpManager.InstanceHolder.INSTANCE;
    }

    private HttpManager() {
        LogUtil.i("codriver_network", "init http manager");
        this.mCookieJarImpl = new CookieJarImpl();
        X509TrustManager trustManager = this.createTrustManager();
        SSLSocketFactory sslSocketFactory = this.createSSLSocketFactory(trustManager);
        HostnameVerifier hostnameVerifier = this.createHostnameVerifier();
        Builder builder = (new Builder()).connectTimeout(10L, TimeUnit.SECONDS).writeTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).cookieJar(this.mCookieJarImpl);
        if(trustManager != null && sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(hostnameVerifier);
        } else {
            LogUtil.e("codriver_network", "trustManager==null || sslSocketFactory==null");
        }

        this.mOkHttpClient = builder.build();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClient getOkHttpClient() {
        return getInstance().mOkHttpClient;
    }

    public static void cancelAllRequests() {
        LogUtil.i("codriver_network", "cancel all requests");
        getInstance().mOkHttpClient.dispatcher().cancelAll();
    }

    public static void cancelRequest(String url) {
        List<Call> calls = getInstance().mOkHttpClient.dispatcher().runningCalls();
        Iterator var2 = calls.iterator();

        Call call;
        while(var2.hasNext()) {
            call = (Call)var2.next();
            if(TextUtils.equals(call.request().url().toString(), url)) {
                call.cancel();
                LogUtil.e("codriver_network", "cancel running reuqest, url=" + url);
            }
        }

        calls = getInstance().mOkHttpClient.dispatcher().queuedCalls();
        var2 = calls.iterator();

        while(var2.hasNext()) {
            call = (Call)var2.next();
            if(TextUtils.equals(call.request().url().toString(), url)) {
                call.cancel();
                LogUtil.e("codriver_network", "cancel queued reuqest, url=" + url);
            }
        }

    }

    public static void clearCookies() {
        LogUtil.i("codriver_network", "clear cookies");
        getInstance().mCookieJarImpl.clear();
    }

    public static void get(String url, HttpCallback callback) {
        Request request = HttpManagerUtil.buildGetRequest(url);
        getInstance().execute(request, callback);
    }

    public static void postForm(String url, Map<String, String> params, HttpCallback callback) {
        Request request = HttpManagerUtil.buildPostFormRequest(url, params);
        getInstance().execute(request, callback);
    }

    public static void postJson(String url, String postJson, HttpCallback callback) {
        Request request = HttpManagerUtil.buildPostJsonRequest(url, postJson);
        getInstance().execute(request, callback);
    }

    public static void postString(String url, String params, HttpCallback callback) {
        Request request = HttpManagerUtil.buildPostStringRequest(url, params);
        getInstance().execute(request, callback);
    }

    public static void downloadFile(String url, String destFileDir, long startPosition, DownloadCallback callback) {
        getInstance().download(url, destFileDir, startPosition, callback);
    }

    private void execute(Request request, final HttpCallback callback) {
        this.mOkHttpClient.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, Response response) {
                String url = call.request().url().toString();
                if(callback == null) {
                    LogUtil.e("codriver_network", "HttpCallback is null");
                }

                try {
                    List<String> cookies = response.headers().values("Set-Cookie");
                    HttpManager.this.cookieCallback(callback, url, cookies);
                    int statusCode = response.code();
                    if(response.isSuccessful()) {
                        String body = response.body().string();
                        HttpManager.this.successCallback(callback, url, statusCode, body);
                    } else {
                        HttpManager.this.errorCallback(callback, url, "[1001]statusCode=" + statusCode);
                    }
                } catch (Exception var10) {
                    var10.printStackTrace();
                    HttpManager.this.errorCallback(callback, url, "[1002]" + var10.toString());
                } finally {
                    response.close();
                }

            }

            public void onFailure(Call call, IOException e) {
                HttpManager.this.errorCallback(callback, call.request().url().toString(), "[1003]" + e.toString());
            }
        });
    }

    private void successCallback(final HttpCallback callback, final String url, final int statusCode, final String response) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if(callback != null) {
                    callback.onSuccess(url, statusCode, response);
                }

            }
        });
    }

    private void cookieCallback(final HttpCallback callback, final String url, final List<String> cookies) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if(callback != null) {
                    callback.onCookies(url, cookies);
                }

            }
        });
    }

    private void errorCallback(final HttpCallback callback, final String url, final String errMsg) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if(callback != null) {
                    if(errMsg.contains("java.io.IOException: Canceled")) {
                        callback.onCancel(url);
                    } else {
                        callback.onError(url, errMsg);
                    }
                }

            }
        });
    }

    private void download(String url, final String destFileDir, long startPosition, final DownloadCallback downloadCallback) {
        final Request request = HttpManagerUtil.buildDownloadRequest(url, startPosition);
        this.mOkHttpClient.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, Response response) {
                String url = call.request().url().toString();
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;

                try {
                    inputStream = response.body().byteStream();
                    String fileName = HttpManagerUtil.getFileNameFromUrl(url);
                    if(fileName == null) {
                        LogUtil.e("codriver_network", "get file name from url failed! url=" + url);
                        fileName = "temp" + System.currentTimeMillis() + ".bat";
                    }

                    File file = new File(destFileDir, fileName);
                    if(!file.exists()) {
                        file.createNewFile();
                    }

                    LogUtil.i("codriver_network", "cacheFile = " + file.getAbsolutePath());
                    HttpManager.this.downloadStartCallback(downloadCallback, url, file.getAbsolutePath());
                    long contentLength = response.body().contentLength();
                    long downloadLength = 0L;
                    byte[] buffer = new byte[8192];
                    int index = 0;

                    int len;
                    for(fileOutputStream = new FileOutputStream(file); (len = inputStream.read(buffer)) != -1; ++index) {
                        fileOutputStream.write(buffer, 0, len);
                        downloadLength += (long)len;
                        if(index % 20 == 0) {
                            HttpManager.this.downloadProgressCallback(downloadCallback, url, downloadLength, contentLength);
                        }
                    }

                    fileOutputStream.flush();
                    HttpManager.this.downloadCompleteCallback(downloadCallback, url, file.getAbsolutePath());
                } catch (Exception var31) {
                    var31.printStackTrace();
                    HttpManager.this.downloadErrorCallback(downloadCallback, url, var31.toString());
                } finally {
                    if(inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception var30) {
                            var30.printStackTrace();
                        }
                    }

                    if(fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception var29) {
                            var29.printStackTrace();
                        }
                    }

                    if(request != null) {
                        try {
                            response.close();
                        } catch (Exception var28) {
                            var28.printStackTrace();
                        }
                    }

                }

            }

            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                HttpManager.this.downloadErrorCallback(downloadCallback, call.request().url().toString(), e.toString());
            }
        });
    }

    private void downloadCompleteCallback(final DownloadCallback downloadCallback, final String url, final String filePath) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if(downloadCallback != null) {
                    downloadCallback.onDownloadComplete(url, filePath);
                }

            }
        });
    }

    private void downloadStartCallback(final DownloadCallback downloadCallback, final String url, final String filePath) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if(downloadCallback != null) {
                    downloadCallback.onDownloadStart(url, filePath);
                }

            }
        });
    }

    private void downloadProgressCallback(final DownloadCallback downloadCallback, final String url, final long downloadSize, final long size) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if(downloadCallback != null) {
                    downloadCallback.onDownloadProgress(url, downloadSize, size);
                }

            }
        });
    }

    private void downloadErrorCallback(final DownloadCallback downloadCallback, final String url, final String errMsg) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if(downloadCallback != null) {
                    downloadCallback.onDownloadError(url, errMsg);
                }

            }
        });
    }

    private SSLSocketFactory createSSLSocketFactory(TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
        } catch (KeyManagementException var4) {
            var4.printStackTrace();
        }

        return null;
    }

    private X509TrustManager createTrustManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private HostnameVerifier createHostnameVerifier() {
        return new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                LogUtil.d("codriver_network", "hostname=" + hostname);
                return true;
            }
        };
    }

    public static void addUserAgent(String params) {
        HttpConfig.USER_AGENT = params;
    }

    private static class InstanceHolder {
        public static final HttpManager INSTANCE = new HttpManager();

        private InstanceHolder() {
        }
    }
}
