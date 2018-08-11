package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface PointOrderService {
    /**
     * 使用积分兑换商品
     * @return
     * @throws ServiceException
     */
    void saveOrder(Integer userId,Integer goodsId) throws ServiceException;

}
