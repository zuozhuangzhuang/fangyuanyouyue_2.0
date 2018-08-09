package com.fangyuanyouyue.wallet.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.model.UserWallet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

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

    private Date startTime;//会员开通时间

    private Date endTime;//会员过期时间

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private String vipLevelDesc;//会员等级描述

    private Integer vipType;//会员类型 1体验会员 2月会员 3年会员

    private Integer status;//会员状态1已开通 2未开通


    public WalletDto() {
    }

    public WalletDto(UserWallet userWallet) {
        this.walletId = userWallet.getId();
        this.userId = userWallet.getUserId();
        this.balance = userWallet.getBalance();
        this.balanceFrozen = userWallet.getBalanceFrozen();
        this.point = userWallet.getPoint();
        this.score = userWallet.getScore();
        this.addTime = DateUtil.getFormatDate(userWallet.getAddTime(), DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(userWallet.getAddTime(), DateUtil.DATE_FORMT);
    }

}
