package com.fangyuanyouyue.order.controller;


import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.param.OrderParam;
import com.fangyuanyouyue.order.service.TimerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/timer")
@Api(description = "订单定时器系统Controller")
@RefreshScope
public class TimerController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TimerService timerService;


    @ApiOperation(value = "定时取消订单", notes = "(void)定时取消订单",hidden = true)
    @PostMapping(value = "/cancelOrder")
    @ResponseBody
    public BaseResp cancelOrder(OrderParam param) throws IOException {
        try {
            log.info("----》定时取消订单《----");
            //定时取消订单
            timerService.cancelOrder();
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    @ApiOperation(value = "自动收货", notes = "(void)12天自动收货",hidden = true)
    @PostMapping(value = "/saveReceiptGoods")
    @ResponseBody
    public BaseResp saveReceiptGoods(OrderParam param) throws IOException {
        try {
            log.info("----》自动收货《----");
            //自动收货
            timerService.saveReceiptGoods();
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
