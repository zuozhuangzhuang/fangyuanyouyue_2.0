package com.fangyuanyouyue.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.constant.StatusEnum;
import com.fangyuanyouyue.user.dao.DailyStatisticsMapper;
import com.fangyuanyouyue.user.dao.ProcessMapper;
import com.fangyuanyouyue.user.dao.UserAuthApplyMapper;
import com.fangyuanyouyue.user.dao.UserInfoExtMapper;
import com.fangyuanyouyue.user.dto.admin.AdminProcessDto;
import com.fangyuanyouyue.user.model.DailyStatistics;
import com.fangyuanyouyue.user.model.UserAuthApply;
import com.fangyuanyouyue.user.model.UserInfoExt;
import com.fangyuanyouyue.user.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service(value = "timerService")
public class TimerServiceImpl implements TimerService{
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private UserAuthApplyMapper userAuthApplyMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private DailyStatisticsMapper dailyStatisticsMapper;
    @Autowired
    private SchedualOrderService schedualOrderService;
    @Autowired
    private SchedualGoodsService schedualGoodsService;
    @Autowired
    private SchedualForumService schedualForumService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ProcessMapper processMapper;

    @Override
    public void shopAuthTimeOut() throws ServiceException {
        //1、查询endTime <= 当前时间 的userAauthApply 2、设置为status=3（已拒绝，拒绝理由：官方认证到期） 3、修改userInfoExt、userAuthApply表 4、发消息
        List<UserAuthApply> applyList = userAuthApplyMapper.selectByEndTime(DateUtil.getFormatDate(new Date(), DateUtil.DATE_FORMT), StatusEnum.AUTH_TYPE_ACCEPT.getCode());
        if(applyList != null && applyList.size() > 0){
            for(UserAuthApply apply:applyList){
                apply.setStatus(StatusEnum.AUTH_TYPE_REJECT.getCode());
                apply.setReason("官方认证到期");
                UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(apply.getUserId());
                userInfoExt.setAuthType(StatusEnum.AUTH_TYPE_REJECT.getCode());
                userAuthApplyMapper.updateByPrimaryKeySelective(apply);
                userInfoExtMapper.updateByPrimaryKeySelective(userInfoExt);
                //发送消息
                schedualMessageService.easemobMessage(apply.getUserId().toString(),"您的认证店铺已到期！",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
            }
        }
    }
    @Override
    public void dailyStatistics() throws ServiceException {

        //昨日注册用户总数
        Integer yesterdayUser=systemService.processYesterdayUser();
        //昨日商品总数
        Integer yesterdayGoods=processMapper.getYestoryGoods();
        //抢购
        Integer yestoryAuction = processMapper.getYestoryAuction();
        //官方鉴定
        Integer yestoryPlatfomAppraisal = processMapper.getYestoryPlatfomAppraisal();
        //昨日订单总数
        Integer yesterdayOrder= processMapper.getYestoryOrder();
        //昨日帖子总数
        Integer yesterdayForum=processMapper.getYestoryForum();
        //视频
        Integer yestoryVideo = processMapper.getYestoryVideo();
        //全民鉴定
        Integer yestoryAppraisal = processMapper.getYestoryAppraisal();
        //会员
        Integer yestoryVip = processMapper.getYestoryVip();
        //平台收入
        BigDecimal yestoryIncome = processMapper.getYestoryIncome();
        //平台支出
        BigDecimal yestoryExpend = processMapper.getYestoryExpend();

        DailyStatistics dailyStatistics = new DailyStatistics();
        dailyStatistics.setDate(DateUtil.getDateAfterDay(new Date(),-1));
        dailyStatistics.setUserCount(yesterdayUser == null?0:yesterdayUser);
        dailyStatistics.setGoodsCount(yesterdayGoods == null?0:yesterdayGoods);
        dailyStatistics.setAuctionCount(yestoryAuction == null?0:yestoryAuction);
        dailyStatistics.setPlatformAppraisalCount(yestoryPlatfomAppraisal == null?0:yestoryPlatfomAppraisal);
        dailyStatistics.setOrderCount(yesterdayOrder == null?0:yesterdayOrder);
        dailyStatistics.setForumCount(yesterdayForum == null?0:yesterdayForum);
        dailyStatistics.setVideoCount(yestoryVideo == null?0:yestoryVideo);
        dailyStatistics.setAppraisalCount(yestoryAppraisal == null?0:yestoryAppraisal);
        dailyStatistics.setVipCount(yestoryVip == null?0:yestoryVip);
        dailyStatistics.setIncomeCount(yestoryIncome == null?new BigDecimal(0):yestoryIncome);
        dailyStatistics.setExpendCount(yestoryExpend == null?new BigDecimal(0):yestoryExpend);
        dailyStatistics.setAddTime(DateStampUtils.getTimesteamp());
        dailyStatisticsMapper.insert(dailyStatistics);
    }

}
