package com.fangyuanyouyue.goods.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品评论Dto
 */
public class GoodsCommentDto {
    //评论点赞状态
    private Integer isLike = 2;//评论是否点赞 1已点赞 2未点赞
    //GoodsComment
    private Integer id;//评论ID

    private Integer goodsId;//评论商品id

    private String mainUrl;//商品主图

    private String goodsName;//商品名称

    private String descprition;//商品描述

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
        if (maps == null)
            return null;
        List<GoodsCommentDto> dtolist = new ArrayList<>();
        for (Map<String,Object> model : maps) {
            GoodsCommentDto dto = new GoodsCommentDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public String getImg1Url() {
        return img1Url;
    }

    public void setImg1Url(String img1Url) {
        this.img1Url = img1Url;
    }

    public String getImg2Url() {
        return img2Url;
    }

    public void setImg2Url(String img2Url) {
        this.img2Url = img2Url;
    }

    public String getImg3Url() {
        return img3Url;
    }

    public void setImg3Url(String img3Url) {
        this.img3Url = img3Url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<GoodsCommentDto> getReplys() {
        return replys;
    }

    public void setReplys(List<GoodsCommentDto> replys) {
        this.replys = replys;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserHeadImgUrl() {
        return fromUserHeadImgUrl;
    }

    public void setFromUserHeadImgUrl(String fromUserHeadImgUrl) {
        this.fromUserHeadImgUrl = fromUserHeadImgUrl;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserHeadImgUrl() {
        return toUserHeadImgUrl;
    }

    public void setToUserHeadImgUrl(String toUserHeadImgUrl) {
        this.toUserHeadImgUrl = toUserHeadImgUrl;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDescprition() {
        return descprition;
    }

    public void setDescprition(String descprition) {
        this.descprition = descprition;
    }
}
