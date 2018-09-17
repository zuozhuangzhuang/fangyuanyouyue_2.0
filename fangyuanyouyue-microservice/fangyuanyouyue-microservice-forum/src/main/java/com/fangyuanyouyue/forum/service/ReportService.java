package com.fangyuanyouyue.forum.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.param.AdminForumParam;

public interface ReportService {

    /**
     * 获取举报帖子、视频列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager getForumReportPage(AdminForumParam param) throws ServiceException;

    /**
     * 获取举报全民鉴定列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager getAppraisalReportPage(AdminForumParam param) throws ServiceException;

    /**
     * 后台处理举报
     * @param id
     * @param content
     * @throws ServiceException
     */
    void dealReport(Integer id,String content) throws ServiceException;
}
