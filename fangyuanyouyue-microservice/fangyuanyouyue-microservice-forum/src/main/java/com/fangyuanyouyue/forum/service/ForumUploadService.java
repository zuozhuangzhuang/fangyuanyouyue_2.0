package com.fangyuanyouyue.forum.service;

import com.fangyuanyouyue.base.exception.ServiceException;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface ForumUploadService {
	
	/**
	 * 解析帖子文件
	 * @param filePath
	 * @return 
	 * @throws ServiceException
	 */
	String uploadForum(String filePath) throws ServiceException;
	
}
