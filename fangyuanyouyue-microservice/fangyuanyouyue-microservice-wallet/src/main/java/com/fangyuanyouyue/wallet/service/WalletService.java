package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.BonusPoolDto;
import com.fangyuanyouyue.wallet.dto.WalletDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包操作接口
 */
public interface WalletService {
    /**
     * 充值
     * @param userId
     * @param price
     * @param type
     * @throws ServiceException
     */
    void recharge(Integer userId, BigDecimal price,Integer type) throws ServiceException;

    /**
     * 提现
     * @param userId
     * @param price
     * @param type
     * @throws ServiceException
     */
    void withdrawDeposit(Integer userId, BigDecimal price,Integer type,String account,String realName,String payPwd) throws ServiceException;

    /**
     * 获取用户钱包信息
     * @param userId
     * @return
     * @throws ServiceException
     */
    WalletDto getWallet(Integer userId) throws ServiceException;

    /**
     * 修改积分
     * @param userId
     * @param score
     * @param type 1 增加 2减少
     * @throws ServiceException
     */
    void updateScore(Integer userId,Long score,Integer type) throws ServiceException;

    /**
     * 修改余额
     * @param userId
     * @param amount
     * @param type 1充值 2消费
     * @throws ServiceException
     */
    void updateBalance(Integer userId,BigDecimal amount,Integer type) throws ServiceException;


}
