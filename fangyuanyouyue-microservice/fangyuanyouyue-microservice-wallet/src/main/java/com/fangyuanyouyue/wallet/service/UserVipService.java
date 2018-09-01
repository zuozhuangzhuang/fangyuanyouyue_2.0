package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface UserVipService {
    /**
     * 开通/续费会员
     * @param userId
     * @param vipLevel
     * @param vipType
     * @param type
     * @param payType
     * @param payPwd
     * @throws ServiceException
     */
    Object addVipOrder(Integer userId,Integer vipLevel,Integer vipType,Integer type,Integer payType,String payPwd) throws ServiceException;

    /**
     * 修改会员订单状态
     * @param orderNo
     * @param thirdOrderNo
     * @param payType
     * @return
     * @throws ServiceException
     */
    boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException;
}
