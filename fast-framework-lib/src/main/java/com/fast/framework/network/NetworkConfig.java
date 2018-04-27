/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

/**
 * Created by lishicong on 2016/12/13.
 */

public class NetworkConfig {

    private static final String ERROR_INIT_CONFIG_WITH_NULL = "network config can not be initialized with null";

    private Builder config;
    private static NetworkConfig instance;

    private NetworkConfig() {

    }

    public static NetworkConfig getInstance() {
        if (instance == null) {
            synchronized (NetworkConfig.class) {
                if (instance == null) {
                    instance = new NetworkConfig();
                }
            }
        }
        return instance;
    }

    public synchronized void init(Builder config) {
        if (config == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        }
        if (this.config == null) {
            this.config = config;
        }
    }

    public static class Builder {

        private String httpServerBaseUrl;

        public void setHttpServerBaseUrl(String httpServerBaseUrl) {
            this.httpServerBaseUrl = httpServerBaseUrl;
        }

        public Builder build() {
            return this;
        }
    }

    public String getHttpServerUrl() {
        return config.httpServerBaseUrl;
    }

}
