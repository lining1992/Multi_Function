package com.fast.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by lishicong on 2017/6/22.
 */

public class BitmapUtil {

    /**
     * drawable 转换成 bitmap
     *
     * @param drawable
     *
     * @return
     */
    public static Bitmap convertDrawableToBitmap(Drawable drawable) {

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565; // 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
        Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas); // 把drawable内容画到画布中
        return bitmap;
    }

    /**
     * 通过资源ID获取bitmap
     *
     * @param context
     * @param resId
     *
     * @return
     */
    public static Bitmap getBitmapByRes(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 通过文件路径获取bitmap
     * <p>
     * if(!bmp.isRecycle() ){
     * bmp.recycle()   //回收图片所占的内存
     * system.gc()  //提醒系统及时回收
     * }
     *
     * @param filePath
     *
     * @return
     */
    public static Bitmap getBitmapByFile(String filePath) {
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inTempStorage = new byte[12 * 1024];
        bfOptions.inJustDecodeBounds = true;
        File file = new File(filePath);
        FileInputStream fs = null;
        Bitmap bmp = null;
        try {
            fs = new FileInputStream(file);
            if (fs != null) {
                bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bmp;
    }
}
