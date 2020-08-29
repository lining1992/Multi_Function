package com.example.commonlibrary.android.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/2 14:37
 * <p>
 * description : 用于展示{@code RecyclerView}列表的{@ViewHolder}的基类，
 * 与{@link RecyclerAdapter}配合使用，支持{@code Item}的
 * 渲染({@link BaseViewHolder#onBindItemView(Object, int)})
 * 和局部刷新({@link BaseViewHolder#onRefreshItemView(List <Object>)})
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected void onRefreshItemView(List<Object> payloads) {

    }

    protected abstract void onBindItemView(T t, int position);
}
