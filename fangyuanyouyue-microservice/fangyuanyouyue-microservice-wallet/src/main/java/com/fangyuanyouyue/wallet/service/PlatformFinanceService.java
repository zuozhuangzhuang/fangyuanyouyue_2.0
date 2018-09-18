package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.param.AdminWalletParam;

import java.math.BigDecimal;

public interface PlatformFinanceService {
    /**
     * 平台收支
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager platformFinance(AdminWalletParam param) throws ServiceException;

    /**
     * 新增平台收支记录
     * @param userId
     * @param amount
     * @param payType
     * @param type
     * @param orderNo
     * @param title
     * @param orderType
     * @param sellerId
     * @param buyerId
     * @param payNo
     * @throws ServiceException
     */
    void saveFinace(Integer userId, BigDecimal amount, Integer payType, Integer type, String orderNo, String title,Integer orderType,Integer sellerId,Integer buyerId,String payNo) throws ServiceException;
}
