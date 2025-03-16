package com.example.myapplication.dao;

import static java.sql.DriverManager.getConnection;

import android.util.Log;

import com.example.myapplication.entity.Order;
import com.example.myapplication.entity.Park;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDao {

    private static final String TAG = "mysql-app-OrderDao";


    //查询自己租用的车位历史订单
    public List<Order> getAllOrderByIdRent(int user_id) {
        Connection connection = JDBCUtils.getConn();
        Order order = null;
        List<Order> orderArrayList = new ArrayList<>();
        try {
//            String sql = "select A.*,B.username from tb_order A JOIN tb_user B ON A.owner_id=B.user_id";
            String sql = "select order_id,park_id,user_id,DATE_FORMAT(start_time, '%Y-%m-%d %k:%i:%s'),DATE_FORMAT(end_time, '%Y-%m-%d %k:%i:%s'),total_time,park_price,total_price,park_desc,owner_nickname,park_loca,status,park_pic from tb_order where user_id=? and status=2";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, user_id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int order_id = rs.getInt(1);
                        int park_id = rs.getInt(2);
                        int user_id1 = rs.getInt(3);
                        String start_time = rs.getString(4);
                        String end_time = rs.getString(5);
                        double total_time = rs.getDouble(6);
                        double park_price = rs.getDouble(7);
                        double total_price = rs.getDouble(8);
                        String park_desc = rs.getString(9);
                        String owner_nickname = rs.getString(10);
                        String park_loca = rs.getString(11);
                        int status = rs.getInt(12);
                        String park_pic=rs.getString(13);
//                        String username=rs.getString(8);

                        order = new Order(order_id, park_id, user_id1, start_time, end_time, total_time, park_price, total_price, park_desc, owner_nickname, park_loca, status,park_pic);
                        orderArrayList.add(order);
                    }
                    connection.close();
                    ps.close();
                }

            } else {
                Log.e(TAG, "getAllOrderByIdRent: 连接数据库失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderArrayList;
    }

    public boolean deleteOrder(int orderId) {
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "delete from tb_order where order_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, orderId);
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
            Log.e(TAG, "异常delete：" + e.getMessage());
            return false;
        }
    }

    public static List<HashMap<String, Object>> getinfo() throws SQLException {

//       先定义一个List<HashMap<String,Object>>类型的数据并实例化
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

//        调用连接函数，传入数据库名的形参，获得conn对象，因为getConnection的返回类型就是Connection及conn
        Connection conn = JDBCUtils.getConn();

//        由conn对象创建执行sql语句的对象（Statement类型),调用方法createStatement()
        Statement sta = conn.createStatement();

//        定义sql语句
        String sql = "select * from tb_order ";

//        调用Statement对象执行sql语句,返回结果result是ResultSet类型，就是结果集，具体百度
        ResultSet result = sta.executeQuery(sql);

//        判断一下是否为空
        if (result == null) {
            return null;
        }

//        条件是当结果集是否有下一行，这是一个相当于指针的东西，第一次调用时会把第一行设置为当前行，第二次回吧第二行设置为当前行，以此类推，直到没有下一行，循环结束
        while (result.next()) {
//            每次循环都会新实例化一个HashMap对象，用于将遍历到的数据填进去
            HashMap<String, Object> map = new HashMap<>();
//            往map中填数据，map的数据类型相当于键值对
//            键是name，值是result.getString("empname"),意思是结果集指针所在行的字段名中的数据
            map.put("order_name", result.getString("order_name"));
//            每次循环完就添加到list中，最终list的样子是：[{name=xx},{name=aaa},.......]
            list.add(map);


        }
//        最后记得把list返回出去，不然拿不到这个list
        return list;
    }


    public List<Order> getAllOrderByParkListRented(List<Park> parkList) {
        Connection connection = JDBCUtils.getConn();
        Order order = null;
        List<Order> orderArrayList = new ArrayList<>();
        try {

            if (connection != null) {// connection不为null表示与数据库建立了连接
                for (Park park : parkList) {
                    int park_id = park.getPark_id();
//            String sql = "select A.*,B.username from tb_order A JOIN tb_user B ON A.owner_id=B.user_id";
                    String sql = "select order_id,park_id,user_id,DATE_FORMAT(start_time, '%Y-%m-%d %k:%i:%s'),DATE_FORMAT(end_time, '%Y-%m-%d %k:%i:%s'),total_time,park_price,total_price,park_desc,owner_nickname,park_loca,status,park_pic from tb_order where park_id=? and status=2";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    if (ps != null) {
                        ps.setInt(1, park_id);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            int order_id = rs.getInt(1);
                            int park_id1 = rs.getInt(2);
                            int user_id1 = rs.getInt(3);
                            String start_time = rs.getString(4);
                            String end_time = rs.getString(5);
                            double total_time = rs.getDouble(6);
                            double park_price = rs.getDouble(7);
                            double total_price = rs.getDouble(8);
                            String park_desc = rs.getString(9);
                            String owner_nickname = rs.getString(10);
                            String park_loca = rs.getString(11);
                            int status = rs.getInt(12);
                            String park_pic=rs.getString(13);
//                        String username=rs.getString(8);

                            order = new Order(order_id, park_id1, user_id1, start_time, end_time, total_time, park_price, total_price, park_desc, owner_nickname, park_loca, status,park_pic);
                            orderArrayList.add(order);
                        }
                    }
                    ps.close();
                }
                connection.close();

            } else {
                Log.e(TAG, "getAllOrderByParkIdRented: 连接数据库失败");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderArrayList;
    }



    public boolean insertOrder(Order order) {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "insert into tb_order(park_id,user_id,start_time,park_price,park_desc,owner_nickname,park_loca,status,park_pic) values (?,?,?,?,?,?,?,?,?)";
            Log.e(TAG, "register:sql ");
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    Log.e(TAG, "insertOrder: rs1");
                    //将数据插入数据库
                    ps.setInt(1, order.getParkId());
                    ps.setInt(2, order.getUserId());
                    ps.setString(3, order.getStartTime());
                    ps.setDouble(4, order.getParkPrice());
                    ps.setString(5, order.getParkDesc());
                    ps.setString(6, order.getOwnerNickname());
                    ps.setString(7, order.getPark_loca());
                    ps.setInt(8, 1);
                    ps.setString(9,order.getPark_pic());


                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    Log.e(TAG, "insertOrder: rs2");
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
            Log.e(TAG, "异常insertOrder：" + e.getMessage());
            return false;
        }

    }

    public Order findCurrentOrder(int user_id) {
        Connection connection = JDBCUtils.getConn();
        Order order = null;
        try {
//            String sql = "select A.*,B.username from tb_order A JOIN tb_user B ON A.owner_id=B.user_id";
            String sql = "select order_id,park_id,user_id,DATE_FORMAT(start_time, '%Y-%m-%d %k:%i:%s'),DATE_FORMAT(end_time, '%Y-%m-%d %k:%i:%s'),total_time,park_price,total_price,park_desc,owner_nickname,park_loca,status,park_pic from tb_order where user_id=? and status=1";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, user_id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int order_id = rs.getInt(1);
                        int park_id = rs.getInt(2);
                        int user_id1 = rs.getInt(3);
                        String start_time = rs.getString(4);
                        String end_time = rs.getString(5);
                        double total_time = rs.getDouble(6);
                        double park_price = rs.getDouble(7);
                        double total_price = rs.getDouble(8);
                        String park_desc = rs.getString(9);
                        String owner_nickname = rs.getString(10);
                        String park_loca = rs.getString(11);
                        int status = rs.getInt(12);
                        String park_pic=rs.getString(13);
//                        String username=rs.getString(8);

                        order = new Order(order_id, park_id, user_id1, start_time, end_time, total_time, park_price, total_price, park_desc, owner_nickname, park_loca, status,park_pic);
                    }
                    connection.close();
                    ps.close();
                }

            } else {
                Log.e(TAG, "findCurrentOrder: 连接数据库失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }


    public boolean endOrder(int order_id, String endTime, int total_time, Double total_price) {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        try {
            String sql = "update tb_order set end_time=?,total_time=?,total_price=?,status=?  where order_id =?";
            Log.e(TAG, "register:sql ");
            if (connection != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    Log.e(TAG, "insertOrder: rs1");
                    //将数据插入数据库
                    ps.setString(1, endTime);
                    ps.setInt(2, total_time);
                    ps.setDouble(3, total_price);
                    ps.setInt(4, 2);
                    ps.setInt(5, order_id);


                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    Log.e(TAG, "insertOrder: rs2");
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
            Log.e(TAG, "异常insertOrder：" + e.getMessage());
            return false;
        }

    }
}
