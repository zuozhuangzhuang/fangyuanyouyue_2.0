package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.model.GoodsBargain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品议价表DTO
 */
@Getter
@Setter
@ToString
public class BargainDto {
    private Integer userId;//申请压价用户id

    private Integer goodsId;//商品id

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private Integer bargainId;//商品议价信息ID

    private BigDecimal price;//出价钱

    private String reason;//议价理由

    private Integer status;//状态 1申请 2同意 3拒绝 4取消

    private String addTime;//申请时间

    private String updateTime;//时间（根据status判断：2 成交时间、3 拒绝时间）

    private String leftTime;//时间（根据status判断：1 剩余时间）

    public BargainDto() {
    }

    public BargainDto(GoodsBargain goodsBargain) {
        this.userId = goodsBargain.getUserId();
        this.bargainId = goodsBargain.getId();
        this.goodsId = goodsBargain.getGoodsId();
        this.price = goodsBargain.getPrice();
        this.reason = goodsBargain.getReason();
        this.status = goodsBargain.getStatus();
        this.addTime = DateUtil.getFormatDate(goodsBargain.getAddTime(), DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(goodsBargain.getUpdateTime(), DateUtil.DATE_FORMT);
        this.leftTime = DateUtil.getTimeDifference(goodsBargain.getAddTime().getTime()+24*60*60*1000,System.currentTimeMillis());
    }

    public static List<BargainDto> toDtoList(List<GoodsBargain> list) {
        if (list == null) {
            return null;
        }
        List<BargainDto> dtolist = new ArrayList<>();
        for (GoodsBargain model : list) {
            BargainDto dto = new BargainDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
