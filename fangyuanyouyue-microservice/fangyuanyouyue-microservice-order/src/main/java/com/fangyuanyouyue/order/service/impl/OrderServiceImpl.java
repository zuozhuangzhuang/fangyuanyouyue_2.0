package com.fangyuanyouyue.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fangyuanyouyue.order.dto.SellerDto;
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
import org.springframework.transaction.annotation.Transactional;

@Service(value = "orderService")
@Transactional(rollbackFor=Exception.class)
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
        // FIXME: 2018/8/3 邮费计算错误
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
        BigDecimal payFreight = new BigDecimal(0);//总邮费，初始为0
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
        orderPay.setProvince(addressInfo.getProvince());
        orderPay.setCity(addressInfo.getCity());
        orderPay.setArea(addressInfo.getArea());
        orderPay.setAddress(addressInfo.getAddress());
        orderPay.setPostCode(addressInfo.getPostCode());
        orderPay.setOrderNo(orderInfo.getOrderNo());
        orderPay.setAmount(amount);
        orderPay.setPayAmount(payAmount);
        orderPay.setFreight(payFreight);//运费金额，初始化为0
        orderPay.setCount(goodsIds.length);
        orderPay.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
        orderPay.setAddTime(DateStampUtils.getTimesteamp());
        orderPayMapper.insert(orderPay);

        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        //卖家DTO列表
        List<SellerDto> sellerDtos = new ArrayList<>();
        Map<Integer,List<GoodsInfo>> sellerGoods = new HashMap<>();
        if(type == 1){
            //状态 1普通商品
            for(Integer goodsId:goodsIds){
                GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(goodsId)).getString("data")),GoodsInfo.class);
                if(goods == null){
//                throw new ServiceException("商品异常！");
                    continue;
                }else{
                    if(goods.getStatus() != 1){//非出售中商品
                        throw new ServiceException("商品中包含已售出商品！");
                    }
                    BigDecimal freight = new BigDecimal(0);//邮费，初始为0
                    //计算总订单总金额
                    //每个商品生成一个订单详情表
                    OrderDetail orderDetail = new OrderDetail();
                    //买家ID
                    orderDetail.setUserId(userId);
                    //卖家ID
                    orderDetail.setSellerId(goods.getUserId());
                    orderDetail.setOrderId(orderInfo.getId());
                    orderDetail.setGoodsId(goodsId);
                    orderDetail.setGoodsName(goods.getName());
                    orderDetail.setAddTime(DateStampUtils.getTimesteamp());
                    //商品主图
                    String goodsMainImg = JSONObject.parseObject(schedualGoodsService.goodsMainImg(goodsId)).getString("data");
                    orderDetail.setMainImgUrl(goodsMainImg);
                    orderDetail.setAmount(goods.getPrice());
                    //TODO 计算优惠券（每个商品都可以使用优惠券）
                    //取运费最高者计算
                    if(goods.getPostage().compareTo(freight) > 0){
                        freight = goods.getPostage();
                        orderDetail.setFreight(freight);
                    }else{//如果不是最高邮费，就设置为0
                        orderDetail.setFreight(new BigDecimal(0));
                    }
                    //实际支付加上邮费
                    orderDetail.setPayAmount(goods.getPrice().add(orderDetail.getFreight()));
                    orderDetail.setDescription(goods.getDescription());
                    orderDetailMapper.insert(orderDetail);
                    //修改商品的状态为已售出
                    schedualGoodsService.updateGoodsStatus(goodsId,2);//状态  1出售中 2已售出 5删除
                    //订单详情DTO
                    OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail,1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                    orderDetailDtos.add(orderDetailDto);

                    //获取卖家信息
                    UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(goods.getUserId())).getString("data")), UserInfo.class);
                    SellerDto sellerDto = new SellerDto();
                    sellerDto.setSellerHeadImgUrl(seller.getHeadImgUrl());
                    sellerDto.setSellerId(seller.getId());
                    sellerDto.setSellerName(seller.getNickName());
                    boolean flag = true;
                    for(SellerDto dto:sellerDtos){
                        if(dto.getSellerId() == seller.getId()){
                            flag = false;
                        }
                    }
                    if(flag){
                        sellerDtos.add(sellerDto);
                    }
                    //根据卖家在map中存放商品列表
                    List<GoodsInfo> goodsList = sellerGoods.get(goods.getUserId());
                    if(goodsList == null){//新的商家及商品列表
                        goodsList = new ArrayList<>();
                        sellerGoods.put(goods.getUserId(),goodsList);
                    }
                    goodsList.add(goods);

                    payFreight = payFreight.add(orderDetailDto.getFreight());//总邮费
                    amount = amount.add(orderDetailDto.getAmount());//原价
                    payAmount = payAmount.add(orderDetailDto.getPayAmount());//实际支付

                }
            }
            //删除买家购物车内此商品信息:goodsFeign/cartRemove
            schedualGoodsService.cartRemove(userId,goodsIds);
        }else if(type == 2){// 2抢购商品
            GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(goodsIds[0])).getString("data")),GoodsInfo.class);
            if(goods == null){
                throw new ServiceException("抢购异常！");
            }else {
                if(goods.getStatus() != 1){//非出售中抢购
                    throw new ServiceException("抢购已售出！");
                }
                //每个商品生成一个订单详情表
                OrderDetail orderDetail = new OrderDetail();
                //买家ID
                orderDetail.setUserId(userId);
                //卖家ID
                orderDetail.setSellerId(goods.getUserId());
                orderDetail.setOrderId(orderInfo.getId());
                orderDetail.setGoodsId(goodsIds[0]);
                orderDetail.setGoodsName(goods.getName());
                orderDetail.setAddTime(DateStampUtils.getTimesteamp());
                //商品主图
                String goodsMainImg = JSONObject.parseObject(schedualGoodsService.goodsMainImg(goodsIds[0])).getString("data");
                orderDetail.setMainImgUrl(goodsMainImg);
                orderDetail.setAmount(goods.getPrice());
                //TODO 计算优惠券（每个商品都可以使用优惠券）
                //抢购下单只有一件
                orderDetail.setFreight(goods.getPostage());
                //实际支付加上邮费
                orderDetail.setPayAmount(goods.getPrice().add(orderDetail.getFreight()));
                orderDetail.setDescription(goods.getDescription());
                orderDetailMapper.insert(orderDetail);
                //修改商品的状态为已售出
                schedualGoodsService.updateGoodsStatus(goodsIds[0],2);//状态  1出售中 2已售出 5删除
                //生成DTO
                OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail,1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                orderDetailDtos.add(orderDetailDto);

                //获取卖家信息
                UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(goods.getUserId())).getString("data")), UserInfo.class);
                SellerDto sellerDto = new SellerDto();
                sellerDto.setSellerHeadImgUrl(seller.getHeadImgUrl());
                sellerDto.setSellerId(seller.getId());
                sellerDto.setSellerName(seller.getNickName());
                boolean flag = true;
                for(SellerDto dto:sellerDtos){
                    if(dto.getSellerId() == seller.getId()){
                        flag = false;
                    }
                }
                if(flag){
                    sellerDtos.add(sellerDto);
                }
                payFreight = payFreight.add(orderDetailDto.getFreight());//总邮费
                amount = amount.add(orderDetailDto.getAmount());//原价
                payAmount = payAmount.add(orderDetailDto.getPayAmount());//实际支付
            }
        }else{
            throw new ServiceException("商品状态错误！");
        }
        orderInfo.setAmount(amount);
        orderInfoMapper.updateByPrimaryKey(orderInfo);

        //TODO 计算运费，根据卖家ID，此卖家商品列表拆单
        separatesOrder(orderInfo,orderPay,sellerGoods);
        orderPay.setAmount(amount);
        orderPay.setPayAmount(payAmount);
        orderPay.setFreight(payFreight);
        orderPayMapper.updateByPrimaryKey(orderPay);


        OrderPayDto orderPayDto = new OrderPayDto(orderPay);
        OrderDto orderDto = new OrderDto(orderInfo);
        orderDto.setNickName(user.getNickName());
        orderDto.setOrderPayDto(orderPayDto);
        orderDto.setOrderDetailDtos(orderDetailDtos);
        orderDto.setSellerDtos(sellerDtos);
        return orderDto;
    }

    /**
     * 生成子订单
     * @param mainOrder 提供订单信息
     * @param mainOrderPay 提供支付订单信息
     * @param listMap 提供每个店铺及店铺商品列表
     * @return
     */
    private void separatesOrder(OrderInfo mainOrder,OrderPay mainOrderPay,Map<Integer,List<GoodsInfo>> listMap){
        if(listMap.size() == 0){
            return;
        }
        if(listMap.size() == 1){//只有一个卖家

        }
        //每个商家的商品遍历一遍
        for(Integer sellerId:listMap.keySet()){
            //生成总订单，包含多条订单详情
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setUserId(mainOrder.getUserId());
            //订单号
            final IdGenerator idg = IdGenerator.INSTANCE;
            String id = idg.nextId();
            orderInfo.setOrderNo("1"+id);

            BigDecimal amount = new BigDecimal(0);//原价，初始为0
            BigDecimal payAmount = new BigDecimal(0);//实际支付金额,初始为0
            BigDecimal payFreight = new BigDecimal(0);//总邮费，初始为0
            orderInfo.setAmount(amount);
            orderInfo.setCount(listMap.get(sellerId).size());
            orderInfo.setStatus(mainOrder.getStatus());//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
            orderInfo.setAddTime(DateStampUtils.getTimesteamp());
            //主订单ID
            orderInfo.setMainOrderId(mainOrder.getId());
            //卖家ID
            orderInfo.setSellerId(sellerId);
            orderInfoMapper.insert(orderInfo);
            //生成订单支付表
            OrderPay orderPay = new OrderPay();
            orderPay.setOrderId(orderInfo.getId());
            orderPay.setReceiverName(mainOrderPay.getReceiverName());
            orderPay.setReceiverPhone(mainOrderPay.getReceiverPhone());
            orderPay.setProvince(mainOrderPay.getProvince());
            orderPay.setCity(mainOrderPay.getCity());
            orderPay.setArea(mainOrderPay.getArea());
            orderPay.setAddress(mainOrderPay.getAddress());
            orderPay.setPostCode(mainOrderPay.getPostCode());
            orderPay.setOrderNo(orderInfo.getOrderNo());
            orderPay.setAmount(amount);
            orderPay.setPayAmount(payAmount);
            orderPay.setFreight(payFreight);//运费金额，初始化为0
            orderPay.setCount(mainOrderPay.getCount());
            orderPay.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
            orderPay.setAddTime(DateStampUtils.getTimesteamp());
            orderPayMapper.insert(orderPay);
            //订单详情，出现在这里的商品都是正常的商品，不再做判断
            for(GoodsInfo goods:listMap.get(sellerId)){
                BigDecimal freight = new BigDecimal(0);//邮费，初始为0
                //计算总订单总金额
                //每个商品生成一个订单详情表
                OrderDetail orderDetail = new OrderDetail();
                //买家ID
                orderDetail.setUserId(orderInfo.getUserId());
                //卖家ID
                orderDetail.setSellerId(goods.getUserId());
                orderDetail.setOrderId(orderInfo.getId());
                orderDetail.setGoodsId(goods.getId());
                orderDetail.setGoodsName(goods.getName());
                orderDetail.setAddTime(DateStampUtils.getTimesteamp());
                //商品主图
                String goodsMainImg = JSONObject.parseObject(schedualGoodsService.goodsMainImg(goods.getId())).getString("data");
                orderDetail.setMainImgUrl(goodsMainImg);
                orderDetail.setAmount(goods.getPrice());
                //TODO 计算优惠券（每个商品都可以使用优惠券）
                //取运费最高者计算
                if(goods.getPostage().compareTo(freight) > 0){
                    freight = goods.getPostage();
                    orderDetail.setFreight(freight);
                }else{//如果不是最高邮费，就设置为0
                    orderDetail.setFreight(new BigDecimal(0));
                }
                //实际支付加上邮费
                orderDetail.setPayAmount(goods.getPrice().add(orderDetail.getFreight()));
                orderDetail.setDescription(goods.getDescription());
                orderDetailMapper.insert(orderDetail);
                payFreight = payFreight.add(orderDetail.getFreight());
                amount = amount.add(orderDetail.getAmount());//原价
                payAmount = payAmount.add(orderDetail.getPayAmount());//实际支付
            }
            orderInfo.setAmount(amount);
            orderInfoMapper.updateByPrimaryKey(orderInfo);

            orderPay.setAmount(amount);//原价
            orderPay.setPayAmount(payAmount);//实际支付
            orderPay.setFreight(payFreight);//总邮费
            orderPayMapper.updateByPrimaryKey(orderPay);
        }
    }

    @Override
    public void cancelOrder(Integer userId, Integer orderId) throws ServiceException {
        //根据订单ID获取订单信息
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
        if(orderInfo != null && orderPay != null){
            if(orderInfo.getStatus() != 1){
                throw new ServiceException("订单状态异常！");
            }
            //更改总订单状态
            orderInfo.setStatus(5);//状态 1待支付 2待发货 3待收货 4已完成 5已取消  7已申请退货
            orderPay.setStatus(5);
            orderInfoMapper.updateByPrimaryKey(orderInfo);
            orderPayMapper.updateByPrimaryKey(orderPay);
            //更改子订单状态
            List<OrderInfo> orderInfos = orderInfoMapper.selectChildOrderByOrderId(userId, orderId);
            if(orderInfos != null){
                //存在子订单
                for(OrderInfo info:orderInfos){
                    OrderPay pay = orderPayMapper.selectByOrderId(info.getId());
                    //更改子订单状态
                    info.setStatus(5);//状态 1待支付 2待发货 3待收货 4已完成 5已取消  7已申请退货
                    pay.setStatus(5);
                    orderInfoMapper.updateByPrimaryKey(info);
                    orderPayMapper.updateByPrimaryKey(pay);
                    //TODO 给卖家发消息：订单已取消
                    Integer sellerId = info.getSellerId();
                }
            }
            //获取此订单内所有商品，更改商品状态为出售中
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
            for(OrderDetail orderDetail:orderDetails){
                schedualGoodsService.updateGoodsStatus(orderDetail.getGoodsId(),1);//状态 1出售中 2已售出 5删除
            }
            //TODO 给买家发送信息：订单已取消
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
//            for(OrderDetailDto orderDetailDto:orderDetailDtos){
//                if(orderDetailDto.getFreight().compareTo(new BigDecimal(0)) > 0){
//                    //订单详情DTO邮费为0则说明邮费不是最高或者邮费为0
//                    //支付表中加上此邮费
//                    orderPayDto.getFreight().add(orderDetailDto.getFreight());
//                }
//            }
            orderDto.setOrderDetailDtos(orderDetailDtos);

            orderDto.setNickName(user.getNickName());
            return orderDto;
        }else{
            throw new ServiceException("订单异常！");
        }
    }

    @Override
    public List<OrderDto> myOrderList(Integer userId, Integer start, Integer limit, Integer type, Integer status) throws ServiceException {
        ArrayList<OrderDto> orderDtos;
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
        if(type == 1){//我买下的
            //买家
            List<OrderInfo> listByUserIdTypeStatus = orderInfoMapper.getListByUserIdStatus(userId, start * limit, limit, status);
            orderDtos = OrderDto.toDtoList(listByUserIdTypeStatus);
            for(OrderDto orderDto:orderDtos){//获取订单详情列表
                List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderDto.getOrderId());
                ArrayList<OrderDetailDto> orderDetailDtos = OrderDetailDto.toDtoList(orderDetails, orderDto.getStatus());

                //卖家信息DTO
                List<SellerDto> sellerDtos = new ArrayList<>();
                for(OrderDetailDto orderDetailDto:orderDetailDtos){
//                    GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(orderDetailDto.getGoodsId())).getString("data")),GoodsInfo.class);
                    //获取卖家信息
                    UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(orderDetailDto.getUserId())).getString("data")), UserInfo.class);

                    SellerDto sellerDto = new SellerDto();
                    sellerDto.setSellerHeadImgUrl(seller.getHeadImgUrl());
                    sellerDto.setSellerId(seller.getId());
                    sellerDto.setSellerName(seller.getNickName());
                    boolean flag = true;
                    for(SellerDto dto:sellerDtos){
                        if(dto.getSellerId() == seller.getId()){
                            flag = false;
                        }
                    }
                    if(flag){
                        sellerDtos.add(sellerDto);
                    }
                }
                orderDto.setSellerDtos(sellerDtos);
                orderDto.setOrderDetailDtos(orderDetailDtos);
                orderDto.setNickName(user.getNickName());
            }
        }else if(type == 2){//我卖出的
            //卖家
            //根据卖家ID获取订单列表
            List<OrderInfo> orderBySellerId = orderInfoMapper.getOrderBySellerId(userId, start * limit, limit, status);
            orderDtos = OrderDto.toDtoList(orderBySellerId);
            for(OrderDto orderDto:orderDtos){//获取订单详情列表
                List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderDto.getOrderId());
                ArrayList<OrderDetailDto> orderDetailDtos = OrderDetailDto.toDtoList(orderDetails, orderDto.getStatus());

                //卖家信息DTO
                List<SellerDto> sellerDtos = new ArrayList<>();
                SellerDto sellerDto = new SellerDto();
                sellerDto.setSellerHeadImgUrl(user.getHeadImgUrl());
                sellerDto.setSellerId(user.getId());
                sellerDto.setSellerName(user.getNickName());
                sellerDtos.add(sellerDto);

                orderDto.setSellerDtos(sellerDtos);
                orderDto.setOrderDetailDtos(orderDetailDtos);
                orderDto.setNickName(user.getNickName());
            }
        }else{
            throw new ServiceException("类型异常！");
        }
        return orderDtos;
    }


}
