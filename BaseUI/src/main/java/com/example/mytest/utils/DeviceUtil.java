package com.example.mytest.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.joy.ui.BaseApplication;


/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class DeviceUtil {

    public static String getIMEI() {

        String imei = "";

        try {

            Context ctx = BaseApplication.getContext();
            TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(imei))
                imei = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (imei == null)
                imei = "";
        } catch (Exception e) {

            e.printStackTrace();
        }
        return imei;
    }

    /**
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight() {

        Resources resources = BaseApplication.getAppResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * @return 导航栏的高度
     */
    public static int getNavigationBarHeight() {

        Resources resources = BaseApplication.getAppResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * @return 是否有导航栏
     */
    public static boolean hasNavigationBar() {

        Resources resources = BaseApplication.getAppResources();
        int resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return resourceId > 0 && resources.getBoolean(resourceId);
    }

    public static int getScreenWidth() {

        return BaseApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {

        return BaseApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean isNetworkEnable() {

        ConnectivityManager conManager = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public static boolean isNetworkDisable() {

        return !isNetworkEnable();
    }

    public static boolean sdcardIsEnable() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED);
    }

    /**
     * 检查app是否有安装
     *
     * @param packageName
     * @return
     */
    public static boolean checkAppHas(String packageName) {

        try {

            PackageInfo packageInfo = BaseApplication.getContext().getPackageManager().getPackageInfo(packageName, 0);
            int highBit = packageInfo.versionName.charAt(0);
            return highBit > 50 ? true : false;// 50 = 2
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}