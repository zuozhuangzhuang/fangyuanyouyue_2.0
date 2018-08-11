package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.ForumComment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论
 */
@Getter
@Setter
@ToString
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



	
   
}
