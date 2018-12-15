package com.fangyuanyouyue.user.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.model.AppVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * APP版本更新表DTO
 */
@Getter
@Setter
@ToString
public class AppVersionDto {
    private Integer id;//版本文件id

    private Integer versionNo;//版本号

    private String versionDesc;//版本描述

    private String versionName;//版本名称

    private String versionUrl;//下载地址

    private String packageName;//安卓包名

    private Integer type;//类型1普通更新 2强制更新

    private String addTime;//添加时间

    private String updateTime;//更新时间

    private Integer isInvite;//邀请活动按钮开关 1打开 2关闭

    private Integer switchQRCode;//小程序二维码开关 1打开 2关闭

    private String forumSuffix;//帖子相关、全民鉴定相关列表后缀（包含主页、专栏、帖子评论、我的专栏、我的收藏、个人店铺中帖子列表等模块相关列表）

    private String goodsSuffix;//商城列表、抢购列表、同类推荐、他的店铺、搜索列表等后缀（包括个人中与商品、抢购相关的、订单、我的收藏、我的关注等列表）

    private String shopSuffix;//个人店铺列表后缀

    private String videoSuffix;//视频列表后缀（包括个人模块视频相关列表）

    private String detailSuffix;//帖子详情、商品详情后缀

    public AppVersionDto() {
    }

    public AppVersionDto(AppVersion appVersion) {
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

}
