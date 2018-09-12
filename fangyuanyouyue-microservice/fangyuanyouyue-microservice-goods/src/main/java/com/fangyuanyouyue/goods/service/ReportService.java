package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;

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

    /**
     * 后台处理举报
     * @param id
     * @param content
     * @param status
     * @throws ServiceException
     */
    void dealReport(Integer id,String content,Integer status) throws ServiceException;

    /**
     * 获取举报商品列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager getReportPage(AdminGoodsParam param) throws ServiceException;

}
