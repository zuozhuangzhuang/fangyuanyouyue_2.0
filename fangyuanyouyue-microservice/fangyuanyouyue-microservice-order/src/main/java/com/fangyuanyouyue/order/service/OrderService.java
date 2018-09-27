package com.fangyuanyouyue.order.service;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.dto.CompanyDto;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderDto;
import com.fangyuanyouyue.order.param.AdminOrderParam;

public interface OrderService {
    /**
     * 购物车商品下单
     * @param token
     * @param sellerList
     * @param userId
     * @param addressId
     * @return
     * @throws ServiceException
     */
    OrderDto saveOrderByCart(String token,String sellerList,Integer userId,Integer addressId) throws ServiceException;

    /**
     * 取消订单
     * @param userId
     * @param orderId
     * @throws ServiceException
     */
    void cancelOrder(Integer userId,Integer orderId) throws ServiceException;

    /**
     * 订单详情
     * @param userId
     * @param orderId
     * @return
     * @throws ServiceException
     */
    OrderDto orderDetail(Integer userId,Integer orderId) throws ServiceException;

    /**
     * 我的订单列表
     * @param userId
     * @param start
     * @param limit
     * @param type
     * @param status
     * @param search
     * @return
     * @throws ServiceException
     */
    List<OrderDto> myOrderList(Integer userId, Integer start,Integer limit,Integer type,Integer status,String search) throws ServiceException;

    /**
     * 商品/抢购直接下单
     * @param token
     * @param goodsId
     * @param couponId
     * @param userId
     * @param addressId
     * @param type
     * @return
     * @throws ServiceException
     */
    OrderDto saveOrder(String token,Integer goodsId,Integer couponId,Integer userId,Integer addressId,Integer type) throws ServiceException;

    /**
     * 订单支付
     * @param userId
     * @param orderId
     * @param payType
     * @param payPwd
     * @return
     * @throws ServiceException
     */
    Object getOrderPay(Integer userId,Integer orderId,Integer payType,String payPwd) throws ServiceException;

    /**
     * 获取统计数据
     * @param userId
     * @param type
     * @return
     * @throws ServiceException
     */
    Integer getProcess(Integer userId,Integer type) throws ServiceException;

    /**
     * 卖家确认发货
     * @param userId
     * @param orderId
     * @param companyId
     * @param number
     * @throws ServiceException
     */
    void sendGoods(Integer userId,Integer orderId,Integer companyId,String number) throws ServiceException;

    /**
     * 买家确认收货
     * @param userId
     * @param orderId
     * @throws ServiceException
     */
    void getGoods(Integer userId,Integer orderId) throws ServiceException;

    /**
     * 物流公司列表
     * @return
     * @throws ServiceException
     */
    List<CompanyDto> companyList() throws ServiceException;


    /**
     * 批量删除订单
     * @param userId
     * @param orderIds
     * @throws ServiceException
     */
    void deleteOrder(Integer userId,Integer[] orderIds) throws ServiceException;

    /**
     * 评价卖家
     * @param userId
     * @param orderId
     * @param goodsQuality
     * @param serviceAttitude
     * @throws ServiceException
     */
    void evaluationOrder(Integer userId,Integer orderId,Integer goodsQuality,Integer serviceAttitude) throws ServiceException;

    /**
     * 提醒发货
     * @param userId
     * @param orderId
     * @throws ServiceException
     */
    void reminder(Integer userId,Integer orderId) throws ServiceException;

    /**
     * 修改订单状态
     * @param orderNo
     * @param thirdOrderNo
     * @param payType
     * @return
     * @throws ServiceException
     */
    boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException;

    /**
     * 查看所有用户订单
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager orderList(AdminOrderParam param) throws ServiceException;

    /**
     * 物流公司管理
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager companyList(AdminOrderParam param) throws ServiceException;

    /**
     * 增加物流公司
     * @param number
     * @param name
     * @param price
     * @throws ServiceException
     */
    void addCompany(String number, String name, BigDecimal price) throws ServiceException;

    /**
     * 修改物流公司信息
     * @param id
     * @param number
     * @param name
     * @param price
     * @param status
     * @throws ServiceException
     */
    void updateCompany(Integer id,String number, String name, BigDecimal price,Integer status) throws ServiceException;

    /**
     * 获取简单订单列表
     * @param param
     * @return
     * @throws ServiceException
     */
	Pager simpleOrderList(AdminOrderParam param) throws ServiceException;

    /**
     * 后台获取订单详情
     * @param id
     * @return
     * @throws ServiceException
     */
	AdminOrderDto adminOrderDetail(Integer id) throws ServiceException;
}
