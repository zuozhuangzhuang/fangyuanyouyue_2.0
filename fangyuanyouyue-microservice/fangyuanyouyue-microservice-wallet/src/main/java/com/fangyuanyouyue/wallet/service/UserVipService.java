package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface UserVipService {
    /**
     * 开通/续费会员
     * @param userId
     * @param vipLevel
     * @param vipType
     * @param type
     * @throws ServiceException
     */
    void updateMebber(Integer userId,Integer vipLevel,Integer vipType,Integer type) throws ServiceException;


}
