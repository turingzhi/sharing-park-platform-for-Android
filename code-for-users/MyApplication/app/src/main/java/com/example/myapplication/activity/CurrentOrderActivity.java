package com.example.myapplication.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dao.OrderDao;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Order;
import com.example.myapplication.entity.Park;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.Base64Util;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentOrderActivity extends AppCompatActivity {

    private static final String TAG = "mysql-app-currentOrder";

    private TextView tv_order_id;
    private TextView tv_park_loca;
    private TextView tv_park_desc;
    private TextView tv_owner_nickname;
    private TextView tv_park_price;
    private TextView tv_start_time;
    private TextView tv_alreadyRentTime;
    private Button btn_endUse;
    private Order currentOrder;
    private ImageView iv_back;
    private Context context;
    private ImageView iv_park_pic;

    Handler mainHandler=new Handler();

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        tv_order_id=findViewById(R.id.tv_order_id1);
        tv_park_loca=findViewById(R.id.tv_park_loca);
        tv_park_desc=findViewById(R.id.tv_park_desc);
        tv_owner_nickname=findViewById(R.id.tv_owner_nickname);
        tv_start_time=findViewById(R.id.tv_start_time);
        tv_park_price=findViewById(R.id.tv_park_price);
        tv_alreadyRentTime=findViewById(R.id.tv_alreadyRentTime);
        iv_back=findViewById(R.id.iv_back);
        btn_endUse=findViewById(R.id.btn_endUse);
        iv_park_pic=findViewById(R.id.iv_park_pic);
        mainHandler = new Handler(getMainLooper());
        SharedPreferences spf=getSharedPreferences("user",MODE_PRIVATE);
        final int user_id=spf.getInt("user_id",-1);
        loadCurrentOrder();
        final double[] totalPrice = new double[1];

        new Thread(mRunnable).start();

        mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){

                    Bundle bundle = msg.getData();
                    tv_order_id.setText(String.valueOf(bundle.getInt("order_id")));
                    tv_park_loca.setText(bundle.getString("park_loca"));
                    tv_park_desc.setText(bundle.getString("park_desc"));
                    tv_owner_nickname.setText(bundle.getString("owner_nickname"));
                    String time = bundle.getString("start_time");
                    Log.e(TAG, "handleMessage: "+ time);
                    tv_start_time.setText(bundle.getString("start_time"));
                    tv_park_price.setText(String.valueOf(bundle.getDouble("park_price"))+"元/小时");
                    tv_alreadyRentTime.setText(String.valueOf(bundle.getInt("alreadyRentTime"))+"小时");
                    int order_id=bundle.getInt("order_id");
                    Log.e(TAG, "handleMessage:"+ order_id);
                    int a=bundle.getInt("alreadyRentTime");
                    Bitmap bitmap_park_pic= Base64Util.stringToBitmap(bundle.getString("park_pic"));
                    iv_park_pic.setImageBitmap(bitmap_park_pic);

                    totalPrice[0] =(bundle.getInt("alreadyRentTime"))*(bundle.getDouble("park_price"));




                }
            }
        };


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_endUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CurrentOrderActivity.this)
                        .setTitle("确认结束使用此车位并支付"+totalPrice[0]+"元吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //执行支付订单
                                new Thread(){
                                    @Override
                                    public void run() {
                                        OrderDao orderDao=new OrderDao();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date endDate = new Date(System.currentTimeMillis());
                                        String endTime = formatter.format(endDate);
                                        String startTime1=currentOrder.getStartTime();
                                        Date startTime = null;
                                        try {
                                            startTime = formatter.parse(startTime1);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        long diff = endDate.getTime() - startTime.getTime();
                                        long hours = diff / (1000 * 60 * 60);
                                        int total_time=(int)(hours+1);
                                        Order order = new Order();
                                        order=orderDao.findCurrentOrder(user_id);
                                        int order_id=order.getOrderId();
                                        int park_id=order.getParkId();
                                        double park_price = order.getParkPrice();
                                        double total_price=total_time * park_price;
                                        UserDao userDao = new UserDao();
                                        userDao.updateStatus(user_id,1);
                                        ParkDao parkDao = new ParkDao();
                                        parkDao.setParkStatusUnRented(park_id);
                                        //获取SharePreference(参数一：文件名  参数二：模式)
                                        SharedPreferences userInfo =getSharedPreferences("user",MODE_PRIVATE);
                                        //获取Editor对象
                                        SharedPreferences.Editor edt =userInfo.edit();
                                        //存储信息

                                        edt.putInt("status",1);
                                        //提交
                                        edt.commit();

                                        orderDao.endOrder(order_id,endTime,total_time,total_price);

                                    }
                                }.start();


                                Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_LONG).show();
//                                Intent intent =new Intent(CurrentOrderActivity.this, MainActivity.class);
//                                //传递用户名
//                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();

            }
        });




//        loadCurrentOrder();

    }


    //调用定时刷新函数
    private Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            refresh();//编写的定时刷新函数
        }
    };


    private void refresh() {
        loadCurrentOrder();
    }
    //实现定时刷新
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while(true){
                try {
                    //sleep2秒，可根据需求更换为响应的时间
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(mHandler.obtainMessage());
            }
        }
    };


    private void loadCurrentOrder(){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 0;
                SharedPreferences spf = getSharedPreferences("user",MODE_PRIVATE);
                int user_id=spf.getInt("user_id",-1);
                OrderDao orderDao = new OrderDao();
                currentOrder=orderDao.findCurrentOrder(user_id);
                int park_id=currentOrder.getParkId();
                ParkDao parkDao=new ParkDao();
                String park_pic=parkDao.findParkById(park_id).getPark_pic();
                int order_id=currentOrder.getOrderId();
                Log.e(TAG, "handleMessage:"+ order_id);
                Bundle bundle = new Bundle();
                bundle.putInt("order_id",currentOrder.getOrderId());
                bundle.putString("park_loca",currentOrder.getPark_loca());
                bundle.putString("park_desc",currentOrder.getParkDesc());
                bundle.putString("owner_nickname",currentOrder.getOwnerNickname());
                bundle.putString("start_time",currentOrder.getStartTime());
                bundle.putDouble("park_price",currentOrder.getParkPrice());
                bundle.putString("park_pic",park_pic);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date endDate = new Date(System.currentTimeMillis());
                String startTime1 = currentOrder.getStartTime();
                long hours = 0;
                try {
                    Date startTime = formatter.parse(startTime1);
                    long diff = endDate.getTime() - startTime.getTime();
                    hours = diff / (1000 * 60 * 60);
                    bundle.putInt("alreadyRentTime",(int) (hours+1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                message.setData(bundle);
                mainHandler.sendMessage(message);

            }
        }).start();
    }

}