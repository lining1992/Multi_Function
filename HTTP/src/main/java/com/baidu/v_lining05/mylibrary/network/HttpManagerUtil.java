//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.network;

import android.text.TextUtils;

import com.baidu.v_lining05.mylibrary.util.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Request.Builder;

final class HttpManagerUtil {
    private static final String TAG = "network";

    HttpManagerUtil() {
    }

    private static Builder requestBuilder(String url) {
        return (new Builder()).url(url).addHeader("User-Agent", HttpConfig.USER_AGENT);
    }

    static Request buildGetRequest(String url) {
        LogUtil.i("network", "GET URL: " + url);
        return requestBuilder(url).build();
    }

    static Request buildDownloadRequest(String url, long startPosition) {
        LogUtil.i("network", "DOWNLOAD URL: " + url + " , startPosition=" + startPosition);
        return requestBuilder(url).addHeader("RANGE", "bytes=" + startPosition + "-").build();
    }

    static Request buildPostJsonRequest(String url, String postJson) {
        LogUtil.i("network", "POST URL: " + url);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, postJson);
        return requestBuilder(url).post(requestBody).build();
    }

    static Request buildPostStringRequest(String url, String params) {
        LogUtil.i("network", "POST URL: " + url);
        LogUtil.i("network", "POST PARAM: " + params);
        MediaType mediaType = MediaType.parse("text/html");
        RequestBody requestBody = RequestBody.create(mediaType, params);
        return requestBuilder(url).post(requestBody).build();
    }

    static Request buildPostFormRequest(String url, Map<String, String> params) {
        LogUtil.i("network", "POST URL: " + url);
        if(params == null) {
            params = new HashMap();
        }

        okhttp3.FormBody.Builder formBodyBuilder = new okhttp3.FormBody.Builder();
        Iterator iterator = ((Map)params).entrySet().iterator();

        while(iterator.hasNext()) {
            Entry<String, String> entry = (Entry)iterator.next();
            LogUtil.i("network", "POST PARAM: " + (String)entry.getKey() + "=" + (String)entry.getValue());
            formBodyBuilder.add((String)entry.getKey(), (String)entry.getValue());
        }

        RequestBody requestBody = formBodyBuilder.build();
        return requestBuilder(url).post(requestBody).build();
    }

    static String getFileNameFromUrl(String url) {
        if(TextUtils.isEmpty(url)) {
            return null;
        } else {
            url = url.split("\\?")[0];
            Pattern pattern = Pattern.compile("^.*/(.*)$");
            Matcher matcher = pattern.matcher(url);
            return matcher.find()?matcher.group(1):null;
        }
    }
}
