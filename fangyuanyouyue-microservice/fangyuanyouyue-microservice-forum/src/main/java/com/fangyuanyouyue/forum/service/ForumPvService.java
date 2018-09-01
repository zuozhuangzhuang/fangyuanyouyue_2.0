package com.fangyuanyouyue.forum.service;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface ForumPvService {

    
    /**
     * 计算浏览量
     * @param forumId
     * @return
     */
    Integer countPv(Integer forumId);
    
    /**
     * 增加浏览量
     * @param userId
     * @param forumId
     * @param columnId
     */
    void savePv(Integer userId,Integer forumId,Integer columnId);

}
