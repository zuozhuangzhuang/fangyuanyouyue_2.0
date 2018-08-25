package com.fangyuanyouyue.forum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的评论列表帖子、视频Dto
 */
@Getter
@Setter
@ToString
public class MyForumCommentDto {
    private Integer forumId;//帖子id

    private String title;//标题

    private String videoImg;//视频封面图片

    private String content;//帖子内容

    private String commentContent;//评论内容

    public MyForumCommentDto() {
    }

    public MyForumCommentDto(Map model) {
        this.forumId = (int)model.get("id");
        this.title = (String)model.get("title");
        this.videoImg = (String)model.get("video_img");
        this.content = (String)model.get("content");
        this.commentContent = (String)model.get("commentContent");
    }


    public static List<MyForumCommentDto> toDtoList(List<Map> list) {
        if (list == null)
            return new ArrayList<>();
        List<MyForumCommentDto> dtolist = new ArrayList<>();
        for (Map model : list) {
            MyForumCommentDto dto = new MyForumCommentDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
