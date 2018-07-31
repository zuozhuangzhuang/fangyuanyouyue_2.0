package com.fangyuanyouyue.forum.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;

/**
 * 专栏接口
 * @author wuzhimin
 *
 */
public interface ForumColumnService {

	/**
	 * 获取专栏列表
	 * @param forumId
	 * @param start
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
    List<ForumColumnDto> getColumnList(Integer start,Integer limit) throws ServiceException;
 

}
