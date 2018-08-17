package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumLikesDto;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface ForumLikesService {

	/**
	 * 获取点赞列表
	 * @param forumId
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumLikesDto> getLikesList(Integer forumId,Integer start,Integer limit) throws ServiceException;
    
    /**
     * 计算点赞数量
     * @param forumId
     * @return
     */
    Integer countLikes(Integer forumId);
    
    /**
     * 点赞/取消点赞
     * @param userId
     * @param forumId
     * @param type
     */
    void saveLikes(Integer type,Integer userId,Integer forumId) throws ServiceException;

}
