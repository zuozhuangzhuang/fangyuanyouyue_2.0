package com.fangyuanyouyue.base.enums;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信小程序模板消息id
 */
public enum MiniMsg {
    ORDER_REFUND(1,"退货申请通知","ALEi-Kpw_G8SWKAjde_SV7fsUmY_OfdhRJG17BHdhTA"),
    ORDER_CANCEL(2,"订单取消通知","wHV3uNVKTgIBcKeLXe5NMT_0gmdwQcQkyVcUzsLyiO0"),
    WITHDRAW_APPLY(3,"提现申请通知","wlyA_t59AjXVETSgIwR1pUnKOKqj-MChsaRenkvXUTU"),
    //提现到帐通知
    WITHDRAW_FAILED(5,"提现失败通知","hjaNRgn8GIqIx9eTber_iDkJE89RL2hj7z3hyPLweqA"),
    FORUM_COMMENT(6,"帖子被评论通知","38vDkQ7D2bPTyPe2tBrsdTm7JHKO-VlIKpVPuvQVRnE","123"),
    ORDER_ADD(7,"商品被购买通知","7RtiYELicy756YYG5bl2-3gnXyi5Z_1bet1AKxIq7B0"),
    GOODS_APPRAISAL_APPLY(8,"鉴定已接单提醒","FytcDjdVUps19nb2cp5Wj3Q27L8eupsMutaDgzq8OIA"),
    GOODS_APPRAISAL_END(9,"鉴定完成通知","vOEK_BEEsojqBe6GXhjhIhh3vMbr7YdiRhxvfQ9c0Vw"),
    USER_EXT_APPLY(10,"实名认证审核通知","qJqCQLIl3HHoBYlXeKHzlIfWiqhA3zPaU1i11CSU-p0"),
    USER_EXT_END(11,"实名认证通知","mZ0SYZ7Q_PAswqY2KNR3hBGO3BZ6rfwC0B5YcMdcjIo"),
    USER_VIP_CHANGE(12,"会员等级变更通知","k706o2xOjMOAnLxmdCABIvWf17pM5ZZLC9J8cFP9oaI"),
    USER_VIP_APPLY(13,"会员卡开通成功通知","JZ1z_TTRpv-AfH6sOFUmqvCVo9wcF7aCayF2ZGYDmR8"),
    SYSTEM_MSG(14,"审核通知","IvdVZ1Elfp1R5U7huyw1yfEE3_THWd7EZLz9mj90a0U"),
    REFUND_MSG(15,"退款通知","8WxCpfxPAoJFRu3-Wf8lk_SloyiGmQx4Nb2s8IxzqDU"),
    ;

    private Integer code;//消息
    private String message;//消息内容
    private String templateId;//模板消息id
    private String pagePath;//页面路径

    MiniMsg() {
    }

    MiniMsg(Integer code, String message, String templateId) {
        this.code = code;
        this.message = message;
        this.templateId = templateId;
    }

    MiniMsg(Integer code, String message, String templateId, String pagePath) {
        this.code = code;
        this.message = message;
        this.templateId = templateId;
        this.pagePath = pagePath;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }
}
