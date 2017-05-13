package com.baidu.lining.displayadapter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.lining.displayadapter.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyHolder>{

    private List<String> list;
    private Context context;

    public PersonAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public PersonAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_item,parent,false);
        AutoUtils.autoSize(view);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonAdapter.MyHolder holder, int position) {
        holder.tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        private final TextView tv;

        public MyHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.person_tv);
        }
    }
}
