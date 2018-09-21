package com.fangyuanyouyue.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping(value = "/timer")
@Api(description = "钱包定时器系统Controller")
@RefreshScope
public class TimerController extends BaseController {

    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private PointGoodsService pointGoodsService;
    @Autowired
    private PointOrderService pointOrderService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private TimerService timerService;

    @ApiOperation(value = "定时更新用户等级", notes = "(void)定时更新用户等级",response = BaseResp.class)
    @PostMapping(value = "/updateLevel")
    @ResponseBody
    public BaseResp updateLevel(WalletParam param) throws IOException {
        try {
            log.info("----》定时更新用户等级《----");
            //定时更新用户等级
            timerService.updateLevel();
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "会员自动到期", notes = "(void)会员自动到期",response = BaseResp.class)
    @PostMapping(value = "/cancelVip")
    @ResponseBody
    public BaseResp cancelVip() throws IOException {
        try {
            log.info("----》会员自动到期《----");
            //会员自动到期
            timerService.cancelVip();
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "送优惠券", notes = "(void)送优惠券",response = BaseResp.class)
    @PostMapping(value = "/sendCoupon")
    @ResponseBody
    public BaseResp sendCoupon() throws IOException {
        try {
            log.info("----》送优惠券《----");
            timerService.sendCoupon();
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
