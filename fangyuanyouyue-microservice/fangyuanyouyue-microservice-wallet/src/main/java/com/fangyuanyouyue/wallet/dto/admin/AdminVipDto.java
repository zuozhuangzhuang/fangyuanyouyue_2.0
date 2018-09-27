package com.fangyuanyouyue.wallet.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.model.UserVip;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台查看用户会员DTO
 */
@Getter
@Setter
@ToString
public class AdminVipDto {

    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private String startTime;//会员开通时间

    private String endTime;//会员过期时间

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private String levelDesc;//会员等级描述

    private Integer vipType;//会员类型 1一个月 2三个月 3一年会员

    private Integer status;//会员状态 1已开通 2未开通

    private String addTime;//添加时间

    private String updateTime;//更新时间

    private String vipNo;//会员号

    private Integer isSendMessage;//是否发送7天后到期信息 1是 2否

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    public AdminVipDto() {
    }

    public AdminVipDto(UserVip userVip) {
        this.id = userVip.getId();
        this.userId = userVip.getUserId();
        if(userVip.getStartTime() != null){
            this.startTime = DateUtil.getFormatDate(userVip.getStartTime(), DateUtil.DATE_FORMT);
        }
        if(userVip.getEndTime() != null){
            this.endTime = DateUtil.getFormatDate(userVip.getEndTime(), DateUtil.DATE_FORMT);
        }
        this.vipLevel = userVip.getVipLevel();
        this.levelDesc = userVip.getLevelDesc();
        this.vipType = userVip.getVipType();
        this.status = userVip.getStatus();
        this.addTime = DateUtil.getFormatDate(userVip.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(userVip.getUpdateTime(),DateUtil.DATE_FORMT);
        this.vipNo = userVip.getVipNo();
        this.isSendMessage = userVip.getIsSendMessage();
        this.nickName = userVip.getNickName();
        this.headImgUrl = userVip.getHeadImgUrl();


    }


    public static List<AdminVipDto> toDtoList(List<UserVip> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminVipDto> dtolist = new ArrayList<>();
        for (UserVip model : list) {
            AdminVipDto dto = new AdminVipDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
