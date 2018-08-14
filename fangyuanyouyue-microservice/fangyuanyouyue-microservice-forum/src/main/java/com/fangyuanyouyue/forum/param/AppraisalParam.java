package com.fangyuanyouyue.forum.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "论坛相关参数")
@Getter
@Setter
@ToString
public class AppraisalParam{
	//公用
	@ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
	private Integer start; // 起始页

	@ApiModelProperty(name = "limit", value = "限制页", dataType = "int",hidden = true)
	private Integer limit; // 限制页

    @ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
    private String token;//用户token

	@ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
	private Integer type;//类型

	//TODO 需要在API网关通过授权才能获取到userId
	@ApiModelProperty(name = "userId", value = "用户id", dataType = "int",hidden = true)
	private Integer userId;//用户id
	
	@ApiModelProperty(name = "appraisalId", value = "全民鉴定id", dataType = "int",hidden = true)
	private Integer appraisalId;//鉴定id

	@ApiModelProperty(name = "commentId", value = "评论id", dataType = "int",hidden = true)
	private Integer commentId;//评论id

	@ApiModelProperty(name = "keyword", value = "关键字搜索", dataType = "String",hidden = true)
	private String keyword;//帖子内容

}
