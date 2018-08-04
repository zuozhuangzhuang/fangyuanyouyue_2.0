package com.fangyuanyouyue.goods.service;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.BargainDto;
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
    void addBargain(Integer userId, Integer goodsId, BigDecimal price,String reason,Integer addressId) throws ServiceException;

    /**
     * 处理压价
     * @param userId
     * @param goodsId
     * @throws ServiceException
     */
    void updateBargain(Integer userId, Integer goodsId,Integer bargainId,Integer status) throws ServiceException;

    /**
     * 我的压价列表
     * @param userId
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<BargainDto> bargainList(Integer userId,Integer start,Integer limit) throws ServiceException;
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
