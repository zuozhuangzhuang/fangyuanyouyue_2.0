package com.fangyuanyouyue.user.controller;

import java.io.IOException;

import com.fangyuanyouyue.user.service.UserInfoExtService;
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
import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "用户后台管理系统Controller")
@Controller
@RequestMapping(value = "/adminUser")
@RefreshScope
@CrossOrigin
public class AdminController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoExtService userInfoExtService;

    @ApiOperation(value = "用户列表", notes = "用户列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1正常 2冻结 3删除", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
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




    @ApiOperation(value = "实名认证列表", notes = "实名认证列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "实名认证状态 1申请 2通过 3未通过", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/auth/list")
    @ResponseBody
    public BaseResp authList(BasePageReq param) throws IOException {
        try {
            log.info("实名认证列表");
        	log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
        	Pager pager = userInfoExtService.getExtAuthPage(param);
            return toPage(pager);
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "修改实名认证状态", notes = "修改认证状态",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "申请信息id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 2同意 3拒绝",  required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/auth/status")
    @ResponseBody
    public BaseResp authApplyStatus(AdminUserParam param) throws IOException {
        try {
            log.info("修改实名认证状态");
        	log.info(param.toString());
            if(param.getId() == null){
                return toError("申请信息id不能为空");
            }
            if(param.getStatus() == null){
                return toError("处理状态不能为空");
            }
            userInfoExtService.updateExtAuth(param.getId(),param.getStatus(),param.getContent());


            return toSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "认证店铺列表", notes = "认证店铺列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "认证店铺状态 1申请 2认证 3未认证", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/auth/order/list")
    @ResponseBody
    public BaseResp authOrderList(BasePageReq param) throws IOException {
        try {
            log.info("后台管理查看认证店铺列表");
        	log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
        	Pager pager = userInfoExtService.getShopAuthPage(param);
            return toPage(pager);
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改认证店铺状态", notes = "修改认证状态",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "申请信息id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 2同意 3拒绝",  required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/auth/order/status")
    @ResponseBody
    public BaseResp authOrderStatus(AdminUserParam param) throws IOException {
        try {
            log.info("修改认证店铺状态");
        	log.info(param.toString());
        	if(param.getId() == null){
        	    return toError("申请信息id不能为空");
            }
            if(param.getStatus() == null){
        	    return toError("处理状态不能为空");
            }
            userInfoExtService.updateShopAuth(param.getId(),param.getStatus(),param.getContent());


            return toSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


}
