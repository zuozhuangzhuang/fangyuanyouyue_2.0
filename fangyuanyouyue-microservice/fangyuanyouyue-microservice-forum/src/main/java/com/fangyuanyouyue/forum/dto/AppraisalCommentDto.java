package com.fangyuanyouyue.forum.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fangyuanyouyue.forum.model.AppraisalComment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * banner
 */
@Getter
@Setter
@ToString
public class AppraisalCommentDto {

    private Integer commentId;

    private Integer userId;

    private Integer appraisalId;

    private Integer viewpoint;

    private String content;

    private String pic1;

    private String pic2;

    private String pic3;

    private String pic4;

    private String pic5;

    private String pic6;

    private Integer isWinner;

    private Integer status;

    private Date addTime;

    private Date updateTime;

    public AppraisalCommentDto() {
    	
    }

    public AppraisalCommentDto(AppraisalComment model) {
       
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
