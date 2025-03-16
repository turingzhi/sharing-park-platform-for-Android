package com.example.myapplication.fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Looper.getMainLooper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.RentClickListener;
import com.example.myapplication.ToMyParkClickListener;
import com.example.myapplication.activity.HistoryRentOrderActivity;
import com.example.myapplication.activity.MyParkActivity;
import com.example.myapplication.adapter.ParkAdapter;
import com.example.myapplication.dao.OrderDao;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Order;
import com.example.myapplication.entity.Park;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.JDBCUtils;
import com.example.myapplication.utils.ToastUtil;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ParkFragment extends Fragment {

    private SwipeRefreshLayout re_park;
    private ListView park_list;
    private Handler mainHandler;

    private TextView tv_my_park;

    private ParkAdapter parkAdapter;

    private List<Park> parkList;

    private Context context;

    private RentClickListener rentClickListener;

    private ToMyParkClickListener toMyParkClickListener;

    private static final String TAG = "mysql-app-ParkFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park, container, false);
        mainHandler=new Handler(getMainLooper());
        park_list=view.findViewById(R.id.park_list);
        re_park=view.findViewById(R.id.re_park);
        tv_my_park=view.findViewById(R.id.tv_my_park);




        re_park.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //处理刷新逻辑
                loadPark();
                //停止刷新
                re_park.setRefreshing(false);
            }
        });


        loadPark();


        tv_my_park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyParkActivity.class);
                startActivity(intent);
            }
        });
//        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.header_view, null);
//        park_list.addHeaderView(headerView,null,false);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPark();
    }

    private void loadPark(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences spf=getActivity().getSharedPreferences("user",MODE_PRIVATE);
                int user_id=spf.getInt("user_id",-1);
                ParkDao parkDao = new ParkDao();
                parkList=parkDao.getOtherParkCanBeRented(user_id);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showParkList();
                    }
                });
            }
        }).start();
    }

    private void showParkList() {
        if(parkAdapter==null){        //首次加载的操作
            parkAdapter= new ParkAdapter(context,parkList, (ParkAdapter.RentClickListener) rentClickListener);
            park_list.setAdapter(parkAdapter);
        }else{      //更新数据的操作
            parkAdapter.setParkList(parkList);
            parkAdapter.notifyDataSetChanged();
        }




        //租用按钮
        parkAdapter.setRentClickListener(new RentClickListener() {
            @Override
            public void rentClick(View view, int position) throws ParseException {



                SharedPreferences spf = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                int status=spf.getInt("status",-1);
                Log.e(TAG, "rentClick: "+status );

                if(status==2) {
                    ToastUtil.showCenter(context,"已有进行中的订单，请去进行中的订单里查看");
                }else {
                    final int user_id = spf.getInt("user_id", -1);
                    String nickname=spf.getString("nickname","");

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    String startTime =formatter.format(curDate);
//                    String startTime=formatter.parse(startTime1);
                    Log.e(TAG, "rentClick:startTime"+startTime );
                    //支付订单
                    final Park item = parkList.get(position);
                    final Order order =new Order();
                    order.setParkId(item.getPark_id());
                    order.setUserId(user_id);
                    order.setStartTime(startTime);
                    order.setParkPrice(item.getPark_price());
                    order.setParkDesc(item.getPark_descp());
                    order.setOwnerNickname(nickname);
                    order.setPark_loca(item.getPark_loca());
                    order.setPark_pic(item.getPark_pic());


                    //弹出提示框
                    new AlertDialog.Builder(context)
                            .setTitle("确认租用此车位吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //执行支付订单
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ParkDao parkDao = new ParkDao();
                                            parkDao.setParkStatusRented(item.getPark_id());
                                            OrderDao orderDao = new OrderDao();
                                            orderDao.insertOrder(order);
                                            UserDao userDao = new UserDao();
                                            userDao.updateStatus(user_id,2);
                                            //获取SharePreference(参数一：文件名  参数二：模式)
                                            SharedPreferences userInfo =getActivity().getSharedPreferences("user",MODE_PRIVATE);
                                            //获取Editor对象
                                            SharedPreferences.Editor edt =userInfo.edit();
                                            //存储信息

                                            edt.putInt("status",2);
                                            //提交
                                            edt.commit();
                                            mainHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    loadPark(); //重新加载数据
                                                }
                                            });
                                        }
                                    }).start();


                                    ToastUtil.showCenter(context,"租用成功");
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create().show();
                }

            }
            }

            );


    }



}