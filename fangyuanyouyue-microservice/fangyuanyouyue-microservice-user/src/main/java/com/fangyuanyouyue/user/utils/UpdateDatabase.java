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
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class UpdateDatabase {
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
        String selectUser = "select * from a_user";
        List<Map<String,Object>> users = new ArrayList<>();
        try {
            conn=getConnection();//连接数据库
            //执行动态SQL语句。通常通过PreparedStatement实例实现。
            // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
            ps=conn.prepareStatement(selectUser);// 2.创建Satement并设置参数
            rs=ps.executeQuery(selectUser);  // 3.ִ执行SQL语句
            String title = "insertSql";
            // 4.处理结果集
            StringBuffer insertSql = new StringBuffer();
            while(rs.next()){
                //user_info
                String userInfoSql = getUserInfoSql(rs);
                if(StringUtils.isNotEmpty(userInfoSql)){
                    insertSql.append(userInfoSql+";\r\n");
                }
                //user_info_ext
                String userInfoExtSql = getUserExtSql(rs);
                if(StringUtils.isNotEmpty(userInfoExtSql)){
                    insertSql.append(userInfoExtSql+";\r\n");
                }
                //user_vip
                String userVipSql = getUserVipSql(rs);
                if(StringUtils.isNotEmpty(userVipSql)){
                    insertSql.append(userVipSql+";\r\n");
                }
                //user_wallet
                String userWalletSql = getUserWalletSql(rs);
                if(StringUtils.isNotEmpty(userWalletSql)){
                    insertSql.append(userWalletSql+";\r\n");
                }
                //user_third_party
                if(StringUtils.isNotEmpty(rs.getString("wechat_cliend"))){
                    String userWechatSql = getUserThirdSql(rs,1);
                    if(StringUtils.isNotEmpty(userWechatSql)){
                        insertSql.append(userWechatSql+";\r\n");
                    }
                }
                if(StringUtils.isNotEmpty(rs.getString("qq_cliend"))){
                    String userQQSql = getUserThirdSql(rs,2);
                    if(StringUtils.isNotEmpty(userQQSql)){
                        insertSql.append(userQQSql+";\r\n");
                    }
                }
                insertSql.append("\r\n");
            }
            FileUtils.createFile(title,insertSql.toString().replace("null","NULL").replace("'NULL'","NULL"));
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

    /**
     * user_third_party
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserThirdSql(ResultSet rs,Integer type) throws SQLException {
        Integer userId = rs.getInt("id")+100000;
        String unionId = null;
        String appOpenId = null;
        String nickName = null;
        String headImgUrl = null;
        String addTime = DateStampUtils.formatUnixTime(rs.getLong("add_time"),DateUtil.DATE_FORMT);

        if(type == 1){
            unionId = rs.getString("wechat_cliend");
        }else{
            unionId = rs.getString("qq_cliend");
        }
        StringBuffer userThirdSql = new StringBuffer("insert into user_third_party (" +
                "id, " +
                "user_id, " +
                "type," +
                "union_id, " +
                "app_open_id, " +
                "mp_open_id, " +
                "mini_open_id, " +
                "nick_name, " +
                "head_img_url, " +
                "add_time, " +
                "update_time, " +
                "session_key" +
                ") values ("
                +null+","
                +userId+","
                +type+",'"
                +unionId+"','"
                +appOpenId+"',"
                +null+","
                +null+",'"
                +nickName+"','"
                +headImgUrl+"','"
                +addTime+"',"
                +null+","
                +null+")");
        return userThirdSql.toString();
    }

    /**
     * user_wallet
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserWalletSql(ResultSet rs) throws SQLException{
        Integer userId = rs.getInt("id")+100000;
        BigDecimal balance = rs.getBigDecimal("balance");
        BigDecimal balanceFrozen = new BigDecimal(0);
        Long point = 0L;
        Long score = 0L;
        String addTime = DateStampUtils.formatUnixTime(rs.getLong("add_time"),DateUtil.DATE_FORMT);
        Integer appraisalCount = 1;
        if(StringUtils.isNotEmpty(rs.getString("user_exp_id"))){
            try {
                String selectUserExt = "select * from a_user_exp where id = " + rs.getInt("user_exp_id");
                conn = getConnection();//连接数据库
                //执行动态SQL语句。通常通过PreparedStatement实例实现。
                // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
                ps = conn.prepareStatement(selectUserExt);// 2.创建Satement并设置参数
                rs = ps.executeQuery(selectUserExt);  // 3.ִ执行SQL语句
                if (rs.next()) {
                    //TODO 旧等级与新等级经验值规则是否一致，不一致的话以经验值为主还是等级为主
                    score = Long.valueOf(rs.getInt("exp"));
                }
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
        StringBuffer userWalletSql = new StringBuffer("insert into user_wallet (" +
                "id, " +
                "user_id, " +
                "balance, " +
                "balance_frozen, " +
                "point, " +
                "score, " +
                "add_time, " +
                "update_time, " +
                "appraisal_count ) values ( "
                +null+","
                +userId+","
                +balance+","
                +balanceFrozen+","
                +point+","
                +score+",'"
                +addTime+"',"
                +null+","
                +appraisalCount+")");
        return userWalletSql.toString();
    }

    /**
     * user_vip
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserVipSql(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt("id")+100000;
        Integer status = 2;
        String addTime = DateStampUtils.formatUnixTime(rs.getLong("add_time"),DateUtil.DATE_FORMT);
        StringBuffer userVipSql = new StringBuffer("insert into user_vip (" +
                "id, " +
                "user_id, " +
                "start_time, " +
                "end_time, " +
                "vip_level, " +
                "level_desc, " +
                "vip_type, " +
                "status, " +
                "add_time, " +
                "update_time, " +
                "vip_no, " +
                "is_send_message ) values ("
                +null+","
                +userId+","
                +null+","
                +null+","
                +null+","
                +null+","
                +null+","
                +status+",'"
                +addTime+"',"
                +null+","
                +null+","
                +null+")");
        return userVipSql.toString();
    }

    /**
     * user_info_ext
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserExtSql(ResultSet rs) throws SQLException{
        Integer userId = rs.getInt("id")+100000;
        String addTime = DateStampUtils.formatUnixTime(rs.getLong("add_time"),DateUtil.DATE_FORMT);
        Long credit = 0L;
        Integer fansCount = rs.getInt("fans_count");
        Integer status = 3;
        Integer authType = 3;
        String payPwd = MD5Util.generate(rs.getString("pay_pwd"));
        String identity = null;
        String name = null;
        if(rs.getString("is_ext") != null && rs.getString("is_ext").equals("1")){
            try{
                String selectUserExt = "select * from a_user_ext where id = " + rs.getInt("user_ext_id");
                conn=getConnection();//连接数据库
                //执行动态SQL语句。通常通过PreparedStatement实例实现。
                // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
                ps=conn.prepareStatement(selectUserExt);// 2.创建Satement并设置参数
                rs=ps.executeQuery(selectUserExt);  // 3.ִ执行SQL语句
                if(rs.next()){
                    identity = rs.getString("card_no");
                    name = rs.getString("real_name");
                    if(rs.getString("status").equals("0")){
                        status = 1;
                    }else if(rs.getString("status").equals("1")){
                        status = 2;
                    }
                }
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
        StringBuffer userInfoExtSql = new StringBuffer("insert into user_info_ext (" +
                "id, " +
                "user_id, " +
                "identity, " +
                "name, " +
                "pay_pwd, " +
                "status, " +
                "auth_type, " +
                "add_time, " +
                "update_time, " +
                "credit, " +
                "fans_count) values ("
                +null+","
                +userId+",'"
                +identity+"','"
                +name+"','"
                +payPwd+"',"
                +status+","
                +authType+",'"
                +addTime+"',"
                +null+","
                +credit+","
                +fansCount+")");
        return userInfoExtSql.toString();
    }
    /**
     * user_info
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserInfoSql(ResultSet rs) throws SQLException {
        //每个用户生成一个sql文件
        Integer userId = rs.getInt("id")+100000;
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
        String addTime = DateStampUtils.formatUnixTime(rs.getLong("add_time"),DateUtil.DATE_FORMT);
        Integer isRegHx = Integer.parseInt(rs.getString("is_hx"));

        StringBuffer filecontent = new StringBuffer("insert into user_info (" +
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
                "`level`, " +
                "level_desc, " +
                "reg_type, " +
                "reg_platform, " +
                "`status`, " +
                "pwd_err_count, " +
                "pay_pwd_err_count, " +
                "last_login_time, " +
                "last_login_platform, " +
                "add_time, " +
                "update_time," +
                "is_reg_hx) values ("
                +userId+",'"
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
                +isRegHx+")");
        return filecontent.toString();
    }
    public static void main(String[] args) throws Exception{
        getSqlFile();
//        Date s = DateStampUtils.getDate(new Date().getTime());
//        System.out.println(s);
    }


}
