package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.ForumLikes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 点赞DTO
 */
@Getter
@Setter
@ToString
public class ForumLikesDto {

    private Integer likesId;//自增id

    private Integer userId;//用户id
    
    private String nickName;//用户昵称
    
    private String headImgUrl;//用户头像地址

    private Integer forumId;//帖子id

    private String addTime;//添加时间
    
    public ForumLikesDto() {
    	
    }

    public ForumLikesDto(ForumLikes model) {
    	this.likesId = model.getId();
        this.forumId = model.getForumId();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(), DateUtil.DATE_FORMT);
        this.nickName = model.getNickName();
        this.headImgUrl = model.getHeadImgUrl();
        this.userId = model.getUserId();
    }
    public static List<ForumLikesDto> toDtoList(List<ForumLikes> list) {
        if (list == null) {
            return null;
        }
        List<ForumLikesDto> dtolist = new ArrayList<>();
        for (ForumLikes model : list) {
        	ForumLikesDto dto = new ForumLikesDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }



	
   
}
