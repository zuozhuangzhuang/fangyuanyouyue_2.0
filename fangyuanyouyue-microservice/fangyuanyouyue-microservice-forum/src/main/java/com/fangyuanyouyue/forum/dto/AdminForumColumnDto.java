package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.forum.model.ForumColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 论坛专栏
 */
@Getter
@Setter
@ToString
public class AdminForumColumnDto {

    private Integer id;//专栏id
    
    private Integer typeId;//类型id

    private String typeName;//类型名称

    private String name;//专栏名称

    private String coverImgUrl;//封面图

    private Integer fansCount = 0;//粉丝数量

    public AdminForumColumnDto() {
    	
    }

    public AdminForumColumnDto(ForumColumn forumInfo) {
        this.id = forumInfo.getId();
        this.name = forumInfo.getName();
        this.coverImgUrl = forumInfo.getCoverImgUrl();
        this.fansCount = forumInfo.getFansCount();
        this.typeId = forumInfo.getTypeId();
        this.typeName = forumInfo.getTypeName();
    }
    public static List<AdminForumColumnDto> toDtoList(List<ForumColumn> list) {
        if (list == null) {
            return null;
        }
        List<AdminForumColumnDto> dtolist = new ArrayList<>();
        for (ForumColumn model : list) {
            AdminForumColumnDto dto = new AdminForumColumnDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

   
}
