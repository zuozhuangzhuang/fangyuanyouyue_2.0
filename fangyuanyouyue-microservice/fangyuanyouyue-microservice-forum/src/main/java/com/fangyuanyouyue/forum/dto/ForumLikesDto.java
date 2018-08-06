package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.ForumLikes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 点赞
 */
@Getter
@Setter
@ToString
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



	
   
}
