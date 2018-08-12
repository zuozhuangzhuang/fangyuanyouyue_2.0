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
public class ForumColumnDto {

    private Integer columnId;//帖子id
    
    private Integer typeId;//类型id
    
    private String typeName;//类型名称

    private String name;//专栏名称

    private String coverImgUrl;//封面图

    private Integer fansCount;//粉丝数量

    public ForumColumnDto() {
    	
    }

    public ForumColumnDto(ForumColumn forumInfo) {
        this.columnId = forumInfo.getId();
        this.name = forumInfo.getName();
        this.coverImgUrl = forumInfo.getCoverImgUrl();
        this.fansCount = forumInfo.getFansCount();
        this.typeId = forumInfo.getTypeId();
        this.typeName = forumInfo.getTypeName();
    }
    public static List<ForumColumnDto> toDtoList(List<ForumColumn> list) {
        if (list == null)
            return null;
        List<ForumColumnDto> dtolist = new ArrayList<>();
        for (ForumColumn model : list) {
            ForumColumnDto dto = new ForumColumnDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

   
}
