package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.FeedbackMapper;
import com.fangyuanyouyue.user.dao.UserInfoMapper;
import com.fangyuanyouyue.user.dto.admin.AdminFeedbackDto;
import com.fangyuanyouyue.user.model.Feedback;
import com.fangyuanyouyue.user.model.UserAuthApply;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import com.fangyuanyouyue.user.service.SystemService;
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
}
