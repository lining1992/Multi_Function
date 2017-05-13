package com.baidu.lining.displayadapter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.base.BaseFragment;
import com.zhy.autolayout.utils.AutoUtils;
import com.zhy.base.adapter.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/27.
 */
public class RecycleFragment extends BaseFragment {

    @BindView(R.id.recycle_rv)
    public RecyclerView recycle;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_recycler;
    }

    public void initData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            list.add(i + "");
        }

        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setAdapter(new RecycAdapter(list));

        recycle.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
    }

    class RecycAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private final List<String> list;

        public RecycAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            AutoUtils.autoSize(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //  holder.iv.setImageDrawable(R.drawable.tuijian_touxiang6);
            // holder.tv2.setText();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name_tv, num_tv, tv2, tv3;
        public ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.iv);
            name_tv = (TextView) view.findViewById(R.id.name_tv);
            num_tv = (TextView) view.findViewById(R.id.num_tv);
            tv2 = (TextView) view.findViewById(R.id.tv2);
            tv3 = (TextView) view.findViewById(R.id.tv3);
        }
    }
}
