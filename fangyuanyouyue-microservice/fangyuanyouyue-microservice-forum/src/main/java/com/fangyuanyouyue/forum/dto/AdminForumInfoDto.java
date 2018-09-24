package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
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
public class AdminForumInfoDto {

    private Integer id;//帖子id

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
    
    private Integer totalCount = 0;//总数量

    private Integer realCount = 0;//真实数量

    private Integer baseCount = 0;//基础数量
    
    private String columnName;//专栏名称
    
    public AdminForumInfoDto() {
    	
    }

    public AdminForumInfoDto(ForumInfo forumInfo) {
        this.id = forumInfo.getId();
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
        this.baseCount = forumInfo.getPvCount();
        this.columnName = forumInfo.getColumnName();
    }
    
    public static List<AdminForumInfoDto> toDtoList(List<ForumInfo> list) {
        if (list == null) {
            return null;
        }
        List<AdminForumInfoDto> dtolist = new ArrayList<>();
        for (ForumInfo model : list) {
            AdminForumInfoDto dto = new AdminForumInfoDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }


	
   
}
