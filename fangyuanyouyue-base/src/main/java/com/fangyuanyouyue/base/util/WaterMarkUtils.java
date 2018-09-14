package com.fangyuanyouyue.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class WaterMarkUtils {


    public static String getWaterMark(String imgUrl,String userName) throws Exception {
        int waterNum = userName.length()+"小方圆@".length();
        String imgInfo = HttpUtil.sendGet(imgUrl, "x-oss-process=image/info");
        double imageWidth = JSONObject.parseObject(JSONObject.parseObject(imgInfo).getString("ImageWidth")).getDouble("value");
        double imageHeight = JSONObject.parseObject(JSONObject.parseObject(imgInfo).getString("ImageHeight")).getDouble("value");
        //水印长度 = 图片宽 / 4

        System.out.println("宽："+imageWidth);
        System.out.println("高："+imageHeight);
        String waterParam = safeUrlBase64Encode(("小方圆@"+userName).getBytes());
        System.out.println(new String(safeUrlBase64Decode(waterParam)));
        System.out.println("waterNum:"+waterNum);
        System.out.println("size:"+Math.ceil(imageWidth/4/waterNum));
        imgUrl = imgUrl+"?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,size_"+(int)Math.ceil(imageWidth/4/waterNum)+",text_"+waterParam+",color_FFFFFF,g_se";
        return imgUrl;
    }
//base编码
    public static String safeUrlBase64Encode(byte[] data){
        String encodeBase64 = new BASE64Encoder().encode(data);
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        return safeBase64Str;
    }

    //base编码
    public static byte[] safeUrlBase64Decode(final String safeBase64Str) throws IOException {
        String base64Str = safeBase64Str.replace('-', '+');
        base64Str = base64Str.replace('_', '/');
        int mod4 = base64Str.length()%4;
        if(mod4 > 0){
            base64Str = base64Str + "====".substring(mod4);
        }
        return new BASE64Decoder().decodeBuffer(base64Str);
    }

    public static void main(String[] args) throws Exception {
        String url = getWaterMark("https://xiaofangyuan.oss-cn-shenzhen.aliyuncs.com/pic/2018/09/13/810d50d1-37dc-4018-91d5-7852739ebeea.jpeg","咖喱盖盖");
        System.out.println(url);
    }
}
