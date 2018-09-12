package com.fangyuanyouyue.user.controller;

import java.io.IOException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

@Api(description = "用户后台管理系统Controller")
@Controller
@RequestMapping(value = "/admin/user")
@RefreshScope
@CrossOrigin
public class AdminUserController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "用户列表", notes = "用户列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "receiverName", value = "收货人姓名",  required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "receiverPhone", value = "联系电话",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "province", value = "省",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "市",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "area", value = "区",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "详细收货地址",  required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "postCode", value = "邮编",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1默认地址 2其他",dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResp list(AdminUserParam param) throws IOException {
        try {
            log.info("后台管理查看用户列表");
        	log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
        	Pager pager = userInfoService.getPage(param);
            return toPage(pager);
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改用户状态", notes = "修改用户状态",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1正常 2冻结 3删除",  required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/delete")
    @ResponseBody
    public BaseResp delete(AdminUserParam param) throws IOException {
        try {
            log.info("后台管理修改用户");
        	log.info(param.toString());
        	userInfoService.upateUserStatus(param.getId(), param.getStatus());
            return toSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    

}
