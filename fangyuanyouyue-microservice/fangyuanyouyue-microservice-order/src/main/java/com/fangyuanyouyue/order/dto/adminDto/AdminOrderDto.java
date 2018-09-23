package com.fangyuanyouyue.order.dto.adminDto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.order.dto.OrderDetailDto;
import com.fangyuanyouyue.order.dto.OrderPayDto;
import com.fangyuanyouyue.order.dto.SellerDto;
import com.fangyuanyouyue.order.model.OrderInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单DTO
 */
@Getter
@Setter
@ToString
public class AdminOrderDto {
	
    //订单信息
    private Integer userId;//买家id
    
    private String nickName;//买家昵称

    private String headImgUrl;//买家头像

    private String phone;//买家电话
    
    private String seller;

    private Integer orderId;//订单ID

    private String orderNo;//订单号

    private BigDecimal totalAmount;//订单总额
    
    private String address;

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消

    //order_refund
    private Integer returnStatus;//退货状态 1申请退货 2退货成功 3拒绝退货

    private Integer sellerReturnStatus;//卖家是否同意退货状态 null正常  1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货

    //商品信息
    private List<AdminOrderDetailDto> orderDetailDtos;//订单商品列表
    
    private String orderDetail; //订单详情
    
    //订单支付信息 收货地址信息支付信息中
    private AdminOrderPayDto orderPayDto;//订单支付表

    private String addTime;//下单时间

    //卖家信息
    private List<AdminSellerDto> sellerDtos;//卖家列表

    private Integer isRefund;//是否已退货 1是 2否

    private Integer isEvaluation = 2;//是否已评价 1是 2否

    private String receiveTime;//收货时间
    
    private Integer totalCount;

    public AdminOrderDto() {
    }

    public AdminOrderDto(OrderInfo orderInfo) {
        this.userId = orderInfo.getUserId();
        this.nickName = orderInfo.getPhone()+"<br>"+orderInfo.getNickName();
        this.headImgUrl = orderInfo.getHeadImgUrl();
        this.phone = orderInfo.getPhone();
        this.orderId = orderInfo.getId();
        this.orderNo = orderInfo.getOrderNo();
        this.totalAmount = orderInfo.getAmount();
        this.status = orderInfo.getStatus();
        this.addTime = DateUtil.getFormatDate(orderInfo.getAddTime(), DateUtil.DATE_FORMT);
        this.isRefund = orderInfo.getIsRefund();
        if(orderInfo.getReceiveTime() != null){
            this.receiveTime = DateUtil.getFormatDate(orderInfo.getReceiveTime(), DateUtil.DATE_FORMT);
        }
    }

    public static ArrayList<AdminOrderDto> toDtoList(List<OrderInfo> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        ArrayList<AdminOrderDto> dtolist = new ArrayList<>();
        for (OrderInfo model : list) {
            AdminOrderDto dto = new AdminOrderDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
