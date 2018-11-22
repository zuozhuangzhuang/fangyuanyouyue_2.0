package com.fangyuanyouyue.user.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.SysProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 规则DTO
 */
@Getter
@Setter
@ToString
public class SysPropertyDto {
    private Integer id;

    private String keyWord;//信息配置

    private String addTime;//添加时间

    private String updateTime;//更新时间

    private String value;//具体内容

    public SysPropertyDto() {
    }

    public SysPropertyDto(SysProperty sysProperty) {
        this.id = sysProperty.getId();
        this.keyWord = sysProperty.getKeyWord();
        this.addTime = DateUtil.getFormatDate(sysProperty.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(sysProperty.getUpdateTime(),DateUtil.DATE_FORMT);
        this.value = sysProperty.getValue();
    }

}
