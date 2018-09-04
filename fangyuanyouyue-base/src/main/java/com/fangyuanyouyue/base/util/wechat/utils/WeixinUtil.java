package com.fangyuanyouyue.base.util.wechat.utils;


import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.util.wechat.pay.WechatPayConfig;
import com.fangyuanyouyue.base.util.wechat.pojo.AccessToken;
import com.fangyuanyouyue.base.util.wechat.pojo.Menu;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

/**
 * 公众平台通用接口工具类
 * 
 * @author wuzhimin  2014-2-27
 * 
 */
public class WeixinUtil {
	private static Logger log = Logger.getLogger(WeixinUtil.class);
	
	// 获取access_token的接口地址（GET） 限200（次/天）
	public static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 菜单创建（POST） 限100（次/天）
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	// 获取jsapi_ticket的接口地址（GET） 限200（次/天）
	public static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	//获取用户access_token和openId接口
	public static String user_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token";
	//grant_type=authorization_code&appid=%s&secret=%s&code=%s
	//获取微信账户信息
	public static String old_user_info_url = "https://api.weixin.qq.com/cgi-bin/user/info";
	public static String user_info_url = "https://api.weixin.qq.com/sns/userinfo";
	//?access_token=ACCESS_TOKEN&openid=OPENID
//	public static String info_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
	
	public static String refresh_user_token_url = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	//appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
	
	public static String check_user_token_url = "https://api.weixin.qq.com/sns/auth";
	//access_token=ACCESS_TOKEN&openid=OPENID
	
	//发送模板消息
	public static String templet_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			System.out.println("buffer:\n"+buffer.toString());//TODO 
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.info("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}",e);
		}
		return jsonObject;
	}
	

	/**
	 * 获取access_token
	 * 
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
			} catch (Exception e) {
				accessToken = new AccessToken();
				accessToken.setErrcode(jsonObject.getIntValue("errcode"));
				accessToken.setErrmsg(jsonObject.getString("errmsg"));
				// 获取token失败
				log.error("获取token失败 errcode:"+ jsonObject.getIntValue("errcode")+" errmsg:"+jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	
	/**
	 * 
	 * 刷新用户accessToken有效期
	 * 
	 * @param appId
	 * @param refresh_token
	 * @return
	 */
//	public static AccessTokenDto updateUserAccessToken(String appId,String refresh_token){
//		/*
//		 * access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种：
//		 * 1. 若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
//		 * 2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
//		 *
//		 * refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权，所以，请开发者在refresh_token即将过期时（如第29天时），进行定时的自动刷新并保存好它。
//		 */
//		String request = HttpUtil.sendGet(refresh_user_token_url, String.format("appid=%s&grant_type=refresh_token&refresh_token=%s", appId, refresh_token));
//		AccessTokenDto accessTokenDto = (AccessTokenDto) JsonUtil.getDtoFromJsonObjStr(request, AccessTokenDto.class);
//		return accessTokenDto;
//	}
	
	/**
	 * 
	 * 验证access_token有效性
	 * <br>errcode:0
	 * 
	 * @param access_token
	 * @param openid
	 * @return
	 */
//	public static AccessTokenDto getUserCheckAccessToken(String access_token,String openid){
//		String request = HttpUtil.sendGet(check_user_token_url, String.format("access_token=%s&openid=%s", access_token, openid));
//		AccessTokenDto accessTokenDto = (AccessTokenDto) JsonUtil.getDtoFromJsonObjStr(request, AccessTokenDto.class);
//		return accessTokenDto;
//	}
	
	/**
	 * 
	 * 获得accessToken-Ticket
	 * 
	 * @param accesstoken
	 * @return
	 */
	public static AccessToken getTicket(String accesstoken) {
		AccessToken accessToken = null;
		String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", accesstoken);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setTicket(jsonObject.getString("ticket"));
				accessToken.setTicketExpiresIn(jsonObject.getIntValue("expires_in"));
			} catch (Exception e) {
				accessToken = new AccessToken();
				accessToken.setErrcode(jsonObject.getIntValue("errcode"));
				accessToken.setErrmsg(jsonObject.getString("errmsg"));
				// 获取token失败
				log.error("获取ticket失败 errcode:"+ jsonObject.getIntValue("errcode")+" errmsg:"+jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	/**
	 * 创建菜单
	 * 
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.parseObject(menu.toString()).toString();
		// 调用接口创建菜单
		System.out.println(jsonMenu);
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				result = jsonObject.getIntValue("errcode");
				log.error("创建菜单失败 errcode:"+ jsonObject.getIntValue("errcode")+"errmsg:"+jsonObject.getString("errmsg"));
			}
		}

		return result;
	}
	
	
	public static void main(String[] args) {
		AccessToken accessToken = getAccessToken(WechatPayConfig.APP_ID_WEB, WechatPayConfig.APP_SECRET_WEB);
		System.out.println(accessToken.getExpiresIn());
		System.out.println(accessToken.getToken());
	}
}