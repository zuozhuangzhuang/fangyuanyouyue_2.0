package com.fangyuanyouyue.wallet.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.model.UserVip;
import com.fangyuanyouyue.wallet.model.UserWallet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 钱包信息DTO
 */
@Getter
@Setter
@ToString
public class WalletDto {
    private Integer walletId;//钱包信息ID

    private Integer userId;//用户id

    private BigDecimal balance;//账户余额

    private BigDecimal balanceFrozen;//冻结余额

    private Long point;//积分余额

    private Long score;//用户总积分

    private Long credit;//信誉度分值
    /*
    差：小于-100分
    低：-100~1000分之间
    中：1000~1万之间
    高：1万~50万之间
    优：50万以上
     */
    private Integer creditLevel;//信誉度等级 1差 2低 3中 4高 5优

    private String addTime;//添加时间

    private String updateTime;//更新时间

    private Integer level;//用户等级
    /*
    Lv1：0~500分
    Lv2：3000分
    Lv3：1万分
    Lv4：3万分
    Lv5：8万分
    Lv6：20万分
    Lv7：60万分
    Lv8：100万分
    Lv9：500万分
     */
    private String levelDesc;//等级描述(升级还需xxx积分)

    private String startTime;//会员开通时间

    private String endTime;//会员过期时间

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private String vipLevelDesc;//会员等级描述

    private Integer vipType;//会员类型 1体验会员 2月会员 3年会员

    private Integer status;//会员状态1已开通 2未开通

    private String vipNo;//会员号


    public WalletDto() {
    }

    public WalletDto(UserWallet userWallet, UserVip userVip) {
        if(userWallet != null){
            this.walletId = userWallet.getId();
            this.userId = userWallet.getUserId();
            this.balance = userWallet.getBalance();
            this.balanceFrozen = userWallet.getBalanceFrozen();
            this.point = userWallet.getPoint();
            this.score = userWallet.getScore();
            this.addTime = DateUtil.getFormatDate(userWallet.getAddTime(), DateUtil.DATE_FORMT);
            this.updateTime = DateUtil.getFormatDate(userWallet.getAddTime(), DateUtil.DATE_FORMT);
            this.score = userWallet.getScore();
            //积分等级，计算总积分
            if(score != null){
                if(0 <= score && score < 500){//Lv1
                    this.level = 1;
                    this.levelDesc = "升级还需"+(500-score)+"积分";
                }else if(500 <= score && score < 3000){//Lv2
                    this.level = 2;
                    this.levelDesc = "升级还需"+(3000-score)+"积分";
                }else if(3000 <= score && score < 10000){//Lv3
                    this.level = 3;
                    this.levelDesc = "升级还需"+(10000-score-score)+"积分";
                }else if(10000 <= score && score < 30000){//Lv4
                    this.level = 4;
                    this.levelDesc = "升级还需"+(30000-score)+"积分";
                }else if(30000 <= score && score < 80000){//Lv5
                    this.level = 5;
                    this.levelDesc = "升级还需"+(80000-score)+"积分";
                }else if(80000 <= score && score < 200000){//Lv6
                    this.level = 6;
                    this.levelDesc = "升级还需"+(200000-score)+"积分";
                }else if(200000 <= score && score < 600000){//Lv7
                    this.level = 7;
                    this.levelDesc = "升级还需"+(600000-score)+"积分";
                }else if(600000 <= score && score < 1000000){//Lv8
                    this.level = 8;
                    this.levelDesc = "升级还需"+(1000000-score)+"积分";
                }else if(1000000 <= score){//Lv9
                    this.level = 9;
                    this.levelDesc = "您已升至满级！";
                }
            }
        }
        if(userVip != null){
            if(userVip.getStartTime() != null){
                this.startTime = DateUtil.getFormatDate(userVip.getStartTime(), DateUtil.DATE_FORMT);
            }
            if(userVip.getEndTime() != null){
                this.endTime = DateUtil.getFormatDate(userVip.getEndTime(), DateUtil.DATE_FORMT);
            }
            this.vipLevel = userVip.getVipLevel();
            this.vipLevelDesc = userVip.getLevelDesc();
            this.vipType = userVip.getVipType();
            this.status = userVip.getStatus();
            this.vipNo = userVip.getVipNo();
        }
    }

}
