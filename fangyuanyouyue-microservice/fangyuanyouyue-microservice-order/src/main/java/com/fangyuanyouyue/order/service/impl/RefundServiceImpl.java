package com.fangyuanyouyue.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.Credit;
import com.fangyuanyouyue.base.enums.MiniMsg;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.order.dao.*;
import com.fangyuanyouyue.order.dto.*;
import com.fangyuanyouyue.order.dto.adminDto.AdminCompanyDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderDetailDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderRefundDto;
import com.fangyuanyouyue.order.model.*;
import com.fangyuanyouyue.order.param.AdminOrderParam;
import com.fangyuanyouyue.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    @Transactional(rollbackFor=Exception.class)
    @TxTransaction(isStart=true)
    public void orderReturnToSeller(Integer userId, Integer orderId, String reason,String[] imgUrls) throws ServiceException {
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
            //状态 1待支付 2待发货 3待收货 4已完成 5已取消
            if (orderInfo.getStatus().intValue() == Status.ORDER_GOODS_PAY.getValue() || orderInfo.getStatus().intValue() == Status.ORDER_GOODS_SENDED.getValue()) {
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
                orderRefund.setReturnType(orderInfo.getStatus().equals(Status.ORDER_GOODS_PAY.getValue())?1:2);
                orderRefundMapper.insert(orderRefund);
                orderInfo.setIsRefund(Status.YES.getValue());//是否退货 1是 2否
                orderInfoMapper.updateByPrimaryKey(orderInfo);
                //扣除信誉度
                String result = schedualWalletService.updateCredit(orderInfo.getSellerId(), Credit.RETURN_ORDER.getCredit(), Status.SUB.getValue());
                BaseResp br = ParseReturnValue.getParseReturnValue(result);
                if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(br.getCode(),br.getReport());
                }
                //环信给卖家发送信息 退货：您的商品【商品名称】、【xxx】、【xx】买家已申请退货，点击此处处理一下吧
                List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
                StringBuffer goodsName = new StringBuffer();
                boolean isAuction = false;
                for(OrderDetail detail:orderDetails){
                    //获取商品、抢购信息
                    GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(detail.getGoodsId())).getString("data")), GoodsInfo.class);
                    goodsName.append("【"+goodsInfo.getName()+"】");
                    isAuction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
                }
                schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                        "您的"+(isAuction?"抢购":"商品")+goodsName+"买家已申请退货，点击此处处理一下吧",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_ORDER_SELLER.getMessage(),orderId.toString());

                //发送微信消息
                //formId
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getFormId(orderInfo.getSellerId()));
                if(baseResp !=null && !baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                if(baseResp.getData() != null){
                    String formId = baseResp.getData().toString();
                    //openId
                    baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getOpenId(orderInfo.getSellerId()));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                    if(baseResp.getData() != null){
                        String openId = baseResp.getData().toString();
                        baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(orderInfo.getUserId()));
                        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                        }
                        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(baseResp.getData().toString()), UserInfo.class);
                        Map<String,Object> map = new HashMap<>();
                        map.put("keyword1",user.getNickName());
                        map.put("keyword2",goodsName.toString());
                        OrderPay orderPay = orderPayMapper.selectByOrderId(orderId);
                        map.put("keyword3","￥"+orderPay.getPayAmount());
                        map.put("keyword4",DateUtil.getFormatDate(orderRefund.getAddTime(), DateUtil.DATE_FORMT));
                        JSONArray jsonArray = JSONArray.parseArray(reason);
                        StringBuffer content = new StringBuffer();
                        for(int i=0;i<jsonArray.size();i++){
                            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
                            if(jsonObject.getString("type").equals("0")){
                                content.append(jsonObject.getString("content"));
                            }
                        }
                        map.put("keyword5",content.toString());

                        schedualMessageService.wechatMessage(openId, MiniMsg.ORDER_REFUND.getTemplateId(),MiniMsg.ORDER_REFUND.getPagePath()+orderInfo.getId(),map,formId);
                    }
                }
            }else{
                throw new ServiceException("订单无法退货！");
            }
        }
    }


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
    @Transactional(rollbackFor=Exception.class)
    @TxTransaction(isStart=true)
    public void platformDealReturns(Integer userId, Integer orderId, String reason, Integer status) throws ServiceException {
        OrderRefund orderRefund = orderRefundMapper.selectByPrimaryKey(orderId);
        if(orderRefund == null){
            throw new ServiceException("获取退货信息失败！");
        }
        //1、订单状态修改 2、退货状态修改 3、发送信息给买家卖家
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderRefund.getOrderId());
        OrderPay orderPay = orderPayMapper.selectByOrderId(orderRefund.getOrderId());
        if (orderInfo == null) {
            throw new ServiceException("订单不存在！");
        } else {
            orderRefund.setPlatformReason(reason);//处理理由
            orderRefund.setEndTime(DateStampUtils.getTimesteamp());
            orderRefund.setStatus(status);
            //获取发信息的商品名【xx】
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderRefund.getOrderId());
            StringBuffer goodsName = new StringBuffer();
            boolean isAuction = false;
            for(OrderDetail detail:orderDetails){
                //获取商品、抢购信息
                GoodsInfo goodsInfo = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualGoodsService.goodsInfo(detail.getGoodsId())).getString("data")), GoodsInfo.class);
                goodsName.append("【"+goodsInfo.getName()+"】");
                isAuction = goodsInfo.getType().intValue() == Status.AUCTION.getValue()?true:false;
            }
            if(status.intValue() == 2){
                //同意
                //修改余额
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(orderInfo.getUserId(),orderPay.getPayAmount(),Status.ADD.getValue()));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                orderInfo.setStatus(Status.ORDER_GOODS_COMPLETE.getValue());
                orderPay.setStatus(Status.ORDER_GOODS_COMPLETE.getValue());
                orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                orderPayMapper.updateByPrimaryKeySelective(orderPay);
                //买家新增余额账单

                baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(orderInfo.getUserId(),orderPay.getPayAmount(),Status.PAY_TYPE_BALANCE.getValue(),
                        Status.REFUND.getValue(),orderInfo.getOrderNo(),goodsName.toString()+"退款成功",orderInfo.getSellerId(),orderInfo.getUserId(),Status.GOODS_INFO.getValue(),orderInfo.getOrderNo()));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                //给买家发信息
                schedualMessageService.easemobMessage(orderInfo.getUserId().toString(),
                        "您对"+(isAuction?"抢购":"商品")+goodsName+"申请的退货卖家已同意，货款已退回您的余额。点击此处查看您的余额吧",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_WALLET.getMessage(),"");
                //发送微信消息
                //formId
                baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getFormId(orderInfo.getUserId()));
                if(baseResp !=null && !baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                if(baseResp.getData() != null){
                    String formId = baseResp.getData().toString();
                    //openId
                    baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getOpenId(orderInfo.getUserId()));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                    if(baseResp.getData() != null){
                        String openId = baseResp.getData().toString();
                        Map<String,Object> map = new HashMap<>();
                        map.put("keyword1",goodsName.toString());
                        map.put("keyword2", orderPay.getPayAmount()+"元");
                        map.put("keyword3",DateUtil.getFormatDate(orderRefund.getEndTime(), DateUtil.DATE_FORMT));

                        schedualMessageService.wechatMessage(openId, MiniMsg.REFUND_SUCCESS.getTemplateId(),MiniMsg.REFUND_SUCCESS.getPagePath(),map,formId);
                    }
                }
                //给卖家发信息
                schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                        "买家申请退货的"+(isAuction?"抢购":"商品")+goodsName+"官方已同意，退款已退回买家余额。如有疑问可联系客服咨询详情",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),orderInfo.getId().toString());
                //发送微信消息
                //formId
                baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getFormId(orderInfo.getSellerId()));
                if(baseResp !=null && !baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                if(baseResp.getData() != null){
                    String formId = baseResp.getData().toString();
                    //openId
                    baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getOpenId(orderInfo.getSellerId()));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                    if(baseResp.getData() != null){
                        String openId = baseResp.getData().toString();
                        Map<String,Object> map = new HashMap<>();
                        map.put("keyword1",goodsName.toString());
                        map.put("keyword2", orderPay.getPayAmount()+"元");
                        map.put("keyword3",DateUtil.getFormatDate(orderRefund.getEndTime(), DateUtil.DATE_FORMT));

                        schedualMessageService.wechatMessage(openId, MiniMsg.REFUND_SUCCESS.getTemplateId(),MiniMsg.REFUND_SUCCESS.getPagePath(),map,formId);
                    }
                }
            }else{
                //拒绝
                //订单状态不变
                //给买家发信息
                schedualMessageService.easemobMessage(orderInfo.getUserId().toString(),
                        "很抱歉，您对"+(isAuction?"抢购":"商品")+goodsName+"申请的退货，官方已拒绝",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),orderInfo.getId().toString());
                //发送微信消息
                //formId
                BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getFormId(orderInfo.getUserId()));
                if(baseResp !=null && !baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                if(baseResp.getData() != null){
                    String formId = baseResp.getData().toString();
                    //openId
                    baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getOpenId(orderInfo.getUserId()));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                    if(baseResp.getData() != null){
                        String openId = baseResp.getData().toString();
                        Map<String,Object> map = new HashMap<>();
                        map.put("keyword1",goodsName.toString());
                        map.put("keyword2", orderPay.getPayAmount()+"元");
                        map.put("keyword3", reason);
                        map.put("keyword4",DateUtil.getFormatDate(orderRefund.getEndTime(), DateUtil.DATE_FORMT));

                        schedualMessageService.wechatMessage(openId, MiniMsg.REFUND_FAILED.getTemplateId(),MiniMsg.REFUND_FAILED.getPagePath(),map,formId);
                    }
                }
                //给卖家发信息
                schedualMessageService.easemobMessage(orderInfo.getSellerId().toString(),
                        "买家申请退货的"+(isAuction?"抢购":"商品")+goodsName+"官方已拒绝，拒绝理由为："+reason+"。如有疑问可联系客服咨询详情",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),orderInfo.getId().toString());
                //发送微信消息
                //formId
                baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getFormId(orderInfo.getSellerId()));
                if(baseResp !=null && !baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                if(baseResp.getData() != null){
                    String formId = baseResp.getData().toString();
                    //openId
                    baseResp = ParseReturnValue.getParseReturnValue(schedualUserService.getOpenId(orderInfo.getSellerId()));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                    if(baseResp.getData() != null){
                        String openId = baseResp.getData().toString();
                        Map<String,Object> map = new HashMap<>();
                        map.put("keyword1",goodsName.toString());
                        map.put("keyword2", orderPay.getPayAmount()+"元");
                        map.put("keyword3", reason);
                        map.put("keyword4",DateUtil.getFormatDate(orderRefund.getEndTime(), DateUtil.DATE_FORMT));

                        schedualMessageService.wechatMessage(openId, MiniMsg.REFUND_FAILED.getTemplateId(),MiniMsg.REFUND_FAILED.getPagePath(),map,formId);
                    }
                }
            }
            orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
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
    

    @Override
    public Pager refundList(AdminOrderParam param) throws ServiceException {
        List<OrderRefund> list = orderRefundMapper.getPage(param.getStart(), param.getLimit(), param.getKeyword(), param.getStatus(), param.getStartDate(), param.getEndDate(), param.getOrders(),param.getAscType());
        Integer total = orderRefundMapper.countPage(param.getKeyword(), param.getStatus(), param.getStartDate(), param.getEndDate());
        List<AdminOrderRefundDto> dtos = new ArrayList<AdminOrderRefundDto>();
        for(OrderRefund refund:list) {
        	AdminOrderRefundDto dto = new AdminOrderRefundDto(refund);
        	OrderInfo info = orderInfoMapper.selectByOrderId(dto.getOrderId());
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
            	orderDetail += "卖家："+detail.getNickName()+" - "+detail.getPhone()+"，商品："+detail.getGoodsName() + "<br>";
            }
            orderDto.setOrderDetail(orderDetail);
            orderDto.setTotalCount(orderDetailDtos.size());
            
            dto.setOrderInfo(orderDto);
        	
        	dtos.add(dto);
        }
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(dtos);
        return pager;
    }
    
}
