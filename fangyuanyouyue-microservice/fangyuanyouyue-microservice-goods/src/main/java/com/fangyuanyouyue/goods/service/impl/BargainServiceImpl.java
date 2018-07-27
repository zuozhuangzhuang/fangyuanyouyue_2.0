package com.fangyuanyouyue.goods.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.GoodsBargainMapper;
import com.fangyuanyouyue.goods.dao.GoodsInfoMapper;
import com.fangyuanyouyue.goods.model.GoodsBargain;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.service.BargainService;

@Service(value = "bargainService")
public class BargainServiceImpl implements BargainService{
    @Autowired
    private GoodsBargainMapper goodsBargainMapper;
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Override
    public void addBargain(Integer userId, Integer goodsId, BigDecimal price, String reason) throws ServiceException {
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
                goodsBargain.setReason(reason);
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
//        GoodsBargain goodsBargain = goodsBargainMapper.selectByUserIdGoodsId(userId, goodsId);
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
                goodsBargain.setStatus(status);//状态 2同意 3拒绝 4取消
                goodsBargainMapper.updateByPrimaryKey(goodsBargain);
                //如果卖家同意议价，就拒绝此商品剩余的申请中议价
                if(status.intValue() == 2){
                    List<GoodsBargain> goodsBargains = goodsBargainMapper.selectAllByGoodsId(goodsId);
                    for(GoodsBargain bargain:goodsBargains){
                        bargain.setStatus(3);
                        goodsBargainMapper.updateByPrimaryKey(bargain);
                    }
                }
            }else if(goodsBargain.getUserId().intValue() == userId.intValue()){
                //买家取消压价
                if(status.intValue() == 2 || status.intValue() == 3){
                    throw new ServiceException("买家不可同意或拒绝议价！");
                }
                goodsBargain.setStatus(status);//状态 2同意 3拒绝 4取消
                goodsBargainMapper.updateByPrimaryKey(goodsBargain);
            }else{
                throw new ServiceException("压价信息异常！");
            }
        }
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
