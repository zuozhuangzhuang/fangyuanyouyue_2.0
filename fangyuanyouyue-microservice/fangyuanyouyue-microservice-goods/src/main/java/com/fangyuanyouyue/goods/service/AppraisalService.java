package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.AppraisalOrderInfoDto;

/**
 * 官方鉴定
 */
public interface AppraisalService {

    /**
     * 申请鉴定
     * @param userId
     * @param goodsIds
     * @param title
     * @param description
     * @param imgUrl
     * @param videoUrl
     * @return
     * @throws ServiceException
     */
    AppraisalOrderInfoDto addAppraisal(Integer userId, Integer[] goodsIds, String title, String description, String imgUrl, String videoUrl) throws ServiceException;

    /**
     * 取消鉴定，删除鉴定订单
     * @param userId
     * @param orderId
     * @throws ServiceException
     */
    void cancelAppraisal(Integer userId,Integer orderId) throws ServiceException;

}
