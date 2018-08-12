package com.fangyuanyouyue.user.service.impl;

import java.util.UUID;

import org.apache.tomcat.jni.File;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.user.service.FileUploadService;

@Service(value = "uploadFileService")
@Transactional(rollbackFor=Exception.class)
public class FileUploadServiceImpl implements FileUploadService{
    @Value("${access_key_id}")
    private String accessKeyId;

    @Value("${access_key_secret}")
    private String accessKeySecret;

    @Value("${endpoint}")
    private String endpoint;

    @Value("${oss_path}")
    private String ossPath;

    @Value("${bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile file) throws ServiceException {
        String fileUrl = null;
        String fileName = getFileName(file.getOriginalFilename());
        fileName = "pic" + fileName;
        fileUrl = uploadFile(file, fileUrl, fileName);
        return fileUrl;
    }

    @Override
    public String uploadVideo(MultipartFile file) throws ServiceException {
        String fileUrl = null;
        String fileName = getFileName(file.getOriginalFilename());
        fileName = "video" + fileName;
        fileUrl = uploadFile(file, fileUrl, fileName);
        return fileUrl;
    }

    /**
     * 上传文件，获取地址
     * @param file
     * @param fileUrl
     * @param fileName
     * @return
     */
    private String uploadFile(MultipartFile file, String fileUrl, String fileName) {
        try{
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 上传文件流file
            ossClient.putObject(bucket, fileName, file.getInputStream());
            // 关闭client
            ossClient.shutdown();
            fileUrl = ossPath+fileName;
        }catch(Exception e){
            e.printStackTrace();
        }
        //TODO 视频截取图片
//        VideoUtil.fetchPic();
        return fileUrl;
    }

    /**
     * 获取文件名
     * @param fileName
     * @return
     */
    private String getFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        String suffix = fileName.substring(dotIndex);
        fileName = UUID.randomUUID()+suffix;
        String date = DateUtil.getCurrentDate("/yyyy/MM/dd/");
        return date + fileName;
    }


}
