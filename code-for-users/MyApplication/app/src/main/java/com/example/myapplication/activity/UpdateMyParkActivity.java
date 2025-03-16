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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dao.InfoDao;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Info;
import com.example.myapplication.entity.Park;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.Base64Util;
import com.example.myapplication.utils.ToastUtil;

import java.io.ByteArrayOutputStream;

public class UpdateMyParkActivity extends AppCompatActivity {

    private EditText et_park_name;

    private EditText et_park_loca;

    private EditText et_park_price;

    private EditText et_park_desc;

    private TextView tv_park_status;

    private Button btn_commit;

    private ImageView iv_back;

    private ImageView iv_park_pic;


    Handler mainHandler=new Handler();

    private Uri uri;
    private String imageString;

    final String TAG="updateMyPark";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_park);
        et_park_name=findViewById(R.id.et_park_name);
        et_park_loca=findViewById(R.id.et_park_loca);
        et_park_price=findViewById(R.id.et_park_price);
        et_park_desc=findViewById(R.id.et_park_desc);
        tv_park_status=findViewById(R.id.tv_park_status);
        btn_commit=findViewById(R.id.btn_commit);
        iv_back=findViewById(R.id.iv_back);
        iv_park_pic=findViewById(R.id.iv_park_pic);


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

        Intent intent=getIntent();

        final int park_id = intent.getIntExtra("park_id",-1);
//        final String park_name = intent.getStringExtra("park_name");
//        final String park_loca = intent.getStringExtra("park_loca");
//        final String park_desc= intent.getStringExtra("park_desc");
//        final double park_price = intent.getDoubleExtra("park_price",-1);
//        final int park_status = intent.getIntExtra("park_status",-1);
//        final String park_pic=intent.getStringExtra("park_pic");
//        Log.e(TAG, "onCreate: park_status"+park_status);

        new Thread(){
            @Override
            public void run() {
                ParkDao parkDao = new ParkDao();
                Park park= parkDao.findParkById(park_id);
                Message msg = Message.obtain();
                msg.what=1;
                Bundle bundle=new Bundle();
                bundle.putString("park_name",park.getPark_name());
                bundle.putString("park_loca",park.getPark_loca());
                bundle.putString("park_desc",park.getPark_descp());
                bundle.putDouble("par k_price",park.getPark_price());
                bundle.putInt("park_status",park.getPark_status());
                bundle.putString("park_pic",park.getPark_pic());

                msg.setData(bundle);
                msg.what=0;
                handler.sendMessage(msg);

            }
        }.start();


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Bundle bundle1=msg.getData();
                String park_name1=bundle1.getString("park_name");
                String park_loca1=bundle1.getString("park_loca");
                String park_desc1=bundle1.getString("park_desc");
                Double park_price1=bundle1.getDouble("park_price");
                int park_status1=bundle1.getInt("park_status");
                String park_pic1=bundle1.getString("park_pic");
                et_park_name.setText(park_name1);
                et_park_loca.setText(park_loca1);
                et_park_desc.setText(park_desc1);
                et_park_price.setText(String.valueOf(park_price1));

                Bitmap bitmap_park_pic = Base64Util.stringToBitmap(park_pic1);
//                    finalViewHolder.iv_park_pic.setImageBitmap(bitmap_park_pic);
                iv_park_pic.setImageBitmap(bitmap_park_pic);
                imageString=park_pic1;


                if(park_status1==0){
                    tv_park_status.setText("待审核");
                }else if(park_status1==1){
                    tv_park_status.setText("可被租用");
                }else if(park_status1==2){
                    tv_park_status.setText("租用中");
                }
            } else {
                Toast.makeText(getApplicationContext(),"展示失败",Toast.LENGTH_LONG).show();
            }
        }
    };

    public void commit(View view){


        final String park_name1=et_park_name.getText().toString();
        final String park_loca1=et_park_loca.getText().toString();
        final String park_desc1=et_park_desc.getText().toString();
        final double park_price1= Double.parseDouble(et_park_price.getText().toString());


        new Thread(){
            @Override
            public void run() {
                Intent intent=getIntent();
                int park_id = intent.getIntExtra("park_id",-1);
                ParkDao parkDao = new ParkDao();
                Park park = parkDao.findParkById(park_id);
                park.setPark_name(park_name1);
                park.setPark_loca(park_loca1);
                park.setPark_descp(park_desc1);
                park.setPark_price(park_price1);
                park.setPark_pic(imageString);

                int msg=parkDao.update(park);
                hand1.sendEmptyMessage(msg);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                ToastUtil.showLong(getApplicationContext(),"提交成功");

//                Intent intent =new Intent(MyInfoActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO Auto-generated method stub
        if (requestCode == 0 && resultCode == -1) {
            uri = data.getData();
            iv_park_pic.setImageURI(uri);
            Log.i("tt", uri.getPath());
            Log.i("tt", uri.getEncodedPath());
//            imageString = "";

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