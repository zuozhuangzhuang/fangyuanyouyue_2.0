package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.model.ForumInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 帖子、视频信息
 */
@Getter
@Setter
@ToString
public class ForumInfoDto {

    private Integer forumId;//帖子id

    private Integer userId; //作者用户id
    
    private String nickName;//作者昵称
    
    private String headImgUrl;//作者头像

    private String title;//标题

    private String videoUrl;//视频地址url
    
    private String videoImg;//视频封面图片

    private Integer videoLength;//视频长度，单位秒

    private String label;//标签

    private Integer type;//帖子类型 1帖子 2视频

    private Integer status;//帖子状态

    private String addTime;//发布时间

    private String content;//帖子内容
    
   // private List<String> imgs;//帖子图片，最多9张
    
    private Integer commentCount = 0;//评论数量
    
    private Integer likesCount = 0;//点赞数量

    private Integer viewCount = 0;//浏览数量
    
    private Integer isCollect = StatusEnum.NO.getValue(); //是否收藏
    
    private Integer isLikes = StatusEnum.NO.getValue(); //是否点赞
    
    private Integer isFans = StatusEnum.NO.getValue(); //是否关注作者
    
    public ForumInfoDto() {
    	
    }

    public ForumInfoDto(ForumInfo forumInfo) {
        this.forumId = forumInfo.getId();
        this.userId = forumInfo.getUserId();
        this.title = forumInfo.getTitle();
        this.content = forumInfo.getContent();
        this.videoLength = forumInfo.getVideoLength();
        this.videoUrl = forumInfo.getVideoUrl();
        this.addTime = DateUtil.getFormatDate(forumInfo.getAddTime(), DateUtil.DATE_FORMT);
        this.type = forumInfo.getType();
        this.status = forumInfo.getStatus();
        this.videoImg = forumInfo.getVideoImg();
        this.nickName = forumInfo.getNickName();
        this.headImgUrl = forumInfo.getHeadImgUrl();
    }
    
    public static List<ForumInfoDto> toDtoList(List<ForumInfo> list) {
        if (list == null) {
            return null;
        }
        List<ForumInfoDto> dtolist = new ArrayList<>();
        for (ForumInfo model : list) {
            ForumInfoDto dto = new ForumInfoDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


	
   
}
