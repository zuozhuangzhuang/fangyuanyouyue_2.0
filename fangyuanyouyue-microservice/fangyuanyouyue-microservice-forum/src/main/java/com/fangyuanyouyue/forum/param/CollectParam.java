package com.fangyuanyouyue.forum.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CollectParam {
    //公用
    @ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
    private Integer start; //起始页

    @ApiModelProperty(name = "limit", value = "限制页", dataType = "int",hidden = true)
    private Integer limit; //限制页

    @ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
    private Integer type;//类型


    @ApiModelProperty(name = "status", value = "状态", dataType = "int",hidden = true)
    private Integer status;//状态

    @ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
    private String token;//用户token


    //Collect
    @ApiModelProperty(name = "collectId", value = "收藏对象ID", dataType = "int",hidden = true)
    private Integer collectId;//收藏对象ID

    @ApiModelProperty(name = "collectType", value = "收藏类型 3视频 4专栏 5鉴赏", dataType = "int",hidden = true)
    private Integer collectType;//收藏类型 3视频 4专栏 5鉴赏

    @ApiModelProperty(name = "search", value = "搜索内容", dataType = "String",hidden = true)
    private String search;//搜索内容

}
