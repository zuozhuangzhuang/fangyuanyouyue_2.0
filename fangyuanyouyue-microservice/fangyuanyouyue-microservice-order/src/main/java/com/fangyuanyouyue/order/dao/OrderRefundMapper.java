package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.OrderRefund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}