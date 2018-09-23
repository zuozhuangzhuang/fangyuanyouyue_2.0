package com.fangyuanyouyue.wallet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.wallet.dao.UserVipCouponDetailMapper;
import com.fangyuanyouyue.wallet.dao.UserVipMapper;
import com.fangyuanyouyue.wallet.dao.VipOrderMapper;
import com.fangyuanyouyue.wallet.model.UserVip;
import com.fangyuanyouyue.wallet.model.UserVipCouponDetail;
import com.fangyuanyouyue.wallet.model.VipOrder;
import com.fangyuanyouyue.wallet.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Service(value = "userVipService")
@Transactional(rollbackFor=Exception.class)
public class UserVipServiceImpl implements UserVipService{

    @Autowired
    private UserVipMapper userVipMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private VipOrderMapper vipOrderMapper;
    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserVipCouponDetailMapper userVipCouponDetailMapper;

    @Override
    public boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException {
        VipOrder vipOrder = vipOrderMapper.selectByOrderNo(orderNo);
        if(vipOrder == null){
            throw new ServiceException("订单不存在！");
        }
        UserVip userVip = userVipMapper.selectByUserId(vipOrder.getUserId());
        if(userVip == null){
            throw new ServiceException("获取用户会员信息失败！");
        }else{
            StringBuffer time = new StringBuffer();
            time.append(vipOrder.getVipType().intValue() == Status.VIP_TYPE_ONE_MONTH.getValue()?"一个月"
                    :(vipOrder.getVipType().intValue() == Status.VIP_TYPE_THREE_MONTH.getValue()?"三个月":"一年"));
            if(vipOrder.getType() == 1){
                //开通
                if(userVip.getStatus().intValue() == Status.IS_VIP.getValue()){//已开通
                    throw new ServiceException("已开通会员！");
                }else{
                    //开始时间：当前时间
                    userVip.setStartTime(DateStampUtils.getTimesteamp());
                    //计算结束时间
                    if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_ONE_MONTH.getValue()){
                        userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),1));
                    }else if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_THREE_MONTH.getValue()){
                        userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),3));
                    }else if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                        userVip.setEndTime(DateUtil.getDateAfterYear(DateStampUtils.getTimesteamp(),1));
                    }else{
                        throw new ServiceException("会员类型错误！");
                    }
                    userVip.setVipLevel(vipOrder.getVipLevel());//会员等级 1铂金会员 2至尊会员
                    userVip.setLevelDesc(vipOrder.getVipLevel() == 1?"铂金会员":"至尊会员");
                    userVip.setVipType(vipOrder.getVipType());//会员类型 1一个月 2三个月 3一年会员
                    userVip.setStatus(Status.IS_VIP.getValue());//会员状态 1已开通 2未开通
                    //TODO 生成NO.xxxx :年月日 基数与开通顺序的和，例：180912123457
                    int no = 111111 + userVip.getId();
                    String date = DateUtil.getFormatDate(DateStampUtils.getTimesteamp(),"yyMMdd");
                    userVip.setVipNo(date + no);
                    //开通会员送第一个月优惠券
                    if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                        if(vipOrder.getVipLevel().intValue() == Status.VIP_LEVEL_LOW.getValue()){
                            userCouponService.insertUserCoupon(userVip.getUserId(),1);
                            userCouponService.insertUserCoupon(userVip.getUserId(),2);
                        }else{
                            userCouponService.insertUserCoupon(userVip.getUserId(),1);
                            userCouponService.insertUserCoupon(userVip.getUserId(),2);
                            userCouponService.insertUserCoupon(userVip.getUserId(),3);
                        }
                        UserVipCouponDetail userVipCouponDetail = new UserVipCouponDetail();
                        userVipCouponDetail.setUserId(userVip.getUserId());
                        userVipCouponDetail.setAddTime(DateStampUtils.getTimesteamp());
                        userVipCouponDetail.setVipLevel(userVip.getVipLevel());
                        userVipCouponDetail.setVipStartTime(userVip.getStartTime());
                        userVipCouponDetail.setCount(1);
                        userVipCouponDetailMapper.insert(userVipCouponDetail);
                        schedualMessageService.easemobMessage(vipOrder.getUserId().toString(),
                                "您本月赠送的代金券已到账，点击前往查看~",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_WALLET.getMessage(),"");
                    }
                    //余额账单
                    walletService.addUserBalanceDetail(vipOrder.getUserId(),vipOrder.getAmount(),payType,Status.EXPEND.getValue(),vipOrder.getOrderNo(),
                            "开通"+time.toString()+userVip.getLevelDesc(),Status.ADD_VIP.getValue(),null,vipOrder.getUserId(),thirdOrderNo);
                    //系统消息：恭喜您，您开通的1个月/3个月/1年 铂金/至尊 会员已生效，即刻起享受会员专属特权！
                    schedualMessageService.easemobMessage(vipOrder.getUserId().toString(),
                            "恭喜您，您开通的"+time.toString()+userVip.getLevelDesc()+"已生效，即刻起享受会员专属特权！",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
                }
            }else{
                //续费
                if(userVip.getStatus() == Status.NOT_VIP.getValue()){//未开通
                    throw new ServiceException("请开通会员！");
                }
                if(userVip.getVipLevel().intValue() == vipOrder.getVipLevel()){//续费相同等级会员
                    //计算结束时间
                    if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_ONE_MONTH.getValue()){
                            userVip.setEndTime(DateUtil.getDateAfterMonth(userVip.getEndTime(),1));
                    }else if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_THREE_MONTH.getValue()){
                            userVip.setEndTime(DateUtil.getDateAfterMonth(userVip.getEndTime(),3));
                    }else if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                            userVip.setEndTime(DateUtil.getDateAfterYear(userVip.getEndTime(),1));
                    }else{
                            throw new ServiceException("会员类型错误！");
                    }
                    userVip.setStatus(Status.IS_VIP.getValue());//会员状态 1已开通 2未开通
                }else{
                    //覆盖原会员信息
                    //开始时间：当前时间
                    userVip.setStartTime(DateStampUtils.getTimesteamp());
                    //计算结束时间
                    if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_ONE_MONTH.getValue()){
                        userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),1));
                    }else if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_THREE_MONTH.getValue()){
                        userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),3));
                    }else if(vipOrder.getVipType().intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                        userVip.setEndTime(DateUtil.getDateAfterYear(DateStampUtils.getTimesteamp(),1));
                    }else{
                        throw new ServiceException("会员类型错误！");
                    }
                    userVip.setVipLevel(vipOrder.getVipLevel());//会员等级 1铂金会员 2至尊会员
                    userVip.setLevelDesc(vipOrder.getVipLevel().intValue() == Status.VIP_LEVEL_LOW.getValue()?"铂金会员":"至尊会员");
                    userVip.setVipType(vipOrder.getVipType());//会员类型 1一个月 2三个月 3一年会员
                    userVip.setStatus(Status.IS_VIP.getValue());//会员状态 1已开通 2未开通
                }

                //余额账单
                walletService.addUserBalanceDetail(vipOrder.getUserId(),vipOrder.getAmount(),payType,Status.EXPEND.getValue(),vipOrder.getOrderNo(),
                        "开通"+time.toString()+userVip.getLevelDesc(),Status.RENEW_VIP.getValue(),null,vipOrder.getUserId(),thirdOrderNo);
            }
            userVip.setIsSendMessage(null);
            userVipMapper.updateByPrimaryKey(userVip);
            vipOrder.setStatus(Status.ORDER_COMPLETE.getValue());
            vipOrderMapper.updateByPrimaryKey(vipOrder);
            return true;
        }
    }

    @Override
    public Object addVipOrder(Integer userId, Integer vipLevel, Integer vipType,Integer type,Integer payType,String payPwd) throws ServiceException {
        try{
            VipOrder vipOrder = new VipOrder();
            vipOrder.setUserId(userId);
            //订单号
            final IdGenerator idg = IdGenerator.INSTANCE;
            String id = idg.nextId();
            vipOrder.setOrderNo(id);
            /*
            vipLevel 会员等级 1铂金会员 2至尊会员
            vipType 会员类型 1一个月 2三个月 3一年会员
            铂金会员：
                35元/月、90元/3个月、300元/年
            至尊会员：
                88元/月、240元/3个月、900元/年
             */
            if(vipLevel.intValue() == Status.VIP_LEVEL_LOW.getValue()){
                if(vipType.intValue() == Status.VIP_TYPE_ONE_MONTH.getValue()){
                    vipOrder.setAmount(new BigDecimal(35));
                }else if(vipType.intValue() == Status.VIP_TYPE_THREE_MONTH.getValue()){
                    vipOrder.setAmount(new BigDecimal(90));
                }else if(vipType.intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                    vipOrder.setAmount(new BigDecimal(300));
                }else{
                    throw new ServiceException("会员类型错误！");
                }
            }else if(vipLevel.intValue() == Status.VIP_LEVEL_HIGH.getValue()){
                if(vipType == Status.VIP_TYPE_ONE_MONTH.getValue()){
                    vipOrder.setAmount(new BigDecimal(88));
                }else if(vipType == Status.VIP_TYPE_THREE_MONTH.getValue()){
                    vipOrder.setAmount(new BigDecimal(240));
                }else if(vipType == Status.VIP_TYPE_ONE_YEAR.getValue()){
                    vipOrder.setAmount(new BigDecimal(900));
                }else{
                    throw new ServiceException("会员类型错误！");
                }
            }else{
                throw new ServiceException("会员等级错误！");
            }
            vipOrder.setStatus(Status.ORDER_UNPAID.getValue());
            vipOrder.setAddTime(DateStampUtils.getTimesteamp());
            vipOrder.setVipLevel(vipLevel);
            vipOrder.setLevelDesc(vipLevel.intValue()  == Status.VIP_LEVEL_LOW.getValue().intValue()?"铂金会员":"至尊会员");
            vipOrder.setType(type);
            vipOrder.setVipType(vipType);
            vipOrderMapper.insert(vipOrder);

            StringBuffer payInfo = new StringBuffer();
            //支付
            if(payType.intValue() == Status.PAY_TYPE_WECHAT.getValue()){
                WechatPayDto wechatPayDto = walletService.orderPayByWechat(vipOrder.getOrderNo(), vipOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.vip_wechat_notify.getNotifUrl());
                return wechatPayDto;
            }else if(payType.intValue() == Status.PAY_TYPE_ALIPAY.getValue()){
                String info = walletService.orderPayByALi(vipOrder.getOrderNo(), vipOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.vip_alipay_notify.getNotifUrl());
                payInfo.append(info);
            }else if(payType.intValue() == Status.PAY_TYPE_BALANCE.getValue()) {
                Boolean verifyPayPwd = JSONObject.parseObject(schedualUserService.verifyPayPwd(userId, payPwd)).getBoolean("data");
                if(!verifyPayPwd){
                    throw new ServiceException(ReCode.PAYMENT_PASSWORD_ERROR.getValue(),ReCode.PAYMENT_PASSWORD_ERROR.getMessage());
                }
                //余额支付
                walletService.updateBalance(userId,vipOrder.getAmount(),Status.SUB.getValue());
                //修改用户会员信息
                updateOrder(vipOrder.getOrderNo(),null,Status.PAY_TYPE_BALANCE.getValue());

                payInfo.append("余额支付成功！");
            }else if(payType.intValue() == Status.PAY_TYPE_MINI.getValue()){
                WechatPayDto wechatPayDto = walletService.orderPayByWechatMini(userId, vipOrder.getOrderNo(), vipOrder.getAmount(), NotifyUrl.mini_test_notify.getNotifUrl() + NotifyUrl.vip_wechat_notify.getNotifUrl());
                return wechatPayDto;
            }else{
                throw new ServiceException("支付方式错误！");
            }
            return payInfo.toString();
        }catch (ServiceException e){
            throw new ServiceException(e.getMessage());
        }catch (Exception e){
            throw new ServiceException("会员开通/续费下单失败！");
        }
    }

    @Override
    public void updateUserVip(Integer userId, Integer vipLevel, Integer vipType,Integer type) throws ServiceException {
        UserVip userVip = userVipMapper.selectByUserId(userId);
        if(type.intValue() == 1){
            if(userVip.getStatus().intValue() == Status.IS_VIP.getValue()){//已开通
                throw new ServiceException("已开通会员！");
            }
            //计算结束时间
            if(vipType.intValue() == Status.VIP_TYPE_ONE_MONTH.getValue()){
                userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),1));
            }else if(vipType.intValue() == Status.VIP_TYPE_THREE_MONTH.getValue()){
                userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),3));
            }else if(vipType.intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                userVip.setEndTime(DateUtil.getDateAfterYear(DateStampUtils.getTimesteamp(),1));
            }else{
                throw new ServiceException("会员类型错误！");
            }
            userVip.setVipLevel(vipLevel);//会员等级 1铂金会员 2至尊会员
            userVip.setLevelDesc(vipLevel == 1?"铂金会员":"至尊会员");
            userVip.setVipType(vipType);//会员类型 1一个月 2三个月 3一年会员
            userVip.setStatus(Status.IS_VIP.getValue());//会员状态 1已开通 2未开通
            //TODO 生成NO.xxxx :年月日 基数与开通顺序的和，例：180912123457
            int no = 111111 + userVip.getId();
            String date = DateUtil.getFormatDate(DateStampUtils.getTimesteamp(),"yyMMdd");
            userVip.setVipNo(date + no);
            //开通会员送第一个月优惠券
            if(vipType.intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                if(vipLevel.intValue() == Status.VIP_LEVEL_LOW.getValue()){
                    userCouponService.insertUserCoupon(userVip.getUserId(),1);
                    userCouponService.insertUserCoupon(userVip.getUserId(),2);
                }else{
                    userCouponService.insertUserCoupon(userVip.getUserId(),1);
                    userCouponService.insertUserCoupon(userVip.getUserId(),2);
                    userCouponService.insertUserCoupon(userVip.getUserId(),3);
                }
                UserVipCouponDetail userVipCouponDetail = new UserVipCouponDetail();
                userVipCouponDetail.setUserId(userVip.getUserId());
                userVipCouponDetail.setAddTime(DateStampUtils.getTimesteamp());
                userVipCouponDetail.setVipLevel(userVip.getVipLevel());
                userVipCouponDetail.setVipStartTime(userVip.getStartTime());
                userVipCouponDetail.setCount(1);
                userVipCouponDetailMapper.insert(userVipCouponDetail);
                schedualMessageService.easemobMessage(userId.toString(),
                        "您本月赠送的代金券已到账，点击前往查看~",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_WALLET.getMessage(),"");
            }
        }else{
            if(userVip.getStatus() == Status.NOT_VIP.getValue()){//未开通
                throw new ServiceException("请开通会员！");
            }
            if(userVip.getVipLevel().intValue() == vipLevel){//续费相同等级会员
                //计算结束时间
                if(vipType.intValue() == Status.VIP_TYPE_ONE_MONTH.getValue()){
                    userVip.setEndTime(DateUtil.getDateAfterMonth(userVip.getEndTime(),1));
                }else if(vipType.intValue() == Status.VIP_TYPE_THREE_MONTH.getValue()){
                    userVip.setEndTime(DateUtil.getDateAfterMonth(userVip.getEndTime(),3));
                }else if(vipType.intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                    userVip.setEndTime(DateUtil.getDateAfterYear(userVip.getEndTime(),1));
                }else{
                    throw new ServiceException("会员类型错误！");
                }
                userVip.setStatus(Status.IS_VIP.getValue());//会员状态 1已开通 2未开通
            }else{
                //覆盖原会员信息
                //开始时间：当前时间
                userVip.setStartTime(DateStampUtils.getTimesteamp());
                //计算结束时间
                if(vipType.intValue() == Status.VIP_TYPE_ONE_MONTH.getValue()){
                    userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),1));
                }else if(vipType.intValue() == Status.VIP_TYPE_THREE_MONTH.getValue()){
                    userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),3));
                }else if(vipType.intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                    userVip.setEndTime(DateUtil.getDateAfterYear(DateStampUtils.getTimesteamp(),1));
                }else{
                    throw new ServiceException("会员类型错误！");
                }
                userVip.setVipLevel(vipLevel);//会员等级 1铂金会员 2至尊会员
                userVip.setLevelDesc(vipLevel.intValue() == Status.VIP_LEVEL_LOW.getValue()?"铂金会员":"至尊会员");
                userVip.setVipType(vipType);//会员类型 1一个月 2三个月 3一年会员
                userVip.setStatus(Status.IS_VIP.getValue());//会员状态 1已开通 2未开通
            }
        }
        userVip.setIsSendMessage(null);
        userVipMapper.updateByPrimaryKey(userVip);
    }

    @Override
    public boolean isUserVip(Integer userId) throws ServiceException {
        UserVip userVip = userVipMapper.selectByUserId(userId);
        return userVip.getStatus().intValue() == Status.IS_VIP.getValue()?true:false;
    }
}
