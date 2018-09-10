package com.fangyuanyouyue.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.goods.dao.*;
import com.fangyuanyouyue.goods.dto.BargainDto;
import com.fangyuanyouyue.goods.dto.GoodsCommentDto;
import com.fangyuanyouyue.goods.dto.GoodsDto;
import com.fangyuanyouyue.goods.model.*;
import com.fangyuanyouyue.goods.service.BargainService;
import com.fangyuanyouyue.goods.service.SchedualMessageService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import com.fangyuanyouyue.goods.service.SchedualWalletService;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private GoodsCorrelationMapper goodsCorrelationMapper;
    @Autowired
    private GoodsCommentMapper goodsCommentMapper;
    @Autowired
    private CommentLikesMapper commentLikesMapper;
    @Autowired
    private SchedualWalletService schedualWalletService;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private BargainOrderMapper bargainOrderMapper;

    @Override
    public Object addBargain(Integer userId, Integer goodsId, BigDecimal price, String reason,Integer addressId,String payPwd,Integer payType) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        if(goodsInfo == null || goodsInfo.getStatus().intValue() == 3 || goodsInfo.getStatus().intValue() == 5){
            throw new ServiceException("商品不存在或已下架！");
        }else{
            if(goodsInfo.getUserId().intValue() == userId.intValue()){
                throw new ServiceException("不可以对自己的商品进行压价！");
            }
            if(goodsInfo.getStatus().intValue() != 1){
                throw new ServiceException("商品已售出或已下架！");
            }
            if(goodsInfo.getType().intValue() == 2){
                throw new ServiceException("抢购无法压价！");
            }
            //压价金额不可以高于原价，否则岂不是显得客户很傻
            if(price.compareTo(goodsInfo.getPrice())>=0){
                throw new ServiceException("压价不得高于原价！");
            }
            //如果用户已经存在申请中的压价，不能压第二次
            List<GoodsBargain> goodsBargains = goodsBargainMapper.selectByUserIdGoodsId(userId, goodsId,1);
            if(goodsBargains != null && goodsBargains.size()>0){
                throw new ServiceException("此商品您已压价！");
            }else{
                //生成议价订单
                BargainOrder bargainOrder = new BargainOrder();
                bargainOrder.setUserId(userId);
                bargainOrder.setGoodsId(goodsId);
                bargainOrder.setAmount(price);
                bargainOrder.setAddressId(addressId);
                if(StringUtils.isNotEmpty(reason)){
                    bargainOrder.setReason(reason);
                }
                bargainOrder.setStatus(1);//状态 1申请 2同意 3拒绝 4取消
                bargainOrder.setAddTime(DateStampUtils.getTimesteamp());
                //订单号
                final IdGenerator idg = IdGenerator.INSTANCE;
                String id = idg.nextId();
                bargainOrder.setOrderNo(id);
                bargainOrderMapper.insert(bargainOrder);

                StringBuffer payInfo = new StringBuffer();
                if(payType.intValue() == 1){//微信,如果回调失败就不做处理，成功就在回调接口中继续订单支付
                    WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualWalletService.orderPayByWechat(bargainOrder.getOrderNo(), bargainOrder.getAmount(),NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.bargain_wechat_notify.getNotifUrl())).getString("data")), WechatPayDto.class);
                    return wechatPayDto;
                }else if(payType.intValue() == 2){//支付宝,如果回调失败就不做处理，成功就在回调接口中继续订单支付
                    String info = JSONObject.parseObject(schedualWalletService.orderPayByALi(bargainOrder.getOrderNo(), bargainOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.bargain_alipay_notify.getNotifUrl())).getString("data");
                    payInfo.append(info);
                }else if(payType == 3){
                    if(StringUtils.isEmpty(payPwd)){
                        throw new ServiceException("支付密码不能为空！");
                    }
                    //验证支付密码
                    Boolean verifyPayPwd = JSONObject.parseObject(schedualUserService.verifyPayPwd(userId, payPwd)).getBoolean("data");
                    if(!verifyPayPwd){
                        throw new ServiceException("支付密码错误！");
                    }else{
                        //压价时扣除用户余额，如果余额不足就不可以议价
                        //调用wallet-service修改余额功能
                        BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(userId, bargainOrder.getAmount(), 2)), BaseResp.class);
                        if(baseResp.getCode() == 1){
                            throw new ServiceException(baseResp.getReport().toString());
                        }
                        payInfo.append("余额支付成功");
                        updateOrder(bargainOrder.getOrderNo(),null,3);
                    }
                }else if(payType.intValue() == 4){
                    //小程序支付
                    WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualWalletService.orderPayByWechatMini(userId,bargainOrder.getOrderNo(), bargainOrder.getAmount(),NotifyUrl.mini_test_notify.getNotifUrl()+NotifyUrl.bargain_wechat_notify.getNotifUrl())).getString("data")), WechatPayDto.class);
                    return wechatPayDto;
                }else{
                    throw new ServiceException("支付方式错误！");
                }
                return payInfo.toString();
            }
        }

    }

    @Override
    public boolean updateOrder(String orderNo, String thridOrderNo, Integer payType) throws ServiceException{
        BargainOrder bargainOrder = bargainOrderMapper.selectByOrderNo(orderNo);
        if(bargainOrder == null){
            throw new ServiceException("未找到订单！");
        }
        GoodsBargain goodsBargain = new GoodsBargain();
        goodsBargain.setUserId(bargainOrder.getUserId());
        goodsBargain.setGoodsId(bargainOrder.getGoodsId());
        goodsBargain.setPrice(bargainOrder.getAmount());
        goodsBargain.setAddressId(bargainOrder.getAddressId());
        goodsBargain.setReason(bargainOrder.getReason());
        goodsBargain.setStatus(1);//状态 1申请 2同意 3拒绝 4取消
        goodsBargain.setAddTime(DateStampUtils.getTimesteamp());
        goodsBargain.setIsDelete(2);//是否删除 1是 2否
        goodsBargain.setBargainNo(orderNo);
        goodsBargainMapper.insert(goodsBargain);
        bargainOrder.setStatus(2);
        bargainOrderMapper.updateByPrimaryKeySelective(bargainOrder);
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(bargainOrder.getGoodsId());
        //议价：恭喜您！您的商品【商品名称】有新的议价，点击此处查看详情
        schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                "恭喜您！您的商品【"+goodsInfo.getName()+"】有新的议价，点击此处查看详情","2","2",goodsInfo.getId().toString());
        //买家新增余额账单
        schedualWalletService.addUserBalanceDetail(goodsBargain.getUserId(),goodsBargain.getPrice(),payType,2,bargainOrder.getOrderNo(),goodsInfo.getName(),goodsInfo.getUserId(),goodsBargain.getUserId(),3);
        return true;
    }

    @Override
    public Integer updateBargain(Integer userId, Integer goodsId,Integer bargainId,Integer status) throws ServiceException {
        GoodsBargain goodsBargain = goodsBargainMapper.selectByPrimaryKey(bargainId);
        Integer orderId = null;
        //查询用户正在申请的压价
        //2.卖家同意压价 3.卖家拒绝压价 4.买家取消压价
        if(goodsBargain == null){
            throw new ServiceException("压价信息异常！");
        }else{
            //判断商品状态
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            if(goodsInfo == null || goodsInfo.getStatus().intValue() == 3 || goodsInfo.getStatus().intValue() == 5){
                throw new ServiceException("商品不存在或已下架！");
            }
            if(goodsInfo.getStatus().intValue() != 1){
                throw new ServiceException("商品已售出！");
            }
            //判断压价状态
            if(goodsBargain.getStatus().intValue() == 2){
                throw new ServiceException("已同意压价！");
            }
            if(goodsBargain.getStatus().intValue() == 3){
                throw new ServiceException("已拒绝压价！");
            }
            if(goodsBargain.getStatus().intValue() == 4){
                throw new ServiceException("已取消压价！");
            }
            if(goodsInfo.getUserId().intValue() == userId.intValue()){
                //卖家处理压价
                if(status == 4){
                    throw new ServiceException("卖家不可取消议价！");
                }
                //卖家同意后生成订单，拒绝后退回余额
                if(status.intValue() == 2){//状态 2同意 3拒绝 4取消
                    //生成订单
                    //每次提交的鉴定生成一个订单，批量鉴定有多个订单详情
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setIsResolve(2);//是否拆单 1是 2否
                    orderInfo.setUserId(goodsBargain.getUserId());
                    //订单号
                    final IdGenerator idg = IdGenerator.INSTANCE;
                    String id = idg.nextId();
                    orderInfo.setOrderNo("1"+id);

                    orderInfo.setAmount(goodsInfo.getPrice());//原价
                    orderInfo.setCount(1);
                    orderInfo.setStatus(2);//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货
                    orderInfo.setAddTime(DateStampUtils.getTimesteamp());
                    orderInfo.setSellerId(goodsInfo.getUserId());
                    orderInfo.setIsRefund(2);
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
                    orderDetail.setMainOrderId(orderInfo.getId());//主订单ID
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
                    goodsInfo.setStatus(2);//状态  1出售中 2已售出 3已下架（已结束） 5删除
                    goodsInfoMapper.updateByPrimaryKey(goodsInfo);
                    //议价：恭喜您！您对商品【商品名称】的议价卖家已同意，点击此处查看订单详情
                    //如果卖家同意议价，就拒绝此商品剩余的申请中议价
                    orderId = orderInfo.getId();
                    schedualMessageService.easemobMessage(goodsBargain.getUserId().toString(),
                            "恭喜您！您对商品【"+goodsInfo.getName()+"】的议价卖家已同意，点击此处查看订单详情","3","2",orderId.toString());
                    List<GoodsBargain> goodsBargains = goodsBargainMapper.selectAllByGoodsId(goodsId,1);//状态 1申请 2同意 3拒绝 4取消
                    for(GoodsBargain bargain:goodsBargains){
                        bargain.setStatus(3);
                        goodsBargainMapper.updateByPrimaryKey(bargain);
                        //议价：您对商品【商品名称】的议价已被卖家拒绝，点击此处查看详情
                        schedualMessageService.easemobMessage(bargain.getUserId().toString(),
                                "您对商品【"+goodsInfo.getName()+"】的议价已被卖家拒绝，点击此处查看详情","2","2",bargain.getGoodsId().toString());
                        //买家新增余额账单
                        schedualWalletService.addUserBalanceDetail(bargain.getUserId(),bargain.getPrice(),3,1,bargain.getBargainNo(),goodsInfo.getName(),goodsInfo.getUserId(),bargain.getUserId(),3);
                    }
                }else if(status.intValue() == 3){
                    //退回余额
                    //调用wallet-service修改余额功能
                    BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(goodsBargain.getUserId(), goodsBargain.getPrice(),1)), BaseResp.class);
                    if(baseResp.getCode() == 1){
                        throw new ServiceException(baseResp.getReport().toString());
                    }
                    //议价：您对商品【商品名称】的议价已被卖家拒绝，点击此处查看详情
                    schedualMessageService.easemobMessage(goodsBargain.getUserId().toString(),
                            "您对商品【"+goodsInfo.getName()+"】的议价已被卖家拒绝，点击此处查看详情","2","2",goodsBargain.getGoodsId().toString());
                    //买家新增余额账单
                    schedualWalletService.addUserBalanceDetail(goodsBargain.getUserId(),goodsBargain.getPrice(),3,1,goodsBargain.getBargainNo(),goodsInfo.getName(),goodsInfo.getUserId(),goodsBargain.getUserId(),3);
                }else{
                    throw new ServiceException("状态异常！");
                }
            }else if(goodsBargain.getUserId().intValue() == userId.intValue()){
                //买家取消压价
                if(status.intValue() == 2 || status.intValue() == 3){
                    throw new ServiceException("买家不可同意或拒绝议价！");
                }
                //退回余额
                //调用wallet-service修改余额功能
                BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(goodsBargain.getUserId(), goodsBargain.getPrice(),1)), BaseResp.class);
                if(baseResp.getCode() == 1){
                    throw new ServiceException(baseResp.getReport().toString());
                }
                //买家新增余额账单
                schedualWalletService.addUserBalanceDetail(goodsBargain.getUserId(),goodsBargain.getPrice(),3,1,goodsBargain.getBargainNo(),goodsInfo.getName(),goodsInfo.getUserId(),goodsBargain.getUserId(),3);
            }else{
                throw new ServiceException("压价信息异常！");
            }
            goodsBargain.setStatus(status);//状态 2同意 3拒绝 4取消
            goodsBargainMapper.updateByPrimaryKey(goodsBargain);
            return orderId;
        }
    }

    @Override
    public List<GoodsDto> bargainList(Integer userId,Integer start,Integer limit,String search) throws ServiceException {
        //根据申请议价用户ID获取此用户的所有压价申请，筛选出商品ID列表，遍历商品列表，根据商品ID和用户ID查询压价列表，存入商品dto中
        List<Integer> goodsIdsByUserId = goodsBargainMapper.selectGoodsIdsByUserId(userId,start*limit,limit,search);
        List<GoodsDto> goodsDtos = new ArrayList<>();
        for(Integer goodsId:goodsIdsByUserId){
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            GoodsDto goodsDto = setDtoByGoodsInfo(userId,goodsInfo);
            //压价信息
            List<GoodsBargain> bargains = goodsBargainMapper.selectByUserIdGoodsId(userId, goodsInfo.getId(), null);
            List<BargainDto> bargainDtos = BargainDto.toDtoList(bargains);
            for(BargainDto bargainDto:bargainDtos){
                UserInfo seller = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(bargainDto.getUserId())).getString("data")), UserInfo.class);
                bargainDto.setNickName(seller.getNickName());
                bargainDto.setHeadImgUrl(seller.getHeadImgUrl());
            }
            goodsDto.setBargainDtos(bargainDtos);
            goodsDtos.add(goodsDto);
        }
        return goodsDtos;
    }

    @Override
    public GoodsBargain bargainDetail(Integer userId,Integer bargainId, Integer goodsId) throws ServiceException {
        GoodsBargain goodsBargain = goodsBargainMapper.selectByPrimaryKey(bargainId);
        if(goodsBargain == null){
            throw new ServiceException("压价信息异常！");
        }else{
            //判断商品状态
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            if(goodsInfo == null || goodsInfo.getStatus().intValue() == 3 || goodsInfo.getStatus().intValue() == 5){
                throw new ServiceException("商品不存在或已下架！");
            }
            if(goodsInfo.getStatus().intValue() != 1){
                throw new ServiceException("商品已售出！");
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


    /**
     * 给GoodsDto赋值
     * @param goodsInfo
     * @return
     * @throws ServiceException
     */
    private GoodsDto setDtoByGoodsInfo(Integer userId,GoodsInfo goodsInfo) throws ServiceException{
        if(goodsInfo == null || goodsInfo.getStatus().intValue() == 3 || goodsInfo.getStatus().intValue() == 5){
            throw new ServiceException("商品不存在或已下架！");
        }else{
            List<GoodsImg> goodsImgs = goodsImgMapper.getImgsByGoodsId(goodsInfo.getId());
            String mainImgUrl = null;
            for(GoodsImg goodsImg:goodsImgs){
                if(goodsImg.getType() == 1){
                    mainImgUrl = goodsImg.getImgUrl();
                }
            }
            List<GoodsCorrelation> goodsCorrelations = goodsCorrelationMapper.getCorrelationsByGoodsId(goodsInfo.getId());
            //按照先后顺序获取评论
            List<Map<String, Object>> maps = goodsCommentMapper.selectMapByGoodsIdCommentId(null,goodsInfo.getId(), 0, 3);
            List<GoodsCommentDto> goodsCommentDtos = GoodsCommentDto.mapToDtoList(maps);
            for(GoodsCommentDto goodsCommentDto:goodsCommentDtos){
                Map<String, Object> map = goodsCommentMapper.selectByCommentId(goodsCommentDto.getCommentId());
                if(map != null){
                    goodsCommentDto.setToUserId((Integer)map.get("user_id"));
                    goodsCommentDto.setToUserHeadImgUrl((String)map.get("head_img_url"));
                    goodsCommentDto.setToUserName((String)map.get("nick_name"));
                }
                goodsCommentDto.setGoodsName(goodsInfo.getName());
                goodsCommentDto.setDescription(goodsInfo.getDescription());
                goodsCommentDto.setMainUrl(mainImgUrl);
                if(userId != null){
                    //获取每条评论是否点赞
                    CommentLikes commentLikes = commentLikesMapper.selectByUserId(userId, goodsCommentDto.getId());
                    if(commentLikes != null){
                        goodsCommentDto.setIsLike(1);
                    }
                }
            }
            //获取卖家信息
            UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(goodsInfo.getUserId())).getString("data")), UserInfo.class);
            GoodsDto goodsDto = new GoodsDto(user,goodsInfo,goodsImgs,goodsCorrelations,goodsCommentDtos);
            goodsDto.setCommentCount(goodsCommentMapper.selectCount(goodsInfo.getId()));
            return goodsDto;
        }
    }


    @Override
    public Integer getProcess(Integer userId) throws ServiceException {
        /**
         * 市集：
         *  商品：
         *   待处理的议价
         */
        //状态 1申请
        List<GoodsBargain> goodsIdsByUserId = goodsBargainMapper.selectAllByUserId(userId,1);
        return goodsIdsByUserId == null?0:goodsIdsByUserId.size();
    }

    @Override
    public void deleteBargain(Integer userId, Integer[] goodsIds) throws ServiceException {
        for(Integer goodsId:goodsIds){
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            //取消用户在所选商品的议价信息
            List<GoodsBargain> goodsBargains = goodsBargainMapper.selectByUserIdGoodsId(userId, goodsId, null);
            if(goodsBargains == null || goodsBargains.size() == 0){
                throw new ServiceException("议价信息不存在！");
            }
            for(GoodsBargain bargain:goodsBargains){
                //删除商品内所有议价信息
                if(bargain.getStatus() == 1){
                    bargain.setStatus(4);
                    //退回余额
                    //调用wallet-service修改余额功能
                    BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(bargain.getUserId(), bargain.getPrice(),1)), BaseResp.class);
                    if(baseResp.getCode() == 1){
                        throw new ServiceException(baseResp.getReport().toString());
                    }
                    updateBargain(userId,goodsId,bargain.getId(),4);
                    //买家新增余额账单
                    schedualWalletService.addUserBalanceDetail(bargain.getUserId(),bargain.getPrice(),3,1,bargain.getBargainNo(),goodsInfo.getName(),goodsInfo.getUserId(),bargain.getUserId(),3);
                }
                bargain.setIsDelete(1);//是否删除 1是 2否
                goodsBargainMapper.updateByPrimaryKey(bargain);
            }
        }
    }
}
