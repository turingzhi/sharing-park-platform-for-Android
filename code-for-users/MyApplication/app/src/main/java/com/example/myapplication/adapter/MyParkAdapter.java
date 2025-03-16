package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.myapplication.DeleteMyParkClickListener;
import com.example.myapplication.R;
import com.example.myapplication.RentClickListener;
import com.example.myapplication.UpdateMyParkClickListener;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Park;
import com.example.myapplication.fragment.ParkFragment;
import com.example.myapplication.utils.Base64Util;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * 加载我的车位适配器
 * */
public class MyParkAdapter extends BaseAdapter {
    final String TAG = "ParkAdapter";

    private Context context;

    private List<Park> parkList;

    UpdateMyParkClickListener updateMyParkClickListener;

    DeleteMyParkClickListener deleteMyParkClickListener;

    public void setDeleteMyParkClickListener(DeleteMyParkClickListener deleteMyParkClickListener) {
        this.deleteMyParkClickListener = deleteMyParkClickListener;
    }

    private Map<Integer,String> getPark=new HashMap<>();

    public MyParkAdapter(Context context, List<Park> parkList, UpdateMyParkClickListener updateMyParkClickListener, DeleteMyParkClickListener deleteMyParkClickListener) {
        this.context = context;
        this.parkList = parkList;
        this.updateMyParkClickListener = updateMyParkClickListener;
        this.deleteMyParkClickListener = deleteMyParkClickListener;
    }

    public MyParkAdapter(Context context, List<Park> parkList , UpdateMyParkClickListener updateMyParkClickListener) {
        this.context = context;
        this.parkList = parkList;
        this.updateMyParkClickListener= (com.example.myapplication.UpdateMyParkClickListener) updateMyParkClickListener;
    }

    public MyParkAdapter(Context context, List<Park> parkList) {
        this.context = context;
        this.parkList = parkList;
    }

    public void setParkList(List<Park> parkList) {
        this.parkList = parkList;
    }

    public void setUpdateMyParkClickListener(UpdateMyParkClickListener updateMyParkClickListener) {
        this.updateMyParkClickListener = updateMyParkClickListener;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_my_park,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_park_name=convertView.findViewById(R.id.tv_park_name);
            viewHolder.tv_park_loca=convertView.findViewById(R.id.tv_park_loca);
            viewHolder.tv_park_price=convertView.findViewById(R.id.tv_park_price);
            viewHolder.tv_park_desc=convertView.findViewById(R.id.tv_park_desc);
            viewHolder.btn_update=convertView.findViewById(R.id.btn_update);
            viewHolder.tv_park_id=convertView.findViewById(R.id.tv_park_id);
            viewHolder.tv_park_status=convertView.findViewById(R.id.tv_park_status);
            viewHolder.btn_delete=convertView.findViewById(R.id.btn_delete);
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
        int status=item.getPark_status();
        Bitmap bitmap_park_pic = Base64Util.stringToBitmap(item.getPark_pic());
//                    finalViewHolder.iv_park_pic.setImageBitmap(bitmap_park_pic);
        viewHolder.iv_park_pic.setImageBitmap(bitmap_park_pic);
        if(status==0){
            viewHolder.tv_park_status.setText("待审核");
        }else if(status==1){
            viewHolder.tv_park_status.setText("可以正常使用");
        }else if(status==2){
            viewHolder.tv_park_status.setText("已被租用");
        }else if(status==3){
            viewHolder.tv_park_status.setText("异常状态");
        }


//        UserDao userDao = new UserDao();
//        String ownerName = userDao.getUsername(item.getOwner_id());
//        Log.e(TAG, "getView: ownerName"+ownerName);

//        viewHolder.tv_park_owner.setText(String.valueOf(item.getOwner_id()));
//        Log.e(TAG, "getView:item.getOwner_id "+getPark.get(item.getOwner_id()));
        viewHolder.tv_park_desc.setText(item.getPark_descp());

        viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMyParkClickListener.updateMyParkClick(v,position);
            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMyParkClickListener.deleteMyParkClick(v,position);
            }
        });
        return convertView;
    }

    public interface updateMyParkClickListener {
        void updateMyParkClick(View view, int position);
    }


    //删除按钮点击事件
//        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteClickListener.deleteClick(v,position);
//            }
//        });








    private class ViewHolder{

        private TextView tv_park_name,tv_park_loca,tv_park_price,tv_park_desc,tv_park_id,tv_park_status;
        private ImageView iv_park_pic;
        private Button btn_update,btn_delete;
    }



}
