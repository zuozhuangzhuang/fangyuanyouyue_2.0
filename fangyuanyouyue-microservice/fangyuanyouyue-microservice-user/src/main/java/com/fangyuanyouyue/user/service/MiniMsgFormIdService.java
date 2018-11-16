package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface MiniMsgFormIdService {
    /**
     * 存储用户formId
     * @param userId
     * @param formId
     * @throws ServiceException
     */
    void addFormId(Integer userId,String formId) throws ServiceException;

    /**
     * 获取用户formId
     * @param userId
     * @return
     * @throws ServiceException
     */
    String getFormId(Integer userId) throws ServiceException;
}
