package com.fangyuanyouyue.goods.dto.adminDto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.model.Report;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品举报信息Dto
 */
@Getter
@Setter
@ToString
public class AdminReportGoodsDto {
    //举报信息id
    private Integer id;

    //举报人id
    private Integer userId;

    //举报人昵称
    private String nickName;

    //举报人头像
    private String headImgUrl;

    //被举报商品id
    private Integer goodsId;

    //被举报商品名
    private String goodsName;

    //被举报时间
    private String addTime;

    //处理时间
    private String updateTime;

    //举报处理状态 1已处理 2未处理
    private Integer status;
    
    private String reason;//举报原因

    private Integer type;//举报原因

    public AdminReportGoodsDto() {
    }

    public AdminReportGoodsDto(Report report) {
        this.id = report.getId();
        this.userId = report.getUserId();
        this.nickName = report.getNickName();
        this.headImgUrl = report.getHeadImgUrl();
        this.goodsId = report.getBusinessId();
        this.goodsName = report.getGoodsName();
        this.addTime = DateUtil.getFormatDate(report.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(report.getUpdateTime(),DateUtil.DATE_FORMT);
        this.status = report.getStatus();
        this.reason = report.getReason();
        this.type = report.getType();
    }

    public static ArrayList<AdminReportGoodsDto> toDtoList(List<Report> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        ArrayList<AdminReportGoodsDto> dtolist = new ArrayList<>();
        for (Report model : list) {
            AdminReportGoodsDto dto = new AdminReportGoodsDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
