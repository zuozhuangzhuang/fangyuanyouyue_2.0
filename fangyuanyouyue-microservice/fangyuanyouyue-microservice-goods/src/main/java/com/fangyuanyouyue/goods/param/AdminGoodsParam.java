package com.fangyuanyouyue.goods.param;

import com.fangyuanyouyue.base.BasePageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "后台管理商品参数类")
@Getter
@Setter
@ToString
public class AdminGoodsParam extends BasePageReq{
    /**
     *
     */
    private static final long serialVersionUID = 1198374192047731064L;

    //商品名称
    @ApiModelProperty(name = "name", value = "名称", dataType = "String",hidden = true)
    private String name;

    //业务ID：例：商品id、视频id、积分商城商品id...
    @ApiModelProperty(name = "businessId", value = "业务ID：例：商品id、视频id、积分商城商品id...", dataType = "int",hidden = true)
    private Integer businessId;

    //跳转类型 1页面 2链接 3图片（businessId为空）
    @ApiModelProperty(name = "jumpType", value = "跳转类型 1页面 2链接 3图片（businessId为空）", dataType = "int",hidden = true)
    private Integer jumpType;

    //业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏 7积分商品
    @ApiModelProperty(name = "businessType", value = "业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏 7积分商品", dataType = "int",hidden = true)
    private Integer businessType;

    //类型
    @ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
    private Integer type;

    //标题
    @ApiModelProperty(name = "title", value = "标题", dataType = "String",hidden = true)
    private String title;


    //图片地址
    @ApiModelProperty(name = "imgUrl", value = "图片地址", dataType = "String",hidden = true)
    private String imgUrl;

    //图片地址数组
    @ApiModelProperty(name = "imgUrls", value = "图片地址数组", dataType = "String",hidden = true)
    private String[] imgUrls;

    //排序
    @ApiModelProperty(name = "sort", value = "排序", dataType = "int",hidden = true)
    private Integer sort;

    //最小价格
    @ApiModelProperty(name = "priceMin", value = "最小价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal priceMin;

    //最大价格
    @ApiModelProperty(name = "priceMax", value = "最大价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal priceMax;

    //品质 1：认证店铺 2：官方保真 3：高信誉度 4：我的关注
    @ApiModelProperty(name = "quality", value = "品质 1：认证店铺 2：官方保真 3：高信誉度 4：我的关注", dataType = "int",hidden = true)
    private Integer quality;

    //综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序
    @ApiModelProperty(name = "synthesize", value = "综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序", dataType = "int",hidden = true)
    private Integer synthesize;

    //商品类目表ID
    @ApiModelProperty(name = "goodsCategoryId", value = "商品类目表ID", dataType = "int",hidden = true)
    private Integer goodsCategoryId;

    //商品分类数组（同一商品可多种分类）
    @ApiModelProperty(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）", dataType = "int",hidden = true)
    private Integer[] goodsCategoryIds;

    //运费
    @ApiModelProperty(name = "postage", value = "运费", dataType = "BigDecimal",hidden = true)
    private BigDecimal postage;

    //标签
    @ApiModelProperty(name = "label", value = "标签", dataType = "String",hidden = true)
    private String label;

    //GoodsInfo:商品详情  Appraisal:描述
    @ApiModelProperty(name = "description", value = "商品详情", dataType = "String",hidden = true)
    private String description;

    //最低价
    @ApiModelProperty(name = "floorPrice", value = "最低价", dataType = "BigDecimal",hidden = true)
    private BigDecimal floorPrice;

    //降价时间间隔(单位秒)
    @ApiModelProperty(name = "intervalTime", value = "降价时间间隔", dataType = "int",hidden = true)
    private Integer intervalTime;

    //降价幅度
    @ApiModelProperty(name = "markdown", value = "降价幅度", dataType = "BigDecimal",hidden = true)
    private BigDecimal markdown;

    //视频路径
    @ApiModelProperty(name = "videoUrl", value = "视频路径", dataType = "String",hidden = true)
    private String videoUrl;

    //视频截图路径
    @ApiModelProperty(name = "videoImg", value = "视频截图路径", dataType = "String",hidden = true)
    private String videoImg;

    //评论内容
    @ApiModelProperty(name = "content", value = "评论内容", dataType = "String",hidden = true)
    private String content;

    //价格
    @ApiModelProperty(name = "price", value = "价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal price;

    //视频长度
    @ApiModelProperty(name = "videoLength", value = "视频长度", dataType = "int",hidden = true)
    private Integer videoLength;

    //视频长度
    @ApiModelProperty(name = "isShow", value = "是否鉴定展示 1是 2否", dataType = "int",hidden = true)
    private Integer isShow;

    //上级id
    @ApiModelProperty(name = "parentId", value = "上级id", dataType = "int",hidden = true)
    private Integer parentId;

    //是否官方鉴定 1已鉴定 2未鉴定
    @ApiModelProperty(name = "isAppraisal", value = "是否官方鉴定 1已鉴定 2未鉴定", dataType = "int",hidden = true)
    private Integer isAppraisal;
}
