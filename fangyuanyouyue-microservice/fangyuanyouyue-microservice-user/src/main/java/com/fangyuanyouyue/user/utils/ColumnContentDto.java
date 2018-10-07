package com.fangyuanyouyue.user.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 帖子信息DTO
 */
@Getter
@Setter
@ToString
public class ColumnContentDto{
    private Integer id;
    private Integer type;
    private String content;
    private String imgUrl;
}
