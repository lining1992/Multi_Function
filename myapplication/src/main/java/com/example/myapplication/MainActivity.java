package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.mvp.MainCantract;
import com.example.commonlibrary.mvp.MainPresenter;
import com.example.commonlibrary.utils.LogUtil;
import com.example.commonlibrary.utils.TaskExecutor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class MainActivity extends BaseActivity implements MainCantract.View {

    private MainCantract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadData();
    }

    @Override
    protected void initVariables() {

    }

    protected void loadData() {
        mPresenter.loadData();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.d("debugli", "===threadName===" + Thread.currentThread().getName());
                LogUtil.d("debugli", "===code===" + response.code());
                LogUtil.d("debugli", "===net response===" + response.networkResponse());
                LogUtil.d("debugli", "===cache response===" + response.cacheResponse());
                response.close();
            }
        };
        File httpCacheDirectory = new File(getCacheDir(), "responses");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.pingInterval(100, TimeUnit.MILLISECONDS);
        builder.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
        OkHttpClient build = builder.build();
        Request request = new Request.Builder().url("https://www.baidu.com").build();
        final Call call = build.newCall(request);
//        call.enqueue(callback);
        Request request2 = new Request.Builder().url("https://www.baidu.com").build();
        final Call call2 = build.newCall(request2);
//        call2.enqueue(callback);
        TaskExecutor.enqueue(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    LogUtil.d("debugli", "===net response===" + response.networkResponse());
                    LogUtil.d("debugli", "===cache response===" + response.cacheResponse());
                    response.body().close();
                    Response response1 = call2.execute();
                    LogUtil.d("debugli", "===net response1===" + response1.networkResponse());
                    LogUtil.d("debugli", "===cache response1===" + response1.cacheResponse());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void    initView() {
        mPresenter = new MainPresenter();
        mPresenter.bindView(this);
        bindPresenter(mPresenter);
    }

    @Override
    public void bindPresenter(MainCantract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onSuccess() {
        LogUtil.d("debugli", "onSuccess");
    }

    @Override
    public void onFail() {
        LogUtil.d("debugli", "onFail");
    }

    public void button(View view) {
        loadData();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
