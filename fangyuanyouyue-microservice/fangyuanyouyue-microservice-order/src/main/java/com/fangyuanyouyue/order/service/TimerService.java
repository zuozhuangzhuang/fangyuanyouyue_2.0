package com.fangyuanyouyue.order.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface TimerService {
    /**
     * 定时取消订单
     * @throws ServiceException
     */
    void cancelOrder() throws ServiceException;

    /**
     * 自动收货
     * @throws ServiceException
     */
    void saveReceiptGoods() throws ServiceException;
}
