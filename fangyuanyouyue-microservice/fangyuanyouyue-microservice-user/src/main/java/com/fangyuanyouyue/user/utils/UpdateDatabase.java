package com.fangyuanyouyue.user.utils;

import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
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
    public static Connection getOldConnection(){
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
    public static Connection getNewConnection(){
        String url="jdbc:mysql://localhost:3306/new_xiaofangyuan?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=true";
        String userName="root";
        String password="123456";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动！");
            e.printStackTrace();
        }
        try {
            Connection new_conn= DriverManager.getConnection(url, userName, password);
            if(new_conn!=null){
                System.out.println("connection successful");
            }
            return new_conn;
        } catch (SQLException e) {
            System.out.println( "connection fail");
            e.printStackTrace();
        }
        return null;
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
 *
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
 *
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
     * a_user_fans
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserFansSql(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt("id")+100000;
        Integer toUserId = null;
        String addTime = null;

        StringBuffer userFansSql = new StringBuffer();
        PreparedStatement fans_ps = null;
        ResultSet fans_rs = null;
        try{
            String selectUserFans = "select * from a_fans where user_id =" + rs.getInt("id");
            fans_ps = conn.prepareStatement(selectUserFans);
            fans_rs = fans_ps.executeQuery(selectUserFans);
            while (fans_rs.next()){
                toUserId = fans_rs.getInt("to_user_id")+100000;
                addTime = DateStampUtils.formatUnixTime(fans_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                userFansSql.append("insert into user_fans (" +
                        "id, " +
                        "user_id, " +
                        "to_user_id," +
                        "add_time) values ("
                        +null+","
                        +userId+","
                        +toUserId+",'"
                        +addTime+"');\r\n"
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //释放资源
            try {
                fans_ps.close();
                fans_rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userFansSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_address
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserAddressSql(ResultSet rs) throws SQLException{
        Integer userId = rs.getInt("id")+100000;
        String receiverName = null;
        String receiverPhone = null;
        String province = null;
        String city = null;
        String area = null;
        String address = null;
        String postCode = null;
        //类型 1默认地址 2其他
        Integer type = null;
        String addTime = null;
        String updateTime = null;

        StringBuffer userAddressSql = new StringBuffer();
        PreparedStatement address_ps = null;
        ResultSet address_rs = null;
        try{
            String selectUserAddress = "select * from a_address where user_id =" + rs.getInt("id");
            address_ps = conn.prepareStatement(selectUserAddress);
            address_rs = address_ps.executeQuery(selectUserAddress);
            while (address_rs.next()){
                receiverName = address_rs.getString("user_name");
                receiverPhone = address_rs.getString("phone");
                province = address_rs.getString("province");
                city = address_rs.getString("city");
                area = address_rs.getString("area");
                address = address_rs.getString("address");
                type = address_rs.getInt("is_default")==0?1:2;
                addTime = DateStampUtils.formatUnixTime(address_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                userAddressSql.append("insert into user_address_info (" +
                        "id, " +
                        "user_id, " +
                        "receiver_name," +
                        "receiver_phone, " +
                        "province, " +
                        "city, " +
                        "area, " +
                        "address, " +
                        "post_code," +
                        "type, " +
                        "add_time, " +
                        "update_time" +
                        ") values ("
                        +null+","
                        +userId+",'"
                        +receiverName+"','"
                        +receiverPhone+"','"
                        +province+"','"
                        +city+"','"
                        +area+"','"
                        +address+"','"
                        +postCode+"',"
                        +type+",'"
                        +addTime+"',"
                        +updateTime+");\r\n"
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //释放资源
            try {
                address_ps.close();
                address_rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userAddressSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_user_finance
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserFinanceSql(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt("id")+100000;
        BigDecimal amount = null;
        BigDecimal beforAmount = null;
        BigDecimal afterAmount = null;
        //支付类型 1微信 2支付宝 3余额 4小程序
        Integer payType = null;
        //收支类型 1收入 2支出 3退款
        Integer type = null;
        Date updateTime = null;
        String title = null;
        String orderNo = null;
        //订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺
        Integer orderType = null;
        Integer sellerId = null;
        Integer buyerId = null;
        String payNo = null;
        String addTime = null;

        StringBuffer userFinanceSql = new StringBuffer();
        PreparedStatement finance_ps = null;
        ResultSet finance_rs = null;
        try{
            String selectUserWithWraw = "select * from a_user_finance where user_id = " + rs.getInt("id");
            //执行动态SQL语句。通常通过PreparedStatement实例实现。
            // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
            finance_ps = conn.prepareStatement(selectUserWithWraw);// 2.创建Satement并设置参数
            finance_rs = finance_ps.executeQuery(selectUserWithWraw);  // 3.ִ执行SQL语句
            while (finance_rs.next()) {
                amount = finance_rs.getBigDecimal("price");
                //新：payType 支付类型 1微信 2支付宝 3余额 4小程序
                //旧：type 收支方式0支付宝  1微信  2余额支付  4订单退款
                payType = finance_rs.getInt("type")==0?2:finance_rs.getInt("type")==2?3:finance_rs.getInt("type")==4?3:1;
                //新：type 收支类型 1收入 2支出 3退款
                //新：orderType 订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺 11系统修改余额
                //旧：status 收支类型 0充值 1表示收入 2表示支出 3表示提现 4订单退款 5提现拒绝返回额度 6卖家退款 7系统操作
                Integer status = finance_rs.getInt("status");
                title = finance_rs.getString("goods_name");
                if(status.equals(0)){
                    type = 1;
                    orderType = 6;
                }else if(status.equals(1)){
                    type = 1;
                    orderType = 1;
                }else if(status.equals(2)){
                    type = 2;
                    orderType = 1;
                }else if(status.equals(3)){
                    type = 2;
                    orderType = 7;
                }else if(status.equals(4)){
                    type = 3;
                    orderType = 1;
                }else if(status.equals(5)){
                    type = 3;
                    orderType = 7;
                }else if(status.equals(6)){
                    type = 3;
                    orderType = 1;
                }else{
                    if(title.endsWith("增加")){
                        type = 1;
                    }else{
                        type = 2;
                    }
                    orderType = 11;
                }
                orderNo = finance_rs.getString("order_no");
                if(StringUtils.isEmpty(orderNo)){
                    //订单号
                    final IdGenerator idg = IdGenerator.INSTANCE;
                    orderNo = idg.nextId();
                }
                beforAmount = finance_rs.getBigDecimal("before_price");
                afterAmount = finance_rs.getBigDecimal("after_price");
                addTime = DateStampUtils.formatUnixTime(finance_rs.getLong("add_time"),DateUtil.DATE_FORMT);

                userFinanceSql.append("insert into user_balance_detail (" +
                        "id, " +
                        "user_id, " +
                        "amount," +
                        "befor_amount, " +
                        "after_amount, " +
                        "pay_type," +
                        "type, " +
                        "add_time, " +
                        "update_time," +
                        "title, " +
                        "order_no, " +
                        "order_type," +
                        "seller_id, " +
                        "buyer_id, " +
                        "pay_no" +
                        ") values ("
                        +null+","
                        +userId+","
                        +amount+","
                        +beforAmount+","
                        +afterAmount+","
                        +payType+","
                        +type+",'"
                        +addTime+"',"
                        +updateTime+",'"
                        +(title==null?0:title)+"','"
                        +orderNo+"',"
                        +orderType+","
                        +sellerId+","
                        +buyerId+",'"
                        +payNo+"');\r\n"
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //释放资源
            try {
                finance_rs.close();
                finance_ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userFinanceSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_user_withdraw
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserWithdrawSql(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt("id")+100000;
        BigDecimal amount = new BigDecimal(0);
        Integer payType = null;
        Integer status = null;
        String content = null;
        String account = null;
        String realName = null;
        String nickName = null;
        String addTime = null;
        BigDecimal serviceCharge = new BigDecimal(0);
        StringBuffer userWithdrawSql = new StringBuffer();

        PreparedStatement with_ps = null;
        ResultSet with_rs = null;
        try {
            String selectUserWithWraw = "select * from a_user_withdraw where (status = 0 or status = 1) and user_id = " + rs.getInt("id");
            //执行动态SQL语句。通常通过PreparedStatement实例实现。
            // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
            with_ps = conn.prepareStatement(selectUserWithWraw);// 2.创建Satement并设置参数
            with_rs = with_ps.executeQuery(selectUserWithWraw);  // 3.ִ执行SQL语句
            while (with_rs.next()) {
                amount = with_rs.getBigDecimal("price");
                //旧：提现方式 0支付宝 1微信
                //新：提现方式 1微信 2支付宝
                payType = with_rs.getInt("pay_type")==0?2:1;
                //旧：提现状态  0申请中  1申请通过 2已拒绝
                //新：申请状态 1申请中 2提现成功 3申请拒绝
                status = with_rs.getInt("status")==0?1:2;
                account = with_rs.getString("account");
                realName = with_rs.getString("real_name");
                addTime = DateStampUtils.formatUnixTime(with_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                userWithdrawSql.append(
                        "insert into user_withdraw (" +
                        "id, " +
                        "user_id, " +
                        "amount," +
                        "pay_type, " +
                        "status, " +
                        "content," +
                        "account, " +
                        "real_name, " +
                        "add_time," +
                        "service_charge) values ("
                        +null+","
                        +userId+","
                        +amount+","
                        +payType+","
                        +status+",'"
                        +content+"','"
                        +account+"','"
                        +realName+"','"
                        +addTime+"',"
                        +serviceCharge+");\r\n"
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            //释放资源
            try {
                with_rs.close();
                with_ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return userWithdrawSql.toString().replace("null","NULL").replace("'NULL'","NULL");
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
        StringBuffer userThirdSql = new StringBuffer(
                "insert into user_third_party (" +
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
                +null+")"
        );
        return userThirdSql.toString().replace("null","NULL").replace("'NULL'","NULL");
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
            PreparedStatement wallet_ps = null;
            ResultSet wallet_rs = null;
            try {
                String selectUserExt = "select * from a_user_exp where id = " + rs.getInt("user_exp_id");
                //执行动态SQL语句。通常通过PreparedStatement实例实现。
                // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
                wallet_ps = conn.prepareStatement(selectUserExt);// 2.创建Satement并设置参数
                wallet_rs = wallet_ps.executeQuery(selectUserExt);  // 3.ִ执行SQL语句
                while (wallet_rs.next()) {
                    point = Long.valueOf(wallet_rs.getInt("exp"));
                    score = Long.valueOf(wallet_rs.getInt("exp"));
                }
            }finally{
                //释放资源
                try {
                    wallet_rs.close();
                    wallet_ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        StringBuffer userWalletSql = new StringBuffer(
                "insert into user_wallet (" +
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
                +appraisalCount+")"
        );
        return userWalletSql.toString().replace("null","NULL").replace("'NULL'","NULL");
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
        StringBuffer userVipSql = new StringBuffer(
                "insert into user_vip (" +
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
                +null+")"
        );
        return userVipSql.toString().replace("null","NULL").replace("'NULL'","NULL");
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
            PreparedStatement ext_ps = null;
            ResultSet ext_rs = null;
            try{
                String selectUserExt = "select * from a_user_ext where id = " + rs.getInt("user_ext_id");
                //执行动态SQL语句。通常通过PreparedStatement实例实现。
                // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
                ext_ps=conn.prepareStatement(selectUserExt);// 2.创建Satement并设置参数
                ext_rs=ext_ps.executeQuery(selectUserExt);  // 3.ִ执行SQL语句
                while(ext_rs.next()){
                    identity = ext_rs.getString("card_no");
                    name = ext_rs.getString("real_name");
                    if(ext_rs.getString("status").equals("0")){
                        status = 1;
                    }else if(ext_rs.getString("status").equals("1")){
                        status = 2;
                    }
                }
            }finally{
                //释放资源
                try {
                    ext_rs.close();
                    ext_ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        StringBuffer userInfoExtSql = new StringBuffer(
                "insert into user_info_ext (" +
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
                +fansCount+")"
        );
        return userInfoExtSql.toString().replace("null","NULL").replace("'NULL'","NULL");
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

        StringBuffer filecontent = new StringBuffer(
                "insert into user_info (" +
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
                +logPwd+"','"
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
                +isRegHx+")"
        );
        return filecontent.toString().replace("null","NULL").replace("'NULL'","NULL");
    }


    public static void main(String[] args) throws Exception {
        String selectUser = "select * from a_user limit 0,100";
        long start = System.currentTimeMillis();
//        getSqlFile(selectUser);
        insertIntoDatabase(selectUser);
        System.out.println(System.currentTimeMillis()-start);
    }

    /**
     * 导出数据，加工为新的sql语句文件
     */
    static void getSqlFile(String selectUser){
        List<Map<String,Object>> users = new ArrayList<>();
        try {
            conn=getOldConnection();//连接数据库
            //执行动态SQL语句。通常通过PreparedStatement实例实现。
            // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
            ps=conn.prepareStatement(selectUser);// 2.创建Satement并设置参数
            rs=ps.executeQuery(selectUser);  // 3.ִ执行SQL语句
            String title = "insertSql";
            // 4.处理结果集
            StringBuffer insertSql = new StringBuffer();
            while(rs.next()){
                System.out.println("【"+rs.getString("nickName")+"】开始");
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
                //a_user_withdraw
                String userWithdrawSql = getUserWithdrawSql(rs);
                if(StringUtils.isNotEmpty(userWithdrawSql)){
                    insertSql.append(userWithdrawSql);
                }
                //a_user_finance
                String userFinanceSql = getUserFinanceSql(rs);
                if(StringUtils.isNotEmpty(userFinanceSql)){
                    insertSql.append(userFinanceSql);
                }
                //a_address
                String userAddressSql = getUserAddressSql(rs);
                if(StringUtils.isNotEmpty(userAddressSql)){
                    insertSql.append(userAddressSql);
                }
                //a_user_fans
                String userFansSql = getUserFansSql(rs);
                if(StringUtils.isNotEmpty(userFansSql)){
                    insertSql.append(userFansSql);
                }
                insertSql.append("\r\n");
            }

            FileUtils.createFile(title,insertSql.toString());
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
     * 插入数据库
     */
    static void insertIntoDatabase(String selectUser){
        List<Map<String,Object>> users = new ArrayList<>();
        try {
            conn=getOldConnection();//连接数据库
            Connection new_conn = getNewConnection();
            PreparedStatement new_ps = null;
            //执行动态SQL语句。通常通过PreparedStatement实例实现。
            // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
            ps=conn.prepareStatement(selectUser);// 2.创建Satement并设置参数
            rs=ps.executeQuery(selectUser);  // 3.ִ执行SQL语句
            String title = "insertSql";
            // 4.处理结果集
            StringBuffer insertSql = new StringBuffer();
            while(rs.next()){
                System.out.println("【"+rs.getString("nickName")+"】开始");
                //user_info
                String userInfoSql = getUserInfoSql(rs);
                if(StringUtils.isNotEmpty(userInfoSql)){
                    try{
                        new_ps = new_conn.prepareStatement(userInfoSql);
                        new_ps.executeLargeUpdate(userInfoSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //user_info_ext
                String userInfoExtSql = getUserExtSql(rs);
                if(StringUtils.isNotEmpty(userInfoExtSql)){
                    try{
                        new_ps = new_conn.prepareStatement(userInfoExtSql);
                        new_ps.executeLargeUpdate(userInfoExtSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //user_vip
                String userVipSql = getUserVipSql(rs);
                if(StringUtils.isNotEmpty(userVipSql)){
                    try{
                        new_ps = new_conn.prepareStatement(userVipSql);
                        new_ps.executeLargeUpdate(userVipSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //user_wallet
                String userWalletSql = getUserWalletSql(rs);
                if(StringUtils.isNotEmpty(userWalletSql)){
                    try{
                        new_ps = new_conn.prepareStatement(userWalletSql);
                        new_ps.executeLargeUpdate(userWalletSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //user_third_party
                if(StringUtils.isNotEmpty(rs.getString("wechat_cliend"))){
                    String userWechatSql = getUserThirdSql(rs,1);
                    if(StringUtils.isNotEmpty(userWechatSql)){
                        try{
                            new_ps = new_conn.prepareStatement(userWechatSql);
                            new_ps.executeLargeUpdate(userWechatSql);
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(StringUtils.isNotEmpty(rs.getString("qq_cliend"))){
                    String userQQSql = getUserThirdSql(rs,2);
                    if(StringUtils.isNotEmpty(userQQSql)){
                        try{
                            new_ps = new_conn.prepareStatement(userQQSql);
                            new_ps.executeLargeUpdate(userQQSql);
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
                //a_user_withdraw
                String userWithdrawSql = getUserWithdrawSql(rs);
                if(StringUtils.isNotEmpty(userWithdrawSql)){
                    try{
                        new_ps = new_conn.prepareStatement(userWithdrawSql);
                        new_ps.executeLargeUpdate(userWithdrawSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //a_user_finance
                String userFinanceSql = getUserFinanceSql(rs);
                if(StringUtils.isNotEmpty(userFinanceSql)){
                    try{
                        new_ps = new_conn.prepareStatement(userFinanceSql);
                        new_ps.executeLargeUpdate(userFinanceSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //a_address
                String userAddressSql = getUserAddressSql(rs);
                if(StringUtils.isNotEmpty(userAddressSql)){
                    try{
                        new_ps = new_conn.prepareStatement(userAddressSql);
                        new_ps.executeLargeUpdate(userAddressSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //a_user_fans
                String userFansSql = getUserFansSql(rs);
                if(StringUtils.isNotEmpty(userFansSql)){
                    try{
                        new_ps = new_conn.prepareStatement(userFansSql);
                        new_ps.executeLargeUpdate(userFansSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
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
}
