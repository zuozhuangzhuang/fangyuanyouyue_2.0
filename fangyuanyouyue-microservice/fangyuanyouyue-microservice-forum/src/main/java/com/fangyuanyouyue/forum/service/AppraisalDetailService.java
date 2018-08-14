package com.fangyuanyouyue.forum.service;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;

/**
 * 全民鉴定接口
 * @author wuzhimin
 *
 */
public interface AppraisalDetailService {

 
    /**
     * 获取全民鉴定详情
     * @param id
     * @return
     * @throws ServiceException
     */
    AppraisalDetailDto getAppraisalDetail(Integer id) throws ServiceException;
    
    /**
     * 获取全民鉴定列表
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<AppraisalDetailDto> getAppraisalList(Integer start,Integer limit) throws ServiceException;

    /**
     * 发起鉴定
     * @param userId
     * @param bonus
     * @param title
     * @param content
     * @throws ServiceException
     */
    void addAppraisal(Integer userId, BigDecimal bonus,String title,String content) throws ServiceException;
}
