package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户地址信息表
 */
@Getter
@Setter
@ToString
public class UserAddressInfo {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private String receiverName;//收货人

    private String receiverPhone;//收货人手机号码

    private String province;//省份

    private String city;//城市

    private String area;//区域

    private String address;//详细地址

    private String postCode;//邮编

    private Integer type;//类型 1默认地址 2其他

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}