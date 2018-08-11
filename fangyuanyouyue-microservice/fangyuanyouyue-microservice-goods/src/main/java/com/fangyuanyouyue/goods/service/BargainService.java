package com.fangyuanyouyue.goods.service;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.BargainDto;
import com.fangyuanyouyue.goods.dto.GoodsDto;
import com.fangyuanyouyue.goods.model.GoodsBargain;

public interface BargainService {
    /**
     * （批量）商品压价
     * @param userId
     * @param goodsId
     * @param price
     * @param reason
     * @param payPwd
     * @throws ServiceException
     */
    void addBargain(Integer userId, Integer goodsId, BigDecimal price,String reason,Integer addressId,String payPwd) throws ServiceException;

    /**
     * 处理压价
     * @param userId
     * @param goodsId
     * @throws ServiceException
     */
    Integer updateBargain(Integer userId, Integer goodsId,Integer bargainId,Integer status) throws ServiceException;

    /**
     * 我的压价列表
     * @param userId
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<GoodsDto> bargainList(Integer userId, Integer start, Integer limit) throws ServiceException;

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
}
