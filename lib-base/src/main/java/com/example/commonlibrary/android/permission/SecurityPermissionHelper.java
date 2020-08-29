package com.example.commonlibrary.android.permission;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.example.commonlibrary.utils.LogUtil;

/**
 * 安全隐私助手适配，没有权限去隐私助手界面
 * <p>
 *
 * @author yinxuming
 * @date 2019/3/1
 */
public class SecurityPermissionHelper {
    // 通过传递不同的参数跳转不同的页面
    private static String locationFlag = "com.baidu.bodyguard-Location";
    private static String voiceFlag = "com.baidu.bodyguard-Voice";

    // 指定跳转目标
    private static String targetPath = "com.baidu.bodyguard.ui.activity.MainActivity";
    private static String targetPackage = "com.baidu.bodyguard";

    private static String intentKey = "privacy_action";


    public static void voice(Context context) {
        Intent intent = new Intent();
        // 跳转到麦克风页面
        intent.putExtra(intentKey, voiceFlag);
        intent.setComponent(new ComponentName(targetPackage, targetPath));

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtil.e(e);
            gotoSettingPage(context);
        }
    }

    public static void location(Context context) {
        Intent intent = new Intent();
        // 跳转定位服务页面
        intent.putExtra(intentKey, locationFlag);
        intent.setComponent(new ComponentName(targetPackage, targetPath));

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            LogUtil.e(exception);
            gotoSettingPage(context);
        }
    }

    public static void main(Context context) {
        // 不带参数跳转到隐私助手主页
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(targetPackage, targetPath));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtil.e(e);
            gotoSettingPage(context);
        }
    }

    private static void gotoSettingPage(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            LogUtil.e(exception);
        }
    }
}
