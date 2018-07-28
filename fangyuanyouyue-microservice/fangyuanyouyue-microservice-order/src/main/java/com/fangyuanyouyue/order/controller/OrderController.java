package com.fangyuanyouyue.order.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.ResultUtil;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.param.OrderParam;
import com.fangyuanyouyue.order.service.OrderService;
import com.fangyuanyouyue.order.service.SchedualGoodsService;
import com.fangyuanyouyue.order.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


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
    //TODO 1.商品加入购物车后单个下单 2.商品加入购物车后打包下单 3.商品不加入购物车直接下单 4.抢购不加入购物车直接下单

    @ApiOperation(value = "商品下单", notes = "(OrderDto)商品下单",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsIds", value = "商品ID数组",allowMultiple = true,required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "收货地址id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/saveOrder")
    @ResponseBody
    public BaseResp saveOrder(OrderParam param) throws IOException {
        try {
            log.info("----》商品下单《----");
            log.info("参数："+param.toString());
            //参数判断
            if(param.getGoodsIds()==null && param.getGoodsIds().length == 0){
                return toError("商品id不能为空！");
            }
            if(param.getAddressId()==null || param.getAddressId().intValue()==0){
                return toError("收货地址id不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
            //TODO 下单商品
            OrderDto orderDto = orderService.saveOrder(param.getToken(),param.getGoodsIds(), userId, param.getAddressId(),param.getType());
            return toSuccess(orderDto);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "取消订单", notes = "(void)取消订单",response = ResultUtil.class)
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
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
            //TODO 取消订单
            orderService.cancelOrder(userId,param.getOrderId());
            return toSuccess("取消订单成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }

    }


    @ApiOperation(value = "订单详情", notes = "(OrderDto)订单详情",response = ResultUtil.class)
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
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
            //TODO 订单详情
            OrderDto orderDto = orderService.orderDetail(userId, param.getOrderId());
            return toSuccess(orderDto);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "我的订单列表", notes = "(OrderDto)我的订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token ", value = "用户token", required = true, dataType = "String", paramType = "query"),
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
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);

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
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
}
