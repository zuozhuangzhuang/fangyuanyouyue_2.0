package com.fangyuanyouyue.base.enums;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信小程序模板消息id
 */
public enum MiniMsg {
    SYSTEM_MSG(1,"审核结果通知","9PCKkgtnPg0vL_uyz7L0Fr8OvXDJz-amOjG5PCCZg-A"),
    GOODS_APPRAISAL_END(2,"鉴定结果通知","uqQqr8f4nV7Miu7MgxW6h3H-wa7MBUyBcpujT_rw8B4"),
    GET_BENEFIT(3,"收益到账通知","YynYJ7m0S-NDPu9UGDKDkGcIH2PLhK46W8a383lhTNA"),
    WITHDRAW_SUCCESS(4,"提现到账通知","-cbrzZCURwlmrlZ4m2oncE2LMOeDQAr05Zqs2Tnkdik"),
    WITHDRAW_FAILED(5,"提现失败通知","hjaNRgn8GIqIx9eTber_iLAfE6vcR72bRMjAeikqtj8"),
    ORDER_ADD(6,"商品被购买通知","7RtiYELicy756YYG5bl2-w_KAau9pgb0-TGuU9EGKEk"),
    ORDER_REFUND(7,"退货申请通知","ALEi-Kpw_G8SWKAjde_SV7fsUmY_OfdhRJG17BHdhTA"),
    REFUND_SUCCESS(8,"退款成功通知","0ewa6a6F3A9IQVt4wJwFNv1FEMSk__wRolJJqzUDsGw"),
    REFUND_FAILED(9,"退款失败通知","IGQUFPamaa3grODQCT6uvTxlq7eWzP_Cq2BeLpfeDNk"),
    ;

    private Integer code;//消息编号
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
