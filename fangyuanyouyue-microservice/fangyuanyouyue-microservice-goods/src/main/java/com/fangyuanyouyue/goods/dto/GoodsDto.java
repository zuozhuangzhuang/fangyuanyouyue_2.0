package com.fangyuanyouyue.goods.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.model.GoodsCorrelation;
import com.fangyuanyouyue.goods.model.GoodsImg;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.model.UserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品信息Dto
 */
@Getter
@Setter
@ToString
public class GoodsDto {
    //GoodsInfo
    private Integer goodsId;//商品ID

    private String description;//商品详情

    private BigDecimal price;//商品价格

    private BigDecimal postage;//运费

    private Integer sort;//排序

    private String label;//标签

    private Integer status;//状态 1出售中 2已售出 3已下架（已结束） 5删除

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

    private String videoImg;//视频截图路径

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

    private Long credit;//信誉度

    private Integer creditLevel;//信誉度等级 1差 2低 3中 4高 5优

    //GoodsBargain
    private List<BargainDto> bargainDtos;//用户议价信息列表

    //OrderInfo
    private Integer orderId;//订单ID

    private Integer orderStatus;//订单状态（不存在订单时为null）

    //抢购降价历史
    private List<GoodsIntervalHistoryDto> historyDtos;//抢购降价历史


    private Integer videoLength;//视频长度

    private BigDecimal startPrice;//初始价格

    public GoodsDto() {
    }
    
    public GoodsDto(GoodsInfo goodsInfo) {

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
        this.videoLength = goodsInfo.getVideoLength();
        this.startPrice = goodsInfo.getStartPrice();
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
            this.addTime = DateUtil.getFormatDate(goodsInfo.getAddTime(), DateUtil.DATE_FORMT_YEAR);
            this.updateTime = DateUtil.getFormatDate(goodsInfo.getUpdateTime(), DateUtil.DATE_FORMT_YEAR);
            this.videoUrl = goodsInfo.getVideoUrl();
            this.videoLength = goodsInfo.getVideoLength();
            this.startPrice = goodsInfo.getStartPrice();

        }
        //GoodsImg
        if(goodsImgs != null && goodsImgs.size()>0){
            List<GoodsImgDto> goodsImgDtos = GoodsImgDto.toDtoList(goodsImgs);
            this.goodsImgDtos = goodsImgDtos;
            for(GoodsImgDto goodsImgDto:goodsImgDtos){
                if(goodsImgDto.getType() == 1){
                    this.mainUrl = goodsImgDto.getImgUrl();
                }
                if(goodsImgDto.getType() == 3){
                    this.videoImg = goodsImgDto.getImgUrl();
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
}
