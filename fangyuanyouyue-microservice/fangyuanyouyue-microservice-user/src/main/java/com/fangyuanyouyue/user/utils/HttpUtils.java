package com.fangyuanyouyue.user.utils;

import com.fangyuanyouyue.base.util.HttpUtil;

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
    public static void main(String[] args) throws Exception {
//        try {
//            saveImagestodisk();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        addUser();
    }

    static void addUser() throws Exception {
        String[] names = new String[]{
                "天尊艺术馆","紫竹艺苑","艺博轩",
                "鹏程工艺坊","旧时光钟表行","艺宝阁",
                "皆大欢喜艺宝堂","集书斋","无有艺人不养君子",
                "湘约茶坊","茶斋宝阁","红袖茶园",
                "茶花荟翠","上善轩","万宝筒",
                "纪宝斋古玩店","南無阿弥陀佛","时尚坊",
                "琉璃时光","新文艺","古受新藏",
                "山里人家","数码港","九龙钟表行",
                "大包小包","大藏小藏",
                "旧码头","票证堂","鉴古董心",
                "红粉佳人","云想衣裳","服饰珠宝行",
                "博雅工艺坊","和谐工艺坊","中华工艺坊",
                "现代收藏","工艺珍藏","小云楼",
                "檀缘轩","雨澜宝轩","竹茗堂",
                "泉贯至今","澜熙典藏","件件咸宜",
                "玉石情愿","金石典藏","绿肥红瘦",
                "声声慢","藏爱","藏红缘",
                "藏红藏绿","千般藏","幸福有藏",
                "藏人生","乾无坤德","厚德有藏",
                "香格里拉","藏之港湾","收藏之道",
        };
        for(int i=341,j=0;j<names.length;i++,j++){
            System.out.println("13333333"+i);
            System.out.println(names[j]);
//            HttpUtil.sendPost("http://192.168.1.66:8769/user/user/regist","phone=13333333"+i+"&nickName="+names[j]+"&loginPwd=e10adc3949ba59abbe56e057f20f883e&regPlatform=1");
//            HttpUtil.sendPost("http://zuul.fangyuanyouyue.com/user/user/regist","phone=13333333"+i+"&nickName="+names[j]+"&loginPwd=e10adc3949ba59abbe56e057f20f883e&regPlatform=1");
            HttpUtil.sendPost("http://gateway.fangyuanyouyue.com/user/user/regist","phone=13333333"+i+"&nickName="+names[j]+"&loginPwd=e10adc3949ba59abbe56e057f20f883e&regPlatform=1");
        }
    }

}
