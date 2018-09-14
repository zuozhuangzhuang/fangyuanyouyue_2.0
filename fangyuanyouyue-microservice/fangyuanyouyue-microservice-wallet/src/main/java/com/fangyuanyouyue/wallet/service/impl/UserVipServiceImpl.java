package com.fangyuanyouyue.wallet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.wallet.dao.UserVipMapper;
import com.fangyuanyouyue.wallet.dao.VipOrderMapper;
import com.fangyuanyouyue.wallet.model.UserVip;
import com.fangyuanyouyue.wallet.model.VipOrder;
import com.fangyuanyouyue.wallet.service.SchedualMessageService;
import com.fangyuanyouyue.wallet.service.SchedualUserService;
import com.fangyuanyouyue.wallet.service.UserVipService;
import com.fangyuanyouyue.wallet.service.WalletService;
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
            if(vipOrder.getType() == 1){
                //开通
                if(userVip.getStatus() == 1){//已开通
                    throw new ServiceException("已开通会员！");
                }else{
                    //开始时间：当前时间
                    userVip.setStartTime(DateStampUtils.getTimesteamp());
                    //计算结束时间
                    switch (vipOrder.getVipType()){
                        case 1://1 一月会员
                            userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),1));
                            break;
                        case 2://2 三月会员
                            userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),3));
                            break;
                        case 3://3 一年会员
                            //TODO 送优惠券
                            userVip.setEndTime(DateUtil.getDateAfterYear(DateStampUtils.getTimesteamp(),1));
                            break;
                    }
                    userVip.setVipLevel(vipOrder.getVipLevel());//会员等级 1铂金会员 2至尊会员
                    userVip.setLevelDesc(vipOrder.getVipLevel() == 1?"铂金会员":"至尊会员");
                    userVip.setVipType(vipOrder.getVipType());//会员类型 1一个月 2三个月 3一年会员
                    StringBuffer time = new StringBuffer();
                    time.append(vipOrder.getVipType() == 1?"一个月":(vipOrder.getVipType() == 2?"三个月":"一年"));
                    userVip.setStatus(1);//会员状态 1已开通 2未开通
                    //TODO 生成NO.xxxx :年月日 基数与开通顺序的和，例：180912123457
                    int no = 111111 + userVip.getId();
                    String date = DateUtil.getFormatDate(DateStampUtils.getTimesteamp(),"yyMMdd");
                    userVip.setVipNo(date + no);
                    //系统消息：恭喜您，您开通的1个月/3个月/1年 铂金/至尊 会员已生效，即刻起享受会员专属特权！
                    schedualMessageService.easemobMessage(vipOrder.getUserId().toString(),
                            "恭喜您，您开通的"+time.toString()+userVip.getLevelDesc()+"已生效，即刻起享受会员专属特权！","1","1","");
                }
            }else{//续费
                if(userVip.getStatus() == 2){//已开通
                    throw new ServiceException("请开通会员！");
                }
                if(userVip.getVipLevel().intValue() == vipOrder.getVipLevel()){//续费相同等级会员
                    //计算结束时间
                    switch (vipOrder.getVipType()){
                        case 1://1 一月会员
                            userVip.setEndTime(DateUtil.getDateAfterMonth(userVip.getEndTime(),1));
                            break;
                        case 2://2 三月会员
                            userVip.setEndTime(DateUtil.getDateAfterMonth(userVip.getEndTime(),3));
                            break;
                        case 3://3 一年会员
                            //TODO 送优惠券
                            userVip.setEndTime(DateUtil.getDateAfterYear(userVip.getEndTime(),1));
                            break;
                        default:
                            throw new ServiceException("会员类型错误！");
                    }
                    userVip.setStatus(1);//会员状态 1已开通 2未开通
                }else{
                    //覆盖原会员信息
                    //开始时间：当前时间
                    userVip.setStartTime(DateStampUtils.getTimesteamp());
                    //计算结束时间
                    switch (vipOrder.getVipType()){
                        case 1://1 一月会员
                            userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),1));
                            break;
                        case 2://2 三月会员
                            userVip.setEndTime(DateUtil.getDateAfterMonth(DateStampUtils.getTimesteamp(),3));
                            break;
                        case 3://3 一年会员
                            //TODO 送优惠券
                            userVip.setEndTime(DateUtil.getDateAfterYear(DateStampUtils.getTimesteamp(),1));
                            break;
                    }
                    userVip.setVipLevel(vipOrder.getVipLevel());//会员等级 1铂金会员 2至尊会员
                    userVip.setLevelDesc(vipOrder.getVipLevel() == 1?"铂金会员":"至尊会员");
                    userVip.setVipType(vipOrder.getVipType());//会员类型 1一个月 2三个月 3一年会员
                    userVip.setStatus(1);//会员状态 1已开通 2未开通
                }
            }
            userVip.setIsSendMessage(null);
            userVipMapper.updateByPrimaryKey(userVip);
            vipOrder.setStatus(2);
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
            if(vipLevel == 1){
                if(vipType == 1){
                    vipOrder.setAmount(new BigDecimal(35));
                }else if(vipType == 2){
                    vipOrder.setAmount(new BigDecimal(90));
                }else if(vipType == 3){
                    vipOrder.setAmount(new BigDecimal(300));
                }else{
                    throw new ServiceException("会员类型错误！");
                }
            }else if(vipLevel == 2){
                if(vipType == 1){
                    vipOrder.setAmount(new BigDecimal(88));
                }else if(vipType == 2){
                    vipOrder.setAmount(new BigDecimal(240));
                }else if(vipType == 3){
                    vipOrder.setAmount(new BigDecimal(900));
                }else{
                    throw new ServiceException("会员类型错误！");
                }
            }else{
                throw new ServiceException("会员等级错误！");
            }
            vipOrder.setStatus(1);//状态 1待支付 2已完成 3已删除
            vipOrder.setAddTime(DateStampUtils.getTimesteamp());
            vipOrder.setVipLevel(vipLevel);
            vipOrder.setLevelDesc(vipLevel  == 1?"铂金会员":"至尊会员");
            vipOrder.setType(type);
            vipOrder.setVipType(vipType);
            vipOrderMapper.insert(vipOrder);

            StringBuffer payInfo = new StringBuffer();
            //支付
            if(payType.intValue() == 1){//微信,如果回调失败就不做处理，成功就在回调接口中继续生成全民鉴定
                WechatPayDto wechatPayDto = walletService.orderPayByWechat(vipOrder.getOrderNo(), vipOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.vip_wechat_notify.getNotifUrl());
                return wechatPayDto;
            }else if(payType.intValue() == 2){//支付宝,如果回调失败就不做处理，成功就在回调接口中继续生成全民鉴定
                String info = walletService.orderPayByALi(vipOrder.getOrderNo(), vipOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.vip_alipay_notify.getNotifUrl());
                payInfo.append(info);
            }else if(payType.intValue() == 3) {//余额
                //验证支付密码
                Boolean verifyPayPwd = JSONObject.parseObject(schedualUserService.verifyPayPwd(userId, payPwd)).getBoolean("data");
                if(!verifyPayPwd){
                    throw new ServiceException("支付密码错误！");
                }
                //余额支付
                walletService.updateBalance(userId,vipOrder.getAmount(),2);
                //修改用户会员信息
                updateOrder(vipOrder.getOrderNo(),null,3);

                payInfo.append("余额支付成功！");
            }else if(payType.intValue() == 4){//小程序支付
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

    public static void main(String[] args) {
        System.out.println(DateUtil.getFormatDate(DateStampUtils.getTimesteamp()));
    }
}
