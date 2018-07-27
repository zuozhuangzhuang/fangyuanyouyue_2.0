package com.fangyuanyouyue.goods.service;

import java.math.BigDecimal;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.model.GoodsBargain;

public interface BargainService {
    /**
     * （批量）商品压价
     * @param userId
     * @param goodsId
     * @param price
     * @param reason
     * @throws ServiceException
     */
    void addBargain(Integer userId, Integer goodsId, BigDecimal price,String reason) throws ServiceException;

    /**
     * 处理压价
     * @param userId
     * @param goodsId
     * @throws ServiceException
     */
    void updateBargain(Integer userId, Integer goodsId,Integer bargainId,Integer status) throws ServiceException;

    /**
     * 压价详情
     * @param userId
     * @param bargainId
     * @param goodsId
     * @return
     * @throws ServiceException
     */
    GoodsBargain bargainDetail(Integer userId,Integer bargainId, Integer goodsId) throws ServiceException;
}
