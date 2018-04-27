//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.network.callback;

import java.util.List;

public interface HttpCallback {
    void onSuccess(String var1, int var2, String var3);

    void onCookies(String var1, List<String> var2);

    void onError(String var1, String var2);

    void onCancel(String var1);
}
