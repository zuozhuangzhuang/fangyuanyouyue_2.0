package com.fangyuanyouyue.forum.service;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface ForumPvService {

    
    /**
     * 计算点赞数量
     * @param forumId
     * @return
     */
    Integer countPv(Integer forumId);
    
    /**
     * 
     * @param userId
     * @param forumId
     * @param type
     */
    void savePv(Integer userId,Integer forumId);

}
