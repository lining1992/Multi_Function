package com.baidu.lining.displayadapter.ui.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.base.BaseActivity;
import com.baidu.lining.displayadapter.base.BaseFragment;
import com.baidu.lining.displayadapter.refresh.LoadMoreListener;
import com.baidu.lining.displayadapter.refresh.MoreRecycleView;
import com.baidu.lining.displayadapter.ui.adapter.PersonAdapter;
import com.baidu.lining.displayadapter.view.ProgressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/16.
 */
public class MuiltTypeRecyc extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_title)
    TextView title;
    @BindView(R.id.recyclerView)
    MoreRecycleView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    List list = new ArrayList<>();
    private PersonAdapter recycAdapter;
    private int limit = 10;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_muilt_type;
    }

    @Override
    public void initData() {
        super.initData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                //注意此处
                recyclerView.refreshComplete();
                lodeMoreData();
            }
        });

        refreshLayout.post(()->{
            refreshLayout.setRefreshing(true);
            recyclerView.refreshComplete();
            lodeData();
        });

        recyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        if (list.size() >= 3 * limit) {
                            recyclerView.loadMoreEnd();
                            return;
                        }

                        for (int i = 0; i < limit; i++) {
                            list.add(i + "");
                        }
                        recycAdapter.notifyDataSetChanged();
                        recyclerView.loadMoreComplete();

                    }
                }, 2000);
            }
        });
    }

    private void lodeMoreData() {
        for(int i=0;i<50;i++){
            list.add("position"+i);
        }
        recycAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    private void lodeData() {

        for(int i=0;i<20;i++){
            list.add("position"+i);
        }
        recycAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);

    }

    @Override
    public void initView() {
        super.initView();
        title.setText("刷新列表");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycAdapter = new PersonAdapter(this,list);

        //设置自定义加载中和到底了效果
        ProgressView progressView = new ProgressView(this);
        progressView.setIndicatorId(ProgressView.BallPulse);
        progressView.setIndicatorColor(0xff69b3e0);
        recyclerView.setFootLoadingView(progressView);

        TextView textView = new TextView(this);
        textView.setText("已经到底啦~");
        recyclerView.setFootEndView(textView);
        recyclerView.setAdapter(recycAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    }
}
