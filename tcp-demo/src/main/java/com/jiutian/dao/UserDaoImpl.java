package com.jiutian.dao;

import com.jiutian.pojo.User;

import java.sql.*;

/**
 * @Date: 2022/3/20 21:11
 * @Author: jiutian
 * @Description:
 */
public class UserDaoImpl implements UserDao{
    String dbName = "my_net";
    //连接Mysql数据库驱动程序的写法
    String driver = "com.mysql.cj.jdbc.Driver";
    //定义URL，数据库访问的地址
    String url = "jdbc:mysql://localhost:3306/" + dbName + "?characterEncoding=UTF-8&serverTimezone=GMT%2B8";
    //定义数据库用户名和密码
    String username = "root";
    String password = "123456";

    @Override
    public User getUserById(Long id) {
        User user = null;
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "select id, user_name, pwd from my_net.user where id = " + "'" + id + "'";

        PreparedStatement pstat = null;
        try {
            assert conn != null;
            pstat = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = null;
        long s1 = 0L;
        String s2 = null;
        String s3 = null;
        try {
            assert pstat != null;
            rs = pstat.executeQuery();
            while (rs.next()) {
                s1 = rs.getLong("id");
                s2 = rs.getString("user_name");
                s3 = rs.getString("pwd");
            }
            user = new User(s1,s2, s3);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getUserByName(String userName) {
        User user = null;
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "select id, user_name, pwd  from my_net.user where user_name = " + "'" + userName + "'";

        PreparedStatement pstat = null;
        try {
            assert conn != null;
            pstat = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = null;
        long s1 = 0L;
        String s2 = null;
        String s3 = null;
        try {
            assert pstat != null;
            rs = pstat.executeQuery();
            while (rs.next()) {
                s1 = rs.getLong("id");
                s2 = rs.getString("user_name");
                s3 = rs.getString("pwd");
            }
            user = new User(s1,s2, s3);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
