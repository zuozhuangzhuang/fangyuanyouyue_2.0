package com.fangyuanyouyue.order.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.fangyuanyouyue.order.dto.*;
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
    public OrderDto saveOrderByCart(String token,String sellerString, Integer userId, Integer addressId) throws ServiceException {
        // FIXME: 2018/8/5 事务处理（如果提交多个商品，前面的商品状态正常，且正常生成订单后修改状态，再出现异常，前面的商品状态不会rollback）
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

        //卖家DTO列表
        List<SellerDto> sellerDtos = new ArrayList<>();
        List<AddOrderDto> addOrderDtos = new ArrayList<>();
        JSONArray objects = JSONArray.parseArray(sellerString);
        for(int i=0;i<objects.size();i++){
            String str = objects.getString(i);
            AddOrderDto addOrderDto = JSONObject.toJavaObject(JSONObject.parseObject(str), AddOrderDto.class);
            addOrderDtos.add(addOrderDto);
            //获取卖家信息
            UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(addOrderDto.getSellerId())).getString("data")), UserInfo.class);
            SellerDto sellerDto = new SellerDto();
            sellerDto.setSellerHeadImgUrl(seller.getHeadImgUrl());
            sellerDto.setSellerId(seller.getId());
            sellerDto.setSellerName(seller.getNickName());
            sellerDtos.add(sellerDto);
        }
        //1.下总订单
        //2.下支付订单
        //3.拆单
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
        orderInfo.setCount(0);//商品数量，初始为0
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
        orderPay.setCount(0);//商品数量，初始为0
        orderPay.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
        orderPay.setAddTime(DateStampUtils.getTimesteamp());
        orderPayMapper.insert(orderPay);
        //生成子订单，在总订单中加入价格和邮费，实际支付价格
        List<OrderDetailDto> orderDetailDtos = separatesOrder(orderInfo, orderPay, addOrderDtos);
        OrderDto orderDto = new OrderDto(orderInfo);
        OrderPayDto orderPayDto = new OrderPayDto(orderPay);
        orderDto.setOrderPayDto(orderPayDto);
        orderDto.setOrderDetailDtos(orderDetailDtos);
        orderDto.setSellerDtos(sellerDtos);
        orderDto.setNickName(user.getNickName());
        return orderDto;
    }

    /**
     * 购物车下单—生成子订单
     * @param mainOrder 提供订单信息
     * @param mainOrderPay 提供支付订单信息
     * @param addOrderDstos 提供每个店铺及店铺商品列表
     * @return
     */
    private List<OrderDetailDto> separatesOrder(OrderInfo mainOrder,OrderPay mainOrderPay,List<AddOrderDto> addOrderDstos) throws ServiceException{
        if(addOrderDstos.size() == 0){
            return null;
        }
        //每个商家的商品遍历一遍
        List<Integer> goodsList=new ArrayList<>();
        int count = 0;//商品数，初始为0
        for(AddOrderDto addOrderDto:addOrderDstos){
            List<AddOrderDetailDto> addOrderDetailDtos = addOrderDto.getAddOrderDetailDtos();
            for(AddOrderDetailDto addOrderDetailDto:addOrderDetailDtos) {
                GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(addOrderDetailDto.getGoodsId())).getString("data")), GoodsInfo.class);
                if (goods.getStatus() != 1) {//状态 1出售中 2已售出 5删除
                    throw new ServiceException("商品状态异常！");
                }
                count++;
                goodsList.add(goods.getId());
            }
        }
        BigDecimal mainAmount = new BigDecimal(0);//原价，初始为0
        BigDecimal mainPayAmount = new BigDecimal(0);//实际支付金额,初始为0
        BigDecimal mainPayFreight = new BigDecimal(0);//总邮费，初始为0
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        for(AddOrderDto addOrderDto:addOrderDstos){
            BigDecimal amount = new BigDecimal(0);//原价，初始为0
            BigDecimal payAmount = new BigDecimal(0);//实际支付金额,初始为0
            BigDecimal payFreight = new BigDecimal(0);//总邮费，初始为0
            OrderInfo orderInfo;
            OrderPay orderPay;
            mainOrder.setIsResolve(2);//是否拆单 1是 2否 无论是否拆单，下单时主订单状态为未拆单，子订单与主订单状态恒相反
            //只有一个卖家的情况下不拆单，只生成订单详情表
            if(addOrderDstos.size() == 1){
                mainOrder.setSellerId(addOrderDto.getSellerId());//卖家ID
                orderInfo = mainOrder;
                orderPay = mainOrderPay;
            }else{
                //生成总订单，包含多条订单详情
                orderInfo = new OrderInfo();
                orderInfo.setIsResolve(1);//是否拆单 1是 2否
                orderInfo.setUserId(mainOrder.getUserId());
                //订单号
                final IdGenerator idg = IdGenerator.INSTANCE;
                String id = idg.nextId();
                orderInfo.setOrderNo("1"+id);
                orderInfo.setAmount(amount);
                orderInfo.setCount(addOrderDto.getAddOrderDetailDtos().size());
                orderInfo.setStatus(mainOrder.getStatus());//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                orderInfo.setAddTime(DateStampUtils.getTimesteamp());
                //主订单ID
                orderInfo.setMainOrderId(mainOrder.getId());
                //卖家ID
                orderInfo.setSellerId(addOrderDto.getSellerId());
                orderInfoMapper.insert(orderInfo);
                //生成订单支付表
                orderPay = new OrderPay();
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

            }

            List<AddOrderDetailDto> addOrderDetailDtos = addOrderDto.getAddOrderDetailDtos();
            //订单详情，出现在这里的商品都是正常的商品，不再做判断
            BigDecimal freight = new BigDecimal(0);//邮费，初始为0
            for(AddOrderDetailDto addOrderDetailDto:addOrderDetailDtos){
                GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(addOrderDetailDto.getGoodsId())).getString("data")),GoodsInfo.class);
                //计算总订单总金额
                //每个商品生成一个订单详情表
                OrderDetail orderDetail = new OrderDetail();
                //买家ID
                orderDetail.setUserId(orderInfo.getUserId());
                //卖家ID
                orderDetail.setSellerId(goods.getUserId());
                orderDetail.setMainOrderId(mainOrder.getId());//主订单ID
                orderDetail.setOrderId(orderInfo.getId());
                orderDetail.setGoodsId(goods.getId());
                orderDetail.setGoodsName(goods.getName());
                orderDetail.setAddTime(DateStampUtils.getTimesteamp());
                //商品主图
                String goodsMainImg = JSONObject.parseObject(schedualGoodsService.goodsMainImg(goods.getId())).getString("data");
                orderDetail.setMainImgUrl(goodsMainImg);
                orderDetail.setAmount(goods.getPrice());
                //TODO 计算优惠券（每个商品都可以使用优惠券）
                Integer couponId = addOrderDetailDto.getCouponId();//优惠券ID
                orderDetail.setCouponId(couponId);
                //取运费最高者计算
                if(goods.getPostage().compareTo(freight) > 0){
                    freight = goods.getPostage();
                    orderDetail.setFreight(goods.getPostage());
                }else{//如果不是最高邮费，就设置为0
                    orderDetail.setFreight(new BigDecimal(0));
                }
                //实际支付加上邮费
                orderDetail.setPayAmount(goods.getPrice().add(orderDetail.getFreight()));
                orderDetail.setDescription(goods.getDescription());
                orderDetailMapper.insert(orderDetail);
                //修改商品的状态为已售出
                schedualGoodsService.updateGoodsStatus(addOrderDetailDto.getGoodsId(),2);//状态  1出售中 2已售出 5删除
                payFreight = payFreight.add(orderDetail.getFreight());
                amount = amount.add(orderDetail.getAmount());//原价
                payAmount = payAmount.add(orderDetail.getPayAmount());//实际支付
                OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail,orderInfo.getStatus());
                orderDetailDtos.add(orderDetailDto);
            }
            mainAmount = mainAmount.add(amount);
            mainPayAmount = mainPayAmount.add(payAmount);
            mainPayFreight = mainPayFreight.add(payFreight);
            //子订单
            orderInfo.setAmount(amount);
            orderInfo.setCount(addOrderDetailDtos.size());
            orderInfoMapper.updateByPrimaryKey(orderInfo);
            //子支付订单
            orderPay.setCount(addOrderDetailDtos.size());
            orderPay.setAmount(amount);//原价
            orderPay.setPayAmount(payAmount);//实际支付
            orderPay.setFreight(payFreight);//总邮费
            orderPayMapper.updateByPrimaryKey(orderPay);
        }
        //删除买家购物车内此商品信息:goodsFeign/cartRemove
        Integer[] goodsIds = new Integer[goodsList.size()];
        goodsList.toArray(goodsIds);
        schedualGoodsService.cartRemove(mainOrder.getUserId(),goodsIds);
        //总订单
        mainOrder.setAmount(mainAmount);
        mainOrder.setCount(count);
        orderInfoMapper.updateByPrimaryKey(mainOrder);
        //总支付订单
        mainOrderPay.setCount(count);
        mainOrderPay.setAmount(mainAmount);
        mainOrderPay.setFreight(mainPayFreight);
        mainOrderPay.setPayAmount(mainPayAmount);
        orderPayMapper.updateByPrimaryKey(mainOrderPay);
        return orderDetailDtos;
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
        //根据订单ID获取订单信息
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        if(userId.intValue() == orderInfo.getUserId().intValue()){//用户是买家
            if(orderInfo.getIsResolve().intValue() == 1){//已拆单的主订单
                throw new ServiceException("订单状态异常！");
            }
        }
        //买家
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(orderInfo.getUserId())).getString("data")), UserInfo.class);
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
        List<OrderDetail> orderDetails;
        if(orderInfo.getMainOrderId() == null){//主订单
            orderDetails = orderDetailMapper.selectByMainOrderId(orderId);
        }else{
            orderDetails = orderDetailMapper.selectByOrderId(orderId);
        }
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

            //处理卖家信息
            List<SellerDto> sellerDtos = getSellerDtos(orderDetailDtos);
            orderDto.setSellerDtos(sellerDtos);
            return orderDto;
        }else{
            throw new ServiceException("订单异常！");
        }
    }

    private List<SellerDto> getSellerDtos(ArrayList<OrderDetailDto> orderDetailDtos) {
        //卖家信息DTO
        List<SellerDto> sellerDtos = new ArrayList<>();
        for(OrderDetailDto orderDetailDto:orderDetailDtos){
            //获取卖家信息
            UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(orderDetailDto.getSellerId())).getString("data")), UserInfo.class);

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
        return sellerDtos;
    }

    @Override
    public List<OrderDto> myOrderList(Integer userId, Integer start, Integer limit, Integer type, Integer status) throws ServiceException {
        ArrayList<OrderDto> orderDtos;
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
        if(type == 1){//我买下的
            //获取所有未拆单的订单
            List<OrderInfo> listByUserIdTypeStatus = orderInfoMapper.getListByUserIdStatus(userId, start * limit, limit, status);
            orderDtos = OrderDto.toDtoList(listByUserIdTypeStatus);
            for(OrderDto orderDto:orderDtos){//获取订单详情列表
                List<OrderDetail> orderDetails = orderDetailMapper.selectByMainOrderId(orderDto.getOrderId());
                ArrayList<OrderDetailDto> orderDetailDtos = OrderDetailDto.toDtoList(orderDetails, orderDto.getStatus());
                //卖家信息DTO
                List<SellerDto> sellerDtos = getSellerDtos(orderDetailDtos);
                //订单支付表
                OrderPay orderPay = orderPayMapper.selectByOrderId(orderDto.getOrderId());
                OrderPayDto orderPayDto = new OrderPayDto(orderPay);
                orderDto.setOrderPayDto(orderPayDto);
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
                //订单支付表
                OrderPay orderPay = orderPayMapper.selectByOrderId(orderDto.getOrderId());
                OrderPayDto orderPayDto = new OrderPayDto(orderPay);
                orderDto.setOrderPayDto(orderPayDto);
                orderDto.setSellerDtos(sellerDtos);
                orderDto.setOrderDetailDtos(orderDetailDtos);
                orderDto.setNickName(user.getNickName());
            }
        }else{
            throw new ServiceException("类型异常！");
        }
        return orderDtos;
    }

    @Override
    public OrderDto saveOrder(String token,Integer goodsId,Integer couponId,Integer userId,Integer addressId) throws ServiceException {
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

        //获取商品信息
        GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(goodsId)).getString("data")),GoodsInfo.class);
        if (goods.getStatus() != 1) {//状态 1出售中 2已售出 5删除
            throw new ServiceException("商品状态异常！");
        }
        //1.下订单
        //2.下支付订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setIsResolve(2);//是否拆单 1是 2否
        orderInfo.setUserId(userId);
        //订单号
        final IdGenerator idg = IdGenerator.INSTANCE;
        String id = idg.nextId();
        orderInfo.setOrderNo("1"+id);

        BigDecimal amount = goods.getPrice();//原价
        BigDecimal payAmount = goods.getPrice().add(goods.getPostage());//实际支付金额
        BigDecimal payFreight = goods.getPostage();//总邮费
        orderInfo.setAmount(amount);
        orderInfo.setCount(1);//商品数量，初始为0
        orderInfo.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
        orderInfo.setAddTime(DateStampUtils.getTimesteamp());
        orderInfo.setSellerId(goods.getUserId());//卖家ID
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
        orderPay.setFreight(payFreight);
        orderPay.setCount(1);//商品数量，初始为0
        orderPay.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
        orderPay.setAddTime(DateStampUtils.getTimesteamp());
        orderPayMapper.insert(orderPay);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setUserId(userId);
        orderDetail.setMainOrderId(orderInfo.getId());//主订单ID
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setGoodsId(goodsId);
        orderDetail.setGoodsName(goods.getName());
        orderDetail.setAddTime(DateStampUtils.getTimesteamp());
        orderDetail.setCouponId(couponId);
        //商品主图
        String goodsMainImg = JSONObject.parseObject(schedualGoodsService.goodsMainImg(goods.getId())).getString("data");
        orderDetail.setMainImgUrl(goodsMainImg);
        orderDetail.setAmount(amount);
        orderDetail.setFreight(payFreight);
        orderDetail.setPayAmount(payAmount);
        orderDetail.setDescription(goods.getDescription());
        orderDetail.setSellerId(goods.getUserId());
        orderDetailMapper.insert(orderDetail);
        //修改商品的状态为已售出
        schedualGoodsService.updateGoodsStatus(goodsId,2);//状态  1出售中 2已售出 5删除
        ArrayList<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        orderDetailDtos.add(new OrderDetailDto(orderDetail,orderInfo.getStatus()));
        //卖家dto
        List<SellerDto> sellerDtos = getSellerDtos(orderDetailDtos);
        //生成子订单，在总订单中加入价格和邮费，实际支付价格
        OrderDto orderDto = new OrderDto(orderInfo);
        OrderPayDto orderPayDto = new OrderPayDto(orderPay);
        orderDto.setOrderPayDto(orderPayDto);
        orderDto.setOrderDetailDtos(orderDetailDtos);
        orderDto.setSellerDtos(sellerDtos);
        orderDto.setNickName(user.getNickName());
        return orderDto;

    }
}
