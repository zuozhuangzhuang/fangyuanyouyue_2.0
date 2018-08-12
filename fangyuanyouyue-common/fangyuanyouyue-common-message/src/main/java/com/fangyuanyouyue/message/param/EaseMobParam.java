package com.fangyuanyouyue.message.param;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 后台请求基础参数
 */
@ApiModel(value = "后台请求基础参数")
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
    private String type; // 扩展消息类型 如系统消息、订单消息

    @ApiModelProperty(name = "businessId", value = "扩展消息业务ID", dataType = "string",hidden = true)
    private String businessId; //扩展消息业务ID，如订单消息里面的订单id
    

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
}
