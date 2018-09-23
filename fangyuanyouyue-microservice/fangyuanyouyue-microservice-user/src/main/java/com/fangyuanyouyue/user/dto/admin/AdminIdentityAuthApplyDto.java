package com.fangyuanyouyue.user.dto.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.IdentityAuthApply;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户反馈信息dto
 */
@Getter
@Setter
@ToString
public class AdminIdentityAuthApplyDto {

    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private String name;//姓名

    private String identity;//身份证号码

    private String identityImgCover;//身份证封面图

    private String identityImgBack;//身份证背面

    private String rejectDesc;//拒绝原因

    private Integer status;//状态 1申请 2通过 3拒绝

    private String addTime;//添加时间

    private String phone;//手机号码

    private String nickName;//昵称
    

    public AdminIdentityAuthApplyDto(IdentityAuthApply model) {
        this.id = model.getId();
        this.userId = model.getUserId();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(),DateUtil.DATE_FORMT);
        this.nickName = model.getNickName();
        this.name = model.getName();
        this.identity = model.getIdentity();
        this.identityImgBack = model.getIdentityImgBack();
        this.identityImgCover = model.getIdentityImgCover();
        this.rejectDesc = model.getRejectDesc();
        this.phone = model.getPhone();
        this.status = model.getStatus();
    }


    public static List<AdminIdentityAuthApplyDto> toDtoList(List<IdentityAuthApply> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminIdentityAuthApplyDto> dtolist = new ArrayList<>();
        for (IdentityAuthApply model : list) {
            AdminIdentityAuthApplyDto dto = new AdminIdentityAuthApplyDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


}
