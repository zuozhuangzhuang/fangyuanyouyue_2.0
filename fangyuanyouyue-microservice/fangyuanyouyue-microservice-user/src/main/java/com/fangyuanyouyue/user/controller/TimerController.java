package com.fangyuanyouyue.user.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.param.UserParam;
import com.fangyuanyouyue.user.service.TimerService;
import com.fangyuanyouyue.user.service.UserInfoService;
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
@Api(description = "用户定时器系统Controller",hidden = true)
@RefreshScope
public class TimerController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TimerService timerService;

    @ApiOperation(value = "官方认证自动过期", notes = "官方认证自动过期")
    @PostMapping(value = "/shopAuthTimeOut")
    @ResponseBody
    public BaseResp shopAuthTimeOut() throws IOException {
        try {
            log.info("----》官方认证自动过期《----");
            timerService.shopAuthTimeOut();
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "每日统计数据", notes = "每日统计数据")
    @PostMapping(value = "/dailyStatistics")
    @ResponseBody
    public BaseResp dailyStatistics() throws IOException {
        try {
            log.info("----》每日统计数据《----");
            timerService.dailyStatistics();
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
