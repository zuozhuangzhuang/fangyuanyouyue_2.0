package com.fangyuanyouyue.forum.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.service.TimerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/timer")
@Api(description = "首页定时器Controller",hidden = true)
@RefreshScope
public class TimerController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private TimerService timerService;

    @ApiOperation(value = "结算全民鉴定", notes = "（void）结算全民鉴定",response = BaseResp.class)
    @PostMapping(value = "/appraisalEnd")
    @ResponseBody
    public BaseResp appraisalEnd() throws IOException {
        try {
            log.info("----》结算全民鉴定《----");
            timerService.appraisalEnd();
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
    @ApiOperation(value = "专栏返利", notes = "（void）专栏返利",response = BaseResp.class)
    @PostMapping(value = "/dailyWage")
    @ResponseBody
    public BaseResp dailyWage() throws IOException {
        try {
            log.info("----》专栏返利《----");
            timerService.dailyWage();
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
