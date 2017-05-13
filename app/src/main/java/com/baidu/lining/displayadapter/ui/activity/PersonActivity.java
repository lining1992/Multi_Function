package com.baidu.lining.displayadapter.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.ui.adapter.PersonAdapter;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
public class PersonActivity extends AutoLayoutActivity{

    private Activity activity;
    List<String> list = new ArrayList<String>();
    private RecyclerView rl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_person);

        bindView();
        initData();
    }

    private void initData() {
        for(int i=0;i<20;i++){
            list.add("Hello"+i);
        }

        Log.d("debugli",list.get(0).toString());
        rl.setHasFixedSize(true);
        rl.setLayoutManager(new LinearLayoutManager(this));
        rl.setAdapter(new PersonAdapter(this,list));
    }

    private void bindView() {
        CollapsingToolbarLayout ctb = (CollapsingToolbarLayout)findViewById(R.id.person_ctb);
        Toolbar tb = (Toolbar) findViewById(R.id.person_tb);
        rl = (RecyclerView) findViewById(R.id.person_rl);

        tb.setTitle("Person");

        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ctb.setTitle("CollapsingToolbarLayout");
        ctb.setExpandedTitleColor(Color.WHITE);
        ctb.setCollapsedTitleTextColor(Color.BLACK);
    }
}
