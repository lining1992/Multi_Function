//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

final class CookieJarImpl implements CookieJar {
    private final ConcurrentHashMap<String, List<Cookie>> mCookieStore = new ConcurrentHashMap();

    CookieJarImpl() {
    }

    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.mCookieStore.put(url.host(), cookies);
    }

    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = (List)this.mCookieStore.get(url.host());
        return (List)(cookies != null?cookies:new ArrayList());
    }

    public void clear() {
        this.mCookieStore.clear();
    }
}
