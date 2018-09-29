package com.fangyuanyouyue.forum.dto.admin;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.model.Report;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 帖子、视频、全民鉴定举报信息Dto
 */
@Getter
@Setter
@ToString
public class AdminReportDto {
    //举报信息id
    private Integer id;

    //举报人id
    private Integer userId;

    //举报人昵称
    private String nickName;

    //举报人头像
    private String headImgUrl;

    //被举报对象id
    private Integer businessId;

    //被举报对象名
    private String name;

    //被举报时间
    private String addTime;

    //处理时间
    private String updateTime;

    //举报处理状态 1已处理 2未处理
    private Integer status;

    //举报类型 1商品\抢购 2视频 3帖子 4全民鉴定
    private Integer type;
    
    //举报原因
    private String reason;

    public AdminReportDto() {
    }

    public AdminReportDto(Report report) {
        this.id = report.getId();
        this.userId = report.getUserId();
        this.nickName = report.getNickName();
        this.headImgUrl = report.getHeadImgUrl();
        this.businessId = report.getBusinessId();
        this.name = report.getName();
        this.addTime = DateUtil.getFormatDate(report.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(report.getUpdateTime(),DateUtil.DATE_FORMT);
        this.status = report.getStatus();
        this.type = report.getType();
        this.reason = report.getReason();
    }

    public static ArrayList<AdminReportDto> toDtoList(List<Report> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        ArrayList<AdminReportDto> dtolist = new ArrayList<>();
        for (Report model : list) {
            AdminReportDto dto = new AdminReportDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
