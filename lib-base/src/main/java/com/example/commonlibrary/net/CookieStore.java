/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example.commonlibrary.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.commonlibrary.utils.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by lishicong on 2016/12/12.
 */
public class CookieStore {

    private static final String COOKIE_PREFERENCES = "fast_cookies";

    private final Map<String, ConcurrentHashMap<String, Cookie>> mCookies;
    private final SharedPreferences mCookiePrefs;

    public CookieStore(Context context) {

        mCookies = new HashMap<>();
        mCookiePrefs = context.getSharedPreferences(COOKIE_PREFERENCES, Context.MODE_PRIVATE);

        // 将持久化的cookies缓存到内存中 即map cookies
        Map<String, ?> prefsMap = mCookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
            for (String name : cookieNames) {
                String encodedCookie = mCookiePrefs.getString(name, null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null) {
                        if (!mCookies.containsKey(entry.getKey())) {
                            mCookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                        }
                        mCookies.get(entry.getKey()).put(name, decodedCookie);
                    }
                }
            }
        }

    }

    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    public void add(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);

        // 将cookies缓存到内存中 如果缓存过期 就重置此cookie
        if (cookie.expiresAt() > System.currentTimeMillis()) {
            if (!mCookies.containsKey(url.host())) {
                mCookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
            }
            mCookies.get(url.host()).put(name, cookie);
        } else {
            if (mCookies.containsKey(url.host())) {
                mCookies.get(url.host()).remove(name);
            }
        }

        // 讲cookies持久化到本地
        SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
        if (mCookies.get(url.host()) == null) {
            mCookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
        }
        prefsWriter.putString(url.host(), TextUtils.join(",", mCookies.get(url.host()).keySet()));
        prefsWriter.putString(name, encodeCookie(new OkHttpCookies(cookie)));
        prefsWriter.apply();
    }

    public List<Cookie> get(HttpUrl url) {
        ArrayList<Cookie> ret = new ArrayList<>();
        if (mCookies.containsKey(url.host())) {
            ret.addAll(mCookies.get(url.host()).values());
        }
        return ret;
    }

    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        mCookies.clear();
        return true;
    }

    public boolean remove(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);

        if (mCookies.containsKey(url.host()) && mCookies.get(url.host()).containsKey(name)) {
            mCookies.get(url.host()).remove(name);

            SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
            if (mCookiePrefs.contains(name)) {
                prefsWriter.remove(name);
            }
            prefsWriter.putString(url.host(), TextUtils.join(",", mCookies.get(url.host()).keySet()));
            prefsWriter.apply();

            return true;
        } else {
            return false;
        }
    }

    public List<Cookie> getCookies() {
        ArrayList<Cookie> ret = new ArrayList<>();
        for (String key : mCookies.keySet()) {
            ret.addAll(mCookies.get(key).values());
        }

        return ret;
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    protected String encodeCookie(OkHttpCookies cookie) {
        if (cookie == null) {
            return null;
        }
        byte[] bytes = null;
        ByteArrayOutputStream os = null;
        ObjectOutputStream outputStream = null;
        try {
            os = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
            bytes = os.toByteArray();
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
        }

        if (bytes != null) {
            return byteArrayToHexString(bytes);
        } else {
            return null;
        }
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((OkHttpCookies) objectInputStream.readObject()).getCookies();
        } catch (IOException e) {
            LogUtil.e(e);
        } catch (ClassNotFoundException e) {
            LogUtil.e(e);
        }

        return cookie;
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(
                    hexString.charAt(i + 1), 16));
        }
        return data;
    }


}
