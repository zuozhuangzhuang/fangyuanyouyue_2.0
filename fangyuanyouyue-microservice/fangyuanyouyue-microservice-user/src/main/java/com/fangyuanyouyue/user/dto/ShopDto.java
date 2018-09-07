package com.fangyuanyouyue.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 个人店铺DTO
 */
@Getter
@Setter
@ToString
public class ShopDto {
    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private Integer userId;//用户ID

    private Integer gender;//性别，1男 2女 0不确定

    private Integer level;//用户等级

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private Integer creditLevel;//信誉度等级 1差 2低 3中 4高 5优

    private Integer authtype;//官方认证状态 1已认证 2未认证

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
        //信誉度
        Long credit = (Long)map.get("credit");
        if(credit != null) {
            if (credit < -100) {//差
                this.creditLevel = 1;
            } else if (-100 <= credit && credit < 1000) {//低
                this.creditLevel = 2;
            } else if (1000 <= credit && credit < 10000) {//中
                this.creditLevel = 3;
            } else if (10000 <= credit && credit < 500000) {//高
                this.creditLevel = 4;
            } else if (500000 <= credit) {//优
                this.creditLevel = 5;
            }
        }
        this.authtype = (Integer)map.get("auth_type");
        this.signature = (String)map.get("signature");
    }

    public static List<ShopDto> toDtoList(List<Map<String,Object>> list) {
        if (list == null) {
            return null;
        }
        List<ShopDto> dtolist = new ArrayList<>();
        for (Map<String,Object> model : list) {
            ShopDto dto = new ShopDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}

