package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.model.GoodsInfo;

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

    /**
     * 抢购降价
     * @param goodsInfo
     * @throws ServiceException
     */
    void getPriceDown(GoodsInfo goodsInfo) throws ServiceException;
}
