//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.network.request;

import com.baidu.v_lining05.mylibrary.util.LogUtil;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class RequestParamSigner {
    private static final char[] HEX_REFER_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public RequestParamSigner() {
    }

    public static String sign(Map<String, String> params, String signPrefix, String signPostfix) {
        List<RequestParamSigner.KeyValuePair> keyValuePairList = buildKeyValuePairList(params);
        sort(keyValuePairList);
        StringBuffer stringBuffer = new StringBuffer();
        Iterator var5 = keyValuePairList.iterator();

        while(var5.hasNext()) {
            RequestParamSigner.KeyValuePair keyValuePair = (RequestParamSigner.KeyValuePair)var5.next();
            stringBuffer.append(keyValuePair.toString());
        }

        LogUtil.d("network", "str for signed: " + stringBuffer.toString());
        LogUtil.d("network", "key_sign: prefix=" + signPrefix + "; suffix=" + signPostfix);
        return toMd5(signPrefix + stringBuffer.toString() + signPostfix);
    }

    private static List<RequestParamSigner.KeyValuePair> buildKeyValuePairList(Map<String, String> params) {
        ArrayList keyValuePairList = new ArrayList();

        try {
            Iterator iterator = params.keySet().iterator();

            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                String value = URLEncoder.encode((String)params.get(key), "UTF-8");
                keyValuePairList.add(new RequestParamSigner.KeyValuePair(key, value));
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return keyValuePairList;
    }

    private static void sort(List<RequestParamSigner.KeyValuePair> keyValuePairList) {
        Collections.sort(keyValuePairList, new Comparator<RequestParamSigner.KeyValuePair>() {
            public int compare(RequestParamSigner.KeyValuePair lhs, RequestParamSigner.KeyValuePair rhs) {
                return lhs.key.compareTo(rhs.key);
            }
        });
    }

    public static String toMd5(String str) {
        String md5Str = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte[] b = md.digest();
            md5Str = byteArrayToHex(b);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return md5Str.toLowerCase();
    }

    public static String byteArrayToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int index = 0;
        byte[] var3 = bytes;
        int var4 = bytes.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            hexChars[index++] = HEX_REFER_CHARS[b >>> 4 & 15];
            hexChars[index++] = HEX_REFER_CHARS[b & 15];
        }

        return new String(hexChars);
    }

    private static class KeyValuePair {
        String key;
        String value;

        KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
