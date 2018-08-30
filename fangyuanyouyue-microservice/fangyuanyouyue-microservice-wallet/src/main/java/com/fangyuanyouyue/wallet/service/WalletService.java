package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.WalletDto;

import java.math.BigDecimal;

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
    Object recharge(Integer userId, BigDecimal price,Integer type) throws Exception;

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
    boolean updateBalance(Integer userId,BigDecimal amount,Integer type) throws ServiceException;

    /**
     * 查询免费鉴定次数
     * @param userId
     * @return
     * @throws ServiceException
     */
    Integer getAppraisalCount(Integer userId) throws ServiceException;

    /**
     * 修改剩余免费鉴定次数
     * @param userId
     * @param count
     * @throws ServiceException
     */
    void updateAppraisalCount(Integer userId,Integer count) throws ServiceException;

    /**
     * 修改信誉度
     * @param userId
     * @param credit
     * @param type 1增加 2减少
     * @throws ServiceException
     */
    void updateCredit(Integer userId,Long credit,Integer type) throws ServiceException;

    /**
     * 修改支付密码
     * @param userId
     * @param payPwd
     * @param newPwd
     * @throws ServiceException
     */
    void updatePayPwd(Integer userId,String payPwd,String newPwd) throws ServiceException;

    /**
     * 微信支付
     * @param orderNo
     * @param price
     * @param notifyUrl
     * @throws ServiceException
     */
    WechatPayDto orderPayByWechat(String orderNo, BigDecimal price,String notifyUrl) throws Exception;

    /**
     * 支付宝支付
     * @param orderNo
     * @param price
     * @param notifyUrl
     * @return
     * @throws ServiceException
     */
    String orderPayByALi(String orderNo, BigDecimal price,String notifyUrl) throws Exception;
}
