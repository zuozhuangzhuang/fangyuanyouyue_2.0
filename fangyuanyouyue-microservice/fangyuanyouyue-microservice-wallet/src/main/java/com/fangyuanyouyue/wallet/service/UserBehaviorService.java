package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface UserBehaviorService {

    /**
     * 新增用户行为信息
     * @param userId
     * @param businessId
     * @param businessType
     * @param type
     * @throws ServiceException
     */
    void addUserBehavior(Integer userId,Integer toUserId, Integer businessId, Integer businessType, Integer type) throws ServiceException;

}
