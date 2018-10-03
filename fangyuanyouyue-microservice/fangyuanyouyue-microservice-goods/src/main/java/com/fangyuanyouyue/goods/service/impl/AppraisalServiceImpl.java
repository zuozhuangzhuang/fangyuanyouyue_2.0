package com.fangyuanyouyue.goods.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.goods.dao.*;
import com.fangyuanyouyue.goods.dto.AppraisalDetailDto;
import com.fangyuanyouyue.goods.dto.AppraisalOrderInfoDto;
import com.fangyuanyouyue.goods.dto.AppraisalUrlDto;
import com.fangyuanyouyue.goods.dto.adminDto.AdminAppraisalDetailDto;
import com.fangyuanyouyue.goods.model.*;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.SchedualMessageService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import com.fangyuanyouyue.goods.service.SchedualWalletService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Autowired
    private SchedualMessageService schedualMessageService;

    @Override
    public AppraisalOrderInfoDto addAppraisal(Integer userId, Integer[] goodsIds, String title, String description, String[] imgUrls, String videoUrl,String videoImg) throws ServiceException {
        //验证手机号
        UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
        if(StringUtils.isEmpty(user.getPhone())){
            throw new ServiceException("未绑定手机号！");
        }
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
        appraisalOrderInfo.setStatus(Status.ORDER_UNPAID.getValue());
        appraisalOrderInfoMapper.insert(appraisalOrderInfo);
        List<String> goodsNames = new ArrayList<>();
        //用来存放订单详情DTO列表
        if(goodsIds != null && goodsIds.length != 0){//用户对商品提交鉴定
            for(Integer goodsId:goodsIds){
                GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
                if(goodsInfo == null || goodsInfo.getStatus().intValue() == 3 || goodsInfo.getStatus().intValue() == 5){
                    throw new ServiceException("商品不存在或已下架！");
                }else{
                    GoodsAppraisalDetail goodsAppraisalDetail = goodsAppraisalDetailMapper.selectByUserIdGoodsId(userId, goodsId);
                    if(goodsAppraisalDetail != null){
                        throw new ServiceException("【"+goodsInfo.getName()+"】您已申请过鉴定！");
                    }else{
                        //生成鉴定和订单
                        goodsAppraisalDetail = new GoodsAppraisalDetail();
                        goodsAppraisalDetail.setUserId(userId);
                        goodsAppraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
                        goodsAppraisalDetail.setOrderId(appraisalOrderInfo.getId());
                        goodsAppraisalDetail.setGoodsId(goodsId);
                        goodsAppraisalDetail.setStatus(4);//状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示)
                        goodsAppraisalDetail.setTitle(goodsInfo.getName());
                        goodsAppraisalDetail.setDescription(goodsInfo.getDescription());
                        //根据鉴定费算法 超过2000元0.5%
                        BigDecimal price;
                        if(goodsInfo.getPrice().compareTo(BigDecimal.valueOf(2000)) <= 0){
                            //小于2000元
                            price = new BigDecimal(10).setScale(2,BigDecimal.ROUND_HALF_UP);
                        }else{
                            price = goodsInfo.getPrice().multiply(new BigDecimal(0.005)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        }
                        goodsAppraisalDetail.setPrice(price);
                        if(goodsInfo.getUserId().intValue() == userId.intValue()){
                            goodsAppraisalDetail.setType(1);//鉴定类型 1商家鉴定 2买家 3普通用户
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
                            if(goodsImg.getType().intValue() == 3){//视频截图图片
                                addAppraisalImg(goodsImg.getImgUrl(),goodsAppraisalDetail.getId(),3);
                            }else{
                                addAppraisalImg(goodsImg.getImgUrl(),goodsAppraisalDetail.getId(),1);
                            }
                        }
                        //订单总金额
                        amount = amount.add(goodsAppraisalDetail.getPrice());//原价
                        goodsNames.add(goodsInfo.getName());
                    }
                }
            }
        }else{//用户提交图片或视频进行鉴定
            //先查询用户免费鉴定次数，如果是 我要鉴定，且有免费鉴定次数，直接鉴定
            Integer appraisalCount = JSONObject.parseObject(schedualWalletService.getAppraisalCount(userId)).getInteger("data");
            GoodsAppraisalDetail goodsAppraisalDetail = new GoodsAppraisalDetail();
            goodsAppraisalDetail.setUserId(userId);
            goodsAppraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
            goodsAppraisalDetail.setOrderId(appraisalOrderInfo.getId());
            goodsAppraisalDetail.setType(3);//鉴定类型 1商家鉴定 2买家 3普通用户
            goodsAppraisalDetail.setTitle(title);
            goodsAppraisalDetail.setDescription(description);
            goodsAppraisalDetail.setStatus(4);//状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示)
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
                if(StringUtils.isNotEmpty(videoImg)){
                    addAppraisalImg(videoImg,goodsAppraisalDetail.getId(),3);
                }else{
                    throw new ServiceException("视频封面图不能为空！");
                }
            }
            //订单总金额
            amount = amount.add(price);//原价
            goodsNames.add("我要鉴定");
        }

        appraisalOrderInfo.setAmount(amount);
        appraisalOrderInfoMapper.updateByPrimaryKey(appraisalOrderInfo);
        AppraisalOrderInfoDto appraisalOrderInfoDto = new AppraisalOrderInfoDto(appraisalOrderInfo);
        appraisalOrderInfoDto.setGoodsNames(goodsNames);
        //用户需要支付对应的资金才可以鉴定成功
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
        //取消鉴定时删除鉴定订单
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
        if(detail == null || detail.getStatus().equals(5)){
            throw new ServiceException("未找到鉴定信息！");
        }
        AppraisalDetailDto detailDto = new AppraisalDetailDto(detail);
        //获取鉴定图片列表
        List<AppraisalUrl> appraisalUrls = appraisalUrlMapper.selectListBuUserId(detail.getId());
        ArrayList<AppraisalUrlDto> appraisalUrlDtos = AppraisalUrlDto.toDtoList(appraisalUrls);
        detailDto.setAppraisalUrlDtos(appraisalUrlDtos);
        return detailDto;
    }

    @Override
    public Object payAppraisal(Integer userId, Integer orderId, Integer payType, String payPwd) throws ServiceException {
        //只有买家能调用订单支付接口，直接根据orderId查询订单
        AppraisalOrderInfo orderInfo = appraisalOrderInfoMapper.selectByPrimaryKey(orderId);
        if(orderInfo == null){
            throw new ServiceException("订单不存在！");
        }else{
            StringBuffer payInfo = new StringBuffer();
            if(payType.intValue() == Status.PAY_TYPE_WECHAT.getValue()){
                WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualWalletService.orderPayByWechat(orderInfo.getOrderNo(), orderInfo.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.appraisal_wechat_notify.getNotifUrl())).getString("data")), WechatPayDto.class);
                return wechatPayDto;
            }else if(payType.intValue() == Status.PAY_TYPE_ALIPAY.getValue()){
                String info = JSONObject.parseObject(schedualWalletService.orderPayByALi(orderInfo.getOrderNo(), orderInfo.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.appraisal_alipay_notify.getNotifUrl())).getString("data");
                payInfo.append(info);
            }else if(payType.intValue() == Status.PAY_TYPE_BALANCE.getValue()) {
                //验证支付密码
                Boolean verifyPayPwd = JSONObject.parseObject(schedualUserService.verifyPayPwd(userId, payPwd)).getBoolean("data");
                if (!verifyPayPwd) {
                    throw new ServiceException(ReCode.PAYMENT_PASSWORD_ERROR.getValue(),ReCode.PAYMENT_PASSWORD_ERROR.getMessage());
                } else {
                    //调用wallet-service修改余额功能
                    BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(userId, orderInfo.getAmount(), Status.SUB.getValue())), BaseResp.class);
                    if(baseResp.getCode() == 1){
                        throw new ServiceException(baseResp.getReport().toString());
                    }
                }
                //订单支付成功
                updateOrder(orderInfo.getOrderNo(),null,Status.PAY_TYPE_BALANCE.getValue());
                payInfo.append("余额支付成功！");
            }else if(payType.intValue() == Status.PAY_TYPE_MINI.getValue()){
                //小程序支付
                WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualWalletService.orderPayByWechatMini(userId,orderInfo.getOrderNo(), orderInfo.getAmount(), NotifyUrl.mini_notify.getNotifUrl()+NotifyUrl.appraisal_wechat_notify.getNotifUrl())).getString("data")), WechatPayDto.class);
                return wechatPayDto;
            }else{
                throw new ServiceException("支付类型错误！");
            }

            return payInfo.toString();
        }
    }

    @Override
    public boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException{
        try{
            AppraisalOrderInfo appraisalOrderInfo = appraisalOrderInfoMapper.selectByOrderNo(orderNo);
            if(appraisalOrderInfo == null){
                throw new ServiceException("订单不存在！");
            }
            appraisalOrderInfo.setPayNo(thirdOrderNo);
            appraisalOrderInfo.setStatus(Status.ORDER_COMPLETE.getValue());
            appraisalOrderInfoMapper.updateByPrimaryKey(appraisalOrderInfo);
            List<GoodsAppraisalDetail> listByOrderIdStatus = goodsAppraisalDetailMapper.getListByOrderIdStatus(appraisalOrderInfo.getId(), 4);
            StringBuffer title = new StringBuffer();
            int type = 1;
            if(listByOrderIdStatus != null && listByOrderIdStatus.size() > 0){
                for(GoodsAppraisalDetail detail:listByOrderIdStatus){
                    detail.setStatus(0);//支付后修改为申请中
                    goodsAppraisalDetailMapper.updateByPrimaryKey(detail);
                    title.append("【"+detail.getTitle()+"】");
                    if(detail.getType().equals(1)){
                        //商品鉴定
                        type = 2;
                    }else if(detail.getType().equals(2)){
                        type = 3;
                    }
                }
                if(type == 1){
                    title.append("官方鉴定");
                }else if(type == 2){
                    title.append("商品鉴定");
                }else if(type == 3){
                    title.append("鉴定详情");
                }

                //系统消息：您的鉴定申请已提交，专家将于两个工作日内给出答复，请注意消息通知
                schedualMessageService.easemobMessage(appraisalOrderInfo.getUserId().toString(),
                        "您的鉴定申请已提交，专家将于两个工作日内给出答复，请注意消息通知",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
                //余额账单
                schedualWalletService.addUserBalanceDetail(appraisalOrderInfo.getUserId(),appraisalOrderInfo.getAmount(),payType,Status.EXPEND.getValue(),orderNo,title.toString(),null,appraisalOrderInfo.getUserId(),Status.PLATFORM_APPRAISAL.getValue(),thirdOrderNo);
                return true;
            }else{
                return false;
            }
        } catch (Exception e){
            throw new ServiceException("官方鉴定申请失败！");
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

    @Override
    public Pager appraisalList(AdminGoodsParam param) throws ServiceException {
        Integer total = goodsAppraisalDetailMapper.countPage(param.getType(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
        //商品列表
        List<GoodsAppraisalDetail> appraisalPage = goodsAppraisalDetailMapper.getAppraisalPage(param.getType(),param.getStart(),param.getLimit(),
                param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        List<AdminAppraisalDetailDto> dtos = AdminAppraisalDetailDto.toDtoList(appraisalPage);
        for(AdminAppraisalDetailDto dto:dtos){

            //获取鉴定图片列表
            List<AppraisalUrl> appraisalUrls = appraisalUrlMapper.selectListBuUserId(dto.getAppraisalDetailId());
            ArrayList<AppraisalUrlDto> appraisalUrlDtos = AppraisalUrlDto.toDtoList(appraisalUrls);
            dto.setAppraisalUrlDtos(appraisalUrlDtos);
        }
        //遍历商品列表，添加到GoodsDtos中
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(dtos);
        return pager;
    }

    @Override
    public void updateAppraisal(Integer id, Integer status, String opinion, Integer isShow) throws ServiceException {
        GoodsAppraisalDetail goodsAppraisalDetail = goodsAppraisalDetailMapper.selectByPrimaryKey(id);
        if(goodsAppraisalDetail == null || goodsAppraisalDetail.getStatus().equals(5)){
            throw new ServiceException("未找到鉴定信息！");
        }
        goodsAppraisalDetail.setStatus(status);
        goodsAppraisalDetail.setIsShow(isShow);
        goodsAppraisalDetail.setOpinion(opinion);
        goodsAppraisalDetail.setSubmitTime(DateStampUtils.getTimesteamp());
        StringBuffer title = new StringBuffer();
        if(status == 1 && goodsAppraisalDetail.getGoodsId() != null){
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsAppraisalDetail.getGoodsId());
            //卖家申请的才可以展示鉴标识
            if(goodsInfo.getUserId().equals(goodsAppraisalDetail.getUserId())){
                goodsInfo.setIsAppraisal(Status.YES.getValue());
                goodsInfoMapper.updateByPrimaryKey(goodsInfo);
            }
        }
        if(status == 3){
            title.append("【"+goodsAppraisalDetail.getTitle()+"】");
            if(goodsAppraisalDetail.getType().equals(1)){
                title.append("商品鉴定存疑");
            }else if(goodsAppraisalDetail.getType().equals(2)){
                title.append("鉴定详情存疑");
            }else{
                title.append("官方鉴定存疑");
            }
            //退还鉴定金
            schedualWalletService.updateBalance(goodsAppraisalDetail.getUserId(),goodsAppraisalDetail.getPrice(),Status.ADD.getValue());
            //订单号
            final IdGenerator idg = IdGenerator.INSTANCE;
            String orderNo = idg.nextId();
            schedualWalletService.addUserBalanceDetail(goodsAppraisalDetail.getUserId(),goodsAppraisalDetail.getPrice(),Status.PAY_TYPE_BALANCE.getValue(),Status.REFUND.getValue(),orderNo,title.toString(),null,goodsAppraisalDetail.getUserId(),Status.APPRAISAL.getValue(),orderNo);

            schedualMessageService.easemobMessage(goodsAppraisalDetail.getUserId().toString(),"您申请的鉴定结果为“存疑”鉴定费用已退回您的余额，点击此处查看您的余额吧",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_WALLET.getMessage(),"");
        }else{
            schedualMessageService.easemobMessage(goodsAppraisalDetail.getUserId().toString(),"您申请的鉴定已得到官方专家的答复！点击此处前往查看吧",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_PLATFORM_APPRAISAL.getMessage(),"");
        }
        goodsAppraisalDetailMapper.updateByPrimaryKey(goodsAppraisalDetail);
    }

    @Override
    public void deleteAppraisal(Integer userId, Integer[] ids) throws ServiceException {
        for(Integer id:ids){
            GoodsAppraisalDetail detail = goodsAppraisalDetailMapper.selectByPrimaryKey(id);
            if(detail == null || detail.getStatus().equals(5)){
                throw new ServiceException("未找到鉴定信息！");
            }else{
                if(detail.getUserId().equals(userId)){
                    detail.setStatus(5);
                    goodsAppraisalDetailMapper.updateByPrimaryKey(detail);
                }else{
                    throw new ServiceException("无权删除！");
                }
            }
        }
    }
}
