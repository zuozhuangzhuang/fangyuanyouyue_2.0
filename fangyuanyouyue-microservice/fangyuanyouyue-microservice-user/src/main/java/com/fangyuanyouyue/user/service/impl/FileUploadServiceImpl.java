package com.fangyuanyouyue.user.service.impl;

import java.util.Base64;
import java.util.UUID;

import com.fangyuanyouyue.base.util.WaterMarkUtils;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.service.UserInfoService;
import org.apache.tomcat.jni.File;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public String uploadPic(Integer userId,Integer type,MultipartFile file) throws Exception {

//        String watermark = base64Encode("小方圆@"+userInfo.getNickName());

        String fileUrl = null;
        String fileName = getFileName(file.getOriginalFilename());
        fileName = "pic" + fileName;
        fileUrl = uploadFile(file, fileUrl, fileName);

        if(type != null && (type == 2 || type == 3 || type == 4)){
            UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
            if(userInfo == null){
                throw new ServiceException("用户不存在！");
            }
            return WaterMarkUtils.getWaterMark(fileUrl,userInfo.getNickName());
        }else{
            return fileUrl;
        }
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
