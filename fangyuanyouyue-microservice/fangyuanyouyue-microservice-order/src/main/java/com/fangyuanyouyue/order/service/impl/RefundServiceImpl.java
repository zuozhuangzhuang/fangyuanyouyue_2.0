package com.fangyuanyouyue.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.order.dao.*;
import com.fangyuanyouyue.order.dto.*;
import com.fangyuanyouyue.order.model.*;
import com.fangyuanyouyue.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "refundService")
@Transactional(rollbackFor=Exception.class)
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
        //TODO 退货扣除用户
        //1、检测订单状态 2、检测是否退货 3、新增退货 4、发送信息
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        if (orderInfo == null) {
            throw new ServiceException("订单不存在！");
        } else {
            if (orderInfo.getUserId().intValue() != userId.intValue()) {
                throw new ServiceException("你无法操作该条订单！");
            }
            OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderId, null,null);
            if(orderRefund != null){
                throw new ServiceException("您已申请退货！");
            }
            //状态 1待支付 2待发货 3待收货 4已完成 5已取消 6已删除
            if (orderInfo.getStatus().intValue() == 2 || orderInfo.getStatus().intValue() == 3) {
                //退货信息
                orderRefund = new OrderRefund();
                //待发货处理时间2天，已发货处理时间3天
//                orderRefund.setEndTime(DateUtil.getDateAfterDay(new Date(),orderInfo.getStatus()));
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
                //环信给卖家发送信息 退货：您的商品【商品名称】、【xxx】、【xx】买家已申请退货，点击此处处理一下吧
                //您的抢购【抢购名称】买家已申请退货，点击此处处理一下吧
                List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
                StringBuffer goodsName = new StringBuffer();
                boolean isAuction = false;
                for(OrderDetail detail:orderDetails){
                    //获取商品、抢购信息
                    GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(detail.getGoodsId())).getString("data")), GoodsInfo.class);
                    goodsName.append("【"+goodsInfo.getName()+"】");
                    isAuction = goodsInfo.getType() == 2?true:false;
                }
                schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                        "您的"+(isAuction?"抢购":"商品")+goodsName+"买家已申请退货，点击此处处理一下吧","3","2",orderId.toString());
            }else{
                throw new ServiceException("订单无法退货！");
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
            OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderId, 1,1);
            if (orderRefund == null) {
                throw new ServiceException("获取退货信息失败！");
            }else{
                orderRefund.setSellerReturnStatus(status);
                orderRefund.setRefuseReason(reason);//处理理由
                orderRefund.setDealTime(new Date());
                orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
            }
        }
    }

    @Override
    public void platformDealReturns(Integer userId, Integer orderId, String reason, Integer status) throws ServiceException {
        //1、订单状态修改 2、退货状态修改 3、发送信息给买家卖家
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
        if (orderInfo == null) {
            throw new ServiceException("订单不存在！");
        } else {
            OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderId, 1,null);
            if (orderRefund == null) {
                throw new ServiceException("获取退货信息失败！");
            }else{
                orderRefund.setPlatformReason(reason);//处理理由
                orderRefund.setEndTime(new Date());
                orderRefund.setStatus(status);
                //获取发信息的商品名【xx】
                List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
                StringBuffer goodsName = new StringBuffer();
                boolean isAuction = false;
                for(OrderDetail detail:orderDetails){
                    //获取商品、抢购信息
                    GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(detail.getGoodsId())).getString("data")), GoodsInfo.class);
                    goodsName.append("【"+goodsInfo.getName()+"】");
                    isAuction = goodsInfo.getType() == 2?true:false;
                }
                if(status.intValue() == 2){//同意
                    //修改余额
                    BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(orderInfo.getUserId(),orderPay.getPayAmount(),1)), BaseResp.class);
                    if(baseResp.getCode() == 1){
                        throw new ServiceException(baseResp.getReport().toString());
                    }
                    orderInfo.setStatus(4);//设置为已完成
                    orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                    //退货：您对商品/抢购【商品名称】申请的退货官方已同意，货款已退回您的余额。点击此处查看您的余额吧
                    //给买家发信息
                    schedualMessageService.easemobMessage(orderInfo.getUserId().toString(),
                            "您对"+(isAuction?"抢购":"商品")+goodsName+"申请的退货卖家已同意，货款已退回您的余额。点击此处查看您的余额吧","13","2","");
                    //给卖家发信息
                    //买家申请退货的商品/抢购【名称】官方已同意，退款已退回买家余额。如有疑问可联系客服咨询详情
                    schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                            "买家申请退货的"+(isAuction?"抢购":"商品")+goodsName+"官方已同意，退款已退回买家余额。如有疑问可联系客服咨询详情","3","2",orderInfo.getId().toString());
                }else{//拒绝
                    //订单状态不变
                    //给买家发信息
                    //很抱歉，您对商品/抢购【商品名称】申请的退货，官方已拒绝
                    schedualMessageService.easemobMessage(orderInfo.getUserId().toString(),
                            "很抱歉，您对"+(isAuction?"抢购":"商品")+goodsName+"申请的退货，官方已拒绝","3","2",orderInfo.getId().toString());
                    //买家申请退货的商品/抢购【名称】官方已拒绝，退款已退回买家余额。如有疑问可联系客服咨询详情
                    schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                            "买家申请退货的"+(isAuction?"抢购":"商品")+goodsName+"官方已拒绝，退款已退回买家余额。如有疑问可联系客服咨询详情","3","2",orderInfo.getId().toString());

                }
                orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
            }
        }
    }

    @Override
    public OrderRefundDto orderReturnDetail(Integer userId, Integer orderId) throws ServiceException {
        OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderId, null,null);
        if(orderRefund == null){
            throw new ServiceException("没找到退货信息！");
        }else{
            OrderRefundDto orderRefundDto = new OrderRefundDto(orderRefund);
            return orderRefundDto;
        }
    }
}
