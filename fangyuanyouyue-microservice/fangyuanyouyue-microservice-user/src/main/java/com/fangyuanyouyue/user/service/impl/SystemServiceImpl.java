package com.fangyuanyouyue.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.FeedbackMapper;
import com.fangyuanyouyue.user.dao.UserInfoMapper;
import com.fangyuanyouyue.user.dto.admin.AdminFeedbackDto;
import com.fangyuanyouyue.user.dto.admin.AdminProcessDto;
import com.fangyuanyouyue.user.model.Feedback;
import com.fangyuanyouyue.user.model.UserAuthApply;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.*;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Integer total = feedbackMapper.countPage(param.getKeyword(),param.getStartDate(),param.getEndDate(),param.getType());

        List<Feedback> feedbacks = feedbackMapper.getPage(param.getStart()*param.getLimit(),param.getLimit(),param.getKeyword(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType(),param.getType());
        List<AdminFeedbackDto> datas = AdminFeedbackDto.toDtoList(feedbacks);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(datas);
        return pager;
    }

    @Override
    public void sendMessage(String content) throws ServiceException {
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
        System.out.println("今日注册用户数量："+todayUserCount);
        return todayUserCount;
    }

    @Override
    public Integer processAllUser() throws ServiceException {
        Integer allUserCount = userInfoMapper.getAllUserCount();
        System.out.println("全部用户数量："+allUserCount);
        return allUserCount;
    }

//    @Override
//    public Integer processMonthUser() throws ServiceException {
//        Integer monthUserCount = userInfoMapper.getMonthUserCount();
//        System.out.println("本月注册用户数量："+monthUserCount);
//    }
}
