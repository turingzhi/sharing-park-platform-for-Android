package com.example.myapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.HomeInfoDetailClickListener;
import com.example.myapplication.R;
import com.example.myapplication.activity.InfoDetailActivity;
import com.example.myapplication.dao.InfoDao;
import com.example.myapplication.entity.Info;
import com.example.myapplication.entity.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/*
* 加载HomeFragment的适配器
* */
public class HomeInfoAdapter extends BaseAdapter {

    final String TAG = "HomeInfoAdapter";

    private Context context;

    private List<Info> infoList;

    private HomeInfoDetailClickListener homeInfoDetailClickListener;

    public HomeInfoAdapter(Context context, List<Info> infoList, HomeInfoDetailClickListener homeInfoDetailClickListener) {
        this.context = context;
        this.infoList = infoList;
        this.homeInfoDetailClickListener = homeInfoDetailClickListener;
    }

    public void setHomeInfoDetailClickListener(HomeInfoDetailClickListener homeInfoDetailClickListener) {
        this.homeInfoDetailClickListener = homeInfoDetailClickListener;
    }

    public HomeInfoAdapter(Context context, List<Info> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    public void setInfoList(List<Info> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView==null){   //tv_park_name,tv_park_loca,tv_park_price,tv_park_owner,tv_park_desc
            convertView= LayoutInflater.from(context).inflate(R.layout.item_home_info,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_info_title=convertView.findViewById(R.id.tv_info_title);
            viewHolder.tv_info_date=convertView.findViewById(R.id.tv_info_date);
            viewHolder.ll_info=convertView.findViewById(R.id.ll_info);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        final Info item = infoList.get(position);
        Handler handler=new Handler();
        Log.e(TAG, "getView: position"+position );
        viewHolder.tv_info_title.setText(item.getInfo_title());
        viewHolder.tv_info_date.setText(item.getInfo_date());
        viewHolder.ll_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeInfoDetailClickListener.HomeInfoDetailClick(v,position);
            }
        });



        return convertView;
    }

    private class ViewHolder{
        private TextView tv_info_title,tv_info_date;
        private LinearLayout ll_info;
    }

}
