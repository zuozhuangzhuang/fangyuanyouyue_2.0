package com.fangyuanyouyue.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.AES;
import com.fangyuanyouyue.base.util.wechat.pay.WechatPayConfig;
import com.fangyuanyouyue.message.model.WeChatMessage;
import com.fangyuanyouyue.message.service.WeChatMessageService;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service(value = "weChatMessageService")
public class WeChatMessageServiceImpl implements WeChatMessageService{

    @Override
    public String checkSignature(WeChatMessage message){
        String signature = message.getSignature();
        String timestamp = message.getTimestamp();
        String nonce = message.getNonce();


        //将token、timestamp、nonce三个参数进行字典排序
//        // 被加密的数据
//        byte[] dataByte = Base64.decodeBase64(signature);
//        // 加密秘钥
//        byte[] aeskey = Base64.decodeBase64(WechatPayConfig.SESSION_KEY);
//        byte[] ivByte = new byte[]{};
//        // 偏移量
//        String newuserInfo;
//        try {
//            //AES解密
//            AES aes = new AES();
//            byte[] resultByte = aes.decrypt(dataByte, aeskey, ivByte);
//            if (null != resultByte && resultByte.length > 0) {
//                newuserInfo = new String(resultByte, "UTF-8");
//                System.out.println("解密完毕,解密结果为newuserInfo:"+ newuserInfo);
//                JSONObject jsonObject = JSONObject.parseObject(newuserInfo);
//            }else{
//                throw new ServiceException("解密异常!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ServiceException("解密异常!检查解密数据！");
//        }
        String[] arr = new String[] {WechatPayConfig.MESSAGE_TOKEN, timestamp, nonce};
        Arrays.sort(arr);

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }

        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) ? message.getEchostr() : "" : "";
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param byteArray
     * @return
     */
    private String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     * @param mByte
     * @return
     */
    private String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

}
