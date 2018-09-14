package com.fangyuanyouyue.user.service;

import org.springframework.web.multipart.MultipartFile;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface FileUploadService {
    /**
     * 上传图片文件，获取下载url
     * @param file
     * @return
     * @throws ServiceException
     */
    String uploadPic(Integer userId,Integer type,MultipartFile file) throws Exception;

    /**
     * 上传视频文件
     * @param file
     * @return
     * @throws ServiceException
     */
    String uploadVideo(MultipartFile file) throws ServiceException;
}
