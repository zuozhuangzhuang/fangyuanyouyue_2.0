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
     * 1系统消息（不跳转） 2商品详情 3抢购详情 4帖子详情 5视频详情 6全民鉴定详情 7钱包页面 8会员页面
     * 9官方鉴定列表 10买家订单详情 11卖家订单详情 12我的专栏 13实名认证通过 14官方认证通过 15申请专栏通过
     * 16实名认证拒绝 17官方认证拒绝 18申请专栏拒绝
     */
    @ApiModelProperty(name = "jumpType", value = "跳转类型", dataType = "string",hidden = true)
    private String jumpType;

    @ApiModelProperty(name = "businessId", value = "扩展消息业务ID", dataType = "string",hidden = true)
    private String businessId; //扩展消息业务ID，如订单消息里面的订单id

}
