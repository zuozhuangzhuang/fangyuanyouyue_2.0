package com.fangyuanyouyue.user.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

import com.fangyuanyouyue.base.util.HttpUtil;
import com.fangyuanyouyue.base.util.WaterMarkUtils;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.service.UserInfoService;
import com.fangyuanyouyue.user.utils.HttpUtils;
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
    @Override
    public String uploadFile(MultipartFile file, String fileUrl, String fileName) {
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
    @Override
    public String getFileName(String fileName) throws ServiceException{
        int dotIndex = fileName.lastIndexOf(".");
        String suffix = fileName.substring(dotIndex);
        fileName = UUID.randomUUID()+suffix;
        String date = DateUtil.getCurrentDate("/yyyy/MM/dd/");
        return date + fileName;
    }

    /**
     * 转移旧图片
     * @param fileUrl 旧图片路径
     * @param fileName 文件名
     * @return
     */
     String transferFile(String fileUrl, String fileName) {
        try{
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 上传文件流file
            ossClient.putObject(bucket, fileName, getInputStream(fileUrl));
            // 关闭client
            ossClient.shutdown();
            fileUrl = ossPath+fileName;
        }catch(Exception e){
            e.printStackTrace();
        }
        return fileUrl;
    }

    /**
     * GET请求获取输入流
     * @param imgUrl
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(String imgUrl) throws IOException {
        InputStream inputStream=null;
        HttpURLConnection httpurlconn=null;
        try {
            URL url=new URL(imgUrl);
            if(url!=null) {
                httpurlconn=(HttpURLConnection) url.openConnection();
                //设置连接超时时间
                httpurlconn.setConnectTimeout(3000);
                //表示使用GET方式请求
                httpurlconn.setRequestMethod("GET");
                int responsecode=httpurlconn.getResponseCode();
                if(responsecode==200)
                {
                    //从服务返回一个输入流
                    inputStream=httpurlconn.getInputStream();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static void main(String[] args) throws ServiceException {
        FileUploadServiceImpl fileUploadService = new FileUploadServiceImpl();
        String fileName = fileUploadService.getFileName("001.jpg");
        String newUrl = fileUploadService.transferFile("http://app.fangyuanyouyue.com/static/pic/default/001.jpg", "pic"+fileName);
        System.out.println(newUrl);
    }
}
