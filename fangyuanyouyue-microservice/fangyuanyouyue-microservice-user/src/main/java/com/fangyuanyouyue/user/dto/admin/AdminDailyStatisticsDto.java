package com.fangyuanyouyue.user.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.DailyStatistics;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 每日统计表dto
 */
@Getter
@Setter
@ToString
public class AdminDailyStatisticsDto {
    private Integer id;

    private String date;//日期

    private Integer userCount=0;//新注册用户数

    private Integer orderCount=0;//新增下单数

    private BigDecimal incomeCount=new BigDecimal(0);//平台收入

    private BigDecimal expendCount=new BigDecimal(0);//平台支出

    private Integer goodsCount=0;//新增商品数

    private Integer forumCount=0;//新增帖子数

    private Integer auctionCount=0;//新增抢购数

    private Integer videoCount=0;//新增视频数

    private Integer appraisalCount=0;//新增全民鉴定

    private Integer platformAppraisalCount=0;//新增官方鉴定数

    private Integer vipCount=0;//新开通会员数

    private String addTime;//添加时间

    public AdminDailyStatisticsDto() {
    }

    public AdminDailyStatisticsDto(DailyStatistics dailyStatistics) {
        this.id = dailyStatistics.getId();
        this.date = DateUtil.getFormatDate(dailyStatistics.getDate(),DateUtil.DATE_FORMT_YEAR);
        this.userCount = dailyStatistics.getUserCount();
        this.orderCount = dailyStatistics.getOrderCount();
        this.incomeCount = dailyStatistics.getIncomeCount();
        this.expendCount = dailyStatistics.getExpendCount();
        this.goodsCount = dailyStatistics.getGoodsCount();
        this.forumCount = dailyStatistics.getForumCount();
        this.auctionCount = dailyStatistics.getAuctionCount();
        this.videoCount = dailyStatistics.getVideoCount();
        this.appraisalCount = dailyStatistics.getAppraisalCount();
        this.platformAppraisalCount = dailyStatistics.getPlatformAppraisalCount();
        this.vipCount = dailyStatistics.getVipCount();
        this.addTime = DateUtil.getFormatDate(dailyStatistics.getAddTime(),DateUtil.DATE_FORMT);
    }


    public static List<AdminDailyStatisticsDto> toDtoList(List<DailyStatistics> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminDailyStatisticsDto> dtolist = new ArrayList<>();
        for (DailyStatistics model : list) {
            AdminDailyStatisticsDto dto = new AdminDailyStatisticsDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}