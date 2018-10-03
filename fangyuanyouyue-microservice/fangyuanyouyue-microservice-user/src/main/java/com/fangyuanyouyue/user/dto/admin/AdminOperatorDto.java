package com.fangyuanyouyue.user.dto.admin;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.SysOperator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 */
@Getter
@Setter
@ToString
public class AdminOperatorDto {
    private Integer id;

    private String userCode;//登录名

    private String userName;//姓名

    private String loginPwd;//登录密码

    private Integer roleId;//角色

    private String qq;//QQ

    private String email;//邮箱

    private String phone;//手机号码

    private Integer status;//状态

    private String remarks;//备注

    private String lastLoginTime;//最后登录时间

    private String addTime;//添加时间

    private String updateTime;//更新时间
    

    public AdminOperatorDto(SysOperator model) {
        this.id = model.getId();
       	this.userCode = model.getUserCode();
       	this.userName = model.getUserName();
       	this.loginPwd = model.getLoginPwd();
       	this.qq = model.getQq();
       	this.email = model.getEmail();
       	this.phone = model.getPhone();
       	this.status = model.getStatus();
       	this.remarks = model.getRemarks();
       	this.lastLoginTime = DateUtil.getFormatDate(model.getLastLoginTime(), DateUtil.DATE_FORMT);
       	this.addTime = DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
    }


    public static List<AdminOperatorDto> toDtoList(List<SysOperator> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminOperatorDto> dtolist = new ArrayList<>();
        for (SysOperator model : list) {
            AdminOperatorDto dto = new AdminOperatorDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


}
