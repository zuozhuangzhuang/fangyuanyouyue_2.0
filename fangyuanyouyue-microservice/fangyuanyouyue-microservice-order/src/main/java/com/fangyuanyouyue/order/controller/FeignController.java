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


//    @ApiOperation(value = "生成订单", notes = "(OrderDto)生成订单",response = BaseResp.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "goodsIds", value = "商品ID数组",allowMultiple = true,required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "addressId", value = "收货地址id", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品",required = true, dataType = "int", paramType = "query")
//    })
    @PostMapping(value = "/saveOrder")
    @ResponseBody
    public BaseResp saveOrder(OrderParam param) throws IOException {
        try {
            log.info("----》生成订单《----");
            log.info("参数："+param.toString());
            //参数判断
            if(param.getGoodsIds()==null && param.getGoodsIds().length == 0){
                return toError("商品id不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
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
            //TODO 下单商品
            OrderDto orderDto = orderService.saveOrder(param.getToken(),param.getGoodsIds(), userId, param.getAddressId(),param.getType());
            return toSuccess(orderDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

}
