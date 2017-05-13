package com.baidu.lining.displayadapter.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.base.BaseFragment;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/27.
 */
public class ListFragment extends BaseFragment {

    private List<String> list;

    @BindView(R.id.lv)
    public ListView lv;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_list;
    }

    public void initView() {


        list = new ArrayList<String>();

        for (int i = 0; i < 50; i++) {
            list.add(i + "");
        }

        lv.setAdapter(new MyAdapter());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(activity, "点击了" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item, null);
                view.setTag(viewHolder);

                AutoUtils.autoSize(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            return view;
        }
    }

    class ViewHolder {

    }
}
