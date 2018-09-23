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
}
