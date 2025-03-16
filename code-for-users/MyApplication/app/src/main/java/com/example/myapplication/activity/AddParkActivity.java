package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Park;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.Base64Util;

import java.io.ByteArrayOutputStream;

/*
* 提交自己空余车位
* */
public class AddParkActivity extends AppCompatActivity {

    private static final String TAG = "AddPark";

    private EditText et_parkName;
    private EditText et_parkLoca;
    private EditText et_parkPrice;
    private EditText et_parkDesc;
    private Button btn_commit;
    private ImageView iv_back;
    private ImageView iv_park_pic;
    private Uri uri;
    private String imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_park);
        et_parkName=findViewById(R.id.et_parkName);
        et_parkLoca=findViewById(R.id.et_parkLoca);
        et_parkPrice=findViewById(R.id.et_parkPrice);
        et_parkDesc=findViewById(R.id.et_parkDesc);
        btn_commit=findViewById(R.id.btn_commit);
        iv_back=findViewById(R.id.iv_back);
        iv_park_pic=findViewById(R.id.iv_park_pic);
        imageString="";

        SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
        final int user_id = spf.getInt("user_id", -1);
        String username = spf.getString("username", "null");
        final String nickname = spf.getString("nickname", "null");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent =new Intent(MyParkActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        //主线程使用网络请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //选择本地图片
        iv_park_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent, 0);
            }
        });



        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int owner_id = user_id;//    int park_id;int owner_id;String park_name;String park_loca;Double park_price;int park_status;String park_descp;
                String park_name = et_parkName.getText().toString();
                String park_loca = et_parkLoca.getText().toString();
                Double park_price = Double.valueOf(et_parkPrice.getText().toString());
                int park_status = 0;//0 待审核
                String park_descp = et_parkDesc.getText().toString();
                String owner_nickname=nickname;


                final Park park = new Park();

//        user.setUser_id(20);
                park.setOwner_id(owner_id);
                park.setPark_name(park_name);
                park.setPark_loca(park_loca);
                park.setPark_price(park_price);
                park.setPark_status(park_status);
                park.setPark_descp(park_descp);
                park.setOwner_nickname(owner_nickname);
                park.setPark_pic(imageString);


                new Thread(){
                    @Override
                    public void run() {

                        int msg=0;

                        ParkDao parkDao = new ParkDao();
                        boolean flag = parkDao.addPark(park);
                        if(flag){
                            msg=1;
                        }

                        hand.sendEmptyMessage(msg);


                    }
                }.start();


            }

            @SuppressLint("HandlerLeak")
            final Handler hand = new Handler()
            {
                public void handleMessage(Message msg) {
                    if(msg.what == 0) {
                        Toast.makeText(getApplicationContext(),"提交失败",Toast.LENGTH_LONG).show();
                    } else if(msg.what == 1) {
                        Toast.makeText(getApplicationContext(),"提交成功",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        //将想要传递的数据用putExtra封装在intent中
                        intent.putExtra("a","注册");
                        setResult(RESULT_CANCELED,intent);
                        finish();
                    }
                }
            };
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO Auto-generated method stub
        if (requestCode == 0 && resultCode == -1) {
            uri = data.getData();
            iv_park_pic.setImageURI(uri);
            Log.i("tt", uri.getPath());
            Log.i("tt", uri.getEncodedPath());
            imageString = "";

            //将图片转换成Base64编码
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                String park_pic = Base64Util.bitmapToString(bitmap);
                Log.e(TAG, "onActivityResult: park_pic  " + park_pic);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                imageString = "data:image/jpeg;base64," + imageString;

            } catch (Exception e) {
            }

        }
    }
}