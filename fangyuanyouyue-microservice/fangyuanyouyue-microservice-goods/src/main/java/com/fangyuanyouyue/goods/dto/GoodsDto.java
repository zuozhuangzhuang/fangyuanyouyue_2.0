package com.fangyuanyouyue.goods.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.model.GoodsCorrelation;
import com.fangyuanyouyue.goods.model.GoodsImg;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.model.UserInfo;

/**
 * 商品信息Dto
 */
public class GoodsDto {
    //GoodsInfo
    private Integer goodsId;//商品ID

    private String description;//商品详情

    private BigDecimal price;//商品价格

    private BigDecimal postage;//运费

    private Integer sort;//排序

    private String label;//标签

    private Integer status;//状态 普通商品 1出售中 2已售出 5删除

    private BigDecimal floorPrice;//最低价

    private Integer intervalTime;//降价时间间隔(单位是秒)

    private BigDecimal markdown;//降价幅度

    private String lastIntervalTime;//最后一次降价时间

    private Integer isCoupon=2;//是否可用券 1可用 2不可用

    private Integer authType;//官方认证状态 1已认证 2未认证

    private Integer isAppraisal;//是否官方鉴定 1已鉴定 2未鉴定

    private Integer isCollect;//是否收藏/关注 1未关注未收藏（商品/抢购） 2已关注未收藏(抢购) 3未关注已收藏（商品/抢购） 4已关注已收藏(抢购)

    private String addTime;//发布时间

    private String updateTime;//修改时间

    //GoodsImg
    private List<GoodsImgDto> goodsImgDtos;//商品图片

    private String mainUrl;//商品主图

    private String videoUrl;//视频路径

    //GoodsCorrelation
    private List<GoodsCorrelationDto> goodsCorrelations;//商品分类

    //GoodsComment
    private List<GoodsCommentDto> goodsCommentDtos;//商品评论列表

    private Integer commentCount;//商品评论总数

    //公用
    private Integer type;//类型 1普通商品 2抢购商品 /1主图（展示在第一张的图片） 2次图

    //商家相关
    private Integer userId;//发布用户id

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private String name;//商品名称

    private String userAddress;//店家地址

    private Integer fansCount;//粉丝数量

    private Integer collectCount;//关注数量

    private Integer level;//用户等级

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private Integer credit;//信誉度

    public GoodsDto() {
    }

    public GoodsDto(UserInfo user, GoodsInfo goodsInfo, List<GoodsImg> goodsImgs, List<GoodsCorrelation> goodsCorrelations, List<GoodsCommentDto> goodsCommentDtos) {
        if(user != null){
            this.nickName = user.getNickName();
            this.headImgUrl = user.getHeadImgUrl();
            this.userAddress = user.getAddress();
            this.level = user.getLevel();
        }
        if(goodsInfo != null){
            //GoodsInfo
            this.goodsId = goodsInfo.getId();
            this.userId = goodsInfo.getUserId();
            this.name = goodsInfo.getName();
            this.description = goodsInfo.getDescription();
            this.price = goodsInfo.getPrice();
            this.postage = goodsInfo.getPostage();
            this.sort = goodsInfo.getSort();
            this.label = goodsInfo.getLabel();
            this.type = goodsInfo.getType();
            this.status = goodsInfo.getStatus();
            this.floorPrice = goodsInfo.getFloorPrice();
            this.intervalTime = goodsInfo.getIntervalTime();
            this.markdown = goodsInfo.getMarkdown();
            if(goodsInfo.getLastIntervalTime() != null){
                this.lastIntervalTime = DateUtil.getFormatDate(goodsInfo.getLastIntervalTime(), DateUtil.DATE_FORMT);
            }
            this.isAppraisal = goodsInfo.getIsAppraisal();
            this.addTime = DateUtil.getFormatDate(goodsInfo.getAddTime(), DateUtil.DATE_FORMT);
            this.updateTime = DateUtil.getFormatDate(goodsInfo.getUpdateTime(), DateUtil.DATE_FORMT);
            this.videoUrl = goodsInfo.getVideoUrl();
        }
        //GoodsImg
        if(goodsImgs != null && goodsImgs.size()>0){
            List<GoodsImgDto> goodsImgDtos = GoodsImgDto.toDtoList(goodsImgs);
            this.goodsImgDtos = goodsImgDtos;
            for(GoodsImgDto goodsImgDto:goodsImgDtos){
                if(goodsImgDto.getSort() == 1){
                    this.mainUrl = goodsImgDto.getImgUrl();
                }
            }
        }
        if(goodsCorrelations != null && goodsCorrelations.size()>0){
            //GoodsCorrelation
            this.goodsCorrelations = GoodsCorrelationDto.toDtoList(goodsCorrelations);
        }
        //GoodsComment
        if(goodsCommentDtos != null && goodsCommentDtos.size()>0){
            this.goodsCommentDtos = goodsCommentDtos;
        }
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<GoodsImgDto> getGoodsImgDtos() {
        return goodsImgDtos;
    }

    public void setGoodsImgDtos(List<GoodsImgDto> goodsImgDtos) {
        this.goodsImgDtos = goodsImgDtos;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public List<GoodsCorrelationDto> getGoodsCorrelations() {
        return goodsCorrelations;
    }

    public void setGoodsCorrelations(List<GoodsCorrelationDto> goodsCorrelations) {
        this.goodsCorrelations = goodsCorrelations;
    }

    public List<GoodsCommentDto> getGoodsCommentDtos() {
        return goodsCommentDtos;
    }

    public void setGoodsCommentDtos(List<GoodsCommentDto> goodsCommentDtos) {
        this.goodsCommentDtos = goodsCommentDtos;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public BigDecimal getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(BigDecimal floorPrice) {
        this.floorPrice = floorPrice;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getLastIntervalTime() {
        return lastIntervalTime;
    }

    public void setLastIntervalTime(String lastIntervalTime) {
        this.lastIntervalTime = lastIntervalTime;
    }

    public BigDecimal getMarkdown() {
        return markdown;
    }

    public void setMarkdown(BigDecimal markdown) {
        this.markdown = markdown;
    }


    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getIsAppraisal() {
        return isAppraisal;
    }

    public void setIsAppraisal(Integer isAppraisal) {
        this.isAppraisal = isAppraisal;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public Integer getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(Integer isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getAddTime() {
        return addTime;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
