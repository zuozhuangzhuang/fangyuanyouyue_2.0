package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.model.ForumComment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 帖子视频评论
 */
@Getter
@Setter
@ToString
public class ForumCommentDto {

    private Integer commentId;//评论ID

    private Integer userId;//用户id
    
    private String nickName;//昵称
    
    private String headImgUrl;//头像

    private Integer forumId;//帖子id

    private String content;//内容

    private String addTime;//添加时间
    
    private Integer likesCount = 0;//点赞数量
    
    private Integer commentCount = 0;//被回复数量
    
    private Integer isLikes = StatusEnum.NO.getValue(); //是否点赞
    
    public ForumCommentDto() {
    	
    }

    public ForumCommentDto(ForumComment model) {
        this.forumId = model.getForumId();
        this.commentId = model.getId();
        this.content = model.getContent();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
        this.headImgUrl = model.getHeadImgUrl();
        this.nickName = model.getNickName();
        this.userId = model.getUserId();
    }
    public static List<ForumCommentDto> toDtoList(List<ForumComment> list) {
        if (list == null) {
            return null;
        }
        List<ForumCommentDto> dtolist = new ArrayList<>();
        for (ForumComment model : list) {
        	ForumCommentDto dto = new ForumCommentDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }



	
   
}
