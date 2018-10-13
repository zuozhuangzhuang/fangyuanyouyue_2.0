package com.fangyuanyouyue.goods.dto.adminDto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.dto.AppraisalUrlDto;
import com.fangyuanyouyue.goods.model.GoodsAppraisalDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 鉴定详情DTO
 */
@Getter
@Setter
@ToString
public class AdminAppraisalDetailDto {
    private Integer appraisalDetailId;//鉴定详情ID

    private Integer userId;//用户ID

    private Integer orderId;//鉴定订单id

    private Integer goodsId;//商品id

    private String opinion;//鉴定观点

    private Integer status;//状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示)

    private String title;//鉴定标题

    private BigDecimal price;//鉴定金额

    private String description;//描述

//    private String submitTime;//审核时间

    private String addTime;//添加时间

//    private String updateTime;//更新时间

    private Integer type;//鉴定类型 1卖家鉴定 2买家鉴定 3我要鉴定

    private List<AdminAppraisalUrlDto> appraisalUrlDtos;//鉴定图片表DTO数组

    //用户昵称
    private String nickName;

    public AdminAppraisalDetailDto() {
    }

    public AdminAppraisalDetailDto(GoodsAppraisalDetail goodsAppraisalDetail) {
        this.userId = goodsAppraisalDetail.getUserId();
        this.appraisalDetailId = goodsAppraisalDetail.getId();
        this.orderId = goodsAppraisalDetail.getOrderId();
        this.goodsId = goodsAppraisalDetail.getGoodsId();
        this.opinion = goodsAppraisalDetail.getOpinion();
        this.status = goodsAppraisalDetail.getStatus();
        this.title = goodsAppraisalDetail.getTitle();
        this.price = goodsAppraisalDetail.getPrice();
        this.description = goodsAppraisalDetail.getDescription();
//        if(goodsAppraisalDetail.getSubmitTime() != null){
//            this.submitTime = DateUtil.getFormatDate(goodsAppraisalDetail.getSubmitTime(), DateUtil.DATE_FORMT);
//        }
        this.addTime = DateUtil.getFormatDate(goodsAppraisalDetail.getAddTime(), DateUtil.DATE_FORMT);
//        this.updateTime = DateUtil.getFormatDate(goodsAppraisalDetail.getUpdateTime(), DateUtil.DATE_FORMT);
        this.type = goodsAppraisalDetail.getType();
        this.nickName = goodsAppraisalDetail.getNickName();
    }


    public static ArrayList<AdminAppraisalDetailDto> toDtoList(List<GoodsAppraisalDetail> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        ArrayList<AdminAppraisalDetailDto> dtolist = new ArrayList<>();
        for (GoodsAppraisalDetail model : list) {
            AdminAppraisalDetailDto dto = new AdminAppraisalDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
