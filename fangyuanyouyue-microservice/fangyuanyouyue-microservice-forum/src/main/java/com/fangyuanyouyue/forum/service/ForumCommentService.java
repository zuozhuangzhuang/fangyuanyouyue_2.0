package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumCommentDto;
import com.fangyuanyouyue.forum.dto.MyForumCommentDto;

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
    List<ForumCommentDto> getCommentList(Integer userId,Integer forumId,Integer start,Integer limit) throws ServiceException;
    
	/**
	 * 获取评论列表
	 * @param userId
	 * @param commentId
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumCommentDto> getCommentCommentList(Integer userId,Integer commentId,Integer start,Integer limit) throws ServiceException;
    
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
	ForumCommentDto saveComment(Integer userId,Integer forumId,String content,Integer commentId) throws ServiceException;

	/**
	 * 我的帖子、视频评论列表
	 * @param userId
	 * @param statr
	 * @param limit
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
    List<MyForumCommentDto> myComments(Integer userId, Integer statr, Integer limit, Integer type) throws ServiceException;

	/**
	 * 删除帖子/视频评论
	 * @param userId
	 * @param ids
	 * @throws ServiceException
	 */
    void deleteForumComment(Integer userId,Integer[] ids) throws ServiceException;

}
