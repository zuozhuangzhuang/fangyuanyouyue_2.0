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
    private Integer userId;

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
    
    private String token;//token

    private String menu;

    private String submenu;
    
    private List<AdminMenuDto> menus;
    

    public AdminOperatorDto(SysOperator model) {
        this.userId = model.getId();
       	this.userCode = model.getUserCode();
       	this.userName = model.getUserName();
       	this.loginPwd = model.getLoginPwd();
       	this.qq = model.getQq();
       	this.email = model.getEmail();
       	this.phone = model.getPhone();
       	this.status = model.getStatus();
       	this.remarks = model.getRemarks();
       	if(model.getLastLoginTime()!=null) {
            this.lastLoginTime = DateUtil.getFormatDate(model.getLastLoginTime(), DateUtil.DATE_FORMT);
        }
       	this.addTime = DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
    }

    public void setMenu(List<AdminMenuDto> menus){
        this.menus = menus;
        menu = "";
        submenu = "";
        boolean menuCurrent = true;
        for(AdminMenuDto dto:menus){
            menu += "<li role='presentation' class=''><a data-toggle='tab' href='#admui-navTabsItem-"+dto.getId()+"' aria-controls='admui-navTabsItem-1' role='tab' aria-expanded='false'><i class='icon "+dto.getIcon()+"'></i><span>"+dto.getText()+"</span></a></li>";
            String active = menuCurrent?"active":"";
            menuCurrent = false;
            submenu += "<div class='tab-pane animation-fade height-full "+active+"' id='admui-navTabsItem-"+dto.getId()+"' role='tabpanel'><div><ul class='site-menu'><li class='site-menu-category'>"+dto.getText()+"</li>";
            for(AdminMenuDto child:dto.getChildren()){
                submenu += "<li class='site-menu-item'><a href='"+child.getUrl()+"' data-iframe target='_blank'><i class='site-menu-icon "+child.getIcon()+"' aria-hidden='true'></i><span class='site-menu-title'>"+child.getText()+"</span></a></li>";
            }
            submenu += "</ul></div></div>";
        }

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
