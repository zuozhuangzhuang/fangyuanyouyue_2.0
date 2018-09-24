package com.fangyuanyouyue.user.dto.admin;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.UserAuthApply;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户反馈信息dto
 */
@Getter
@Setter
@ToString
public class AdminUserAuthApplyDto {

    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private Integer status;//认证状态 1申请中 2同意 3拒绝

    private String reason;//拒绝原因

    private String addTime;//申请时间

    private String phone;//手机号码

    private String nickName;//昵称
    

    public AdminUserAuthApplyDto(UserAuthApply model) {
        this.id = model.getId();
        this.userId = model.getUserId();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(),DateUtil.DATE_FORMT);
        this.nickName = model.getNickName();
        this.phone = model.getPhone();
        this.status = model.getStatus();
        this.reason = model.getReason();
        
    }


    public static List<AdminUserAuthApplyDto> toDtoList(List<UserAuthApply> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminUserAuthApplyDto> dtolist = new ArrayList<>();
        for (UserAuthApply model : list) {
            AdminUserAuthApplyDto dto = new AdminUserAuthApplyDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


}
