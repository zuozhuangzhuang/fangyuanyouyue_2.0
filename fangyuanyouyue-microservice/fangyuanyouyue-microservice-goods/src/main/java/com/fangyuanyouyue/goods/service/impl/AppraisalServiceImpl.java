package com.fangyuanyouyue.goods.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.BannerIndexMapper;
import com.fangyuanyouyue.goods.dao.GoodsAppraisalDetailMapper;
import com.fangyuanyouyue.goods.dao.GoodsAppraisalMapper;
import com.fangyuanyouyue.goods.dao.GoodsCategoryMapper;
import com.fangyuanyouyue.goods.dao.GoodsCommentMapper;
import com.fangyuanyouyue.goods.dao.GoodsCorrelationMapper;
import com.fangyuanyouyue.goods.dao.GoodsImgMapper;
import com.fangyuanyouyue.goods.dao.GoodsInfoMapper;
import com.fangyuanyouyue.goods.dao.GoodsQuickSearchMapper;
import com.fangyuanyouyue.goods.dao.HotSearchMapper;
import com.fangyuanyouyue.goods.model.GoodsAppraisal;
import com.fangyuanyouyue.goods.model.GoodsAppraisalDetail;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.SchedualUserService;

@Service(value = "appraisalService")
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

    @Override
    public void addAppraisal(Integer userId, Integer[] goodsIds, String title, String description, BigDecimal price,String imgUrl) throws ServiceException {
        //可能提交多个商品鉴定
        GoodsAppraisal goodsAppraisal = new GoodsAppraisal();
        goodsAppraisal.setAddTime(DateStampUtils.getTimesteamp());
        goodsAppraisal.setUserId(userId);
        goodsAppraisalMapper.insert(goodsAppraisal);
        if(goodsIds != null || goodsIds.length != 0){
            for(Integer goodsId:goodsIds){
                GoodsAppraisalDetail goodsAppraisalDetail = goodsAppraisalDetailMapper.selectByUserIdGoodsId(userId, goodsId);
                if(goodsAppraisalDetail != null){
                    throw new ServiceException("您已申请过鉴定！");
                }else{
                    GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
                    if(goodsInfo == null){
                        throw new ServiceException("鉴定列表中包含不存在或已下架商品！");
                    }else{
                        goodsAppraisalDetail = new GoodsAppraisalDetail();
                        goodsAppraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
                        goodsAppraisalDetail.setAppraisalId(goodsAppraisal.getId());
                        goodsAppraisalDetail.setGoodsId(goodsId);
                        goodsAppraisalDetail.setStatus(0);//状态 0申请 1真 2假 3存疑
                        goodsAppraisalDetail.setTitle(title);
                        goodsAppraisalDetail.setDescription(description);
                        goodsAppraisalDetail.setPrice(price);
                        if(goodsInfo.getUserId() == userId){
                            goodsAppraisalDetail.setType(1);//鉴定类型 1商家鉴定 2买家 3普通用户
                            goodsInfo.setIsAppraisal(2);//是否鉴定 1未鉴定 2已鉴定
                        }else{
                            goodsAppraisalDetail.setType(2);
                        }
                        goodsAppraisalDetailMapper.insert(goodsAppraisalDetail);
                    }
                }
            }
        }else{
            GoodsAppraisalDetail goodsAppraisalDetail = new GoodsAppraisalDetail();
            goodsAppraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
            goodsAppraisalDetail.setAppraisalId(goodsAppraisal.getId());
            goodsAppraisalDetail.setStatus(0);//状态 0申请 1真 2假 3存疑
            goodsAppraisalDetail.setTitle(title);
            goodsAppraisalDetail.setType(3);//鉴定类型 1商家鉴定 2买家 3普通用户
            goodsAppraisalDetail.setDescription(description);
            goodsAppraisalDetail.setPrice(price);
            goodsAppraisalDetailMapper.insert(goodsAppraisalDetail);
        }
        //TODO 生成订单
        //用户需要支付对应的资金才可以鉴定成功
    }
}
