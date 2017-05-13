package com.baidu.lining.displayadapter.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.base.BaseFragment;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/27.
 */
public class RecycleViewGridFragment extends BaseFragment {

    @BindView(R.id.grid_rv)
    public RecyclerView grid_rv;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_recyc_grid;
    }

    public void initData() {
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            list.add(i + "");
        }

        grid_rv.setLayoutManager(new GridLayoutManager(getActivity(), 2,
                GridLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter(list);
        grid_rv.setAdapter(myAdapter);
        myAdapter.setOnItemClick(new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                String s = list.get(position);
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnItemClickListener {
        public void OnItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private final List<String> list;
        private OnItemClickListener onItemClick;

        public MyAdapter(List<String> list) {
            this.list = list;
        }

        public void setOnItemClick(OnItemClickListener onItemClick) {
            this.onItemClick = onItemClick;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.grid_list_item, parent, false);

            MyViewHolder viewHolder = new MyViewHolder(view, onItemClick);
            AutoUtils.autoSize(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.grid_tv.setText(list.get(position));
        }


        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView grid_tv;
        public OnItemClickListener onItemClick;


        public MyViewHolder(View view, OnItemClickListener onItemClick) {
            super(view);

            this.onItemClick = onItemClick;

            Random random = new Random();
            view.setBackgroundColor(Color.argb(200, random.nextInt(255),
                    random.nextInt(255), random.nextInt(255)));

            grid_tv = (TextView) view.findViewById(R.id.grid_tv);

            view.setOnClickListener(this);

            //recyclerview，注意添加这一行
            AutoUtils.autoSize(itemView, AutoAttr.BASE_HEIGHT);
        }

        @Override
        public void onClick(View view) {
            onItemClick.OnItemClick(view, getAdapterPosition());
        }
    }
}
