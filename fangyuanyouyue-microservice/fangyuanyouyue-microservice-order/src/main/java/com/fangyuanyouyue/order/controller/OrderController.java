package com.fangyuanyouyue.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.dto.CompanyDto;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.param.OrderParam;
import com.fangyuanyouyue.order.service.OrderService;
import com.fangyuanyouyue.order.service.SchedualGoodsService;
import com.fangyuanyouyue.order.service.SchedualRedisService;
import com.fangyuanyouyue.order.service.SchedualUserService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/order")
@Api(description = "订单系统Controller")
@RefreshScope
public class OrderController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualGoodsService schedualGoodsService;
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SchedualRedisService schedualRedisService;

    @ApiOperation(value = "购物车商品下单", notes = "(OrderDto)购物车商品下单",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sellerList", value = "下单信息，格式为：[{\"sellerId\":16,\"addOrderDetailDtos\":[{\"goodsId\":2,\"couponId\":10}]},{\"sellerId\":25,\"addOrderDetailDtos\":[{\"goodsId\":102}]}]的字符串",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "收货地址id",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/saveOrderByCart")
    @ResponseBody
    public BaseResp saveOrderByCart(OrderParam param) throws IOException {
        try {
            log.info("----》购物车商品下单《----");
            log.info("参数："+param.toString());
            //参数判断
            if(param.getAddressId()==null || param.getAddressId().intValue()==0){
                return toError("收货地址id不能为空！");
            }
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            if(param.getSellerList() == null){
                return toError(ReCode.FAILD.getValue(),"下单信息不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if(jsonObject != null && (Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            //TODO 下单商品
            OrderDto orderDto = orderService.saveOrderByCart(param.getToken(),param.getSellerList(), userId, param.getAddressId());
            return toSuccess(orderDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "取消订单", notes = "(void)取消订单",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/cancelOrder")
    @ResponseBody
    public BaseResp cancelOrder(OrderParam param) throws IOException {
        try {
            log.info("----》取消订单《----");
            log.info("参数："+param.toString());
            //参数判断
            if(param.getOrderId()==null){
                return toError("订单ID不能为空！");
            }
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            //取消订单
            orderService.cancelOrder(userId,param.getOrderId());
            return toSuccess("取消订单成功！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }

    }


    @ApiOperation(value = "订单详情", notes = "(OrderDto)订单详情",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/orderDetail")
    @ResponseBody
    public BaseResp orderDetail(OrderParam param) throws IOException {
        try {
            log.info("----》订单详情《----");
            log.info("参数："+param.toString());
            //参数判断
            if(param.getOrderId()==null){
                return toError("订单ID不能为空！");
            }
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            //订单详情
            OrderDto orderDto = orderService.orderDetail(userId, param.getOrderId());
            return toSuccess(orderDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "我的订单列表", notes = "(OrderDto)我的订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1买家（我买下的） 2卖家（我卖出的）", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "订单状态 0全部 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/myOrderList")
    @ResponseBody
    public BaseResp myOrderList(OrderParam param) throws IOException {
        try {
            log.info("----》我的订单列表《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }

            if(param.getStart()==null){
                return toError("start不能为空！");
            }
            if(param.getLimit()==null){
                return toError("limit不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            if(param.getStatus() == null){
                return toError("订单状态不能为空！");
            }
            //TODO 我的订单列表
            List<OrderDto> orderDtos = orderService.myOrderList(userId, param.getStart(), param.getLimit(), param.getType(), param.getStatus());
            return toSuccess(orderDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "商品/抢购直接下单", notes = "(OrderDto)商品/抢购直接下单",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "couponId", value = "优惠券ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "收货地址id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/saveOrder")
    @ResponseBody
    public BaseResp saveOrder(OrderParam param) throws IOException {
        try {
            log.info("----》商品/抢购直接下单《----");
            log.info("参数："+param.toString());
            //参数判断
            if(param.getAddressId()==null || param.getAddressId().intValue()==0){
                return toError("收货地址id不能为空！");
            }
            if(param.getGoodsId() == null){
                return toError("商品ID不能为空！");
            }
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if(jsonObject != null && (Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getType().intValue() == 2){
                //TODO 非会员只能免费抢购一次，会员可无限制抢购——验证是否为会员

            }
            //TODO 下单商品
            OrderDto orderDto = orderService.saveOrder(param.getToken(),param.getGoodsId(),param.getCouponId(), userId, param.getAddressId());
            return toSuccess(orderDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "订单支付", notes = "(OrderDto)订单支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "支付方式  1微信 2支付宝 3余额", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/getOrderPay")
    @ResponseBody
    public BaseResp getOrderPay(OrderParam param) throws IOException {
        try {
            log.info("----》订单支付《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getOrderId()==null){
                return toError("订单ID不能为空！");
            }
            if(param.getPayType()==null){
                return toError("支付类型不能为空！");
            }
            if(param.getPayPwd() == null){
                return toError("支付密码不能为空！");
            }
            //TODO 订单支付
            String payInfo = orderService.getOrderPay(userId, param.getOrderId(), param.getPayType(), param.getPayPwd());
            return toSuccess(payInfo);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "卖家确认发货", notes = "(void)卖家确认发货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "物流公司", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "number", value = "物流号", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/sendGoods")
    @ResponseBody
    public BaseResp sendGoods(OrderParam param) throws IOException {
        try {
            log.info("----》卖家确认发货《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getOrderId()==null || param.getOrderId().intValue()==0){
                return toError("订单ID不能为空！");
            }
            if(param.getCompanyId()==null || param.getCompanyId().intValue()==0){
                return toError("物流公司ID不能为空！");
            }
            if(StringUtils.isEmpty(param.getNumber())){
                return toError("物流号不能为空！");
            }
            //TODO 卖家确认发货
            orderService.sendGoods(userId, param.getOrderId(), param.getCompanyId(),param.getNumber());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "买家确认收货", notes = "(void)买家确认收货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/getGoods")
    @ResponseBody
    public BaseResp getGoods(OrderParam param) throws IOException {
        try {
            log.info("----》买家确认收货《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getOrderId()==null || param.getOrderId().intValue()==0){
                return toError("订单ID不能为空！");
            }
            //TODO 买家确认收货
            orderService.getGoods(userId, param.getOrderId());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "物流公司", notes = "(CompanyDto)物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/companyList")
    @ResponseBody
    public BaseResp companyList(OrderParam param) throws IOException {
        try {
            log.info("----》物流公司《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            //TODO 物流公司
            List<CompanyDto> companyDtos = orderService.companyList();
            return toSuccess(companyDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


//
//    @ApiOperation(value = "物流公司", notes = "()物流公司")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
//    })
//    @PostMapping(value = "/companyList")
//    @ResponseBody
//    public BaseResp companyList(OrderParam param) throws IOException {
//        try {
//            log.info("----》物流公司《----");
//            log.info("参数："+param.toString());
//            //验证用户
//            if(StringUtils.isEmpty(param.getToken())){
//                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
//            }
//            Integer userId = (Integer)schedualRedisService.get(param.getToken());
//            String verifyUser = schedualUserService.verifyUserById(userId);
//            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
//            if((Integer)jsonObject.get("code") != 0){
//                return toError(jsonObject.getString("report"));
//            }
//            if(param.getOrderId()==null){
//                return toError("订单ID不能为空！");
//            }
////            /**
////             *
////             */
////            //订单物流详情，即时查询
////            List<LogisticsInfoDto> dtos = new ArrayList<LogisticsInfoDto>();
////            try{
////                /*
////                 * author zzz
////                 * 每次查看订单详情都要请求物流信息，感觉不合理，如果希望后台控制物流查询，至少也得独立出来一个接口，防止服务器资源被无故占用
////                 */
////                KdniaoTrackQueryAPI utils = new KdniaoTrackQueryAPI();
////                String datas = utils.getOrderTracesByJson(order.getCompanyNo(),order.getLogistics());
////                logisticLog.info(datas);
////
////                ObjectMapper mapper = new ObjectMapper();
////                JsonFactory factory = mapper.getFactory();
////                JsonParser jp = factory.createParser(datas);
////                ObjectNode obj = mapper.readTree(jp);
////
////                ArrayNode data = (ArrayNode) obj.get("Traces");
////
////                for (int j = 0; j < data.size(); j++) {
////                    JsonNode trace=data.get(j);
////
////                    LogisticsInfoDto logisticsInfo = new LogisticsInfoDto();
////                    //以上的操作，都是为了获取下面这俩字段，然后放到logisticsInfo里面
////                    logisticsInfo.setAcceptTime(trace.get("AcceptTime").asText());
////                    logisticsInfo.setInfo(trace.get("AcceptStation").asText());
////                    dtos.add(logisticsInfo);
////                }
////            }catch(Exception e){
////                logisticLog.error("查找不到快递");
////            }
//            //TODO 物流公司
//            List<CompanyDto> companyDtos = orderService.companyList();
//            return toSuccess(companyDtos);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            return toError(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }


}
