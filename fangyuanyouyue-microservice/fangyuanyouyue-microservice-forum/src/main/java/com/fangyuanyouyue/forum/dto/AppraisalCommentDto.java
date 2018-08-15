package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.AppraisalComment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 鉴定评论dto
 */
@Getter
@Setter
@ToString
public class AppraisalCommentDto {

    private Integer commentId;//评论id

    private Integer userId;
    
    private String nickName;//作者昵称
    
    private String headImgUrl;//作者头像

    private Integer appraisalId;//鉴定id

    private Integer viewpoint;//评论观点

    private String content;//评论内容

    private String pic1;//图片1

    private String pic2;

    private String pic3;

    private String pic4;

    private String pic5;

    private String pic6;

    private Integer isWinner; //是否赢了
    
    private Integer isLikes; //是否点赞
    
    private Integer likesCount; //同意次数

    private Integer status;//状态

    private String addTime;//添加时间

    private String updateTime;//修改时间


    public AppraisalCommentDto(AppraisalComment comment) {
        this.commentId = comment.getId();
        this.userId = comment.getUserId();
        this.appraisalId = comment.getAppraisalId();
        this.viewpoint = comment.getViewpoint();
        this.content = comment.getContent();
        this.pic1 = comment.getPic1();
        this.pic2 = comment.getPic2();
        this.pic3 = comment.getPic3();
        this.pic4 = comment.getPic4();
        this.pic5 = comment.getPic5();
        this.pic6 = comment.getPic6();
        this.isWinner = comment.getIsWinner();
        this.status = comment.getStatus();
        this.addTime = DateUtil.getFormatDate(comment.getUpdateTime(), DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(comment.getAddTime(), DateUtil.DATE_FORMT);
    }

    public static List<AppraisalCommentDto> toDtoList(List<AppraisalComment> list) {
        if (list == null)
            return null;
        List<AppraisalCommentDto> dtolist = new ArrayList<>();
        for (AppraisalComment model : list) {
            AppraisalCommentDto dto = new AppraisalCommentDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
