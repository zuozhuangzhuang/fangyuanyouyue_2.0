 package com.fangyuanyouyue.forum.controller;

 import com.alibaba.fastjson.JSONObject;
 import com.fangyuanyouyue.base.BaseController;
 import com.fangyuanyouyue.base.BaseResp;
 import com.fangyuanyouyue.base.enums.ReCode;
 import com.fangyuanyouyue.base.exception.ServiceException;
 import com.fangyuanyouyue.base.model.WxPayResult;
 import com.fangyuanyouyue.base.util.WechatUtil.WXPayUtil;
 import com.fangyuanyouyue.base.util.alipay.util.AlipayNotify;
 import com.fangyuanyouyue.forum.dto.ForumColumnDto;
 import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;
 import com.fangyuanyouyue.forum.dto.MyColumnDto;
 import com.fangyuanyouyue.forum.param.ForumParam;
 import com.fangyuanyouyue.forum.service.ForumColumnService;
 import com.fangyuanyouyue.forum.service.SchedualRedisService;
 import com.fangyuanyouyue.forum.service.SchedualUserService;
 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiImplicitParam;
 import io.swagger.annotations.ApiImplicitParams;
 import io.swagger.annotations.ApiOperation;
 import org.apache.commons.lang.StringUtils;
 import org.apache.log4j.Logger;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.cloud.context.config.annotation.RefreshScope;
 import org.springframework.web.bind.annotation.*;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import java.io.IOException;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;

 @RestController
@RequestMapping(value = "/column")
@Api(description = "专栏Controller")
@RefreshScope
public class ForumColumnController extends BaseController {
	protected Logger log = Logger.getLogger(this.getClass());
	protected Logger wechatLog = Logger.getLogger(this.getClass());
	protected Logger alipaytLog = Logger.getLogger(this.getClass());

	@Autowired
	private ForumColumnService forumColumnService;
	@Autowired
	private SchedualUserService schedualUserService;
	@Autowired
	private SchedualRedisService schedualRedisService;

	@ApiOperation(value = "专栏列表", notes = "（ForumColumnTypeDto）获取全部专栏", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "typeId", value = "专栏分类id",required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query")
	})
	@PostMapping(value = "/list")
	@ResponseBody
	public BaseResp forumColumn(ForumParam param) throws IOException {
		try {
			log.info("----》获取全部专栏《----");
			log.info("参数：" + param.toString());

			if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
				return toError("分页参数错误");
			}
			List<ForumColumnTypeDto> dto = forumColumnService.getColumnList(param.getTypeId(),param.getStart(),param.getLimit());

			return toSuccess(dto);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}

	@ApiOperation(value = "精选专栏", notes = "(ForumColumnDto)获取精选专栏", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query")
	})
	@PostMapping(value = "/chosen")
	@ResponseBody
	public BaseResp chosenColumn(ForumParam param) throws IOException {
		try {
			log.info("----》获取精选的专栏《----");
			log.info("参数：" + param.toString());

			if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
				return toError("分页参数错误");
			}
			List<ForumColumnDto> dto = forumColumnService.getChosenColumnList(param.getStart(),param.getLimit());

			return toSuccess(dto);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}


	@ApiOperation(value = "获取专栏分类列表", notes = "（ForumColumnTypeDto）获取专栏分类列表", response = BaseResp.class)
	@PostMapping(value = "/getForumTypeList")
	@ResponseBody
	public BaseResp getForumTypeList() throws IOException {
		try {
			log.info("----》获取专栏分类列表《----");
			//获取专栏分类列表
			List<ForumColumnTypeDto> typeList = forumColumnService.getForumTypeList();
			return toSuccess(typeList);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}

	@ApiOperation(value = "申请专栏", notes = "(WechatPayDto、String)申请专栏", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "typeId", value = "专栏分类id",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "专栏名称",required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "payType", value = "支付方式  1微信 2支付宝 3余额 4小程序",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "payPwd", value = "支付密码", dataType = "String", paramType = "query")
	})
	@PostMapping(value = "/addColumn")
	@ResponseBody
	public BaseResp addColumn(ForumParam param) throws IOException {
		try {
			log.info("----》申请专栏《----");
			log.info("参数：" + param.toString());
			//验证用户
			if(StringUtils.isEmpty(param.getToken())){
				return toError("用户token不能为空！");
			}
			Integer userId = (Integer)schedualRedisService.get(param.getToken());
			String verifyUser = schedualUserService.verifyUserById(userId);
			JSONObject jsonObject = JSONObject.parseObject(verifyUser);
			if((Integer)jsonObject.get("code") != 0){
				return toError(jsonObject.getString("report"));
			}
			//验证实名认证
			if(JSONObject.parseObject(schedualUserService.isAuth(userId)).getBoolean("data") == false){
				return toError("用户未实名认证！");
			}
			if(param.getTypeId() == null){
				return toError("专栏分类id不能为空！");
			}
			if(StringUtils.isEmpty(param.getName())){
				return toError("专栏名称不能为空！");
            }
            if(param.getPayType() == null){
				return toError("支付方式不能为空！");

			}
			//申请专栏
            Object info = forumColumnService.addColumn(userId,param.getTypeId(),param.getName(),param.getPayType(),param.getPayPwd());

			return toSuccess(info);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}


	 @ApiOperation(value = "申请专栏回调接口", notes = "申请专栏", response = BaseResp.class)
	 @PostMapping(value = "/notify/wechat")
	 @ResponseBody
	 public String notify(HttpServletRequest request, HttpServletResponse response) throws IOException {

		 String resXml = "";

		 try{
			 //把如下代码贴到的你的处理回调的servlet 或者.do 中即可明白回调操作
			 wechatLog.info("微信支付小方圆回调数据开始");


			 //示例报文
			 //		String xml = "<xml><appid><![CDATA[wxb4dc385f953b356e]]></appid><bank_type><![CDATA[CCB_CREDIT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1228442802]]></mch_id><nonce_str><![CDATA[1002477130]]></nonce_str><openid><![CDATA[o-HREuJzRr3moMvv990VdfnQ8x4k]]></openid><out_trade_no><![CDATA[1000000000051249]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[1269E03E43F2B8C388A414EDAE185CEE]]></sign><time_end><![CDATA[20150324100405]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1009530574201503240036299496]]></transaction_id></xml>";
			 String inputLine;
			 String notityXml = "";
			 try {
				 while ((inputLine = request.getReader().readLine()) != null) {
					 notityXml += inputLine;
				 }
				 request.getReader().close();
			 } catch (Exception e) {
				 e.printStackTrace();
			 }

			 wechatLog.info("接收到的报文：" + notityXml);


//			Map m = parseXmlToList2(notityXml);
			 Map m = WXPayUtil.xmlToMap(notityXml);
			 WxPayResult wpr = new WxPayResult();
			 if(m != null){
				 wpr.setAppid(m.get("appid").toString());
				 wpr.setBankType(m.get("bank_type").toString());
				 wpr.setCashFee(m.get("cash_fee").toString());
				 //wpr.setFeeType(m.get("fee_type").toString());
				 //wpr.setIsSubscribe(m.get("is_subscribe").toString());
				 wpr.setMchId(m.get("mch_id").toString());
				 wpr.setNonceStr(m.get("nonce_str").toString());
				 wpr.setOpenid(m.get("openid").toString());
				 wpr.setOutTradeNo(m.get("out_trade_no").toString());
				 wpr.setResultCode(m.get("result_code").toString());
				 wpr.setReturnCode(m.get("return_code")==null?"":m.get("return_code").toString());
				 wpr.setSign(m.get("sign").toString());
				 wpr.setTimeEnd(m.get("time_end").toString());
				 wpr.setTotalFee(m.get("total_fee").toString());
				 wpr.setTradeType(m.get("trade_type").toString());
				 wpr.setTransactionId(m.get("transaction_id").toString());
			 }
			 wechatLog.info("返回信息："+wpr.toString());
			 if("SUCCESS".equals(wpr.getResultCode())){
				 //支付成功
				 boolean result = forumColumnService.applyColumn(wpr.getOutTradeNo(), wpr.getTransactionId(),1);

				 wechatLog.info("支付成功！");
				 if(result){
					 resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							 + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

					 wechatLog.info("处理成功！");
				 }else{
					 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							 + "<return_msg><![CDATA[FAILD]]></return_msg>" + "</xml> ";

					 wechatLog.info("处理失败！");
				 }


			 }else{
				 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						 + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				 wechatLog.info("支付失败！");
			 }

			 wechatLog.info("微信支付回调结束");


		 } catch (Exception e) {
			 e.printStackTrace();
			 wechatLog.error("微信通知后台处理系统出错", e);
			 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					 + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		 }
		 return resXml;
	 }

	 //支付宝回调
	 @ApiOperation(value = "申请官方鉴赏支付宝回调接口", notes = "支付宝回调", response = BaseResp.class,hidden = true)
	 @RequestMapping(value = "/notify/alipay", method = RequestMethod.POST)
	 @ResponseBody
	 public String orderNotify(HttpServletRequest request) throws IOException {

		 alipaytLog.info("-----------支付宝后台通知-----------");
		 //HttpServletRequest request = getRequest();
		 // 获取支付宝POST过来反馈信息
		 Map<String, String> params = new HashMap<String, String>();
		 Map requestParams = request.getParameterMap();
		 String response = "";
		 for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			 String name = (String) iter.next();
			 String[] values = (String[]) requestParams.get(name);
			 String valueStr = "";
			 for (int i = 0; i < values.length; i++) {
				 valueStr = (i == values.length - 1) ? valueStr + values[i]
						 : valueStr + values[i] + ",";
			 }
			 // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			 // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			 params.put(name, valueStr);

		 }
		 for (String key : params.keySet()) {
			 response += key + "=" + params.get(key) + ",";
		 }
		 if (response.equals("")) {
			 alipaytLog.warn("无数据返回");
			 return "";
		 }
		 alipaytLog.warn("支付宝响应报文[订单号:" + params.get("out_trade_no") + "]：" + response);
		 String ret = "";
		 try {
			 // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			 // 商户订单号
			 String out_trade_no = new String(request.getParameter(
					 "out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			 alipaytLog.info("商户订单号：" + out_trade_no);
			 // 支付宝交易号
			 String trade_no = new String(request.getParameter("trade_no")
					 .getBytes("ISO-8859-1"), "UTF-8");
			 alipaytLog.info("支付宝交易号：" + trade_no);
			 // 交易状态
			 String trade_status = new String(request.getParameter(
					 "trade_status").getBytes("ISO-8859-1"), "UTF-8");

			 alipaytLog.info("交易状态：" + trade_status);
			 // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			 alipaytLog.info("支付响应报文开始验证");
			 if (AlipayNotify.verify(params)) {// 验证成功

				 alipaytLog.info("支付宝支付验证成功[订单号:" + trade_no + "]");
				 // ////////////////////////////////////////////////////////////////////////////////////////
				 // 请在这里加上商户的业务逻辑程序代码

				 // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

				 if (trade_status.equals("TRADE_FINISHED")) {

					 alipaytLog.info("支付宝支付完成！TRADE_FINISHED");
					 // 判断该笔订单是否在商户网站中已经做过处理
					 // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					 // 如果有做过处理，不执行商户的业务程序
					 boolean result = forumColumnService.applyColumn(out_trade_no, trade_no,2);
					 if(result){
						 ret = "success"; // 请不要修改或删除
					 }else{
						 ret = "fail";
					 }
					 // 注意：
					 // 该种交易状态只在两种情况下出现
					 // 1、开通了普通即时到账，买家付款成功后。
					 // 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
				 } else if (trade_status.equals("TRADE_SUCCESS")) {
					 alipaytLog.info("支付宝支付完成！TRADE_SUCCESS");
					 // service.doUpdate(out_trade_no);
					 boolean result = forumColumnService.applyColumn(out_trade_no, trade_no,2);
					 if(result){
						 ret = "success"; // 请不要修改或删除
					 }else{
						 ret = "fail";
					 }
					 // 判断该笔订单是否在商户网站中已经做过处理
					 // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					 // 如果有做过处理，不执行商户的业务程序

					 // 注意：
					 // 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
				 }else{
					 ret = "fail";
				 }
				 // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				 // ////////////////////////////////////////////////////////////////////////////////////////
			 } else {// 验证失败
				 alipaytLog.error("支付宝支付验证失败！");
				 ret = "fail";
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
			 alipaytLog.error("支付宝通知后台处理系统出错", e);
			 ret = "fail";
		 }
		 return ret;
	 }


	 @ApiOperation(value = "我是栏主", notes = "(MyColumnDto)我是栏主", response = BaseResp.class)
	 @ApiImplicitParams({
			 @ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
			 @ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
			 @ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query")
	 })
	 @PostMapping(value = "/myColumn")
	 @ResponseBody
	 public BaseResp myColumn(ForumParam param) throws IOException {
		 try {
			 log.info("----》我是栏主《----");
			 log.info("参数：" + param.toString());
			 //验证用户
			 if(StringUtils.isEmpty(param.getToken())){
				 return toError("用户token不能为空！");
			 }
			 Integer userId = (Integer)schedualRedisService.get(param.getToken());
			 String verifyUser = schedualUserService.verifyUserById(userId);
			 JSONObject jsonObject = JSONObject.parseObject(verifyUser);
			 if((Integer)jsonObject.get("code") != 0){
				 return toError(jsonObject.getString("report"));
			 }

			 if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
				 return toError("分页参数错误");
			 }
			 //我是栏主
			 MyColumnDto myColumn = forumColumnService.myColumn(userId,param.getStart(),param.getLimit());

			 return toSuccess(myColumn);
		 } catch (ServiceException e) {
			 e.printStackTrace();
			 return toError(e.getMessage());
		 } catch (Exception e) {
			 e.printStackTrace();
			 return toError("系统繁忙，请稍后再试！");
		 }
	 }


//	 @ApiOperation(value = "用户的专栏", notes = "(MyColumnDto)用户的专栏", response = BaseResp.class)
//	 @ApiImplicitParams({
//			 @ApiImplicitParam(name = "userId", value = "用户id",required = true, dataType = "int", paramType = "query"),
//			 @ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
//			 @ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query")
//	 })
//	 @PostMapping(value = "/userColumn")
//	 @ResponseBody
//	 public BaseResp userColumn(ForumParam param) throws IOException {
//		 try {
//			 log.info("----》用户的专栏《----");
//			 log.info("参数：" + param.toString());
//			 //验证用户
//			 if(param.getUserId() == null){
//				 return toError("用户id不能为空！");
//			 }
//			 Integer userId = (Integer)schedualRedisService.get(param.getToken());
//			 String verifyUser = schedualUserService.verifyUserById(userId);
//			 JSONObject jsonObject = JSONObject.parseObject(verifyUser);
//			 if((Integer)jsonObject.get("code") != 0){
//				 return toError(jsonObject.getString("report"));
//			 }
//
//			 if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
//				 return toError("分页参数错误");
//			 }
//			 //用户的专栏
//			 MyColumnDto myColumn = forumColumnService.myColumn(param.getUserId(),param.getStart(),param.getLimit());
//
//			 return toSuccess(myColumn);
//		 } catch (ServiceException e) {
//			 e.printStackTrace();
//			 return toError(e.getMessage());
//		 } catch (Exception e) {
//			 e.printStackTrace();
//			 return toError("系统繁忙，请稍后再试！");
//		 }
//	 }

 }
