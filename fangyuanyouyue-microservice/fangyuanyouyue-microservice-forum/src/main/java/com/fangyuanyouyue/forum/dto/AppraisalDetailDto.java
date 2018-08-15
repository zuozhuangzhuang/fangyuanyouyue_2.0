package com.fangyuanyouyue.forum.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.model.AppraisalDetail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * banner
 */
@Getter
@Setter
@ToString
public class AppraisalDetailDto {

    private Integer appraisalId;

    private Integer userId;

    private String title;
    
    private String nickName;//作者昵称
    
    private String headImgUrl;//作者头像

    private BigDecimal bonus; //奖金

    private String label;

    private Integer status; 

    private String endTime; //介绍日期

    private String addTime; //发布日期

    private String content;
    
    private Integer commentCount = 0;//评论数量
    
    private Integer likesCount = 0;//点赞数量

    private Integer viewCount = 0;//浏览数量
    
    private Integer truthCount = 0;//看真数量
    
    private Integer untruthCount = 0; //看假数量
    
    private Integer isCollect = StatusEnum.NO.getValue(); //是否收藏
    
    private Integer isLikes = StatusEnum.NO.getValue(); //是否点赞

    public AppraisalDetailDto() {
    	
    }

    public AppraisalDetailDto(AppraisalDetail model) {
       
    }
    public static List<AppraisalDetailDto> toDtoList(List<AppraisalDetail> list) {
        if (list == null)
            return null;
        List<AppraisalDetailDto> dtolist = new ArrayList<>();
        for (AppraisalDetail model : list) {
            AppraisalDetailDto dto = new AppraisalDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
