//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.http.network.request;

import com.example.http.network.HttpManager;
import com.example.http.network.callback.HttpCallback;
import com.example.http.util.LogUtil;

import java.util.List;

public abstract class AbsHttpPostRequest implements IHttpRequest, HttpCallback {
    protected static final String TAG = "network";
    private String mRequesUrl;

    public AbsHttpPostRequest() {
    }

    public void execute() {
        this.mRequesUrl = this.getUrl();
        HttpManager.postForm(this.mRequesUrl, this.getParams(), this);
    }

    public void cancel() {
        HttpManager.cancelRequest(this.mRequesUrl);
    }

    public void onCookies(String url, List<String> cookies) {
    }

    public void onCancel(String url) {
        LogUtil.e("network", this.getClass().getSimpleName() + " canceled!");
    }
}
