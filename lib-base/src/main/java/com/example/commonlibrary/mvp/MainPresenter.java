package com.example.commonlibrary.mvp;

import com.example.commonlibrary.bean.Result;
import com.example.commonlibrary.mvp.model.MainModel;
import com.example.commonlibrary.utils.LogUtil;
import com.example.commonlibrary.utils.TaskExecutor;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class MainPresenter extends BasePresenter<MainCantract.View> implements MainCantract.Presenter {


    private final MainModel mainModel;

    public MainPresenter() {
        mainModel = new MainModel();
    }

    @Override
    public void bindView(MainCantract.View view) {
        setView(view);
    }

    @Override
    public void loadData() {
        LogUtil.d("load data===");
        TaskExecutor.io(new Runnable() {
            @Override
            public void run() {
                final Result result = mainModel.loadData();
                TaskExecutor.ui(new Runnable() {
                    @Override
                    public void run() {
                        if (getView() != null ) {
                            if (result.isSucceed()) {
                                LogUtil.d("load data===" + result.getData());
                                getView().onSuccess();
                            } else {
                                getView().onFail();
                            }
                        }
                    }
                });
            }
        });

    }


}
