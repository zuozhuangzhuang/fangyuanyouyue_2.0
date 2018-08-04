package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.goods.dao.*;
import com.fangyuanyouyue.goods.dto.BargainDto;
import com.fangyuanyouyue.goods.model.*;
import com.fangyuanyouyue.goods.service.BargainService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service(value = "bargainService")
@Transactional(rollbackFor=Exception.class)
public class BargainServiceImpl implements BargainService{
    @Autowired
    private GoodsBargainMapper goodsBargainMapper;
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderPayMapper orderPayMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private UserAddressInfoMapper userAddressInfoMapper;

    @Override
    public void addBargain(Integer userId, Integer goodsId, BigDecimal price, String reason,Integer addressId) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        if(goodsInfo == null){
            throw new ServiceException("商品不存在！");
        }else{
            if(goodsInfo.getStatus().intValue() != 1){
                throw new ServiceException("商品已售出或已下架！");
            }
            if(goodsInfo.getType().intValue() == 2){
                throw new ServiceException("抢购无法压价！");
            }
            //压价金额不可以高于原价，否则岂不是显得客户很傻
            if(price.compareTo(goodsInfo.getPrice())>=0){
                throw new ServiceException("压价不得低于原价！");
            }
            //如果用户已经存在申请中的压价，不能压第二次
            GoodsBargain goodsBargain = goodsBargainMapper.selectByUserIdGoodsId(userId, goodsId);
            if(goodsBargain != null){
                throw new ServiceException("此商品您已压价！");
            }else{
                goodsBargain = new GoodsBargain();
                goodsBargain.setUserId(userId);
                goodsBargain.setGoodsId(goodsId);
                goodsBargain.setPrice(price);
                goodsBargain.setAddressId(addressId);
                if(StringUtils.isNotEmpty(reason)){
                    goodsBargain.setReason(reason);
                }
                goodsBargain.setStatus(1);//状态 1申请 2同意 3拒绝
                goodsBargain.setAddTime(DateStampUtils.getTimesteamp());
                goodsBargainMapper.insert(goodsBargain);
                //压价时扣除用户余额，如果余额不足就不可以议价
                //TODO 扣除用户余额，调用钱包系统
                //TODO 压价时环信发送信息到店家
            }
        }
    }

    @Override
    public void updateBargain(Integer userId, Integer goodsId,Integer bargainId,Integer status) throws ServiceException {
        GoodsBargain goodsBargain = goodsBargainMapper.selectByPrimaryKey(bargainId);
        //查询用户正在申请的压价
        //1.买家取消压价 2.卖家同意压价 3.卖家拒绝压价
        if(goodsBargain == null){
            throw new ServiceException("压价信息异常！");
        }else{
            //判断商品状态
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            if(goodsInfo == null){
                throw new ServiceException("商品不存在！");
            }
            if(goodsInfo.getStatus().intValue() != 1){
                throw new ServiceException("商品已售出或已下架！");
            }
            //判断压价状态
            if(goodsBargain.getStatus().intValue() == 2){
                throw new ServiceException("已同意压价！");
            }
            if(goodsBargain.getStatus().intValue() == 3){
                throw new ServiceException("已拒绝压价！");
            }
            if(goodsInfo.getUserId().intValue() == userId.intValue()){
                //卖家处理压价
                if(status == 4){
                    throw new ServiceException("卖家不可取消议价！");
                }
                //TODO 卖家同意后生成订单，拒绝后退回余额
                if(status.intValue() == 2){//状态 2同意 3拒绝 4取消
                    //生成订单
                    //每次提交的鉴定生成一个订单，批量鉴定有多个订单详情
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setUserId(goodsBargain.getUserId());
                    //订单号
                    final IdGenerator idg = IdGenerator.INSTANCE;
                    String id = idg.nextId();
                    orderInfo.setOrderNo("1"+id);

                    orderInfo.setAmount(goodsInfo.getPrice());//原价
                    orderInfo.setCount(1);
                    orderInfo.setStatus(1);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                    orderInfo.setAddTime(DateStampUtils.getTimesteamp());
                    orderInfoMapper.insert(orderInfo);
                    //生成订单支付表
                    UserAddressInfo addressInfo = userAddressInfoMapper.selectByPrimaryKey(goodsBargain.getAddressId());
                    OrderPay orderPay = new OrderPay();
                    orderPay.setOrderId(orderInfo.getId());
                    //地址信息
                    orderPay.setReceiverName(addressInfo.getReceiverName());
                    orderPay.setReceiverPhone(addressInfo.getReceiverPhone());
                    orderPay.setProvince(addressInfo.getProvince());
                    orderPay.setCity(addressInfo.getCity());
                    orderPay.setArea(addressInfo.getArea());
                    orderPay.setAddress(addressInfo.getAddress());
                    orderPay.setPostCode(addressInfo.getPostCode());
                    orderPay.setOrderNo(orderInfo.getOrderNo());
                    orderPay.setAmount(goodsInfo.getPrice().add(goodsInfo.getPostage()));//原价
                    orderPay.setPayAmount(goodsBargain.getPrice());//实际支付金额
                    orderPay.setFreight(new BigDecimal(0));//运费金额(议价默认为0)
                    orderPay.setCount(1);
                    //压价下单默认已支付
                    orderPay.setStatus(2);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                    orderPay.setAddTime(DateStampUtils.getTimesteamp());
                    orderPayMapper.insert(orderPay);

                    if (goodsInfo.getStatus() != 1) {//非出售中抢购
                        throw new ServiceException("抢购已售出！");
                    }
                    //每个商品生成一个订单详情表
                    OrderDetail orderDetail = new OrderDetail();
                    //买家ID
                    orderDetail.setUserId(goodsBargain.getUserId());
                    //卖家ID
                    orderDetail.setSellerId(userId);
                    orderDetail.setOrderId(orderInfo.getId());
                    orderDetail.setGoodsId(goodsId);
                    orderDetail.setGoodsName(goodsInfo.getName());
                    orderDetail.setAddTime(DateStampUtils.getTimesteamp());
                    //商品主图
                    StringBuffer goodsMainImg = new StringBuffer();
                    List<GoodsImg> imgsByGoodsId = goodsImgMapper.getImgsByGoodsId(goodsId);
                    for(GoodsImg goodsImg:imgsByGoodsId){
                        if(goodsImg.getType() == 1){//主图
                            goodsMainImg.append(goodsImg.getImgUrl());
                        }
                    }
                    orderDetail.setMainImgUrl(goodsMainImg.toString());
                    orderDetail.setAmount(goodsInfo.getPrice().add(goodsInfo.getPostage()));
                    //邮费为0
                    orderDetail.setFreight(new BigDecimal(0));
                    //实际支付
                    orderDetail.setPayAmount(goodsBargain.getPrice());
                    orderDetail.setDescription(goodsInfo.getDescription());
                    orderDetailMapper.insert(orderDetail);
                    //修改商品的状态为已售出
                    goodsInfo.setStatus(2);//状态  1出售中 2已售出 5删除
                    goodsInfoMapper.updateByPrimaryKey(goodsInfo);
                    //如果卖家同意议价，就拒绝此商品剩余的申请中议价
                    List<GoodsBargain> goodsBargains = goodsBargainMapper.selectAllByGoodsId(goodsId);
                    for(GoodsBargain bargain:goodsBargains){
                        bargain.setStatus(3);
                        goodsBargainMapper.updateByPrimaryKey(bargain);
                    }
                }else if(status.intValue() == 3){
                    //TODO 退回余额
                }else{
                    throw new ServiceException("状态异常！");
                }
            }else if(goodsBargain.getUserId().intValue() == userId.intValue()){
                //买家取消压价
                if(status.intValue() == 2 || status.intValue() == 3){
                    throw new ServiceException("买家不可同意或拒绝议价！");
                }
            }else{
                throw new ServiceException("压价信息异常！");
            }
            goodsBargain.setStatus(status);//状态 2同意 3拒绝 4取消
            goodsBargainMapper.updateByPrimaryKey(goodsBargain);
        }
    }

    @Override
    public List<BargainDto> bargainList(Integer userId,Integer start,Integer limit) throws ServiceException {
        List<GoodsBargain> goodsBargains = goodsBargainMapper.selectAllByUserId(userId,start*limit,limit);
        if(goodsBargains == null){
            throw new ServiceException("获取压价列表失败！");
        }
        return BargainDto.toDtoList(goodsBargains);
    }

    @Override
    public GoodsBargain bargainDetail(Integer userId,Integer bargainId, Integer goodsId) throws ServiceException {
        GoodsBargain goodsBargain = goodsBargainMapper.selectByPrimaryKey(bargainId);
        if(goodsBargain == null){
            throw new ServiceException("压价信息异常！");
        }else{
            //判断商品状态
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            if(goodsInfo == null){
                throw new ServiceException("商品不存在！");
            }
            if(goodsInfo.getStatus().intValue() != 1){
                throw new ServiceException("商品已售出或已下架！");
            }
            if(goodsInfo.getUserId().intValue() == userId.intValue()){//卖家获取压价详情

            }else if(goodsBargain.getUserId().intValue() == userId.intValue()){//买家获取压价详情

            }
            //判断压价信息状态
            if(goodsBargain.getStatus().intValue() == 4){
                throw new ServiceException("压价信息异常！");
            }

        }
        return goodsBargain;
    }
}
