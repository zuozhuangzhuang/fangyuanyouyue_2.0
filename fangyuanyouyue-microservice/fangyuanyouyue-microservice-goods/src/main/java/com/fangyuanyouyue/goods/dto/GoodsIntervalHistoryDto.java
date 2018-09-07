package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.model.GoodsIntervalHistory;
import com.fangyuanyouyue.goods.model.GoodsIntervalHistory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 抢购降价历史DTO
 */
@Getter
@Setter
@ToString
public class GoodsIntervalHistoryDto {
    private Integer historyId;//降价信息id

    private Integer goodsId;//抢购id

    private BigDecimal markdown;//降价幅度

    private String addTime;//添加时间

    private String updateTime;//更新时间

    private BigDecimal previousPrice;//降价前价格
    public GoodsIntervalHistoryDto() {
    }

    public GoodsIntervalHistoryDto(GoodsIntervalHistory history) {
        this.historyId = history.getId();
        this.goodsId = history.getGoodsId();
        this.markdown = history.getMarkdown();
        this.addTime = DateUtil.getFormatDate(history.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(history.getUpdateTime(),DateUtil.DATE_FORMT);
        this.previousPrice = history.getPreviousPrice();
    }


    public static List<GoodsIntervalHistoryDto> toDtoList(List<GoodsIntervalHistory> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<GoodsIntervalHistoryDto> dtolist = new ArrayList<>();
        for (GoodsIntervalHistory model : list) {
            GoodsIntervalHistoryDto dto = new GoodsIntervalHistoryDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
