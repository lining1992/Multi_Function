package com.example.commonlibrary.mvp.model;

import com.example.commonlibrary.api.MainApi;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.bean.Result;
import com.example.commonlibrary.mvp.MainCantract;
import com.example.commonlibrary.net.ResultObserver;
import com.example.commonlibrary.net.RetrofitClient;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class MainModel implements MainCantract.Model {

    @Override
    public Result loadData() {
        Result<BaseBean> result = new Result();
        RetrofitClient.createService(MainApi.class).getResult()
                .compose(RetrofitClient.<BaseBean>handleResultSync())
                .subscribe(new ResultObserver<>(result));
        return result;
    }
}
