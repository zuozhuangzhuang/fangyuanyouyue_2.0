package com.fangyuanyouyue.user.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.AppVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * APP版本更新表DTO
 */
@Getter
@Setter
@ToString
public class AdminAppVersionDto {
    private Integer id;//版本文件id

    private Integer versionNo;//版本号

    private String versionDesc;//版本描述

    private String versionName;//版本名称

    private String versionUrl;//下载地址

    private String packageName;//安卓包名

    private Integer type;//类型1普通更新 2强制更新

    private String addTime;//添加时间

    private String updateTime;//更新时间

    public AdminAppVersionDto() {
    }

    public AdminAppVersionDto(AppVersion appVersion) {
        this.id = appVersion.getId();
        this.versionNo = appVersion.getVersionNo();
        this.versionDesc = appVersion.getVersionDesc();
        this.versionName = appVersion.getVersionName();
        this.versionUrl = appVersion.getVersionUrl();
        this.packageName = appVersion.getPackageName();
        this.type = appVersion.getType();
        this.addTime = DateUtil.getFormatDate(appVersion.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(appVersion.getUpdateTime(),DateUtil.DATE_FORMT);
    }


    public static List<AdminAppVersionDto> toDtoList(List<AppVersion> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminAppVersionDto> dtolist = new ArrayList<>();
        for (AppVersion model : list) {
            AdminAppVersionDto dto = new AdminAppVersionDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
