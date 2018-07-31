package com.fangyuanyouyue.goods.service;

import java.math.BigDecimal;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface AppraisalService {
    /**
     * 申请鉴定
     * @param userId
     * @param goodsIds
//     * @param type
     * @param title
     * @param description
     * @param price
     * @throws ServiceException
     */
    void addAppraisal(Integer userId, Integer[] goodsIds,  String title, String description, BigDecimal price,String imgUrl) throws ServiceException;
}
