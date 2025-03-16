package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.NbButton;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "mysql-app-register";
    EditText username = null;
    EditText nickname = null;
    EditText password = null;
    EditText email = null;
    EditText telephone = null;
    private NbButton btn_commit;
    private int flag;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        nickname = findViewById(R.id.nickname);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        btn_commit=findViewById(R.id.btn_commit);
        telephone = findViewById(R.id.telephone);
        flag=0;


        handler=new Handler();

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_commit.startAnim();
                flag=1;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //跳转
                        register();
                        Log.e(TAG, "run: login" );
                    }
                },1500);

            }
        });

    }

    public void reg(View view){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

    public void back(View view){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_commit.onResume(this);
    }


    public void register(){

        String username1 = username.getText().toString();
        String nickname1 = nickname.getText().toString();
        String password1 = password.getText().toString();
        String email1 = email.getText().toString();
        String telephone1 = telephone.getText().toString();


        final User user = new User();

        user.setUsername(username1);
        user.setNickname(nickname1);
        user.setPassword(password1);
        user.setEmail(email1);
        user.setTelephone(telephone1);



        new Thread(){
            @Override
            public void run() {

                int msg = 0;
                UserDao userDao = new UserDao();
                User uu = userDao.findUser(user.getUsername());
                if(uu != null){
                    msg = 1;
                }
                else{
                    boolean flag = userDao.register(user);
                    if(flag){
                        msg = 2;
                    }
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
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_LONG).show();
                onResume();
            } else if(msg.what == 1) {
                Toast.makeText(getApplicationContext(),"该账号已经存在，请换一个账号",Toast.LENGTH_LONG).show();
                onResume();
            } else if(msg.what == 2) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                finish();
            }
        }
    };
}
