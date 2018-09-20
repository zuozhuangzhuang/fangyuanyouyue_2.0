package com.fangyuanyouyue.wallet.dto.admin;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.model.UserBalanceDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户收支信息Dto
 */
@Getter
@Setter
@ToString
public class AdminUserBalanceDto {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private BigDecimal amount;//金额

    private BigDecimal beforAmount;//操作前金额

    private BigDecimal afterAmount;//操作后金额

    private Integer payType;//支付类型 1微信 2支付宝 3余额 4小程序

    private Integer type;//收支类型 1收入 2支出 3退款

    private String addTime;//支付时间

    private String receivedTime;//到账时间

    private String title;//订单标题

    private String orderNo;//订单号

    private Integer orderType;//订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺

    private String imgUrl;//图片路径

    private Integer sellerId;//卖家id

    private Integer buyerId;//买家id

    private String payNo;//第三方支付流水号

    private String nickName;//用户昵称

    public AdminUserBalanceDto() {
    }

    public AdminUserBalanceDto(UserBalanceDetail model) {
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
        this.payNo = model.getPayNo();
        this.nickName = model.getNickName();
    }
    
    public static List<AdminUserBalanceDto> toDtoList(List<UserBalanceDetail> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminUserBalanceDto> dtolist = new ArrayList<>();
        for (UserBalanceDetail model : list) {
            AdminUserBalanceDto dto = new AdminUserBalanceDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
