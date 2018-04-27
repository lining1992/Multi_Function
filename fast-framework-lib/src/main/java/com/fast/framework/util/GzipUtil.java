/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by lishicong on 2017/6/14.
 */

public class GzipUtil {

    private static final int BUFFERSIZE = 1024;

    /**
     * Zip 压缩数据
     *
     * @param unZipStr
     *
     * @return
     */
    public static String compressForZip(String unZipStr) {

        if (TextUtils.isEmpty(unZipStr)) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(baos);
            zip.putNextEntry(new ZipEntry("0"));
            zip.write(unZipStr.getBytes());
            zip.closeEntry();
            zip.close();
            byte[] encode = baos.toByteArray();
            baos.flush();
            baos.close();
            return Base64.encodeToString(encode, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Zip解压数据
     *
     * @param zipStr
     *
     * @return
     */
    public static String decompressForZip(String zipStr) {

        if (TextUtils.isEmpty(zipStr)) {
            return null;
        }
        byte[] t = Base64.decode(zipStr, Base64.DEFAULT);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(t);
            ZipInputStream zip = new ZipInputStream(in);
            zip.getNextEntry();
            byte[] buffer = new byte[BUFFERSIZE];
            int n = 0;
            while ((n = zip.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, n);
            }
            zip.close();
            in.close();
            out.close();
            return out.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
