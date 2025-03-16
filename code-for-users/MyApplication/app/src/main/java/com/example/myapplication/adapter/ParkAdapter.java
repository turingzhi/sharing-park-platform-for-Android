package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.myapplication.R;
import com.example.myapplication.RentClickListener;
import com.example.myapplication.dao.ParkDao;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Park;
import com.example.myapplication.fragment.ParkFragment;
import com.example.myapplication.utils.Base64Util;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
* 加载车位适配器
* */
public class ParkAdapter extends BaseAdapter {
    final String TAG = "ParkAdapter";

    private Context context;

    private List<Park> parkList;
    com.example.myapplication.RentClickListener rentClickListener;



    private Map<Integer,String> getPark=new HashMap<>();

    public ParkAdapter(Context context, List<Park> parkList , RentClickListener rentClickListener) {
        this.context = context;
        this.parkList = parkList;
        this.rentClickListener= (com.example.myapplication.RentClickListener) rentClickListener;
    }

    public ParkAdapter(Context context, List<Park> parkList) {
        this.context = context;
        this.parkList = parkList;
    }

    public void setParkList(List<Park> parkList) {
        this.parkList = parkList;
    }

    public void setRentClickListener(com.example.myapplication.RentClickListener rentClickListener) {
        this.rentClickListener = rentClickListener;
    }

//    public void setAddClickListener(AddClickListener addClickListener) {
//        this.addClickListener = addClickListener;
//    }

    @Override
    public int getCount() {
        return parkList.size();
    }

    @Override
    public Object getItem(int positon) {
        return parkList.get(positon);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView==null){   //tv_park_name,tv_park_loca,tv_park_price,tv_park_owner,tv_park_desc
            convertView= LayoutInflater.from(context).inflate(R.layout.item_park,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_park_name=convertView.findViewById(R.id.tv_park_name);
            viewHolder.tv_park_loca=convertView.findViewById(R.id.tv_park_loca);
            viewHolder.tv_park_price=convertView.findViewById(R.id.tv_park_price);
            viewHolder.tv_park_owner=convertView.findViewById(R.id.tv_park_owner);
            viewHolder.tv_park_desc=convertView.findViewById(R.id.tv_park_desc);
            viewHolder.btn_rent=convertView.findViewById(R.id.btn_rent);
            viewHolder.tv_park_id=convertView.findViewById(R.id.tv_park_id);
            viewHolder.iv_park_pic=convertView.findViewById(R.id.iv_park_pic);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }



        //数据填充
        final Park item = parkList.get(position);
        Handler handler=new Handler();
        Log.e(TAG, "getView: position"+position );
        viewHolder.tv_park_id.setText(String.valueOf(item.getPark_id()));

        viewHolder.tv_park_name.setText(item.getPark_name());
        Log.e(TAG, "getView:item.getPark_name "+item.getPark_name() );
        viewHolder.tv_park_loca.setText(item.getPark_loca());
        viewHolder.tv_park_price.setText(item.getPark_price()+"元/小时");
        String park_pic=item.getPark_pic();
        if (park_pic != "null") {
            Bitmap bitmap_park_pic = Base64Util.stringToBitmap(park_pic);
            viewHolder.iv_park_pic.setImageBitmap(bitmap_park_pic);
        }

//        UserDao userDao = new UserDao();
//        String ownerName = userDao.getUsername(item.getOwner_id());
//        Log.e(TAG, "getView: ownerName"+ownerName);




//        UserDao userDao= new UserDao();
//        String username = userDao.getUsername(item.getOwner_id());
//        viewHolder.tv_park_owner.setText(username);
//        Log.e(TAG, "getView:item.getOwner_id "+getPark.get(item.getOwner_id()));
        viewHolder.tv_park_owner.setText(item.getOwner_nickname());
        viewHolder.tv_park_desc.setText(item.getPark_descp());

        viewHolder.btn_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rentClickListener.rentClick(v,position);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    public interface RentClickListener {
        void rentClick(View view, int position);
    }



    private class ViewHolder{
        private TextView tv_park_name,tv_park_loca,tv_park_price,tv_park_owner,tv_park_desc,tv_park_id;
        private ImageView iv_park_pic;
        private Button btn_rent;
    }
}
