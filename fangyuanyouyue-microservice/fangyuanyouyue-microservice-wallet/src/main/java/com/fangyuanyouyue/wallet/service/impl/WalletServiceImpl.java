package com.fangyuanyouyue.wallet.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import com.fangyuanyouyue.wallet.dto.WechatPayDto;
import com.fangyuanyouyue.wallet.utils.PayCommonUtil;
import com.fangyuanyouyue.wallet.utils.PropertyUtil;
import com.fangyuanyouyue.wallet.utils.WechatUtil.MyWechatConfig;
import com.fangyuanyouyue.wallet.utils.WechatUtil.WXPay;
import com.fangyuanyouyue.wallet.utils.WechatUtil.WXPayUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.wallet.dao.ConfinedUserMapper;
import com.fangyuanyouyue.wallet.dao.UserBalanceDetailMapper;
import com.fangyuanyouyue.wallet.dao.UserInfoExtMapper;
import com.fangyuanyouyue.wallet.dao.UserRechargeDetailMapper;
import com.fangyuanyouyue.wallet.dao.UserVipMapper;
import com.fangyuanyouyue.wallet.dao.UserWalletMapper;
import com.fangyuanyouyue.wallet.dto.WalletDto;
import com.fangyuanyouyue.wallet.model.ConfinedUser;
import com.fangyuanyouyue.wallet.model.UserInfoExt;
import com.fangyuanyouyue.wallet.model.UserRechargeDetail;
import com.fangyuanyouyue.wallet.model.UserVip;
import com.fangyuanyouyue.wallet.model.UserWallet;
import com.fangyuanyouyue.wallet.service.WalletService;

@Service(value = "walletService")
public class WalletServiceImpl implements WalletService{
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private ConfinedUserMapper confinedUserMapper;
    @Autowired
    private UserVipMapper userVipMapper;
    @Autowired
    private UserRechargeDetailMapper userRechargeDetailMapper;
    @Autowired
    private UserBalanceDetailMapper userBalanceDetailMapper;

    @Override
    public void recharge(Integer userId, BigDecimal amount, Integer type) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包失败！");
        }
        if(type.intValue() == 1){
            //微信
        }else if(type.intValue() == 2){
            //支付宝
        }else{
            throw new ServiceException("充值类型错误！");
        }
        //用户充值明细
        UserRechargeDetail userRechargeDetail = new UserRechargeDetail();
        userRechargeDetail.setUserId(userId);
        userRechargeDetail.setAmount(amount);
        userRechargeDetail.setPayType(type);
        userRechargeDetail.setPayNo("");
        userRechargeDetail.setAddTime(DateStampUtils.getTimesteamp());
        userRechargeDetailMapper.insert(userRechargeDetail);
        //扣除余额 1充值 2消费 payType 1微信 2支付宝 3余额
        updateBalance(userId,amount,1);
    }

    @Override
    public void withdrawDeposit(Integer userId, BigDecimal amount, Integer type, String account, String realName, String payPwd) throws ServiceException {
        //获取被限制的用户（代理不可以余额提现）
        ConfinedUser confinedUser = confinedUserMapper.selectByUserIdStatus(userId, 0);
        if(confinedUser != null){
            throw new ServiceException("此用户被限制使用余额提现！");
        }
        UserInfoExt userInfoExt = userInfoExtMapper.selectUserInfoExtByUserId(userId);
        if(type != 1){//提现方式 0支付宝 1微信
            if(StringUtils.isEmpty(payPwd)){
                throw new ServiceException("支付密码为空！");
            }
            if(userInfoExt.getPayPwd()==null){
                throw new ServiceException("请先设置支付密码再提现");
            }
            if(MD5Util.verify(userInfoExt.getPayPwd(),MD5Util.MD5(payPwd)) == false){
                throw new ServiceException("支付密码错误！");
            }
        }
        //TODO 根据用户会员等级扣除不同手续费
        UserVip userVip = userVipMapper.selectByUserId(userId);
        Integer vipLevel = userVip.getVipLevel();//会员等级
        BigDecimal charge;//手续费
        BigDecimal percent;
        if(vipLevel.intValue() == 1){
            //铂金会员
            percent = new BigDecimal(0.008);
        }else if(vipLevel.intValue() == 2){
            //至尊会员
            percent = new BigDecimal(0.006);
        }else{
            //普通用户
            percent = new BigDecimal(0.008);
        }
        charge = amount.multiply(percent);
        amount = amount.add(charge);
        //扣除余额 type 类型 1充值 2消费 payType 支付类型 1微信 2支付宝 3余额
        updateBalance(userId,amount,2);
    }


    @Override
    public WalletDto getWallet(Integer userId) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包失败！");
        }else{
            UserInfoExt userInfoExt = userInfoExtMapper.selectUserInfoExtByUserId(userId);
            if(userInfoExt == null){
                throw new ServiceException("获取用户扩展信息失败！");
            }else{
                UserVip userVip = userVipMapper.selectByUserId(userId);
                WalletDto walletDto = new WalletDto(userWallet,userVip);
                //信誉度
                walletDto.setCredit(userInfoExt.getCredit());
                long credit = userInfoExt.getCredit();
                if(credit < -100){//差
                    walletDto.setCreditLevel(1);
                }else if(-100 <= credit && credit < 1000){//低
                    walletDto.setCreditLevel(2);
                }else if(1000 <= credit && credit < 10000){//中
                    walletDto.setCreditLevel(3);
                }else if(10000 <= credit && credit < 500000){//高
                    walletDto.setCreditLevel(4);
                }else if(500000 <= credit){//优
                    walletDto.setCreditLevel(5);
                }else{
                    throw new ServiceException("信誉度错误！");
                }
                return walletDto;
            }
        }
    }

    @Override
    public void updateScore(Integer userId, Long score,Integer type) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            if(type == 1){//增加积分 同时增加总积分和积分余额
                userWallet.setScore(userWallet.getScore()+score);
                userWallet.setPoint(userWallet.getPoint()+score);
            }else{//减少积分
                Long updateScore = userWallet.getPoint()-score;
                //修改积分后的用户积分余额
                //如果修改后的积分余额低于0，就返回积分不足
                if(updateScore < 0){
                    throw new ServiceException("积分不足！");
                }
                userWallet.setScore(updateScore);
                userWallet.setPoint(updateScore);
            }
            userWalletMapper.updateByPrimaryKey(userWallet);
        }
    }

    @Override
    public void updateBalance(Integer userId, BigDecimal amount,Integer type) throws ServiceException {
        //获取用户钱包信息
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            if(type.intValue() == 1){//充值
                userWallet.setBalance(userWallet.getBalance().add(amount));
            }else if(type.intValue() == 2){//消费
                //获取被限制的用户（代理不可以余额提现）
                ConfinedUser confinedUser = confinedUserMapper.selectByUserIdStatus(userId, 0);
                if(confinedUser != null){
                    throw new ServiceException("此用户被限制使用余额提现！");
                }
                if(userWallet.getBalance().compareTo(amount) < 0){//余额小于消费金额
                    throw new ServiceException("余额不足！");
                }else{
                    userWallet.setBalance(userWallet.getBalance().subtract(amount));
                }
            }else{
                throw new ServiceException("类型错误！");
            }
            userWalletMapper.updateByPrimaryKey(userWallet);
        }
    }

    @Override
    public Integer getAppraisalCount(Integer userId) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            return userWallet.getAppraisalCount() == null?0:userWallet.getAppraisalCount();
        }
    }

    @Override
    public void updateAppraisalCount(Integer userId, Integer count) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            if(userWallet.getAppraisalCount() < count){
                throw new ServiceException("剩余鉴定次数不足！");
            }else{
                userWallet.setAppraisalCount(userWallet.getAppraisalCount()-count);
                userWalletMapper.updateByPrimaryKey(userWallet);
            }
        }
    }

    @Override
    public void updateCredit(Integer userId, Long credit, Integer type) throws ServiceException {
        UserInfoExt userInfoExt = userInfoExtMapper.selectUserInfoExtByUserId(userId);
        if (userInfoExt == null) {
            throw new ServiceException("获取用户扩展信息失败！");
        } else {
            if(type == 1){//增加
                userInfoExt.setCredit(userInfoExt.getCredit()+credit);
            }else{
                userInfoExt.setCredit(userInfoExt.getCredit()-credit);
            }
            userInfoExtMapper.updateByPrimaryKey(userInfoExt);
        }
    }

    @Override
    public void updatePayPwd(Integer userId, String payPwd, String newPwd) throws ServiceException {
        UserInfoExt userInfoExt = userInfoExtMapper.selectUserInfoExtByUserId(userId);
        //判断旧密码是否正确
        if(!MD5Util.verify(MD5Util.MD5(payPwd),userInfoExt.getPayPwd())){
            throw new ServiceException("旧密码不正确！");
        }
        userInfoExt.setPayPwd(MD5Util.generate(MD5Util.MD5(newPwd)));
    }



//    @Value("${alipay_notify}")
//    private String ALIPAY_NOTIFY;//支付宝后台通知地址
//
//    @Value("${wechat_notify}")
//    private String WECHAT_VIP;//微信后台通知地址

    public static void main(String[] args) throws Exception {
        //模拟下单
        /**
         * 必须参数：
         *    字段名       变量名             示例值                                 描述
         * 1  公众账号ID   appid              wxd678efh567hg6787                     微信支付分配的公众账号ID（企业号corpid即为此appId）
         * 2  商户号       mch_id             1230000109                             微信支付分配的商户号
         * 3  随机字符串   nonce_str          5K8264ILTKCH16CQ2502SI8ZNMTM67VS       随机字符串，长度要求在32位以内。推荐随机数生成算法
         * 4  签名         sign               C380BEC2BFD727A4B6845133519F3AD6       通过签名算法计算得出的签名值，详见签名生成算法
         * 5  商品描述     body               腾讯充值中心-QQ会员充值                商品简单描述，该字段请按照规范传递，具体请见参数规定
         * 6  商户订单号   out_trade_no       20150806125346                         商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号
         * 7  标价金额     total_fee          88                                     订单总金额，单位为分，详见支付金额
         * 8  终端IP       spbill_create_ip   123.12.12.123                          APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
         * 9  通知地址     notify_url         http://www.weixin.qq.com/wxpay/pay.php 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
         * 10 交易类型     trade_type         JSAPI                                  JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付
         */
//        MyWechatConfig config = new MyWechatConfig();
//        WXPay wxpay = new WXPay(config);
//        Map<String, String> data = new HashMap<>();
//        data.put("appid",config.getAppID());
//        data.put("mch_id",config.getMchID());
//        data.put("nonce_str", "5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
//        data.put("sign", "C380BEC2BFD727A4B6845133519F3AD6");
//        data.put("body", "小方圆-微信在线支付");
//        data.put("out_trade_no", "20150806125346");
//        data.put("total_fee", "1");
//        data.put("spbill_create_ip", "192.168.1.6");
//        data.put("trade_type", "APP");
//        data.put("fee_type", "CNY");
//
//        try {
//            Map<String, String> resp = wxpay.unifiedOrder(data);
//            System.out.println(resp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



//        SortedMap<Object, Object> parameters = PayCommonUtil.getWXPrePayID(); // 获取预付单，此处已做封装，需要工具类
//        parameters.put("body", "小方圆-微信在线支付");
//        parameters.put("spbill_create_ip", "127.0.0.1");
//        parameters.put("out_trade_no", "20150806125346");
//        parameters.put("total_fee", "1"); // 测试时，每次支付一分钱，微信支付所传的金额是以分为单位的，因此实际开发中需要x100
//        // parameters.put("total_fee", orders.getOrderAmount()*100+""); // 上线后，将此代码放开
//
//        // 设置签名
//        String sign = PayCommonUtil.createSign("UTF-8", parameters);
//        parameters.put("sign", sign);
//        // 封装请求参数结束
//
//        String requestXML = PayCommonUtil.getRequestXml(parameters); // 获取xml结果
//        System.out.println("封装请求参数是：" + requestXML);
//        // 调用统一下单接口
//        String result = PayCommonUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", requestXML);
//        System.out.println("调用统一下单接口：" + result);
//        SortedMap<Object, Object> parMap = PayCommonUtil.startWXPay(result);
//        System.out.println("最终的map是：" + parMap.toString());
    }

    @Override
    public WechatPayDto orderPayByWechat(Integer orderId, String orderNo, BigDecimal price) throws Exception {

        SortedMap<Object, Object> parameters = PayCommonUtil.getWXPrePayID(); // 获取预付单，此处已做封装，需要工具类
        parameters.put("body", "小方圆-微信在线支付");
        parameters.put("spbill_create_ip", "127.0.0.1");
        parameters.put("out_trade_no", "20150806125346");
        parameters.put("total_fee", "1"); // 测试时，每次支付一分钱，微信支付所传的金额是以分为单位的，因此实际开发中需要x100
        // parameters.put("total_fee", orders.getOrderAmount()*100+""); // 上线后，将此代码放开

        // 设置签名
        String sign = PayCommonUtil.createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        // 封装请求参数结束

        String requestXML = PayCommonUtil.getRequestXml(parameters); // 获取xml结果
        System.out.println("封装请求参数是：" + requestXML);
        // 调用统一下单接口
        String result = PayCommonUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", requestXML);
        System.out.println("调用统一下单接口：" + result);
        SortedMap<Object, Object> parMap = PayCommonUtil.startWXPay(result);
        System.out.println("最终的map是：" + parMap.toString());
        return null;
    }

    @Override
    public WechatPayDto orderPayByALi(Integer orderId) throws ServiceException {
        return null;
    }
}
