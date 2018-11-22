package com.fangyuanyouyue.user.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.SysProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则DTO
 */
@Getter
@Setter
@ToString
public class AdminSysPropertyDto {
    private Integer id;

    private String keyWord;//信息配置表

    private String addTime;//添加时间

    private String updateTime;//更新时间

    private String value;//具体内容

    public AdminSysPropertyDto() {
    }

    public AdminSysPropertyDto(SysProperty sysProperty) {
        this.id = sysProperty.getId();
        this.keyWord = sysProperty.getKeyWord();
        this.addTime = DateUtil.getFormatDate(sysProperty.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(sysProperty.getUpdateTime(),DateUtil.DATE_FORMT);
        this.value = sysProperty.getValue();
    }

    public static List<AdminSysPropertyDto> toDtoList(List<SysProperty> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminSysPropertyDto> dtolist = new ArrayList<>();
        for (SysProperty model : list) {
            AdminSysPropertyDto dto = new AdminSysPropertyDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
