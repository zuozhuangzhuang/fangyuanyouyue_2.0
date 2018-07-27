package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.forum.model.ForumComment;
import com.fangyuanyouyue.forum.utils.DateUtil;

/**
 * 评论
 */
public class ForumCommentDto {

    private Integer commentId;//评论ID

    private Integer userId;
    
    private String nickName;
    
    private String headImgUrl;

    private Integer forumId;

    private String content;

    private String addTime;
    
    public ForumCommentDto() {
    	
    }

    public ForumCommentDto(ForumComment model) {
        this.forumId = model.getForumId();
        this.commentId = model.getId();
        this.content = model.getContent();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
        this.headImgUrl = model.getHeadImgUrl();
        this.nickName = model.getNickName();
    }
    public static List<ForumCommentDto> toDtoList(List<ForumComment> list) {
        if (list == null)
            return null;
        List<ForumCommentDto> dtolist = new ArrayList<>();
        for (ForumComment model : list) {
        	ForumCommentDto dto = new ForumCommentDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

	public Integer getForumId() {
		return forumId;
	}

	public void setForumId(Integer forumId) {
		this.forumId = forumId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}


	
   
}
