package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.ForumLikes;

/**
 * 点赞
 */
public class ForumLikesDto {

    private Integer likesId;

    private Integer userId;
    
    private String nickName;
    
    private String headImgUrl;

    private Integer forumId;

    private String addTime;
    
    public ForumLikesDto() {
    	
    }

    public ForumLikesDto(ForumLikes model) {
        this.forumId = model.getForumId();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
        this.headImgUrl = model.getHeadImgUrl();
        this.nickName = model.getNickName();
    }
    public static List<ForumLikesDto> toDtoList(List<ForumLikes> list) {
        if (list == null)
            return null;
        List<ForumLikesDto> dtolist = new ArrayList<>();
        for (ForumLikes model : list) {
        	ForumLikesDto dto = new ForumLikesDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

	public Integer getLikesId() {
		return likesId;
	}

	public void setLikesId(Integer likesId) {
		this.likesId = likesId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public Integer getForumId() {
		return forumId;
	}

	public void setForumId(Integer forumId) {
		this.forumId = forumId;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}


	
   
}
