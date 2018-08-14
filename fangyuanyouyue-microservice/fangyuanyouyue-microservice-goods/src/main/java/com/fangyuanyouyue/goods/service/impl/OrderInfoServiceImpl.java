package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dao.GoodsInfoMapper;
import com.fangyuanyouyue.goods.dao.OrderDetailMapper;
import com.fangyuanyouyue.goods.dao.OrderInfoMapper;
import com.fangyuanyouyue.goods.dao.OrderRefundMapper;
import com.fangyuanyouyue.goods.model.OrderDetail;
import com.fangyuanyouyue.goods.model.OrderInfo;
import com.fangyuanyouyue.goods.model.OrderRefund;
import com.fangyuanyouyue.goods.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "orderInfoService")
public class OrderInfoServiceImpl implements OrderInfoService{
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Override
    public OrderInfo selectOrderByGoodsId(Integer userId,Integer goodsId) throws ServiceException {
        OrderDetail orderDetail = orderDetailMapper.getByUserIdGoodsId(userId, goodsId);
        if(orderDetail != null){
            OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderDetail.getOrderId());
            return orderInfo;
        }else{
            throw new ServiceException("订单信息错误！");
        }
    }

    @Override
    public OrderRefund seletRefundByOrderId(Integer orderId) throws ServiceException {
        OrderRefund orderRefund = orderRefundMapper.selectByUserIdOrderId(orderId);
        return orderRefund;
    }
}
