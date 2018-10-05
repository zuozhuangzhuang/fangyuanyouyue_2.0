package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 商品类目表
 */
@Getter
@Setter
@ToString
public class GoodsCategory {
    private Integer id;

    private Integer parentId;//上级id
    
    private String parentName; //上级名称

    private String name;//类目名称

    private String imgUrl;//图片地址

    private Integer sort;//排序

    private Integer type;//类型 1普通 2热门

    private Integer status;//状态 0正常 1禁用

    private Integer searchCount = 0;//搜索次数

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}