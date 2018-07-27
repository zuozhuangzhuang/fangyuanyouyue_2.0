package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.forum.model.ForumColumn;

/**
 * 论坛专栏
 */
public class ForumColumnDto {

    private Integer columnId;//帖子id

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

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoverImgUrl() {
		return coverImgUrl;
	}

	public void setCoverImgUrl(String coverImgUrl) {
		this.coverImgUrl = coverImgUrl;
	}

	public Integer getFansCount() {
		return fansCount;
	}

	public void setFansCount(Integer fansCount) {
		this.fansCount = fansCount;
	}

	
   
}
