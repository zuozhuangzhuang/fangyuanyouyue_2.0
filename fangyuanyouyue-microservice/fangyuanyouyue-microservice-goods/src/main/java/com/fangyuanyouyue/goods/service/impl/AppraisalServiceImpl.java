package com.fangyuanyouyue.goods.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.goods.dao.*;
import com.fangyuanyouyue.goods.dto.OrderDetailDto;
import com.fangyuanyouyue.goods.dto.OrderDto;
import com.fangyuanyouyue.goods.dto.OrderPayDto;
import com.fangyuanyouyue.goods.model.*;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service(value = "appraisalService")
@Transactional(rollbackFor=Exception.class)
public class AppraisalServiceImpl implements AppraisalService{
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private GoodsCorrelationMapper goodsCorrelationMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Autowired
    private GoodsCommentMapper goodsCommentMapperl;
    @Autowired
    private HotSearchMapper hotSearchMapper;
    @Autowired
    private BannerIndexMapper bannerIndexMapper;
    @Autowired
    private GoodsQuickSearchMapper goodsQuickSearchMapper;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private GoodsAppraisalMapper goodsAppraisalMapper;
    @Autowired
    private GoodsAppraisalDetailMapper goodsAppraisalDetailMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderPayMapper orderPayMapper;

    @Override
    public OrderDto addAppraisal(Integer userId, Integer[] goodsIds, String title, String description,String imgUrl,String videoUrl) throws ServiceException {
        //用户信息
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
        //可能提交多个商品鉴定
        GoodsAppraisal goodsAppraisal = new GoodsAppraisal();
        goodsAppraisal.setAddTime(DateStampUtils.getTimesteamp());
        goodsAppraisal.setUserId(userId);
        goodsAppraisalMapper.insert(goodsAppraisal);
        //生成订单
        //每次提交的鉴定生成一个订单，批量鉴定有多个订单详情
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
        //鉴定订单没有收货地址信息
        orderPay.setOrderNo(orderInfo.getOrderNo());
        orderPay.setAmount(amount);
        orderPay.setPayAmount(payAmount);
        orderPay.setFreight(freight);//运费金额，初始化为0
        orderPay.setCount(goodsIds.length);
        orderPay.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
        orderPay.setAddTime(DateStampUtils.getTimesteamp());
        orderPayMapper.insert(orderPay);
        //用来存放订单详情DTO列表
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();

        if(goodsIds != null || goodsIds.length != 0){//用户对商品提交鉴定
            for(Integer goodsId:goodsIds){
                GoodsAppraisalDetail goodsAppraisalDetail = goodsAppraisalDetailMapper.selectByUserIdGoodsId(userId, goodsId);
                if(goodsAppraisalDetail != null){
                    throw new ServiceException("您已申请过鉴定！");
                }else{
                    GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
                    if(goodsInfo == null){
                        throw new ServiceException("鉴定列表中包含不存在或已下架商品！");
                    }else{
                        //生成鉴定和订单
                        goodsAppraisalDetail = new GoodsAppraisalDetail();
                        goodsAppraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
                        goodsAppraisalDetail.setAppraisalId(goodsAppraisal.getId());
                        goodsAppraisalDetail.setGoodsId(goodsId);
                        goodsAppraisalDetail.setStatus(0);//状态 0申请 1真 2假 3存疑
                        goodsAppraisalDetail.setTitle(title);
                        goodsAppraisalDetail.setDescription(description);
                        //TODO 根据鉴定费算法
                        BigDecimal price = new BigDecimal(10);
                        goodsAppraisalDetail.setPrice(price);
                        if(goodsInfo.getUserId() == userId){
                            goodsAppraisalDetail.setType(1);//鉴定类型 1商家鉴定 2买家 3普通用户
                            goodsInfo.setIsAppraisal(2);//是否鉴定 1未鉴定 2已鉴定
                        }else{
                            goodsAppraisalDetail.setType(2);
                        }
                        goodsAppraisalDetailMapper.insert(goodsAppraisalDetail);
                        //生成鉴定信息同时生成订单

                        //计算总订单总金额
                        //每个商品生成一个订单详情表
                        OrderDetail orderDetail = new OrderDetail();
                        //买家ID
                        orderDetail.setUserId(userId);
                        //卖家ID
                        orderDetail.setSellerId(goodsInfo.getUserId());
                        orderDetail.setOrderId(orderInfo.getId());
                        orderDetail.setGoodsId(goodsId);
                        orderDetail.setGoodsName(goodsInfo.getName());
                        orderDetail.setAddTime(DateStampUtils.getTimesteamp());
                        //商品主图
                        List<GoodsImg> imgsByGoodsId = goodsImgMapper.getImgsByGoodsId(goodsId);
                        if(imgsByGoodsId != null){
                            for(GoodsImg goodsImg:imgsByGoodsId){
                                if(goodsImg.getType() == 1){
                                    orderDetail.setMainImgUrl(goodsImg.getImgUrl());
                                }
                            }
                        }
                        orderDetail.setPrice(price);
                        //鉴定订单没有优惠
                        orderDetail.setOrgprice(price);
                        //鉴定订单没有邮费
                        orderDetail.setFreight(new BigDecimal(0));
                        orderDetail.setDescription(goodsInfo.getDescription());
                        orderDetailMapper.insert(orderDetail);
                        //订单详情DTO
                        OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail,1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                        orderDetailDtos.add(orderDetailDto);

                        amount = amount.add(price);//实际支付
                        payAmount = payAmount.add(orderDetailDto.getPrice());//原价
                    }
                }
            }
        }else{//用户提交图片或视频进行鉴定
            GoodsAppraisalDetail goodsAppraisalDetail = new GoodsAppraisalDetail();
            goodsAppraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
            goodsAppraisalDetail.setAppraisalId(goodsAppraisal.getId());
            goodsAppraisalDetail.setStatus(0);//状态 0申请 1真 2假 3存疑
            goodsAppraisalDetail.setTitle(title);
            goodsAppraisalDetail.setType(3);//鉴定类型 1商家鉴定 2买家 3普通用户
            goodsAppraisalDetail.setDescription(description);
            //TODO 根据鉴定费算法
            BigDecimal price = new BigDecimal(10);
            goodsAppraisalDetail.setPrice(price);
            goodsAppraisalDetailMapper.insert(goodsAppraisalDetail);

            //生成鉴定信息同时生成订单

            //计算总订单总金额
            //每个商品生成一个订单详情表
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderInfo.getId());
            //买家用户
            orderDetail.setUserId(userId);
            //自己上传鉴定没有卖家ID、商品信息
//            orderDetail.setSellerId();
//            orderDetail.setGoodsId(goodsId);
//            orderDetail.setGoodsName(goodsInfo.getName());
            orderDetail.setAddTime(DateStampUtils.getTimesteamp());
            orderDetail.setPrice(price);
            //鉴定订单没有优惠
            orderDetail.setOrgprice(price);
            //鉴定订单没有邮费
            orderDetail.setFreight(new BigDecimal(0));
            orderDetail.setDescription(description);
            orderDetailMapper.insert(orderDetail);
            //订单详情DTO
            OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail,1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
            orderDetailDtos.add(orderDetailDto);

            amount = amount.add(orderDetailDto.getOrgPrice());//实际支付
            payAmount = payAmount.add(orderDetailDto.getPrice());//原价
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
        //用户需要支付对应的资金才可以鉴定成功
    }
}
