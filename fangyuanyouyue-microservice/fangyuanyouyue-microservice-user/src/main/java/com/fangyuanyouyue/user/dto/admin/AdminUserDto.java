package com.fangyuanyouyue.user.dto.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.UserInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户信息
 */
@Getter
@Setter
@ToString
public class AdminUserDto {
    //UserInfo 用户基本信息表
    private Integer id;//用户ID

    private String token;//用户token

    private String phone;//手机号码

    private String email;//电子邮件

    private String address;//用户所在地

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private String bgImgUrl;//背景图片地址

    private Integer gender;//性别，1男 2女 0不确定

    private String signature;//个性签名

    private String contact;//联系电话

    private Integer level;//用户等级
    
    private BigDecimal balance;


    //UserVip
    private Integer vipLevel;//会员等级

    private String vipLevelDesc;//会员等级描述

    private Integer vipStatus;//会员状态 1已开通 2未开通


    //UserFans
    private Integer fansCount;//粉丝数量

    private Integer collectCount;//关注数量

    private Integer isFollow = 2;//是否关注 1是 2否

    private Integer creditLevel;//信誉度等级 1差 2低 3中 4高 5优

    private Integer authType;//认证状态 1已认证 2未认证

    private Integer extStatus;//实名登记状态 1申请中 2已实名 3未实名

    private Integer isPayPwd;//是否设置支付密码 1是 2否

    private Integer appraisalCount;//免费鉴定次数

    private String addTime;
    
    private Integer status;

    private Integer fansBaseCount;//粉丝基数


    public AdminUserDto(UserInfo model) {
    	this.id = model.getId();
    	this.nickName = model.getNickName();
    	this.gender = model.getGender();
    	this.addTime = DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
    	this.headImgUrl = model.getHeadImgUrl();
    	this.email = model.getEmail();
    	this.phone = model.getPhone();
    	this.address = model.getAddress();
    	this.level = model.getLevel();
    	this.status = model.getStatus();
        this.contact = model.getContact();
    }


    public static List<AdminUserDto> toDtoList(List<UserInfo> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminUserDto> dtolist = new ArrayList<>();
        for (UserInfo model : list) {
            AdminUserDto dto = new AdminUserDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


}
