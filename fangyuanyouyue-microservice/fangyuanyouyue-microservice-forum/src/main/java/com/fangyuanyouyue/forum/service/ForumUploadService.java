package com.fangyuanyouyue.forum.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.fangyuanyouyue.base.exception.ServiceException;

/**
 * 论坛帖子接口
 * @author wuzhimin
 *
 */
public interface ForumUploadService {
	
	/**
	 * 上传帖子文件
	 * @param filePath
	 * @return 
	 * @throws ServiceException
	 */
	String uploadForum(String filePath) throws ServiceException;

	/**
	 * 解压帖子文件
	 * @param file
	 * @param destDirPath
	 * @throws RuntimeException
	 * @throws Exception 
	 */
	String uploadZipFile(HttpServletRequest request,MultipartFile file) throws RuntimeException, Exception;
	
}
