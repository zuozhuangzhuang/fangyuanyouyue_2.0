package com.fangyuanyouyue.forum.param;

import com.fangyuanyouyue.base.BasePageReq;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "论坛后台管理参数")
@Getter
@Setter
@ToString
public class AdminForumParam extends BasePageReq{

    @ApiModelProperty(name = "applyId", value = "专栏申请id", dataType = "int",hidden = true)
    private Integer applyId;//专栏申请id

    @ApiModelProperty(name = "coverImgUrl", value = "封面图片地址", dataType = "String",hidden = true)
    private String coverImgUrl;//封面图片地址

    @ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
    private Integer type;//类型

    @ApiModelProperty(name = "reason", value = "拒绝理由", dataType = "String",hidden = true)
    private String reason;//拒绝理由

    @ApiModelProperty(name = "isChosen", value = "是否精选1是 2否", dataType = "int",hidden = true)
    private Integer isChosen;//是否精选1是 2否

    @ApiModelProperty(name = "name", value = "名称", dataType = "String",hidden = true)
    private String name;//名称

    //修改数量
    @ApiModelProperty(name = "count", value = "修改数量", dataType = "int",hidden = true)
    private Integer count;

    //排列
    @ApiModelProperty(name = "sort", value = "排列", dataType = "int",hidden = true)
    private Integer sort;

    //排列
    @ApiModelProperty(name = "title", value = "标题", dataType = "String",hidden = true)
    private String title;

    //内容
    @ApiModelProperty(name = "content", value = "内容", dataType = "String",hidden = true)
    private String content;

}
