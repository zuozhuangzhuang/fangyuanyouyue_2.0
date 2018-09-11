package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.model.OrderInfo;
import com.fangyuanyouyue.goods.model.OrderRefund;

public interface OrderInfoService {
    /**
     * 根据商品ID获取订单
     * @param userId
     * @param goodsId
     * @return
     * @throws ServiceException
     */
    OrderInfo selectOrderByGoodsId(Integer userId,Integer goodsId) throws ServiceException;

    /**
     * 获取退货详情
     * @param orderId
     * @return
     * @throws ServiceException
     */
    OrderRefund seletRefundByOrderId(Integer orderId) throws ServiceException;

    /**
     * 是否存在未完成订单
     * @param userId
     * @param goodsId
     * @return
     * @throws ServiceException
     */
    boolean ifHasOrder(Integer userId,Integer goodsId) throws ServiceException;
}
