package com.fangyuanyouyue.goods.dto.adminDto;

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
public class AdminAppraisalUrlDto {
    private Integer appraisalUrlId;//鉴定图片表ID

    private Integer appraisalId;//鉴定详情id

    private String imgUrl;//图片/视频地址

    private Integer type;//类型 1图片 2视频 3视频截图

//    private String addTime;//添加时间

    public AdminAppraisalUrlDto() {
    }

    public AdminAppraisalUrlDto(AppraisalUrl appraisalUrl) {
        this.appraisalUrlId = appraisalUrl.getId();
        this.appraisalId = appraisalUrl.getAppraisalId();
        this.imgUrl = appraisalUrl.getUrl();
        this.type = appraisalUrl.getType();
//        this.addTime = DateUtil.getFormatDate(appraisalUrl.getAddTime(), DateUtil.DATE_FORMT);
    }


    public static ArrayList<AdminAppraisalUrlDto> toDtoList(List<AppraisalUrl> list) {
        if (list == null) {
            return null;
        }
        ArrayList<AdminAppraisalUrlDto> dtolist = new ArrayList<>();
        for (AppraisalUrl model : list) {
            AdminAppraisalUrlDto dto = new AdminAppraisalUrlDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
