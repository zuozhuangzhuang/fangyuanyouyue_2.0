package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.ForumInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 论坛信息
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

    private Integer videoLength;//视频长度，单位秒

    private String label;//标签

    private Integer type;//帖子类型 1帖子 2视频

    private Integer status;//帖子状态

    private String addTime;//发布时间

    private String content;//帖子内容
    
    private List<String> imgs;//帖子图片，最多9张
    
    private Long commentCount;//评论数量
    
    private Long likesCount;//点赞数量

    private Long viewCount;//浏览数量
    
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


	
   
}
