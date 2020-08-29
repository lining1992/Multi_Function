package com.example.commonlibrary.net;

import com.example.commonlibrary.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * description : BaseObserver 用于网络请求
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/22 19:46
 */
public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }


    @Override
    public void onError(Throwable e) {
        LogUtil.e("BaseSubscriber===", e.getMessage() + "==Exception==" + e);
        int errorCode;
        String errorMsg;
        if (e instanceof java.net.ConnectException) {
            errorCode = ErrorCode.SOCKET_FAILURE;
            errorMsg = "服务器连接失败";
        } else if (e instanceof javax.net.ssl.SSLException) {
            errorCode = ErrorCode.SOCKET_TIME_OUT;
            errorMsg = "连接超时，请稍后再试";
        } else if (e instanceof java.net.UnknownHostException) {
            errorCode = ErrorCode.UNKNOWN_HOST;
            errorMsg = "无网络连接";
        } else if (e instanceof java.net.SocketTimeoutException) {
            errorCode = ErrorCode.SOCKET_TIME_OUT;
            errorMsg = "连接超时，请稍后再试";
        } else if (e instanceof org.json.JSONException || e instanceof android.net.ParseException) {
            errorCode = ErrorCode.DATA_PARSE_ERROR;
            errorMsg = "服务器数据解析错误异常";
        } else if (e instanceof NetworkException
                && ((NetworkException) e).getErrorCode() == ErrorCode.SOCKET_TIME_OUT) {
            errorCode = ErrorCode.SOCKET_TIME_OUT;
            errorMsg = "当前系统时间不准确，请校正";
        } else {
            errorCode = ErrorCode.UNKNOWN_ERROR;
            errorMsg = "请求失败";
        }

        onFailure(errorCode, errorMsg, e);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    /**
     * 请求成功的回调
     *
     * @param t 数据
     */
    protected abstract void onSuccess(T t);

    /**
     * 请求失败的回调
     *
     * @param errorCode 错误码
     * @param errorMsg  错误信息
     * @param throwable 异常
     */
    protected abstract void onFailure(int errorCode, String errorMsg, Throwable throwable);
}
