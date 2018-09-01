package com.fangyuanyouyue.user.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.UserInfoService;

@Controller
@RequestMapping(value = "/admin/user")
@RefreshScope
@CrossOrigin
public class AdminUserController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResp list(AdminUserParam param) throws IOException {
        try {
        	log.info(param.toString());
        	Pager pager = userInfoService.getPage(param);
            return toPage(pager);
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public BaseResp delete(AdminUserParam param) throws IOException {
        try {
        	log.info(param.toString());
        	userInfoService.upateUserStatus(param.getId(), param.getStatus());
            return toSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    

}
