package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.GoodsAppraisalDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 鉴定详情DTO
 */
@Getter
@Setter
@ToString
public class AppraisalDetailDto {
    private Integer appraisalDetailId;//鉴定详情ID

    private Integer userId;//用户ID

//    private Integer orderId;//鉴定订单id

//    private Integer goodsId;//商品id

    private String opinion;//鉴定观点

    private Integer status;//状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示)

//    private BigDecimal price;//鉴定金额

//    private String description;//描述

//    private String submitTime;//审核时间

//    private String addTime;//添加时间

//    private String updateTime;//更新时间

//    private Integer type;//鉴定类型 1商家鉴定 2买家 3普通用户

    private List<AppraisalUrlDto> appraisalUrlDtos;//鉴定图片表DTO数组

    public AppraisalDetailDto() {
    }

    public AppraisalDetailDto(GoodsAppraisalDetail goodsAppraisalDetail) {
        this.userId = goodsAppraisalDetail.getUserId();
        this.appraisalDetailId = goodsAppraisalDetail.getId();
//        this.orderId = goodsAppraisalDetail.getOrderId();
//        this.goodsId = goodsAppraisalDetail.getGoodsId();
        this.opinion = goodsAppraisalDetail.getOpinion();
        this.status = goodsAppraisalDetail.getStatus();
//        this.price = goodsAppraisalDetail.getPrice();
//        this.description = goodsAppraisalDetail.getDescription();
//        if(goodsAppraisalDetail.getSubmitTime() != null){
//            this.submitTime = DateUtil.getFormatDate(goodsAppraisalDetail.getSubmitTime(), DateUtil.DATE_FORMT);
//        }
//        this.addTime = DateUtil.getFormatDate(goodsAppraisalDetail.getAddTime(), DateUtil.DATE_FORMT);
//        this.updateTime = DateUtil.getFormatDate(goodsAppraisalDetail.getUpdateTime(), DateUtil.DATE_FORMT);
//        this.type = goodsAppraisalDetail.getType();
    }


    public static ArrayList<AppraisalDetailDto> toDtoList(List<GoodsAppraisalDetail> list) {
        if (list == null) {
            return null;
        }
        ArrayList<AppraisalDetailDto> dtolist = new ArrayList<>();
        for (GoodsAppraisalDetail model : list) {
            AppraisalDetailDto dto = new AppraisalDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
