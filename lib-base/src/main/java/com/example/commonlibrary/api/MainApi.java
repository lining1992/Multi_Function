package com.example.commonlibrary.api;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.bean.NetworkModel;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public interface MainApi {

    @POST(".")
    Observable<NetworkModel<BaseBean>> getResult();
}
