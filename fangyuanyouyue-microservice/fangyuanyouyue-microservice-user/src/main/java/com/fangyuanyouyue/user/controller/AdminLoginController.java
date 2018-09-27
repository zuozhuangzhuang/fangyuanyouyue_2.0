package com.fangyuanyouyue.user.controller;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.user.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "用户后台管理系统Controller")
@Controller
@RequestMapping(value = "/system")
@RefreshScope
public class AdminLoginController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;



    @ApiOperation(value = "登录", notes = "用户登录",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginCode", value = "登录账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "登录密码",  required = true,dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/login")
    @ResponseBody
    public BaseResp delete(String loginCode,String password) throws IOException {
        try {
            log.info("后台后台登录 - "+loginCode);
            
            //测试登录
            if("admin".equals(loginCode) && "123456".equals(password)) {
            	HashMap map = new HashMap<String,String>();
            	map.put("token", "abcdefg");
                return toSuccess(map);
            }else {
            	return toError("账号或密码有误");
            }
            
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

}
