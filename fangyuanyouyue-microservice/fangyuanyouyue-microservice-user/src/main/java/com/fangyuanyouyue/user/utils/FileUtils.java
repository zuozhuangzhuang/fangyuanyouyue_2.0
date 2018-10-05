package com.fangyuanyouyue.user.utils;

import com.aliyun.oss.OSSClient;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class FileUtils {
    //生成文件路径
    private static String path = "D:\\file\\";

    //文件路径+名称
    private static String filenameTemp;
    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName,String filecontent){
        Boolean bool = false;
        filenameTemp = path+fileName+".sql";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }else{
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    /**
     * 删除文件
     * @param fileName 文件名称
     * @return
     */
    public static boolean delFile(String fileName){
        Boolean bool = false;
        filenameTemp = path+fileName+".txt";
        File file  = new File(filenameTemp);
        try {
            if(file.exists()){
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bool;
    }

    /**
     * 转移旧图片
     * @param fileUrl 旧图片路径
     * @param fileName 文件名
     * @return
     */
    static String transferFile(String fileUrl, String fileName) {
        try{
            String ossPath="https://xiaofangyuan.oss-cn-shenzhen.aliyuncs.com/";
            String accessKeyId="LTAIpIueVqc3Cl2H";
            String accessKeySecret= "9oBv7Hs1K1te1FLoV80r65vkpRl5Ck";
            String endpoint="oss-cn-shenzhen.aliyuncs.com";
            String bucket="xiaofangyuan";

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

    /**
     * 获取文件名
     * @param fileName
     * @return
     */
    static String getFileName(){
        String fileName = UUID.randomUUID().toString();
        String date = DateUtil.getCurrentDate("/yyyy/MM/dd/");
        return date + fileName;
    }

    public static void main(String[] args) {
//        UUID uuid = UUID.randomUUID();
//        createFile(uuid+"myfile", "我的梦说别停留等待,就让光芒折射泪湿的瞳孔,映出心中最想拥有的彩虹,带我奔向那片有你的天空,因为你是我的梦 我的梦");
        System.out.println(getFileName());
    }


}
