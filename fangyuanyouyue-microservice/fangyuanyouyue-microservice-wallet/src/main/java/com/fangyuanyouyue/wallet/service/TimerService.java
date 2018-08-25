package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;

import javax.xml.ws.Service;

public interface TimerService {
    /**
     * 会员自动到期
     * @throws ServiceException
     */
    void cancelVip() throws ServiceException;

    /**
     * 定时更新用户等级
     * @throws ServiceException
     */
    void updateLevel() throws ServiceException;
}
