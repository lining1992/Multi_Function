package com.example.mytest.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO工具类
 */
public class IOUtil {

    public static void closeInStream(InputStream input) {

        try {

            if (input != null)
                input.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void closeOutStream(OutputStream output) {

        try {

            if (output != null)
                output.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static boolean copyFileFromRaw(InputStream in, File outPutFile) {

        boolean result = false;
        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(outPutFile);

            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = in.read(buffer)) != -1) {

                fos.write(buffer, 0, count);
            }

            result = true;

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            closeInStream(in);
            closeOutStream(fos);
        }

        return result;
    }
}