package com.example.myapplication.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dao.InfoDao;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Info;
import com.example.myapplication.utils.Base64Util;

public class InfoDetailActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_info_title;
    private TextView tv_info_desc;
    private TextView tv_info_date;
    private ImageView iv_info_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);

        iv_back=findViewById(R.id.iv_back);
        tv_info_title=findViewById(R.id.tv_info_title);
        tv_info_desc=findViewById(R.id.tv_info_desc);
        tv_info_date=findViewById(R.id.tv_info_date);
        iv_info_pic=findViewById(R.id.iv_info_pic);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent=getIntent();
        final int info_id=intent.getIntExtra("info_id",-1);

        new Thread(){
            @Override
            public void run() {
                InfoDao infoDao = new InfoDao();
                Info info= infoDao.getInfoById(info_id);
                Message msg = Message.obtain();
                Bundle bundle=new Bundle();
                bundle.putString("info_title",info.getInfo_title());
                bundle.putString("info_desc",info.getInfo_desc());
                bundle.putString("info_date",info.getInfo_date());
                bundle.putString("info_pic",info.getInfo_pic());

                msg.setData(bundle);
                msg.what=0;
                hand1.sendMessage(msg);

            }
        }.start();

    }
    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Bundle bundle1=msg.getData();
                String info_title1=bundle1.getString("info_title");
                String info_desc1=bundle1.getString("info_desc");
                String info_date1=bundle1.getString("info_date");
                String info_pic1=bundle1.getString("info_pic");
                tv_info_title.setText(info_title1);
                tv_info_date.setText(info_date1);
                tv_info_desc.setText(info_desc1);

                Bitmap bitmap = Base64Util.stringToBitmap(info_pic1);

                iv_info_pic.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getApplicationContext(),"展示失败",Toast.LENGTH_LONG).show();
            }
        }
    };


}