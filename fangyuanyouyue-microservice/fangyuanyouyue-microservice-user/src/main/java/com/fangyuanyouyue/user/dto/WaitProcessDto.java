package com.fangyuanyouyue.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 等待处理消息DTO
 */
@Getter
@Setter
@ToString
public class WaitProcessDto {
    private Integer buy;//我买下的
    private Integer sell;//我卖出的
    private Integer goods;//我的市集
//    private Integer snapUp;//我的抢购
//    private Integer comment;//我的评论
//    private Integer my;//全民鉴定
//    private Integer forum;//我的专栏
//    private Integer follow;//我的关注
//    private Integer fans;//我的粉丝
//    private Integer collect;//我的收藏
//    private Integer appraisal;//我的鉴定

}
