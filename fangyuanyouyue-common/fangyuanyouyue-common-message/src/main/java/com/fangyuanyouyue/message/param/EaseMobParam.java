package com.fangyuanyouyue.message.param;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 后台请求基础参数
 */
@ApiModel(value = "后台请求基础参数")
@Getter
@Setter
@ToString
public class EaseMobParam {

	@ApiModelProperty(name = "userName", value = "环信用户名", dataType = "string",hidden = true)
    private String userName; // 

    @ApiModelProperty(name = "password", value = "环信密码", dataType = "string",hidden = true)
    private String password; // 

    @ApiModelProperty(name = "content", value = "消息内容", dataType = "string",hidden = true)
    private String content; // 消息内容

    @ApiModelProperty(name = "time", value = "消息时间", dataType = "date",hidden = true)
    private Date time; // 消息时间

    @ApiModelProperty(name = "nickName", value = "昵称", dataType = "string",hidden = true)
    private String nickName; // 昵称

    @ApiModelProperty(name = "headImgUrl", value = "头像图片url", dataType = "string",hidden = true)
    private String headImgUrl; // 头像url

    @ApiModelProperty(name = "type", value = "消息类型", dataType = "string",hidden = true)
    private String type;//消息类型 1系统消息 2交易消息 3社交消息 4新增粉丝 5邀请我

    /**
     * 跳转类型
     * 1系统消息 2商品消息 3订单消息 4视频消息 5帖子消息 6专栏消息 7全民鉴定消息 8商品、抢购评论消息
     * 9帖子评论消息 10视频评论消息 11全民鉴定评论消息 12会员特权 13钱包余额
     */
    @ApiModelProperty(name = "jumpType", value = "跳转类型", dataType = "string",hidden = true)
    private String jumpType;

    @ApiModelProperty(name = "businessId", value = "扩展消息业务ID", dataType = "string",hidden = true)
    private String businessId; //扩展消息业务ID，如订单消息里面的订单id

}
