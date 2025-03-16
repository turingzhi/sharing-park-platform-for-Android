package com.example.myapplication.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.MyFragment;
import com.example.myapplication.fragment.ParkFragment;


/*
* 主页面
* */
public class MainActivity extends AppCompatActivity implements TabHost.TabContentFactory{
    private TabHost mTabHost;
    private ViewPager viewPager;
    private HomeFragment homeFragment = new HomeFragment();
    private ParkFragment parkFragment = new ParkFragment();
    private MyFragment myFragment = new MyFragment();

    private Fragment[] fragments= new Fragment[]{
            homeFragment,parkFragment,myFragment
    };

    private ImageView addImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化总布局
        mTabHost = findViewById(R.id.tab_host0);
        mTabHost.setup();

        //三个Tab处理

        //1.初始化数据
        int[]titleIDs={R.string.home, R.string.park, R.string.my};

        int[] drawableIDs = {R.drawable.main_tab_icon_home, R.drawable.main_tab_icon_park, R.drawable.main_tab_icon_my};

        //data<-->view
        for (int index = 0; index < titleIDs.length; index++) {
            //提取自己写的按钮
            View view=getLayoutInflater().inflate(R.layout.activity_tab,null,false);
            //获取图标
            ImageView icon=view.findViewById(R.id.main_tab_icon0);
            //获取图标命名
            TextView title=view.findViewById(R.id.main_tab_text0);
            //获取背景
            View tab=view.findViewById(R.id.tab_bg0);
            //设置图标
            icon.setImageResource(drawableIDs[index]);
            //设置图标命名
            title.setText(titleIDs[index]);
            //设置背景颜色
            tab.setBackgroundColor(getResources().getColor(R.color.white));

            //添加tabhost
            mTabHost.addTab(
                    mTabHost.newTabSpec(getString(titleIDs[index]))//以title名设置TAG
                            .setIndicator(view)//以view来分割
                            .setContent(this)//一个tabhost的内容（感觉两者有点重复）
            );

        }

        viewPager = findViewById(R.id.view_pager0);


        //在viewpager添加fragments
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

        //设置ViewPage缓存界面数
        viewPager.setOffscreenPageLimit(3);

        // viewpager切换
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                if(mTabHost!=null){
                    mTabHost.setCurrentTab(position);       //设置当前viewpager和tabhost的互动（共同变换）
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //tab切换
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(mTabHost!=null) {
                    int position = mTabHost.getCurrentTab();
                    viewPager.setCurrentItem(position);     //设置当前tabhost和viewpager的互动（共同变换）
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int status=intent.getIntExtra("status",1);
        if(status==0){
            mTabHost.setCurrentTab(1);
            viewPager.setCurrentItem(1);
        }

    }


    public View createTabContent(String tag) {
        View view =new View(this);// 一个tabhost对应一个页面
        view.setMinimumHeight(0);         // 但是这里内容为用的viewpager//
        view.setMinimumWidth(0);          // 所以不用可以隐藏
        return view;
    }



}

