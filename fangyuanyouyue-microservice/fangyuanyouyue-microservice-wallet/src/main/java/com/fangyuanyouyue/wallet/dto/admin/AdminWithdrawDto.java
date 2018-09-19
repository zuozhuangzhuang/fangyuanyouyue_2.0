package com.fangyuanyouyue.wallet.dto.admin;

import com.fangyuanyouyue.wallet.model.PlatformFinanceDetail;
import com.fangyuanyouyue.wallet.model.UserWithdraw;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户提现信息Dto
 */
@Getter
@Setter
@ToString
public class AdminWithdrawDto {

    private Integer userId;//用户id

    private BigDecimal amount;//提现金额

    private Integer payType;//提现方式 1微信 2支付宝

    private Integer status;//申请状态 1申请中 2提现成功 3申请拒绝

    private String content;//审核详情

    private String account;//支付宝账号

    private String realName;//真实姓名

    private String nickName;//用户昵称

    public AdminWithdrawDto(UserWithdraw withdraw) {
        this.userId = withdraw.getUserId();
        this.amount = withdraw.getAmount();
        this.payType = withdraw.getPayType();
        this.status = withdraw.getStatus();
        this.content = withdraw.getContent();
        this.account = withdraw.getAccount();
        this.realName = withdraw.getRealName();
        this.nickName = withdraw.getNickName();
    }



    public static List<AdminWithdrawDto> toDtoList(List<UserWithdraw> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<AdminWithdrawDto> dtolist = new ArrayList<>();
        for (UserWithdraw model : list) {
            AdminWithdrawDto dto = new AdminWithdrawDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
