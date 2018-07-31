package com.fangyuanyouyue.forum.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "论坛相关参数")
public class ForumParam{
	//公用
	@ApiModelProperty(name = "start", value = "起始页", dataType = "Integer",hidden = true)
	private Integer start; // 起始页

	@ApiModelProperty(name = "limit", value = "限制页", dataType = "Integer",hidden = true)
	private Integer limit; // 限制页

	@ApiModelProperty(name = "type", value = "类型", dataType = "Integer",hidden = true)
	private Integer type;//类型

	//TODO 需要在API网关通过授权才能获取到userId
	@ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer",hidden = true)
	private Integer userId;//帖子id
	
	@ApiModelProperty(name = "forumId", value = "帖子id", dataType = "Integer",hidden = true)
	private Integer forumId;//帖子id

	@ApiModelProperty(name = "title", value = "帖子标题", dataType = "String",hidden = true)
	private String title;//帖子标题

	@ApiModelProperty(name = "content", value = "帖子内容", dataType = "String",hidden = true)
	private String content;//帖子内容

	@ApiModelProperty(name = "columnId", value = "专栏ID", dataType = "Integer",hidden = true)
	private Integer columnId;//专栏ID

	@ApiModelProperty(name = "commentId", value = "评论ID", dataType = "Integer",hidden = true)
	private Integer commentId;//帖子内容

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getForumId() {
		return forumId;
	}

	public void setForumId(Integer forumId) {
		this.forumId = forumId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
}
