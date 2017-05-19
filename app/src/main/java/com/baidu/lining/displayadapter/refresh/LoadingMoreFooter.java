package com.baidu.lining.displayadapter.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.lining.displayadapter.R;

/**
 * Created by lining on 2017/5/17.
 */
public class LoadingMoreFooter extends LinearLayout {

    private LinearLayout loading_view_layout;
    private LinearLayout end_layout;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        // 设置布局居中
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        View view = LayoutInflater.from(context).inflate(R.layout.footer_layout, null);
        // 底部正在加载
        loading_view_layout = (LinearLayout) view.findViewById(R.id.loading_view_layout);
        // 底部加载到底了，无数据了
        end_layout = (LinearLayout) view.findViewById(R.id.end_layout);

        addFootLoadingView(new ProgressBar(context,null,android.R.attr.progressBarStyle));

        TextView textView = new TextView(context);
        textView.setText("已经到底啦~");
        addFootEndView(textView);

        addView(view);
    }

    public void addFootLoadingView(View view) {
        loading_view_layout.removeAllViews();
        loading_view_layout.addView(view);
    }

    public void addFootEndView(View view) {
        end_layout.removeAllViews();
        end_layout.addView(view);
    }

    // 设置已经没有更多数据了
    public void setEnd(){
        setVisibility(View.VISIBLE);
        loading_view_layout.setVisibility(View.GONE);
        end_layout.setVisibility(View.VISIBLE);
    }

    // 设置正在加载数据
    public void setVisible(){
        setVisibility(View.VISIBLE);
        loading_view_layout.setVisibility(View.VISIBLE);
        end_layout.setVisibility(View.GONE);
    }

    public void setGone(){
        setVisibility(View.GONE);
    }
}
