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

    /**
     * 修改用户会员信息
     * @param userId
     * @param vipLevel 会员等级 1铂金会员 2至尊会员
     * @param vipType 会员类型 1一个月 2三个月 3一年会员
     * @param type 类型 1开通 2续费
     * @throws ServiceException
     */
    void updateUserVip(Integer userId,Integer vipLevel,Integer vipType,Integer type) throws ServiceException;

    /**
     * 验证用户是否是会员
     * @param userId
     * @return
     * @throws ServiceException
     */
    boolean isUserVip(Integer userId) throws ServiceException;
}
