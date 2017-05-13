package com.baidu.lining.displayadapter.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.ui.adapter.CityListAdapter;
import com.baidu.lining.displayadapter.base.BaseFragment;
import com.baidu.lining.displayadapter.bean.City;
import com.baidu.lining.displayadapter.constants.DataConstants;
import com.baidu.lining.displayadapter.view.DividerDecoration;
import com.baidu.lining.quicksidebar.QuickSideBarTipsView;
import com.baidu.lining.quicksidebar.QuickSideBarView;
import com.baidu.lining.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/10.
 */
public class PersonFragment extends BaseFragment implements OnQuickSideBarTouchListener {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.quickSideBarView)
    public QuickSideBarView quickSideBarView;
    @BindView(R.id.quickSideBarTipsView)
    public QuickSideBarTipsView quickSideBarTipsView;
    @BindView(R.id.main_title)
    public TextView title;

    HashMap<String,Integer> letters = new HashMap<>();

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_person;
    }

    @Override
    public void initView() {
        super.initView();
        title.setText("Person");
    }

    public void initData(){
        //设置监听
        quickSideBarView.setOnQuickSideBarTouchListener(this);


        //设置列表数据和浮动header
        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Add the sticky headers decoration
        CityListWithHeadersAdapter adapter = new CityListWithHeadersAdapter();

        //GSON解释出来
        Type listType = new TypeToken<LinkedList<City>>(){}.getType();
        Gson gson = new Gson();
        LinkedList<City> cities = gson.fromJson(DataConstants.cityDataList, listType);

        ArrayList<String> customLetters = new ArrayList<>();

        int position = 0;
        for(City city: cities){
            String letter = city.getFirstLetter();
            //如果没有这个key则加入并把位置也加入
            if(!letters.containsKey(letter)){
                letters.put(letter,position);
                customLetters.add(letter);
            }
            position++;
        }

        //不自定义则默认26个字母
        quickSideBarView.setLetters(customLetters);
        adapter.addAll(cities);
        recyclerView.setAdapter(adapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);

        recyclerView.addItemDecoration(new DividerDecoration(activity));
    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        quickSideBarTipsView.setText(letter, position, y);
        //有此key则获取位置并滚动到该位置
        if(letters.containsKey(letter)) {
            recyclerView.scrollToPosition(letters.get(letter));
        }

    }

    @Override
    public void onLetterTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        quickSideBarTipsView.setVisibility(touching? View.VISIBLE:View.INVISIBLE);
    }

    private class CityListWithHeadersAdapter extends CityListAdapter<RecyclerView.ViewHolder>
            implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(getItem(position).getCityName());
        }

        @Override
        public long getHeaderId(int position) {
            Log.d("debugli","======="+getItem(position).getFirstLetter().charAt(0));
            return getItem(position).getFirstLetter().charAt(0);
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_contacts_head, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            View itemView = holder.itemView;
            TextView textView = (TextView) itemView.findViewById(R.id.mHead);
            textView.setText(String.valueOf(getItem(position).getFirstLetter()));
            Log.d("debugli",String.valueOf(getItem(position).getFirstLetter()));
        }
    }
}
