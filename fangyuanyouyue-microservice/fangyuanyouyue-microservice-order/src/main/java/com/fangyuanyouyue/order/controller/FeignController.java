package com.fangyuanyouyue.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.param.OrderParam;
import com.fangyuanyouyue.order.service.OrderService;
import com.fangyuanyouyue.order.service.SchedualGoodsService;
import com.fangyuanyouyue.order.service.SchedualRedisService;
import com.fangyuanyouyue.order.service.SchedualUserService;
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
@RequestMapping(value = "/orderFeign")
//@Api(description = "订单系统外部调用Controller",hidden = true)
@RefreshScope
public class FeignController extends BaseController{
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


    @ApiOperation(value = "获取待处理订单", notes = "(OrderDto)生成订单",response = BaseResp.class)
    @PostMapping(value = "/getProcess")
    @ResponseBody
    public BaseResp getProcess(Integer userId,Integer type) throws IOException {
        try {
            log.info("----》生成订单《----");
            log.info("参数：userId："+userId+",type："+type);
            //参数判断
            //验证用户
            if(userId == null){
                return toError(ReCode.FAILD.getValue(),"用户id不能为空！");
            }
            if(type == null){
                return toError(ReCode.FAILD.getValue(),"类型不能为空！");
            }
            //下单商品
            Integer count = orderService.getProcess(userId,type);
            return toSuccess(count);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

}
