package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.AppraisalCommentDto;
import com.fangyuanyouyue.forum.param.AppraisalParam;

/**
 * 全民鉴定评论接口
 * @author wuzhimin
 *
 */
public interface AppraisalCommentService {

 
    /**
     * 获取评论数量
     * @param id
     * @return
     * @throws ServiceException
     */
    Integer countComment(Integer id) throws ServiceException;
    
    /**
     * 获取全民鉴定的评论
     * @param appraisalId
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<AppraisalCommentDto> getAppraisalCommentList(Integer appraisalId,Integer start,Integer limit) throws ServiceException;
    
    /**
     * 保存评论
     * @param param
     */
    void saveComment(Integer userId,AppraisalParam param);

}
