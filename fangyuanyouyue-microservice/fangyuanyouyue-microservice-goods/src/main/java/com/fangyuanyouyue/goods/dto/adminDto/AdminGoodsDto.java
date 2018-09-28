package com.fangyuanyouyue.goods.dto.adminDto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.dto.*;
import com.fangyuanyouyue.goods.model.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台商品信息Dto
 */
@Getter
@Setter
@ToString
public class AdminGoodsDto {
    private Integer goodsId;//商品ID

    private String description;//商品详情

    private BigDecimal price;//商品价格

    private BigDecimal postage;//运费

    private Integer sort;//排序

    private String label;//标签

    private Integer status;//状态 1出售中 2已售出 3已下架（已结束） 5删除

    private BigDecimal floorPrice;//最低价

    private Integer intervalTime;//降价时间间隔(单位是秒)

    private BigDecimal markdown;//降价幅度

    private String lastIntervalTime;//最后一次降价时间

    private Integer authType=2;//官方认证状态 1已认证 2未认证

    private Integer isAppraisal=2;//是否官方鉴定 1已鉴定 2未鉴定

    private String addTime;//发布时间

    private String updateTime;//修改时间

    private List<GoodsImgDto> goodsImgDtos;//商品图片

    private String videoUrl;//视频路径

    private String videoImg;//视频截图路径

    private List<GoodsCorrelationDto> goodsCorrelations;//商品分类

    //GoodsComment
//    private List<GoodsCommentDto> goodsCommentDtos;//商品评论列表

    private Integer commentCount;//商品评论总数

    private Integer type;//类型 1普通商品 2抢购商品

    private Integer userId;//发布用户id

    private String nickName;//卖家昵称

    private String headImgUrl;//头像图片地址

    private String name;//商品名称

    private String userAddress;//店家地址

    private List<BargainDto> bargainDtos;//用户议价信息列表

    private List<GoodsIntervalHistoryDto> historyDtos;//抢购降价历史

    private Integer videoLength;//视频长度

    private BigDecimal startPrice;//初始价格

    public AdminGoodsDto() {
    }

    public AdminGoodsDto(GoodsInfo goodsInfo) {
        this.goodsId = goodsInfo.getId();
        this.description = goodsInfo.getDescription();
        this.price = goodsInfo.getPrice();
        this.postage = goodsInfo.getPostage();
        this.sort = goodsInfo.getSort();
        this.label = goodsInfo.getLabel();
        this.status = goodsInfo.getStatus();
        this.floorPrice = goodsInfo.getFloorPrice();
        this.intervalTime = goodsInfo.getIntervalTime();
        this.markdown = goodsInfo.getMarkdown();
        if(goodsInfo.getLastIntervalTime() != null){
            this.lastIntervalTime = DateUtil.getFormatDate(goodsInfo.getLastIntervalTime(),DateUtil.DATE_FORMT);
        }
        this.addTime = DateUtil.getFormatDate(goodsInfo.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(goodsInfo.getUpdateTime(),DateUtil.DATE_FORMT);
        this.type = goodsInfo.getType();
        this.userId = goodsInfo.getUserId();
        this.name = goodsInfo.getName();
        this.startPrice = goodsInfo.getStartPrice();
        this.isAppraisal = goodsInfo.getIsAppraisal();
    }


    public static ArrayList<AdminGoodsDto> toDtoList(List<GoodsInfo> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        ArrayList<AdminGoodsDto> dtolist = new ArrayList<>();
        for (GoodsInfo model : list) {
            AdminGoodsDto dto = new AdminGoodsDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
