package com.fangyuanyouyue.user.param;

import java.util.HashMap;
import java.util.List;

import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.user.dto.admin.AdminMenuDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ApiModel(value = "后台管理菜单相关参数")
@Getter
@Setter
@ToString
public class AdminMenuParam extends BasePageReq{
	
	private static final long serialVersionUID = 1198374192047731064L;

	@ApiModelProperty(name = "id", value = "ID", dataType = "int",hidden = true)
	private Integer id;

	@ApiModelProperty(name = "text", value = "菜单名", dataType = "String",hidden = true)
	private String text;

	@ApiModelProperty(name = "icon", value = "菜单icon", dataType = "String",hidden = true)
	private String icon;

	@ApiModelProperty(name = "url", value = "菜单url", dataType = "String",hidden = true)
	private String url;

	@ApiModelProperty(name = "type", value = "操作类型", dataType = "String",hidden = true)
	private String type;
	
	private String menu;

	private Integer roleId;

}
