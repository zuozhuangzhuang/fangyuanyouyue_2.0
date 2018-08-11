package com.fangyuanyouyue.wallet.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.PointGoodsDto;

public interface PointGoodsService {
    /**
     * 获取积分兑换商品
     * @return
     * @throws ServiceException
     */
    List<PointGoodsDto> getPointGoods(Integer start,Integer limit) throws ServiceException;

}
