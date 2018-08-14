package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface ForumInfoService {
	
	/**
	 * 根据主键获取帖子详情
	 * @param id
	 * @return
	 */
	ForumInfoDto getForumInfoById(Integer id) throws ServiceException;

	/**
	 * 获取帖子列表
	 * @param columnId
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumInfoDto> getForumList(Integer columnId,Integer userId, Integer type, String keyword,Integer start,Integer limit) throws ServiceException;

	/**
	 * 发布帖子/视频
	 * @param userId
	 * @param columnId
	 * @param title
	 * @param content
	 * @param videoUrl
	 * @param videoLength
	 * @param videoImg
	 * @param type
	 * @param userIds
	 * @throws ServiceException
	 */
    void addForum(Integer userId,Integer columnId,String title,String content,String videoUrl,Integer videoLength,String videoImg,Integer type,Integer[] userIds) throws ServiceException;
}
