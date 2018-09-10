package com.fangyuanyouyue.order.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.dto.OrderRefundDto;
import com.fangyuanyouyue.order.model.OrderRefund;

import java.util.List;

public interface RefundService {
    /**
     * 退货申请
     * @param userId
     * @param orderId
     * @param reason
     * @param imgUrls
     * @throws ServiceException
     */
    void orderReturnToSeller(Integer userId,Integer orderId,String reason,String[] imgUrls) throws ServiceException;

    /**
     * 退货申请
     * @param userId
     * @param start
     * @param limit
     * @param type
     * @return
     * @throws ServiceException
     */
//    List<OrderDto> orderReturnList(Integer userId, Integer start, Integer limit, Integer type) throws ServiceException;

    /**
     * 卖家处理退货
     * @param userId
     * @param orderId
     * @param reason
     * @param status
     * @throws ServiceException
     */
    void handleReturns(Integer userId, Integer orderId, String reason, Integer status) throws ServiceException;

    /**
     * 退货详情
     * @param userId
     * @param orderId
     * @return
     * @throws ServiceException
     */
    OrderRefundDto orderReturnDetail(Integer userId, Integer orderId) throws ServiceException;

    /**
     *
     * @param userId
     * @param orderId
     * @param reason
     * @param status
     * @throws ServiceException
     */
    void platformDealReturns(Integer userId, Integer orderId, String reason, Integer status) throws ServiceException;
}
