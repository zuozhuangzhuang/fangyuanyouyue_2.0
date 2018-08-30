package com.fangyuanyouyue.forum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 我是栏主DTO
 */
@Getter
@Setter
@ToString
public class MyColumnDto {
    private Integer columnId;//专栏id

    private String name;//专栏名称

    private Integer todayPvCount;//今日活跃量

    private List<ForumInfoDto> forumList;//专栏内帖子列表
}
