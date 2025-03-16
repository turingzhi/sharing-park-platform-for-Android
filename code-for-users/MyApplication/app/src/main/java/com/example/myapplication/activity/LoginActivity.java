package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.utils.NbButton;
import com.example.myapplication.utils.ToastUtil;

/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "mysql-app-MainActivity";

    public static final String USERNAME = "username";

    private String username;
    private String password;
    private String nickname;
    private int user_id;

    private String email;

    private String telephone;
    private int status;
    private String user_pic;

    private NbButton button;
    private RelativeLayout rlContent;
    private Handler handler;
    private Animator animator;
    private TextView tv_reg;
    private int flag;
    private TextView tv_title;
    private LinearLayout ll_account;
    private LinearLayout ll_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button=findViewById(R.id.button_test);
        rlContent=findViewById(R.id.rl_content);
        tv_reg=findViewById(R.id.tv_reg);
        tv_title=findViewById(R.id.tv_title);
        ll_account=findViewById(R.id.ll_account);
        ll_password=findViewById(R.id.ll_password);
        flag=0;
        tv_title.setVisibility(View.VISIBLE);
        ll_account.setVisibility(View.VISIBLE);
        ll_password.setVisibility(View.VISIBLE);
        tv_reg.setVisibility(View.VISIBLE);

        rlContent.getBackground().setAlpha(0);
        handler=new Handler();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.startAnim();
                flag=1;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //跳转
                        login();
                        Log.e(TAG, "run: login" );
                    }
                },1500);

            }
        });
    }

    private void gotoNew() {
        button.gotoNew();

        final Intent intent=new Intent(this,MainActivity.class);

//        tv_title.setVisibility(View.INVISIBLE);
//        ll_account.setVisibility(View.INVISIBLE);
//        ll_password.setVisibility(View.INVISIBLE);
//        tv_reg.setVisibility(View.INVISIBLE);


        int xc=(button.getLeft()+button.getRight())/2;
        int yc=(button.getTop()+button.getBottom())/2;
        animator= ViewAnimationUtils.createCircularReveal(rlContent,xc,yc,0,0);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        intent.putExtra(USERNAME,username);
                        startActivity(intent);
//                        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);

                    }
                },200);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                tv_title.setVisibility(View.VISIBLE);
//                ll_account.setVisibility(View.VISIBLE);
//                ll_password.setVisibility(View.VISIBLE);
//                tv_reg.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

//        rlContent.getBackground().setAlpha(255);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(flag==1){
            animator.cancel();
            rlContent.getBackground().setAlpha(0);
            button.regainBackground();
            flag=0;
        }
//        animator.cancel();
//        rlContent.getBackground().setAlpha(0);
//        button.regainBackground();
    }

    public void reg(View view){
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        button.onResume(this);
    }

    //    /**
//     * function: 登录
//     * */
    public void login(){

        final EditText EditTextAccount = findViewById(R.id.uesrAccount);
        final EditText EditTextPassword = findViewById(R.id.userPassword);
        username=EditTextAccount.getText().toString();
        Log.e(TAG, "login: "+username );
        password=EditTextPassword.getText().toString();

        new Thread(){
            @Override
            public void run() {
                UserDao userDao = new UserDao();
                int msg = userDao.login(username,password);
                Log.e(TAG, "run: msg"+msg);
                if(msg==1){
                    user_id=userDao.findUser(username).getUser_id();
                    Log.e(TAG, "run: user_id"+user_id );
                    nickname=userDao.findUser(username).getNickname();
                    email = userDao.findUser(username).getEmail();
                    telephone = userDao.findUser(username).getTelephone();
                    status=userDao.findUser(username).getStatus();
                    user_pic=userDao.findUser(username).getUser_pic();
                }
                hand1.sendEmptyMessage(msg);
            }
        }.start();

    }


    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_LONG).show();
                onResume();
            } else if (msg.what == 1) {
                //获取SharePreference(参数一：文件名  参数二：模式)
                SharedPreferences userInfo =getSharedPreferences("user",MODE_PRIVATE);
                //获取Editor对象
                SharedPreferences.Editor edt =userInfo.edit();
                //存储信息
                edt.putString("username",username);
                edt.putString("password",password);
                edt.putString("nickname",nickname);
                edt.putString("telephone",telephone);
                edt.putString("email",email);
                edt.putInt("user_id",user_id);
                edt.putInt("status",status);
                edt.putString("user_pic",user_pic);
                //提交
                edt.commit();



                ToastUtil.showLong(getApplicationContext(),"登录成功");
                gotoNew();

            } else if (msg.what == 2){
                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
                onResume();
            } else if (msg.what == 3){
                Toast.makeText(getApplicationContext(), "账号不存在", Toast.LENGTH_LONG).show();
                onResume();
            }
        }
    };
}
