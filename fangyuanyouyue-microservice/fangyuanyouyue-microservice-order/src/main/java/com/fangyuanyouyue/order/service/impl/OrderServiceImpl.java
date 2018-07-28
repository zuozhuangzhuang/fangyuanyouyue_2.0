package com.fangyuanyouyue.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.order.dao.OrderDetailMapper;
import com.fangyuanyouyue.order.dao.OrderInfoMapper;
import com.fangyuanyouyue.order.dao.OrderPayMapper;
import com.fangyuanyouyue.order.dto.OrderDetailDto;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.dto.OrderPayDto;
import com.fangyuanyouyue.order.model.GoodsInfo;
import com.fangyuanyouyue.order.model.OrderDetail;
import com.fangyuanyouyue.order.model.OrderInfo;
import com.fangyuanyouyue.order.model.OrderPay;
import com.fangyuanyouyue.order.model.UserAddressInfo;
import com.fangyuanyouyue.order.model.UserInfo;
import com.fangyuanyouyue.order.service.OrderService;
import com.fangyuanyouyue.order.service.SchedualGoodsService;
import com.fangyuanyouyue.order.service.SchedualUserService;

@Service(value = "orderService")
public class OrderServiceImpl implements OrderService{
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


    @Override
    public OrderDto saveOrder(String token,Integer[] goodsIds, Integer userId, Integer addressId,Integer type) throws ServiceException {
        //获取收货地址
        String result = schedualUserService.getAddressList(token,addressId);
        JSONArray addressArray = JSONArray.parseArray(JSONObject.parseObject(result).getString("data"));
        JSONObject address = null;
        if(addressArray != null && addressArray.size()>0){
            address = JSONObject.parseObject(addressArray.get(0).toString());
        }
        UserAddressInfo addressInfo = JSONObject.toJavaObject(address,UserAddressInfo.class);

        if(addressInfo == null || StringUtils.isEmpty(addressInfo.getAddress()) || StringUtils.isEmpty(addressInfo.getProvince())
                || StringUtils.isEmpty(addressInfo.getCity()) || StringUtils.isEmpty(addressInfo.getArea())){
            throw new  ServiceException("收货地址异常，请先更新地址");
        }
        //获取用户信息
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
        //生成总订单，包含多条订单详情
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        //订单号
        final IdGenerator idg = IdGenerator.INSTANCE;
        String id = idg.nextId();
        orderInfo.setOrderNo("1"+id);

        BigDecimal amount = new BigDecimal(0);//原价，初始为0
        BigDecimal payAmount = new BigDecimal(0);//实际支付金额,初始为0
        BigDecimal freight = new BigDecimal(0);//邮费，初始为0
        orderInfo.setAmount(amount);
        orderInfo.setCount(goodsIds.length);
        orderInfo.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
        orderInfo.setAddTime(DateStampUtils.getTimesteamp());
        orderInfoMapper.insert(orderInfo);
        //生成订单支付表
        OrderPay orderPay = new OrderPay();
        orderPay.setOrderId(orderInfo.getId());
        orderPay.setReceiverName(addressInfo.getReceiverName());
        orderPay.setReceiverPhone(addressInfo.getReceiverPhone());
        orderPay.setAddress(addressInfo.getAddress());
        orderPay.setPostCode(addressInfo.getPostCode());
        orderPay.setOrderNo(orderInfo.getOrderNo());
        orderPay.setAmount(amount);
        orderPay.setPayAmount(payAmount);
        orderPay.setFreight(freight);//运费金额，初始化为0
        orderPay.setCount(goodsIds.length);
        orderPay.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
        orderPay.setAddTime(DateStampUtils.getTimesteamp());
        orderPayMapper.insert(orderPay);

        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        if(type == 1){//状态 1普通商品
            for(Integer goodsId:goodsIds){
                GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(goodsId)).getString("data")),GoodsInfo.class);
                if(goods == null){
//                throw new ServiceException("商品异常！");
                    continue;
                }else{
                    //计算总订单总金额
                    //每个商品生成一个订单详情表
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setUserId(userId);
                    orderDetail.setOrderId(orderInfo.getId());
                    orderDetail.setGoodsId(goodsId);
                    orderDetail.setGoodsName(goods.getName());
                    orderDetail.setAddTime(DateStampUtils.getTimesteamp());
                    //商品主图
                    String goodsMainImg = JSONObject.parseObject(schedualGoodsService.goodsMainImg(goodsId)).getString("data");
                    orderDetail.setMainImgUrl(goodsMainImg);
                    orderDetail.setPrice(goods.getPrice());
                    //TODO 计算优惠券（每个商品都可以使用优惠券）
                    orderDetail.setOrgprice(goods.getPrice());
                    orderDetail.setFreight(goods.getPostage());
                    orderDetail.setDescription(goods.getDescription());
                    orderDetailMapper.insert(orderDetail);
                    //修改商品的状态为已售出
                    schedualGoodsService.updateGoodsStatus(goodsId,2);//状态  1出售中 2已售出 5删除
                    //订单详情DTO
                    OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail,1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                    orderDetailDtos.add(orderDetailDto);

                    amount = amount.add(orderDetailDto.getOrgPrice());//实际支付
                    payAmount = payAmount.add(orderDetailDto.getPrice());//原价
                    //取运费最高者计算
                    if(freight.compareTo(goods.getPostage()) > 0){
                        freight = goods.getPostage();
                    }
                }
            }
        }else if(type == 2){// 2抢购商品
            GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(goodsIds[0])).getString("data")),GoodsInfo.class);
            if(goods == null){
                throw new ServiceException("抢购异常！");
            }else {
                //每个商品生成一个订单详情表
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setUserId(userId);
                orderDetail.setOrderId(orderInfo.getId());
                orderDetail.setGoodsId(goodsIds[0]);
                orderDetail.setGoodsName(goods.getName());
                orderDetail.setAddTime(DateStampUtils.getTimesteamp());
                //商品主图
                String goodsMainImg = JSONObject.parseObject(schedualGoodsService.goodsMainImg(goodsIds[0])).getString("data");
                orderDetail.setMainImgUrl(goodsMainImg);
                orderDetail.setPrice(goods.getPrice());
                //TODO 计算优惠券（每个商品都可以使用优惠券）
                orderDetail.setOrgprice(goods.getPrice());
                orderDetail.setFreight(goods.getPostage());
                orderDetail.setDescription(goods.getDescription());
                orderDetailMapper.insert(orderDetail);
                //修改商品的状态为已售出
                schedualGoodsService.updateGoodsStatus(goodsIds[0],2);//状态  1出售中 2已售出 5删除
                //生成DTO
                OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail,1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                orderDetailDtos.add(orderDetailDto);

                amount = amount.add(orderDetailDto.getOrgPrice());//实际支付
                payAmount = payAmount.add(orderDetailDto.getPrice());//原价
                //取运费最高者计算
                if(freight.compareTo(goods.getPostage()) > 0){
                    freight = goods.getPostage();
                }
            }
        }else{
            throw new ServiceException("商品状态错误！");
        }
        orderInfo.setAmount(amount);
        orderInfoMapper.updateByPrimaryKey(orderInfo);


        orderPay.setAmount(amount);
        orderPay.setPayAmount(payAmount);
        orderPay.setFreight(freight);
        orderPayMapper.updateByPrimaryKey(orderPay);


        OrderPayDto orderPayDto = new OrderPayDto(orderPay);
        OrderDto orderDto = new OrderDto(orderInfo);
        orderDto.setNickName(user.getNickName());
        orderDto.setOrderPayDto(orderPayDto);
        orderDto.setOrderDetailDtos(orderDetailDtos);

        return orderDto;
    }

    @Override
    public void cancelOrder(Integer userId, Integer orderId) throws ServiceException {
        //根据订单ID获取订单信息
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
        if(orderInfo != null && orderPay != null){
            //更改订单状态
            orderInfo.setStatus(5);//状态 1待支付 2待发货 3待收货 4已完成 5已取消  7已申请退货
            orderPay.setStatus(5);
            orderInfoMapper.updateByPrimaryKey(orderInfo);
            orderPayMapper.updateByPrimaryKey(orderPay);
            //获取此订单内所有商品，更改商品状态为出售中
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
            for(OrderDetail orderDetail:orderDetails){
                schedualGoodsService.updateGoodsStatus(orderDetail.getGoodsId(),1);//状态 1出售中 2已售出 5删除
            }
        }else{
            throw new ServiceException("订单异常！");
        }
    }

    @Override
    public OrderDto orderDetail(Integer userId, Integer orderId) throws ServiceException {
        //获取用户信息
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
        //根据订单ID获取订单信息
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
        if(orderInfo != null && orderPay != null && orderDetails != null && orderDetails.size() > 0){
            OrderPayDto orderPayDto = new OrderPayDto(orderPay);
            OrderDto orderDto = new OrderDto(orderInfo);
            orderDto.setOrderPayDto(orderPayDto);
            ArrayList<OrderDetailDto> orderDetailDtos = OrderDetailDto.toDtoList(orderDetails,orderPay.getStatus());
            orderDto.setOrderDetailDtos(orderDetailDtos);

            orderDto.setNickName(user.getNickName());
            return orderDto;
        }else{
            throw new ServiceException("订单异常！");
        }
    }

    @Override
    public List<OrderDto> myOrderList(Integer userId, Integer start, Integer limit, Integer type, Integer status) throws ServiceException {
        //获取用户信息
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
        List<OrderInfo> listByUserIdTypeStatus = orderInfoMapper.getListByUserIdTypeStatus(userId, start * limit, limit, type, status);
        ArrayList<OrderDto> orderDtos = OrderDto.toDtoList(listByUserIdTypeStatus);
        for(OrderDto orderDto:orderDtos){
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderDto.getOrderId());
            ArrayList<OrderDetailDto> orderDetailDtos = OrderDetailDto.toDtoList(orderDetails, orderDto.getStatus());
            orderDto.setOrderDetailDtos(orderDetailDtos);
            orderDto.setNickName(user.getNickName());
        }
        return orderDtos;
    }
}
