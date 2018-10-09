package com.fangyuanyouyue.user.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.enums.Status;
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
//        String url="jdbc:mysql://120.77.173.90:3306/auction?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=true";
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
//        String url="jdbc:mysql://xiaofangyuan-prd.mysql.rds.aliyuncs.com:3306/xiaofangyuan?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=true";
        String userName="root";
//        String userName="wuzhimin";
        String password="123456";
//        String password="Wuzhimin123";
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
 * identity_auth_apply
 * user_third_party
 * user_wallet
 * user_vip
 * user_withdraw
 * user_balance_detail
 * platform_finance_detail
 * user_address_info
 * user_fans
 * goods_info
 * goods_img
 * goods_correlation
 * order_info
 * order_detail
 * order_pay
 * order_refund
 * user_recharge_detail
 * goods_appraisal_detail
 * appraisal_detail
 * forum_info
 * collect
 * confined_user
 *
 */

/**
 * a_user
 * a_user_exp
 * a_user_ext
 * a_user_third
 * a_user_withdraw
 * a_user_finance
 * a_finance
 * a_address
 * a_fans
 * a_goods
 * a_goods_pic
 * a_order
 * a_refund_detail
 * a_appraisal
 * a_appraisal_pic
 * a_appreciate
 * a_appreciate_pic
 * a_confined_user
 * a_collect
 * a_appreciate_collection
 *
 */

    /**
     * comment
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getCommentSql(ResultSet rs) throws SQLException {
        System.out.println("----------comment----------");
        StringBuffer commentSql = new StringBuffer();
        PreparedStatement comment_ps = null;
        ResultSet comment_rs = null;
        PreparedStatement reply_ps = null;
        ResultSet reply_rs = null;
        try{
            /**
             * goods_comment
             */
            Integer id = null;
            Integer goodsId = null;//商品id
            Integer userId = rs.getInt("id")+100000;//用户id
            Integer commentId = null;//回复评论id
            String content = null;//评论内容
            Integer likesCount = 0;//点赞次数
            String img1Url = null;//图片地址1
            String img2Url = null;//图片地址2
            String img3Url = null;//图片地址3
            Integer status = 1;//状态 1正常 2隐藏
            String addTime = null;//添加时间
            String updateTime = null;//更新时间

            /**
             * forum_comment
             */
            Integer forumId;//帖子id

            //评论
            String selectComment = "select * from a_comment where user_id ="+rs.getInt("id");
            comment_ps = conn.prepareStatement(selectComment);
            comment_rs = comment_ps.executeQuery(selectComment);
            while (comment_rs.next()){
                id = comment_rs.getInt("id");
                goodsId = comment_rs.getInt("goods_id");
                content = comment_rs.getString("content");

                forumId = comment_rs.getInt("appreciate_id");
                if(goodsId != null){
                    commentSql.append("insert into goods_comment (" +
                            "id, " +
                            "goods_id, " +
                            "user_id," +
                            "comment_id, " +
                            "content, " +
                            "likes_count," +
                            "img1_url, " +
                            "img2_url, " +
                            "img3_url," +
                            "status, " +
                            "add_time, " +
                            "update_time) values ("
                            +id+","
                            +goodsId+","
                            +userId+","
                            +commentId+",'"
                            +content+"',"
                            +likesCount+",'"
                            +img1Url+"','"
                            +img2Url+"','"
                            +img3Url+"',"
                            +status+",'"
                            +addTime+"','"
                            +updateTime+"');\r\n"
                    );
                }else{
                    commentSql.append("insert into forum_comment (" +
                            "id, " +
                            "user_id, " +
                            "forum_id," +
                            "comment_id, " +
                            "content, " +
                            "status," +
                            "add_time, " +
                            "update_time) values ("
                            +id+","
                            +userId+","
                            +forumId+","
                            +commentId+",'"
                            +content+"',"
                            +status+",'"
                            +addTime+"',"
                            +updateTime+");\r\n"
                    );
                }

                //回复
                String selectReply = "select * from a_reply where comment_id ="+comment_rs.getInt("id");
                reply_ps = conn.prepareStatement(selectComment);
                reply_rs = reply_ps.executeQuery(selectComment);
                while (reply_rs.next()){
                    id = reply_rs.getInt("id");
                    commentId = reply_rs.getInt("comment_id");
                    goodsId = comment_rs.getInt("goods_id");
                    content = comment_rs.getString("content");

                    forumId = comment_rs.getInt("appreciate_id");
                    if(goodsId != null){
                        commentSql.append("insert into goods_comment (" +
                                "id, " +
                                "goods_id, " +
                                "user_id," +
                                "comment_id, " +
                                "content, " +
                                "likes_count," +
                                "img1_url, " +
                                "img2_url, " +
                                "img3_url," +
                                "status, " +
                                "add_time, " +
                                "update_time) values ("
                                +id+","
                                +goodsId+","
                                +userId+","
                                +commentId+",'"
                                +content+"',"
                                +likesCount+",'"
                                +img1Url+"','"
                                +img2Url+"','"
                                +img3Url+"',"
                                +status+",'"
                                +addTime+"','"
                                +updateTime+"');\r\n"
                        );
                    }else{
                        commentSql.append("insert into forum_comment (" +
                                "id, " +
                                "user_id, " +
                                "forum_id," +
                                "comment_id, " +
                                "content, " +
                                "status," +
                                "add_time, " +
                                "update_time) values ("
                                +id+","
                                +userId+","
                                +forumId+","
                                +commentId+",'"
                                +content+"',"
                                +status+",'"
                                +addTime+"',"
                                +updateTime+");\r\n"
                        );
                    }
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(comment_ps != null){
                comment_ps.close();
            }
            if(comment_rs != null){
                comment_rs.close();
            }
            if(reply_ps != null){
                reply_ps.close();
            }
            if(reply_rs != null){
                reply_rs.close();
            }
        }
        return commentSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * collect
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getCollectSql(ResultSet rs) throws SQLException {
        System.out.println("----------collect----------");
        StringBuffer collectSql = new StringBuffer();
        PreparedStatement collect_ps = null;
        ResultSet collect_rs = null;
        try{
            Integer userId = rs.getInt("id")+100000;//用户id
            Integer collectId = null;//收藏对象ID
            Integer collectType = null;//关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏
            Integer type = null;//类型 1关注 2收藏
            String addTime = null;//添加时间
            String updateTime = null;//更新时间

            String selectCollect = "select * from a_collect where user_id ="+rs.getInt("id");
            collect_ps = conn.prepareStatement(selectCollect);
            collect_rs = collect_ps.executeQuery(selectCollect);
            while (collect_rs.next()){
                collectId = collect_rs.getInt("goods_id");
                collectType = 1;
                type = 2;
                addTime = DateStampUtils.formatUnixTime(collect_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                collectSql.append("insert into collect (" +
                        "id, " +
                        "user_id, " +
                        "collect_id," +
                        "collect_type, " +
                        "type, " +
                        "add_time," +
                        "update_time) values ("
                        +null+","
                        +userId+","
                        +collectId+","
                        +collectType+","
                        +type+",'"
                        +addTime+"','"
                        +updateTime+"');\r\n"
                );
            }
            String selectAppraisal = "select * from a_appreciate_collection where user_id ="+rs.getInt("id");
            collect_ps = conn.prepareStatement(selectAppraisal);
            collect_rs = collect_ps.executeQuery(selectAppraisal);
            while (collect_rs.next()){
                collectId = collect_rs.getInt("appreciate_id");
                collectType = 4;
                type = 2;
                addTime = DateStampUtils.formatUnixTime(collect_rs.getLong("create_time"),DateUtil.DATE_FORMT);

                collectSql.append("insert into collect (" +
                        "id, " +
                        "user_id, " +
                        "collect_id," +
                        "collect_type, " +
                        "type, " +
                        "add_time," +
                        "update_time) values ("
                        +null+","
                        +userId+","
                        +collectId+","
                        +collectType+","
                        +type+",'"
                        +addTime+"','"
                        +updateTime+"');\r\n"
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(collect_ps != null){
                collect_ps.close();
            }
            if(collect_rs != null){
                collect_rs.close();
            }
        }
        return collectSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_confined_user
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getConfineUserSql() throws SQLException {
        System.out.println("----------confined_user----------");
        StringBuffer confinedUserSql = new StringBuffer();
        PreparedStatement confine_user_ps = null;
        ResultSet confine_user_rs = null;
        try{
            String selectConfine = "select * from a_confined_user";
            confine_user_ps = conn.prepareStatement(selectConfine);
            confine_user_rs = confine_user_ps.executeQuery(selectConfine);
            while (confine_user_rs.next()){
                Integer userId = confine_user_rs.getInt("id")+100000;//用户id
                Integer status = confine_user_rs.getInt("status");//用于假删除用户  0被限制用户 1被限制后又被解除限制用户（表面正常用户）
                String addTime = DateStampUtils.formatUnixTime(System.currentTimeMillis(),DateUtil.DATE_FORMT);//添加时间
                String updateTime = null;//更新时间
                confinedUserSql.append("insert into confined_user (" +
                        "id, " +
                        "user_id, " +
                        "status," +
                        "add_time, " +
                        "update_time) values ("
                        +null+","
                        +userId+","
                        +status+",'"
                        +addTime+"','"
                        +updateTime+"');\r\n"
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(confine_user_ps != null){
                confine_user_ps.close();
            }
            if(confine_user_rs != null){
                confine_user_rs.close();
            }
        }
        return confinedUserSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_appreciate
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getForumSql(ResultSet rs) throws SQLException {
        System.out.println("----------forum_info----------");
        Integer userId = rs.getInt("id")+100000;//用户id
        StringBuffer forumSql = new StringBuffer();
        PreparedStatement forum_ps = null;
        ResultSet forum_rs = null;
        PreparedStatement forum_pic_ps = null;
        ResultSet forum_pic_rs = null;
        try{
            String selectFroum = "select * from a_appreciate where user_id ="+rs.getInt("id");
            forum_ps = conn.prepareStatement(selectFroum);
            forum_rs = forum_ps.executeQuery(selectFroum);
            while (forum_rs.next()){
                Integer id = forum_rs.getInt("id");
                String title = forum_rs.getString("title").replace("'","‘");//标题
                String videoUrl = null;//视频链接
                Integer videoLength = null;//视频长度
                String label = null;//标签
                Integer sort=forum_rs.getInt("is_top")==0?2:1;//排列优先级 1置顶 2默认排序
                Integer type = 1;//帖子类型 1帖子 2视频
                Integer status = forum_rs.getInt("status")==0?1:2;//状态 1显示 2隐藏
                String addTime = DateStampUtils.formatUnixTime(forum_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                String updateTime = null;//更新时间
                Integer columnId = 1;//专栏id
                Integer isChosen = 2;//是否精选1是 2否
                String videoImg = null;//视频封面图
                String content = null;//内容描述，富文本
                Integer pvCount = forum_rs.getInt("browse_count");//帖子浏览量基数，展示浏览量为基数＋浏览量个数
                String commentTime = DateStampUtils.formatUnixTime(forum_rs.getLong("comment_time"),DateUtil.DATE_FORMT);

                String selectForumPic = "select * from a_appreciate_pic where img_url <> '' and appreciate_id ="+id;
                forum_pic_ps = conn.prepareStatement(selectForumPic);
                forum_pic_rs = forum_pic_ps.executeQuery(selectForumPic);
                int conteneId = 0;
                List<ColumnContentDto> list = new ArrayList<>();
                while (forum_pic_rs.next()){
                    ColumnContentDto contentDto = new ColumnContentDto();
                    contentDto.setId(conteneId++);
                    contentDto.setType(2);
                    contentDto.setImgUrl(forum_pic_rs.getString("img_url"));
                    list.add(contentDto);
                }
                ColumnContentDto contentDto = new ColumnContentDto();
                contentDto.setId(conteneId);
                contentDto.setType(1);
                contentDto.setContent(forum_rs.getString("description"));
                list.add(contentDto);
                Object json = JSONObject.toJSON(list);
                content = json.toString().replace("'","‘").replace("\\","\\/\\/");
                forumSql.append("insert into forum_info (" +
                        "id, " +
                        "user_id, " +
                        "title," +
                        "video_url, " +
                        "video_length, " +
                        "label," +
                        "sort, " +
                        "type, " +
                        "status," +
                        "add_time," +
                        "update_time, " +
                        "column_id," +
                        "is_chosen, " +
                        "video_img, " +
                        "pv_count," +
                        "comment_time, "  +
                        "content) values ("
                        +id+","
                        +userId+",'"
                        +title+"','"
                        +videoUrl+"',"
                        +videoLength+",'"
                        +label+"',"
                        +sort+","
                        +type+","
                        +status+",'"
                        +addTime+"','"
                        +updateTime+"',"
                        +columnId+","
                        +isChosen+",'"
                        +videoImg+"',"
                        +pvCount+",'"
                        +commentTime+"','"
                        +content+"');\r\n"
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(forum_ps != null){
                forum_ps.close();
            }
            if(forum_rs != null){
                forum_rs.close();
            }
            if(forum_pic_ps != null){
                forum_pic_ps.close();
            }
            if(forum_pic_rs != null){
                forum_pic_rs.close();
            }
        }
        return forumSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_appraisal
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getAppraisalSql(ResultSet rs) throws SQLException{
        System.out.println("----------goods_appraisal----------");
        StringBuffer appraisalSql = new StringBuffer();
        Integer userId = rs.getInt("id")+100000;//用户id
        PreparedStatement appraisal_ps = null;
        ResultSet appraisal_rs = null;
        PreparedStatement appraisal_url_ps = null;
        ResultSet appraisal_url_rs = null;
        try{
            /**
             * goods_appraisal_detail
             */
            Integer id = null;//唯一自增ID
            Integer orderId = 0;//鉴定id
            Integer goodsId = null;//商品id
            String opinion = null;//鉴定观点
            Integer status = null;//状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示) 5删除
            String title = null;//鉴定标题
            BigDecimal price = new BigDecimal(0);//鉴定赏金
            String description = null;//描述
            String submitTime = null;//审核时间
            String addTime = null;//添加时间
            String updateTime = null;//更新时间
            Integer type = 3;//鉴定类型 1卖家鉴定 2买家鉴定 3我要鉴定
            Integer isShow = 2;//是否鉴定展示 1是 2否


            String selectAppraisal = "select * from a_appraisal where user_id =" + rs.getInt("id");
            appraisal_ps = conn.prepareStatement(selectAppraisal);
            appraisal_rs = appraisal_ps.executeQuery(selectAppraisal);
            while (appraisal_rs.next()){
                id = appraisal_rs.getInt("id");
                opinion = appraisal_rs.getString("opinion");
                //鉴定状态 0鉴定中  1已鉴定
                //状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示) 5删除
                status = appraisal_rs.getInt("status");
                //鉴定结果状态 0存疑  1 真品  2非真品
                Integer appraisal_type = appraisal_rs.getInt("type");
                if(appraisal_type.equals(0)){
                    status = 3;
                }else if(appraisal_type.equals(1)){
                    status = 1;
                }else if(appraisal_type.equals(2)){
                    status = 2;
                }
                title = appraisal_rs.getString("title").replace("'","‘").replace("\"","”");
                description = appraisal_rs.getString("content").replace("'","‘").replace("\"","”");
                submitTime = DateStampUtils.formatUnixTime(appraisal_rs.getLong("submit_time"),DateUtil.DATE_FORMT);
                addTime = DateStampUtils.formatUnixTime(appraisal_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                appraisalSql.append("insert into goods_appraisal_detail (" +
                        "id, " +
                        "order_id, " +
                        "goods_id," +
                        "opinion, " +
                        "status, " +
                        "price," +
                        "submit_time, " +
                        "type, " +
                        "add_time," +
                        "update_time, " +
                        "user_id, " +
                        "is_show," +
                        "title, " +
                        "description) values ("
                        +id+","
                        +orderId+","
                        +goodsId+",'"
                        +opinion+"',"
                        +status+","
                        +price+",'"
                        +submitTime+"',"
                        +type+",'"
                        +addTime+"','"
                        +updateTime+"',"
                        +userId+","
                        +isShow+",'"
                        +title+"','"
                        +description+"');\r\n"
                );
                /**
                 * appraisal_url
                 */
                String url = null;//图片/视频地址

                Integer urlType = 1;//类型 1图片 2视频 3视频截图
                String selectAppraisalUrl = "select * from a_appraisal_pic where appraisal_id ="+id;
                appraisal_url_ps = conn.prepareStatement(selectAppraisalUrl);
                appraisal_url_rs = appraisal_url_ps.executeQuery(selectAppraisalUrl);
                while (appraisal_url_rs.next()){
                    url = appraisal_url_rs.getString("url");
                    appraisalSql.append("insert into appraisal_url (" +
                            "id, " +
                            "appraisal_id, " +
                            "url," +
                            "type, " +
                            "add_time) values ("
                            +null+","
                            +id+",'"
                            +url+"',"
                            +urlType+",'"
                            +addTime+"');\r\n"
                    );
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(appraisal_ps != null){
                    appraisal_ps.close();
                }
                if(appraisal_rs != null){
                    appraisal_rs.close();
                }
                if(appraisal_url_ps != null){
                    appraisal_ps.close();
                }
                if(appraisal_url_rs != null){
                    appraisal_rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return appraisalSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_order
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getOrderSql(ResultSet rs) throws SQLException {
        System.out.println("----------order_info----------");
        StringBuffer orderSql = new StringBuffer();
        PreparedStatement order_ps = null;
        ResultSet order_rs = null;
        PreparedStatement seller_ps = null;
        ResultSet seller_rs = null;
        PreparedStatement refund_ps = null;
        ResultSet refund_rs = null;

        Integer userId = rs.getInt("id")+100000;//用户id

        try{

            String selectOrder = "select * from a_order where status <> 4 and is_delete <> 0 and sell_delete <> 0 and user_id ="+rs.getInt("id");
            order_ps = conn.prepareStatement(selectOrder);
            order_rs = order_ps.executeQuery(selectOrder);

            while (order_rs.next()){
                String orderAddTime = DateStampUtils.formatUnixTime(order_rs.getLong("add_time"),DateUtil.DATE_FORMT);//添加时间
                String updateTime = null;//更新时间
                String payNo = order_rs.getString("third_no");//支付流水号
                BigDecimal amount = order_rs.getBigDecimal("price");//订单总金额
                Integer status = null;//状态 1待支付 2待发货 3待收货 4已完成 5已取消
                Integer rechargeStatus = 3;//状态 1待支付 2已完成 3已删除
                Integer old_status = order_rs.getInt("status");
                if(old_status.equals(0)){
                    status = 1;
                    rechargeStatus = 1;
                }else if(old_status.equals(1)){
                    status = 2;
                    rechargeStatus = 2;
                }else if(old_status.equals(2)){
                    status = 4;
                    rechargeStatus = 2;
                }else if(old_status.equals(3)){
                    status = 3;
                    rechargeStatus = 2;
                }else if(old_status.equals(4)){
                    status = 5;
                }
                Integer payType = null;//支付类型 1微信 2支付宝 3余额 4小程序
                //付款方式0支付宝支付  1微信支付 2余额支付 3公众号微信支付
                //支付类型 1微信 2支付宝 3余额 4小程序
                Integer oldPayType = order_rs.getInt("pay_type");
                if(oldPayType.equals(0)){
                    payType = 2;
                }else if(oldPayType.equals(1)){
                    payType = 1;
                }else if(oldPayType.equals(2)){
                    payType = 3;
                }else if(oldPayType.equals(3)){
                    payType = 1;
                }else{
                    payType = 3;
                }
                Integer type = order_rs.getInt("type");

                if(type.equals(1)){
                    orderSql.append("insert into user_recharge_detail (" +
                            "id, " +
                            "user_id, " +
                            "amount," +
                            "pay_type, " +
                            "pay_no, " +
                            "add_time," +
                            "update_time," +
                            "status) values ("
                            +null+","
                            +userId+","
                            +amount+","
                            +payType+",'"
                            +payNo+"','"
                            +orderAddTime+"','"
                            +updateTime+"',"
                            +rechargeStatus+");\r\n"
                    );
                }else{
                    /**
                     * order_info
                     */
                    Integer orderId = null;
                    Integer mainOrderId = null;//总订单id
                    String orderNo = order_rs.getString("order_no");//订单号
                    Integer count = null;//商品总数
                    Integer sellerId = null;//卖家ID
                    Integer isResolve = null;//是否拆单 1是 2否
                    Integer isRefund = 2;//是否退货 1是 2否
                    String receiveTime = null;//收货时间
                    Integer sellerIsDelete = 2;//卖家是否删除 1是 2否
                    Integer buyerIsDelete = 2;//买家是否删除 1是 2否

                    /**
                     * order_pay
                     */
                    String receiverName = null;//收货人姓名
                    String receiverPhone = null;//收货人手机
                    String address = null;//详细地址
                    String postCode = null;//邮政编码
                    BigDecimal payAmount = null;//实际支付金额
                    BigDecimal freight = null;//运费金额
                    String payTime = null;//支付时间
                    Integer sendType = null;//配送方式
                    Integer logisticStatus = null;//物流状态
                    String logisticCode = null;//物流单号
                    String logisticCompany = null;//物流公司
                    String remarks = null;//备注
                    String province = null;//省份
                    String city = null;//城市
                    String area = null;//区域
                    String sendTime = null;//发货时间

                    /**
                     * order_detail
                     */
                    Integer id = null;//唯一自增ID
                    Integer goodsId = null;//商品id
                    String goodsName = null;//商品标题
                    Integer couponId = null;//优惠券ID
                    String mainImgUrl = null;//商品主图
                    String description = null;//商品描述
                    Integer goodsType = null;//类型 1普通商品 2抢购商品
                    String logisticCompanyNo = null;//物流编号
                    orderId = order_rs.getInt("id");
                    mainOrderId = orderId;
                    count = 1;

                    String selectSeller = "select * from a_goods where id =" +order_rs.getInt("goods_id");
                    seller_ps = conn.prepareStatement(selectSeller);
                    seller_rs = seller_ps.executeQuery(selectSeller);
                    if(seller_rs.next()){
                        sellerId = seller_rs.getInt("user_id")+100000;
                    }
                    isResolve = 2;
                    Integer returnStatus = order_rs.getInt("return_status");
                    isRefund = returnStatus == null?1:2;
                    sellerIsDelete = order_rs.getInt("sell_delete")==0?1:2;
                    buyerIsDelete = order_rs.getInt("buy_delete")==0?1:2;

                    orderSql.append("insert into order_info (" +
                            "id, " +
                            "user_id, " +
                            "main_order_id," +
                            "order_no, " +
                            "amount, " +
                            "count," +
                            "status, " +
                            "add_time, " +
                            "seller_id," +
                            "is_resolve, " +
                            "is_refund, " +
                            "receive_time," +
                            "update_time, " +
                            "seller_is_delete, " +
                            "buyer_is_delete) values ("
                            +orderId+","
                            +userId+","
                            +mainOrderId+",'"
                            +orderNo+"',"
                            +amount+","
                            +count+","
                            +status+",'"
                            +orderAddTime+"',"
                            +sellerId+","
                            +isResolve+","
                            +isRefund+",'"
                            +receiveTime+"',"
                            +updateTime+","
                            +sellerIsDelete+","
                            +buyerIsDelete+");\r\n"
                    );

                    receiverName = order_rs.getString("receiver");
                    receiverPhone = order_rs.getString("phone");
                    address = order_rs.getString("address");
                    freight = order_rs.getBigDecimal("postage")==null?new BigDecimal(0):order_rs.getBigDecimal("postage");
                    payAmount = amount.add(freight);

                    payTime = DateStampUtils.formatUnixTime(order_rs.getLong("pay_time"),DateUtil.DATE_FORMT);
                    logisticCode = order_rs.getString("logistics");
                    logisticCompany = order_rs.getString("company_name");
                    logisticCompanyNo = order_rs.getString("company_no");
                    province = order_rs.getString("province");
                    city = order_rs.getString("city");
                    area = order_rs.getString("area");
                    sendTime = DateStampUtils.formatUnixTime(order_rs.getLong("send_time"),DateUtil.DATE_FORMT);

                    orderSql.append("insert into order_pay (" +
                            "id," +
                            "order_id," +
                            "receiver_name," +
                            "receiver_phone," +
                            "address," +
                            "post_code," +
                            "order_no," +
                            "amount," +
                            "pay_amount," +
                            "freight," +
                            "count," +
                            "pay_type," +
                            "pay_no," +
                            "pay_time," +
                            "send_type," +
                            "logistic_status," +
                            "logistic_code," +
                            "logistic_company," +
                            "remarks," +
                            "status," +
                            "add_time," +
                            "update_time," +
                            "province," +
                            "city," +
                            "area," +
                            "send_time, " +
                            "logistic_company_no) values ("
                            +null+","
                            +orderId+",'"
                            +receiverName+"','"
                            +receiverPhone+"','"
                            +address+"','"
                            +postCode+"','"
                            +orderNo+"',"
                            +amount+","
                            +payAmount+","
                            +freight+",'"
                            +count+"',"
                            +payType+",'"
                            +payNo+"','"
                            +payTime+"',"
                            +sendType+","
                            +logisticStatus+",'"
                            +logisticCode+"','"
                            +logisticCompany+"','"
                            +remarks+"',"
                            +status+",'"
                            +orderAddTime+"','"
                            +updateTime+"','"
                            +province+"','"
                            +city+"','"
                            +area+"','"
                            +sendTime+"','"
                            +logisticCompanyNo+"');\r\n"
                    );

                    goodsId = order_rs.getInt("goods_id");
                    goodsName = order_rs.getString("content").replace("'","‘").replace("\"","”");

                    orderSql.append("insert into order_detail (" +
                            "id, " +
                            "user_id, " +
                            "order_id," +
                            "goods_id, " +
                            "goods_name, " +
                            "add_time," +
                            "update_time, " +
                            "coupon_id, " +
                            "main_img_url," +
                            "amount, " +
                            "freight, " +
                            "pay_amount," +
                            "seller_id, " +
                            "main_order_id, " +
                            "description) values ("
                            +null+","
                            +userId+","
                            +orderId+","
                            +goodsId+",'"
                            +goodsName+"','"
                            +orderAddTime+"','"
                            +updateTime+"',"
                            +couponId+",'"
                            +mainImgUrl+"',"
                            +amount+","
                            +freight+","
                            +payAmount+","
                            +sellerId+","
                            +mainOrderId+",'"
                            +description+"');\r\n"
                    );
                    if(isRefund.equals(1)){
                        String reason = null;//申请理由
                        String serviceNo = null;//服务单号
                        String pic1 = null;//图片1
                        String pic2 = null;
                        String pic3 = null;
                        String pic4 = null;
                        String pic5 = null;
                        String pic6 = null;
                        String refundAddtime = null;
                        Integer sellerReturnStatus = null;//卖家是否同意退货状态 1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货
                        String refuseReason = null;//卖家处理退货理由
                        String endTime = null;//最终（后台管理）处理时间
                        String dealTime = null;//卖家处理时间
                        String platformReason = null;//平台处理原因

                        String selectRefund = "select * from a_refund_detail where order_id ="+orderId;
                        refund_ps = conn.prepareStatement(selectRefund);
                        refund_rs = refund_ps.executeQuery(selectRefund);
                        while (refund_rs.next()){
                            reason = refund_rs.getString("content");
                            pic1 = refund_rs.getString("file1");
                            pic2 = refund_rs.getString("file2");
                            pic3 = refund_rs.getString("file3");
                            pic4 = refund_rs.getString("file4");
                            pic5 = refund_rs.getString("file5");
                            pic6 = refund_rs.getString("file6");
                            refundAddtime = DateStampUtils.formatUnixTime(refund_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                            //卖家是否同意退货状态 null正常  1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货
                            //卖家是否同意退货状态           1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货
                            sellerReturnStatus = order_rs.getInt("seller_return_status");
                            refuseReason = refund_rs.getString("seller_refuse");
                            refundAddtime = DateStampUtils.formatUnixTime(refund_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                            endTime = DateStampUtils.formatUnixTime(refund_rs.getLong("end_time"),DateUtil.DATE_FORMT);
                            dealTime = DateStampUtils.formatUnixTime(refund_rs.getLong("seller_return_time"),DateUtil.DATE_FORMT);

                            //退货表
                            orderSql.append("insert into order_refund (" +
                                    "id, " +
                                    "order_id, " +
                                    "reason," +
                                    "service_no, " +
                                    "pic1, " +
                                    "pic2," +
                                    "pic3, " +
                                    "pic4, " +
                                    "pic5, " +
                                    "pic6," +
                                    "status, " +
                                    "add_time, " +
                                    "update_time," +
                                    "seller_return_status, " +
                                    "user_id, " +
                                    "refuse_reason," +
                                    "end_time," +
                                    "deal_time," +
                                    "platform_reason) values ("
                                    +null+","
                                    +orderId+",'"
                                    +reason+"','"
                                    +serviceNo+"','"
                                    +pic1+"','"
                                    +pic2+"','"
                                    +pic3+"','"
                                    +pic4+"','"
                                    +pic5+"','"
                                    +pic6+"',"
                                    +returnStatus+",'"
                                    +refundAddtime+"','"
                                    +updateTime+"',"
                                    +sellerReturnStatus+","
                                    +userId+","
                                    +refuseReason+"','"
                                    +endTime+"','"
                                    +dealTime+"','"
                                    +platformReason+"');\r\n"
                            );
                        }
                    }

                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(order_ps != null){
                    order_ps.close();
                }
                if(order_rs != null){
                    order_rs.close();
                }
                if(seller_ps != null){
                    seller_ps.close();
                }
                if(seller_rs != null){
                    seller_rs.close();
                }
                if(refund_ps != null){
                    refund_ps.close();
                }
                if(refund_rs != null){
                    refund_rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return orderSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_goods、a_goods_pic
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getGoodsSql(ResultSet rs) throws SQLException {
        System.out.println("----------goods_info----------");
        StringBuffer goodsSql = new StringBuffer();
        PreparedStatement goods_ps = null;
        ResultSet goods_rs = null;
        PreparedStatement pic_ps = null;
        ResultSet pic_rs = null;
        PreparedStatement correlation_ps = null;
        ResultSet correlation_rs = null;
        try{
            Integer userId = rs.getInt("id")+100000;
            /**
             * goods_info
             */
            Integer goodsId = null;
            String name = null;
            String description = null;
            BigDecimal price = null;
            BigDecimal postage = null;
            //排序 1置顶 2默认
            Integer sort=2;
            String label = null;
            //类型 1普通商品 2抢购商品
            Integer type = null;
            //状态 1出售中 2已售出 3已下架（已结束） 5删除
            Integer status = null;
            BigDecimal floorPrice = null;
            Integer intervalTime = null;
            BigDecimal markdown = null;
            String lastIntervalTime = null;
            //是否官方鉴定 1已鉴定 2未鉴定
            Integer isAppraisal = null;
            String videoUrl = null;
            String addTime = null;
            String updateTime = null;
            Integer videoLength = null;
            BigDecimal startPrice = null;

            /**
             * goods_correlation
             */
            //商品分类ID
            Integer goodsCategoryId = null;
            //商品分类父ID
            Integer categoryParentId = null;
            //类目名称
            String goodsCategoryName = null;


            String selectGoods = "select * from a_goods where is_auction = 0 and (status = 1 or status = 2) and is_delete = 1 and user_id =" + rs.getInt("id");
            goods_ps = conn.prepareStatement(selectGoods);
            goods_rs = goods_ps.executeQuery(selectGoods);
            while (goods_rs.next()){
                goodsId = goods_rs.getInt("id");
                name = goods_rs.getString("title").replace("'","‘").replace("\"","”");
                price = goods_rs.getBigDecimal("start_price");
                postage = goods_rs.getBigDecimal("postage");
                sort = goods_rs.getInt("is_top")==1?1:2;
                type = 1;
                status = goods_rs.getInt("status");
                isAppraisal = 2;
                addTime = DateStampUtils.formatUnixTime(goods_rs.getLong("add_time"),DateUtil.DATE_FORMT);
                startPrice = price;
                description = goods_rs.getString("description").replace("'","‘").replace("\"","”");


                goodsSql.append("insert into goods_info (" +
                        "id, " +
                        "user_id, " +
                        "name," +
                        "price, " +
                        "postage, " +
                        "sort, " +
                        "label, " +
                        "type, " +
                        "status, " +
                        "floor_price, " +
                        "interval_time, " +
                        "markdown," +
                        "last_interval_time, " +
                        "is_appraisal, " +
                        "video_url," +
                        "add_time, " +
                        "update_time, " +
                        "video_length, " +
                        "start_price, " +
                        "comment_time,  " +
                        "description) values ("
                        +goodsId+","
                        +userId+",'"
                        +name+"',"
                        +price+","
                        +postage+","
                        +sort+","
                        +label+","
                        +type+","
                        +status+","
                        +floorPrice+","
                        +intervalTime+","
                        +markdown+","
                        +lastIntervalTime+","
                        +isAppraisal+",'"
                        +videoUrl+"','"
                        +addTime+"',"
                        +updateTime+","
                        +videoLength+","
                        +startPrice+",'"
                        +null+"','"
                        +description+"');\r\n"
                );
                goodsCategoryId = goods_rs.getInt("catalog_new_id");
                String getNewCatagory = "select * from a_catalog_new where id ="+goodsCategoryId;
                correlation_ps = conn.prepareStatement(getNewCatagory);
                correlation_rs = correlation_ps.executeQuery(getNewCatagory);
                while (correlation_rs.next()){
                    categoryParentId = correlation_rs.getInt("parent_id");
                    goodsSql.append("insert into goods_correlation (" +
                            "id, " +
                            "goods_category_id, " +
                            "goods_id," +
                            "category_parent_id, " +
                            "add_time, " +
                            "update_time) values ("
                            +null+","
                            +goodsCategoryId+","
                            +goodsId+","
                            +categoryParentId+",'"
                            +addTime+"',"
                            +updateTime+");\r\n"
                    );
                }

                String selectGoodsImg = "select * from a_goods_pic where goods_id = "+goods_rs.getInt("id");
                pic_ps = conn.prepareStatement(selectGoodsImg);
                pic_rs = pic_ps.executeQuery(selectGoodsImg);
                /**
                 * goods_img
                 */
                //图片地址
                String imgUrl = null;
                //类型 1主图（展示在第一张的图片） 2次图 3视频截图
                Integer imgType = null;
                //排序
                Integer imgSort = null;
                while (pic_rs.next()){
//                    imgUrl = FileUtils.transferFile(pic_rs.getString("img_url"),"pic"+FileUtils.getFileName()+".jpg");
                    imgUrl = pic_rs.getString("img_url");
                    imgType = pic_rs.getInt("is_main")==0?1:2;
                    imgSort = pic_rs.getInt("sort");
                    goodsSql.append("insert into goods_img (" +
                            "id, " +
                            "goods_id, "+
                            "img_url, "+
                            "type, "+
                            "add_time, "+
                            "sort) values ("
                            +null+","
                            +goodsId+",'"
                            +imgUrl+"',"
                            +imgType+",'"
                            +addTime+"',"
                            +imgSort+");\r\n"
                    );
                }

            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //释放资源
            try {
                goods_ps.close();
                goods_rs.close();
//                pic_ps.close();
//                pic_rs.close();
                if(correlation_ps != null){
                    correlation_ps.close();
                }
                if(correlation_rs != null){
                    correlation_rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return goodsSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * a_user_fans
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserFansSql(ResultSet rs) throws SQLException {
        System.out.println("----------user_fans----------");
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
        System.out.println("----------user_address----------");
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
                address = address_rs.getString("address").replace("'","‘").replace("\"","”");
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
        System.out.println("----------user_finance----------");
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

                if(orderType.equals(Status.WITHDRAW.getValue())){
                    type = Status.EXPEND.getValue();
                }else{
                    if(type.equals(Status.EXPEND.getValue())){
                        type = Status.INCOME.getValue();
                    }else{
                        type = Status.EXPEND.getValue();
                    }
                }
                userFinanceSql.append("insert into platform_finance_detail (" +
                        "id, " +
                        "user_id, " +
                        "amount," +
                        "order_no, " +
                        "pay_no, " +
                        "pay_type," +
                        "type, " +
                        "add_time, " +
                        "update_time," +
                        "title, " +
                        "order_type, " +
                        "seller_id," +
                        "buyer_id) values ("
                        +null+","
                        +userId+","
                        +amount+",'"
                        +orderNo+"','"
                        +payNo+"',"
                        +payType+","
                        +type+",'"
                        +addTime+"','"
                        +updateTime+"','"
                        +(title==null?0:title)+"',"
                        +orderType+","
                        +sellerId+","
                        +buyerId+");\r\n"
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
        System.out.println("----------user_withdraw----------");
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
        System.out.println("----------user_third_part----------");
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
                        +null+");\r\n"
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
        System.out.println("----------user_wallet----------");
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
                        +appraisalCount+");\r\n"
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
        System.out.println("----------user_vip----------");
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
                        +null+");\r\n"
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
        System.out.println("----------user_info_ext----------");
        StringBuffer userInfoExtSql = new StringBuffer();
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
                if(ext_rs.next()){
                    identity = ext_rs.getString("card_no");
                    name = ext_rs.getString("real_name");
                    if(ext_rs.getString("status").equals("0")){
                        status = 1;
                    }else if(ext_rs.getString("status").equals("1")){
                        status = 2;
                    }
                    userInfoExtSql.append("insert into identity_auth_apply (" +
                            "id, " +
                            "user_id, " +
                            "name," +
                            "identity, " +
                            "identity_img_cover, " +
                            "identity_img_back," +
                            "reject_desc, " +
                            "status, " +
                            "add_time," +
                            "update_time) values ("
                            +null+","
                            +userId+",'"
                            +name+"','"
                            +identity+"','"
                            +null+"','"
                            +null+"','"
                            +null+"',"
                            +status+",'"
                            +addTime+"','"
                            +null+"');\r\n"
                    );
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
        userInfoExtSql.append(
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
                        +fansCount+");\r\n"
        );
//        System.out.println(userInfoExtSql.toString().replace("null","NULL").replace("'NULL'","NULL"));
        return userInfoExtSql.toString().replace("null","NULL").replace("'NULL'","NULL");
    }

    /**
     * user_info
     * @param rs
     * @return
     * @throws SQLException
     */
    static String getUserInfoSql(ResultSet rs) throws SQLException {
        System.out.println("----------user_info----------");
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
        Integer status = rs.getInt("status")==0?1:2;
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
                        +isRegHx+");\r\n"
        );
        return filecontent.toString().replace("null","NULL").replace("'NULL'","NULL");
    }


    public static void main(String[] args) throws Exception {
        String selectUser = "select * from a_user";
        long start = System.currentTimeMillis();
//        getSqlFile(selectUser);
        insertIntoDatabase(selectUser);
        String timeDifference = DateUtil.getTimeDifference(System.currentTimeMillis(), start);
        System.out.println(timeDifference);
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
            int all = 7610;
            StringBuffer insertSql = new StringBuffer();
            while(rs.next()){
                String nickName = rs.getString("nickName");
                System.out.println("【"+nickName+"】开始");
                //user_info
                String userInfoSql = getUserInfoSql(rs);
                if(StringUtils.isNotEmpty(userInfoSql)){
                    insertSql.append(userInfoSql);
                }
                //user_info_ext
                String userInfoExtSql = getUserExtSql(rs);
                if(StringUtils.isNotEmpty(userInfoExtSql)){
                    insertSql.append(userInfoExtSql);
                }
                //user_vip
                String userVipSql = getUserVipSql(rs);
                if(StringUtils.isNotEmpty(userVipSql)){
                    insertSql.append(userVipSql);
                }
                //user_wallet
                String userWalletSql = getUserWalletSql(rs);
                if(StringUtils.isNotEmpty(userWalletSql)){
                    insertSql.append(userWalletSql);
                }
                //user_third_party
                if(StringUtils.isNotEmpty(rs.getString("wechat_cliend"))){
                    String userWechatSql = getUserThirdSql(rs,1);
                    if(StringUtils.isNotEmpty(userWechatSql)){
                        insertSql.append(userWechatSql);
                    }
                }
                if(StringUtils.isNotEmpty(rs.getString("qq_cliend"))){
                    String userQQSql = getUserThirdSql(rs,2);
                    if(StringUtils.isNotEmpty(userQQSql)){
                        insertSql.append(userQQSql);
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
                //a_goods
                String goodsSql = getGoodsSql(rs);
                if(StringUtils.isNotEmpty(goodsSql)){
                    insertSql.append(goodsSql);
                }
                //a_order
                String orderSql = getOrderSql(rs);
                if(StringUtils.isNotEmpty(orderSql)){
                    insertSql.append(orderSql);
                }
                //a_appraisal
                String appraisalSql = getAppraisalSql(rs);
                if(StringUtils.isNotEmpty(appraisalSql)){
                    insertSql.append(appraisalSql);
                }
                //a_appreciate
                String forumSql = getForumSql(rs);
                if(StringUtils.isNotEmpty(forumSql)){
                    insertSql.append(forumSql);
                }
                //collect
                String collectSql = getCollectSql(rs);
                if(StringUtils.isNotEmpty(collectSql)){
                    insertSql.append(collectSql);
                }
                insertSql.append("\r\n");
                System.out.println("【"+nickName+"】结束,剩余人数："+(--all));
            }
            FileUtils.createFile(title,insertSql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            //释放资源
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(conn != null){
                    conn.close();
                }
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
            String confineUserSql = getConfineUserSql();
            if(StringUtils.isNotEmpty(confineUserSql)){
                try{
                    new_ps = new_conn.prepareStatement(confineUserSql);
                    new_ps.executeLargeUpdate(confineUserSql);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            //执行动态SQL语句。通常通过PreparedStatement实例实现。
            // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
            ps=conn.prepareStatement(selectUser);// 2.创建Satement并设置参数
            rs=ps.executeQuery(selectUser);  // 3.ִ执行SQL语句
            String title = "insertSql";
            // 4.处理结果集
            int all = 7610;
            while(rs.next()){
                String nickName = rs.getString("nickName");
                System.out.println("【"+nickName+"】开始");
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
                //a_goods
                String goodsSql = getGoodsSql(rs);
                if(StringUtils.isNotEmpty(goodsSql)){
                    try{
                        new_ps = new_conn.prepareStatement(goodsSql);
                        new_ps.executeLargeUpdate(goodsSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //a_order
                String orderSql = getOrderSql(rs);
                if(StringUtils.isNotEmpty(orderSql)){
                    try{
                        new_ps = new_conn.prepareStatement(orderSql);
                        new_ps.executeLargeUpdate(orderSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //a_appraisal
                String appraisalSql = getAppraisalSql(rs);
                if(StringUtils.isNotEmpty(appraisalSql)){
                    try{
                        new_ps = new_conn.prepareStatement(appraisalSql);
                        new_ps.executeLargeUpdate(appraisalSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //a_appreciate
                String forumSql = getForumSql(rs);
                if(StringUtils.isNotEmpty(forumSql)){
                    try{
                        new_ps = new_conn.prepareStatement(forumSql);
                        new_ps.executeLargeUpdate(forumSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                //collect
                String collectSql = getCollectSql(rs);
                if(StringUtils.isNotEmpty(collectSql)){
                    try{
                        new_ps = new_conn.prepareStatement(collectSql);
                        new_ps.executeLargeUpdate(collectSql);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }

                System.out.println("【"+nickName+"】结束,剩余人数："+(--all));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            //释放资源
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
