/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lishicong on 2016/12/13.
 */
public class BitmapUtil {

    // 图片压缩后的宽
    private static final int COMPRESS_WIDTH = 480;
    // 图片压缩后的高
    private static final int COMPRESS_HEIGHT = 800;

    /**
     * 图片压缩
     *
     * @param context
     * @param imgPathName 图片路径
     *
     * @return 返回压缩后文件的路径
     */
    public static Observable<String> compressImgToPath(final Context context, String imgPathName) {

        return Observable.just(imgPathName).subscribeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String pathname) {
                // bitmap转成string
                return bitmapToString(pathname, COMPRESS_WIDTH, COMPRESS_HEIGHT);
            }
        }).map(new Func1<String, byte[]>() {
            @Override
            public byte[] call(String bitmapStr) {
                // 解码string，获得bite[]字节流
                return Base64.decode(bitmapStr, Base64.DEFAULT);
            }
        }).map(new Func1<byte[], String>() {
            @Override
            public String call(byte[] bytes) {
                // 将字节流写入新的文件
                String filePath = FileUtil.getPath(context, FileUtil.getImgUri());
                writeToFile(filePath, bytes);
                return filePath;
            }
        });
    }

    /**
     * 压缩图片
     *
     * @param context
     * @param pathname
     *
     * @return 文件
     */
    public static Observable<File> compressImgToFile(Context context, String pathname) {
        return compressImgToPath(context, pathname).map(new Func1<String, File>() {
            @Override
            public File call(String s) {
                return new File(s);
            }
        });
    }

    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     *
     * @return
     */
    public static String bitmapToString(String filePath, int reqWidth, int reqHeight) {
        int orientation = readPictureDegree(filePath); // 获取旋转角度
        Bitmap bm = getSmallBitmap(filePath, reqWidth, reqHeight);
        if (Math.abs(orientation) > 0) {
            bm = rotaingImageView(orientation, bm); // 旋转图片
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        // 回收bitmap
        bm.recycle();
        bm = null;
        System.gc();
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 把bitmap转换成String
     *
     * @param filePath
     *
     * @return
     */
    public static String bitmapToString(String filePath, int reqWidth, int reqHeight, int requestKB) {
        int orientation = readPictureDegree(filePath); // 获取旋转角度
        Bitmap bm = getSmallBitmap(filePath, reqWidth, reqHeight);
        if (Math.abs(orientation) > 0) {
            bm = rotaingImageView(orientation, bm); // 旋转图片
        }
        byte[] b = getSmallBitmap(bm, requestKB);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @param requestKB
     *
     * @return
     */
    public static String bitmapToString(String filePath, int requestKB) {
        int orientation = readPictureDegree(filePath); // 获取旋转角度

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = 1;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = true;

        options.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);

        if (Math.abs(orientation) > 0) {
            bm = rotaingImageView(orientation, bm); // 旋转图片
        }
        byte[] b = getSmallBitmap(bm, requestKB);
        bm = null;

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 质量压缩图片
     *
     * @param bitmap
     * @param requestKB
     *
     * @return
     */
    public static byte[] getSmallBitmap(Bitmap bitmap, int requestKB) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        int options = 100;
        while (byteArrayOutputStream.toByteArray().length > requestKB * 1024 && options > 0) {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);
            options -= 10;
        }
        byte[] result = byteArrayOutputStream.toByteArray();
        try {
            bitmap = null;
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     *
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    /**
     * 将byte[]写入文件
     *
     * @param filePath
     * @param bytes
     */
    public static void writeToFile(String filePath, byte[] bytes) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片到本地
     *
     * @param filePath
     * @param bitmap
     */
    public static void saveBitmap(String filePath, Bitmap bitmap) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得图片的旋转角度
     *
     * @param path
     *
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     *
     * @return
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

}
