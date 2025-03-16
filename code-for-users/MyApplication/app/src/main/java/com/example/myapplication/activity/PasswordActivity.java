package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.ETC1;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.ToastUtil;


/*
 * 修改个人信息密码
 * */
public class PasswordActivity extends AppCompatActivity {

    private static final String TAG = "PasswordActivity";

    private EditText et_oldPassword;

    private EditText et_newPassword;

    private EditText et_newPassword2;

    private ImageView iv_back;

    private Button btn_save;

    private String oldPassword;
    private String newPassword;
    private String newPassword2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
        int user_id = spf.getInt("user_id", -1);
        String username = spf.getString("username", "null");
        String nickname = spf.getString("nickname", "null");
        String email = spf.getString("email", "null");
        String telephone = spf.getString("telephone", "null");

        et_oldPassword=findViewById(R.id.et_oldPassword);
        et_newPassword=findViewById(R.id.et_newPassword);
        et_newPassword2=findViewById(R.id.et_newPassword2);
        btn_save=findViewById(R.id.btn_save);
        iv_back=findViewById(R.id.iv_back);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void save(View view){

        new Thread(){
            @Override
            public void run() {
                oldPassword=et_oldPassword.getText().toString();
                newPassword=et_newPassword.getText().toString();
                newPassword2=et_newPassword2.getText().toString();
                SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
                int user_id = spf.getInt("user_id", -1);
                String username=spf.getString("username","null");
                String password = spf.getString("password", "null");
                UserDao userDao = new UserDao();
                User user1=userDao.findUser(username);
                Log.e(TAG, "run: oldPassword"+oldPassword);
                Log.e(TAG, "run: newPassword" +newPassword);
                Log.e(TAG, "run: newPassword" +newPassword2);
                int msg=0;
                if(!newPassword.equals(newPassword2)){
                    //msg=2:如果两次密码不一样，提示两次密码不一样
                    Log.e(TAG, "run: 1"+newPassword+" "+newPassword2 );
                    msg=2;
                    hand1.sendEmptyMessage(msg);
                }else if(!oldPassword.equals(password)){
                    //msg=3:输入的原密码不正确
                    msg=3;
                    hand1.sendEmptyMessage(msg);
                }else{
                    user1.setPassword(newPassword);
                    msg=userDao.update(user1);
                    hand1.sendEmptyMessage(msg);
                    //获取SharePreference(参数一：文件名  参数二：模式)
                    SharedPreferences userInfo =getSharedPreferences("user",MODE_PRIVATE);
                    //获取Editor对象
                    SharedPreferences.Editor edt =userInfo.edit();
                    //存储信息
                    edt.putString("password",newPassword);
                    //提交
                    edt.commit();
                }
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                Toast.makeText(getApplicationContext(), "密码更新失败", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                ToastUtil.showLong(getApplicationContext(),"密码更新成功");
                finish();
            }else if(msg.what==2){
                ToastUtil.showLong(getApplicationContext(),"两次密码不一样");
            }else if(msg.what==3){
                ToastUtil.showLong(getApplicationContext(),"输入原密码错误");
            }
        }
    };

}