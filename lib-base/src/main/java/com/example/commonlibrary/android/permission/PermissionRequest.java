package com.example.commonlibrary.android.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.commonlibrary.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/10/23 15:26
 */
public class PermissionRequest {

    private Activity mActivity;
    private String[] mPermissions;
    private PermissionListener mPermissionListener;
    private static int REQUEST_CODE_PERMISSION = 10201;
    private static int REQUEST_CODE_SETTING = 10202;

    public PermissionRequest() {

    }

    public PermissionRequest with(Activity activity) {
        mActivity = activity;
        return this;
    }

    public PermissionRequest request(String[] permissions) {
        mPermissions = permissions;
        return this;
    }

    public PermissionRequest permissionListener(PermissionListener listener) {
        mPermissionListener = listener;
        return this;
    }

    public void checkPermissions() {
        List<String> permissionList = new ArrayList<>();
        for (String permission : mPermissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        String[] ungrantedPermissions = CollectionUtil.toArray(permissionList);
        if (CollectionUtil.isEmpty(ungrantedPermissions)) {
            mPermissionListener.onGranted();
            return;
        }
        boolean showRationale = false;
        for (String permission : ungrantedPermissions) {
            boolean show = ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
            showRationale |= show;
        }
        if (showRationale) {
            showSettingDialog(ungrantedPermissions[0]);
        } else {
            ActivityCompat.requestPermissions(mActivity, ungrantedPermissions, REQUEST_CODE_PERMISSION);
        }
    }

    private boolean isGranted(@NonNull int[] grantResults) {
        if (CollectionUtil.isEmpty(grantResults)) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void showSettingDialog(final String unPermission) {
        // 当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
//        new ContentTwoButtonDialog(mActivity, R.layout.dialog_two_line_content_two_button_land)
//                .cancelable(false)
//                .content("首次使用时，需要开启GPS服务来启用本地音乐功能")
//                .leftText(Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(unPermission) ? "退出应用" : "取消")
//                .leftClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mPermissionListener.onDenied();
//                    }
//                })
//                .rightText("前往设置")
//                .rightClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // 引导用户到设置中去进行设置
////                        gotoSettingPage();
//                        if (mActivity != null) {
//                            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(unPermission)) {
//                                SecurityPermissionHelper.location(mActivity);
//                            } else {
//                                SecurityPermissionHelper.main(mActivity);
//                            }
//                            mActivity.finish();
//                        }
//                    }
//                })
//                .setTransparent(0.6f)
//                .show();
    }

    private void gotoSettingPage() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        mActivity.startActivityForResult(intent, REQUEST_CODE_SETTING);
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != REQUEST_CODE_PERMISSION) {
            return false;
        }
        if (isGranted(grantResults)) {
            mPermissionListener.onGranted();
        } else {
            showSettingDialog(permissions.length > 0 ? permissions[0] : null);
        }
        return true;
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_SETTING) {
            return false;
        }

        checkPermissions();
        return true;
    }
}
