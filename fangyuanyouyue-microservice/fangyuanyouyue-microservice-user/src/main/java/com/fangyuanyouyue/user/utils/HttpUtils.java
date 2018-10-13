package com.fangyuanyouyue.user.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Savepoint;
import java.text.DateFormat;
import java.util.Date;

public class HttpUtils {
    private static String URL_PATH="http://app.fangyuanyouyue.com/static/pic/default/001.jpg";


    public HttpUtils() {
    }

    public static void saveImagestodisk() throws IOException{
        InputStream inputStream=getInputStream(URL_PATH);
        byte[] data=new byte[1024];
        int len=0;
        String ext=URL_PATH.substring(URL_PATH.lastIndexOf(".")).toLowerCase();


        Date date=new Date();

        long lSysTime1 = date.getTime() ;
        FileOutputStream fileoutputstream=new FileOutputStream("D:\\"+lSysTime1+ext);
        while((len=inputStream.read(data))!=-1)
        {
            fileoutputstream.write(data,0,len);

        }
        fileoutputstream.close();
        inputStream.close();

    }

    /**
     * 根据文件路径GET请求获取文件流
     * @param imgUrl
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(String imgUrl) throws IOException{
        InputStream inputStream=null;
        HttpURLConnection httpurlconn=null;
        try {
            URL url=new URL(imgUrl);
            if(url!=null)
            {
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
     * @param args
     */
    public static void main(String[] args) {
        try {
            saveImagestodisk();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
