/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example.commonlibrary.net;

import android.net.Uri;
import android.text.TextUtils;

import com.example.commonlibrary.utils.LogUtil;
import com.example.commonlibrary.utils.MD5Util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 自定义网络代理拦截
 * <p>
 * Created by lishicong on 2016/12/13.
 */

public class UserAgentInterceptor implements Interceptor {

    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private static final boolean NETWORK_LOG = true;
    private final String userAgentHeaderValue;
    private static final long TIME_OUT = 10 * 1000;
    private String token = "9xkJolV4eol1nbOFrKi3TaaTjy8m9QSJy3VI9Hw5IIfrqJ5Co9nbydywPjN4BJoD0ymPDXvRGyiQE6gNTF7qFg==";

    public UserAgentInterceptor(String userAgentHeaderValue) {
        this.userAgentHeaderValue = userAgentHeaderValue;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();

        Request request = addCommonParameter(original);
        Response response = chain.proceed(request);

        return response;
    }

    /**
     * 重新创建Request，并添加公共参数
     *
     * @param original
     */
    private Request addCommonParameter(Request original) {

        String method = original.method();

        if ("POST".equalsIgnoreCase(method)) {
            return addPostCommonParams(original);
        }
        return addGetCommonParams(original);
    }

    /**
     * GET
     *
     * @param original
     * @return
     */
    private Request addGetCommonParams(Request original) {

        HttpUrl originalHttpUrl = original.url();

        // 添加公共参数
        HttpUrl.Builder httpUrlBuilder = originalHttpUrl.newBuilder();
        if (NetworkParams.params != null && NetworkParams.params.size() > 0) {
            for (String key : NetworkParams.params.keySet()) {
                if (!TextUtils.isEmpty(NetworkParams.params.get(key))) {
                    httpUrlBuilder.addQueryParameter(key, NetworkParams.params.get(key));
                }
            }
        }
        // 添加时间戳
//        String timestamp = getNetTime() + "";
        String timestamp = System.currentTimeMillis() + "";
        httpUrlBuilder.addQueryParameter("timestamp", timestamp + "");
//        httpUrlBuilder.addQueryParameter("timestamp", System.currentTimeMillis() + "");
//        httpUrlBuilder.addQueryParameter("timestamp", getNetTime() + "");

        // 参数加密
        TreeMap<String, String> treeMap = new TreeMap<>();
        Uri uri = Uri.parse(httpUrlBuilder.build().url().toString());
        Set<String> params = uri.getQueryParameterNames();
        for (String param : params) {
            if (!TextUtils.isEmpty(uri.getQueryParameter(param))) {
                treeMap.put(param, uri.getQueryParameter(param));
            } else {
                httpUrlBuilder.removeAllQueryParameters(param);
            }
        }

        // 添加签名
        httpUrlBuilder.addQueryParameter("sign", encrypt(treeMap));

        Request.Builder requestBuilder = original.newBuilder().url(httpUrlBuilder.build());

        Request request = requestBuilder.removeHeader(USER_AGENT_HEADER_NAME)
                .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                .addHeader("Connection", "close")
                .build();
        if (NETWORK_LOG) {
            LogUtil.i("HTTP GET REQUEST URL:" + request.url());
        }

        return request;
    }

    /**
     * POST
     *
     * @param original
     * @return
     */
    private Request addPostCommonParams(Request original) {

        Request.Builder requestBuilder = original.newBuilder();

        if (original.body() instanceof FormBody) {

            FormBody.Builder formBodyBuiler = new FormBody.Builder();

            FormBody originalFormBody = (FormBody) original.body();

            TreeMap<String, String> treeMap = new TreeMap<>();

            // 按照以下方式保证和服务器加密方式一致
            // 添加原有请求参数
            for (int i = 0; i < originalFormBody.size(); i++) {
                if (!TextUtils.isEmpty(originalFormBody.value(i))) {
                    formBodyBuiler.add(originalFormBody.name(i), originalFormBody.value(i));
                    treeMap.put(originalFormBody.name(i), originalFormBody.value(i));
                }
            }

            // 添加公共请求参数
            if (NetworkParams.params != null && NetworkParams.params.size() > 0) {
                for (String key : NetworkParams.params.keySet()) {

                    // 添加非空字段的参数
                    if (!TextUtils.isEmpty(NetworkParams.params.get(key))) {
                        formBodyBuiler.add(key, NetworkParams.params.get(key));
                        treeMap.put(key, NetworkParams.params.get(key));
                    }
                }
            }
            // 添加时间戳
//            String timestamp = System.currentTimeMillis() + "";
//            String timestamp = getNetTime() + "";
            String timestamp = System.currentTimeMillis() + "";

            formBodyBuiler.addEncoded("timestamp", timestamp);
            treeMap.put("timestamp", timestamp);

            // 添加签名
            formBodyBuiler.add("sign", encrypt(treeMap));

            FormBody formBody = formBodyBuiler.build();

            // 输出带参数的log
            if (NETWORK_LOG) {
                String params = getPostParamString(formBody);

                LogUtil.i("HTTP POST REQUEST URL:" + original.url() + params);
            }

            requestBuilder.method(original.method(), formBody);
        }

        requestBuilder.removeHeader(USER_AGENT_HEADER_NAME)
                .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                .addHeader("Connection", "close")
                .build();

        Request request = requestBuilder.build();

        return request;
    }


    private String getPostParamString(FormBody formBody) {
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < formBody.size(); i++) {
            if (i == 0) {
                sbf.append("?");
            } else {
                sbf.append("&");
            }

            sbf.append(formBody.encodedName(i));
            sbf.append("=");
            sbf.append(formBody.encodedValue(i));
        }
        return sbf.toString();
    }

    /**
     * 对参数加密
     *
     * @param treeMap
     * @return
     */
    private String encrypt(TreeMap<String, String> treeMap) {
        StringBuffer sbf = new StringBuffer();
        for (String key : treeMap.keySet()) {
            if (!treeMap.get(key).equals("")) {
                sbf.append(key).append("=").append(treeMap.get(key)).append("&");
            }
        }

        LogUtil.i("before_encrypt", sbf.toString());
        String encrypt = MD5Util.getMD5String(sbf.toString());
        return sbf.toString();
    }


    private long getNetTime() {
        URL url;//取得资源对象
        long ld = 0L;
        try {
            url = new URL("http://www.baidu.com");
            //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
            //url = new URL("http://www.bjtime.cn");
            //生成连接对象
            URLConnection uc = url.openConnection();
            //发出连接
            uc.connect();
            //取得网站日期时间
            ld = uc.getDate();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);
            final String format = formatter.format(calendar.getTime());

        } catch (Exception e) {
            LogUtil.e(e);
        }
        if (ld == 0) {
            ld = System.currentTimeMillis();

        }
        return ld;
    }

}