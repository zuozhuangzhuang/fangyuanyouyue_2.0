package com.fangyuanyouyue.forum.service;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface ForumCommentLikesService {

    
    /**
     * 计算点赞数量
     * @param forumId
     * @return
     */
    Integer countLikes(Integer commentId);
    
    /**
     * 点赞/取消点赞
     * @param userId
     * @param forumId
     * @param type
     */
    void saveLikes(Integer type,Integer userId,Integer commentId);

}
