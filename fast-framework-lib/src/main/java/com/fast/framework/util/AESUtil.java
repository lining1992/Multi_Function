/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * AES加密解密 AES-128-ECB加密
 * <p>
 * Created by lishicong on 2017/6/14.
 */

public class AESUtil {

    private static final String AES = "AES";
    private static final String CIPHER = "AES/CBC/PKCS5Padding";
    private static final String IV_PARAMETER = "0123456789abcdef";

    /**
     * AES 加密
     *
     * @param sSrc
     * @param sKey
     *
     * @return
     *
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {

        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance(CIPHER);
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        // BASE64转码，2次加密
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    /**
     * AES 解密
     *
     * @param sSrc
     * @param sKey
     *
     * @return
     *
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) throws Exception {

        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CIPHER);
        IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        // base64解密
        byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);

        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "1234567890123456";
        // 需要加密的字串
        String cSrc = "www.ymfeed.com";
        // 加密
        String enString = AESUtil.encrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String deString = AESUtil.decrypt(enString, cKey);
        System.out.println("解密后的字串是：" + deString);
    }
}
