package com.fangyuanyouyue.user.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.ConfinedUserService;
import com.fangyuanyouyue.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Api(description = "代理后台管理系统Controller")
@Controller
@RequestMapping(value = "/confined")
@RefreshScope
public class AdminConfineController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ConfinedUserService confinedUserService;
    //设置、取消代理
    //查看代理列表

    @ApiOperation(value = "设置、取消代理", notes = "设置、取消代理",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "操作类型 1添加代理 2取消代理",  required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "代理识别号",  required = false,dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateConfined")
    @ResponseBody
    public BaseResp updateConfined(AdminUserParam param) throws IOException {
        try {
            log.info("设置、取消代理");
            log.info(param.toString());
            if(param.getId() == null){
                return toError("用户id不能为空！");
            }
            if(param.getType() == null){
                return toError("操作类型不能为空！");
            }
            confinedUserService.updateConfined(param.getId(), param.getType(), param.getCode());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


}
