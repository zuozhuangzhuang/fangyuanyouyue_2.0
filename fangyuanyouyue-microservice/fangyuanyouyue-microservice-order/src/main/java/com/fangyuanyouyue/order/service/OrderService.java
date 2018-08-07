package com.fangyuanyouyue.order.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.dto.OrderDto;

public interface OrderService {
    /**
     * 购物车商品下单
     * @param token
     * @param sellerList
     * @param userId
     * @param addressId
     * @return
     * @throws ServiceException
     */
    OrderDto saveOrderByCart(String token,String sellerList,Integer userId,Integer addressId) throws ServiceException;

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

    /**
     * 我的订单列表
     * @param userId
     * @param start
     * @param limit
     * @param type
     * @param status
     * @return
     * @throws ServiceException
     */
    List<OrderDto> myOrderList(Integer userId, Integer start,Integer limit,Integer type,Integer status) throws ServiceException;

    /**
     * 商品/抢购直接下单
     * @param token
     * @param goodsId
     * @param couponId
     * @param userId
     * @param addressId
     * @return
     * @throws ServiceException
     */
    OrderDto saveOrder(String token,Integer goodsId,Integer couponId,Integer userId,Integer addressId) throws ServiceException;
}
