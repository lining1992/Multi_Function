package com.baidu.iov.dueros.test.activity;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.LogPrinter;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.iov.dueros.test.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 运行的GC次数
            String runtimeStat = Debug.getRuntimeStat("art.gc.gc-count");
            // GC使用的总耗时，单位是毫秒
            String runtimeStat1 = Debug.getRuntimeStat("art.gc.gc-time");
            // 阻塞式GC的次数
            String runtimeStat2 = Debug.getRuntimeStat("art.gc.blocking-gc-count");
            // 阻塞式GC的总耗时
            String runtimeStat3 = Debug.getRuntimeStat("art.gc.blocking-gc-time");

            LogPrinter logPrinter = new LogPrinter(Log.DEBUG, TAG);
            logPrinter.println("runtimeStat=" + runtimeStat + "=runtimeStat1="
                    + runtimeStat1 + "=runtimeStat2=" + runtimeStat2 + "=runtimeStat3=" + runtimeStat3);

        }
        listPackages();

    }

    class PInfo {
        private String appname = "";
        private String pname = "";
        private String versionName = "";
        private int versionCode =0;
        private Drawable icon;
        private void prettyPrint() {
            Log.d(TAG, appname + "\t" + pname + "\t" + versionName + "\t" + versionCode +"\t");
        }
    }

    private void listPackages() {
        ArrayList<PInfo> apps = getInstalledApps(false);/* false = no system packages */
        final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).prettyPrint();
        }
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName ==null)) {
                continue ;
            }
            PInfo newInfo = new PInfo();
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            res.add(newInfo);
        }
        return res;
    }
}
