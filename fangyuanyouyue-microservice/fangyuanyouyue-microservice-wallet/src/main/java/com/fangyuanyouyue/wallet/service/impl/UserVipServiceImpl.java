package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.dao.UserVipMapper;
import com.fangyuanyouyue.wallet.model.UserVip;
import com.fangyuanyouyue.wallet.service.SchedualMessageService;
import com.fangyuanyouyue.wallet.service.UserVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service(value = "userVipService")
public class UserVipServiceImpl implements UserVipService{

    @Autowired
    private UserVipMapper userVipMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;

    @Override
    public void updateMebber(Integer userId, Integer vipLevel, Integer vipType,Integer type) throws ServiceException {
        UserVip userVip = userVipMapper.selectByUserId(userId);
        if(userVip == null){
            throw new ServiceException("获取用户会员信息失败！");
        }else{
            if(type == 1){//开通
                if(userVip.getStatus() == 1){//已开通
                    throw new ServiceException("已开通会员！");
                }else{
                    //开始时间：当前时间
                    userVip.setStartTime(DateStampUtils.getTimesteamp());
                    //计算结束时间
                    switch (vipType){
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
                    userVip.setVipLevel(vipLevel);//会员等级 1铂金会员 2至尊会员
                    userVip.setLevelDesc(vipLevel == 1?"铂金会员":"至尊会员");
                    userVip.setVipType(vipType);//会员类型 1一个月 2三个月 3一年会员
                    StringBuffer time = new StringBuffer();
                    time.append(vipType == 1?"一个月":(vipType == 2?"三个月":"一年"));
                    userVip.setStatus(1);//会员状态 1已开通 2未开通
                    //TODO 生成NO.xxxx :年月日 基数与开通顺序的和，例：180912123457
                    int no = 111111 + userVip.getId();
                    String date = DateUtil.getFormatDate(DateStampUtils.getTimesteamp(),"yyMMdd");
                    userVip.setVipNo(date + no);
                    //系统消息：恭喜您，您开通的1个月/3个月/1年 铂金/至尊 会员已生效，即刻起享受会员专属特权！
                    schedualMessageService.easemobMessage(userId.toString(),
                            "恭喜您，您开通的"+time.toString()+userVip.getLevelDesc()+"已生效，即刻起享受会员专属特权！","1","1","");
                }
            }else{//续费
                if(userVip.getVipLevel().intValue() == vipLevel){//续费相同等级会员
                    //计算结束时间
                    switch (vipType){
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
                    switch (vipType){
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
                    userVip.setVipLevel(vipLevel);//会员等级 1铂金会员 2至尊会员
                    userVip.setLevelDesc(vipLevel == 1?"铂金会员":"至尊会员");
                    userVip.setVipType(vipType);//会员类型 1一个月 2三个月 3一年会员
                    userVip.setStatus(1);//会员状态 1已开通 2未开通
                }
            }
            userVipMapper.updateByPrimaryKeySelective(userVip);
        }
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getFormatDate(DateStampUtils.getTimesteamp()));
    }
}
