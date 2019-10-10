//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.http.network.request;

import java.util.Map;

public interface IHttpRequest {
    String getUrl();

    Map<String, String> getParams();

    void execute();

    void cancel();
}
