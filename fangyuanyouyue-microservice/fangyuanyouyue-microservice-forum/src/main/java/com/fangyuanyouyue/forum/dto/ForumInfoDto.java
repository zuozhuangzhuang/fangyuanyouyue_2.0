package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.ForumInfo;

/**
 * 论坛信息
 */
public class ForumInfoDto {

    private Integer forumId;//帖子id

    private Integer userId; //作者用户id
    
    private String nickName;//作者昵称
    
    private String headImgUrl;//作者头像

    private String title;//标题

    private String videoUrl;//视频地址url

    private Integer videoLength;//视频长度，单位秒

    private String label;//标签

    private Integer type;//帖子类型 1帖子 2视频

    private Integer status;//帖子状态

    private String addTime;//发布时间

    private String content;//帖子内容
    
    private List<String> imgs;//帖子图片，最多9张
    
    private Long commentCount;//评论数量
    
    private Long likesCount;//点赞数量
    
    public ForumInfoDto() {
    	
    }

    public ForumInfoDto(ForumInfo forumInfo) {
        this.forumId = forumInfo.getId();
        this.title = forumInfo.getTitle();
        this.content = forumInfo.getContent();
        this.videoLength = forumInfo.getVideoLength();
        this.videoUrl = forumInfo.getVideoUrl();
        this.addTime = DateUtil.getFormatDate(forumInfo.getAddTime(), DateUtil.DATE_FORMT);
        this.type = forumInfo.getType();
        this.status = forumInfo.getStatus();
        this.nickName = forumInfo.getNickName();
        this.headImgUrl = forumInfo.getHeadImgUrl();
    }
    public static List<ForumInfoDto> toDtoList(List<ForumInfo> list) {
        if (list == null)
            return null;
        List<ForumInfoDto> dtolist = new ArrayList<>();
        for (ForumInfo model : list) {
            ForumInfoDto dto = new ForumInfoDto(model);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Integer getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(Integer videoLength) {
		this.videoLength = videoLength;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Long getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(Long likesCount) {
		this.likesCount = likesCount;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	
   
}
