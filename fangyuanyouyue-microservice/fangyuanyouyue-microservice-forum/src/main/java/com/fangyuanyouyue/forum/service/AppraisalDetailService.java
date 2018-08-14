package com.fangyuanyouyue.forum.service;

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

}
