package com.fangyuanyouyue.forum.dto.admin;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
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
    
    private String nickName;//栏主名称

    private String coverImgUrl;//封面图

    private Integer totalCount = 0;//总数量

    private Integer realCount = 0;//真实数量

    private Integer baseCount = 0;//基础数量

    private Integer isChosen;//是否精选1是 2否

    private String addTime;

    public AdminForumColumnDto() {
    	
    }

    public AdminForumColumnDto(ForumColumn forumInfo) {
        this.id = forumInfo.getId();
        this.name = forumInfo.getName();
        this.coverImgUrl = forumInfo.getCoverImgUrl();
        this.typeId = forumInfo.getTypeId();
        this.typeName = forumInfo.getTypeName();
        this.nickName = forumInfo.getNickName();
        this.isChosen = forumInfo.getIsChosen();
        this.addTime = DateUtil.getFormatDate(forumInfo.getAddTime(), DateUtil.DATE_FORMT);
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
