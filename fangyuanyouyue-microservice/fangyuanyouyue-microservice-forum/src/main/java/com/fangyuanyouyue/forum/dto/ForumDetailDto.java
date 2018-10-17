package com.fangyuanyouyue.forum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 帖子、视频信息
 */
@Getter
@Setter
@ToString
public class ForumDetailDto {

    private Integer type;
    private String content;
    private String imgUrl;
    
    public ForumDetailDto() {
    	
    }

	
   
}
