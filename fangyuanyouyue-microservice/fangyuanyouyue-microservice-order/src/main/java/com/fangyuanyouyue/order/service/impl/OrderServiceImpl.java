package com.fangyuanyouyue.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fangyuanyouyue.base.util.ParseReturnValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.Credit;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Score;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.order.dao.CompanyMapper;
import com.fangyuanyouyue.order.dao.OrderCommentMapper;
import com.fangyuanyouyue.order.dao.OrderDetailMapper;
import com.fangyuanyouyue.order.dao.OrderInfoMapper;
import com.fangyuanyouyue.order.dao.OrderPayMapper;
import com.fangyuanyouyue.order.dao.OrderRefundMapper;
import com.fangyuanyouyue.order.dao.UserBehaviorMapper;
import com.fangyuanyouyue.order.dao.UserCouponMapper;
import com.fangyuanyouyue.order.dto.AddOrderDetailDto;
import com.fangyuanyouyue.order.dto.AddOrderDto;
import com.fangyuanyouyue.order.dto.CompanyDto;
import com.fangyuanyouyue.order.dto.OrderDetailDto;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.dto.OrderPayDto;
import com.fangyuanyouyue.order.dto.SellerDto;
import com.fangyuanyouyue.order.dto.UserCouponDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminCompanyDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderDetailDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderPayDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderProcessDto;
import com.fangyuanyouyue.order.model.Company;
import com.fangyuanyouyue.order.model.GoodsInfo;
import com.fangyuanyouyue.order.model.OrderComment;
import com.fangyuanyouyue.order.model.OrderDetail;
import com.fangyuanyouyue.order.model.OrderInfo;
import com.fangyuanyouyue.order.model.OrderPay;
import com.fangyuanyouyue.order.model.OrderRefund;
import com.fangyuanyouyue.order.model.UserAddressInfo;
import com.fangyuanyouyue.order.model.UserBehavior;
import com.fangyuanyouyue.order.model.UserCoupon;
import com.fangyuanyouyue.order.model.UserInfo;
import com.fangyuanyouyue.order.param.AdminOrderParam;
import com.fangyuanyouyue.order.service.OrderService;
import com.fangyuanyouyue.order.service.SchedualGoodsService;
import com.fangyuanyouyue.order.service.SchedualMessageService;
import com.fangyuanyouyue.order.service.SchedualUserService;
import com.fangyuanyouyue.order.service.SchedualWalletService;
import com.snowalker.lock.redisson.RedissonLock;
import com.fangyuanyouyue.order.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private UserBehaviorMapper userBehaviorMapper;
    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private RedissonLock redissonLock;

    @Override
    public OrderDto saveOrderByCart(String token,String sellerString, Integer userId, Integer addressId) throws ServiceException {
        //验证手机号
        String verifyUserById = schedualUserService.verifyUserById(userId);
        BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(verifyUserById);
        if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
            throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
        }
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()), UserInfo.class);
        if(StringUtils.isEmpty(user.getPhone())){
            throw new ServiceException(ReCode.NO_PHONE.getValue(),ReCode.NO_PHONE.getMessage());
        }
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

        //卖家DTO列表
        List<SellerDto> sellerDtos = new ArrayList<>();
        List<AddOrderDto> addOrderDtos = new ArrayList<>();
        JSONArray objects;
        try {
            objects = JSONArray.parseArray(sellerString);
            for(int i=0;i<objects.size();i++){
                String str = objects.getString(i);
                AddOrderDto addOrderDto = JSONObject.toJavaObject(JSONObject.parseObject(str), AddOrderDto.class);
                addOrderDtos.add(addOrderDto);
                String verifySeller = schedualUserService.verifyUserById(addOrderDto.getSellerId());
                BaseResp verifySellerResult = ParseReturnValue.getParseReturnValue(verifyUserById);
                if(!verifySellerResult.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(verifySellerResult.getCode(),verifySellerResult.getReport());
                }
                //获取卖家信息
                UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(verifySellerResult.getData().toString()), UserInfo.class);
                SellerDto sellerDto = new SellerDto();
                sellerDto.setSellerHeadImgUrl(seller.getHeadImgUrl());
                sellerDto.setSellerId(seller.getId());
                sellerDto.setSellerName(seller.getNickName());
                sellerDtos.add(sellerDto);
            }
        }catch(JSONException exception){
            throw new ServiceException("下单信息错误！");
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
        orderInfo.setStatus(Status.ORDER_GOODS_PREPAY.getValue());
        orderInfo.setAddTime(new Date());
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
        orderPay.setStatus(Status.ORDER_GOODS_PREPAY.getValue());
        orderPay.setAddTime(DateStampUtils.getTimesteamp());
        orderPayMapper.insert(orderPay);
        //生成子订单，在总订单中加入价格和邮费，实际支付价格
        List<OrderDetailDto> orderDetailDtos = separatesOrder(orderInfo, orderPay, addOrderDtos);
        OrderDto orderDto = new OrderDto(orderInfo);
        OrderPayDto orderPayDto = new OrderPayDto(orderPay);
        orderDto.setOrderPayDto(orderPayDto);
        orderDto.setOrderDetailDtos(orderDetailDtos);
        orderDto.setSellerDtos(sellerDtos);
        return orderDto;
    }

    /**
     * 购物车下单—生成子订单
     * @param mainOrder 提供订单信息
     * @param mainOrderPay 提供支付订单信息
     * @param addOrderDstos 提供每个店铺及店铺商品列表
     * @return
     */
    @Transactional
    @TxTransaction(isStart=true)
    private List<OrderDetailDto> separatesOrder(OrderInfo mainOrder,OrderPay mainOrderPay,List<AddOrderDto> addOrderDstos) throws ServiceException{
        if(addOrderDstos.size() == 0){
            return null;
        }
        //每个商家的商品遍历一遍
        List<Integer> goodsList=new ArrayList<>();
        int count = 0;//商品数，初始为0
        for(AddOrderDto addOrderDto:addOrderDstos){
            List<AddOrderDetailDto> addOrderDetailDtos = addOrderDto.getAddOrderDetailDtos();
            if(addOrderDetailDtos == null || addOrderDetailDtos.size() < 1){
                throw new ServiceException("商品信息错误！");
            }
            for(AddOrderDetailDto addOrderDetailDto:addOrderDetailDtos) {
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.goodsInfo(addOrderDetailDto.getGoodsId()));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), GoodsInfo.class);
                if (goods.getStatus() != 1) {//状态 1出售中 2已售出 3已下架（已结束） 5删除
                    throw new ServiceException("商品状态异常！");
                }
                if(goods.getUserId().intValue() == mainOrder.getUserId().intValue()){
                    throw new ServiceException("不可以对自己的商品进行下单！");
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
            mainOrder.setIsResolve(Status.NO.getValue());//是否拆单 1是 2否 无论是否拆单，下单时主订单状态为未拆单，子订单与主订单状态恒相反
            //只有一个卖家的情况下不拆单，只生成订单详情表
            if(addOrderDstos.size() == 1){
                mainOrder.setSellerId(addOrderDto.getSellerId());//卖家ID
                orderInfo = mainOrder;
                orderPay = mainOrderPay;
            }else{
                //生成总订单，包含多条订单详情
                orderInfo = new OrderInfo();
                orderInfo.setIsResolve(Status.YES.getValue());//是否拆单 1是 2否
                orderInfo.setUserId(mainOrder.getUserId());
                //订单号
                final IdGenerator idg = IdGenerator.INSTANCE;
                String id = idg.nextId();
                orderInfo.setOrderNo("1"+id);
                orderInfo.setAmount(amount);
                orderInfo.setCount(addOrderDto.getAddOrderDetailDtos().size());
                orderInfo.setStatus(mainOrder.getStatus());
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
                orderPay.setStatus(Status.ORDER_GOODS_PREPAY.getValue());
                orderPay.setAddTime(DateStampUtils.getTimesteamp());
                orderPayMapper.insert(orderPay);

            }

            List<AddOrderDetailDto> addOrderDetailDtos = addOrderDto.getAddOrderDetailDtos();
            //订单详情，出现在这里的商品都是正常的商品，不再做判断
            for(AddOrderDetailDto addOrderDetailDto:addOrderDetailDtos){
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.goodsInfo(addOrderDetailDto.getGoodsId()));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), GoodsInfo.class);
                if(goods.getPostage().compareTo(payFreight) > 0){
                    payFreight = goods.getPostage();
                }
            }
            //每个卖家的商品
            StringBuffer goodsName = new StringBuffer();
            for(AddOrderDetailDto addOrderDetailDto:addOrderDetailDtos){
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.goodsInfo(addOrderDetailDto.getGoodsId()));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), GoodsInfo.class);

            	//加入分布式锁，锁住商品id，10秒后释放
            	boolean lock = redissonLock.lock("GoodsOrder"+addOrderDetailDto.getGoodsId().toString(), 10);
            	if(!lock) {
            		throw new ServiceException("您来晚啦，商品已被抢走了～～");
            	}


            	try {
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

	                //计算优惠券（每个商品都可以使用优惠券）
	                Integer couponId = addOrderDetailDto.getCouponId();//优惠券ID

	                BigDecimal price = goods.getPrice();
	                if(couponId != null){
	                    //判断商品所属店铺是否可用优惠券
	                    if(!Boolean.valueOf(JSONObject.parseObject(schedualUserService.userIsAuth(goods.getUserId())).getString("data"))){
	                        throw new ServiceException("【"+goods.getName()+"】所属店铺未认证，不可使用优惠券！");
	                    }
                        baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.getPriceByCoupon(orderInfo.getUserId(),price,couponId));
                        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                        }else{
	                        orderDetail.setCouponId(couponId);
	                        price = (BigDecimal)baseResp.getData();
	                    }
	                }
	                //取运费最高者计算
	//                if(goods.getPostage().compareTo(freight) > 0){
	//                    freight = goods.getPostage();
	//                    orderDetail.setFreight(goods.getPostage());
	//                }else{//如果不是最高邮费，就设置为0
	//                    orderDetail.setFreight(new BigDecimal(0));
	//                }
	                //实际支付加上邮费
	                orderDetail.setPayAmount(price);
	                orderDetail.setDescription(goods.getDescription());
	                orderDetailMapper.insert(orderDetail);
	                //修改商品的状态为已售出
                    BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualGoodsService.updateGoodsStatus(addOrderDetailDto.getGoodsId(), 2));//状态  1出售中 2已售出 3已下架（已结束） 5删除
                    if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
                    }
	                amount = amount.add(orderDetail.getAmount());//原价
	                payAmount = payAmount.add(orderDetail.getPayAmount());//实际支付
	                OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail);
	                //优惠券
	                UserCoupon userCoupon = userCouponMapper.selectUserCouponDetail(orderDetail.getCouponId());
	                if(userCoupon != null){
	                    UserCouponDto userCouponDto = new UserCouponDto(userCoupon);
	                    orderDetailDto.setUserCouponDto(userCouponDto);
	                }
	                orderDetailDtos.add(orderDetailDto);
	                goodsName.append("【"+goods.getName()+"】");
            	}catch (Exception e) {
            		e.printStackTrace();
                    throw new ServiceException("下单出错，请稍后再试！");
				}finally {
                    redissonLock.release("GoodsOrder"+goods.getId());
				}
            }
            mainAmount = mainAmount.add(amount);
            mainPayAmount = mainPayAmount.add(payAmount.add(payFreight));
            mainPayFreight = mainPayFreight.add(payFreight);
            //子订单
            orderInfo.setAmount(amount);
            orderInfo.setCount(addOrderDetailDtos.size());
            orderInfoMapper.updateByPrimaryKey(orderInfo);
            //子支付订单
            orderPay.setCount(addOrderDetailDtos.size());
            orderPay.setAmount(amount);//原价
            orderPay.setPayAmount(payAmount.add(payFreight));//实际支付
            orderPay.setFreight(payFreight);//总邮费
            orderPayMapper.updateByPrimaryKey(orderPay);

            //交易消息：恭喜您！您的商品【大头三年原光】、【xxx】、【xx】已有人下单，点击此处查看订单
            schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                    "恭喜您！您的商品"+goodsName.toString()+"已有人下单，点击此处查看订单",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_ORDER_SELLER.getMessage(),orderInfo.getId().toString());
        }
        //删除买家购物车内此商品信息:goodsFeign/cartRemove
        Integer[] goodsIds = new Integer[goodsList.size()];
        goodsList.toArray(goodsIds);
        BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.cartRemove(mainOrder.getUserId(),goodsIds));
        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
        }
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
    @Transactional
    @TxTransaction(isStart=true)
    public void cancelOrder(Integer userId, Integer orderId) throws ServiceException {
        //根据订单ID获取订单信息
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKeyDetail(orderId);
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
        if(orderInfo != null && orderPay != null){
            if(orderInfo.getStatus() != Status.ORDER_GOODS_PREPAY.getValue()){
                throw new ServiceException("订单状态异常！");
            }
            //更改总订单状态
            orderInfo.setStatus(Status.ORDER_GOODS_CANCEL.getValue());
            orderPay.setStatus(Status.ORDER_GOODS_CANCEL.getValue());
            orderInfoMapper.updateByPrimaryKey(orderInfo);
            orderPayMapper.updateByPrimaryKey(orderPay);
            //获取此订单内所有商品，更改商品状态为出售中
            List<OrderDetail> orderDetails = orderDetailMapper.selectByMainOrderId(orderId);
            StringBuffer goodsName = new StringBuffer();
            boolean isAuction = false;
            for(OrderDetail orderDetail:orderDetails){
                BaseResp  baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.updateGoodsStatus(orderDetail.getGoodsId(),1));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.goodsInfo(orderDetail.getGoodsId()));
                //状态 1出售中 2已售出 3已下架（已结束） 5删除
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), GoodsInfo.class);

                isAuction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
                goodsName.append("【"+goodsInfo.getName()+"】");
            }
            //更改子订单状态
            List<OrderInfo> orderInfos = orderInfoMapper.selectChildOrderByOrderId(userId, orderId);
            if(orderInfos != null && orderInfos.size() > 0){
                //存在子订单
                for(OrderInfo info:orderInfos){
                    OrderPay pay = orderPayMapper.selectByOrderId(info.getId());
                    //更改子订单状态
                    info.setStatus(Status.ORDER_GOODS_CANCEL.getValue());
                    pay.setStatus(Status.ORDER_GOODS_CANCEL.getValue());
                    orderInfoMapper.updateByPrimaryKey(info);
                    orderPayMapper.updateByPrimaryKey(pay);
                    //给卖家发消息：您的商品【名称】买家已取消订单
                    List<OrderDetail> details = orderDetailMapper.selectByOrderId(info.getId());
                    StringBuffer name = new StringBuffer();
                    boolean auction = false;//是否是抢购
                    for(OrderDetail detail:details){
                        BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.goodsInfo(detail.getGoodsId()));
                        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                        }
                        GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), GoodsInfo.class);
                        auction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
                        name.append("【"+goodsInfo.getName()+"】");
                    }
                    schedualMessageService.easemobMessage(info.getSellerId().toString(),
                            "您的"+(auction?"抢购":"商品")+name+"买家已取消订单",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),info.getId().toString());
                }
            }else{
                schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                        "您的"+(isAuction?"抢购":"商品")+goodsName+"买家已取消订单",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),orderInfo.getId().toString());
            }

            //给买家发送信息：您未支付的商品【名称】已取消订单
            schedualMessageService.easemobMessage(userId.toString(),
                    "您未支付的"+(isAuction?"抢购":"商品")+goodsName+"已取消订单",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),orderInfo.getId().toString());
        }else{
            throw new ServiceException("订单异常！");
        }
    }

    @Override
    public OrderDto orderDetail(Integer userId, Integer orderId) throws ServiceException {
        //根据订单ID获取订单信息
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKeyDetail(orderId);
        if(orderInfo == null){
            throw new ServiceException("未找到订单");
        }
        if(userId.intValue() == orderInfo.getUserId().intValue()){//用户是买家
            if(orderInfo.getIsResolve().intValue() == Status.YES.getValue()){//已拆单的主订单
                throw new ServiceException("订单状态异常！");
            }
        }
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
            ArrayList<OrderDetailDto> orderDetailDtos = new ArrayList<>();
            for(OrderDetail orderDetail:orderDetails){
                OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail);
                //优惠券
                UserCoupon userCoupon = userCouponMapper.selectUserCouponDetail(orderDetail.getCouponId());
                if(userCoupon != null){
                    UserCouponDto userCouponDto = new UserCouponDto(userCoupon);
                    orderDetailDto.setUserCouponDto(userCouponDto);
                }
                orderDetailDtos.add(orderDetailDto);
            }
//            for(OrderDetailDto orderDetailDto:orderDetailDtos){
//                if(orderDetailDto.getFreight().compareTo(new BigDecimal(0)) > 0){
//                    //订单详情DTO邮费为0则说明邮费不是最高或者邮费为0
//                    //支付表中加上此邮费
//                    orderPayDto.getFreight().add(orderDetailDto.getFreight());
//                }
//            }
            orderDto.setOrderDetailDtos(orderDetailDtos);


            //处理卖家信息
            List<SellerDto> sellerDtos = getSellerDtos(orderDetailDtos);
            orderDto.setSellerDtos(sellerDtos);
            if(orderInfo.getIsRefund().intValue() == Status.YES.getValue()){
                //退货状态
                OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderInfo.getId(), null,null);
                orderDto.setReturnStatus(orderRefund.getStatus());
                orderDto.setSellerReturnStatus(orderRefund.getSellerReturnStatus());
                orderDto.setReturnTime(DateUtil.getFormatDate(orderRefund.getAddTime(),DateUtil.DATE_FORMT));
            }
            //是否评价
            OrderComment orderComment = orderCommentMapper.selectByOrder(orderId);
            if(orderComment != null){
                orderDto.setIsEvaluation(1);
            }
            return orderDto;
        }else{
            throw new ServiceException("订单异常！");
        }
    }

    /**
     * 封装卖家信息
     * @param orderDetailDtos
     * @return
     */
    private List<SellerDto> getSellerDtos(ArrayList<OrderDetailDto> orderDetailDtos) {
        //卖家信息DTO
        List<SellerDto> sellerDtos = new ArrayList<>();
        for(OrderDetailDto orderDetailDto:orderDetailDtos){
            //获取卖家信息
            UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(orderDetailDto.getSellerId())).getString("data")), UserInfo.class);
            if(seller != null){
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
        }
        return sellerDtos;
    }

    @Override
    public List<OrderDto> myOrderList(Integer userId, Integer start, Integer limit, Integer type, Integer status,String search) throws ServiceException {
        ArrayList<OrderDto> orderDtos = new ArrayList<>();;

        if(type == 1){//我买下的
            //获取所有未拆单的订单
            //增加搜索功能
            List<OrderInfo> listByUserIdTypeStatus = orderInfoMapper.getListByUserIdStatus(userId, start * limit, limit, status,search);
//            orderDtos = OrderDto.toDtoList(listByUserIdTypeStatus);
//            for(OrderDto orderDto:orderDtos){
            for(OrderInfo info:listByUserIdTypeStatus){
                OrderDto orderDto = new OrderDto(info);
                //获取订单详情列表
                //如果是没有拆单的订单根据主订单获取，拆了单的根据订单id获取
                List<OrderDetail> orderDetails;
                if(info.getSellerId() == null){
                    orderDetails = orderDetailMapper.selectByMainOrderId(orderDto.getOrderId());
                }else{
                    orderDetails = orderDetailMapper.selectByOrderId(orderDto.getOrderId());
                }
                ArrayList<OrderDetailDto> orderDetailDtos = new ArrayList<>();
                for(OrderDetail orderDetail:orderDetails){
                    OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail);
                    //优惠券
                    UserCoupon userCoupon = userCouponMapper.selectUserCouponDetail(orderDetail.getCouponId());
                    if(userCoupon != null){
                        UserCouponDto userCouponDto = new UserCouponDto(userCoupon);
                        orderDetailDto.setUserCouponDto(userCouponDto);
                    }
                    orderDetailDtos.add(orderDetailDto);
                }
                //卖家信息DTO
                List<SellerDto> sellerDtos = getSellerDtos(orderDetailDtos);
                //订单支付表
                OrderPay orderPay = orderPayMapper.selectByOrderId(orderDto.getOrderId());
                OrderPayDto orderPayDto = new OrderPayDto(orderPay);
                orderDto.setOrderPayDto(orderPayDto);
                orderDto.setSellerDtos(sellerDtos);
                orderDto.setOrderDetailDtos(orderDetailDtos);
                if(orderDto.getIsRefund() == 1){
                    //退货状态
                    OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderDto.getOrderId(), null,null);
                    orderDto.setReturnStatus(orderRefund.getStatus());
                    orderDto.setSellerReturnStatus(orderRefund.getSellerReturnStatus());
                    orderDto.setReturnTime(DateUtil.getFormatDate(orderRefund.getAddTime(),DateUtil.DATE_FORMT));
                }
                //是否评价
                OrderComment orderComment = orderCommentMapper.selectByOrder(orderDto.getOrderId());
                if(orderComment != null){
                    orderDto.setIsEvaluation(1);
                }
                orderDtos.add(orderDto);
            }
        }else if(type == 2){//我卖出的
            //卖家
            String verifyUserById = schedualUserService.verifyUserById(userId);
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(verifyUserById);
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()), UserInfo.class);
            //根据卖家ID获取订单列表
            List<OrderInfo> orderBySellerId = orderInfoMapper.getOrderBySellerId(userId, start * limit, limit, status,search);
            orderDtos = OrderDto.toDtoList(orderBySellerId);
            //卖家信息DTO
            List<SellerDto> sellerDtos = new ArrayList<>();
            SellerDto sellerDto = new SellerDto();
            sellerDto.setSellerHeadImgUrl(seller.getHeadImgUrl());
            sellerDto.setSellerId(seller.getId());
            sellerDto.setSellerName(seller.getNickName());
            sellerDtos.add(sellerDto);
            for(OrderDto orderDto:orderDtos){//获取订单详情列表
                List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderDto.getOrderId());
                ArrayList<OrderDetailDto> orderDetailDtos = new ArrayList<>();
                for(OrderDetail orderDetail:orderDetails){
                    OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail);
                    //优惠券
                    UserCoupon userCoupon = userCouponMapper.selectUserCouponDetail(orderDetail.getCouponId());
                    if(userCoupon != null){
                        UserCouponDto userCouponDto = new UserCouponDto(userCoupon);
                        orderDetailDto.setUserCouponDto(userCouponDto);
                    }
                    orderDetailDtos.add(orderDetailDto);
                }

                //订单支付表
                OrderPay orderPay = orderPayMapper.selectByOrderId(orderDto.getOrderId());
                OrderPayDto orderPayDto = new OrderPayDto(orderPay);
                orderDto.setOrderPayDto(orderPayDto);
                orderDto.setSellerDtos(sellerDtos);
                orderDto.setOrderDetailDtos(orderDetailDtos);
                if(orderDto.getIsRefund() == 1){
                    //退货状态
                    OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderDto.getOrderId(), null,null);
                    if(orderRefund != null){
                        orderDto.setReturnStatus(orderRefund.getStatus());
                        orderDto.setSellerReturnStatus(orderRefund.getSellerReturnStatus());
                        orderDto.setReturnTime(DateUtil.getFormatDate(orderRefund.getAddTime(),DateUtil.DATE_FORMT));
                    }
                }
            }
        }else{
            throw new ServiceException("类型异常！");
        }
        return orderDtos;
    }

    @Override
    @Transactional
    @TxTransaction(isStart=true)
    public OrderDto saveOrder(String token,Integer goodsId,Integer couponId,Integer userId,Integer addressId,Integer type) throws ServiceException {
    	
        try {
        	//加入分布式锁，锁住商品id，10秒后释放
        	try {
        		if(!redissonLock.lock("GoodsOrder"+goodsId, 20)) {
        			throw new ServiceException("您来晚啦，商品已被抢走了～～");
        		}
        	}catch (Exception e) {
        		throw new ServiceException("您来晚啦，商品已被抢走了～～");
			}
        	
        	
	    	//验证手机号
	        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
	        if(StringUtils.isEmpty(user.getPhone())){
	            throw new ServiceException("未绑定手机号！");
	        }

	        //获取商品信息
	        GoodsInfo goods = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(goodsId)).getString("data")),GoodsInfo.class);
	        if (goods.getStatus() != 1) {//状态 1出售中 2已售出 3已下架（已结束） 5删除
	            throw new ServiceException("商品状态异常！");
	        }
	        if(goods.getUserId().intValue() == userId.intValue()){
	            throw new ServiceException("不可以对自己的商品进行下单！");
	        }

	        if(type.intValue() == Status.AUCTION.getValue()){
	            //非会员只能免费抢购一次，会员可无限制抢购——验证是否为会员
	            if(!Boolean.valueOf(JSONObject.parseObject(schedualWalletService.isUserVip(userId)).getString("data"))){
	                List<UserBehavior> userBehaviors = userBehaviorMapper.selectByUserIdType(userId, Status.BUY_AUCTION.getValue());
	                if(userBehaviors != null && userBehaviors.size() > 0){
	                    throw new ServiceException("非会员只能免费抢购一次！");
	                }else{
	                    UserBehavior userBehavior = new UserBehavior();
	                    userBehavior.setUserId(userId);
	                    userBehavior.setBusinessId(goodsId);
	                    userBehavior.setBusinessType(Status.BUSINESS_TYPE_GOODS.getValue());
	                    userBehavior.setType(Status.BUY_AUCTION.getValue());
	                    userBehavior.setToUserId(goods.getUserId());
	                    userBehavior.setAddTime(DateStampUtils.getTimesteamp());
	                    userBehaviorMapper.insert(userBehavior);
	                }
	            }
	        }
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


	        //1.下订单
	        //2.下支付订单
	        OrderInfo orderInfo = new OrderInfo();
	        orderInfo.setIsResolve(Status.NO.getValue());//是否拆单 1是 2否
	        orderInfo.setUserId(userId);
	        //订单号
	        final IdGenerator idg = IdGenerator.INSTANCE;
	        String id = idg.nextId();
	        orderInfo.setOrderNo("1"+id);

	        BigDecimal amount = goods.getPrice();//原价
	        //计算优惠券（每个商品都可以使用优惠券）
	        if(couponId != null){
	            //判断商品所属店铺是否可用优惠券
	            if(!Boolean.valueOf(JSONObject.parseObject(schedualUserService.userIsAuth(goods.getUserId())).getString("data"))){
	                throw new ServiceException("【"+goods.getName()+"】所属店铺未认证，不可使用优惠券！");
	            }
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.getPriceByCoupon(userId,amount,couponId));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }else{
	                amount = (BigDecimal)baseResp.getData();
	            }
	        }
	        BigDecimal payFreight = goods.getPostage()==null?new BigDecimal(0):goods.getPostage();//总邮费
	        BigDecimal payAmount = amount.add(payFreight);//实际支付金额
	        orderInfo.setAmount(amount);
	        orderInfo.setCount(1);//商品数量，初始为0
	        orderInfo.setStatus(Status.ORDER_GOODS_PREPAY.getValue());
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
	        orderPay.setStatus(Status.ORDER_GOODS_PREPAY.getValue());
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
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualGoodsService.updateGoodsStatus(goodsId, 2));//状态  1出售中 2已售出 3已下架（已结束） 5删除
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
            }

	        ArrayList<OrderDetailDto> orderDetailDtos = new ArrayList<>();
	        OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail);
	        //优惠券
	        UserCoupon userCoupon = userCouponMapper.selectUserCouponDetail(orderDetail.getCouponId());
	        if(userCoupon != null){
	            UserCouponDto userCouponDto = new UserCouponDto(userCoupon);
	            orderDetailDto.setUserCouponDto(userCouponDto);
	        }
	        orderDetailDtos.add(orderDetailDto);
	        //卖家dto
	        List<SellerDto> sellerDtos = getSellerDtos(orderDetailDtos);
	        //生成子订单，在总订单中加入价格和邮费，实际支付价格
	        OrderDto orderDto = new OrderDto(orderInfo);
	        OrderPayDto orderPayDto = new OrderPayDto(orderPay);
	        orderDto.setOrderPayDto(orderPayDto);
	        orderDto.setOrderDetailDtos(orderDetailDtos);
	        orderDto.setSellerDtos(sellerDtos);
	        //交易消息：恭喜您！您的商品【大头三年原光】已有人下单，点击此处查看订单
	        // 交易消息：恭喜您！您的抢购【大头三年原光】已有人下单，点击此处查看订单
	        schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
	                "恭喜您！您的"+(goods.getType()==Status.GOODS.getValue()?"商品【":"抢购【")+goods.getName()+"】已有人下单，点击此处查看订单",
	                Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_ORDER_SELLER.getMessage(),orderInfo.getId().toString());
	        return orderDto;
        }catch (Exception e) {
        	e.printStackTrace();
            throw new ServiceException("下单出错，请稍后再试！");
		}finally {
            redissonLock.release("GoodsOrder" + goodsId);
		}

    }

    @Override
    @Transactional
    @TxTransaction(isStart=true)
    public Object getOrderPay(Integer userId, Integer orderId, Integer payType, String payPwd) throws ServiceException {
        //只有买家能调用订单支付接口，直接根据orderId查询订单
//        OrderInfo orderInfo = orderInfoMapper.getOrderByUserIdOrderId(orderId,userId);
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKeyDetail(orderId);
        if(orderInfo == null){
            throw new ServiceException("订单不存在！");
        }else{
            if(orderInfo.getStatus() != Status.ORDER_GOODS_PREPAY.getValue()){
                throw new ServiceException("订单状态异常！");
            }else{
                OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);

                if(orderPay == null){
                    throw new ServiceException("订单支付信息异常！");
                }


                StringBuffer payInfo = new StringBuffer();
                if(payType.intValue() == Status.PAY_TYPE_WECHAT.getValue()){
                    String getWechatOrder = schedualWalletService.orderPayByWechat(orderInfo.getOrderNo(), orderPay.getPayAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.order_wechat_notify.getNotifUrl());
                    BaseResp result = ParseReturnValue.getParseReturnValue(getWechatOrder);
                    if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(result.getCode(),result.getReport());
                    }
                    WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(result.getData().toString()), WechatPayDto.class);
                    return wechatPayDto;
                }else if(payType.intValue() == Status.PAY_TYPE_ALIPAY.getValue()){
                    String getALiOrder = schedualWalletService.orderPayByALi(orderInfo.getOrderNo(), orderPay.getPayAmount(),NotifyUrl.notify.getNotifUrl()+NotifyUrl.order_alipay_notify.getNotifUrl());
                    BaseResp result = ParseReturnValue.getParseReturnValue(getALiOrder);
                    if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(result.getCode(),result.getReport());
                    }
                    payInfo.append(result.getData());
                }else if(payType.intValue() == Status.PAY_TYPE_BALANCE.getValue()){
                    String verifyPayPwd = schedualUserService.verifyPayPwd(userId, payPwd);
                    BaseResp result = ParseReturnValue.getParseReturnValue(verifyPayPwd);
                    if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(result.getCode(),result.getReport());
                    }
                    if (!(boolean)result.getData()) {
                        throw new ServiceException(ReCode.PAYMENT_PASSWORD_ERROR.getValue(),ReCode.PAYMENT_PASSWORD_ERROR.getMessage());
                    }else{
                        BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(userId, orderPay.getPayAmount(), Status.SUB.getValue()));
                        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                        }
                        updateOrder(orderInfo.getOrderNo(),null,payType);
                        payInfo.append("余额支付成功！");
                    }
                }else if(payType.intValue() == Status.PAY_TYPE_MINI.getValue()){
                    String getMiniOrder = schedualWalletService.orderPayByWechatMini(userId,orderInfo.getOrderNo(), orderPay.getPayAmount(), NotifyUrl.mini_notify.getNotifUrl()+NotifyUrl.order_wechat_notify.getNotifUrl());
                    BaseResp result = ParseReturnValue.getParseReturnValue(getMiniOrder);
                    if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(result.getCode(),result.getReport());
                    }
                    WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(result.getData().toString()), WechatPayDto.class);
                    return wechatPayDto;
                }else{
                    throw new ServiceException("支付类型错误！");
                }

                return payInfo.toString();
            }
        }
    }

    @Override
    public Integer getProcess(Integer userId, Integer type) throws ServiceException {
        /**
         * 订单：
         *  1 我买下的：
         *   待付款+待收货
         *  2 我卖出的：
         *   待发货+待处理退货
         */
        Integer count = 0;
        if(type.intValue() == 1){
            //状态 1待支付 3待收货
            List<OrderInfo> list1 = orderInfoMapper.getListByUserIdStatus(userId, null, null, 1,null);
            List<OrderInfo> list2 = orderInfoMapper.getListByUserIdStatus(userId, null, null, 3,null);
            count = (list1 == null?0:list1.size())+(list2 == null?0:list2.size());
        }else{
            //状态 2待发货  7已申请退货
            List<OrderInfo> list1 = orderInfoMapper.getOrderBySellerId(userId, null, null, 2,null);
            List<OrderInfo> list2 = orderInfoMapper.getOrderBySellerId(userId, null, null, 7,null);
//            List<OrderInfo> list2 = orderInfoMapper.getRefundOrder(userId, null, null, 2);
            for(OrderInfo orderInfo:list2){
                //状态 1申请退货 2退货成功 3拒绝退货
                OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderInfo.getId(), 1,1);
                if(orderRefund != null){
                    count++;
                }
            }
            count+=(list1 == null?0:list1.size());
        }
        return count;
    }

    @Override
    public void sendGoods(Integer userId, Integer orderId, Integer companyId, String number) throws ServiceException {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKeyDetail(orderId);
        if(orderInfo == null){
            throw new ServiceException("订单不存在！");
        }else{
            if(orderInfo.getStatus() != Status.ORDER_GOODS_PAY.getValue()){
                throw new ServiceException("订单状态异常！");
            }else{
                OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
                if(orderPay == null){
                    throw new ServiceException("订单支付信息异常！");
                }
                //获取物流公司名
                Company company = companyMapper.selectByPrimaryKey(companyId);
                //物流状态
//                orderPay.setLogisticStatus();
                orderPay.setLogisticCompany(company.getName());
                orderPay.setLogisticCompanyNo(company.getCompanyNo());
                orderPay.setLogisticCode(number);
                orderPay.setStatus(Status.ORDER_GOODS_SENDED.getValue());
                orderPay.setSendTime(new Date());
                orderPayMapper.updateByPrimaryKey(orderPay);
                orderInfo.setStatus(Status.ORDER_GOODS_SENDED.getValue());
                orderInfoMapper.updateByPrimaryKey(orderInfo);
            }
        }
    }

    @Override
    @Transactional
    @TxTransaction(isStart=true)
    public void getGoods(Integer userId, Integer orderId) throws ServiceException {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKeyDetail(orderId);
        if (orderInfo == null) {
            throw new ServiceException("订单不存在！");
        } else {
            if (orderInfo.getStatus() != Status.ORDER_GOODS_SENDED.getValue()) {
                throw new ServiceException("订单状态异常！");
            } else {
                if(orderInfo.getIsRefund() == Status.YES.getValue()){
                    OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderInfo.getId(), 1, null);
                    if(orderRefund != null){
                        throw new ServiceException("订单正在退货！");
                    }
                }
                OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
                if (orderPay == null) {
                    throw new ServiceException("订单支付信息异常！");
                }
                //修改订单支付表状态
                orderPay.setStatus(Status.ORDER_GOODS_COMPLETE.getValue());
                orderPayMapper.updateByPrimaryKey(orderPay);
                //修改订单状态
                orderInfo.setReceiveTime(DateStampUtils.getTimesteamp());
                orderInfo.setStatus(Status.ORDER_GOODS_COMPLETE.getValue());
                orderInfoMapper.updateByPrimaryKey(orderInfo);
                //卖家增加余额
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(orderInfo.getSellerId(),orderPay.getPayAmount(),Status.ADD.getValue()));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                //商品名称
                List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
                StringBuffer goodsName = new StringBuffer();
                for(OrderDetail detail:orderDetails){
                    //获取商品、抢购信息
                    baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.goodsInfo(detail.getGoodsId()));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                    GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), GoodsInfo.class);
                    goodsName.append("【"+goodsInfo.getName()+"】");
                }
                //卖家余额账单
                baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(orderInfo.getSellerId(),orderPay.getPayAmount(),Status.PAY_TYPE_BALANCE.getValue(),Status.INCOME.getValue(),orderInfo.getOrderNo(),goodsName.toString(),orderInfo.getSellerId(),orderInfo.getUserId(),Status.GOODS_INFO.getValue(),orderInfo.getOrderNo()));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                //卖家增加信誉度
                if(orderPay.getPayAmount().compareTo(new BigDecimal(2000)) <= 0){
                    String result = schedualWalletService.updateCredit(orderInfo.getSellerId(),Credit.NORMAL_ORDER.getCredit(),Status.ADD.getValue());
                    BaseResp br = ParseReturnValue.getParseReturnValue(result);
                    if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(br.getCode(),br.getReport());
                    }
                }else {
                    String result = schedualWalletService.updateCredit(orderInfo.getSellerId(), Credit.BIG_ORDER.getCredit(), Status.ADD.getValue());
                    BaseResp br = ParseReturnValue.getParseReturnValue(result);
                    if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(br.getCode(),br.getReport());
                    }
                }
                //卖家增加积分
                String result = schedualWalletService.updateScore(orderInfo.getSellerId(), Score.getScore(orderPay.getPayAmount()), Status.ADD.getValue());
                BaseResp br = ParseReturnValue.getParseReturnValue(result);
                if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(br.getCode(),br.getReport());
                }
            }
        }
    }

    @Override
    public List<CompanyDto> companyList() throws ServiceException{
        List<Company> list = companyMapper.getList();
        return CompanyDto.toDtoList(list);
    }

    @Override
    public void deleteOrder(Integer userId, Integer[] orderIds) throws ServiceException {
        for(Integer orderId:orderIds){
            OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKeyDetail(orderId);
            if (orderInfo == null) {
                throw new ServiceException("订单不存在！");
            } else {
                if(orderInfo.getStatus().intValue() == Status.ORDER_GOODS_COMPLETE.getValue() || orderInfo.getStatus().intValue() == Status.ORDER_GOODS_CANCEL.getValue()){
                    if(userId.intValue() == orderInfo.getUserId().intValue()){
                        //买家
                        orderInfo.setBuyerIsDelete(Status.YES.getValue());
                    }else if(userId.intValue() == orderInfo.getSellerId().intValue()){
                        //卖家
                        orderInfo.setSellerIsDelete(Status.YES.getValue());
                    }
                    orderInfoMapper.updateByPrimaryKey(orderInfo);
                }else{
                    throw new ServiceException("存在未完成订单，删除失败！");
                }
            }
        }
    }

    @Override
    @Transactional
    @TxTransaction(isStart=true)
    public void evaluationOrder(Integer userId, Integer orderId, Integer goodsQuality, Integer serviceAttitude) throws ServiceException {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKeyDetail(orderId);
        if (orderInfo == null) {
            throw new ServiceException("订单不存在！");
        } else {
            if(orderInfo.getUserId().intValue() != userId.intValue()){
                throw new ServiceException("没有权限评论！");
            }
            //状态 1待支付 2待发货 3待收货 4已完成 5已取消
            if (orderInfo.getStatus().intValue() == Status.ORDER_GOODS_COMPLETE.getValue()) {
                OrderComment orderComment = orderCommentMapper.selectByOrder(orderId);
                if(orderComment != null){
                    throw new ServiceException("订单已评价");
                }else{
                    orderComment = new OrderComment();
                    orderComment.setOrderId(orderId);
                    orderComment.setGoodsQuality(goodsQuality);
                    orderComment.setServiceAttitude(serviceAttitude);
                    //根据分值判断status
                    int start = goodsQuality+serviceAttitude;
                    if(start<=3){
                        //-300信誉度
                        orderComment.setStatus(Status.EVALUATION_BAD.getValue());
                        String result = schedualWalletService.updateCredit(orderInfo.getSellerId(), Credit.EVALUATION_BAD.getCredit(), Status.SUB.getValue());
                        BaseResp br = ParseReturnValue.getParseReturnValue(result);
                        if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(br.getCode(),br.getReport());
                        }
                    }else if(3 < start && start <= 6){
                        //+300信誉度
                        orderComment.setStatus(Status.EVALUATION_NORMAL.getValue());
                        String result = schedualWalletService.updateCredit(orderInfo.getSellerId(),Credit.EVALUATION_NORMAL.getCredit(),Status.ADD.getValue());
                        BaseResp br = ParseReturnValue.getParseReturnValue(result);
                        if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(br.getCode(),br.getReport());
                        }
                    }else if(6 <= start && start <= 10){
                        //+500信誉度
                        orderComment.setStatus(Status.EVALUATION_GOOD.getValue());
                        String result = schedualWalletService.updateCredit(orderInfo.getSellerId(),Credit.EVALUATION_GOOD.getCredit(),Status.ADD.getValue());
                        BaseResp br = ParseReturnValue.getParseReturnValue(result);
                        if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(br.getCode(),br.getReport());
                        }
                    }else{
                        throw new ServiceException("分值错误！");
                    }
                    orderComment.setAddTime(DateStampUtils.getTimesteamp());
                    orderCommentMapper.insert(orderComment);
                }
            }else{
                throw new ServiceException("订单当前状态无法评价！");
            }
        }
    }

    @Override
    public void reminder(Integer userId, Integer orderId) throws ServiceException {
        OrderInfo order = orderInfoMapper.getOrderByUserIdOrderId(orderId, userId);
        if(order != null && order.getStatus() == Status.ORDER_GOODS_PAY.getValue()){
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());

            //给卖家发送信息 您的商品【商品名称】、【xxx】、【xx】买家提醒您发货，点击此处查看订单
            //获取商品名字列表
            StringBuffer goodsName = new StringBuffer();
            boolean isAuction = false;
            for(OrderDetail detail:orderDetails){
                GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(detail.getGoodsId())).getString("data")), GoodsInfo.class);
                isAuction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
                goodsName.append("【"+goodsInfo.getName()+"】");
            }
            schedualMessageService.easemobMessage(order.getSellerId().toString(),
                    "您的"+(isAuction?"抢购":"商品")+goodsName+"买家提醒您发货，点击此处查看订单",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_ORDER_SELLER.getMessage(),orderId.toString());
        }else{
            throw new ServiceException("订单状态错误！");
        }
    }

    @Override
    @Transactional
    @TxTransaction(isStart=true)
    public boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException {
        OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(orderNo);
        if(orderInfo == null){
            throw new ServiceException("订单不存在！");
        }else{
            if(orderInfo.getStatus() != Status.ORDER_GOODS_PREPAY.getValue()){
                throw new ServiceException("订单状态错误！");
            }else{
                OrderPay orderPay = orderPayMapper.selectByOrderId(orderInfo.getId());
                //拆单
                if(orderInfo.getSellerId() == null){//订单为合并主订单，进行拆单
                    orderInfo.setIsResolve(Status.YES.getValue());//是否拆单 1是 2否
                    //获取子订单
                    List<OrderInfo> orderInfos = orderInfoMapper.selectChildOrderByOrderId(orderInfo.getUserId(), orderInfo.getId());
                    for(OrderInfo childOrder:orderInfos){
                        childOrder.setIsResolve(Status.NO.getValue());
                        childOrder.setStatus(Status.ORDER_GOODS_PAY.getValue());
                        orderInfoMapper.updateByPrimaryKey(childOrder);
                        OrderPay pay = orderPayMapper.selectByOrderId(childOrder.getId());
                        pay.setPayType(payType);
                        pay.setPayTime(DateStampUtils.getTimesteamp());
                        pay.setStatus(Status.ORDER_GOODS_PAY.getValue());
                        pay.setPayNo(thirdOrderNo);
                        orderPayMapper.updateByPrimaryKey(pay);

                        //获取商品名字列表
                        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(childOrder.getId());
                        StringBuffer goodsName = new StringBuffer();
                        boolean isAuction = false;
                        for(OrderDetail detail:orderDetails){
                            BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualGoodsService.goodsInfo(detail.getGoodsId()));
                            if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                                throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                            }
                            GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), GoodsInfo.class);
                            isAuction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
                            goodsName.append("【"+goodsInfo.getName()+"】");
                        }
                        //交易信息：恭喜您！您的商品【xxx】已被买下，点击此处查看订单
                        //交易信息：恭喜您！您的抢购【xxx】已被买下，点击此处查看订单
                        schedualMessageService.easemobMessage(childOrder.getSellerId().toString(),
                                "恭喜您！您的"+(isAuction?"抢购":"商品")+goodsName+"已被买下，点击此处查看订单",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_ORDER_SELLER.getMessage(),childOrder.getId().toString());
                        //买家新增余额账单
                        BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(childOrder.getUserId(),pay.getPayAmount(),payType,Status.EXPEND.getValue(),childOrder.getOrderNo(),goodsName.toString(),childOrder.getSellerId(),childOrder.getUserId(),Status.GOODS_INFO.getValue(),thirdOrderNo));
                        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                        }
                    }
                }else{
                    //获取商品名字列表
                    List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderInfo.getId());
                    StringBuffer goodsName = new StringBuffer();
                    boolean isAuction = false;
                    for(OrderDetail detail:orderDetails){
                        GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(detail.getGoodsId())).getString("data")), GoodsInfo.class);
                        isAuction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
                        goodsName.append("【"+goodsInfo.getName()+"】");
                    }
                    //交易信息：恭喜您！您的抢购【xxx】已被买下，点击此处查看订单
                    schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                            "恭喜您！您的"+(isAuction?"抢购":"商品")+goodsName+"已被买下，点击此处查看订单",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_ORDER_SELLER.getMessage(),orderInfo.getId().toString());
                    //买家新增余额账单
                    BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(orderInfo.getUserId(),orderPay.getPayAmount(),payType,Status.EXPEND.getValue(),orderInfo.getOrderNo(),goodsName.toString(),orderInfo.getSellerId(),orderInfo.getUserId(),Status.GOODS_INFO.getValue(),thirdOrderNo));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                }
                orderInfo.setStatus(Status.ORDER_GOODS_PAY.getValue());
                orderInfoMapper.updateByPrimaryKeySelective(orderInfo);

                orderPay.setPayType(payType);
                orderPay.setPayTime(DateStampUtils.getTimesteamp());
                orderPay.setStatus(Status.ORDER_GOODS_PAY.getValue());
                orderPay.setPayNo(thirdOrderNo);
                orderPayMapper.updateByPrimaryKeySelective(orderPay);


                return true;
            }
        }
    }

    @Override
    public Pager orderList(AdminOrderParam param) throws ServiceException {
        //后台查看所有用户订单
        Integer total = orderInfoMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

        List<OrderInfo> list = orderInfoMapper.getOrderPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        List<AdminOrderDto> orderDtos = new ArrayList<>();
        for(OrderInfo info:list){
            AdminOrderDto orderDto = new AdminOrderDto(info);
            //获取订单详情列表
            //如果是没有拆单的订单根据主订单获取，拆了单的根据订单id获取
            List<OrderDetail> orderDetails;
            if(info.getSellerId() == null){
                orderDetails = orderDetailMapper.selectByMainOrderId(orderDto.getOrderId());
            }else{
                orderDetails = orderDetailMapper.selectByOrderId(orderDto.getOrderId());
            }
            ArrayList<AdminOrderDetailDto> orderDetailDtos = AdminOrderDetailDto.toDtoList(orderDetails);
            String orderDetail = "";
            for(AdminOrderDetailDto detail:orderDetailDtos) {
            	orderDetail += "卖家："+detail.getNickName()+" - "+detail.getPhone()+"，商品："+detail.getGoodsName() + "<br>";
            }
            orderDto.setOrderDetail(orderDetail);
            orderDto.setTotalCount(orderDetailDtos.size());

            orderDtos.add(orderDto);
        }
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(orderDtos);
        return pager;
    }
    
    @Override
    public AdminOrderDto adminOrderDetail(Integer orderId) throws ServiceException{
    	OrderInfo info = orderInfoMapper.selectByOrderId(orderId);
    	if(info==null) {
    		throw new ServiceException("找不到订单");
    	}
    	AdminOrderDto orderDto = new AdminOrderDto(info);
        //获取订单详情列表
        //如果是没有拆单的订单根据主订单获取，拆了单的根据订单id获取
        List<OrderDetail> orderDetails;
        if(info.getSellerId() == null){
            orderDetails = orderDetailMapper.selectByMainOrderId(orderDto.getOrderId());
            //orderDto.setSeller("多卖家");
        }else{
            orderDetails = orderDetailMapper.selectByOrderId(orderDto.getOrderId());
        }
        ArrayList<AdminOrderDetailDto> orderDetailDtos = AdminOrderDetailDto.toDtoList(orderDetails);
        String orderDetail = "";
        for(AdminOrderDetailDto detail:orderDetailDtos) {
        	orderDetail += "卖家："+detail.getNickName()+" - "+detail.getPhone()+"，商品："+detail.getGoodsName() + "\n";
        }
        orderDto.setOrderDetail(orderDetail);
        orderDto.setTotalCount(orderDetailDtos.size());
         
        //卖家信息DTO        // List sellerDtos = new ArrayList<>();
        //sellerDtos.addAll(getSellerDtos(OrderDetailDto.toDtoList(orderDetails)));
        //订单支付表
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderDto.getOrderId());
        AdminOrderPayDto orderPayDto = new AdminOrderPayDto(orderPay);
        orderDto.setOrderPayDto(orderPayDto);
        //orderDto.setSellerDtos(sellerDtos);
        orderDto.setOrderDetailDtos(orderDetailDtos);
        if(orderDto.getIsRefund() == 1){
            //退货状态
            OrderRefund orderRefund = orderRefundMapper.selectByOrderIdStatus(orderDto.getOrderId(), null,null);
            if(orderRefund != null){
                orderDto.setReturnStatus(orderRefund.getStatus());
                orderDto.setSellerReturnStatus(orderRefund.getSellerReturnStatus());
            }
        }
        return orderDto;
    }
    
    @Override
    public Pager simpleOrderList(AdminOrderParam param) throws ServiceException {
        //后台查看所有用户订单
        Integer total = orderInfoMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

        List<OrderInfo> list = orderInfoMapper.getOrderPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());

        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(AdminOrderDto.toDtoList(list));
        return pager;
    }

    @Override
    public Pager companyList(AdminOrderParam param) throws ServiceException {
        List<Company> list = companyMapper.getPage(param.getStart(), param.getLimit(), param.getKeyword(), param.getStatus(), param.getOrders(), param.getAscType());
        Integer total = companyMapper.countPage(param.getKeyword(), param.getStatus());
        List<AdminCompanyDto> adminCompanyDtos = AdminCompanyDto.toDtoList(list);
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(adminCompanyDtos);
        return pager;
    }

    @Override
    public void addCompany(String number, String name, BigDecimal price) throws ServiceException {
        Company company = new Company();
        company.setCompanyNo(number);
        company.setName(name);
        company.setPrice(price);
        company.setAddTime(DateStampUtils.getTimesteamp());
        company.setStatus(1);
        companyMapper.insert(company);
    }

    @Override
    public void updateCompany(Integer id, String number, String name, BigDecimal price, Integer status) throws ServiceException {
        Company company = companyMapper.selectByPrimaryKey(id);
        if(company == null){
            throw new ServiceException("没找到物流公司！");
        }
        if(StringUtils.isNotEmpty(number)){
            company.setCompanyNo(number);
        }
        if(StringUtils.isNotEmpty(name)){
            company.setName(name);
        }
        if(price != null){
            company.setPrice(price);
        }
        if(status != null){
            company.setStatus(status);
        }
        companyMapper.updateByPrimaryKey(company);
    }

    @Override
    public AdminOrderProcessDto getOrderProcess(Integer status, String startDate, String endDate) throws ServiceException {

        AdminOrderProcessDto dto = new AdminOrderProcessDto();
        //TODO 从redis中获取统计数据
        return null;
    }


    @Override
    public Integer processTodayOrder(Integer status) throws ServiceException {
        Integer todayOrderCount = orderInfoMapper.getTodayOrderCount(status);
        return todayOrderCount;
    }

    @Override
    public Integer processAllOrder(Integer status) throws ServiceException {
        Integer allOrderCount = orderInfoMapper.getAllOrderCount(status);
        return allOrderCount;
    }

}
