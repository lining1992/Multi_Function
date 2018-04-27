/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support.permissionslib;

import com.fast.framework.R;
import com.fast.framework.widget.FastAlertDialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class RequestActivity extends AppCompatActivity {

    private static final String TAG = "RequestActivity";
    private static final String PERMISSIONS = "permissions";
    private static final String EXPLAIN = "explain";
    private static final int REQUEST_CODE = 1000;

    private String mExplain;
    private String[] mPermissions;

    public static Intent newIntent(Context context, String[] permissions, String explain,
                                   OnRequestPermissionsCallBack callBack) {
        Intent intent = new Intent(context, RequestActivity.class);
        Bundle extras = new Bundle();
        extras.putStringArray(PERMISSIONS, permissions);
        extras.putString(EXPLAIN, explain);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        checkPermission();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        mExplain = extras.getString(EXPLAIN);
        mPermissions = extras.getStringArray(PERMISSIONS);
    }

    private void checkPermission() {
        int deniedIndex = checkSelfPermissions(mPermissions);
        if (deniedIndex != -1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, mPermissions[deniedIndex])) {
                Log.d(TAG, "denied");
                if (TextUtils.isEmpty(mExplain)) {
                    requestPermission();
                } else {
                    showExplainDialog();
                }

            } else {
                Log.d(TAG, "start request permission");
                requestPermission();
            }
        } else {
            Log.d(TAG, "Authorized");
            Intent intent = new Intent();
            intent.putExtra(Constants.GRANT, true);
            sendMessage(intent);
        }
    }

    private int checkSelfPermissions(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
                return i;
            }
        }
        return -1;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, mPermissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:

                int index = PermissionUtils.verifyPermissions(grantResults);

                Intent intent = new Intent();
                Bundle args = new Bundle();
                if (index == -1) {
                    args.putBoolean(Constants.GRANT, true);
                } else {
                    args.putString(Constants.DENIED, permissions[index]);
                }
                intent.putExtras(args);

                sendMessage(intent);
                break;
            default:
                break;
        }
    }

    private void sendMessage(Intent intent) {
        intent.setAction(getPackageName());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    private void showExplainDialog() {

        FastAlertDialog mDialog = new FastAlertDialog();
        mDialog.setFastTitle(getResources().getString(R.string.fast_dialog_permission_title));
        mDialog.setFastContent(getResources().getString(R.string.fast_dialog_permission_content));

        String button = getResources().getString(R.string.fast_confirm);
        mDialog.setFastButton(button, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        mDialog.setFastButton2(getResources().getString(R.string.fast_cancel), new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDialog.show(this.getSupportFragmentManager(), FastAlertDialog.class.getSimpleName());
    }
}
