package com.fangyuanyouyue.message.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 后台请求基础参数
 */
@ApiModel(value = "后台请求基础参数")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Integer getThirdType() {
        return thirdType;
    }

    public void setThirdType(Integer thirdType) {
        this.thirdType = thirdType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MessageParam{" +
                "start=" + start +
                ", limit=" + limit +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", userId=" + userId +
                ", phone='" + phone + '\'' +
                ", unionId='" + unionId + '\'' +
                ", thirdType=" + thirdType +
                ", code='" + code + '\'' +
                '}';
    }
}
