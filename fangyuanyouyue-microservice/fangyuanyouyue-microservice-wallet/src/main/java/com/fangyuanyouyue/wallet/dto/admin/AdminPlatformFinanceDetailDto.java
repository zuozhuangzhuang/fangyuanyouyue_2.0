package com.fangyuanyouyue.wallet.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.model.PlatformFinanceDetail;
import com.fangyuanyouyue.wallet.model.UserBalanceDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 平台收支明细表DTO
 */
@Getter
@Setter
@ToString
public class AdminPlatformFinanceDetailDto {

    private Integer userId;//用户id

    private BigDecimal amount;//金额

    private String payNo;//第三方支付流水号

    private Integer payType;//支付类型 1微信 2支付宝 3余额 4小程序

    private Integer type;//收支类型 1收入 2支出

    private String addTime;//支付时间

    private String updateTime;//更新时间

    private String title;//订单标题

    private String orderNo;//订单号

    private Integer orderType;//订单类型 1商品 13抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺

    private Integer sellerId;//卖家id

    private Integer buyerId;//买家id

    private String nickName;//用户昵称

    public AdminPlatformFinanceDetailDto(PlatformFinanceDetail detail) {
        this.userId = detail.getUserId();
        this.amount = detail.getAmount();
        this.payNo = detail.getPayNo();
        this.payType = detail.getPayType();
        this.type = detail.getType();
        this.addTime = DateUtil.getFormatDate(detail.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(detail.getUpdateTime(),DateUtil.DATE_FORMT);
        this.title = detail.getTitle();
        this.orderNo = detail.getOrderNo();
        this.orderType = detail.getOrderType();
        this.sellerId = detail.getSellerId();
        this.buyerId = detail.getBuyerId();
        this.nickName = detail.getNickName();
    }


    public static List<AdminPlatformFinanceDetailDto> toDtoList(List<PlatformFinanceDetail> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminPlatformFinanceDetailDto> dtolist = new ArrayList<>();
        for (PlatformFinanceDetail model : list) {
            AdminPlatformFinanceDetailDto dto = new AdminPlatformFinanceDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
