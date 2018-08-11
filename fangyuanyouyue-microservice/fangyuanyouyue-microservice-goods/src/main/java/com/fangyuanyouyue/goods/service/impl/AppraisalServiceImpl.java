package com.fangyuanyouyue.goods.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.goods.dao.*;
import com.fangyuanyouyue.goods.dto.AppraisalDetailDto;
import com.fangyuanyouyue.goods.dto.AppraisalOrderInfoDto;
import com.fangyuanyouyue.goods.dto.AppraisalUrlDto;
import com.fangyuanyouyue.goods.model.*;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import com.fangyuanyouyue.goods.service.SchedualWalletService;
import org.apache.commons.lang.StringUtils;
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
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private GoodsAppraisalDetailMapper goodsAppraisalDetailMapper;
    @Autowired
    private AppraisalOrderInfoMapper appraisalOrderInfoMapper;
    @Autowired
    private AppraisalUrlMapper appraisalUrlMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private SchedualWalletService schedualWalletService;

    @Override
    public AppraisalOrderInfoDto addAppraisal(Integer userId, Integer[] goodsIds, String title, String description, String[] imgUrls, String videoUrl) throws ServiceException {
        //只有我要鉴定才可以用免费鉴定
        //生成订单
        //每次提交的鉴定生成一个订单，批量鉴定有多个订单详情
        AppraisalOrderInfo appraisalOrderInfo = new AppraisalOrderInfo();
        appraisalOrderInfo.setUserId(userId);
        //订单号
        final IdGenerator idg = IdGenerator.INSTANCE;
        String id = idg.nextId();
        appraisalOrderInfo.setOrderNo("1"+id);

        BigDecimal amount = new BigDecimal(0);//原价，初始为0
        appraisalOrderInfo.setAmount(amount);
        if(goodsIds == null){
            appraisalOrderInfo.setCount(1);
        }else{
            appraisalOrderInfo.setCount(goodsIds.length);
        }
        appraisalOrderInfo.setAddTime(DateStampUtils.getTimesteamp());
        appraisalOrderInfoMapper.insert(appraisalOrderInfo);
        //用来存放订单详情DTO列表
        if(goodsIds != null && goodsIds.length != 0){//用户对商品提交鉴定
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
                        goodsAppraisalDetail.setUserId(userId);
                        goodsAppraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
                        goodsAppraisalDetail.setOrderId(appraisalOrderInfo.getId());
                        goodsAppraisalDetail.setGoodsId(goodsId);
                        goodsAppraisalDetail.setStatus(4);//状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示)
                        goodsAppraisalDetail.setDescription(goodsInfo.getDescription());
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
                        List<GoodsImg> imgsByGoodsId = goodsImgMapper.getImgsByGoodsId(goodsId);
                        //鉴定图片表
                        if(StringUtils.isNotEmpty(goodsInfo.getVideoUrl())){//新增商品视频信息
                            addAppraisalImg(goodsInfo.getVideoUrl(),goodsAppraisalDetail.getId(),2);
                        }
                        for(GoodsImg goodsImg:imgsByGoodsId){//商品图片s
                            addAppraisalImg(goodsImg.getImgUrl(),goodsAppraisalDetail.getId(),1);
                        }
                        //订单总金额
                        amount = amount.add(goodsAppraisalDetail.getPrice());//原价
                    }
                }
            }
        }else{//用户提交图片或视频进行鉴定
            //TODO 先查询用户免费鉴定次数，如果是 我要鉴定，且有免费鉴定次数，直接鉴定
            Integer appraisalCount = JSONObject.parseObject(schedualWalletService.getAppraisalCount(userId)).getInteger("data");
            GoodsAppraisalDetail goodsAppraisalDetail = new GoodsAppraisalDetail();
            goodsAppraisalDetail.setUserId(userId);
            goodsAppraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
            goodsAppraisalDetail.setOrderId(appraisalOrderInfo.getId());
            goodsAppraisalDetail.setType(3);//鉴定类型 1商家鉴定 2买家 3普通用户
            goodsAppraisalDetail.setDescription(description);
            goodsAppraisalDetail.setStatus(4);//状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示)
            //TODO 根据鉴定费算法
            BigDecimal price = new BigDecimal(10);//自己上传图片或视频收费10元
            if(appraisalCount > 0){//免费鉴定
                goodsAppraisalDetail.setStatus(0);
                price = new BigDecimal(0);
                JSONObject.parseObject(schedualWalletService.updateAppraisalCount(userId,1)).getInteger("data");
            }
            goodsAppraisalDetail.setPrice(price);
            goodsAppraisalDetailMapper.insert(goodsAppraisalDetail);
            //鉴定图片表
            if(imgUrls != null){
                for(String imgUrl:imgUrls){
                    addAppraisalImg(imgUrl,goodsAppraisalDetail.getId(),1);
                }
            }
            if(StringUtils.isNotEmpty(videoUrl)){
                addAppraisalImg(videoUrl,goodsAppraisalDetail.getId(),2);
            }
            //订单总金额
            amount = amount.add(price);//原价
        }

        appraisalOrderInfo.setAmount(amount);
        appraisalOrderInfoMapper.updateByPrimaryKey(appraisalOrderInfo);
        AppraisalOrderInfoDto appraisalOrderInfoDto = new AppraisalOrderInfoDto(appraisalOrderInfo);
        //TODO 用户需要支付对应的资金才可以鉴定成功
        return appraisalOrderInfoDto;
    }

    /**
     * 生成鉴定图片表信息
     * @param url
     * @param appraisalId
     * @param type 1图片 2视频
     */
    private void addAppraisalImg(String url,Integer appraisalId,Integer type){
        //鉴定图片表
        AppraisalUrl appraisalUrl = new AppraisalUrl();
        appraisalUrl.setAppraisalId(appraisalId);
        appraisalUrl.setUrl(url);
        appraisalUrl.setType(type);
        appraisalUrl.setAddTime(DateStampUtils.getTimesteamp());
        appraisalUrlMapper.insert(appraisalUrl);
    }


    @Override
    public void cancelAppraisal(Integer userId, Integer orderId) throws ServiceException {
        //TODO 取消鉴定时删除鉴定订单
        AppraisalOrderInfo appraisalOrderInfo = appraisalOrderInfoMapper.selectByPrimaryKey(orderId);
        if(appraisalOrderInfo == null){
            throw new ServiceException("获取鉴定信息失败！");
        }else{
            List<GoodsAppraisalDetail> goodsAppraisalDetails = goodsAppraisalDetailMapper.selectListByUserId(userId,orderId,null,null,4);
            for(GoodsAppraisalDetail detail:goodsAppraisalDetails){
                if(detail != null){
                    //删除鉴定详情
                    goodsAppraisalDetailMapper.deleteByPrimaryKey(detail.getId());
                    //图片、视频 删不删都行
                    List<AppraisalUrl> appraisalUrls = appraisalUrlMapper.selectListBuUserId(detail.getId());
                    //删除相关图片、视频
                    for(AppraisalUrl appraisalUrl:appraisalUrls){
                        appraisalUrlMapper.deleteByPrimaryKey(appraisalUrl.getId());
                    }
                }
            }
            //删除相关订单
            appraisalOrderInfoMapper.deleteByPrimaryKey(orderId);
        }
    }

    @Override
    public List<AppraisalDetailDto> getAppraisal(Integer userId, Integer start, Integer limit) throws ServiceException {
        //根据用户ID分页获取鉴定列表
        List<GoodsAppraisalDetail> details = goodsAppraisalDetailMapper.selectListByUserId(userId,null,start*limit,limit,null);
        if(details == null){
            throw new ServiceException("获取鉴定信息失败！");
        }
        ArrayList<AppraisalDetailDto> detadilDtos = setDtoByDetails(details);

        return detadilDtos;
    }

    @Override
    public AppraisalDetailDto appraisalDetail(Integer userId, Integer detailId) throws ServiceException {
        //获取鉴定详情
        GoodsAppraisalDetail detail = goodsAppraisalDetailMapper.selectByPrimaryKey(detailId);
        AppraisalDetailDto detailDto = new AppraisalDetailDto(detail);
        //获取鉴定图片列表
        List<AppraisalUrl> appraisalUrls = appraisalUrlMapper.selectListBuUserId(detail.getId());
        ArrayList<AppraisalUrlDto> appraisalUrlDtos = AppraisalUrlDto.toDtoList(appraisalUrls);
        detailDto.setAppraisalUrlDtos(appraisalUrlDtos);
        return detailDto;
    }

    @Override
    public String payAppraisal(Integer userId, Integer orderId, Integer type, String payPwd) throws ServiceException {
        //只有买家能调用订单支付接口，直接根据orderId查询订单
        AppraisalOrderInfo orderInfo = appraisalOrderInfoMapper.selectByPrimaryKey(orderId);
        if(orderInfo == null){
            throw new ServiceException("订单不存在！");
        }else{
            StringBuffer payInfo = new StringBuffer();
            switch (type){
                case 1://支付宝
                    payInfo.append("支付宝支付回调！");
                    break;
                case 2://微信
                    payInfo.append("微信支付回调！");
                    break;
                case 3://余额
                    //验证支付密码
                    Boolean verifyPayPwd = Boolean.valueOf(JSONObject.parseObject(schedualUserService.verifyPayPwd(userId, payPwd)).getString("data"));
                    if(!verifyPayPwd){
                        throw new ServiceException("支付密码错误！");
                    }else{
                        //调用wallet-service修改余额功能
                        schedualWalletService.updateBalance(userId,orderInfo.getAmount(),2);
                    }
                    payInfo.append("余额支付成功！");
                    break;
                default:
                    throw new ServiceException("支付类型错误！");
            }
            List<GoodsAppraisalDetail> listByOrderIdStatus = goodsAppraisalDetailMapper.getListByOrderIdStatus(orderId, 4);
            for(GoodsAppraisalDetail detail:listByOrderIdStatus){
                detail.setStatus(0);//支付后修改为申请中
                goodsAppraisalDetailMapper.updateByPrimaryKey(detail);
            }
            return payInfo.toString();
        }
    }

    @Override
    public List<AppraisalDetailDto> getAllAppraisal(Integer start, Integer limit) throws ServiceException {
        List<GoodsAppraisalDetail> details = goodsAppraisalDetailMapper.getListByStatus(start * limit, limit);
        if(details == null){
            throw new ServiceException("获取鉴定信息失败！");
        }

        ArrayList<AppraisalDetailDto> detadilDtos = setDtoByDetails(details);
        return detadilDtos;
    }

    /**
     * 封装鉴定详情dto列表
     * @param details
     * @return
     */
    private ArrayList<AppraisalDetailDto> setDtoByDetails(List<GoodsAppraisalDetail> details) {
        ArrayList<AppraisalDetailDto> detadilDtos = new ArrayList<>();
        for(GoodsAppraisalDetail detail:details){
            AppraisalDetailDto detailDto = new AppraisalDetailDto(detail);
            if(StringUtils.isNotEmpty(detail.getOpinion())){
                detailDto.setOpinion(detail.getOpinion());
            }
            detailDto.setStatus(detail.getStatus());
            //获取鉴定图片列表
            List<AppraisalUrl> appraisalUrls = appraisalUrlMapper.selectListBuUserId(detail.getId());
            ArrayList<AppraisalUrlDto> appraisalUrlDtos = AppraisalUrlDto.toDtoList(appraisalUrls);
            detailDto.setAppraisalUrlDtos(appraisalUrlDtos);
            detadilDtos.add(detailDto);
        }
        return detadilDtos;
    }
}
