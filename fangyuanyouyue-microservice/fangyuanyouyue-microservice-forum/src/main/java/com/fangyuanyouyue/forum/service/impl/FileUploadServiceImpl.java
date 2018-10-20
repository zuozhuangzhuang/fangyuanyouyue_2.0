package com.fangyuanyouyue.forum.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.oss.OSSClient;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.service.FileUploadService;

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
    public String uploadPic(String name,InputStream file) throws Exception {

//        String watermark = base64Encode("小方圆@"+userInfo.getNickName());

        String fileUrl = null;
        String fileName = getFileName(name);
        fileName = "pic" + fileName;
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
    public String uploadFile(InputStream file, String fileUrl, String fileName) {
        try{
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 上传文件流file
            ossClient.putObject(bucket, fileName, file);
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
