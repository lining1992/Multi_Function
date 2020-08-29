package com.example.commonlibrary.net;

/**
 * @author : leixing
 *         email : leixing@baidu.com
 * @date : 2018/8/2 20:22
 * <p>
 * description : xxx
 */
public interface ErrorCode {
    int UNKNOWN_ERROR = 1000;
    int NET_CONNECT_FAILURE = 1001;
    int NO_RESULT = 1002;
    int UNKNOWN_HOST = 1003;
    int SOCKET_TIME_OUT = 1004;
    int DATA_PARSE_ERROR = 1005;
    int SOCKET_FAILURE = 1006;
    int CANCEL = 2000;
}
