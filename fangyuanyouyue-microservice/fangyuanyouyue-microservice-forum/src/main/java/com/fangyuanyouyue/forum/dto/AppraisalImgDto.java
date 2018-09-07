package com.fangyuanyouyue.forum.dto;

import com.fangyuanyouyue.forum.model.AppraisalImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


/**
 * 鉴定图片DTO
 */
@Getter
@Setter
@ToString
public class AppraisalImgDto {

    private String imgUrl;//图片路径

    private Integer appraisalId;//鉴定id

    private String addTime;//添加时间

    public AppraisalImgDto() {
    }

    public AppraisalImgDto(AppraisalImg img) {
        this.imgUrl = img.getImgUrl();
        this.appraisalId = img.getAppraisalId();
        this.addTime = img.getImgUrl();
    }


    public static ArrayList<AppraisalImgDto> toDtoList(List<AppraisalImg> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        ArrayList<AppraisalImgDto> dtolist = new ArrayList<>();
        for (AppraisalImg model : list) {
            AppraisalImgDto dto = new AppraisalImgDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
