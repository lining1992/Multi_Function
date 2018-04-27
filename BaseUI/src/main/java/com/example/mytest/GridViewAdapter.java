package com.example.mytest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mytest.adapter.ExLvAdapter;
import com.example.mytest.adapter.ExLvViewHolder;

import java.util.List;

/**
 * Created by lijuan on 2016/9/12.
 */
public class GridViewAdapter extends ExLvAdapter<GridViewAdapter.ViewHolder,Model> {
    private List<Model> mDatas;
    private LayoutInflater inflater;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize;

    public GridViewAdapter(List<Model> mDatas, int curIndex, int pageSize) {
        this.mDatas = mDatas;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页,如果够，则直接返回每一页显示的最大条目个数pageSize,如果不够，则有几项就返回几,(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);

    }

    @Override
    public Model getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflateLayout(parent, R.layout.item));
    }


    class ViewHolder extends ExLvViewHolder<Model> {
        public TextView tv;
        public ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text);
            iv = (ImageView) itemView.findViewById(R.id.image);
        }

        @Override
        public void invalidateItemView(int position, Model model) {
            /**
             * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
             */
            int pos = position + curIndex * pageSize;
            tv.setText(model.name);
            iv.setBackgroundResource(R.mipmap.album);
//            Glide.with(getContext()).load(model.iconRes).crossFade(1000).
//                    placeholder(R.mipmap.album).error(R.mipmap.album)
//                    .bitmapTransform(new GlideCircleTransform(getContext()),
//                            new AlbumTransformation(getContext())).
//                    into(iv);
        }
    }
}