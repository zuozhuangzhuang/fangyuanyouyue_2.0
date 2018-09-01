package com.fangyuanyouyue.base.util.alipay.config;

import com.fangyuanyouyue.base.util.DateStampUtils;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	public static void main(String[] args) {
		Long a = new Long("1442424955163");
		
		System.out.println(DateStampUtils.formatGMTUnixTime(a));
	}
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088521233786104";
	public static String seller = "2118401905@qq.com";
	// 商户的私钥
	// 商户的私钥
	public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALJU44PPMDFPJnnbHeD86buXJppvbLsWQmE2VETA9NapP9IURIDbIqf/kmrdJVcvnTIBTcZwB6aq+JMpYSZeN6DCFs/aKrZoLkcUX093c/V8FjZjvNcqtgOl9YSbTYDX/JzU/nsxfPKNxDQ1rkNy9fAZ3c2G8+77YkzSZ8337vd1AgMBAAECgYEAqLcdRM/ZBV2fxjuiqgA9Vafr8sImOV4W0Qfoc9+mBAy9/5ADO3GW+SAXMxkkQbcfXnbTUUEeS/WHFRtZ+UW8E7wbCjCEGA1X3PQdWBSWXRiR0uB4sjp1oP+y/fm+ZwDpZz7owQpkE1PngpM5j4+M5KcJ2ITytL1+M9w7w5PVLEUCQQDYCIjPsl6l5KTSSqrpmKj8t33CiT+own+KJVAE5RsnDvjcZrxK3BPlYgHyrgjNYcl2/4on7aRrj0RjAVpPhQBvAkEA01LHGkQNjLQ4+AdfNe5qp6FtzTiEzamHlGM8B13xrpkSt6q3yD1dtdPl9TaPsBnqfvQdYfWRD5EIN/nvOR0wWwJBAKXtwC9PAFWJDXOb+DRdlgYtZYGi62tSoKVZzWFFG5rfbBPVMPCKoxl/MqHUPFyLrDMjSVkrtSsMKmmk41nKfS8CQGPZewYoHZbcGQboxM4AxxhJqE4NZIKgldVBRj3c37M11Kg/2/KbPEoLw14n16DHvgsaYT/F/jKyvoVkbOo5pn0CQBjIBX3yDzRxcrSSfOImV8pr5P0cVDgJVeAzpWpCUDetnZJxe37TKf0iCrvL77ocEMeGCh4hr4nY4xW6QkQYd2o=";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	//安全校验码(Key)默认加密
	//public static String key = "iyt3iyw8ypjy7r6axk4uspvcivlhsmhc";
	public static String key = "8fq7bg636rjdjcwito2ldgs5xi0qim7n";

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";
	// 签名方式 不需修改
	public static String sign_type_refund = "MD5";

}
