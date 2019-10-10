package com.example.http.network.request;

import com.example.http.network.HttpManager;
import com.example.http.network.callback.HttpCallback;
import com.example.http.util.LogUtil;

/**
 * 创建人：v_lining05
 * 创建时间：2018/4/11
 */
public abstract class AbsHttpGetRequest implements IHttpRequest, HttpCallback {

    private String url;

    public AbsHttpGetRequest() {
    }

    public void execute() {
        url = this.getUrl();
        HttpManager.get(url, this);
    }

    public void cancel() {
        HttpManager.cancelRequest(this.url);
    }

    @Override
    public void onCancel(String var1) {
        LogUtil.e("network", this.getClass().getSimpleName() + " canceled!");
    }
}
