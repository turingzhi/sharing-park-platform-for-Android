package com.example.myapplication.dao;

import com.example.myapplication.entity.User;
import com.example.myapplication.utils.JDBCUtils;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * author: yan
 * date: 2022.02.17
 * **/
public class UserDao {

    private static final String TAG = "mysql-app-UserDao";

    String username="";

    public String getUsername(int user_id){
        Log.e(TAG, "getUsername: start" );
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();
        String username="";
        try {
            // mysql简单的查询语句。这里是根据user表的userAccount字段来查询某条记录
            String sql = "select * from tb_user where user_id = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, user_id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        //注意：下标是从1开始
                        username = rs.getString("username");
                        Log.e(TAG, "getUsername: "+username );

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "异常login：" + e.getMessage());
        }
        return username;

    }

    /**
     * function: 登录
     * */
    public int login(String username, String password){

        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();
        int msg = 0;
        try {
            // mysql简单的查询语句。这里是根据user表的userAccount字段来查询某条记录
            String sql = "select * from tb_user where username = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    Log.e(TAG,"账号：" + username);
                    //根据账号进行查询
                    ps.setString(1, username);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    int count = rs.getMetaData().getColumnCount();
                    //将查到的内容储存在map里
                    while (rs.next()){
                        // 注意：下标是从1开始的
                        for (int i = 1;i <= count;i++){
                            String field = rs.getMetaData().getColumnName(i);
                            map.put(field, rs.getString(field));
                        }
                    }
                    connection.close();
                    ps.close();

                    if (map.size()!=0){
                        StringBuilder s = new StringBuilder();
                        //寻找密码是否匹配
                        for (String key : map.keySet()){
                            if(key.equals("password")){
                                if(password.equals(map.get(key))){
                                    msg = 1;            //密码正确
                                }
                                else
                                    msg = 2;            //密码错误
                                break;
                            }
                        }
                    }else {
                        Log.e(TAG, "查询结果为空");
                        msg = 3;
                    }
                }else {
                    msg = 0;
                }
            }else {
                msg = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "异常login：" + e.getMessage());
            msg = 0;
        }
        return msg;
    }


    /**
     * function: 注册
     * */
    public boolean register(User user){
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "insert into tb_user(username,nickname,password,email,telephone) values (?,?,?,?,?)";
            Log.e(TAG, "register:sql " );
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    Log.e(TAG, "register: rs1" );
                    //将数据插入数据库
                    ps.setString(1,user.getUsername());
                    ps.setString(2,user.getNickname());
                    ps.setString(3,user.getPassword());
                    ps.setString(4,user.getEmail());
                    ps.setString(5,user.getTelephone());

                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    Log.e(TAG, "register: rs2" );
                    if(rs>0)
                        return true;
                    else
                        return false;
                }else {
                    return  false;
                }
            }else {
                return  false;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
            return false;
        }

    }

    /**
     * function: 根据账号进行查找该用户是否存在
     * */
    public User findUser(String username) {

        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();
        User user = null;
        try {
            String sql = "select * from tb_user where username = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        //注意：下标是从1开始
                        int user_id = rs.getInt(1);
                        String username1 = rs.getString(2);
                        String nickname = rs.getString(3);
                        String password = rs.getString(4);
                        String email = rs.getString(5);
                        String telephone = rs.getString(6);
                        int status=rs.getInt(7);
                        String user_pic=rs.getString(8);

                        user = new User(user_id, username1, nickname, password,email,telephone,status,user_pic);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "异常findUser：" + e.getMessage());
            return null;
        }
        return user;
    }

    public int update(User user) {

        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();
        String nickname1 = user.getNickname();
        String email1 = user.getEmail();
        String telephone1 = user.getTelephone();
        String password1 = user.getPassword();
        String username1 = user.getUsername();
        String user_pic1=user.getUser_pic();
        int user_id =user.getUser_id();
        try {
            String sql = "update tb_user set username=?,nickname=?,password=?,email=?,telephone=?,user_pic=? where user_id = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, username1);
                    ps.setString(2, nickname1);
                    ps.setString(3, password1);
                    ps.setString(4, email1);
                    ps.setString(5, telephone1);
                    ps.setString(6,user_pic1);
                    ps.setInt(7, user_id);
                    int rs = ps.executeUpdate();
                    return 1;

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "异常update：" + e.getMessage());
            return 0;
        }
        return 0;
    }//需要修改一下

    public int updateStatus(int user_id,int status) {

        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "update tb_user set status=? where user_id = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, status);
                    ps.setInt(2, user_id);
                    int rs = ps.executeUpdate();
                    return 1;

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "异常updateStatus：" + e.getMessage());
            return 0;
        }
        return 0;
    }

}
