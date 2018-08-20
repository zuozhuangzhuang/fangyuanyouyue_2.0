package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.base.exception.ServiceException;

/**
 * 举报接口
 */
public interface ReportService {
    /**
     * 举报
     * @param userId
     * @param businessId
     * @param reason
     * @param reason
     * @throws ServiceException
     */
    void report(Integer userId,Integer businessId,String reason,Integer type) throws ServiceException;
}
