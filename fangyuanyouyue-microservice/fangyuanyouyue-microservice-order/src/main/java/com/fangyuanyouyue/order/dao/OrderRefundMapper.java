package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.OrderInfo;
import com.fangyuanyouyue.order.model.OrderRefund;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderRefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefund record);

    int insertSelective(OrderRefund record);

    OrderRefund selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefund record);

    int updateByPrimaryKey(OrderRefund record);

    /**
     * 根据订单ID和状态获取退货信息
     * @param orderId
     * @param status 状态 1申请退货 2退货成功 3拒绝退货
     * @param sellerReturnStatus 卖家是否同意退货状态 1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货
     * @return
     */
    OrderRefund selectByOrderIdStatus(@Param("orderId")Integer orderId, @Param("status")Integer status,@Param("sellerReturnStatus")Integer sellerReturnStatus);
    /**
     * 分页总条数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    Integer countPage(@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 分页获取订单
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<OrderRefund> getPage(@Param("start") Integer start,
                                 @Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,
                                 @Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);
}