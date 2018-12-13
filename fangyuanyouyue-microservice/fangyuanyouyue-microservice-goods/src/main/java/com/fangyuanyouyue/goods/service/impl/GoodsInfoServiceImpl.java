package com.fangyuanyouyue.goods.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.*;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.goods.dao.*;
import com.fangyuanyouyue.goods.dto.*;
import com.fangyuanyouyue.goods.dto.adminDto.AdminGoodsCategoryDto;
import com.fangyuanyouyue.goods.dto.adminDto.AdminGoodsDto;
import com.fangyuanyouyue.goods.model.*;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.*;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(value = "goodsInfoService")
@Transactional(rollbackFor=Exception.class)
public class GoodsInfoServiceImpl implements GoodsInfoService{
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Autowired
    private GoodsCorrelationMapper goodsCorrelationMapper;
    @Autowired
    private GoodsCommentMapper goodsCommentMapper;
    @Autowired
    private HotSearchMapper hotSearchMapper;
    @Autowired
    private BannerIndexMapper bannerIndexMapper;
    @Autowired
    private GoodsQuickSearchMapper goodsQuickSearchMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private CommentLikesMapper commentLikesMapper;
    @Autowired
    private GoodsBargainMapper goodsBargainMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private BargainService bargainService;
    @Autowired
    private SchedualMessageService schedualMessageService;
    @Autowired
    private GoodsIntervalHistoryMapper goodsIntervalHistoryMapper;
    @Autowired
    private SchedualWalletService schedualWalletService;
    @Autowired
    private TimerService timerService;
    @Autowired
    private GoodsTopDetailMapper goodsTopDetailMapper;
    @Autowired
    private GoodsTopOrderMapper goodsTopOrderMapper;
    @Autowired
    private SysPropertyMapper sysPropertyMapper;

    @Override
    public GoodsInfo selectByPrimaryKey(Integer id) throws ServiceException{
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(id);
        if(goodsInfo == null){
            throw new ServiceException("商品不存在！");
        }
        return goodsInfo;
    }


    @Override
    public String goodsMainImg(Integer goodsId) throws ServiceException {
        List<GoodsImg> imgsByGoodsId = goodsImgMapper.getImgsByGoodsId(goodsId);
        if(imgsByGoodsId != null){
            for(GoodsImg goodsImg:imgsByGoodsId){
                if(goodsImg.getType() == 1){
                    return goodsImg.getImgUrl();
                }
            }
        }
        throw new ServiceException("获取商品主图失败！");
    }

    @Override
    public List<GoodsDto> getGoodsInfoList(Integer myId,GoodsParam param) throws ServiceException{
        try{
            if(StringUtils.isNotEmpty(param.getSearch())){
                HotSearch hotSearch = hotSearchMapper.selectByName(param.getSearch());
                if(hotSearch == null){
                    hotSearch = new HotSearch();
                    hotSearch.setAddTime(DateStampUtils.getTimesteamp());
                    hotSearch.setName(param.getSearch());
                    hotSearch.setCount(1);
                    hotSearchMapper.insert(hotSearch);
                }else{
                    hotSearch.setCount(hotSearch.getCount()+1);
                    hotSearchMapper.updateByPrimaryKeySelective(hotSearch);
                }
            }
        }catch (DuplicateKeyException e){
            e.printStackTrace();
        }
        List<GoodsInfo> goodsInfos = new ArrayList<>();
        if(Status.AUCTION.getValue().equals(param.getType())){
            goodsInfos = goodsInfoMapper.getAuctionList(myId,param.getUserId(),param.getStatus(),param.getSearch(),
                    param.getPriceMin(),param.getPriceMax(),param.getSynthesize(),param.getQuality(),param.getStart()*param.getLimit(),param.getLimit(),param.getGoodsCategoryIds());
        }else{
            goodsInfos = goodsInfoMapper.getGoodsList(myId,param.getUserId(),param.getStatus(),param.getSearch(),
                    param.getPriceMin(),param.getPriceMax(),param.getSynthesize(),param.getQuality(),param.getStart()*param.getLimit(),param.getLimit(),param.getType(),param.getGoodsCategoryIds());
        }
        //分类热度加一
        if(param.getGoodsCategoryIds() != null && param.getGoodsCategoryIds().length>0){
            goodsCategoryMapper.addSearchCountByCategoryIds(param.getGoodsCategoryIds());
        }
//        List<GoodsDto> goodsDtos = new ArrayList<>();
        List<GoodsDto> goodsDtos = GoodsDto.toDtoList(goodsInfos);
        //遍历商品列表，添加到GoodsDtos中
//        for (GoodsInfo goodsInfo:goodsInfos) {
//            //抢购降价
//            if(goodsInfo.getType().equals(Status.AUCTION.getValue())){
//                timerService.getPriceDown(goodsInfo);
//            }
//            GoodsDto goodsDto = setDtoByGoodsInfo(param.getUserId(),goodsInfo);
//            //token不为空为我的商品列表，均为卖家。商品列表其实不需要返回这些信息
//            goodsDtos.add(goodsDto);
//        }
        return goodsDtos;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    @TxTransaction(isStart=true)
    public void addGoods(UserInfo user,GoodsParam param) throws ServiceException {
        //验证手机号
        if(StringUtils.isEmpty(user.getPhone())){
            throw new ServiceException(ReCode.NO_PHONE.getValue(),ReCode.NO_PHONE.getMessage());
        }
        //商品表 goods_info
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setUserId(user.getId());
        //是否鉴定根据用户是否官方认证
//        if(Boolean.valueOf(JSONObject.parseObject(schedualUserService.userIsAuth(userId)).getString("data"))){
//            goodsInfo.setIsAppraisal(1);//已认证
//        }else{
//            goodsInfo.setIsAppraisal(2);//未认证
//        }

        goodsInfo.setIsAppraisal(Status.NO.getValue());//未认证
        goodsInfo.setName(param.getGoodsInfoName());
        goodsInfo.setDescription(param.getDescription());
        goodsInfo.setPrice(param.getPrice());
        goodsInfo.setStartPrice(param.getPrice());
        goodsInfo.setPostage(param.getPostage()==null?new BigDecimal(0):param.getPostage());
        //排序：是否置顶
//        goodsInfo.setSort(param.getSort());
        if(StringUtils.isNotEmpty(param.getLabel())){
            goodsInfo.setLabel(param.getLabel());
        }
        goodsInfo.setType(param.getType());
        goodsInfo.setStatus(Status.GOODS_IN_SALE.getValue());//状态 1出售中 2 已售出 3已下架（已结束） 5删除
        if(StringUtils.isNotEmpty(param.getVideoUrl())){
            goodsInfo.setVideoUrl(param.getVideoUrl());
            if(param.getVideoLength() != null){
                goodsInfo.setVideoLength(param.getVideoLength());
            }
        }
        goodsInfo.setAddTime(DateStampUtils.getTimesteamp());
        if(param.getType() == Status.AUCTION.getValue()){
            goodsInfo.setFloorPrice(param.getFloorPrice());
            goodsInfo.setIntervalTime(param.getIntervalTime());
            goodsInfo.setMarkdown(param.getMarkdown());
            goodsInfo.setLastIntervalTime(DateStampUtils.getTimesteamp());
        }else{
            goodsInfo.setCommentTime(DateStampUtils.getTimesteamp());
        }
        goodsInfo.setMainImgUrl(param.getImgUrls()[0]);
        goodsInfoMapper.insert(goodsInfo);
        //视频截图路径
        if(StringUtils.isNotEmpty(param.getVideoImg())){
            saveGoodsPicOne(goodsInfo.getId(),param.getVideoImg(),3,1);
        }
        //初始化商品分类关联表
        for(int i=0;i<param.getGoodsCategoryIds().length;i++){
            GoodsCorrelation goodsCorrelation = new GoodsCorrelation();
            goodsCorrelation.setGoodsId(goodsInfo.getId());
            goodsCorrelation.setAddTime(DateStampUtils.getTimesteamp());
            goodsCorrelation.setGoodsCategoryId(param.getGoodsCategoryIds()[i]);
            goodsCorrelation.setCategoryParentId(goodsCategoryMapper.selectParentId(param.getGoodsCategoryIds()[i]));
            goodsCorrelationMapper.insert(goodsCorrelation);
        }
        //商品图片表 goods_img
        //每个图片储存一条商品图片表信息
        for(int i=0;i<param.getImgUrls().length;i++){
            if(i == 0){
                saveGoodsPicOne(goodsInfo.getId(),param.getImgUrls()[i],1,i+1);
            }else{
                saveGoodsPicOne(goodsInfo.getId(),param.getImgUrls()[i],2,i+1);
            }
        }
        //增加积分、信誉度
        String result = schedualWalletService.updateScore(user.getId(), Score.ADD_GOODSINFO.getScore(),Status.ADD.getValue());
        BaseResp br = ParseReturnValue.getParseReturnValue(result);
        if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
            throw new ServiceException(br.getCode(),br.getReport());
        }
        if(param.getType() == Status.GOODS.getValue()){
            result = schedualWalletService.updateCredit(user.getId(), Credit.ADD_GOODSINFO.getCredit(),Status.ADD.getValue());
            br = ParseReturnValue.getParseReturnValue(result);
            if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                throw new ServiceException(br.getCode(),br.getReport());
            }
        }else{
            result = schedualWalletService.updateCredit(user.getId(), Credit.ADD_AUCTION.getCredit(),Status.ADD.getValue());
            br = ParseReturnValue.getParseReturnValue(result);
            if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                throw new ServiceException(br.getCode(),br.getReport());
            }
        }
        //给被邀请的用户发送信息 邀请我：用户“用户昵称”上传商品【商品名称】时邀请了您！点击此处前往查看吧
        //邀请我：用户“用户昵称”上传抢购【抢购名称】时邀请了您！点击此处前往查看吧
        if(param.getUserIds() != null && param.getUserIds().length > 0){
            for(Integer toUserId:param.getUserIds()){
                if(goodsInfo.getType()==Status.GOODS.getValue()){
                    schedualMessageService.easemobMessage(toUserId.toString(),
                            "用户“"+user.getNickName()+"”上传商品【"+goodsInfo.getName()+"】时邀请了您！点击此处前往查看吧",Status.INVITE_MESSAGE.getMessage(),
                            Status.JUMP_TYPE_GOODS.getMessage(),goodsInfo.getId().toString());
                }else{
                    schedualMessageService.easemobMessage(toUserId.toString(),
                            "用户“"+user.getNickName()+"”上传抢购【"+goodsInfo.getName()+"】时邀请了您！点击此处前往查看吧",Status.INVITE_MESSAGE.getMessage(),
                            Status.JUMP_TYPE_AUCTION.getMessage(),goodsInfo.getId().toString());
                }
            }
        }
//        return setDtoByGoodsInfo(null,goodsInfo);
    }

    /**
     * 给GoodsDto赋值
     * @param goodsInfo
     * @return
     * @throws ServiceException
     */
    private GoodsDto setDtoByGoodsInfo(Integer userId,GoodsInfo goodsInfo) throws ServiceException{
        if(goodsInfo == null){
            throw new ServiceException("商品不存在或已下架！");
        }else{

            List<GoodsImg> goodsImgs = goodsImgMapper.getImgsByGoodsId(goodsInfo.getId());
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
                goodsCommentDto.setMainUrl(goodsInfo.getMainImgUrl());
                if(userId != null){
                    //获取每条评论是否点赞
                    CommentLikes commentLikes = commentLikesMapper.selectByUserId(userId, goodsCommentDto.getId());
                    if(commentLikes != null){
                        goodsCommentDto.setIsLike(1);
                    }
                }
            }


            GoodsDto goodsDto = new GoodsDto(goodsInfo,goodsImgs,goodsCorrelations,goodsCommentDtos);
            if(userId != null){
                Integer type;
                List<GoodsBargain> bargains;
                //压价信息
                if(userId.intValue() == goodsInfo.getUserId().intValue()) {//卖家
                    type = 2;
                    bargains = goodsBargainMapper.selectAllByGoodsId(goodsInfo.getId(),null);
                }else{
                    type = 1;
                    bargains = goodsBargainMapper.selectByUserIdGoodsId(userId, goodsInfo.getId(), null);
                }
                List<BargainDto> bargainDtos = BargainDto.toDtoList(bargains);
                if(bargainDtos != null && bargainDtos.size()>0){
                    goodsDto.setBargainDtos(bargainDtos);
                }
                //如果商品status为已售出，获取商品所属订单ID
                if (goodsInfo.getStatus().equals(Status.GOODS_SOLD.getValue())) {
                    OrderDetail orderDetail = orderDetailMapper.selectOrderByGoodsIdStatus(userId, goodsInfo.getId(), type);
                    if (orderDetail != null) {
                        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderDetail.getOrderId());
                        goodsDto.setOrderStatus(orderInfo.getStatus());
                        goodsDto.setOrderId(orderDetail.getOrderId());
                    }
                }
            }
            goodsDto.setCommentCount(goodsCommentMapper.selectCount(goodsInfo.getId()));
            if(goodsInfo.getType() == 2){
                //只取最近三次降价记录
                List<GoodsIntervalHistory> historyList = goodsIntervalHistoryMapper.selectByGoodsId(goodsInfo.getId(),0,3);
                goodsDto.setHistoryDtos(GoodsIntervalHistoryDto.toDtoList(historyList));
            }
            return goodsDto;
        }
    }

    /**
     * 添加商品图片路径
     * @param goodsId
     * @param type
     * @param sort
     * @throws ServiceException
     */
    private void saveGoodsPicOne(Integer goodsId, String imgUrl, Integer type,Integer sort) throws ServiceException{
        GoodsImg goodsImg = new GoodsImg();
        goodsImg.setAddTime(DateStampUtils.getTimesteamp());
        goodsImg.setGoodsId(goodsId);
        goodsImg.setType(type);//类型 1主图 2次图 3视频截图
        goodsImg.setSort(sort);
        goodsImg.setImgUrl(imgUrl);
        goodsImgMapper.insert(goodsImg);
    }

    @Override
    public void deleteGoods(Integer userId,Integer[] goodsIds) throws ServiceException {
        //批量修改商品状态为删除
        for(Integer goodsId:goodsIds){
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            if(goodsInfo == null || goodsInfo.getStatus().equals(Status.GOODS_DELETE.getValue())){
                throw new ServiceException("商品不存在或已下架！");
            }else{
                if(!goodsInfo.getUserId().equals(userId)){
                    throw new ServiceException("无权删除！");
                }
                //取消所有议价
                List<GoodsBargain> goodsBargains = goodsBargainMapper.selectAllByGoodsId(goodsId,Status.BARGAIN_APPLY.getValue());//状态 1申请 2同意 3拒绝 4取消
                for(GoodsBargain bargain:goodsBargains){
                    bargainService.updateBargain(goodsInfo.getUserId(),goodsId,bargain.getId(),Status.BARGAIN_REFUSE.getValue());
                }
                goodsInfo.setStatus(Status.GOODS_DELETE.getValue());//状态 普通商品 1出售中 2已售出 3已下架（已结束） 5删除
                goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
            }
        }
    }

    @Override
    public void modifyGoods(GoodsParam param) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(param.getGoodsId());
        if(goodsInfo == null || goodsInfo.getStatus().equals(Status.GOODS_DELETE.getValue())){
            throw new ServiceException("商品不存在！");
        }else{
            if(goodsInfo.getStatus().equals(Status.GOODS_SOLD.getValue())){
                throw new ServiceException("商品已售出！");
            }
            //修改商品信息
            if(StringUtils.isNotEmpty(param.getGoodsInfoName())){
                goodsInfo.setName(param.getGoodsInfoName());
            }
            if(StringUtils.isNotEmpty(param.getDescription())){
                goodsInfo.setDescription(param.getDescription());
            }
            if(param.getPrice() != null){
                goodsInfo.setPrice(param.getPrice());
                goodsInfo.setStartPrice(param.getPrice());
            }
            if(param.getPostage() != null){
                goodsInfo.setPostage(param.getPostage());
            }
            if(StringUtils.isNotEmpty(param.getLabel())){
                goodsInfo.setLabel(param.getLabel());
            }
//            goodsInfo.setSort(param.getSort());
            if(param.getFloorPrice() != null){
                goodsInfo.setFloorPrice(param.getFloorPrice());
            }
            if(param.getIntervalTime() != null){
                goodsInfo.setIntervalTime(param.getIntervalTime());
            }
            if(param.getMarkdown() != null){
                goodsInfo.setMarkdown(param.getMarkdown());
            }
            if(StringUtils.isNotEmpty(param.getVideoUrl())){
                goodsInfo.setVideoUrl(param.getVideoUrl());
                if(param.getVideoLength() != null){
                    goodsInfo.setVideoLength(param.getVideoLength());
                }
            }
            //视频截图路径
            if(StringUtils.isNotEmpty(param.getVideoImg())){
                saveGoodsPicOne(goodsInfo.getId(),param.getVideoImg(),3,1);
            }
            if(param.getGoodsCategoryIds() != null && param.getGoodsCategoryIds().length > 0){
                //删除旧商品分类关联表
                List<GoodsCorrelation> goodsCorrelations = goodsCorrelationMapper.getCorrelationsByGoodsId(goodsInfo.getId());
                for(GoodsCorrelation correlation:goodsCorrelations){
                    goodsCorrelationMapper.deleteByPrimaryKey(correlation.getId());
                }
                //初始化商品分类关联表
                for(int i=0;i<param.getGoodsCategoryIds().length;i++){
                    GoodsCorrelation goodsCorrelation = new GoodsCorrelation();
                    goodsCorrelation.setGoodsId(goodsInfo.getId());
                    goodsCorrelation.setAddTime(DateStampUtils.getTimesteamp());
                    goodsCorrelation.setGoodsCategoryId(param.getGoodsCategoryIds()[i]);
                    goodsCorrelation.setCategoryParentId(goodsCategoryMapper.selectParentId(param.getGoodsCategoryIds()[i]));
                    goodsCorrelationMapper.insert(goodsCorrelation);
                }
            }
            //新增商品图片信息
            //每个图片储存一条商品图片表信息
            if(param.getImgUrls() != null && param.getImgUrls().length > 0){
                //删除旧商品图片信息
                goodsImgMapper.deleteByGoodsId(goodsInfo.getId());
                goodsInfo.setMainImgUrl(param.getImgUrls()[0]);
                for(int i=0;i<param.getImgUrls().length;i++){
                    if(i == 0){
                        saveGoodsPicOne(goodsInfo.getId(),param.getImgUrls()[i],1,i+1);
                    }else{
                        saveGoodsPicOne(goodsInfo.getId(),param.getImgUrls()[i],2,i+1);
                    }
                }
            }
            //如果是已下架的商品或抢购，重新上架
            goodsInfo.setStatus(Status.GOODS_IN_SALE.getValue());//状态 1出售中 2已售出 3已下架（已结束） 5删除
            goodsInfo.setUpdateTime(DateStampUtils.getTimesteamp());
            if(goodsInfo.getType().equals(Status.GOODS.getValue())){
                goodsInfo.setCommentTime(DateStampUtils.getTimesteamp());
            }
            if(goodsInfo.getType().equals(Status.AUCTION.getValue())){
                goodsInfo.setLastIntervalTime(DateStampUtils.getTimesteamp());
            }
            goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);

            //抢购 重新编辑可以重新上架，删除旧降价历史
            if(goodsInfo.getType() == 2){
                //抢购
                List<GoodsIntervalHistory> goodsIntervalHistories = goodsIntervalHistoryMapper.selectByGoodsId(goodsInfo.getId(), null, null);
                for(GoodsIntervalHistory history:goodsIntervalHistories){
                    goodsIntervalHistoryMapper.deleteByPrimaryKey(history.getId());
                }
            }
        }
    }

    @Override
    public List<GoodsCategoryDto> categoryList() throws ServiceException{
        List<GoodsCategory> goodsCategories = goodsCategoryMapper.categoryParentList(null);
        List<GoodsCategoryDto> categoryDtos = GoodsCategoryDto.toDtoList(goodsCategories);
        for(GoodsCategoryDto categoryDto:categoryDtos){
            categoryDto.setChildList(GoodsCategoryDto.toDtoList(goodsCategoryMapper.getChildCategoryList(categoryDto.getCategoryId())));
        }
        return categoryDtos;
    }

    @Override
    public GoodsDto goodsInfoByToken(Integer goodsId, Integer userId) throws ServiceException {
        //获取收藏表信息获取商品集合（存在多条此商品重复信息）
        List<GoodsInfo> goodsInfos = goodsInfoMapper.selectMyCollectGoods(userId, null, null, goodsId,null,null,null);
        GoodsInfo goodsInfo;
        GoodsDto goodsDto;
        //是否收藏/关注 1未关注未收藏（商品/抢购） 2已关注未收藏(抢购) 3未关注已收藏（商品/抢购） 4已关注已收藏(抢购)
        if(goodsInfos != null && goodsInfos.size() > 0){//如果goodsInfos大于多条，说明存在多条收藏状态
            goodsInfo = goodsInfos.get(0);
            if(goodsInfo == null){
                throw new ServiceException("未找到商品、抢购！");
            }
            //抢购降价
            if(goodsInfo.getType().equals(Status.AUCTION.getValue())){
                timerService.getPriceDown(goodsInfo);
            }
            goodsDto = setDtoByGoodsInfo(userId,goodsInfo);
            //如果有两条，说明即收藏，又关注
            if(goodsInfos.size()>1){
                goodsDto.setIsCollect(4);
            }else{
                //只有一条收藏数据——判断是收藏还是关注
                Collect collect = collectMapper.selectByCollectId(userId,goodsId, null,null);
                if(collect == null){
                    throw new ServiceException("获取收藏信息失败！");
                }
                if(collect.getType() == 1){//类型 1关注 2收藏
                    goodsDto.setIsCollect(2);
                }else{
                    goodsDto.setIsCollect(3);
                }
            }
        }else{
            goodsInfo = goodsInfoMapper.selectByPrimaryKeyDetail(goodsId);
            if(goodsInfo == null){
                throw new ServiceException("未找到商品、抢购！");
            }
            //抢购降价
            if(goodsInfo.getType().equals(Status.AUCTION.getValue())){
                timerService.getPriceDown(goodsInfo);
            }
            goodsDto = setDtoByGoodsInfo(userId,goodsInfo);
            goodsDto.setIsCollect(1);
        }
//        if(goodsInfo.getStatus().intValue() == 5){
//            throw new ServiceException("商品不存在或已下架！");
//        }
        //是否官方认证
        Map<String, Object> goodsUserInfoExtAndVip = goodsInfoMapper.getGoodsUserInfoExtAndVip(goodsId);
        if(goodsUserInfoExtAndVip != null){
            goodsDto.setAuthType((Integer)goodsUserInfoExtAndVip.get("auth_type") == 2?1:2);
            //已认证=可用，未认证=不可用
            goodsDto.setIsCoupon(goodsDto.getAuthType());
            goodsDto.setVipLevel((Integer)goodsUserInfoExtAndVip.get("vip_level"));
            Long credit = (Long)goodsUserInfoExtAndVip.get("credit");
            goodsDto.setCredit(credit);
            if(credit != null){
                if(credit < -100){//差
                    goodsDto.setCreditLevel(1);
                }else if(-100 <= credit && credit < 1000){//低
                    goodsDto.setCreditLevel(2);
                }else if(1000 <= credit && credit < 10000){//中
                    goodsDto.setCreditLevel(3);
                }else if(10000 <= credit && credit < 500000){//高
                    goodsDto.setCreditLevel(4);
                }else if(500000 <= credit){//优
                    goodsDto.setCreditLevel(5);
                }
            }
        }
        //卖家信息
        //粉丝数
        goodsDto.setFansCount(goodsInfoMapper.getGoodsUserFansCount(goodsId));
        //关注数
        goodsDto.setCollectCount(goodsInfoMapper.getGoodsUserCollectCount(goodsId));
        return goodsDto;
    }

    @Override
    public GoodsDto goodsInfo(Integer goodsId) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKeyDetail(goodsId);
        if(goodsInfo == null){
            throw new ServiceException("商品不存在或已下架！");
        }
        //抢购降价
        if(goodsInfo.getType().equals(Status.AUCTION.getValue())){
            timerService.getPriceDown(goodsInfo);
        }
        GoodsDto goodsDto = setDtoByGoodsInfo(null,goodsInfo);
        //是否官方认证
        Map<String, Object> goodsUserInfoExtAndVip = goodsInfoMapper.getGoodsUserInfoExtAndVip(goodsId);
        if(goodsUserInfoExtAndVip != null){
            goodsDto.setAuthType((Integer)goodsUserInfoExtAndVip.get("auth_type") == 2?1:2);
            //已认证=可用，未认证=不可用
            goodsDto.setIsCoupon(goodsDto.getAuthType());
            goodsDto.setVipLevel((Integer)goodsUserInfoExtAndVip.get("vip_level"));
            Long credit = (Long)goodsUserInfoExtAndVip.get("credit");
            goodsDto.setCredit(credit);
            if(credit != null){
                if(credit < -100){//差
                    goodsDto.setCreditLevel(1);
                }else if(-100 <= credit && credit < 1000){//低
                    goodsDto.setCreditLevel(2);
                }else if(1000 <= credit && credit < 10000){//中
                    goodsDto.setCreditLevel(3);
                }else if(10000 <= credit && credit < 500000){//高
                    goodsDto.setCreditLevel(4);
                }else if(500000 <= credit){//优
                    goodsDto.setCreditLevel(5);
                }
            }
        }
        //卖家信息
        //粉丝数
        goodsDto.setFansCount(goodsInfoMapper.getGoodsUserFansCount(goodsId));
        //关注数
        goodsDto.setCollectCount(goodsInfoMapper.getGoodsUserCollectCount(goodsId));
        return goodsDto;
    }

    @Override
    public List<GoodsDto> similarGoods(Integer goodsId,Integer pageNum, Integer pageSize) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        //根据商品
        if(goodsInfo == null){
            throw new ServiceException("获取商品失败！");
        }else{
            //根据商品获取此商品的所属的分类列表
            //按照会员和认证店铺进行排序
            List<Integer> goodsCategoryIds = goodsCorrelationMapper.selectCategoryIdByGoodsId(goodsId);
            //根据分类列表获取商品的列表
            List<GoodsInfo> goodsInfos = goodsInfoMapper.selectByCategoryIds(goodsId,goodsCategoryIds,pageNum*pageSize,pageSize);
            //获取商品的分类集合
            List<GoodsDto> goodsDtos = GoodsDto.toDtoList(goodsInfos);
//            for(GoodsInfo model:goodsInfos){
//                goodsDtos.add(setDtoByGoodsInfo(null,model));
//            }
            return goodsDtos;
        }
    }

    @Override
    public List<BannerIndexDto> getBanner(Integer type) throws ServiceException {
        List<BannerIndex> banners = bannerIndexMapper.getBanner(type);
        List<BannerIndexDto> bannerIndexDtos = BannerIndexDto.toDtoList(banners);
        return bannerIndexDtos;
    }
    @Override
    public List<SearchDto> hotSearch() throws ServiceException {
        List<HotSearch> hotSearchList = hotSearchMapper.getHotSearchList();
        List<SearchDto> searchDtos = SearchDto.toDtoList(hotSearchList);
        if(searchDtos == null || searchDtos.size()==0){
            throw new ServiceException("获取热门查询失败！");
        }else{
            return searchDtos;
        }
    }

    @Override
    public List<GoodsCategoryDto> hotCategary() throws ServiceException {
        List<GoodsCategory> hotCategaryList = goodsCategoryMapper.getHotCategaryList();
        List<GoodsCategoryDto> goodsCategoryDtos = GoodsCategoryDto.toDtoList(hotCategaryList);
        if(goodsCategoryDtos == null || goodsCategoryDtos.size()==0){
            throw new ServiceException("获取热门分类失败！");
        }else{
            return goodsCategoryDtos;
        }
    }

    @Override
    public List<GoodsQuickSearchDto> quickSearch() throws ServiceException {
        List<GoodsQuickSearch> quickSearchList = goodsQuickSearchMapper.getQuickSearchList();
        List<GoodsQuickSearchDto> goodsQuickSearchDtos = GoodsQuickSearchDto.toDtoList(quickSearchList);
        if(goodsQuickSearchDtos == null || goodsQuickSearchDtos.size()==0){
            throw new ServiceException("获取快速查询条件列表失败！");
        }else{
            return goodsQuickSearchDtos;
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    @TxTransaction(isStart=true)
    public void updateGoodsStatus(Integer goodsId,Integer status) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        if(goodsInfo == null){
            throw new ServiceException("获取商品失败！");
        }else{
            try{
                if(goodsInfo.getType().intValue() == Status.AUCTION.getValue()){
                    goodsInfo.setLastIntervalTime(DateStampUtils.getTimesteamp());
                }
                if(status == 2){//已售出
                    //拒绝此商品的所有议价
                    //压价信息
                    List<GoodsBargain> goodsBargains = goodsBargainMapper.selectAllByGoodsId(goodsId,1);//状态 1申请 2同意 3拒绝 4取消
                    for(GoodsBargain bargain:goodsBargains){
                        bargain.setStatus(Status.BARGAIN_REFUSE.getValue());
                        goodsBargainMapper.updateByPrimaryKeySelective(bargain);
                        //退回余额
                        BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(bargain.getUserId(), bargain.getPrice(),Status.ADD.getValue()));
                        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                        }
                        //买家新增余额账单
                        baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(bargain.getUserId(),bargain.getPrice(),Status.PAY_TYPE_BALANCE.getValue(),Status.REFUND.getValue(),bargain.getBargainNo(),"【"+goodsInfo.getName()+"】",goodsInfo.getUserId(),bargain.getUserId(),Status.BARGAIN.getValue(),bargain.getBargainNo()));
                        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                        }
                        schedualMessageService.easemobMessage(bargain.getUserId().toString(),
                                "您对商品【"+goodsInfo.getName()+"】的议价已被卖家拒绝，点击此处查看详情",Status.SELLER_MESSAGE.getMessage(),Status.JUMP_TYPE_GOODS.getMessage(),bargain.getGoodsId().toString());
                    }
                }
                goodsInfo.setStatus(status);
                goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
            }catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("修改商品状态出错，请稍后再试！");
            }
        }
    }



    @Override
    public Pager categoryPage(AdminGoodsParam param) throws ServiceException {
        Integer total = goodsCategoryMapper.countPage(param.getKeyword(),param.getStatus(),param.getType(),param.getParentId());
        //商品分类列表
        param.setOrders("parent_id,id");
        List<GoodsCategory> goodsCategoryDtos = goodsCategoryMapper.getPage(param.getStart(),param.getLimit(),
                param.getKeyword(),param.getStatus(),param.getType(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType(),param.getParentId());
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(AdminGoodsCategoryDto.toDtoList(goodsCategoryDtos));
        return pager;
    }

    @Override
    public Pager getGoodsPage(AdminGoodsParam param) throws ServiceException {
        Integer total = goodsInfoMapper.countPage(param.getType(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());
        //商品列表
        List<GoodsInfo> goodsList = goodsInfoMapper.getGoodsPage(param.getType(),param.getStart(),param.getLimit(),
                param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        List<AdminGoodsDto> dtos = new ArrayList<>();
        //遍历商品列表，添加到GoodsDtos中
        for (GoodsInfo goodsInfo:goodsList) {
            AdminGoodsDto goodsDto = new AdminGoodsDto(goodsInfo);

            List<GoodsImg> goodsImgs = goodsImgMapper.getImgsByGoodsId(goodsInfo.getId());
            
            List<GoodsCorrelation> goodsCorrelations = goodsCorrelationMapper.getCorrelationsByGoodsId(goodsInfo.getId());
            
            goodsDto.setGoodsCorrelations(GoodsCorrelationDto.toDtoList(goodsCorrelations));
//            String mainImgUrl = null;
//            for(GoodsImg goodsImg:goodsImgs){
//                if(goodsImg.getType() == 1){
//                    mainImgUrl = goodsImg.getImgUrl();
//                }
//                if(goodsImg.getType() == 3){
//                	mainImgUrl = goodsImg.getImgUrl();
//                }
//            }
//            goodsDto.setMainUrl(mainImgUrl);
            
            goodsDto.setGoodsImgDtos(GoodsImgDto.toDtoList(goodsImgs));

            String ret = schedualUserService.userIsAuth(goodsInfo.getUserId());
            if(JSONObject.parseObject(ret).getIntValue("code")==0&&JSONObject.parseObject(ret).getBoolean("data")) {
                goodsDto.setAuthType(1);
            }
            dtos.add(goodsDto);
        }
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(dtos);
        return pager;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    @TxTransaction(isStart=true)
    public void updateGoods(AdminGoodsParam param) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(param.getId());
        if(goodsInfo == null){
            throw new ServiceException("商品不存在！");
        }else{
            //修改商品信息
            if(param.getSort() != null){
                goodsInfo.setSort(param.getSort());
            }
            //允许后台修改商品所属分类
            if(param.getGoodsCategoryIds() != null && param.getGoodsCategoryIds().length > 0){
                //删除旧商品分类关联表
                List<GoodsCorrelation> goodsCorrelations = goodsCorrelationMapper.getCorrelationsByGoodsId(goodsInfo.getId());
                for(GoodsCorrelation correlation:goodsCorrelations){
                    goodsCorrelationMapper.deleteByPrimaryKey(correlation.getId());
                }
                //初始化商品分类关联表
                for(int i=0;i<param.getGoodsCategoryIds().length;i++){
                    GoodsCorrelation goodsCorrelation = new GoodsCorrelation();
                    goodsCorrelation.setGoodsId(goodsInfo.getId());
                    goodsCorrelation.setAddTime(DateStampUtils.getTimesteamp());
                    goodsCorrelation.setGoodsCategoryId(param.getGoodsCategoryIds()[i]);
                    goodsCorrelation.setCategoryParentId(goodsCategoryMapper.selectParentId(param.getGoodsCategoryIds()[i]));
                    goodsCorrelationMapper.insert(goodsCorrelation);
                }
            }

            if(param.getIsAppraisal() != null){
                goodsInfo.setIsAppraisal(param.getIsAppraisal());
            }

            if(param.getName() != null){
                goodsInfo.setName(param.getName());
            }

            if(param.getDescription() != null){
                goodsInfo.setDescription(param.getDescription());
            }
            if(param.getStatus() != null){
                goodsInfo.setStatus(param.getStatus());//状态 1出售中 2已售出 3已下架（已结束） 5删除
                if(param.getStatus().equals(5)){
                    //取消所有议价
                    List<GoodsBargain> goodsBargains = goodsBargainMapper.selectAllByGoodsId(param.getId(),Status.BARGAIN_APPLY.getValue());//状态 1申请 2同意 3拒绝 4取消
                    for(GoodsBargain bargain:goodsBargains){
                        bargainService.updateBargain(goodsInfo.getUserId(),param.getId(),bargain.getId(),Status.BARGAIN_REFUSE.getValue());
                    }
                }
            }
            if(param.getIsAppraisal() != null){
                goodsInfo.setIsAppraisal(param.getIsAppraisal());
            }
            goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
            if(param.getStatus()!=null&&param.getStatus().intValue() == 5){
                if(StringUtils.isEmpty(param.getContent())){
                    throw new ServiceException("删除理由不能为空！");
                }
                //卖家-20信誉度
                String result = schedualWalletService.updateCredit(goodsInfo.getUserId(),Credit.DELETE_FAKE.getCredit(),Status.SUB.getValue());
                BaseResp br = ParseReturnValue.getParseReturnValue(result);
                if(!br.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(br.getCode(),br.getReport());
                }
                //很抱歉，您的商品/抢购【名称】已被官方删除，删除理由：……。点击查看详情
                if(goodsInfo.getType().equals(Status.GOODS.getValue())){
                    schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                            "很抱歉，您的商品【"+goodsInfo.getName()+"】已被官方删除，删除理由："+param.getContent()+"。点击查看详情",
                            Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_GOODS.getMessage(),goodsInfo.getId().toString());
                }else{
                    schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                            "很抱歉，您的抢购【"+goodsInfo.getName()+"】已被官方删除，删除理由："+param.getContent()+"。点击查看详情",
                            Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_AUCTION.getMessage(),goodsInfo.getId().toString());
                }
            }
        }
    }

    @Override
    public void updateCategory(Integer categoryId,Integer parentId,String name,String imgUrl,Integer sort,Integer type,Integer status) throws ServiceException {
        if(status == 2){
            goodsCategoryMapper.deleteByPrimaryKey(categoryId);
        }else{
            GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(categoryId);
            if(goodsCategory == null){
                throw new ServiceException("未找到分类信息！");
            }
            goodsCategory.setParentId(parentId);
            if(StringUtils.isNotEmpty(name)){
                goodsCategory.setName(name);
            }
            if(StringUtils.isNotEmpty(imgUrl)){
                goodsCategory.setImgUrl(imgUrl);
            }
            if(sort != null){
                goodsCategory.setSort(sort);
            }
            if(type != null){
                goodsCategory.setType(type);
            }
            if(status != null){
                goodsCategory.setStatus(status);
            }
            goodsCategoryMapper.updateByPrimaryKeySelective(goodsCategory);
        }
    }

    @Override
    public void addCategory(AdminGoodsParam param) throws ServiceException {
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.setParentId(param.getParentId());
        goodsCategory.setName(param.getName());
        goodsCategory.setImgUrl(param.getImgUrl());
        goodsCategory.setSort(param.getSort());
        goodsCategory.setType(param.getType());
        goodsCategory.setStatus(param.getStatus());
        goodsCategory.setAddTime(DateStampUtils.getTimesteamp());
        goodsCategoryMapper.insert(goodsCategory);
    }

    @Override
    public AdminGoodsDto adminGoodsDetail(Integer goodsId) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        AdminGoodsDto goodsDto = new AdminGoodsDto(goodsInfo);

        //图片
        List<GoodsImg> goodsImgs = goodsImgMapper.getImgsByGoodsId(goodsInfo.getId());

        goodsDto.setGoodsImgDtos(GoodsImgDto.toDtoList(goodsImgs));

        String ret = schedualUserService.userIsAuth(goodsInfo.getUserId());
        if(JSONObject.parseObject(ret).getIntValue("code")==0&&JSONObject.parseObject(ret).getBoolean("data")) {
            goodsDto.setAuthType(1);
        }
        //分类
        List<GoodsCorrelationDto> goodsCorrelationDtos = GoodsCorrelationDto.toDtoList(goodsCorrelationMapper.getCorrelationsByGoodsId(goodsInfo.getId()));
        goodsDto.setGoodsCorrelations(goodsCorrelationDtos);

        goodsDto.setCommentCount(goodsCommentMapper.selectCount(goodsInfo.getId()));
        return goodsDto;
    }




    @Override
    public Integer processTodayGoods() throws ServiceException {
        Integer todayGoodsCount = goodsInfoMapper.getTodayGoodsCount();
        return todayGoodsCount;
    }

    @Override
    public Integer processAllGoods() throws ServiceException {
        Integer allGoodsCount = goodsInfoMapper.getAllGoodsCount();
        return allGoodsCount;
    }


	@Override
	public List<GoodsCategory> getCategory() {
		return goodsCategoryMapper.getTopCategory();
	}




	@Override
	public List<GoodsCategory> getCategoryByParent(Integer parentId) {
		return goodsCategoryMapper.getChildCategoryList(parentId);
	}

	

    @Override
    @Transactional(rollbackFor=Exception.class)
    @TxTransaction(isStart=true)
    public Object setGoodsTop(Integer userId, Integer goodsId, Integer payType, String payPwd) throws ServiceException {
        //1、判断商品与用户关系 2、判断支付方式——免费置顶、下单置顶
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        if(goodsInfo == null){
            throw new ServiceException("商品异常！");
        }
        if(!goodsInfo.getUserId().equals(userId)){
            throw new ServiceException("无权操作！");
        }
        if(!goodsInfo.getStatus().equals(Status.GOODS_IN_SALE.getValue())){
            throw new ServiceException("商品状态异常！");
        }
        StringBuffer payInfo = new StringBuffer();
        if(payType == null){
            //免费置顶：1、查看用户会员等级 2、查看用户免费置顶次数 3、生成置顶详情 4、修改免费置顶次数
            BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.getUserVipLevel(userId));
            if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                throw new ServiceException(baseResp.getCode(),baseResp.getReport());
            }
            Integer vipLevel = (Integer)baseResp.getData();
            if(vipLevel != null){
                baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.getFreeTopCount(userId));
                if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                }
                Integer topCount = (Integer) baseResp.getData();
                if(topCount > 0){
                    //置顶详情
                    GoodsTopDetail goodsTopDetail = new GoodsTopDetail() ;
                    goodsTopDetail.setUserId(userId);
                    goodsTopDetail.setGoodsId(goodsId);
                    goodsTopDetail.setAddTime(DateStampUtils.getTimesteamp());
                    goodsTopDetail.setStatus(Status.IS_TOP.getValue());
                    goodsTopDetailMapper.insert(goodsTopDetail);
                    //减少免费次数
                    baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateTopCount(userId,Status.SUB.getValue(),1));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                    goodsInfo.setCommentTime(DateStampUtils.getTimesteamp());
                    goodsInfoMapper.updateByPrimaryKey(goodsInfo);
                    schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                            "您的"+(goodsInfo.getType().equals(Status.AUCTION.getValue())?"抢购【":"商品【")+goodsInfo.getName()+"】已成功置顶啦~",
                            Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),goodsInfo.getId().toString());
                    return null;
                }else{
                    throw new ServiceException("无免费置顶次数！");
                }
            }else{
                throw new ServiceException("此用户无法免费置顶！");
            }
        }else{
            //下单置顶：生成置顶订单
            GoodsTopOrder goodsTopOrder = new GoodsTopOrder();
            goodsTopOrder.setUserId(userId);
            //订单号
            final IdGenerator idg = IdGenerator.INSTANCE;
            String id = idg.nextId();
            goodsTopOrder.setOrderNo(id);
            goodsTopOrder.setCount(1);
            goodsTopOrder.setAmount(new BigDecimal(2).setScale(2,BigDecimal.ROUND_HALF_UP));
            goodsTopOrder.setStatus(Status.ORDER_UNPAID.getValue());
            goodsTopOrder.setAddTime(DateStampUtils.getTimesteamp());
            goodsTopOrder.setGoodsId(goodsId);
            goodsTopOrderMapper.insert(goodsTopOrder);
            if(payType.equals(Status.PAY_TYPE_WECHAT.getValue())){
                String getWechatOrder = schedualWalletService.orderPayByWechat(goodsTopOrder.getOrderNo(), goodsTopOrder.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.top_wechat_notify.getNotifUrl());
                BaseResp result = ParseReturnValue.getParseReturnValue(getWechatOrder);
                if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(result.getCode(),result.getReport());
                }
                WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(result.getData().toString()), WechatPayDto.class);
                return wechatPayDto;
            }else if(payType.equals(Status.PAY_TYPE_ALIPAY.getValue())){
                String getALiOrder = schedualWalletService.orderPayByALi(goodsTopOrder.getOrderNo(), goodsTopOrder.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.top_alipay_notify.getNotifUrl());
                BaseResp result = ParseReturnValue.getParseReturnValue(getALiOrder);
                if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(result.getCode(),result.getReport());
                }
                payInfo.append(result.getData());
            }else if(payType.equals(Status.PAY_TYPE_BALANCE.getValue())){
                String verifyPayPwd = schedualUserService.verifyPayPwd(userId, payPwd);
                BaseResp result = ParseReturnValue.getParseReturnValue(verifyPayPwd);
                if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(result.getCode(),result.getReport());
                }
                if (!(boolean)result.getData()) {
                    throw new ServiceException(ReCode.PAYMENT_PASSWORD_ERROR.getValue(),ReCode.PAYMENT_PASSWORD_ERROR.getMessage());
                }else{
                    BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.updateBalance(userId, goodsTopOrder.getAmount(), Status.SUB.getValue()));
                    if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(baseResp.getCode(),baseResp.getReport());
                    }
                }
                payInfo.append("余额支付成功！");
                //生成商品置顶详情
                updateOrder(goodsTopOrder.getOrderNo(),null,Status.PAY_TYPE_BALANCE.getValue());
            }else if(payType.equals(Status.PAY_TYPE_MINI.getValue())){
                String getMiniOrder = schedualWalletService.orderPayByWechatMini(userId,goodsTopOrder.getOrderNo(), goodsTopOrder.getAmount(), NotifyUrl.mini_notify.getNotifUrl()+NotifyUrl.top_wechat_notify.getNotifUrl());
                BaseResp result = ParseReturnValue.getParseReturnValue(getMiniOrder);
                if(!result.getCode().equals(ReCode.SUCCESS.getValue())){
                    throw new ServiceException(result.getCode(),result.getReport());
                }
                WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(result.getData().toString()), WechatPayDto.class);
                return wechatPayDto;
            }else{
                throw new ServiceException("支付方式错误！");
            }
        }
        return payInfo.toString();
    }

    /**
     * 修改订单状态
     * @param orderNo
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    @TxTransaction(isStart=true)
    public boolean updateOrder(String orderNo, String thirdOrderNo, Integer payType) throws ServiceException {
        GoodsTopOrder goodsTopOrder = goodsTopOrderMapper.selectByOrderNo(orderNo);
        if(goodsTopOrder == null){
            throw new ServiceException("订单不存在！");
        }
        //置顶详情
        GoodsTopDetail goodsTopDetail = new GoodsTopDetail() ;
        goodsTopDetail.setUserId(goodsTopOrder.getUserId());
        goodsTopDetail.setGoodsId(goodsTopOrder.getGoodsId());
        goodsTopDetail.setAddTime(DateStampUtils.getTimesteamp());
        goodsTopDetail.setStatus(Status.IS_TOP.getValue());
        goodsTopDetailMapper.insert(goodsTopDetail);
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsTopOrder.getGoodsId());
        goodsInfo.setCommentTime(DateStampUtils.getTimesteamp());
        goodsInfoMapper.updateByPrimaryKey(goodsInfo);
        goodsTopOrder.setStatus(Status.ORDER_COMPLETE.getValue());
        goodsTopOrderMapper.updateByPrimaryKey(goodsTopOrder);
        //新增余额账单
        BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBalanceDetail(goodsTopOrder.getUserId(),goodsTopOrder.getAmount(),payType,Status.EXPEND.getValue(),orderNo,"【"+goodsInfo.getName()+"】置顶",null,goodsTopOrder.getUserId(),Status.GOODS_TOP.getValue(),thirdOrderNo));
        if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
            throw new ServiceException(baseResp.getCode(),baseResp.getReport());
        }
        //您的商品/抢购【名称】已成功置顶啦~
        schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                "您的"+(goodsInfo.getType().equals(Status.AUCTION.getValue())?"抢购【":"商品【")+goodsInfo.getName()+"】已成功置顶啦~",
                Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),goodsInfo.getId().toString());
        return true;
    }

    @Override
    public ShareDto getShareData(Integer shopId) throws ServiceException {
        ShareDto shareDto = new ShareDto();
        int count = goodsInfoMapper.goodsCount(shopId);
        shareDto.setGoodsCount(count);
        List<GoodsInfo> goodsList = goodsInfoMapper.getShareGoodsImgs(shopId);
        List<GoodsDto> goodsDtos = GoodsDto.toDtoList(goodsList);
        shareDto.setGoodsList(goodsDtos);
        SysProperty ruleByKey = sysPropertyMapper.getRuleByKey(Status.SHARE_INVITE_RULE.getMessage());
        if(ruleByKey != null){
            shareDto.setRule(ruleByKey.getValue());
        }
        SysProperty shareImgUrl = sysPropertyMapper.getRuleByKey(Status.SHARE_IMG_URL.getMessage());
        if(shareImgUrl != null){
            shareDto.setImgUrl(shareImgUrl.getValue());
        }
        return shareDto;
    }
}
