package com.example.myapplication.activity;

import static android.os.Looper.getMainLooper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.DeleteMyParkClickListener;
import com.example.myapplication.R;
import com.example.myapplication.RentClickListener;
import com.example.myapplication.UpdateMyParkClickListener;
import com.example.myapplication.adapter.MyParkAdapter;
import com.example.myapplication.adapter.ParkAdapter;
import com.example.myapplication.dao.OrderDao;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Order;
import com.example.myapplication.entity.Park;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/*
* 我的车位界面
* */
public class MyParkActivity extends AppCompatActivity {
    private ImageView iv_back;

    private List<Park> parkList;

    private SwipeRefreshLayout re_park;

    private ListView park_list;

    private LinearLayout ll_myPark;

    private Handler mainHandler;

    private MyParkAdapter myParkAdapter;

    private TextView tv_publish_park;



    final String TAG = "MyParkActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_park);
        iv_back=findViewById(R.id.iv_back);
        park_list=findViewById(R.id.park_list);
        ll_myPark=findViewById(R.id.ll_myPark);
        re_park=findViewById(R.id.re_park);
        tv_publish_park=findViewById(R.id.tv_publish_park);
        mainHandler=new Handler(getMainLooper());

        SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
        int user_id = spf.getInt("user_id", -1);

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

        tv_publish_park.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MyParkActivity.this, AddParkActivity.class);
                startActivity(intent);
                finish();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent =new Intent(MyParkActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        loadPark();
    }

    private void loadPark() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                ParkDao parkDao = new ParkDao();
                SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
                int user_id = spf.getInt("user_id", -1);
                parkList=parkDao.getAllParkById(user_id);
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
        if(myParkAdapter==null){        //首次加载的操作
            myParkAdapter= new MyParkAdapter(this,parkList);
            park_list.setAdapter(myParkAdapter);
        }else{      //更新数据的操作
            myParkAdapter.setParkList(parkList);
            myParkAdapter.notifyDataSetChanged();
        }


        //修改按钮
        myParkAdapter.setUpdateMyParkClickListener(new UpdateMyParkClickListener() {
           @Override
           public void updateMyParkClick(View v, int position) {
                       final Park item = parkList.get(position);;
                       Intent intent =new Intent(MyParkActivity.this, UpdateMyParkActivity.class);
                       intent.putExtra("park_id",item.getPark_id());
//                       intent.putExtra("park_name",item.getPark_name());
//                       intent.putExtra("park_loca",item.getPark_loca());
//                       intent.putExtra("park_desc",item.getPark_descp());
//                       intent.putExtra("park_price",item.getPark_price());
//                       intent.putExtra("park_status",item.getPark_status());
//                       intent.putExtra("park_pic",item.getPark_pic());
                       startActivity(intent);

                    }
        });

        //删除自己的车位
        myParkAdapter.setDeleteMyParkClickListener(new DeleteMyParkClickListener() {
            @Override
            public void deleteMyParkClick(View v, int position) {
                SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);

                int status=spf.getInt("status",-1);
                final Park item = parkList.get(position);;
                    //弹出提示框
                    new AlertDialog.Builder(MyParkActivity.this)
                            .setTitle("确认删除此车位吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //执行删除车位
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ParkDao parkDao = new ParkDao();
                                            Log.e(TAG, "run: park_id"+item.getPark_id());
                                            parkDao.deletePark(item.getPark_id());
                                            mainHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    loadPark(); //重新加载数据
                                                }
                                            });
                                        }
                                    }).start();
                                    ToastUtil.showCenter(MyParkActivity.this,"删除成功");
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create().show();
                }




        });




    }
}