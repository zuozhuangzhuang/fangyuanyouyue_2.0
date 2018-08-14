package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.AppraisalUrl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 鉴定图片表DTO
 */
@Getter
@Setter
@ToString
public class AppraisalUrlDto {
    private Integer appraisalUrlId;//鉴定图片表ID

    private Integer appraisalId;//鉴定详情id

    private String url;//图片/视频地址

    private Integer type;//类型 1图片 2视频 3视频截图

//    private String addTime;//添加时间

    public AppraisalUrlDto() {
    }

    public AppraisalUrlDto(AppraisalUrl appraisalUrl) {
        this.appraisalUrlId = appraisalUrl.getId();
        this.appraisalId = appraisalUrl.getAppraisalId();
        this.url = appraisalUrl.getUrl();
        this.type = appraisalUrl.getType();
//        this.addTime = DateUtil.getFormatDate(appraisalUrl.getAddTime(), DateUtil.DATE_FORMT);
    }


    public static ArrayList<AppraisalUrlDto> toDtoList(List<AppraisalUrl> list) {
        if (list == null)
            return null;
        ArrayList<AppraisalUrlDto> dtolist = new ArrayList<>();
        for (AppraisalUrl model : list) {
            AppraisalUrlDto dto = new AppraisalUrlDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
