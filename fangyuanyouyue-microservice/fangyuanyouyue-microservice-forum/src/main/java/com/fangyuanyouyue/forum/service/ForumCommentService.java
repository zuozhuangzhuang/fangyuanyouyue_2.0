package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumCommentDto;

/**
 * 帖子评论接口
 * @author wuzhimin
 *
 */
public interface ForumCommentService {

	/**
	 * 获取评论列表
	 * @param forumId
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumCommentDto> getCommentList(Integer forumId,Integer start,Integer limit) throws ServiceException;
    
	/**
	 * 获取评论列表
	 * @param forumId
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumCommentDto> getCommentCommentList(Integer commentId,Integer start,Integer limit) throws ServiceException;
    
    /**
     * 计算评论数量
     * @param forumId
     * @return
     */
    Integer countComment(Integer forumId);
    
    /**
     * 	发表评论
     * @param userId
     * @param forumId
     * @param content
     * @param commentId
     */
    void saveComment(Integer userId,Integer forumId,String content,Integer commentId) throws ServiceException;
    
    /**
     * 删除评论
     * @param commentId
     */
    void deleteComment(Integer commentId) throws ServiceException;

}
