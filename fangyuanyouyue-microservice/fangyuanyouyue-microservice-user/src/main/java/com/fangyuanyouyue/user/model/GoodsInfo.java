package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 商品详情表
 */
@Getter
@Setter
@ToString
public class GoodsInfo {
    private Integer id;

    private Integer userId;//发布用户id

    private String name;//商品名称

    private String description;//商品详情

    private BigDecimal price;//商品价格

    private BigDecimal postage;//运费

    private Integer sort=2;//排序 1置顶 2默认

    private String label;//标签

    private Integer type;//类型 1普通商品 2抢购商品

    private Integer status;//状态 1出售中 2已售出 3已下架（已结束） 5删除

    private BigDecimal floorPrice;//最低价

    private Integer intervalTime;//降价时间间隔(单位秒)

    private BigDecimal markdown;//降价幅度

    private Date lastIntervalTime;//最后一次降价时间

    private Integer isAppraisal;//是否官方鉴定 1已鉴定 2未鉴定

    private String videoUrl;//视频路径

    private Timestamp addTime;//发布时间

    private Date updateTime;//修改时间

    private Integer videoLength;//视频长度

    private BigDecimal startPrice;//初始价格

    private Date commentTime;//最近一次评论时间

    private String mainImgUrl;//商品主图路径
}
