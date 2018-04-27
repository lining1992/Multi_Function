//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.network.callback;

public interface DownloadCallback {
    void onDownloadStart(String var1, String var2);

    void onDownloadProgress(String var1, long var2, long var4);

    void onDownloadError(String var1, String var2);

    void onDownloadComplete(String var1, String var2);
}
