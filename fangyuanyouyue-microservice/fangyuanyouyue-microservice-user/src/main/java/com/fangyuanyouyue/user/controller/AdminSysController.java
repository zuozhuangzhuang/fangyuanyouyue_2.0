package com.fangyuanyouyue.user.controller;

import java.io.IOException;
import java.util.HashMap;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.AppVersionDto;
import com.fangyuanyouyue.user.dto.admin.AdminAppVersionDto;
import com.fangyuanyouyue.user.model.AppVersion;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.SystemService;
import com.fangyuanyouyue.user.service.VersionService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
public class AdminSysController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private SystemService systemService;



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


    @ApiOperation(value = "版本更新列表", notes = "版本更新列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/versionList")
    @ResponseBody
    public BaseResp versionList(AdminUserParam param) throws IOException {
        try {
            log.info("----》版本更新列表《----");
            log.info("参数："+param.toString());
            Pager pager = versionService.getVersionList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "新增版本", notes = "新增版本",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "versionNo", value = "版本号", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "versionDesc", value = "版本描述", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionName", value = "版本名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "apk文件", required = true, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "packageName", value = "安卓包名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通更新 2强制更新", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/versionAdd")
    @ResponseBody
    public BaseResp versionAdd(AdminUserParam param) throws IOException {
        try {
            log.info("----》新增版本《----");
            log.info("参数："+param.toString());
            if(param.getVersionNo() == null){
                return toError("版本号不能为空！");
            }
            if(StringUtils.isEmpty(param.getVersionDesc())){
                return toError("版本描述不能为空！");
            }
            if(StringUtils.isEmpty(param.getVersionName())){
                return toError("版本名称不能为空！");
            }
            if(param.getFile() == null){
                return toError("apk文件不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            versionService.versionAdd(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "修改版本信息", notes = "修改版本信息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "版本文件id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "versionNo", value = "版本号", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "versionDesc", value = "版本描述", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionName", value = "版本名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "apk文件", required = false, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "packageName", value = "安卓包名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通更新 2强制更新", required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/versionModify")
    @ResponseBody
    public BaseResp versionModify(AdminUserParam param) throws IOException {
        try {
            log.info("----》修改版本信息《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("版本文件id不能为空！");
            }
            versionService.versionModify(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "删除版本", notes = "删除版本",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "版本文件id", required = true, dataType = "int", paramType = "query")
    })
    @DeleteMapping(value = "/versionDelete")
    @ResponseBody
    public BaseResp versionDelete(AdminUserParam param) throws IOException {
        try {
            log.info("----》删除版本《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("版本文件id不能为空！");
            }
            versionService.versionDelete(param.getId());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "发送系统消息", notes = "发送系统消息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "消息体", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/sendMessage")
    @ResponseBody
    public BaseResp sendMessage(AdminUserParam param) throws IOException {
        try {
            log.info("----》发送系统消息《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getContent())){
                return toError("消息体不能为空！");
            }
            systemService.sendMessage(param.getContent());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

}
