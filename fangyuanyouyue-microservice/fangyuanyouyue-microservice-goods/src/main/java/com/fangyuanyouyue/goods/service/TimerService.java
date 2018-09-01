package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface TimerService {
    /**
     * 抢购定时降价
     * @throws ServiceException
     */
    void depreciate() throws ServiceException;

    /**
     * 定时拒绝议价（24h）
     * @throws ServiceException
     */
    void refuseBargain() throws ServiceException;
}
