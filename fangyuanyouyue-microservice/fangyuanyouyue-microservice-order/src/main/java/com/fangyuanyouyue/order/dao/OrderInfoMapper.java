package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.GoodsInfo;
import com.fangyuanyouyue.order.model.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    OrderInfo selectByPrimaryKeyDetail(Integer id);

    /**
     * 获取未拆单订单列表
     * @param userId
     * @param start
     * @param limit
     * @param status
     * @param search 商品名、描述包含此字段的订单
     * @return
     */
    List<OrderInfo> getListByUserIdStatus(@Param("userId")Integer userId, @Param("start") Integer start, @Param("limit")Integer limit, @Param("status")Integer status,@Param("search")String search);

    /**
     * 根据卖家获取已拆单订单列表
     * @param sellerId
     * @param start
     * @param limit
     * @param status
     * @param search 商品名、描述包含此字段的订单
     * @return
     */
    List<OrderInfo> getOrderBySellerId(@Param("sellerId")Integer sellerId, @Param("start") Integer start, @Param("limit")Integer limit, @Param("status")Integer status,@Param("search")String search);

    /**
     * 获取总订单的子订单
     * @param userId
     * @return
     */
    List<OrderInfo> selectChildOrderByOrderId(@Param("userId")Integer userId,@Param("mainOrderId")Integer mainOrderId);


    /**
     * 根据用户ID、订单ID获取订单信息
     * @param orderId
     * @param userId
     * @return
     */
    OrderInfo getOrderByUserIdOrderId(@Param("orderId")Integer orderId,@Param("userId")Integer userId);

    /**
     * 获取退货列表
     * @param userId
     * @param start
     * @param limit
     * @param type 1买家 2卖家
     * @return
     */
    List<OrderInfo> getRefundOrder(@Param("userId")Integer userId, @Param("start")Integer start, @Param("limit")Integer limit, @Param("type")Integer type);

    /**
     * 根据订单号获取订单
     * @param orderNo
     * @return
     */
    OrderInfo selectByOrderNo(@Param("orderNo")String orderNo);

    /**
     * 根据状态获取订单列表
     * @param status
     * @return
     */
    List<OrderInfo> selectByStatus(@Param("status")Integer status);

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
    List<OrderInfo> getOrderPage(@Param("start") Integer start,
                                 @Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,
                                 @Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);
}