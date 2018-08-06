package com.fangyuanyouyue.user.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;

@ApiModel(value = "用户相关参数")
@Getter
@Setter
@ToString
public class UserParam{
	//公用

	@ApiModelProperty(name = "userId", value = "用户ID", dataType = "int",hidden = true)
	private Integer userId; //用户ID

	@ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
	private Integer start; // 起始页

	@ApiModelProperty(name = "limit", value = "限制页", dataType = "int",hidden = true)
	private Integer limit; // 限制页

	@ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
	private Integer type;//类型	UserThirdParty：1微信 2QQ 3微博 UserAddressInfo：1默认地址 2其他 UserCollect：1关注 2收藏

	@ApiModelProperty(name = "imgFile", value = "图片文件", dataType = "file",hidden = true)
	private MultipartFile imgFile;//图片文件

	@ApiModelProperty(name = "videoFile", value = "视频文件", dataType = "file",hidden = true)
	private MultipartFile videoFile;//视频文件


	//UserInfo
	@ApiModelProperty(name = "phone", value = "手机号码", dataType = "String",hidden = true)
	private String phone;//手机号码

	@ApiModelProperty(name = "email", value = "电子邮件", dataType = "String",hidden = true)
	private String email;//电子邮件

	@ApiModelProperty(name = "userAddress", value = "用户所在地", dataType = "String",hidden = true)
	private String userAddress;//用户所在地

	@ApiModelProperty(name = "loginPwd", value = "登录密码,MD5小写", dataType = "String",hidden = true)
	private String loginPwd;//登录密码,MD5小写

	@ApiModelProperty(name = "nickName", value = "昵称", dataType = "String",hidden = true)
	private String nickName;//昵称

	@ApiModelProperty(name = "headImgUrl", value = "头像图片地址", dataType = "String",hidden = true)
	private String headImgUrl;//头像图片地址

//	@ApiModelProperty(name = "headImg", value = "头像图片", dataType = "file",hidden = true)
//	private MultipartFile headImg;//头像图片

	@ApiModelProperty(name = "bgImgUrl", value = "背景图片地址", dataType = "String",hidden = true)
	private String bgImgUrl;//背景图片地址

//	@ApiModelProperty(name = "bgImg", value = "背景图片", dataType = "file",hidden = true)
//	private MultipartFile bgImg;//背景图片

	@ApiModelProperty(name = "gender", value = "性别，1男 2女 0不确定", dataType = "int",hidden = true)
	private Integer gender;//性别，1男 2女 0不确定

	@ApiModelProperty(name = "signature", value = "个性签名", dataType = "String",hidden = true)
	private String signature;//个性签名

	@ApiModelProperty(name = "contact", value = "联系电话", dataType = "String",hidden = true)
	private String contact;//联系电话

	@ApiModelProperty(name = "regType", value = "注册来源 1app 2微信小程序", dataType = "int",hidden = true)
	private Integer regType;//注册来源 1app 2微信小程序

	@ApiModelProperty(name = "regPlatform", value = "注册平台 1安卓 2IOS 3小程序", dataType = "int",hidden = true)
	private Integer regPlatform;//注册平台 1安卓 2IOS 3小程序

	@ApiModelProperty(name = "levelDesc", value = "等级描述", dataType = "String",hidden = true)
	private String levelDesc;//等级描述

	@ApiModelProperty(name = "status", value = "状态", dataType = "int",hidden = true)
	private Integer status;//用户信息状态 1正常 2冻结 / 用户扩展实名登记状态 1已实名 2未实名 / 用户会员状态1已开通 2未开通 / 实名认证状态 1申请 2通过 3拒绝

	@ApiModelProperty(name = "updateTime", value = "更新时间", dataType = "int",hidden = true)
	private Date updateTime;//更新时间

	@ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
	private String token;//用户token

	@ApiModelProperty(name = "newPwd", value = "新密码", dataType = "String",hidden = true)
	private String newPwd;//新密码

	@ApiModelProperty(name = "birth", value = "生日", dataType = "String",hidden = true)
	private String birth;//生日

	@ApiModelProperty(name = "name", value = "真实姓名", dataType = "String",hidden = true)
	private String name;//真实姓名

	@ApiModelProperty(name = "identity", value = "身份证号码", dataType = "String",hidden = true)
	private String identity;//身份证号码

	@ApiModelProperty(name = "payPwd", value = "支付密码，md5加密，32位小写字母", dataType = "String",hidden = true)
	private String payPwd;//支付密码，明文6位，MD5小写

	@ApiModelProperty(name = "identityImgCoverUrl", value = "身份证封面图路径", dataType = "String",hidden = true)
	private String identityImgCoverUrl;//身份证封面图

	@ApiModelProperty(name = "identityImgBackUrl", value = "身份证背面路径", dataType = "String",hidden = true)
	private String identityImgBackUrl;//身份证背面

	@ApiModelProperty(name = "receiverName", value = "收货人", dataType = "String",hidden = true)
	private String receiverName;//收货人

	@ApiModelProperty(name = "receiverPhone", value = "收货人手机号码", dataType = "String",hidden = true)
	private String receiverPhone;//收货人手机号码

	@ApiModelProperty(name = "province", value = "省", dataType = "String",hidden = true)
	private String province;//省

	@ApiModelProperty(name = "city", value = "市", dataType = "String",hidden = true)
	private String city;//市

	@ApiModelProperty(name = "area", value = "区", dataType = "String",hidden = true)
	private String area;//区

	@ApiModelProperty(name = "address", value = "详细地址", dataType = "String",hidden = true)
	private String address;//详细地址

	@ApiModelProperty(name = "postCode", value = "邮编", dataType = "String",hidden = true)
	private String postCode;//邮编

	@ApiModelProperty(name = "addressId", value = "地址ID", dataType = "int",hidden = true)
	private Integer addressId;//地址ID

	@ApiModelProperty(name = "unionId", value = "第三方唯一ID", dataType = "String",hidden = true)
	private String unionId;//第三方唯一ID

	@ApiModelProperty(name = "thirdNickName", value = "第三方账号昵称", dataType = "String",hidden = true)
	private String thirdNickName;//第三方账号昵称

	@ApiModelProperty(name = "thirdHeadImgUrl", value = "第三方账号头像地址", dataType = "String",hidden = true)
	private String thirdHeadImgUrl;//第三方账号头像地址

	@ApiModelProperty(name = "loginPlatform", value = "登录平台 1安卓 2IOS 3小程序", dataType = "int",hidden = true)
	private Integer loginPlatform;//登录平台 1安卓 2IOS 3小程序



	//小程序
	@ApiModelProperty(name = "code", value = "微信获取的code", dataType = "String",hidden = true)
	private String code;//微信获取的code

	@ApiModelProperty(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据", dataType = "String",hidden = true)
	private String encryptedData;//包括敏感数据在内的完整用户信息的加密数据

	@ApiModelProperty(name = "iv", value = "加密算法的初始向量", dataType = "String",hidden = true)
	private String iv;//加密算法的初始向量

	//UserFans
	@ApiModelProperty(name = "toUserId", value = "加密算法的初始向量", dataType = "int",hidden = true)
	private Integer toUserId;//被关注人ID

}
