package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * APP版本更新表
 */
@Getter
@Setter
@ToString
public class AppVersion {
    private Integer id;//版本文件id

    private Integer versionNo;//版本号

    private String versionDesc;//版本描述

    private String versionName;//版本名称

    private String versionUrl;//下载地址

    private String packageName;//安卓包名

    private Integer type;//类型1普通更新 2强制更新

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}