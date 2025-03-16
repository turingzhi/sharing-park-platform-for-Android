package com.example.myapplication.dao;

import static java.sql.DriverManager.getConnection;

import android.util.Log;

import com.example.myapplication.entity.Info;
import com.example.myapplication.entity.Park;
import com.example.myapplication.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoDao {

    private static final String TAG = "mysql-app-ParkDao";

    public List<Info> getAllInfo() {
        Connection connection = JDBCUtils.getConn();
        Info info = null;
        List<Info> infoArrayList = new ArrayList<>();
        try {
//            String sql = "select A.*,B.username from tb_park A JOIN tb_user B ON A.owner_id=B.user_id";
            String sql = "select info_id,info_title,info_desc,info_date,info_pic from tb_info";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int info_id = rs.getInt(1);
                        String info_title = rs.getString(2);
                        String info_desc = rs.getString(3);
                        String info_date=rs.getString(4);
                        String info_pic=rs.getString(5);

                        info = new Info(info_id,info_title,info_desc,info_date,info_pic);
                        infoArrayList.add(info);
                    }
                    connection.close();
                    ps.close();
                }

            }
            else {
                Log.e(TAG, "getAllPark: 连接数据库失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infoArrayList;
    }

    public Info getInfoById(int info_id) {
        Connection connection = JDBCUtils.getConn();
        Info info = null;
        try {
//            String sql = "select A.*,B.username from tb_park A JOIN tb_user B ON A.owner_id=B.user_id";
            String sql = "select info_id,info_title,info_desc,info_date,info_pic from tb_info where info_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1,info_id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int info_id1 = rs.getInt(1);
                        String info_title = rs.getString(2);
                        String info_desc = rs.getString(3);
                        String info_date=rs.getString(4);
                        String info_pic=rs.getString(5);

                        info = new Info(info_id1,info_title,info_desc,info_date,info_pic);
                    }
                    connection.close();
                    ps.close();
                }

            }
            else {
                Log.e(TAG, "getAllPark: 连接数据库失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

}
