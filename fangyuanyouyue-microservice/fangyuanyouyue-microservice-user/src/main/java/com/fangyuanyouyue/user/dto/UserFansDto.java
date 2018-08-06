package com.fangyuanyouyue.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户粉丝DTO
 */
@Getter
@Setter
@ToString
public class UserFansDto {
    private Integer userId;//用户ID

    private Integer level;//用户等级

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private Integer credit;//信誉度

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private String signature;//个性签名

    private Integer authtype;//认证状态 1已认证 2未认证

    public UserFansDto() {
    }

    public UserFansDto(Map<String,Object> map) {
        this.nickName = (String)map.get("nick_name");
        this.headImgUrl = (String)map.get("head_img_url");
        this.userId = (Integer)map.get("id");
        this.level = (Integer)map.get("level");
        this.vipLevel = (Integer)map.get("vip_level");
        this.credit = (Integer)map.get("credit");
        this.authtype = (Integer)map.get("auth_type");
        this.signature = (String)map.get("signature");
    }
    public static List<UserFansDto> toDtoList(List<Map<String,Object>> list) {
        if (list == null)
            return new ArrayList<>();
        List<UserFansDto> dtolist = new ArrayList<>();
        for (Map<String,Object> model : list) {
            UserFansDto dto = new UserFansDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
