package com.fangyuanyouyue.wallet.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.model.BonusPool;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 奖池DTO
 */
@Getter
@Setter
@ToString
public class BonusPoolDto {
    private Integer bonusId;//唯一自增ID

    private String bonusName;//奖品名字

//    private Double probability;//中奖概率

    private String addTime;//添加时间

    private String updateTime;//更新时间

    public BonusPoolDto() {
    }

    public BonusPoolDto(BonusPool bonusPool) {
        this.bonusId = bonusPool.getId();
        this.bonusName = bonusPool.getBonusName();
//        this.probability = bonusPool.getProbability();
        this.addTime = DateUtil.getFormatDate(bonusPool.getAddTime(), DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(bonusPool.getUpdateTime(), DateUtil.DATE_FORMT);
    }

    public static List<BonusPoolDto> toDtoList(List<BonusPool> list) {
        if (list == null)
            return null;
        List<BonusPoolDto> dtolist = new ArrayList<>();
        for (BonusPool model : list) {
            BonusPoolDto dto = new BonusPoolDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
