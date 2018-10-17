package com.fangyuanyouyue.forum.service;

import java.io.File;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface FileUploadService {
    /**
     * 上传图片文件，获取下载url
     * @param file
     * @return
     * @throws ServiceException
     */
    String uploadPic(File file) throws Exception;


    /**
     * 上传文件，获取地址
     * @param file
     * @param fileUrl
     * @param fileName
     * @return
     * @throws ServiceException
     */
    String uploadFile(File file, String fileUrl, String fileName) throws ServiceException;

    /**
     * 获取文件名
     * @param fileName
     * @return
     * @throws ServiceException
     */
    String getFileName(String fileName) throws ServiceException;
}
