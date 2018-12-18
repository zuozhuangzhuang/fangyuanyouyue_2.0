package com.fangyuanyouyue.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.MiniPage;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
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
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

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
    @Autowired
    private InviteCodeMapper inviteCodeMapper;
    @Autowired
    private FileUploadService fileUploadService;

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
    public String getQRCode(Integer userId,Integer id, Integer type) throws ServiceException {
        String url = null;
        String scene = ""+id;
        for(MiniPage miniPage : MiniPage.values()){
            if(type.equals(miniPage.getType())){
                url = miniPage.getUrl();
            }
        }
        if(userId != null && type.equals(MiniPage.SHOP_DETAIL.getType())){
            InviteCode inviteCode = inviteCodeMapper.selectUserCodeByUserId(userId);
            if(inviteCode != null){
                scene += "#"+inviteCode.getUserCode();
            }
        }
        //TODO 从redis中取出access_token，目前未将其存入redis (#^.^#)
        AccessToken accessTokenObj = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
        String miniQr = WXQRCode.getMiniQrBase64(scene, accessTokenObj.getToken(), url);
        return miniQr;
    }

    @Override
    public String getQRCodeUrl(Integer userId,Integer id, Integer type) throws ServiceException {
        String url = null;
        String scene = ""+id;
        for(MiniPage miniPage : MiniPage.values()){
            if(type.equals(miniPage.getType())){
                url = miniPage.getUrl();
            }
        }
        if(userId != null && type.equals(MiniPage.SHOP_DETAIL.getType())){
            InviteCode inviteCode = inviteCodeMapper.selectUserCodeByUserId(userId);
            if(inviteCode != null){
                scene += "#"+inviteCode.getUserCode();
            }
        }
        //TODO 从redis中取出access_token，目前未将其存入redis (#^.^#)
        AccessToken accessTokenObj = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
        InputStream miniQrInput = WXQRCode.getMiniQrInput(scene, accessTokenObj.getToken(), url);

        final IdGenerator idg = IdGenerator.INSTANCE;
        String fileName = "pic" +DateUtil.getCurrentDate("/yyyy/MM/dd/")+UUID.randomUUID()+".png";
        String imgUrl = fileUploadService.uploadFileByInputStream(miniQrInput,fileName);
        return imgUrl;
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
