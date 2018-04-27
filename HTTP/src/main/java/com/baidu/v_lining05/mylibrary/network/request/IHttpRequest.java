//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.network.request;

import java.util.Map;

public interface IHttpRequest {
    String getUrl();

    Map<String, String> getParams();

    void execute();

    void cancel();
}
