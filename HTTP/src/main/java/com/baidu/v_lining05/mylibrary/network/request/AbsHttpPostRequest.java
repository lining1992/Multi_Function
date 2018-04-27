//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.network.request;

import com.baidu.v_lining05.mylibrary.network.HttpManager;
import com.baidu.v_lining05.mylibrary.network.callback.HttpCallback;
import com.baidu.v_lining05.mylibrary.util.LogUtil;

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
