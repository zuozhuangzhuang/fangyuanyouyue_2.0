package com.fangyuanyouyue.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.Credit;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.order.dao.*;
import com.fangyuanyouyue.order.model.*;
import com.fangyuanyouyue.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service(value = "timerService")
@Transactional(rollbackFor=Exception.class)
public class TimerServiceImpl implements TimerService{
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
    public void cancelOrder() throws ServiceException {
        //1、获取所有未支付的订单 2、判断下单时间与现在时间的差值是否大于24h 3、取消大于24h的订单 4、修改商品状态
        //状态 1待支付 2待发货 3待收货 4已完成 5已取消
        List<OrderInfo> orders = orderInfoMapper.selectByStatus(1);
        if(orders != null && orders.size() > 0){
            for(OrderInfo orderInfo:orders){
                if((System.currentTimeMillis() - orderInfo.getAddTime().getTime()) >= 24*60*60*1000){
                //测试暂用3min
//                if((new Date().getTime() - orderInfo.getAddTime().getTime()) >= 3*60*1000){
                    OrderPay orderPay = orderPayMapper.selectByOrderId(orderInfo.getId());
                    //更改总订单状态
                    orderInfo.setStatus(Status.ORDER_GOODS_CANCEL.getValue());
                    orderPay.setStatus(Status.ORDER_GOODS_CANCEL.getValue());
                    orderInfoMapper.updateByPrimaryKey(orderInfo);
                    orderPayMapper.updateByPrimaryKey(orderPay);
                    //更改子订单状态
                    List<OrderInfo> orderInfos = orderInfoMapper.selectChildOrderByOrderId(orderInfo.getUserId(), orderInfo.getId());
                    if(orderInfos != null){
                        //存在子订单
                        for(OrderInfo info:orderInfos){
                            OrderPay pay = orderPayMapper.selectByOrderId(info.getId());
                            //更改子订单状态
                            info.setStatus(Status.ORDER_GOODS_CANCEL.getValue());
                            pay.setStatus(Status.ORDER_GOODS_CANCEL.getValue());
                            orderInfoMapper.updateByPrimaryKey(info);
                            orderPayMapper.updateByPrimaryKey(pay);
                            //给卖家发消息：您的商品【名称】买家已取消订单
                            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(info.getId());
                            StringBuffer goodsName = new StringBuffer();
                            boolean isAuction = false;//是否是抢购
                            for(OrderDetail detail:orderDetails){
                                GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(
                                        schedualGoodsService.goodsInfo(detail.getGoodsId())).getString("data")), GoodsInfo.class);
                                isAuction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
                                goodsName.append("【"+goodsInfo.getName()+"】");
                            }
                            schedualMessageService.easemobMessage(info.getSellerId().toString(),
                                    "您的"+(isAuction?"抢购":"商品")+goodsName+"买家超时已取消订单","3","2",info.getId().toString());
                        }
                    }
                    //获取此订单内所有商品，更改商品状态为出售中
                    List<OrderDetail> orderDetails = orderDetailMapper.selectByMainOrderId(orderInfo.getId());
                    StringBuffer goodsName = new StringBuffer();
                    boolean isAuction = false;
                    for(OrderDetail orderDetail:orderDetails){
                        schedualGoodsService.updateGoodsStatus(orderDetail.getGoodsId(),1);//状态 1出售中 2已售出 3已下架（已结束） 5删除
                        GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(
                                schedualGoodsService.goodsInfo(orderDetail.getGoodsId())).getString("data")), GoodsInfo.class);

                        isAuction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
                        goodsName.append("【"+goodsInfo.getName()+"】");
                    }
                    //给买家发送信息：您未支付的商品【名称】已取消订单
                    schedualMessageService.easemobMessage(orderInfo.getUserId().toString(),
                            "您超时未支付的"+(isAuction?"抢购":"商品")+goodsName+"已取消订单","3","2",orderInfo.getId().toString());
                }
            }
        }
    }

    @Override
    public void saveReceiptGoods() throws ServiceException {
        //1、获取所有待收货且发货时间超过12天的订单 2、修改订单状态 3、卖家增加余额 4、买家、卖家增加积分
        List<OrderPay> orderPays = orderPayMapper.selectByStatus(3, DateUtil.getDateAfterDay(new Date(), -12));
        if(orderPays != null && orderPays.size() > 0){
            for(OrderPay orderPay:orderPays){
                OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderPay.getOrderId());
                //修改订单支付表状态
                orderPay.setStatus(Status.ORDER_GOODS_COMPLETE.getValue());
                orderPayMapper.updateByPrimaryKey(orderPay);
                //修改订单状态
                orderInfo.setStatus(Status.ORDER_GOODS_COMPLETE.getValue());
                orderInfoMapper.updateByPrimaryKey(orderInfo);
                //卖家增加余额
                BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(orderInfo.getSellerId(),orderPay.getPayAmount(),Status.ADD.getValue())), BaseResp.class);
                if(baseResp.getCode() == 1){
                    throw new ServiceException(baseResp.getReport().toString());
                }
                //卖家成交增加积分
                if(orderPay.getPayAmount().compareTo(new BigDecimal(2000)) <= 0){//2000以内+20分
                    schedualWalletService.updateScore(orderInfo.getSellerId(),20L,1);
                }else{//2000以上+50分
                    schedualWalletService.updateScore(orderInfo.getSellerId(),50L,1);
                }
            }
        }
    }

    @Override
    public void updateOrderRefund() throws ServiceException {
        //1、获取申请了退货的订单 2、判断订单状态为待发货还是已发货 3、根据订单状态判断处理逻辑（待发货2天处理时间，自动同意）（已发货3天处理时间，自动拒绝） 4、更新退货申请表
        List<OrderInfo> refundOrders = orderInfoMapper.getRefundOrder(null, null, null, null);
        if(refundOrders != null){
            for(OrderInfo info:refundOrders){
                OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(info.getId(), 1,1);
                if(orderRefund != null){
                    if(info.getStatus().intValue() == Status.ORDER_GOODS_PAY.getValue()){
                        //退货申请时间 + 2天 < 当前时间
                        if(DateUtil.getDateAfterDay(orderRefund.getAddTime(),2).getTime() < new Date().getTime()){
                            //自动同意
                            orderRefund.setSellerReturnStatus(4);
                        }
                    }else if(info.getStatus().intValue() == Status.ORDER_GOODS_SENDED.getValue()){
                        //退货申请时间 + 3天 < 当前时间
                        if(DateUtil.getDateAfterDay(orderRefund.getAddTime(),3).getTime() < new Date().getTime()){
                            //自动拒绝
                            orderRefund.setSellerReturnStatus(5);
                        }
                    }else{
                        throw new ServiceException("订单状态错误！");
                    }
                    //自动处理扣除卖家信誉度
                    schedualWalletService.updateCredit(info.getSellerId(), Credit.RETURN_TIMEOUT.getCredit(), Status.SUB.getValue());
                    orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
                }
            }
        }
    }
}
