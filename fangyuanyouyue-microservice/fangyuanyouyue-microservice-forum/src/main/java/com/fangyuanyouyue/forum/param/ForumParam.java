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
	@ApiModelProperty(name = "ids", value = "id数组", dataType = "int",hidden = true)
	private Integer[] ids; //id数组

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
	
	@ApiModelProperty(name = "forumId", value = "帖子id", dataType = "int",hidden = true)
	private Integer forumId;//帖子id

	@ApiModelProperty(name = "title", value = "帖子标题", dataType = "String",hidden = true)
	private String title;//帖子标题

	@ApiModelProperty(name = "content", value = "帖子内容", dataType = "String",hidden = true)
	private String content;//帖子内容

	@ApiModelProperty(name = "columnId", value = "专栏ID", dataType = "int",hidden = true)
	private Integer columnId;//专栏ID

	@ApiModelProperty(name = "commentId", value = "评论ID", dataType = "int",hidden = true)
	private Integer commentId;//评论ID

	@ApiModelProperty(name = "keyword", value = "关键字搜索", dataType = "String",hidden = true)
	private String keyword;//关键字搜索

	@ApiModelProperty(name = "searchType", value = "搜索类型", dataType = "int",hidden = true)
	private Integer searchType;//搜索类型

	@ApiModelProperty(name = "videoUrl", value = "视频链接", dataType = "String",hidden = true)
	private String videoUrl;//视频链接

	@ApiModelProperty(name = "videoLength", value = "视频长度", dataType = "int",hidden = true)
	private Integer videoLength;//视频长度

	@ApiModelProperty(name = "videoImg", value = "视频封面图", dataType = "String",hidden = true)
	private String videoImg;//视频封面图

	@ApiModelProperty(name = "userIds", value = "邀请用户id数组", dataType = "int",hidden = true)
	private Integer[] userIds;//邀请用户id数组

	@ApiModelProperty(name = "typeId", value = "专栏分类id", dataType = "int",hidden = true)
	private Integer typeId;//专栏分类id

	@ApiModelProperty(name = "name", value = "专栏名称", dataType = "String",hidden = true)
	private String name;//专栏名称

	@ApiModelProperty(name = "coverImgUrl", value = "封面图片地址", dataType = "String",hidden = true)
	private String coverImgUrl;//封面图片地址

	@ApiModelProperty(name = "listType", value = "列表类型 1普通列表 2我的帖子/视频列表", dataType = "int",hidden = true)
	private Integer listType;//列表类型 1普通列表 2我的帖子/视频列表

	@ApiModelProperty(name = "payType", value = "支付类型 1微信 2支付宝 3余额 4小程序", dataType = "int",hidden = true)
	private Integer payType;//支付类型 1微信 2支付宝 3余额 4小程序

	@ApiModelProperty(name = "payPwd", value = "支付密码", dataType = "String",hidden = true)
	private String payPwd;//支付密码

	@ApiModelProperty(name = "applyId", value = "专栏申请id", dataType = "int",hidden = true)
	private Integer applyId;//专栏申请id

	@ApiModelProperty(name = "status", value = "状态", dataType = "int",hidden = true)
	private Integer status;//状态

	@ApiModelProperty(name = "reason", value = "拒绝理由", dataType = "String",hidden = true)
	private String reason;//拒绝理由
}
