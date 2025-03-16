package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.entity.Order;
import com.example.myapplication.utils.Base64Util;

import java.util.List;


/*
* 加载租用别人车位的历史订单
* */
public class OrderAdapter extends BaseAdapter {

    final String TAG = "OrderAdapter";

    private Context context;

    private List<Order> orderList;

    com.example.myapplication.DeleteClickListener deleteClickListener;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public OrderAdapter(Context context, List<Order> orderList, com.example.myapplication.DeleteClickListener deleteClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.deleteClickListener = deleteClickListener;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView==null){   //tv_park_name,tv_park_loca,tv_park_price,tv_park_owner,tv_park_desc
            convertView= LayoutInflater.from(context).inflate(R.layout.item_history_rent_order,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_order_id=convertView.findViewById(R.id.tv_order_id);
            viewHolder.tv_park_loca=convertView.findViewById(R.id.tv_park_loca);
            viewHolder.tv_park_desc=convertView.findViewById(R.id.tv_park_desc);
            viewHolder.tv_owner_nickname=convertView.findViewById(R.id.tv_owner_nickname);
            viewHolder.tv_rent_time=convertView.findViewById(R.id.tv_rent_time);
            viewHolder.tv_park_price=convertView.findViewById(R.id.tv_park_price);
            viewHolder.tv_park_total_price=convertView.findViewById(R.id.tv_park_total_price);
            viewHolder.btn_delete=convertView.findViewById(R.id.btn_delete);
            viewHolder.iv_park_pic=convertView.findViewById(R.id.iv_park_pic);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        //数据填充
        final Order item = orderList.get(position);
        Handler handler=new Handler();
        Log.e(TAG, "getView: position"+position );
        viewHolder.tv_order_id.setText(String.valueOf(item.getOrderId()));
        viewHolder.tv_park_loca.setText(item.getPark_loca());
        viewHolder.tv_park_desc.setText(item.getParkDesc());
        viewHolder.tv_owner_nickname.setText(item.getOwnerNickname());
        viewHolder.tv_rent_time.setText(String.valueOf(item.getStartTime()+"-"+item.getEndTime()));
        viewHolder.tv_park_price.setText(item.getParkPrice()+"元/小时");
        viewHolder.tv_park_total_price.setText(String.valueOf(item.getTotalPrice())+"元");
        Bitmap bitmap_park_pic = Base64Util.stringToBitmap(item.getPark_pic());
//                    finalViewHolder.iv_park_pic.setImageBitmap(bitmap_park_pic);
        viewHolder.iv_park_pic.setImageBitmap(bitmap_park_pic);

      //  Log.e(TAG, "getView:item.getPark_name "+item.getPark_name() );


//        UserDao userDao = new UserDao();
//        String ownerName = userDao.getUsername(item.getOwner_id());
//        Log.e(TAG, "getView: ownerName"+ownerName);

//        Log.e(TAG, "getView:item.getOwner_id "+getPark.get(item.getOwner_id()));

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClickListener.deleteClick(v,position);
            }
        });
        return convertView;
    }

    public void setDeleteClickListener(com.example.myapplication.DeleteClickListener deleteClickListener) {
        this.deleteClickListener=deleteClickListener;
    }

    private class ViewHolder{

        private TextView tv_order_id,tv_park_loca,tv_park_desc,tv_owner_nickname,tv_rent_time,tv_park_price,tv_park_total_price;
        private ImageView iv_park_pic;
        private Button btn_delete;
    }

    public interface DeleteClickListener {
        void deleteClick(View view, int position);
    }
}
