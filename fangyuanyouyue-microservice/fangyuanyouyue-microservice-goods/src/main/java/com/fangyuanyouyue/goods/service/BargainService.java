package com.fangyuanyouyue.goods.service;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.BargainDto;
import com.fangyuanyouyue.goods.dto.GoodsDto;
import com.fangyuanyouyue.goods.model.GoodsBargain;

public interface BargainService {
    /**
     * 商品压价
     * @param userId
     * @param goodsId
     * @param price
     * @param reason
     * @param payPwd
     * @param payType
     * @throws ServiceException
     */
    Object addBargain(Integer userId, Integer goodsId, BigDecimal price,String reason,Integer addressId,String payPwd,Integer payType) throws ServiceException;


    /**
     * 处理压价
     * @param userId
     * @param goodsId
     * @param bargainId
     * @param status 状态 2同意 3拒绝 4取消
     * @return
     * @throws ServiceException
     */
    Integer updateBargain(Integer userId, Integer goodsId,Integer bargainId,Integer status) throws ServiceException;

    /**
     * 我的压价列表
     * @param userId
     * @param start
     * @param limit
     * @param search
     * @return
     * @throws ServiceException
     */
    List<GoodsDto> bargainList(Integer userId, Integer start, Integer limit,String search) throws ServiceException;

    /**
     * 压价详情
     * @param userId
     * @param bargainId
     * @param goodsId
     * @return
     * @throws ServiceException
     */
    GoodsBargain bargainDetail(Integer userId,Integer bargainId, Integer goodsId) throws ServiceException;


    /**
     * 获取统计信息
     * @param userId
     * @return
     * @throws ServiceException
     */
    Integer getProcess(Integer userId) throws ServiceException;

    /**
     * 删除我的议价
     * @param userId
     * @param goodsIds
     * @return
     * @throws ServiceException
     */
    void deleteBargain(Integer userId,Integer[] goodsIds) throws ServiceException;

    /**
     * 修改议价订单状态
     * @param orderNo
     * @param thridOrderNo
     * @param payType
     * @return
     * @throws ServiceException
     */
    boolean updateOrder(String orderNo, String thridOrderNo, Integer payType) throws ServiceException;
}
