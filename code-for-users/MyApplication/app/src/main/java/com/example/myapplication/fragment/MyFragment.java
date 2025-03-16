package com.example.myapplication.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activity.CurrentOrderActivity;
import com.example.myapplication.activity.HistoryRentOrderActivity;
import com.example.myapplication.activity.HistoryRentedOrderActivity;
import com.example.myapplication.activity.MyInfoActivity;
import com.example.myapplication.activity.MyParkActivity;
import com.example.myapplication.activity.PasswordActivity;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.Base64Util;
import com.example.myapplication.utils.ToastUtil;


public class MyFragment extends Fragment {

    private TextView tv_username;

    private TextView tv_nickname;

    private RelativeLayout re_myInfo;

    private RelativeLayout re_updateInfo;

    private RelativeLayout re_myPark;

    private RelativeLayout re_historyParkToRent;

    private RelativeLayout re_historyParkRented;

    private RelativeLayout re_exit;

    private RelativeLayout re_currentOrder;

    private Button btn_exit;

    private Context context;

    private RelativeLayout re_updatePassword;

    private ImageView iv_user_pic;

    private static final String TAG = "mysql-app-MyFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        re_myInfo=view.findViewById(R.id.re_myInfo);
        tv_username = view.findViewById(R.id.tv_username);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        re_updateInfo = view.findViewById(R.id.re_updateInfo);
        re_myPark = view.findViewById(R.id.re_myPark);
        re_historyParkToRent = view.findViewById(R.id.re_historyParkToRent);
        re_historyParkRented = view.findViewById(R.id.re_historyParkRented);
        re_currentOrder=view.findViewById(R.id.re_currentOrder);
        re_exit = view.findViewById(R.id.re_exit);
        btn_exit=view.findViewById(R.id.btn_exit);
        re_updatePassword=view.findViewById(R.id.re_updatePassword);
        iv_user_pic=view.findViewById(R.id.iv_user_pic1);

        loadData();




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
    private void loadData(){
        SharedPreferences spf = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        int user_id = spf.getInt("user_id", -1);
        final int status=spf.getInt("status",-1);
        String username = spf.getString("username", "null");
        String nickname = spf.getString("nickname", "null");
        String user_pic=spf.getString("user_pic","null");
        if(user_pic!="null"){
            Bitmap bitmap_user_pic= Base64Util.stringToBitmap(user_pic);
            iv_user_pic.setImageBitmap(bitmap_user_pic);
        }
        Log.e(TAG, "onCreateView: user_id" + user_id);
        Log.e(TAG, "onCreateView: username" + username);

        UserDao userDao = new UserDao();


        tv_username.setText(username);
        tv_nickname.setText(nickname);

        re_currentOrder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(status==1){
                    ToastUtil.showCenter(context,"目前没有进行中的订单");
                }else {
                    Intent intent = new Intent(getActivity(), CurrentOrderActivity.class);
                    startActivity(intent);
                }

            }
        });

        re_updateInfo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(intent);
            }
        });

        re_updatePassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PasswordActivity.class);
                startActivity(intent);
            }
        });

        re_myInfo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(intent);
            }
        });

        re_myPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyParkActivity.class);
                startActivity(intent);
            }
        });


        re_historyParkToRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryRentOrderActivity.class);
                startActivity(intent);
            }
        });


        re_historyParkRented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryRentedOrderActivity.class);
                startActivity(intent);
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1.创建弹框对象,显示在当前页面
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                // 2.编辑弹框样式
                // 2.1 创建标题
                ab.setTitle("提示");
                // 2.3 设置图标
                ab.setIcon(R.mipmap.park_normal);
                // 2.4 设置内容
                ab.setMessage("您是否确定退出？");
                // 2.5 设置按钮
                ab.setPositiveButton("取消",null);
                ab.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 实现程序的退出，结束当前
                        getActivity().finish();
                    }
                });
                // 3.创建弹框
                ab.create();
                // 4.显示弹框
                ab.show();


            }
        });
    }
}

