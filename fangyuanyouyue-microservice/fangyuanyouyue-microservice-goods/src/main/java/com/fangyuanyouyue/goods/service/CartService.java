package com.fangyuanyouyue.goods.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.CartShopDto;
import com.fangyuanyouyue.goods.dto.GoodsDto;

public interface CartService {

    /**
     * 添加商品到购物车
     * @param userId
     * @param goodsId
     * @throws ServiceException
     */
    void addGoodsToCart(Integer userId,Integer goodsId) throws ServiceException;

    /**
     * 我的购物车
     * @param userId
     * @return
     * @throws ServiceException
     */
    List<CartShopDto> getCart(Integer userId) throws ServiceException;

    /**
     * 移出购物车
     * @param cartDetailIds
     * @throws ServiceException
     */
    void cartRemove(Integer[] cartDetailIds) throws ServiceException;

    /**
     *
     * @return
     * @throws ServiceException
     */
    List<GoodsDto> choice(Integer userId) throws ServiceException;
}
