package com.fangyuanyouyue.goods.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品评论Dto
 */
@Getter
@Setter
@ToString
public class GoodsCommentDto {
    //评论点赞状态
    private Integer isLike = 2;//评论是否点赞 1已点赞 2未点赞
    //GoodsComment
    private Integer id;//评论ID

    private Integer goodsId;//评论商品id

    private String mainUrl;//商品主图

    private String goodsName;//商品名称

    private String description;//商品描述

    private Integer fromUserId;//发表评论用户id

    private String fromUserName;//发表评论用户昵称

    private String fromUserHeadImgUrl;//发表评论用户头像

    private Integer toUserId;//被评论用户id

    private String toUserName;//被评论用户昵称

    private String toUserHeadImgUrl;//被评论用户头像


    private Integer commentId;//回复评论id

    private String content;//评论内容

    private Integer likesCount;//点赞次数

    private String img1Url;//图片地址1

    private String img2Url;//图片地址2

    private String img3Url;//图片地址3

    private Integer status;//状态 1正常 2隐藏

    private List<GoodsCommentDto> replys;//评论下的回复

    public GoodsCommentDto() {
    }


    public GoodsCommentDto(Map map) {
        this.id = (Integer)map.get("id");
        this.goodsId = (Integer)map.get("goods_id");
        this.fromUserId = (Integer)map.get("user_id");
        this.fromUserName = (String)map.get("nick_name");
        this.fromUserHeadImgUrl = (String)map.get("head_img_url");
        this.commentId = (Integer)map.get("comment_id");
        this.content = (String)map.get("content");
        this.likesCount = (Integer)map.get("likes_count");
        this.img1Url = (String)map.get("img1_url");
        this.img2Url = (String)map.get("img2_url");
        this.img3Url = (String)map.get("img3_url");
        this.status = (Integer)map.get("status");
    }


    public static List<GoodsCommentDto> mapToDtoList(List<Map<String,Object>> maps) {
        if (maps == null) {
            return null;
        }
        List<GoodsCommentDto> dtolist = new ArrayList<>();
        for (Map<String,Object> model : maps) {
            GoodsCommentDto dto = new GoodsCommentDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
