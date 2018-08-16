package com.fangyuanyouyue.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.order.dao.*;
import com.fangyuanyouyue.order.dto.OrderDetailDto;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.dto.OrderPayDto;
import com.fangyuanyouyue.order.dto.SellerDto;
import com.fangyuanyouyue.order.model.*;
import com.fangyuanyouyue.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "refundService")
public class RefundServiceImpl implements RefundService{
    @Autowired
    private SchedualGoodsService schedualGoodsService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderPayMapper orderPayMapper;
    @Autowired
    private SchedualWalletService schedualWalletService;
    @Autowired
    private OrderRefundMapper orderRefundMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private OrderCommentMapper orderCommentMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;


    @Override
    public void orderReturnToSeller(Integer userId, Integer orderId, String reason,String[] imgUrls) throws ServiceException {
        //TODO 退货扣除用户积分
        //1、检测订单状态 2、检测是否退货 3、新增退货 4、发送信息
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        if (orderInfo == null) {
            throw new ServiceException("订单不存在！");
        } else {
            if (orderInfo.getUserId().intValue() != userId.intValue()) {
                throw new ServiceException("你无法操作该条订单！");
            }
            OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderId, null);
            if(orderRefund != null){
                throw new ServiceException("您已申请退货！");
            }
            //状态 1待支付 2待发货 3待收货 4已完成 5已取消 6已删除
            if (orderInfo.getStatus().intValue() == 2 || orderInfo.getStatus().intValue() == 3) {
                //退货信息
                orderRefund = new OrderRefund();
                orderRefund.setUserId(userId);
                orderRefund.setOrderId(orderId);
                if(imgUrls != null && imgUrls.length > 0){
                    for(int i=0;i<imgUrls.length;i++){
                        if(i==0){
                            orderRefund.setPic1(imgUrls[i]);
                        }
                        if(i==1){
                            orderRefund.setPic2(imgUrls[i]);
                        }
                        if(i==2){
                            orderRefund.setPic3(imgUrls[i]);
                        }
                        if(i==3){
                            orderRefund.setPic4(imgUrls[i]);
                        }
                        if(i==4){
                            orderRefund.setPic5(imgUrls[i]);
                        }
                        if(i==5){
                            orderRefund.setPic6(imgUrls[i]);
                        }
                    }
                }
                orderRefund.setReason(reason);
                orderRefund.setStatus(1);//状态 1申请退货 2退货成功 3拒绝退货
                orderRefund.setSellerReturnStatus(1);//卖家是否同意退货状态 null正常  1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货
                orderRefund.setAddTime(DateStampUtils.getTimesteamp());
                orderRefundMapper.insert(orderRefund);
                orderInfo.setIsRefund(1);//是否退货 1是 2否
                orderInfoMapper.updateByPrimaryKey(orderInfo);
                //TODO 环信给卖家发送信息 退货：您的商品【商品名称】、【xxx】、【xx】买家已申请退货，点击此处处理一下吧
                //您的抢购【抢购名称】买家已申请退货，点击此处处理一下吧
//                schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),"您的商品【"+"商品名称"+"】买家已申请退货，点击此处处理一下吧","txt",orderId.toString());

            }
        }
    }

//    @Override
//    public List<OrderDto> orderReturnList(Integer userId, Integer start, Integer limit, Integer type) throws ServiceException {
//        List<OrderInfo> refundOrder = orderInfoMapper.getRefundOrder(userId, start * limit, limit, type);
//        ArrayList<OrderDto> orderDtos = new ArrayList<>();
//        for(OrderInfo orderInfo:refundOrder){//获取订单详情列表
//            OrderDto orderDto = new OrderDto(orderInfo);
//            UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(orderInfo.getUserId())).getString("data")), UserInfo.class);
//            UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(orderInfo.getSellerId())).getString("data")), UserInfo.class);
//            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderInfo.getId());
//            ArrayList<OrderDetailDto> orderDetailDtos = OrderDetailDto.toDtoList(orderDetails, orderInfo.getStatus());
//
//            //卖家信息DTO
//            List<SellerDto> sellerDtos = new ArrayList<>();
//            SellerDto sellerDto = new SellerDto();
//            sellerDto.setSellerHeadImgUrl(seller.getHeadImgUrl());
//            sellerDto.setSellerId(seller.getId());
//            sellerDto.setSellerName(seller.getNickName());
//            sellerDtos.add(sellerDto);
//            //订单支付表
//            OrderPay orderPay = orderPayMapper.selectByOrderId(orderDto.getOrderId());
//            OrderPayDto orderPayDto = new OrderPayDto(orderPay);
//            orderDto.setOrderPayDto(orderPayDto);
//            orderDto.setSellerDtos(sellerDtos);
//            orderDto.setOrderDetailDtos(orderDetailDtos);
//            orderDto.setNickName(user.getNickName());
//            //退货状态
//            OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderInfo.getId(), null);
//            orderDto.setReturnStatus(orderRefund.getStatus());
//            orderDto.setSellerReturnStatus(orderRefund.getSellerReturnStatus());
//            orderDtos.add(orderDto);
//        }
//        return orderDtos;
//    }

    @Override
    public void handleReturns(Integer userId, Integer orderId, String reason, Integer status) throws ServiceException {
        //1、订单状态修改 2、退货状态修改 3、发送信息给买家卖家
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
        if (orderInfo == null) {
            throw new ServiceException("订单不存在！");
        } else {
            if (orderInfo.getSellerId().intValue() != userId.intValue()) {
                throw new ServiceException("你无法操作该条订单！");
            }
            OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderId, 1);
            if (orderRefund == null) {
                throw new ServiceException("获取退货信息失败！");
            }else{
                if(status.intValue() == 2){//同意
                    orderInfo.setStatus(4);//设置为已完成
                    orderRefund.setStatus(2);
                    orderRefund.setSellerReturnStatus(2);
                    orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                    orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
                    //修改余额
                    schedualWalletService.updateBalance(orderInfo.getUserId(),orderPay.getPayAmount(),1);
                    //TODO 退货：您对商品/抢购【商品名称】申请的退货卖家已同意，货款已退回您的余额。点击此处查看您的余额吧
                }else{//拒绝
                    //订单状态不变
                    orderRefund.setStatus(3);
                    orderRefund.setRefuseReason(reason);//拒绝理由
                    orderRefund.setSellerReturnStatus(3);
                    orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
                }
            }
        }
    }
}
