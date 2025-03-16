package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.ETC1;
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
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.Base64Util;
import com.example.myapplication.utils.JDBCUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;


/*
 * 修改个人信息界面
 * */
public class MyInfoActivity extends AppCompatActivity {

    private static final String TAG = "MyInfo";

    private EditText et_nickname;

    private EditText et_email;

    private EditText et_telephone;

    private ImageView iv_back;

    private Button btn_save;

    private ImageView iv_user_pic;

    private Uri uri;

    private String imageString;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
        int user_id = spf.getInt("user_id", -1);
        String username = spf.getString("username", "null");
        String nickname = spf.getString("nickname", "null");
        String email = spf.getString("email", "null");
        String telephone = spf.getString("telephone", "null");
        String user_pic = spf.getString("user_pic", "null");


        et_nickname = findViewById(R.id.et_nickname);
        et_email = findViewById(R.id.et_email);
        et_telephone = findViewById(R.id.et_telephone);
        btn_save = findViewById(R.id.btn_save);
        iv_back = findViewById(R.id.iv_back);
        iv_user_pic = findViewById(R.id.iv_user_pic);

        et_nickname.setText(nickname);
        et_email.setText(email);
        et_telephone.setText(telephone);

        if (user_pic != "null") {
            Bitmap bitmap_user_pic = Base64Util.stringToBitmap(user_pic);
            iv_user_pic.setImageBitmap(bitmap_user_pic);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent =new Intent(MyInfoActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        //主线程使用网络请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //选择本地图片
        iv_user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent, 0);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO Auto-generated method stub
        if (requestCode == 0 && resultCode == -1) {
            uri = data.getData();
            iv_user_pic.setImageURI(uri);
            Log.i("tt", uri.getPath());
            Log.i("tt", uri.getEncodedPath());
            imageString = "";

            //将图片转换成Base64编码
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                String user_pic = Base64Util.bitmapToString(bitmap);
                Log.e(TAG, "onActivityResult: user_pic  " + user_pic);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                imageString = "data:image/jpeg;base64," + imageString;

            } catch (Exception e) {
            }

        }
    }


    public void save(View view) {


        final String nickname1 = et_nickname.getText().toString();
        final String email1 = et_email.getText().toString();
        final String telephone1 = et_telephone.getText().toString();
        final String user_pic = et_telephone.getText().toString();


        new Thread() {
            @Override
            public void run() {
                SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
                int user_id = spf.getInt("user_id", -1);
                String username = spf.getString("username", "null");
                UserDao userDao = new UserDao();
                User user1 = userDao.findUser(username);
                user1.setNickname(nickname1);
                user1.setEmail(email1);
                user1.setTelephone(telephone1);
                user1.setUser_pic(imageString);
                Log.e(TAG, "run: imageString  "+imageString );

                //获取SharePreference(参数一：文件名  参数二：模式)
                SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
                //获取Editor对象
                SharedPreferences.Editor edt = userInfo.edit();
                //存储信息
                edt.putString("nickname", nickname1);
                edt.putString("telephone", telephone1);
                edt.putString("email", email1);
                edt.putInt("user_id", user_id);
                edt.putString("user_pic",imageString);
                //提交
                edt.commit();

                int msg = userDao.update(user1);
                hand1.sendEmptyMessage(msg);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                ToastUtil.showLong(getApplicationContext(), "更新成功");

                finish();
            }
        }
    };

}