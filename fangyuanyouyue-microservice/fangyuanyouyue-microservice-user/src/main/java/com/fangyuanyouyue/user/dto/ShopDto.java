package com.fangyuanyouyue.user.dto;

import com.fangyuanyouyue.user.model.UserAddressInfo;
import com.fangyuanyouyue.user.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 个人店铺DTO
 */
public class ShopDto {
    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private Integer userId;//卖家ID

    private Integer gender;//性别，1男 2女 0不确定

    private Integer level;//用户等级

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private Integer vipType;//会员类型 1体验会员 2月会员 3年会员

    private Integer credit;//信誉度

    private Integer authtype;//认证状态 1已认证 2未认证

    private String signature;//个性签名

    private String imgUrl1;//图片地址1

    private String imgUrl2;//图片地址2

    private String imgUrl3;//图片地址3

    public ShopDto() {
    }

    public ShopDto(Map<String,Object> map) {
        this.nickName = (String)map.get("nick_name");
        this.headImgUrl = (String)map.get("head_img_url");
        this.userId = (Integer)map.get("id");
        this.gender = (Integer)map.get("gender");
        this.level = (Integer)map.get("level");
        this.vipLevel = (Integer)map.get("vip_level");
        this.vipType = (Integer)map.get("vip_type");
        this.credit = (Integer)map.get("credit");
        this.authtype = (Integer)map.get("auth_type");
        this.signature = (String)map.get("signature");
    }
    public static List<ShopDto> toDtoList(List<Map<String,Object>> list) {
        if (list == null)
            return null;
        List<ShopDto> dtolist = new ArrayList<>();
        for (Map<String,Object> model : list) {
            ShopDto dto = new ShopDto(model);
            dtolist.add(dto);
        }
        return dtolist;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Integer getVipType() {
        return vipType;
    }

    public void setVipType(Integer vipType) {
        this.vipType = vipType;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
    }

    public Integer getAuthtype() {
        return authtype;
    }

    public void setAuthtype(Integer authtype) {
        this.authtype = authtype;
    }
}

