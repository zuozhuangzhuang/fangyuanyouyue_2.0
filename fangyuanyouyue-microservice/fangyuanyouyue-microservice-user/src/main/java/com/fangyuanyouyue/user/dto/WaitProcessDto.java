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
    private Integer buy = 0;//我买下的（待付款+待收货）
    private Integer sell = 0;//我卖出的（待发货+待处理退货）
    private Integer goods = 0;//我的市集（待处理的议价）
//    private Integer snapUp;//我的抢购
//    private Integer comment;//我的评论
//    private Integer my;//全民鉴定
//    private Integer forum;//我的专栏
//    private Integer follow;//我的关注
//    private Integer fans;//我的粉丝
//    private Integer collect;//我的收藏
//    private Integer appraisal;//我的鉴定

}
