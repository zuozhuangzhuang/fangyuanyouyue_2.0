package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ForumInfo {
    private Integer id;//唯一自增ID

    private Integer userId;//发布用户id

    private String title;//标题

    private String videoUrl;//视频链接

    private Integer videoLength;//视频长度

    private String label;//标签

    private Integer sort;//排列优先级

    private Integer type;//帖子类型 1图文 2视频

    private Integer status;//状态 1显示 2隐藏

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer columnId;//专栏id

    private Integer isChosen;//是否精选1是 2否

    private String videoImg;//视频封面图

    private String content;//内容描述，富文本

    private String nickName;//用户昵称

    private String headImgUrl;//用户头像

    private Integer pvCount;//帖子浏览量基数，展示浏览量为基数＋浏览量个数
}