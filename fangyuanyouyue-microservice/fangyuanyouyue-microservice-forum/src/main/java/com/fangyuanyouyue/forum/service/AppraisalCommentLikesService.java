package com.fangyuanyouyue.forum.service;

import com.fangyuanyouyue.base.exception.ServiceException;

/**
 * 全民鉴定点赞接口
 * @author wuzhimin
 *
 */
public interface AppraisalCommentLikesService {

 
    /**
     * 获取评论点赞数量
     * @param commentId
     * @return
     * @throws ServiceException
     */
    Integer countCommentLikes(Integer commentId) throws ServiceException;
    
    /**
     * 保存点赞
     * @param userId
     * @param commentId
     * @param type
     * @throws ServiceException
     */
    void saveLikes(Integer userId,Integer commentId,Integer type) throws ServiceException;

}
