package com.example.myapplication.fragment;

import static android.os.Looper.getMainLooper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.myapplication.HomeInfoDetailClickListener;
import com.example.myapplication.R;
import com.example.myapplication.RentClickListener;
import com.example.myapplication.activity.InfoDetailActivity;
import com.example.myapplication.adapter.HomeInfoAdapter;
import com.example.myapplication.adapter.ParkAdapter;
import com.example.myapplication.dao.InfoDao;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.entity.Info;
import com.example.myapplication.entity.Park;
import com.example.myapplication.utils.JDBCUtils;
import com.example.myapplication.utils.ToastUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private SwipeRefreshLayout re_info;
    private ListView info_list;
    private Handler mainHandler;

    private HomeInfoAdapter homeInfoAdapter;

    private List<Info> infoList;

    private Context context;

    private HomeInfoDetailClickListener homeInfoDetailClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainHandler=new Handler(getMainLooper());
        info_list=view.findViewById(R.id.info_list);
        re_info=view.findViewById(R.id.re_info);
        re_info.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //处理刷新逻辑
                loadInfo();
                //停止刷新
                re_info.setRefreshing(false);
            }
        });


        loadInfo();

//        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.header_view, null);
//        park_list.addHeaderView(headerView,null,false);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadInfo();
    }

    private void loadInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InfoDao infoDao = new InfoDao();
                infoList=infoDao.getAllInfo();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showInfoList();
                    }
                });
            }
        }).start();
    }

    private void showInfoList() {
        if(homeInfoAdapter==null){        //首次加载的操作
            homeInfoAdapter= new HomeInfoAdapter(context,infoList);
            info_list.setAdapter(homeInfoAdapter);
        }else{      //更新数据的操作
            homeInfoAdapter.setInfoList(infoList);
            homeInfoAdapter.notifyDataSetChanged();
        }


        homeInfoAdapter.setHomeInfoDetailClickListener(new HomeInfoDetailClickListener() {
            @Override
            public void HomeInfoDetailClick(View view, int position) {
                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                final Info item=infoList.get(position);
                intent.putExtra("info_id",item.getInfo_id());
//                intent.putExtra("info_title",item.getInfo_title());
//                intent.putExtra("info_desc",item.getInfo_desc());
//                intent.putExtra("info_pic",item.getInfo_pic());
//                intent.putExtra("info_date",item.getInfo_date());

                startActivity(intent);
            }
        });



    }



}