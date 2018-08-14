package com.fangyuanyouyue.forum.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private BigDecimal bonus;

    private String label;

    private Integer sort;

    private Integer status;

    private Date endTime;

    private Date updateTime;

    private Date addTime;

    private String content;

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
