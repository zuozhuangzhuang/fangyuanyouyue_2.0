package com.fangyuanyouyue.goods.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Arrays;

@ApiModel(value = "商品参数类")
@Getter
@Setter
@ToString
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

    @ApiModelProperty(name = "imgUrls", value = "图片地址数组", dataType = "String",hidden = true)
    private String[] imgUrls;//图片地址数组

    @ApiModelProperty(name = "price", value = "价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal price;//GoodsInfo:价格 Appraisal:鉴定赏金

    @ApiModelProperty(name = "status", value = "状态", dataType = "int",hidden = true)
    private Integer status;//状态 GoodsComment：1正常 2隐藏 GoodsInfo：普通商品 1出售中 2已售出 3已下架（已结束） 5删除 Appraisal: 0申请 1真 2假 3存疑

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

    @ApiModelProperty(name = "floorPrice", value = "最低价", dataType = "BigDecimal",hidden = true)
    private BigDecimal floorPrice;//最低价

    @ApiModelProperty(name = "intervalTime", value = "降价时间间隔", dataType = "int",hidden = true)
    private Integer intervalTime;//降价时间间隔(单位秒)

    @ApiModelProperty(name = "markdown", value = "降价幅度", dataType = "BigDecimal",hidden = true)
    private BigDecimal markdown;//降价幅度

    @ApiModelProperty(name = "videoUrl", value = "视频路径", dataType = "String",hidden = true)
    private String videoUrl;//视频路径

    @ApiModelProperty(name = "videoImg", value = "视频截图路径", dataType = "String",hidden = true)
    private String videoImg;//视频截图路径

   //GoodsComment
    @ApiModelProperty(name = "commentId", value = "回复评论id", dataType = "int",hidden = true)
    private Integer commentId;//回复评论id

    @ApiModelProperty(name = "commentIds", value = "回复评论id数组", dataType = "int",hidden = true)
    private Integer[] commentIds;//回复评论id

    @ApiModelProperty(name = "content", value = "评论内容", dataType = "String",hidden = true)
    private String content;//评论内容

    @ApiModelProperty(name = "img1Url", value = "图片地址1", dataType = "String",hidden = true)
    private String img1Url;//图片地址1

    @ApiModelProperty(name = "img2Url", value = "图片地址2", dataType = "String",hidden = true)
    private String img2Url;//图片地址2

    @ApiModelProperty(name = "img3Url", value = "图片地址3", dataType = "String",hidden = true)
    private String img3Url;//图片地址3

    //GoodsCategory
    @ApiModelProperty(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）", dataType = "int",hidden = true)
    private Integer[] goodsCategoryIds;//商品分类数组（同一商品可多种分类）

    //CartDetail
    @ApiModelProperty(name = "cartDetailIds", value = "购物车明细表ID数组", dataType = "int",hidden = true)
    private Integer[] cartDetailIds;//购物车明细表ID数组

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
    private Integer quality;//品质 1：认证店铺 2：官方保真 3：高信誉度 4：我的关注 5：（抢购）已完成

    //轮播图 BannerIndex
    @ApiModelProperty(name = "businessId", value = "业务ID:商品ID/用户ID", dataType = "int",hidden = true)
    private Integer businessId;//业务ID:商品ID/用户ID

    //Collect
    @ApiModelProperty(name = "collectIds", value = "收藏对象ID数组", dataType = "int",hidden = true)
    private Integer[] collectIds;//收藏对象ID数组

    @ApiModelProperty(name = "collectType", value = "关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏", dataType = "int",hidden = true)
    private Integer collectType;//关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏

    //Appraisal
    @ApiModelProperty(name = "detailId", value = "鉴定详情ID", dataType = "int",hidden = true)
    private Integer detailId;//鉴定详情ID

    //ReportGoods
    @ApiModelProperty(name = "reason", value = "举报原因", dataType = "String",hidden = true)
    private String reason;//举报原因

    //GoodsBargain
    @ApiModelProperty(name = "bargainId", value = "压价详情ID", dataType = "int",hidden = true)
    private Integer bargainId;//压价详情ID

    @ApiModelProperty(name = "addressId", value = "收货地址id", dataType = "int",hidden = true)
    private Integer addressId;//收货地址id

    @ApiModelProperty(name = "payPwd", value = "支付密码", dataType = "String",hidden = true)
    private String payPwd;//支付密码

    @ApiModelProperty(name = "userIds", value = "邀请用户id数组", dataType = "int",hidden = true)
    private Integer[] userIds;//邀请用户id数组

    @ApiModelProperty(name = "businessType", value = "业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏 7积分商品", dataType = "int",hidden = true)
    private Integer businessType;//业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏 7积分商品

    @ApiModelProperty(name = "payType", value = "支付类型 1微信 2支付宝 3余额 4小程序", dataType = "int",hidden = true)
    private Integer payType;//支付类型 1微信 2支付宝 3余额 4小程序

    @ApiModelProperty(name = "videoLength", value = "视频长度", dataType = "int",hidden = true)
    private Integer videoLength;//视频长度

    @ApiModelProperty(name = "ids", value = "id数组", dataType = "int",hidden = true)
    private Integer[] ids;//id数组

    @ApiModelProperty(name = "formId", value = "小程序表单提交ID", dataType = "String",hidden = true)
    private String formId;//小程序表单提交ID

    @ApiModelProperty(name = "id", value = "id", dataType = "int",hidden = true)
    private Integer id;
    @ApiModelProperty(name = "shopId", value = "店铺id", dataType = "int",hidden = true)
    private Integer shopId;
}
