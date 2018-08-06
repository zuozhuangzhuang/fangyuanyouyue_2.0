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
public class ForumParam{
	//公用
	@ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
	private Integer start; // 起始页

	@ApiModelProperty(name = "limit", value = "限制页", dataType = "int",hidden = true)
	private Integer limit; // 限制页

	@ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
	private Integer type;//类型

	//TODO 需要在API网关通过授权才能获取到userId
	@ApiModelProperty(name = "userId", value = "用户id", dataType = "int",hidden = true)
	private Integer userId;//帖子id
	
	@ApiModelProperty(name = "forumId", value = "帖子id", dataType = "int",hidden = true)
	private Integer forumId;//帖子id

	@ApiModelProperty(name = "title", value = "帖子标题", dataType = "String",hidden = true)
	private String title;//帖子标题

	@ApiModelProperty(name = "content", value = "帖子内容", dataType = "String",hidden = true)
	private String content;//帖子内容

	@ApiModelProperty(name = "columnId", value = "专栏ID", dataType = "int",hidden = true)
	private Integer columnId;//专栏ID

	@ApiModelProperty(name = "commentId", value = "评论ID", dataType = "int",hidden = true)
	private Integer commentId;//帖子内容

}
