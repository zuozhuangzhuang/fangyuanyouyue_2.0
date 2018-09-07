package com.fangyuanyouyue.wallet.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.model.UserBalanceDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 余额账单Dto
 */
@Getter
@Setter
@ToString
public class UserBalanceDto {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private BigDecimal amount;//金额

    private BigDecimal beforAmount;//操作前金额

    private BigDecimal afterAmount;//操作后金额

    private Integer payType;//支付类型 1微信 2支付宝 3余额

    private Integer type;//收支类型 1收入 2支出

    private String addTime;//支付时间

    private String receivedTime;//到账时间

    private String title;//订单标题

    private String orderNo;//订单号

    private Integer orderType;//订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5申请专栏 6充值 7开通、续费会员 8认证店铺

    private String imgUrl;//图片路径

    private Integer sellerId;//卖家id

    private Integer buyerId;//买家id

    public UserBalanceDto() {
    }

    public UserBalanceDto(UserBalanceDetail model) {
        this.id = model.getId();
        this.userId = model.getUserId();
        this.amount = model.getAmount();
        this.beforAmount = model.getBeforAmount();
        this.afterAmount = model.getAfterAmount();
        this.payType = model.getPayType();
        this.type = model.getType();
        this.addTime = DateUtil.getFormatDate(model.getAddTime(),DateUtil.DATE_FORMT);
        this.title = model.getTitle();
        this.orderNo = model.getOrderNo();
        this.orderType = model.getOrderType();
        this.sellerId = model.getSellerId();
        this.buyerId = model.getBuyerId();
    }
    
    public static List<UserBalanceDto> toDtoList(List<UserBalanceDetail> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<UserBalanceDto> dtolist = new ArrayList<>();
        for (UserBalanceDetail model : list) {
            UserBalanceDto dto = new UserBalanceDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
