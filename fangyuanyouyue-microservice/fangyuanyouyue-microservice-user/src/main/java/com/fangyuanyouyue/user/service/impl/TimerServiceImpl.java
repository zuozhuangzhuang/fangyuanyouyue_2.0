package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.constant.StatusEnum;
import com.fangyuanyouyue.user.dao.UserAuthApplyMapper;
import com.fangyuanyouyue.user.dao.UserInfoExtMapper;
import com.fangyuanyouyue.user.model.UserAuthApply;
import com.fangyuanyouyue.user.model.UserInfoExt;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import com.fangyuanyouyue.user.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
