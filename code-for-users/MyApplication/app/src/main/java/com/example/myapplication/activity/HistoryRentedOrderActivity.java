package com.example.myapplication.activity;

import static android.os.Looper.getMainLooper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.DeleteClickListener;
import com.example.myapplication.R;
import com.example.myapplication.RentClickListener;
import com.example.myapplication.adapter.HistoryRentedOrderAdapter;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.adapter.ParkAdapter;
import com.example.myapplication.dao.OrderDao;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.entity.Order;
import com.example.myapplication.entity.Park;
import com.example.myapplication.utils.ToastUtil;

import java.util.List;

/*
* 查看自己车位被租用的历史订单
* */
public class HistoryRentedOrderActivity extends AppCompatActivity {

    private ImageView iv_back;

    private List<Order> orderList;

    private List<Park> parkList;

    private Handler mainHandler;

    private Context context;

    private SwipeRefreshLayout re_order;

    private ListView order_list;

    private HistoryRentedOrderAdapter historyRentedOrderAdapter;

    private DeleteClickListener deleteClickListener;

    private static final String TAG = "HistoryRentOrderActivity";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_rented_order);
        iv_back = findViewById(R.id.iv_back);
        mainHandler = new Handler(getMainLooper());
        order_list = findViewById(R.id.order_list);
        re_order = findViewById(R.id.re_order);
        Log.e(TAG, "onCreate: 1" );
        context=HistoryRentedOrderActivity.this;

        re_order.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //处理刷新逻辑
                loadOrder();
                //停止刷新
                re_order.setRefreshing(false);
            }
        });


        loadOrder();


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HistoryRentOrderActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrder();
    }


    private void loadOrder() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                ParkDao parkDao = new ParkDao();
                OrderDao orderDao=new OrderDao();
                SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
                int user_id = spf.getInt("user_id", -1);
                Log.e(TAG, "run: user_id"+user_id);
                parkList = parkDao.getAllParkById(user_id);
                orderList=orderDao.getAllOrderByParkListRented(parkList);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showOrderList();
                    }
                });
            }
        }).start();
    }

    private void showOrderList() {
        if (historyRentedOrderAdapter == null) {        //首次加载的操作
            historyRentedOrderAdapter = new HistoryRentedOrderAdapter(context, orderList, deleteClickListener);
            order_list.setAdapter(historyRentedOrderAdapter);
        } else {      //更新数据的操作
            historyRentedOrderAdapter.setOrderList(orderList);
            historyRentedOrderAdapter.notifyDataSetChanged();
        }


        //租用按钮
        historyRentedOrderAdapter.setDeleteClickListener(new DeleteClickListener() {
                                                @Override
                                                public void deleteClick(View view, int position) {
                                                    //支付订单
                                                    final Order item = orderList.get(position);
                                                    //弹出提示框
                                                    new AlertDialog.Builder(context)
                                                            .setTitle("删除此历史订单？")
                                                            .setMessage("确认删除此历史订单吗？")
                                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //执行支付订单
                                                                    new Thread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            OrderDao orderDao = new OrderDao();
                                                                            orderDao.deleteOrder(item.getOrderId());
                                                                            mainHandler.post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    loadOrder(); //重新加载数据
                                                                                }
                                                                            });
                                                                        }
                                                                    }).start();

//                                //跳转并更新status1
//                                FragmentManager manager = getFragmentManager();//获取到父fragment的管理器
//                                OrderFragment home = (OrderFragment) manager.getFragments().get(0);//获取到父Fragment
//                                home.getmFragment()[2].onResume();//更新status1
//                                home.getmTabHost().setCurrentTab(1);//跳转status1
//                                home.getViewPager().setCurrentItem(1);


                                                                    ToastUtil.showCenter(context, "支付成功");
                                                                }
                                                            })
                                                            .setNegativeButton("取消", null)
                                                            .create().show();
                                                }
                                            }

        );


    }
}