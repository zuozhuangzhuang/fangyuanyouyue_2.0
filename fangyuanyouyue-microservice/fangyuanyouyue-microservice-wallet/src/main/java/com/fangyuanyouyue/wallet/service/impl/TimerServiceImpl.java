package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.constant.StatusEnum;
import com.fangyuanyouyue.wallet.dao.*;
import com.fangyuanyouyue.wallet.model.UserInfo;
import com.fangyuanyouyue.wallet.model.UserVip;
import com.fangyuanyouyue.wallet.model.UserVipCouponDetail;
import com.fangyuanyouyue.wallet.model.UserWallet;
import com.fangyuanyouyue.wallet.service.SchedualMessageService;
import com.fangyuanyouyue.wallet.service.TimerService;
import com.fangyuanyouyue.wallet.service.UserCouponService;
import com.fangyuanyouyue.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service(value = "timerService")
@Transactional(rollbackFor=Exception.class)
public class TimerServiceImpl implements TimerService{
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private BonusPoolMapper bonusPoolMapper;
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserVipMapper userVipMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private ConfinedUserMapper confinedUserMapper;
    @Autowired
    private UserRechargeDetailMapper userRechargeDetailMapper;
    @Autowired
    private UserBalanceDetailMapper userBalanceDetailMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserVipCouponDetailMapper userVipCouponDetailMapper;



    @Override
    public void cancelVip() throws ServiceException {
        //会员记录。后台页面查看
        //1、根据会员到期时间查询会员列表 2、修改会员状态
        //获取7天后过期的会员 现在时间 < 结束时间 < 7天后时间
        List<UserVip> userVips = userVipMapper.selectByEndTime(DateUtil.getDateAfterDay(DateStampUtils.getTimesteamp(),7));
        if(userVips != null && userVips.size() > 0){
            for(UserVip userVip:userVips){
                if(userVip.getEndTime().getTime() < System.currentTimeMillis()){
                    //结束时间 < 当前时间 = 直接注销会员
                    userVip.setStartTime(null);
                    userVip.setEndTime(null);
                    userVip.setVipLevel(null);
                    userVip.setLevelDesc(null);
                    userVip.setVipType(null);
                    userVip.setStatus(Status.NOT_VIP.getValue());
                    userVip.setIsSendMessage(null);
                    userVipMapper.updateByPrimaryKey(userVip);
                    //您的会员已到期！点击此处去重新开通~
                    schedualMessageService.easemobMessage(userVip.getUserId().toString(),
                            "您的会员已到期！点击此处去重新开通~",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_VIP.getMessage(),"");
                }else{
                    if(userVip.getIsSendMessage() == null || userVip.getIsSendMessage() != StatusEnum.YES.getCode()){//只通知一次
                        //您的会员将于**月**日**时到期。请及时续费哦。 点击此处去续费~
                        schedualMessageService.easemobMessage(userVip.getUserId().toString(),
                                "您的会员将于"+DateUtil.getFormatDate(userVip.getEndTime(),DateUtil.DATE_FORMT)+"到期。请及时续费哦。 点击此处去续费~",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_VIP.getMessage(),"");
                        userVip.setIsSendMessage(StatusEnum.YES.getCode());
                        userVipMapper.updateByPrimaryKeySelective(userVip);
                    }
                }
            }
        }
    }

    @Override
    public void updateLevel() throws ServiceException {
        //1、获取所有用户的信息 2、根据用户id获取钱包用户总积分信息 3、根据总积分更新用户等级
        List<UserInfo> all = userInfoMapper.findAll();
        if(all != null && all.size() > 0){
            for(UserInfo info:all){
                UserWallet userWallet = userWalletMapper.selectByUserId(info.getId());
                //积分等级，计算总积分
                Long score = userWallet.getScore();
                WalletServiceImpl.setUserLevel(score, info);
                userInfoMapper.updateByPrimaryKeySelective(info);
            }
        }
    }

    @Override
    public void sendCoupon() throws ServiceException, ParseException {
        //1、获取所有年会员用户 2、判断是否满足条件 3、铂金会员每个月送5元+20元，至尊会员每个月送5元+20元+50元
        List<UserVip> userVips = userVipMapper.selectUserVipYear();
        if(userVips != null && userVips.size() > 0){
            //判断用户是否该发放优惠券
            for(UserVip userVip:userVips){
                //应发优惠券次数
                int monthsBetween = DateUtil.monthsBetween(userVip.getStartTime(), DateStampUtils.getTimesteamp());
                //已发优惠券次数
                int count = userVipCouponDetailMapper.selectDetailByUserId(userVip.getUserId(),userVip.getStartTime());
                //应发 > 0 && 应发 > 已发
                if(monthsBetween > 0 && monthsBetween>count){
                    UserInfo info = userInfoMapper.selectByPrimaryKey(userVip.getUserId());
                    System.out.println("用户“"+info.getNickName()+"”发"+(monthsBetween-count)+"次代金券");
                    for(int i=0;i<(monthsBetween-count);i++){
                        if(userVip.getVipType().intValue() == Status.VIP_TYPE_ONE_YEAR.getValue()){
                            if(userVip.getVipLevel().intValue() == Status.VIP_LEVEL_LOW.getValue()){
                                userCouponService.insertUserCoupon(userVip.getUserId(),1);
                                userCouponService.insertUserCoupon(userVip.getUserId(),2);
                            }else{
                                userCouponService.insertUserCoupon(userVip.getUserId(),1);
                                userCouponService.insertUserCoupon(userVip.getUserId(),2);
                                userCouponService.insertUserCoupon(userVip.getUserId(),3);
                            }
                            //新增领取记录
                            UserVipCouponDetail userVipCouponDetail = new UserVipCouponDetail();
                            userVipCouponDetail.setUserId(userVip.getUserId());
                            userVipCouponDetail.setAddTime(DateStampUtils.getTimesteamp());
                            userVipCouponDetail.setVipLevel(userVip.getVipLevel());
                            userVipCouponDetail.setVipStartTime(userVip.getStartTime());
                            userVipCouponDetail.setCount(count+1);
                            userVipCouponDetailMapper.insert(userVipCouponDetail);
                            schedualMessageService.easemobMessage(userVip.getUserId().toString(),
                                    "您本月赠送的代金券已到账，点击前往查看~",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_WALLET.getMessage(),"");
                        }
                    }
                }
            }
        }
    }
}
