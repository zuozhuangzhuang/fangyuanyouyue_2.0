package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.GoodsBargain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品议价表DTO
 */
public class BargainDto {
    private Integer goodsId;//商品id

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private String mainUrl;//商品主图

    private Integer bargainId;//商品议价信息ID

    private BigDecimal price;//出价钱

    private String reason;//议价理由

    private Integer status;//状态 1申请 2同意 3拒绝

    public BargainDto() {
    }

    public BargainDto(GoodsBargain goodsBargain) {
        this.bargainId = goodsBargain.getId();
        this.goodsId = goodsBargain.getGoodsId();
        this.price = goodsBargain.getPrice();
        this.reason = goodsBargain.getReason();
        this.status = goodsBargain.getStatus();
    }

    public static List<BargainDto> toDtoList(List<GoodsBargain> list) {
        if (list == null)
            return null;
        List<BargainDto> dtolist = new ArrayList<>();
        for (GoodsBargain model : list) {
            BargainDto dto = new BargainDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

    public Integer getBargainId() {
        return bargainId;
    }

    public void setBargainId(Integer bargainId) {
        this.bargainId = bargainId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }
}
