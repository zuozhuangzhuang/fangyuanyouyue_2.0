package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.goods.dao.AppraisalOrderInfoMapper;
import com.fangyuanyouyue.goods.dao.GoodsAppraisalDetailMapper;
import com.fangyuanyouyue.goods.dao.GoodsAppraisalMapper;
import com.fangyuanyouyue.goods.dao.GoodsInfoMapper;
import com.fangyuanyouyue.goods.dto.AppraisalOrderInfoDto;
import com.fangyuanyouyue.goods.model.AppraisalOrderInfo;
import com.fangyuanyouyue.goods.model.GoodsAppraisal;
import com.fangyuanyouyue.goods.model.GoodsAppraisalDetail;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service(value = "appraisalService")
@Transactional(rollbackFor=Exception.class)
public class AppraisalServiceImpl implements AppraisalService{
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private GoodsAppraisalMapper goodsAppraisalMapper;
    @Autowired
    private GoodsAppraisalDetailMapper goodsAppraisalDetailMapper;
    @Autowired
    private AppraisalOrderInfoMapper appraisalOrderInfoMapper;

    @Override
    public AppraisalOrderInfoDto addAppraisal(Integer userId, Integer[] goodsIds, String title, String description, String imgUrl, String videoUrl) throws ServiceException {
        //用户信息
//        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
        //可能提交多个商品鉴定
        GoodsAppraisal goodsAppraisal = new GoodsAppraisal();
        goodsAppraisal.setAddTime(DateStampUtils.getTimesteamp());
        goodsAppraisal.setUserId(userId);
        goodsAppraisalMapper.insert(goodsAppraisal);
        //生成订单
        //每次提交的鉴定生成一个订单，批量鉴定有多个订单详情
        AppraisalOrderInfo appraisalOrderInfo = new AppraisalOrderInfo();
        appraisalOrderInfo.setUserId(userId);
        //订单号
        final IdGenerator idg = IdGenerator.INSTANCE;
        String id = idg.nextId();
        appraisalOrderInfo.setOrderNo("1"+id);

        BigDecimal amount = new BigDecimal(0);//原价，初始为0
        appraisalOrderInfo.setAppraisalId(goodsAppraisal.getId());
        appraisalOrderInfo.setAmount(amount);
        appraisalOrderInfo.setCount(goodsIds.length);
        appraisalOrderInfo.setAddTime(DateStampUtils.getTimesteamp());
        appraisalOrderInfoMapper.insert(appraisalOrderInfo);
        //用来存放订单详情DTO列表

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
                        if(goodsInfo.getUserId().intValue() == userId.intValue()){
                            goodsAppraisalDetail.setType(1);//鉴定类型 1商家鉴定 2买家 3普通用户
                            goodsInfo.setIsAppraisal(2);//是否鉴定 1未鉴定 2已鉴定
                        }else{
                            goodsAppraisalDetail.setType(2);
                        }
                        goodsAppraisalDetailMapper.insert(goodsAppraisalDetail);

                        //订单总金额
                        amount = amount.add(goodsAppraisalDetail.getPrice());//原价
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

            //订单总金额
            amount = amount.add(price);//原价
        }

        appraisalOrderInfo.setAmount(amount);
        appraisalOrderInfoMapper.updateByPrimaryKey(appraisalOrderInfo);
        AppraisalOrderInfoDto appraisalOrderInfoDto = new AppraisalOrderInfoDto(appraisalOrderInfo);

        //TODO 用户需要支付对应的资金才可以鉴定成功
        return appraisalOrderInfoDto;
    }

    @Override
    public void cancelAppraisal(Integer userId, Integer orderId) throws ServiceException {
        //TODO 取消鉴定时删除鉴定订单
    }
}
