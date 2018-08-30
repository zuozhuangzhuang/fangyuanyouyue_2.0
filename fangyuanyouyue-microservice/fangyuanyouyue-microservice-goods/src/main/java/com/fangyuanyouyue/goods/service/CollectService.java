package com.fangyuanyouyue.goods.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.GoodsDto;

public interface CollectService {
    /**
     * 收藏/关注或取消
     * @param userId
     * @param collectIds
     * @param collectType
     * @param type
     * @param status
     * @throws ServiceException
     */
    void collect(Integer userId,Integer[] collectIds,Integer collectType,Integer type,Integer status) throws ServiceException;

    /**
     * 我的收藏/关注列表 (商品、抢购)
     * @param userId
     * @param collectType
     * @param type
     * @param search
     * @return
     * @throws ServiceException
     */
    List<GoodsDto> collectList(Integer userId, Integer collectType, Integer type,Integer start,Integer limit,String search) throws ServiceException;
}
