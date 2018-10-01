package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface TimerService {
    /**
     * 官方认证自动过期
     * @throws ServiceException
     */
    void shopAuthTimeOut() throws ServiceException;

    /**
     * 每日统计数据
     * @throws ServiceException
     */
    void dailyStatistics() throws ServiceException;
}
