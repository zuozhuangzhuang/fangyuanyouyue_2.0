package com.fangyuanyouyue.goods.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Arrays;

@ApiModel(value = "商品参数类")
public class GoodsParam{
    //公用
    @ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
    private Integer start; // 起始页

    @ApiModelProperty(name = "limit", value = "限制页", dataType = "int",hidden = true)
    private Integer limit; // 限制页

    @ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
    private Integer type;//类型 goodsInfo：1普通商品 2秒杀商品 goodsImg：1主图 2次图 goodsCategory：1普通 2热门 bannerIndex：业务类型,0:商品 1：个人 collect: 1关注 2收藏

    @ApiModelProperty(name = "goodsId", value = "商品id", dataType = "int",hidden = true)
    private Integer goodsId;//商品id

    @ApiModelProperty(name = "goodsIds", value = "商品id数组", dataType = "int",hidden = true)
    private Integer[] goodsIds;//商品id数组

    @ApiModelProperty(name = "imgUrl", value = "图片地址", dataType = "String",hidden = true)
    private String imgUrl;//图片地址

    @ApiModelProperty(name = "imgUrls", value = "图片地址数组", dataType = "String",hidden = true)
    private String[] imgUrls;//图片地址数组

    @ApiModelProperty(name = "price", value = "价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal price;//GoodsInfo:价格 Appraisal:鉴定赏金

    @ApiModelProperty(name = "status", value = "状态", dataType = "int",hidden = true)
    private Integer status;//状态 GoodsComment：1正常 2隐藏 GoodsInfo：普通商品 1出售中 2已售出 5删除 Appraisal: 0申请 1真 2假 3存疑

    @ApiModelProperty(name = "title", value = "描述标题", dataType = "String",hidden = true)
    private String title;//BannerIndex描述标题 Appraisal:鉴定标题

    @ApiModelProperty(name = "description", value = "商品详情", dataType = "String",hidden = true)
    private String description;//GoodsInfo:商品详情  Appraisal:描述

    //GoodsInfo
    @ApiModelProperty(name = "userId", value = "卖家id", dataType = "int",hidden = true)
    private Integer userId;//卖家id

    @ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
    private String token;//用户token


    @ApiModelProperty(name = "goodsInfoName", value = "商品名称", dataType = "String",hidden = true)
    private String goodsInfoName;//商品名称


    @ApiModelProperty(name = "postage", value = "运费", dataType = "BigDecimal",hidden = true)
    private BigDecimal postage;//运费

    @ApiModelProperty(name = "label", value = "标签", dataType = "String",hidden = true)
    private String label;//标签

    @ApiModelProperty(name = "sort", value = "排序", dataType = "int",hidden = true)
    private Integer sort;//排序

    @ApiModelProperty(name = "floorPrice", value = "最低价", dataType = "BigDecimal",hidden = true)
    private BigDecimal floorPrice;//最低价

    @ApiModelProperty(name = "intervalTime", value = "降价时间间隔", dataType = "int",hidden = true)
    private Integer intervalTime;//降价时间间隔(单位秒)

    @ApiModelProperty(name = "markdown", value = "降价幅度", dataType = "BigDecimal",hidden = true)
    private BigDecimal markdown;//降价幅度

    @ApiModelProperty(name = "videoUrl", value = "视频路径", dataType = "String",hidden = true)
    private String videoUrl;//视频路径


    //GoodsImg
    @ApiModelProperty(name = "goodsImgId", value = "商品图片表ID", dataType = "int",hidden = true)
    private Integer goodsImgId;//商品图片表ID

//    @ApiModelProperty(name = "file1", value = "图片文件1路径", dataType = "file",hidden = true)
//    private String file1;//图片文件1路径
//
//    @ApiModelProperty(name = "file2", value = "图片文件2路径", dataType = "file",hidden = true)
//    private String file2;
//
//    @ApiModelProperty(name = "file3", value = "图片文件3路径", dataType = "file",hidden = true)
//    private String file3;
//
//    @ApiModelProperty(name = "file4", value = "图片文件4路径", dataType = "file",hidden = true)
//    private String file4;
//
//    @ApiModelProperty(name = "file5", value = "图片文件5路径", dataType = "file",hidden = true)
//    private String file5;
//
//    @ApiModelProperty(name = "file6", value = "图片文件6路径", dataType = "file",hidden = true)
//    private String file6;



   //GoodsComment
    @ApiModelProperty(name = "goodsCommentId", value = "商品评论表ID", dataType = "int",hidden = true)
    private Integer goodsCommentId;//商品评论表ID

    @ApiModelProperty(name = "commentId", value = "回复评论id", dataType = "int",hidden = true)
    private Integer commentId;//回复评论id

    @ApiModelProperty(name = "content", value = "评论内容", dataType = "String",hidden = true)
    private String content;//评论内容

    @ApiModelProperty(name = "img1Url", value = "图片地址1", dataType = "String",hidden = true)
    private String img1Url;//图片地址1

    @ApiModelProperty(name = "img2Url", value = "图片地址2", dataType = "String",hidden = true)
    private String img2Url;//图片地址2

    @ApiModelProperty(name = "img3Url", value = "图片地址3", dataType = "String",hidden = true)
    private String img3Url;//图片地址3



    //GoodsCategory
    @ApiModelProperty(name = "goodsCategoryId", value = "商品类目表ID", dataType = "int",hidden = true)
    private Integer goodsCategoryId;//商品类目表ID

    @ApiModelProperty(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）", dataType = "int",hidden = true)
    private Integer[] goodsCategoryIds;//商品分类数组（同一商品可多种分类）

    @ApiModelProperty(name = "parentId", value = "上级id", dataType = "int",hidden = true)
    private Integer parentId;//上级id

    @ApiModelProperty(name = "goodsCategoryName", value = "类目名称", dataType = "String",hidden = true)
    private String goodsCategoryName;//类目名称



    //CartInfo
    @ApiModelProperty(name = "cartInfoId", value = "购物车表ID", dataType = "int",hidden = true)
    private Integer cartInfoId;//购物车表ID



    //CartDetail
    @ApiModelProperty(name = "cartDetailId", value = "购物车明细表ID", dataType = "int",hidden = true)
    private Integer cartDetailId;//购物车明细表ID

    @ApiModelProperty(name = "cartDetailIds", value = "购物车明细表ID数组", dataType = "int",hidden = true)
    private Integer[] cartDetailIds;//购物车明细表ID数组

    @ApiModelProperty(name = "cartId", value = "购物车id", dataType = "int",hidden = true)
    private Integer cartId;//购物车id

    @ApiModelProperty(name = "count", value = "数量", dataType = "int",hidden = true)
    private Integer count;//数量


    //条件查询
    @ApiModelProperty(name = "search", value = "搜索词条", dataType = "String",hidden = true)
    private String search;//搜索词条

    @ApiModelProperty(name = "synthesize", value = "综合", dataType = "int",hidden = true)
    private Integer synthesize;//综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序

    @ApiModelProperty(name = "priceMin", value = "最小价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal priceMin;//最小价格

    @ApiModelProperty(name = "priceMax", value = "最大价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal priceMax;//最大价格

    @ApiModelProperty(name = "quality", value = "品质", dataType = "int",hidden = true)
    private Integer quality;//品质 1：认证店铺 2：官方保真 3：高信誉度 4：我的关注


    //轮播图 BannerIndex
    @ApiModelProperty(name = "bannerIndexId", value = "轮播图ID", dataType = "int",hidden = true)
    private Integer bannerIndexId;//轮播图ID

    @ApiModelProperty(name = "businessId", value = "业务ID:商品ID/用户ID", dataType = "int",hidden = true)
    private Integer businessId;//业务ID:商品ID/用户ID

    @ApiModelProperty(name = "jumpType", value = "跳转类型,0:商品 1：个人", dataType = "int",hidden = true)
    private Integer jumpType;//跳转类型,0:商品 1：个人


    //Collect
    @ApiModelProperty(name = "collectId", value = "收藏对象ID", dataType = "int",hidden = true)
    private Integer[] collectId;//收藏对象ID

    @ApiModelProperty(name = "collectType", value = "关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏", dataType = "int",hidden = true)
    private Integer collectType;//关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏


    //Appraisal
    @ApiModelProperty(name = "opinion", value = "鉴定观点", dataType = "String",hidden = true)
    private String opinion;//鉴定观点


    //ReportGoods
    @ApiModelProperty(name = "reason", value = "举报原因", dataType = "String",hidden = true)
    private String reason;//举报原因

    //GoodsBargain
    @ApiModelProperty(name = "bargainId", value = "压价详情ID", dataType = "int",hidden = true)
    private Integer bargainId;//压价详情ID

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getGoodsInfoName() {
        return goodsInfoName;
    }

    public void setGoodsInfoName(String goodsInfoName) {
        this.goodsInfoName = goodsInfoName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getGoodsImgId() {
        return goodsImgId;
    }

    public void setGoodsImgId(Integer goodsImgId) {
        this.goodsImgId = goodsImgId;
    }


    public Integer getGoodsCommentId() {
        return goodsCommentId;
    }

    public void setGoodsCommentId(Integer goodsCommentId) {
        this.goodsCommentId = goodsCommentId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg1Url() {
        return img1Url;
    }

    public void setImg1Url(String img1Url) {
        this.img1Url = img1Url;
    }

    public String getImg2Url() {
        return img2Url;
    }

    public void setImg2Url(String img2Url) {
        this.img2Url = img2Url;
    }

    public String getImg3Url() {
        return img3Url;
    }

    public void setImg3Url(String img3Url) {
        this.img3Url = img3Url;
    }

    public Integer getGoodsCategoryId() {
        return goodsCategoryId;
    }

    public void setGoodsCategoryId(Integer goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }

    public Integer[] getGoodsCategoryIds() {
        return goodsCategoryIds;
    }

    public void setGoodsCategoryIds(Integer[] goodsCategoryIds) {
        this.goodsCategoryIds = goodsCategoryIds;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getGoodsCategoryName() {
        return goodsCategoryName;
    }

    public void setGoodsCategoryName(String goodsCategoryName) {
        this.goodsCategoryName = goodsCategoryName;
    }

    public Integer getCartInfoId() {
        return cartInfoId;
    }

    public void setCartInfoId(Integer cartInfoId) {
        this.cartInfoId = cartInfoId;
    }

    public Integer getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(Integer cartDetailId) {
        this.cartDetailId = cartDetailId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public BigDecimal getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(BigDecimal priceMin) {
        this.priceMin = priceMin;
    }

    public BigDecimal getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(BigDecimal priceMax) {
        this.priceMax = priceMax;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getJumpType() {
        return jumpType;
    }

    public void setJumpType(Integer jumpType) {
        this.jumpType = jumpType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBannerIndexId() {
        return bannerIndexId;
    }

    public void setBannerIndexId(Integer bannerIndexId) {
        this.bannerIndexId = bannerIndexId;
    }

    public Integer[] getCartDetailIds() {
        return cartDetailIds;
    }

    public String[] getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String[] imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setCartDetailIds(Integer[] cartDetailIds) {
        this.cartDetailIds = cartDetailIds;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BigDecimal getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(BigDecimal floorPrice) {
        this.floorPrice = floorPrice;
    }

    public BigDecimal getMarkdown() {
        return markdown;
    }

    public void setMarkdown(BigDecimal markdown) {
        this.markdown = markdown;
    }

    public Integer getCollectType() {
        return collectType;
    }

    public void setCollectType(Integer collectType) {
        this.collectType = collectType;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Integer[] getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(Integer[] goodsIds) {
        this.goodsIds = goodsIds;
    }

    public Integer getSynthesize() {
        return synthesize;
    }

    public void setSynthesize(Integer synthesize) {
        this.synthesize = synthesize;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer[] getCollectId() {
        return collectId;
    }

    public void setCollectId(Integer[] collectId) {
        this.collectId = collectId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getBargainId() {
        return bargainId;
    }

    public void setBargainId(Integer bargainId) {
        this.bargainId = bargainId;
    }

    @Override
    public String toString() {
        return "GoodsParam{" +
                "start=" + start +
                ", limit=" + limit +
                ", type=" + type +
                ", goodsId=" + goodsId +
                ", goodsIds=" + Arrays.toString(goodsIds) +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgUrls=" + Arrays.toString(imgUrls) +
                ", price=" + price +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", goodsInfoName='" + goodsInfoName + '\'' +
                ", postage=" + postage +
                ", label='" + label + '\'' +
                ", sort=" + sort +
                ", floorPrice=" + floorPrice +
                ", intervalTime=" + intervalTime +
                ", markdown=" + markdown +
                ", videoUrl='" + videoUrl + '\'' +
                ", goodsImgId=" + goodsImgId +
                ", goodsCommentId=" + goodsCommentId +
                ", commentId=" + commentId +
                ", content='" + content + '\'' +
                ", img1Url='" + img1Url + '\'' +
                ", img2Url='" + img2Url + '\'' +
                ", img3Url='" + img3Url + '\'' +
                ", goodsCategoryId=" + goodsCategoryId +
                ", goodsCategoryIds=" + Arrays.toString(goodsCategoryIds) +
                ", parentId=" + parentId +
                ", goodsCategoryName='" + goodsCategoryName + '\'' +
                ", cartInfoId=" + cartInfoId +
                ", cartDetailId=" + cartDetailId +
                ", cartDetailIds=" + Arrays.toString(cartDetailIds) +
                ", cartId=" + cartId +
                ", count=" + count +
                ", search='" + search + '\'' +
                ", synthesize=" + synthesize +
                ", priceMin=" + priceMin +
                ", priceMax=" + priceMax +
                ", quality=" + quality +
                ", bannerIndexId=" + bannerIndexId +
                ", businessId=" + businessId +
                ", jumpType=" + jumpType +
                ", collectId=" + Arrays.toString(collectId) +
                ", collectType=" + collectType +
                ", opinion='" + opinion + '\'' +
                ", reason='" + reason + '\'' +
                ", bargainId=" + bargainId +
                '}';
    }
}
