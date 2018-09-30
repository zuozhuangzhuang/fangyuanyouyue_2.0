package com.fangyuanyouyue.forum.service;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.Pager;
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
     * @param userId
     * @param appraisalId
     * @return
     * @throws ServiceException
     */
    AppraisalDetailDto getAppraisalDetail(Integer userId,Integer appraisalId) throws ServiceException;
    
    /**
     * 获取全民鉴定列表
     * @param userId
     * @param keyword
     * @param start
     * @param limit
     * @param type
     * @return
     * @throws ServiceException
     */
    List<AppraisalDetailDto> getAppraisalList(Integer userId,String keyword,Integer start,Integer limit,Integer type) throws ServiceException;

    /**
     * 发起鉴定
     * @param userId
     * @param bonus
     * @param title
     * @param content
     * @param imgUrls
     * @param userIds
     * @param payType
     * @param payPwd
     * @throws ServiceException
     */
    Object addAppraisal(Integer userId, BigDecimal bonus,String title,String content,String[] imgUrls,Integer[] userIds,Integer payType,String payPwd) throws ServiceException;

    /**
     * 鉴定内邀请好友
     * @param userId
     * @param appraisalId
     * @param userIds
     * @throws ServiceException
     */
    void invite(Integer userId,Integer appraisalId,Integer[] userIds) throws ServiceException;

    /**
     * 根据订单号继续发布鉴定
     * @param orderNo
     * @param thirdOrderNo
     * @param payType
     * @return
     * @throws ServiceException
     */
    boolean applyAppraisal(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException;
    
    /**
     * 获取后台分页数据
     * @param param
     * @return
     */
    Pager getPage(BasePageReq param);

    /**
     * 删除全民鉴定
     * @param userId
     * @param ids
     * @throws ServiceException
     */
    void deleteAppraisal(Integer userId,Integer[] ids) throws ServiceException;
}
