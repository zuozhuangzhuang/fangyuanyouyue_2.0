package com.fangyuanyouyue.base.util.wechat.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author biexiansheng
 *
 */
public class DeleteUser {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");//加载数据库驱动
            System.out.println("加载数据库驱动成功");
            String url="jdbc:mysql://localhost:3306/auction";//声明自己的数据库auction的url
            String user="root";//声明自己的数据库账号
            String password="123456";//声明自己的数据库密码
            //建立数据库连接，获得连接对象conn
            Connection conn=DriverManager.getConnection(url,user,password);
            System.out.println("连接数据库成功");
            String sql="delete from a_user where id=4150";//生成一条sql语句
            String deletSql = "DELETE a.* ,b.* ,c.*\n" +
                    "FROM table1 a\n" +
                    "LEFT JOIN table2 b\n" +
                    "ON a.id = b.aid\n" +
                    "LEFT JOIN table3 c\n" +
                    "ON a.id = c.aid\n" +
                    "WHERE a.id = 1";
            Statement stmt=conn.createStatement();//创建Statement对象
            stmt.executeUpdate(sql);//执行sql语句
            System.out.println("数据库删除成功");
            conn.close();
            System.out.println("数据库关闭成功");//关闭数据库的连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}