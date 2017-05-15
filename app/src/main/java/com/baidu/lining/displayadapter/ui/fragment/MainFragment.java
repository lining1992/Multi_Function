package com.baidu.lining.displayadapter.ui.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.base.BaseFragment;
import com.baidu.lining.displayadapter.bean.ChannelItem;
import com.baidu.lining.displayadapter.ui.activity.ChannelActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/9.
 */
public class MainFragment extends BaseFragment{

    private ArrayList<Fragment> fragmentList;

    private String[] titles = new String[]{"详情列表","登陆注册","移动支付","recyc列表","表格列表","输入框"};;
    private List<ChannelItem> channelList;

    private ListFragment listFragment;
    private RegisterFragment registerFragment;
    private PayFragment payFragment;
    private RecycleFragment recycleFragment;
    private RecycleViewGridFragment recycleViewGridFragment;
    private EditFragment editFragment;
    private ArrayList<Fragment> fragmentArrayList;

    @BindView(R.id.tb)
    public TabLayout tb;
    @BindView(R.id.vp)
    public ViewPager vp;
    @BindView(R.id.wel_add_tab)
    public ImageView addTab;
    @BindView(R.id.main_title)
    public TextView title;
    @BindView(R.id.iv)
    public ImageView iv;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_welcome;
    }

    @Override
    public void initView(){
        title.setText("Home");

        listFragment = new ListFragment();
        registerFragment = new RegisterFragment();
        payFragment = new PayFragment();
        recycleFragment = new RecycleFragment();
        recycleViewGridFragment = new RecycleViewGridFragment();
        editFragment = new EditFragment();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.toggleDrawer();
            }
        });

        addTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                if(fragmentArrayList!=null && fragmentArrayList.size()>0){
                    intent.putExtra("main",fragmentArrayList.size());
                }else{
                    intent.putExtra("main",titles.length);
                }
                startActivityForResult(intent,1);
                getActivity().overridePendingTransition(0,0);
            }
        });
    }

    @Override
    public void initData() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(listFragment);
        fragmentList.add(registerFragment);
        fragmentList.add(payFragment);
        fragmentList.add(recycleFragment);
        fragmentList.add(recycleViewGridFragment);
        fragmentList.add(editFragment);

        MyPagerAdapter adapter = new MyPagerAdapter(activity.getSupportFragmentManager(),
                fragmentList,fragmentArrayList);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);

        tb.setupWithViewPager(vp);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> list;
        private final ArrayList<Fragment> fragmentArrayList;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> list,
                              ArrayList<Fragment> fragmentArrayList){
            super(fm);
            this.list = list;
            this.fragmentArrayList = fragmentArrayList;
        }

        @Override
        public int getCount() {
            if(fragmentArrayList != null){
                return fragmentArrayList.size();
            }else{
                return list.size();
            }
        }

        @Override
        public Fragment getItem(int position) {
            if(fragmentArrayList != null && fragmentArrayList.size()>position){
                return fragmentArrayList.get(position);
            }else{
                return list.get(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(channelList != null){
                return channelList.get(position).getName();
            }else{
                return titles[position];
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == 100){
                    setChangelView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setChangelView(Intent data) {
        initTab(data);
    }

    private void initTab(Intent data) {

        channelList = (List<ChannelItem>) data.getSerializableExtra("channel");
        
        if(channelList !=null){
            Log.d("debugli", channelList.size()+"");
            fragmentArrayList = new ArrayList<Fragment>();
            for(int i=0;i<channelList.size();i++){
                switch (i){
                    case 0:
                        fragmentArrayList.add(new ListFragment());
                        break;
                    case 1:
                        fragmentArrayList.add(new PayFragment());
                        break;
                    case 2:
                        fragmentArrayList.add(new RecycleFragment());
                        break;
                    case 3:
                        fragmentArrayList.add(new RegisterFragment());
                        break;
                    case 4:
                        fragmentArrayList.add(new RecycleViewGridFragment());
                        break;
                    case 5:
                        fragmentArrayList.add(new EditFragment());
                        break;
                }
            }
            MyPagerAdapter adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(),fragmentList, fragmentArrayList);
            vp.setAdapter(adapter);
            vp.setCurrentItem(0);

            tb.setupWithViewPager(vp);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        fragmentList = null;
        fragmentArrayList = null;
    }

    @Override
    public void onResume() {
        super.onResume();
     //   initData();
    }
}
