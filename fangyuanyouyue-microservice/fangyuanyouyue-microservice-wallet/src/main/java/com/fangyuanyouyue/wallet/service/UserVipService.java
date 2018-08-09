package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface UserVipService {
    /**
     * 开通会员
     * @param userId
     * @param vipLevel
     * @param vipType
     * @throws ServiceException
     */
    void openMebber(Integer userId,Integer vipLevel,Integer vipType) throws ServiceException;


}
