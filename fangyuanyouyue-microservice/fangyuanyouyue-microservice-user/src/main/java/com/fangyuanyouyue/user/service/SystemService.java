package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.param.AdminUserParam;

public interface SystemService {
    /**
     * 用户反馈
     * @param userId
     * @param content
     * @param type
     * @param version
     * @throws ServiceException
     */
    void feedback(Integer userId,String content,Integer type,String version) throws ServiceException;

    /**
     * 意见反馈列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager feedbackList(AdminUserParam param) throws ServiceException;
}
