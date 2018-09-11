package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.base.util.WechatUtil.PayCommonUtil;
import com.fangyuanyouyue.base.util.alipay.util.GenOrderUtil;
import com.fangyuanyouyue.base.util.wechat.pay.WechatPay;
import com.fangyuanyouyue.wallet.dao.*;
import com.fangyuanyouyue.wallet.dto.UserBalanceDto;
import com.fangyuanyouyue.wallet.dto.UserCouponDto;
import com.fangyuanyouyue.wallet.dto.WalletDto;
import com.fangyuanyouyue.wallet.model.*;
import com.fangyuanyouyue.wallet.service.UserCouponService;
import com.fangyuanyouyue.wallet.service.WalletService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

@Service(value = "walletService")
@Transactional(rollbackFor=Exception.class)
public class WalletServiceImpl implements WalletService{

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
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserWithdrawMapper userWithdrawMapper;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;

    @Override
    public Object recharge(Integer userId, BigDecimal amount, Integer type) throws Exception {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包失败！");
        }
        //用户充值明细
        UserRechargeDetail userRechargeDetail = new UserRechargeDetail();
        userRechargeDetail.setUserId(userId);
        userRechargeDetail.setAmount(amount);
        userRechargeDetail.setPayType(type);
        //订单号
        final IdGenerator idg = IdGenerator.INSTANCE;
        String id = idg.nextId();
        userRechargeDetail.setPayNo(id);
        userRechargeDetail.setAddTime(DateStampUtils.getTimesteamp());
        userRechargeDetail.setStatus(1);
        userRechargeDetailMapper.insert(userRechargeDetail);

        if(type.intValue() == 1){
            //微信
            WechatPayDto wechatPayDto = orderPayByWechat(userRechargeDetail.getPayNo(), userRechargeDetail.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.recharge_wechat_notify.getNotifUrl());
            return wechatPayDto;
        }else if(type.intValue() == 2){
            //支付宝
            String aliPay = orderPayByALi(userRechargeDetail.getPayNo(), userRechargeDetail.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.recharge_alipay_notify.getNotifUrl());
            return aliPay;
        }else if(type.intValue() == 4){//小程序支付
            WechatPayDto wechatPayDto = orderPayByWechatMini(userId, userRechargeDetail.getPayNo(), userRechargeDetail.getAmount(), NotifyUrl.mini_test_notify.getNotifUrl()+NotifyUrl.recharge_wechat_notify.getNotifUrl());
            return wechatPayDto;
        }else{
            throw new ServiceException("充值类型错误！");
        }
    }

    @Override
    public void withdrawDeposit(Integer userId, BigDecimal amount, Integer type, String account, String realName, String payPwd) throws ServiceException {

        //获取被限制的用户（代理不可以余额提现）
        ConfinedUser confinedUser = confinedUserMapper.selectByUserIdStatus(userId, 0);
        if(confinedUser != null){
            throw new ServiceException("此用户被限制使用余额提现！");
        }
        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setUserId(userId);
        userWithdraw.setAmount(amount);
        userWithdraw.setPayType(type);
        userWithdraw.setStatus(1);

        UserInfoExt userInfoExt = userInfoExtMapper.selectUserInfoExtByUserId(userId);
        if(type != 1){//提现方式 1微信 2支付宝
            if(StringUtils.isEmpty(payPwd)){
                throw new ServiceException("支付密码为空！");
            }
            if(userInfoExt.getPayPwd()==null){
                throw new ServiceException("请先设置支付密码再提现");
            }
            if(MD5Util.verify(MD5Util.MD5(payPwd),userInfoExt.getPayPwd()) == false){
                throw new ServiceException("支付密码错误！");
            }
            userWithdraw.setAccount(account);
            userWithdraw.setRealName(realName);
        }/*else{
            //TODO 微信提现需要用户绑定微信账号

            userWithdraw.setAccount("用户unionID");
        }*/

        userWithdrawMapper.insert(userWithdraw);

        //根据用户会员等级扣除不同手续费
        UserVip userVip = userVipMapper.selectByUserId(userId);
        Integer vipLevel = userVip.getVipLevel();//会员等级
        BigDecimal charge;//手续费
        BigDecimal percent;
        if(vipLevel == null){
            //普通用户
            percent = new BigDecimal(0.008);
        }else{
            if(vipLevel.intValue() == 1){
                //铂金会员
                percent = new BigDecimal(0.008);
            }else{
                //至尊会员
                percent = new BigDecimal(0.006);
            }
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
//                //获取用户优惠券列表
//                List<UserCouponDto> listByUserId = userCouponService.getListByUserId(userId);
//                walletDto.setCouponDtos(listByUserId);
                return walletDto;
            }
        }
    }

    @Override
    public void updateScore(Integer userId, Long score,Integer type) throws ServiceException {
        //TODO 每个用户每天可增加200积分 增加一张用户积分记录表，记录用户积分增加历史，按天筛选，不可超过200分
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            if(type == 1){//增加积分 同时增加总积分和积分余额
                userWallet.setScore(userWallet.getScore()+score);
                userWallet.setPoint(userWallet.getPoint()+score);
                //修改用户等级
                UserInfo info = userInfoMapper.selectByPrimaryKey(userId);
                //积分等级，计算总积分
                setUserLevel(userWallet.getScore(), info);
            }else{//减少积分
                Long updateScore = userWallet.getPoint()-score;
                //修改积分后的用户积分余额
                //如果修改后的积分余额低于0，就返回积分不足
                if(updateScore < 0){
                    throw new ServiceException("积分不足！");
                }
                userWallet.setPoint(updateScore);
            }
            userWalletMapper.updateByPrimaryKey(userWallet);
        }
    }

    /**
     * 根据用户积分设置用户等级
     * @param score
     * @param info
     */
    static void setUserLevel(Long score, UserInfo info) {
        if(score != null) {
            if (0 <= score && score < 500) {//Lv1
                info.setLevel(1);
                info.setLevelDesc("升级还需" + (500 - score) + "积分");
            } else if (500 <= score && score < 3000) {//Lv2
                info.setLevel(2);
                info.setLevelDesc("升级还需" + (3000 - score) + "积分");
            } else if (3000 <= score && score < 10000) {//Lv3
                info.setLevel(3);
                info.setLevelDesc("升级还需" + (10000 - score) + "积分");
            } else if (10000 <= score && score < 30000) {//Lv4
                info.setLevel(4);
                info.setLevelDesc("升级还需" + (30000 - score) + "积分");
            } else if (30000 <= score && score < 80000) {//Lv5
                info.setLevel(5);
                info.setLevelDesc("升级还需" + (80000 - score) + "积分");
            } else if (80000 <= score && score < 200000) {//Lv6
                info.setLevel(6);
                info.setLevelDesc("升级还需" + (200000 - score) + "积分");
            } else if (200000 <= score && score < 600000) {//Lv7
                info.setLevel(7);
                info.setLevelDesc("升级还需" + (600000 - score) + "积分");
            } else if (600000 <= score && score < 1000000) {//Lv8
                info.setLevel(8);
                info.setLevelDesc("升级还需" + (1000000 - score) + "积分");
            } else if (1000000 <= score) {//Lv9
                info.setLevel(9);
                info.setLevelDesc("您已升至满级！");
            }
        }
    }

    @Override
    public boolean updateBalance(Integer userId, BigDecimal amount,Integer type) throws ServiceException {
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
            return true;
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
        //TODO 只做了减少次数，后期可支持 增加次数
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
        userInfoExtMapper.updateByPrimaryKeySelective(userInfoExt);
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
/*
小程序ID	    appid	            是	String(32)	wxd678efh567hg6787	                    微信分配的小程序ID
商户号	        mch_id	            是	String(32)	1230000109	                            微信支付分配的商户号
随机字符串	    nonce_str	        是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	    随机字符串，长度要求在32位以内。推荐随机数生成算法
签名	        sign	            是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	    通过签名算法计算得出的签名值，详见签名生成算法
商品描述	    body	            是	String(128)	腾讯充值中心-QQ会员充值               商品简单描述，该字段请按照规范传递，具体请见参数规定
商户订单号	    out_trade_no	    是	String(32)	20150806125346	                        商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
标价金额	    total_fee	        是	Int	        88	                                    订单总金额，单位为分，详见支付金额
终端IP	        spbill_create_ip	是	String(16)	123.12.12.123	                        APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
通知地址	    notify_url	        是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
交易类型	    trade_type	        是	String(16)	JSAPI	                                小程序取值如下：JSAPI，详细说明见参数规定
用户标识	    openid	            否	String(128)	oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	        trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
*/
//        MyWechatConfig config = new MyWechatConfig();
//        WXPay wxpay = new WXPay(config);
//        Map<String, String> data = new HashMap<>();
//        data.put("appid",config.getAppID());
//        data.put("mch_id",config.getMchID());
//        data.put("nonce_str", PayCommonUtil.CreateNoncestr());
//        data.put("sign", WXPayUtil.generateSignature(data,"key=ShenZhenShiXiaoFangYuan123456789"));
//        data.put("body", "小方圆-微信在线支付");
//        data.put("out_trade_no", "20150806125346121");
//        data.put("total_fee", "1");
//        data.put("spbill_create_ip", "127.0.0.1");
//        data.put("trade_type", "APP");
//        data.put("fee_type", "CNY");
//
//        try {
//            Map<String, String> resp = wxpay.unifiedOrder(data);
//            System.out.println(resp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        SortedMap<Object, Object> parameters = PayCommonUtil.getWXPrePayID("","","1234"); // 获取预付单，此处已做封装，需要工具类
        parameters.put("body", "小方圆-微信在线支付");
        parameters.put("spbill_create_ip", "127.0.0.1");
        parameters.put("out_trade_no", "2015080612534612");
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
        WechatPayDto parMap = PayCommonUtil.startWXPay(result);
        System.out.println("最终的map是：" + parMap.toString());
    }

    @Override
    public WechatPayDto orderPayByWechat(String orderNo, BigDecimal price,String notifyUrl) throws ServiceException {
        WechatPay util = new WechatPay();
        System.out.println("请求路径："+notifyUrl);
        if(price.doubleValue() < 0.01){
            throw new ServiceException("金额不能小于0.01元");
        }
        WechatPayDto dto= util.genOrder(orderNo, price.doubleValue()+"", "小方圆-微信在线支付", notifyUrl, "127.0.0.1");
        if(dto==null|| org.apache.commons.lang3.StringUtils.isEmpty(dto.getPrepayId())||dto.getSign()==null){
            throw new ServiceException("在线支付生成订单信息出错！");
        }
        return dto;
    }

    @Override
    public WechatPayDto orderPayByWechatMini(Integer userId, String orderNo, BigDecimal price, String notifyUrl) throws ServiceException {
        //根据userId获取三方openId
        UserThirdParty userThirdByUserId = userThirdPartyMapper.getUserThirdByUserId(userId, 1);
        String openId = userThirdByUserId.getMiniOpenId();
        WechatPay util = new WechatPay();
        System.out.println("请求路径："+notifyUrl);
        if(price.doubleValue() < 0.01){
            throw new ServiceException("金额不能小于0.01元");
        }
        WechatPayDto dto = util.genOrderMini(openId,orderNo, price.doubleValue()+"", "小方圆-微信小程序支付", notifyUrl, "127.0.0.1");
        if(dto==null|| org.apache.commons.lang3.StringUtils.isEmpty(dto.getPrepayId())||dto.getSign()==null){
            throw new ServiceException("小程序支付生成订单信息出错！");
        }
        return dto;
    }

    @Override
    public String orderPayByALi(String orderNo, BigDecimal price,String notifyUrl) throws ServiceException,Exception {
        GenOrderUtil util = new GenOrderUtil();
        String orderInfo="";
        if(price.doubleValue() < 0.01){
            throw new ServiceException("金额不能小于0.01元");
        }
        orderInfo = util.getOrder(orderNo, "小方圆下单",  "小方圆下单", notifyUrl, price);
        return orderInfo;
    }

    @Override
    public List<UserBalanceDto> billList(Integer userId, Integer start, Integer limit, Integer type, String date) throws ServiceException {
//        List<BillMonthDto> billMonthDtos = new ArrayList<>();
        //按月份筛选余额账单列表
        Date searchDate = new Date();
        if(StringUtils.isNotEmpty(date)){
            searchDate = DateUtil.getTimestamp(date,DateUtil.DATE_FORMT_MONTH);
        }
        List<UserBalanceDetail> userBalanceDetails = userBalanceDetailMapper.selectByUserIdType(userId, start * limit, limit, type, searchDate);
        List<UserBalanceDto> dtoList = UserBalanceDto.toDtoList(userBalanceDetails);
        for(UserBalanceDto dto:dtoList){
            if(dto.getSellerId() != null){
                UserInfo info = userInfoMapper.selectByPrimaryKey(dto.getSellerId());
                //对当前用户来说只有别人的头像

                //1收入 2支出
                dto.setImgUrl(info.getHeadImgUrl());
            }
        }
//        BillMonthDto billMonthDto = new BillMonthDto();
        return dtoList;
    }


    @Override
    public UserBalanceDto billDetail(Integer userId, String orderNo) throws ServiceException {
        UserBalanceDetail userBalanceDetail = userBalanceDetailMapper.selectByUserIdOrderNo(userId,orderNo);
        if(userBalanceDetail == null){
            throw new ServiceException("未找到订单!");
        }
        UserBalanceDto userBalanceDto = new UserBalanceDto(userBalanceDetail);
        UserInfo info = userInfoMapper.selectByPrimaryKey(userBalanceDetail.getSellerId());
        //对当前用户来说只有别人的头像

        //1收入 2支出

        userBalanceDto.setImgUrl(info.getHeadImgUrl());
        return userBalanceDto;
    }

    @Override
    public void addUserBalanceDetail(Integer userId, BigDecimal amount, Integer payType, Integer type, String orderNo, String title,Integer orderType,Integer sellerId,Integer buyerId) throws ServiceException {
        //TODO 确认下单后生成用户和平台收支表信息
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);

        UserBalanceDetail userBalance = new UserBalanceDetail();
        userBalance.setUserId(userId);
        userBalance.setAmount(amount);
        userBalance.setPayType(payType);
        userBalance.setType(type);
        userBalance.setAddTime(DateStampUtils.getTimesteamp());
        userBalance.setTitle(title);
        userBalance.setOrderNo(orderNo);
        userBalance.setOrderType(orderType);
        userBalance.setSellerId(sellerId);
        userBalance.setBuyerId(buyerId);
        userBalanceDetailMapper.insert(userBalance);

    }

    @Override
    public boolean updateOrder(String orderNo, String thirdOrderNo, Integer payType) throws ServiceException {
        //获取订单
        UserRechargeDetail userRechargeDetail = userRechargeDetailMapper.selectByOrderNo(orderNo);
        if(userRechargeDetail == null){
            throw new ServiceException("订单不存在！");
        }
        userRechargeDetail.setStatus(2);
        userRechargeDetailMapper.updateByPrimaryKeySelective(userRechargeDetail);
        //充值
        updateBalance(userRechargeDetail.getUserId(),userRechargeDetail.getAmount(),1);
        return true;
    }
}
