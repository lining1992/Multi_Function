package com.example.commonlibrary.mvp;

import com.example.commonlibrary.bean.Result;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public interface MainCantract {

    interface View {
        void bindPresenter(Presenter presenter);
        void onSuccess();
        void onFail();
    }

    interface Model {
        Result loadData();
    }

    interface Presenter {
        void bindView(View view);
        void loadData();
    }
}
