package com.fangyuanyouyue.goods.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.service.*;
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
@Api(description = "商品定时器系统Controller",hidden = true)
@RefreshScope
public class TimerController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private SchedualOrderService schedualOrderService;
    @Autowired
    private TimerService timerService;


    @ApiOperation(value = "抢购定时降价", notes = "(void)抢购定时降价",response = BaseResp.class,hidden = true)
    @PostMapping(value = "/depreciate")
    @ResponseBody
    public BaseResp depreciate() throws IOException {
        try {
            log.info("----》抢购定时降价《----");
            timerService.depreciate();
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "议价定时拒绝", notes = "(void)议价定时拒绝",response = BaseResp.class,hidden = true)
    @PostMapping(value = "/refuseBargain")
    @ResponseBody
    public BaseResp refuseBargain() throws IOException {
        try {
            log.info("----》议价定时拒绝《----");
            timerService.refuseBargain();
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
