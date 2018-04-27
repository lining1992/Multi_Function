/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

/**
 * 文件IO工具类
 * <p>
 * Created by lishicong on 2016/12/5.
 */

public class FileUtil {

    private FileUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取文件缓存数据目录(返回SDCard/Android/data/你的应用包名/files/file)
     *
     * @param context
     * @param file    目录名
     *
     * @return
     */
    public static String getExternalFilesDir(Context context, String file) {
        return context.getExternalFilesDir(file).getPath();
    }

    /**
     * 获取临时缓存数据目录(返回SDCard/Android/data/你的应用包名/cache)
     *
     * @param context
     * @param file    目录名
     *
     * @return
     */
    public static String getExternalCacheDir(Context context, String file) {
        return context.getExternalCacheDir().getPath() + File.separator + file;
    }

    /**
     * 删除临时缓存数据目录
     *
     * @param context
     *
     * @return
     */
    public static boolean deleteExternalCacheDir(Context context) {
        return context.getExternalCacheDir().delete();
    }

    /**
     * 检查是否存在SD卡
     *
     * @return
     */
    public static boolean hasSDCard() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 创建文件夹
     *
     * @param path
     *
     * @return
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (!isExist(dir)) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 判断File对象所指的目录或文件是否存在
     *
     * @param file
     *
     * @return
     */
    public static boolean isExist(File file) {
        return file.exists();
    }

    /**
     * 将字节写入文件
     *
     * @param bytes    字节数组
     * @param fileDir  文件存放路径
     * @param fileName 文件名
     *
     * @throws IOException
     */
    public static void writeByteToFile(byte[] bytes, String fileDir, String fileName) throws IOException {

        if (hasSDCard()) {

            File file = createDir(fileDir);

            if (file != null) {
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + File.separator + fileName);
                fos.write(bytes);
                fos.close();
            }
        }
    }

    /**
     * 读取文件
     *
     * @param fileName
     *
     * @return
     *
     * @throws IOException
     */
    public static String readFile(String fileName) throws IOException {

        if (hasSDCard() && isExist(new File(fileName))) {

            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            String res = new String(buffer, "UTF-8");
            fin.close();
            return res;
        }
        return null;
    }
}
