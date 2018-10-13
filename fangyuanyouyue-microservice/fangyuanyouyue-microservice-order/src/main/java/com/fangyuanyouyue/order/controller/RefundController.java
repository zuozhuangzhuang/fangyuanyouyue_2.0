package com.fangyuanyouyue.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.order.dto.OrderRefundDto;
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

@RestController
@RequestMapping(value = "/refund")
@Api(description = "退货系统Controller")
@RefreshScope
public class RefundController extends BaseController{
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


    @ApiOperation(value = "退货申请", notes = "(void)退货申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "退货理由",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrls", value = "图片路径数组",allowMultiple = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/orderReturnToSeller")
    @ResponseBody
    public BaseResp orderReturnToSeller(OrderParam param) throws IOException {
        try {
            log.info("----》退货申请《----");
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

            if(StringUtils.isEmpty(param.getReason())){
                return toError("退货理由不能为空！");
            }
            //退货申请
            refundService.orderReturnToSeller(userId,param.getOrderId(),param.getReason(),param.getImgUrls());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "退货详情", notes = "(OrderRefundDto)退货详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/orderReturnDetail")
    @ResponseBody
    public BaseResp orderReturnDetail(OrderParam param) throws IOException {
        try {
            log.info("----》退货详情《----");
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
            //退货详情
            OrderRefundDto orderRefundDto = refundService.orderReturnDetail(userId, param.getOrderId());
            return toSuccess(orderRefundDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }





    @ApiOperation(value = "卖家处理退货", notes = "(void)卖家处理退货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "处理理由", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 2同意 3拒绝",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/handleReturns")
    @ResponseBody
    public BaseResp handleReturns(OrderParam param) throws IOException {
        try {
            log.info("----》卖家处理退货《----");
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

            if(param.getStatus() == null){
                return toError("处理状态不能为空！");
            }
            //卖家处理退货
            refundService.handleReturns(userId,param.getOrderId(),param.getReason(),param.getStatus());
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
