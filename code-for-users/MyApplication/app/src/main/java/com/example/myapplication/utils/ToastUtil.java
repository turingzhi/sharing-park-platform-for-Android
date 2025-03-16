package com.example.myapplication.utils;


import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    /**
     * 显示长消息
     * @param context
     * @param msg
     */
    public static void showLong(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showCenter(Context context,String msg){
        Toast toast=Toast.makeText(context,msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
