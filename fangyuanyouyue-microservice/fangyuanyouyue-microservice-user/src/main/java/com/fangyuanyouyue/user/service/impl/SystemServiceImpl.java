package com.fangyuanyouyue.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.MiniPage;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.WXQRCode;
import com.fangyuanyouyue.base.util.wechat.pay.WechatPayConfig;
import com.fangyuanyouyue.base.util.wechat.pojo.AccessToken;
import com.fangyuanyouyue.base.util.wechat.utils.WeixinUtil;
import com.fangyuanyouyue.user.dao.*;
import com.fangyuanyouyue.user.dto.admin.AdminDailyStatisticsDto;
import com.fangyuanyouyue.user.dto.admin.AdminFeedbackDto;
import com.fangyuanyouyue.user.dto.admin.AdminProcessDto;
import com.fangyuanyouyue.user.dto.admin.AdminSysMsgLogDto;
import com.fangyuanyouyue.user.model.*;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service(value = "systemService")
@Transactional(rollbackFor=Exception.class)
public class SystemServiceImpl implements SystemService {
    @Autowired
    private FeedbackMapper feedbackMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private SchedualOrderService schedualOrderService;
    @Autowired
    private SchedualGoodsService schedualGoodsService;
    @Autowired
    private SchedualForumService schedualForumService;
    @Autowired
    private SysMsgLogMapper sysMsgLogMapper;
    @Autowired
    private DailyStatisticsMapper dailyStatisticsMapper;
    @Autowired
    private SysPropertyMapper sysPropertyMapper;

    @Override
    public void feedback(Integer userId, String content, Integer type, String version) throws ServiceException {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setContent(content);
        feedback.setType(type);
        feedback.setVersion(version);
        feedback.setAddTime(DateStampUtils.getTimesteamp());
        feedbackMapper.insert(feedback);
    }

    @Override
    public Pager feedbackList(AdminUserParam param) throws ServiceException {

        Integer total = feedbackMapper.countPage(param.getKeyword(),param.getStartDate(),param.getEndDate());

        List<Feedback> feedbacks = feedbackMapper.getPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        List<AdminFeedbackDto> datas = AdminFeedbackDto.toDtoList(feedbacks);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);
        return pager;
    }

    @Override
    public void sendMessage(Integer userId,String content) throws ServiceException {
        SysMsgLog sysMsgLog = new SysMsgLog();
        sysMsgLog.setContent(content);
        sysMsgLog.setAddTime(DateStampUtils.getTimesteamp());
        sysMsgLog.setUserId(userId);
        sysMsgLogMapper.insert(sysMsgLog);
        List<UserInfo> allHxUser = userInfoMapper.findAllHxUser();
        for(UserInfo userInfo:allHxUser){
            schedualMessageService.easemobMessage(userInfo.getId().toString(),content, Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
        }
    }

    @Override
    public AdminProcessDto getProcess() throws ServiceException {

        //今日订单总数
        Integer todayOrder= JSONObject.parseObject(schedualOrderService.processTodayOrder()).getIntValue("data");
        //订单总数
        Integer allOrder=JSONObject.parseObject(schedualOrderService.processAllOrder()).getIntValue("data");
        //今日注册用户总数
        Integer todayUser=processTodayUser();
        //注册用户总数
        Integer allUser=processAllUser();
        //今日商品总数
        Integer todayGoods=JSONObject.parseObject(schedualGoodsService.processTodayGoods()).getIntValue("data");
        //商品总数
        Integer allGoods=JSONObject.parseObject(schedualGoodsService.processAllGoods()).getIntValue("data");
        //今日帖子总数
        Integer todayForum=JSONObject.parseObject(schedualForumService.processTodayForum()).getIntValue("data");
        //帖子总数
        Integer allForum=JSONObject.parseObject(schedualForumService.processAllForum()).getIntValue("data");

        AdminProcessDto dto = new AdminProcessDto();
        dto.setTodayOrder(todayOrder);
        dto.setAllOrder(allOrder);
        dto.setTodayUser(todayUser);
        dto.setAllUser(allUser);
        dto.setTodayGoods(todayGoods);
        dto.setAllGoods(allGoods);
        dto.setTodayForum(todayForum);
        dto.setAllForum(allForum);
        return dto;
    }



    @Override
    public Integer processTodayUser() throws ServiceException {
        Integer todayUserCount = userInfoMapper.getTodayUserCount();
        return todayUserCount;
    }

    @Override
    public Integer processAllUser() throws ServiceException {
        Integer allUserCount = userInfoMapper.getAllUserCount();
        return allUserCount;
    }

    @Override
    public Integer processYesterdayUser() throws ServiceException {
        Integer yesterdayUser = userInfoMapper.processYesterdayUser();
        return yesterdayUser;
    }


    @Override
    public Pager sysMsgList(AdminUserParam param) throws ServiceException {

        Integer total = sysMsgLogMapper.countPage(param.getKeyword(),param.getStartDate(),param.getEndDate(),param.getType());

        List<SysMsgLog> sysMsgLogs = sysMsgLogMapper.getPage(param.getStart()*param.getLimit(),param.getLimit(),param.getKeyword(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType(),param.getType());
        List<AdminSysMsgLogDto> datas = AdminSysMsgLogDto.toDtoList(sysMsgLogs);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);
        return pager;
    }

    @Override
    public List<AdminDailyStatisticsDto> getProcessList(Integer count) throws ServiceException {
        List<DailyStatistics> dailyStatistics = dailyStatisticsMapper.selectByDayCount(count);
        List<AdminDailyStatisticsDto> dtos = AdminDailyStatisticsDto.toDtoList(dailyStatistics);
        Collections.reverse(dtos);
        return dtos;
    }

    @Override
    public String getQRCode(Integer id, Integer type) throws ServiceException {
        String url = null;
        for(MiniPage miniPage : MiniPage.values()){
            if(type.equals(miniPage.getType())){
                url = miniPage.getUrl();
            }
        }
        //TODO 从redis中取出access_token，目前未将其存入redis (#^.^#)
        AccessToken accessTokenObj = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
        String miniQr = WXQRCode.getMiniQr(id.toString(), accessTokenObj.getToken(), url);
        return "data:image/jpeg;base64,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";
    }

    @Override
    public void updateQRSwitch(Integer status) throws ServiceException {
        if(status.equals(Status.ISQRCODE.getValue())){
            //开
            SysProperty ruleByKey = sysPropertyMapper.getRuleByKey(Status.QR_RULE_OFF.getMessage());
            if(ruleByKey == null){
                throw new ServiceException("没有找到此配置！");
            }
            ruleByKey.setKeyWord(Status.QR_RULE.getMessage());
            sysPropertyMapper.updateByPrimaryKey(ruleByKey);
        }else if(status.equals(Status.NOTQRCODE.getValue())){
            //关
            SysProperty ruleByKey = sysPropertyMapper.getRuleByKey(Status.QR_RULE.getMessage());
            if(ruleByKey == null){
                throw new ServiceException("没有找到此配置！");
            }
            ruleByKey.setKeyWord(Status.QR_RULE_OFF.getMessage());
            sysPropertyMapper.updateByPrimaryKey(ruleByKey);
        }else{
            throw new ServiceException("开关状态错误！");
        }
    }

    @Override
    public void updateInviteSwitch(Integer status) throws ServiceException {
        if(status.equals(Status.ISINVITE.getValue())){
            //开
            SysProperty ruleByKey = sysPropertyMapper.getRuleByKey(Status.INVITE_RULE_OFF.getMessage());
            if(ruleByKey == null){
                throw new ServiceException("没有找到此配置！");
            }
            ruleByKey.setKeyWord(Status.INVITE_RULE.getMessage());
            sysPropertyMapper.updateByPrimaryKey(ruleByKey);
        }else if(status.equals(Status.NOTINVITE.getValue())){
            //关
            SysProperty ruleByKey = sysPropertyMapper.getRuleByKey(Status.INVITE_RULE.getMessage());
            if(ruleByKey == null){
                throw new ServiceException("没有找到此配置！");
            }
            ruleByKey.setKeyWord(Status.INVITE_RULE_OFF.getMessage());
            sysPropertyMapper.updateByPrimaryKey(ruleByKey);
        }else{
            throw new ServiceException("开关状态错误！");
        }
    }
}
