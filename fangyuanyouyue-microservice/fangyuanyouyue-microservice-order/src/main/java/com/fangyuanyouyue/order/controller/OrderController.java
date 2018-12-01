package com.fangyuanyouyue.order.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.model.WxPayResult;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.base.util.WechatUtil.WXPayUtil;
import com.fangyuanyouyue.base.util.alipay.util.AlipayNotify;
import com.fangyuanyouyue.order.dto.*;
import com.fangyuanyouyue.order.param.OrderParam;
import com.fangyuanyouyue.order.service.OrderService;
import com.fangyuanyouyue.order.service.SchedualGoodsService;
import com.fangyuanyouyue.order.service.SchedualRedisService;
import com.fangyuanyouyue.order.service.SchedualUserService;
import com.snowalker.lock.redisson.RedissonLock;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping(value = "/order")
@Api(description = "订单系统Controller")
@RefreshScope
public class OrderController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    protected Logger wechatLog = Logger.getLogger(this.getClass());
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
    @Autowired
    private RedissonLock redissonLock;

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
                return toError("用户token不能为空！");
            }
            if(param.getSellerList() == null){
                return toError("下单信息不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }

            //加入分布式锁，锁住商品id，10秒后释放
            try {
                List<SellerDto> sellerDtos = new ArrayList<>();
                List<AddOrderDto> addOrderDtos = new ArrayList<>();
                JSONArray objects;
                objects = JSONArray.parseArray(param.getSellerList());
                for (int i = 0; i < objects.size(); i++) {
                    String str = objects.getString(i);
                    AddOrderDto addOrderDto = JSONObject.toJavaObject(JSONObject.parseObject(str), AddOrderDto.class);
                    for (AddOrderDetailDto detailDto : addOrderDto.getAddOrderDetailDtos()) {
                        boolean lock = redissonLock.lock("Goods"+detailDto.getGoodsId(), 10);
                        if(!lock) {
                            Log.info("分布式锁获取失败");
                            throw new ServiceException("下单失败，您来晚了，宝贝已被抢走了~");
                        }
                    }
                }
            }catch (Exception e) {
                throw new ServiceException("下单失败，您来晚了，宝贝已被抢走了~");
            }
            //购物车商品下单
            OrderDto orderDto = orderService.saveOrderByCart(param.getToken(),param.getSellerList(), userId, param.getAddressId());
            return toSuccess(orderDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }finally {
            try {
                List<SellerDto> sellerDtos = new ArrayList<>();
                List<AddOrderDto> addOrderDtos = new ArrayList<>();
                JSONArray objects;
                objects = JSONArray.parseArray(param.getSellerList());
                for (int i = 0; i < objects.size(); i++) {
                    String str = objects.getString(i);
                    AddOrderDto addOrderDto = JSONObject.toJavaObject(JSONObject.parseObject(str), AddOrderDto.class);
                    for (AddOrderDetailDto detailDto : addOrderDto.getAddOrderDetailDtos()) {
                        redissonLock.release("Goods" + detailDto.getGoodsId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            //取消订单
            orderService.cancelOrder(userId,param.getOrderId());
            return toSuccess("取消订单成功！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            //订单详情
            OrderDto orderDto = orderService.orderDetail(userId, param.getOrderId());
            return toSuccess(orderDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
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
            @ApiImplicitParam(name = "status", value = "订单状态 0全部 1待支付 2待发货 3待收货 4已完成 5已取消 7退货",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索条件",required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/myOrderList")
    @ResponseBody
    public BaseResp myOrderList(OrderParam param) throws IOException {
        try {
            log.info("----》我的订单列表《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            if(param.getStatus() == null){
                return toError("订单状态不能为空！");
            }
            //我的订单列表
            List<OrderDto> orderDtos = orderService.myOrderList(userId, param.getStart(), param.getLimit(), param.getType(), param.getStatus(),param.getSearch());
            return toSuccess(orderDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }

            //加入分布式锁，锁住商品id，10秒后释放
            try {
                boolean lock = redissonLock.lock("Goods"+param.getGoodsId(), 10);
                if(!lock) {
                    Log.info("分布式锁获取失败");
                    throw new ServiceException("下单失败，您来晚了，宝贝已被抢走了~");
                }
            }catch (Exception e) {
                throw new ServiceException("下单失败，您来晚了，宝贝已被抢走了~");
            }
            //商品/抢购直接下单
            OrderDto orderDto = orderService.saveOrder(param.getToken(),param.getGoodsId(),param.getCouponId(), userId, param.getAddressId(),param.getType());
            return toSuccess(orderDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }finally {
            try{
                redissonLock.release("Goods"+param.getGoodsId());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @ApiOperation(value = "订单支付", notes = "(WechatPatDto、String)订单支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "支付方式  1微信 2支付宝 3余额 4小程序", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/getOrderPay")
    @ResponseBody
    public BaseResp getOrderPay(OrderParam param) throws IOException {
        try {
            log.info("\n" +
                    "                      d*##$.\n" +
                    " zP\"\"\"\"\"$e.           $\"    $o\n" +
                    "4$       '$          $\"      $\n" +
                    "'$        '$        J$       $F\n" +
                    " 'b        $k       $>       $\n" +
                    "  $k        $r     J$       d$\n" +
                    "  '$         $     $\"       $~\n" +
                    "   '$        \"$   '$E       $\n" +
                    "    $         $L   $\"      $F ...\n" +
                    "     $.       4B   $      $$$*\"\"\"*b\n" +
                    "     '$        $.  $$     $$      $F\n" +
                    "      \"$       R$  $F     $\"      $\n" +
                    "       $k      ?$ u*     dF      .$\n" +
                    "       ^$.      $$\"     z$      u$$$$e\n" +
                    "        #$b             $E.dW@e$\"    ?$\n" +
                    "         #$           .o$$# d$$$$c    ?F\n" +
                    "          $      .d$$#\" . zo$>   #$r .uF\n" +
                    "          $L .u$*\"      $&$$$k   .$$d$$F\n" +
                    "           $$\"            \"\"^\"$$$P\"$P9$\n" +
                    "          JP              .o$$$$u:$P $$\n" +
                    "          $          ..ue$\"      \"\"  $\"\n" +
                    "         d$          $F              $\n" +
                    "         $$     ....udE             4B\n" +
                    "          #$    \"\"\"\"` $r            @$\n" +
                    "           ^$L        '$            $F\n" +
                    "             RN        4N           $\n" +
                    "              *$b                  d$\n" +
                    "               $$k                 $F\n" +
                    "               $$b                $F\n" +
                    "                 $\"\"               $F\n" +
                    "                 '$                $\n" +
                    "                  $L               $\n" +
                    "                  '$               $\n" +
                    "                   $               $");
            log.info("----》订单支付《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getOrderId()==null){
                return toError("订单ID不能为空！");
            }
            if(param.getPayType()==null){
                return toError("支付类型不能为空！");
            }
            //订单支付
            Object payInfo = orderService.getOrderPay(userId, param.getOrderId(), param.getPayType(), param.getPayPwd());
            return toSuccess(payInfo);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
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
            //卖家确认发货
            orderService.sendGoods(userId, param.getOrderId(), param.getCompanyId(),param.getNumber());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getOrderId()==null || param.getOrderId().intValue()==0){
                return toError("订单ID不能为空！");
            }
            //买家确认收货
            orderService.getGoods(userId, param.getOrderId());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            //物流公司
            List<CompanyDto> companyDtos = orderService.companyList();
            return toSuccess(companyDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "删除订单", notes = "(void)删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderIds", value = "订单id数组",allowMultiple = true,required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/deleteOrder")
    @ResponseBody
    public BaseResp deleteOrder(OrderParam param) throws IOException {
        try {
            log.info("----》删除订单《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getOrderIds() == null || param.getOrderIds().length < 1){
                return toError("订单不能为空！");
            }
            //删除订单
            orderService.deleteOrder(userId,param.getOrderIds());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "评价卖家", notes = "(void)评价卖家")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsQuality", value = "商品质量 1一颗星 2~3~4~5~",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "serviceAttitude", value = "服务质量 1一颗星 2~3~4~5~",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/evaluationOrder")
    @ResponseBody
    public BaseResp evaluationOrder(OrderParam param) throws IOException {
        try {
            log.info("----》评价卖家《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getOrderId() == null){
                return toError("订单id不能为空！");
            }
            if(param.getGoodsQuality() == null){
                return toError("商品质量不能为空！");
            }
            if(param.getServiceAttitude() == null){
                return toError("服务质量不能为空！");
            }
            //评价卖家
            orderService.evaluationOrder(userId,param.getOrderId(),param.getGoodsQuality(),param.getServiceAttitude());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "提醒发货", notes = "(void)提醒发货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id",required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/reminder")
    @ResponseBody
    public BaseResp reminder(OrderParam param) throws IOException {
        try {
            log.info("----》提醒发货《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getOrderId() == null){
                return toError("订单id不能为空！");
            }
            //提醒发货
            orderService.reminder(userId,param.getOrderId());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "订单支付回调接口", notes = "订单支付", response = BaseResp.class)
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
                boolean result = orderService.updateOrder(wpr.getOutTradeNo(), wpr.getTransactionId(),1);

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

        log.info("-----------支付宝后台通知-----------");
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
            log.warn("无数据返回");
            return "";
        }
        log.warn("支付宝响应报文[订单号:" + params.get("out_trade_no") + "]：" + response);
        String ret = "";
        try {
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            // 商户订单号
            String out_trade_no = new String(request.getParameter(
                    "out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            log.info("商户订单号：" + out_trade_no);
            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no")
                    .getBytes("ISO-8859-1"), "UTF-8");
            log.info("支付宝交易号：" + trade_no);
            // 交易状态
            String trade_status = new String(request.getParameter(
                    "trade_status").getBytes("ISO-8859-1"), "UTF-8");

            log.info("交易状态：" + trade_status);
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            log.info("支付响应报文开始验证");
            if (AlipayNotify.verify(params)) {// 验证成功

                log.info("支付宝支付验证成功[订单号:" + trade_no + "]");
                // ////////////////////////////////////////////////////////////////////////////////////////
                // 请在这里加上商户的业务逻辑程序代码

                // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

                if (trade_status.equals("TRADE_FINISHED")) {

                    log.info("支付宝支付完成！TRADE_FINISHED");
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序
//                    boolean result = orderService.saveNotify(out_trade_no, trade_no,Type.PAYTYPE_ALIPAY.getValue());

                    boolean result = orderService.updateOrder(out_trade_no, trade_no,2);
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

                    log.info("支付宝支付完成！TRADE_SUCCESS");

                    // service.doUpdate(out_trade_no);
                    boolean result = orderService.updateOrder(out_trade_no, trade_no,2);
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
                log.error("支付宝支付验证失败！");
                ret = "fail";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付宝通知后台处理系统出错", e);
            ret = "fail";
        }

        return ret;

    }



    @ApiOperation(value = "验证是否可以购买抢购", notes = "(Boolean)验证是否可以购买抢购")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/verifyFreeAuction")
    @ResponseBody
    public BaseResp verifyFreeAuction(OrderParam param) throws IOException {
        try {
            log.info("----》验证是否可以购买抢购《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            //验证是否可以购买抢购
            Boolean result = orderService.verifyFreeAuction(userId);
            return toSuccess(result);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

}
