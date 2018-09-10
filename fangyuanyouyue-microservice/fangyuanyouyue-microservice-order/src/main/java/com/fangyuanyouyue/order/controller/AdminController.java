package com.fangyuanyouyue.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.param.OrderParam;
import com.fangyuanyouyue.order.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/adminOrder")
@Api(description = "订单系统后台管理相关Controller")
@RefreshScope
public class AdminController extends BaseController{
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
    @Autowired
    private RefundService refundService;


//    @ApiOperation(value = "查看所有用户订单", notes = "(OrderDto)查看订单",response = BaseResp.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "类型 1买家（我买下的） 2卖家（我卖出的）", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "status", value = "订单状态 0全部 1待支付 2待发货 3待收货 4已完成 5已取消 7退货",required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "search", value = "搜索条件",required = false, dataType = "String", paramType = "query")
//    })
//    @PostMapping(value = "/saveOrderByCart")
//    @ResponseBody
//    public BaseResp saveOrderByCart(OrderParam param) throws IOException {
//        try {
//            log.info("----》查看所有用户订单《----");
//            log.info("参数："+param.toString());
//            //参数判断
//            //验证用户
//            if(param.getStart() == null || param.getStart() < 0){
//                return toError("起始页数错误！");
//            }
//            if(param.getLimit() == null || param.getLimit() < 1){
//                return toError("每页个数错误！");
//            }
//            if(param.getType() == null){
//                return toError("类型不能为空！");
//            }
//            //购物车商品下单
//            List<OrderDto> orderDtos = orderService.myOrderList(null, param.getStart(), param.getLimit(), param.getType(), param.getStatus(),param.getSearch());
//            return toSuccess(orderDtos);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            return toError(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }

    //TODO 查看订单详情


    @ApiOperation(value = "官方处理退货", notes = "(void)官方处理退货")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "处理理由", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 2同意 3拒绝",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/platformDealReturns")
    @ResponseBody
    public BaseResp platformDealReturns(OrderParam param) throws IOException {
        try {
            log.info("----》官方处理退货《----");
            log.info("参数："+param.toString());
            //验证用户
//            if(StringUtils.isEmpty(param.getToken())){
//                return toError("用户token不能为空！");
//            }
//            Integer userId = (Integer)schedualRedisService.get(param.getToken());
//            String verifyUser = schedualUserService.verifyUserById(userId);
//            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
//            if((Integer)jsonObject.get("code") != 0){
//                return toError(jsonObject.getString("report"));
//            }
            if(param.getOrderId() == null){
                return toError("订单id不能为空！");
            }
            if(param.getStatus() == null){
                return toError("处理状态不能为空！");
            }
            //官方处理退货
            refundService.platformDealReturns(null,param.getOrderId(),param.getReason(),param.getStatus());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
}
