package com.fangyuanyouyue.user.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 */
@ApiModel(value = "后台管理员相关参数")
@Getter
@Setter
@ToString
public class AdminOperatorParam {

    @ApiModelProperty(name = "id", value = "xx", dataType = "int",hidden = true)
    private Integer id;

    @ApiModelProperty(name = "roleIds", value = "xx", dataType = "int",hidden = true)
    private Integer[] roleIds;

    @ApiModelProperty(name = "userId", value = "xx", dataType = "int",hidden = true)
    private Integer userId;

    @ApiModelProperty(name = "roleId", value = "xx", dataType = "int",hidden = true)
    private Integer roleId;

    @ApiModelProperty(name = "password", value = "xx", dataType = "String",hidden = true)
    private String password;

    @ApiModelProperty(name = "confirm", value = "xx", dataType = "String",hidden = true)
    private String confirm;

    @ApiModelProperty(name = "userCode", value = "xx", dataType = "String",hidden = true)
    private String userCode;

    @ApiModelProperty(name = "state", value = "xx", dataType = "String",hidden = true)
    private String state;

    //规则内容
    @ApiModelProperty(name = "ruleContent", value = "规则内容", dataType = "String",hidden = true)
    private String ruleContent;

    //规则类型 1邀请规则
    @ApiModelProperty(name = "ruleType", value = "规则类型", dataType = "int",hidden = true)
    private Integer ruleType;

    public AdminOperatorParam() {
    	
    }



}
