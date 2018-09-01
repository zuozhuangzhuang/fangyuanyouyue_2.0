package com.fangyuanyouyue.message.param;

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
public class MessageParam {
    //公用
    @ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
    private Integer start; // 起始页
    @ApiModelProperty(name = "limit", value = "限制页", dataType = "int",hidden = true)
    private Integer limit; // 限制页
    @ApiModelProperty(name = "name", value = "搜素字段", dataType = "int",hidden = true)
    private String name; // 搜素字段
    @ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
    private Integer type;//验证码类型 0表示注册 1表示密码找回 2 表示支付密码相关 3验证旧手机，4绑定新手机

    @ApiModelProperty(name = "userId", value = "发布用户id", dataType = "int",hidden = true)
    private Integer userId;//用户id

    @ApiModelProperty(name = "phone", value = "用户手机号", dataType = "String",hidden = true)
    private String phone;//用户手机号

    @ApiModelProperty(name = "unionId", value = "三方唯一识别号", dataType = "String",hidden = true)
    private String unionId;//三方唯一识别号

    @ApiModelProperty(name = "thirdType", value = "类型 1微信 2QQ 3微博", dataType = "int",hidden = true)
    private Integer thirdType;//类型 1微信 2QQ 3微博

    @ApiModelProperty(name = "code", value = "验证码", dataType = "String",hidden = true)
    private String code;//验证码
}
