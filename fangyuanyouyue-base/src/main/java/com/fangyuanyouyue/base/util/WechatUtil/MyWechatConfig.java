package com.fangyuanyouyue.base.util.WechatUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MyWechatConfig extends WXPayConfig {
    private byte[] certData;

//    public MyWechatConfig() throws Exception {
//        //证书路径
//        String certPath = "/path/to/apiclient_cert.p12";
//
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
//    }

    public String getAppID() {
        return "wx306dfd8f2342f051";
    }

    public String getMchID() {
        return "1418798002";
    }

    public String getKey() {
        return "ShenZhenShiXiaoFangYuan123456789";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo("test.fangyuanyouyue.com",true);
            }
        };
    }


}
