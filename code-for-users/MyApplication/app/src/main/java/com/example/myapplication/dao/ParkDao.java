package com.example.myapplication.dao;

import static java.sql.DriverManager.getConnection;

import android.util.Log;

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

public class ParkDao {

    private static final String TAG = "mysql-app-ParkDao";

    public List<Park> getAllPark() {
        Connection connection = JDBCUtils.getConn();
        Park park = null;
        List<Park> parkArrayList = new ArrayList<>();
        try {
//            String sql = "select A.*,B.username from tb_park A JOIN tb_user B ON A.owner_id=B.user_id";
            String sql = "select * from tb_park";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int park_id = rs.getInt(1);
                        int owner_id = rs.getInt(2);
                        String park_name = rs.getString(3);
                        String park_loca = rs.getString(4);
                        double park_price = rs.getDouble(5);
                        int park_status = rs.getInt(6);
                        String park_desc = rs.getString(7);
                        String owner_nickname=rs.getString(8);
                        String park_pic=rs.getString(9);
//                        String username=rs.getString(8);

                        park = new Park(park_id,owner_id,park_name,park_loca,park_price,park_status,park_desc,owner_nickname,park_pic);
                        parkArrayList.add(park);
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
        return parkArrayList;
    }


    public List<Park> getAllParkById(int user_id) {
        Connection connection = JDBCUtils.getConn();
        Park park = null;
        List<Park> parkArrayList = new ArrayList<>();
        try {
//            String sql = "select A.*,B.username from tb_park A JOIN tb_user B ON A.owner_id=B.user_id";
            String sql = "select * from tb_park where owner_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1,user_id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int park_id = rs.getInt(1);
                        int owner_id = rs.getInt(2);
                        String park_name = rs.getString(3);
                        String park_loca = rs.getString(4);
                        double park_price = rs.getDouble(5);
                        int park_status = rs.getInt(6);
                        String park_desc = rs.getString(7);
                        String owner_nickname=rs.getString(8);
                        String park_pic=rs.getString(9);
//                        String username=rs.getString(8);

                        park = new Park(park_id,owner_id,park_name,park_loca,park_price,park_status,park_desc,owner_nickname,park_pic);
                        parkArrayList.add(park);
                    }
                    connection.close();
                    ps.close();
                }

            }
            else {
                Log.e(TAG, "getAllParkById: 连接数据库失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parkArrayList;
    }

    public boolean setParkStatusRented (int park_id) {
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "update tb_park set park_status =2 where park_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, park_id);

                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    if (rs > 0)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常set：" + e.getMessage());
            return false;
        }

    }

    public boolean setParkStatusUnRented (int park_id) {
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "update tb_park set park_status =1 where park_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, park_id);

                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    if (rs > 0)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常set：" + e.getMessage());
            return false;
        }

    }



    public List<Park> getOtherParkCanBeRented(int user_id) {
        Connection connection = JDBCUtils.getConn();
        Park park = null;
        List<Park> parkArrayList = new ArrayList<>();
        try {
//            String sql = "select A.*,B.username from tb_park A JOIN tb_user B ON A.owner_id=B.user_id";
            String sql = "select * from tb_park where park_status=1 and owner_id!=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1,user_id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int park_id = rs.getInt(1);
                        int owner_id = rs.getInt(2);
                        String park_name = rs.getString(3);
                        String park_loca = rs.getString(4);
                        double park_price = rs.getDouble(5);
                        int park_status = rs.getInt(6);
                        String park_desc = rs.getString(7);
                        String owner_nickname=rs.getString(8);
                        String park_pic=rs.getString(9);
                        park = new Park(park_id,owner_id,park_name,park_loca,park_price,park_status,park_desc,owner_nickname,park_pic);
                        parkArrayList.add(park);
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
        return parkArrayList;

    }

    public boolean addPark(Park park) {
        Connection connection = JDBCUtils.getConn();
        try {
            String sql = "insert into tb_park(owner_id,park_name,park_loca,park_price,park_status,park_descp,owner_nickname,park_pic) values (?,?,?,?,?,?,?,?)";
            Log.e(TAG, "register:sql " );
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    Log.e(TAG, "addPark: rs1" );
                    //将数据插入数据库
                    ps.setInt(1,park.getOwner_id());
                    ps.setString(2,park.getPark_name());
                    ps.setString(3,park.getPark_loca());
                    ps.setDouble(4,park.getPark_price());
                    ps.setInt(5,park.getPark_status());
                    ps.setString(6,park.getPark_descp());
                    ps.setString(7,park.getOwner_nickname());
                    ps.setString(8,park.getPark_pic());

                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    Log.e(TAG, "addPark: rs2" );
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
            Log.e(TAG, "异常addPark：" + e.getMessage());
            return false;
        }
    }

    public boolean deletePark(int park_id) {
        Connection connection = JDBCUtils.getConn();
        try {
            String sql = "delete from tb_park where park_id=?";
            Log.e(TAG, "deletePark:sql "+park_id);
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, park_id);
                    int rs = ps.executeUpdate();
                    if (rs > 0)
                        return true;
                    else
                        return false;
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常deletePark：" + e.getMessage());
            return false;
        }
        return false;
    }

    public Park findParkById(int park_id) {
        Connection connection = JDBCUtils.getConn();
        Park park = null;
        List<Park> parkArrayList = new ArrayList<>();
        try {
//            String sql = "select A.*,B.username from tb_park A JOIN tb_user B ON A.owner_id=B.user_id";
            String sql = "select * from tb_park where park_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1,park_id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        int park_id1 = rs.getInt(1);
                        int owner_id = rs.getInt(2);
                        String park_name = rs.getString(3);
                        String park_loca = rs.getString(4);
                        double park_price = rs.getDouble(5);
                        int park_status = rs.getInt(6);
                        String park_desc = rs.getString(7);
                        String owner_nickname=rs.getString(8);
                        String park_pic=rs.getString(9);
//                        String username=rs.getString(8);

                        park = new Park(park_id1,owner_id,park_name,park_loca,park_price,park_status,park_desc,owner_nickname,park_pic);

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
        return park;


    }

    public int update(Park park) {
        Connection connection = JDBCUtils.getConn();
        try {
            String sql = "update tb_park set park_name=?,park_loca=?,park_price=?,park_descp=?,park_status=0 ,park_pic=? where park_id=?";
            Log.e(TAG, "register:sql " );
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    Log.e(TAG, "addPark: rs1" );
                    //将数据插入数据库
                    ps.setString(1,park.getPark_name());
                    ps.setString(2,park.getPark_loca());
                    ps.setDouble(3,park.getPark_price());
                    ps.setString(4,park.getPark_descp());
                    ps.setString(5,park.getPark_pic());
                    ps.setInt(6,park.getPark_id());

                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    Log.e(TAG, "addPark: rs2" );
                    if(rs>0)
                        return 1;
                    else
                        return 0;
                }else {
                    return  0;
                }
            }else {
                return  0;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "异常addPark：" + e.getMessage());
            return 0;
        }

    }

}
