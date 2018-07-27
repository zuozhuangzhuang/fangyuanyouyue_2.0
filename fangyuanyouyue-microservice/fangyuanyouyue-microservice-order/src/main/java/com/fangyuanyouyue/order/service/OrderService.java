package com.fangyuanyouyue.order.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.dto.OrderDto;

public interface OrderService {
    /**
     * 商品下单
     * @param goodsIds
     * @param userId
     * @param addressId
     * @return
     * @throws ServiceException
     */
    OrderDto saveOrder(String token,Integer[] goodsIds,Integer userId,Integer addressId,Integer type) throws ServiceException;

    /**
     * 取消订单
     * @param userId
     * @param orderId
     * @throws ServiceException
     */
    void cancelOrder(Integer userId,Integer orderId) throws ServiceException;

    /**
     * 订单详情
     * @param userId
     * @param orderId
     * @return
     * @throws ServiceException
     */
    OrderDto orderDetail(Integer userId,Integer orderId) throws ServiceException;
}
