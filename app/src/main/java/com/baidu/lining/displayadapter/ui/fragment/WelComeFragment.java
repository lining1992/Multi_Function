package com.baidu.lining.displayadapter.ui.fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/9.
 */

public class WelComeFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private MainFragment home;
    private LocationFragment location;
    private LikeFragment like;
    private PersonFragment person;

    @BindView(R.id.rg)
    public RadioGroup rg;
    @BindView(R.id.rb_home)
    public RadioButton rb_home;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_welcome1;
    }

    @Override
    public void initView() {

        rg.setOnCheckedChangeListener(this);
        rb_home.setChecked(true);


    }

    public void hideAllFragment(FragmentTransaction transaction) {
        if (home != null) {
            transaction.hide(home);
        }
        if (location != null) {
            transaction.hide(location);
        }
        if (like != null) {
            transaction.hide(like);
        }
        if (person != null) {
            transaction.hide(person);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        hideAllFragment(ft);
        switch (i) {
            case R.id.rb_home:
                if (home == null) {
                    home = new MainFragment();
                    ft.add(R.id.wel_fl, home);
                } else {
                    ft.show(home);
                }
                break;
            case R.id.rb_location:
                if (location == null) {
                    location = new LocationFragment();
                    ft.add(R.id.wel_fl, location);
                } else {
                    ft.show(location);
                }
                break;
            case R.id.rb_like:
                if (like == null) {
                    like = new LikeFragment();
                    ft.add(R.id.wel_fl, like);
                } else {
                    ft.show(like);
                }
                break;
            case R.id.rb_person:
                if (person == null) {
                    person = new PersonFragment();
                    ft.add(R.id.wel_fl, person);
                } else {
                    ft.show(person);
                }
                break;
        }
        ft.commit();
    }
}
