package com.baidu.lining.displayadapter.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.ui.adapter.PersonAdapter;
import com.baidu.lining.displayadapter.base.BaseFragment;
import com.baidu.lining.mylibrary.CaledarAdapter;
import com.baidu.lining.mylibrary.CalendarBean;
import com.baidu.lining.mylibrary.CalendarDateView;
import com.baidu.lining.mylibrary.CalendarUtil;
import com.baidu.lining.mylibrary.CalendarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/5/10.
 */
public class LocationFragment extends BaseFragment{

    @BindView(R.id.list)
    public RecyclerView mList;
    @BindView(R.id.calendarDateView)
    public CalendarDateView mCalendarDateView;
    @BindView(R.id.main_title)
    public TextView title;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_location;
    }

    @Override
    public void initData() {

        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if(convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_xiaomi, parentView, false);
                }
                TextView text= (TextView) convertView.findViewById(R.id.text);
                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                text.setText(""+bean.day);
                chinaText.setText(bean.chinaDay);
                return convertView;
            }
        });

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
            title.setText(bean.year + "/" + bean.moth + "/" + bean.day);
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
            title.setText(data[0] + "/" + data[1] + "/" + data[2]);
    }

    public void initView() {

        List<String> list = new ArrayList<String>();
        for(int i=0;i<20;i++){
            list.add("position"+i);
        }
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(new PersonAdapter(getActivity(),list));

    }
}
