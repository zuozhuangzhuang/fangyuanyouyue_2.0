package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;

/**
 * 专栏接口
 * @author wuzhimin
 *
 */
public interface ForumColumnService {

	/**
	 * 获取全部专栏
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumColumnTypeDto> getColumnList(Integer start,Integer limit) throws ServiceException;

	/**
	 * 获取精选专栏
	 * @return
	 * @throws ServiceException
	 */
    List<ForumColumnDto> getChosenColumnList() throws ServiceException;

	/**
	 * 获取专栏分类列表
	 * @return
	 * @throws ServiceException
	 */
    List<ForumColumnTypeDto> getForumTypeList() throws ServiceException;

	/**
	 * 发布专栏
	 * @param userId
	 * @param typeId
	 * @param name
	 * @throws ServiceException
	 */
	void addColumn(Integer userId,Integer typeId,String name) throws ServiceException;
}
