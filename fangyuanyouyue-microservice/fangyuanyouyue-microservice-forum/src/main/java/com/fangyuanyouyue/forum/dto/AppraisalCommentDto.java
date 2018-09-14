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
    
    private String nickName;//评论作者昵称
    
    private String headImgUrl;//评论作者头像

    private Integer appraisalId;//鉴定id

    private Integer viewpoint;//评论观点 1看真 2看假

    private String content;//评论内容

    private Integer isWinner = 2; //是否赢了 1是 2否
    
    private Integer isLikes = 2; //是否点赞 1是 2否
    
    private Integer likesCount; //同意数

    private Integer status;//状态

    private String addTime;//添加时间

    private String updateTime;//修改时间


    public AppraisalCommentDto(AppraisalComment comment) {
        this.commentId = comment.getId();
        this.userId = comment.getUserId();
        this.appraisalId = comment.getAppraisalId();
        this.viewpoint = comment.getViewpoint();
        this.content = comment.getContent();
        this.isWinner = comment.getIsWinner();
        this.status = comment.getStatus();
        this.addTime = DateUtil.getFormatDate(comment.getAddTime(), DateUtil.DATE_FORMT);
        if(comment.getUpdateTime() != null){
            this.updateTime = DateUtil.getFormatDate(comment.getUpdateTime(), DateUtil.DATE_FORMT);
        }
        this.nickName = comment.getNickName();
        this.headImgUrl = comment.getHeadImgUrl();
    }

    public static List<AppraisalCommentDto> toDtoList(List<AppraisalComment> list) {
        if (list == null) {
            return null;
        }
        List<AppraisalCommentDto> dtolist = new ArrayList<>();
        for (AppraisalComment model : list) {
            AppraisalCommentDto dto = new AppraisalCommentDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
