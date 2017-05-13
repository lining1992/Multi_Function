package com.baidu.lining.displayadapter.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.ui.fragment.WelComeFragment;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AutoLayoutActivity implements NavigationView.OnNavigationItemSelectedListener{


    @BindView(R.id.main_navi)
    public NavigationView navigationView;
    //    @BindView(R.id.toolbar)
//    public Toolbar toolbar;
    @BindView(R.id.main_draw)
    public DrawerLayout drawerLayout;

    private FrameLayout fl;
    public static Toolbar toolbar;
    public String packageName;
    public TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setImmersionStatus();

        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);
        initView();

    }

    // 设置透明状态栏
    private void setImmersionStatus() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView() {
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fl,new WelComeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_new:
                Toast.makeText(this,"我是咨询",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_tweet:
                startActivity(new Intent(this, PersonActivity.class));
                break;
            case R.id.menu_blog:
                break;
        }
        return false;
    }

    /**
     * DrawerLayout侧滑菜单开关
     */
    public void toggleDrawer() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }



}
