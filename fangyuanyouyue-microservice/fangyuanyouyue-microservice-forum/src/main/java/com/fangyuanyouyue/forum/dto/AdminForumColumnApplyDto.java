package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.ForumColumnApply;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 论坛专栏
 */
@Getter
@Setter
@ToString
public class AdminForumColumnApplyDto {

    private Integer id;

    private Integer userId;//申请用户id
    
    private String nickName;
    
    private String headImgUrl;
    
    private String phone;

    private Integer typeId;//专栏分类id

    private String columnName;//专栏名

    private Integer status;//状态 0申请中 1通过 2未通过

    private String reason;//拒绝理由

    private String addTime;//添加时间

    public AdminForumColumnApplyDto() {
    	
    }

    public AdminForumColumnApplyDto(ForumColumnApply model) {
        this.id = model.getId();
        this.columnName = model.getColumnName();
        this.status = model.getStatus();
        this.reason = model.getReason();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
        this.typeId = model.getTypeId();
        this.phone = model.getPhone();
        this.nickName = model.getNickName();
        this.headImgUrl = model.getHeadImgUrl();
    }
    
    public static List<AdminForumColumnApplyDto> toDtoList(List<ForumColumnApply> list) {
        if (list == null) {
            return null;
        }
        List<AdminForumColumnApplyDto> dtolist = new ArrayList<>();
        for (ForumColumnApply model : list) {
            AdminForumColumnApplyDto dto = new AdminForumColumnApplyDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

   
}
