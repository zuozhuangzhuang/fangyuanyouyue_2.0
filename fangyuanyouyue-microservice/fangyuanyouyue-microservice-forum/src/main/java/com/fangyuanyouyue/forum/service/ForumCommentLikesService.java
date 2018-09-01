package com.fangyuanyouyue.forum.service;

import com.fangyuanyouyue.base.exception.ServiceException;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface ForumCommentLikesService {

    
    /**
     * 计算点赞数量
     * @param commentId
     * @return
     */
    Integer countLikes(Integer commentId);
    
    /**
     * 点赞/取消点赞
     * @param type
     * @param userId
     * @param commentId
     * @throws ServiceException
     */
    void saveLikes(Integer type,Integer userId,Integer commentId) throws ServiceException;

}
