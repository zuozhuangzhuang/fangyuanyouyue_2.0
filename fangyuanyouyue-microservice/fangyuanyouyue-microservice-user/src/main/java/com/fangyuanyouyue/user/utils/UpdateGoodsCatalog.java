package com.fangyuanyouyue.user.utils;

import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.base.util.wechat.pay.utils.MD5;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class UpdateGoodsCatalog {
    static Connection conn;
    static PreparedStatement ps;
    static ResultSet rs;
    /**
     * 写一个连接数据库的方法
     */
    public static Connection getConnection(){
//        String url="jdbc:mysql://120.77.101.123:3306/auction";
//        String url="jdbc:mysql://120.77.173.90:3306/auction";
        String url="jdbc:mysql://localhost:3306/auction?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=true";
        String userName="root";
//        String userName="gaozhuo";
        String password="123456";
//        String password="xfy1234!@#$";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动！");
            e.printStackTrace();
        }
        try {
            conn= DriverManager.getConnection(url, userName, password);
            if(conn!=null){
                System.out.println("connection successful");
            }
        } catch (SQLException e) {
            System.out.println( "connection fail");
            e.printStackTrace();
        }
        return conn;
    }
/**
 * user_info
 * user_info_ext
 * user_third_party
 * user_wallet
 * user_vip
 * user_withdraw
 * user_balance_detail
 * user_address_info
 * user_fans
 * goods_info
 * goods_img
 * goods_correlation
 * goods_appraisal_detail
 * appraisal_detail
 * forum_info
 * order_info
 * order_detail
 * order_pay
 * order_refund
 */
/**
 * a_user
 * a_user_exp
 * a_user_ext
 * a_user_third
 * a_user_withdraw
 * a_user_finance
 * a_address
 * a_fans
 * a_goods
 * a_goods_pic
 * a_appraisal
 * a_appraisal_pic
 * a_appreciate
 * a_appreciate_pic
 * a_order
 * a_refund_detail
 */
    /**
     * 导出数据，加工为新的sql语句文件
     */
    static void getSqlFile(){
        String selectUser = "select * from a_user limit 0,10";
        List<Map<String,Object>> users = new ArrayList<>();
        try {
            conn=getConnection();//连接数据库
            //执行动态SQL语句。通常通过PreparedStatement实例实现。
            // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
            ps=conn.prepareStatement(selectUser);// 2.创建Satement并设置参数
            rs=ps.executeQuery(selectUser);  // 3.ִ执行SQL语句

            // 4.处理结果集
            while(rs.next()){
                //user_info
                String userInfoSql = getUserInfoSql(rs);
                FileUtils.createFile(String.valueOf(rs.getInt("id")),userInfoSql);
                //user_info_ext
                String userInfoExtSql = getUserExtSql(rs);
                FileUtils.createFile(String.valueOf(rs.getInt("id")),userInfoExtSql);
                //user_wallet
                String userWalletSql = "insert into user_vip (id, user_id, start_time, end_time, vip_level, level_desc, vip_type, status, add_time, update_time, vip_no, is_send_message ) values (";
                FileUtils.createFile(String.valueOf(rs.getInt("id")),userWalletSql);
                //user_vip
                String userVipSql = "insert into user_wallet (id, user_id, balance, balance_frozen, point, score, add_time, update_time, appraisal_count ) values (";
                FileUtils.createFile(String.valueOf(rs.getInt("id")),userVipSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            //释放资源
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    static String getUserExtSql(ResultSet rs) throws SQLException{
        String selectUserExt = "select * from a_user_ext where id = " + Integer.parseInt(rs.getString("user_ext_id"));
        conn=getConnection();//连接数据库
        //执行动态SQL语句。通常通过PreparedStatement实例实现。
        // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
        PreparedStatement ext_ps=conn.prepareStatement(selectUserExt);// 2.创建Satement并设置参数
        ResultSet ext_rs=ext_ps.executeQuery(selectUserExt);  // 3.ִ执行SQL语句
        //user_info_ext
        if(ext_rs.next()){
        String userInfoExtSql = "insert into user_info_ext (id, user_id, identity, name, pay_pwd, status, auth_type, add_time, update_time, credit, fans_count) values (";
            String identity = ext_rs.getString("card_no");
            String name = ext_rs.getString("real_name");
            String payPwd = MD5Util.generate(rs.getString("pay_pwd"));

        return userInfoExtSql;
        }else{
            return null;
        }
    }
    /**
     * user_info表insert语句
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserInfoSql(ResultSet rs) throws SQLException {
        //每个用户生成一个sql文件
        Integer userId = rs.getInt("id");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        String logPwd = MD5Util.generate(rs.getString("login_pwd"));
        String nickName = rs.getString("nickName");
        String imgUrl = rs.getString("img_url");
        Integer gender = Integer.parseInt(rs.getString("gender")==null?"0":rs.getString("gender"));
        String signature = rs.getString("autograph");
        String contact = StringUtils.isEmpty(rs.getString("contact"))?"":rs.getString("contact");
        Integer level = 0;
        String level_desc = "";
        Integer status = Integer.parseInt(rs.getString("status"));
        Date addTime = DateStampUtils.getTimesteamp();
        Integer isRegHx = Integer.parseInt(rs.getString("is_hx"));

        String filecontent = "insert into user_info (" +
                "id, " +
                "phone, " +
                "email, " +
                "address, " +
                "login_pwd, " +
                "nick_name, " +
                "head_img_url, " +
                "bg_img_url, " +
                "gender, " +
                "signature, " +
                "contact, " +
                "level, " +
                "level_desc, " +
                "reg_type, " +
                "reg_platform, " +
                "status, " +
                "pwd_err_count, " +
                "pay_pwd_err_count, " +
                "last_login_time, " +
                "last_login_platform, " +
                "add_time, " +
                "update_time," +
                "is_reg_hx) values ("
                +null+",'"
                +phone+"',"
                +null+",'"
                +address+"','"
                + logPwd+"','"
                +nickName+"','"
                +imgUrl+"',"
                +null+","
                +gender+",'"
                +signature+"','"
                +contact+"',"
                +null+","
                +null+","
                +null+","
                +null+","
                +status+","
                +null+","
                +null+","
                +null+","
                +null+",'"
                +addTime+"',"
                +null+","
                +isRegHx+")";
        return filecontent;
    }
    public static void main(String[] args) throws Exception{
        getSqlFile();
    }


}
