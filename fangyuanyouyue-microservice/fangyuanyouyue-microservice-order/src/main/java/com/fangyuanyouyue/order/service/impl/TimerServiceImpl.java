package com.fangyuanyouyue.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.order.dao.*;
import com.fangyuanyouyue.order.model.GoodsInfo;
import com.fangyuanyouyue.order.model.OrderDetail;
import com.fangyuanyouyue.order.model.OrderInfo;
import com.fangyuanyouyue.order.model.OrderPay;
import com.fangyuanyouyue.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service(value = "timerService")
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
        //状态 1待支付 2待发货 3待收货 4已完成 5已取消 6已删除
        List<OrderInfo> orders = orderInfoMapper.selectByStatus(1);
        if(orders != null && orders.size() > 0){
            for(OrderInfo orderInfo:orders){
//                if((new Date().getTime() - orderInfo.getAddTime().getTime()) >= 24*60*60*1000){
                //测试暂用3min
                if((new Date().getTime() - orderInfo.getAddTime().getTime()) >= 3*60*1000){
                    OrderPay orderPay = orderPayMapper.selectByOrderId(orderInfo.getId());
                    //更改总订单状态
                    orderInfo.setStatus(5);//状态 1待支付 2待发货 3待收货 4已完成 5已取消
                    orderPay.setStatus(5);
                    orderInfoMapper.updateByPrimaryKey(orderInfo);
                    orderPayMapper.updateByPrimaryKey(orderPay);
                    //更改子订单状态
                    List<OrderInfo> orderInfos = orderInfoMapper.selectChildOrderByOrderId(orderInfo.getUserId(), orderInfo.getId());
                    if(orderInfos != null){
                        //存在子订单
                        for(OrderInfo info:orderInfos){
                            OrderPay pay = orderPayMapper.selectByOrderId(info.getId());
                            //更改子订单状态
                            info.setStatus(5);//状态 1待支付 2待发货 3待收货 4已完成 5已取消  7已申请退货
                            pay.setStatus(5);
                            orderInfoMapper.updateByPrimaryKey(info);
                            orderPayMapper.updateByPrimaryKey(pay);
                            //给卖家发消息：您的商品【名称】买家已取消订单
                            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(info.getId());
                            StringBuffer goodsName = new StringBuffer();
                            boolean isAuction = false;//是否是抢购
                            for(OrderDetail detail:orderDetails){
                                GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(
                                        schedualGoodsService.goodsInfo(detail.getGoodsId())).getString("data")), GoodsInfo.class);
                                isAuction = goodsInfo.getType() == 2?true:false;
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
                        schedualGoodsService.updateGoodsStatus(orderDetail.getGoodsId(),1);//状态 1出售中 2已售出 5删除
                        GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(
                                schedualGoodsService.goodsInfo(orderDetail.getGoodsId())).getString("data")), GoodsInfo.class);

                        isAuction = goodsInfo.getType() == 2?true:false;
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
                orderPay.setStatus(4);
                orderPayMapper.updateByPrimaryKey(orderPay);
                //修改订单状态
                orderInfo.setStatus(4);
                orderInfoMapper.updateByPrimaryKey(orderInfo);
                //卖家增加余额
                BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(orderInfo.getSellerId(),orderPay.getPayAmount(),1)), BaseResp.class);
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
}
