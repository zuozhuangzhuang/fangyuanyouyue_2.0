package com.fangyuanyouyue.user.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.AppVersionDto;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.param.UserParam;
import com.fangyuanyouyue.user.service.SystemService;
import com.fangyuanyouyue.user.service.UserInfoService;
import com.fangyuanyouyue.user.service.VersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping(value = "/system")
@Api(description = "系统相关Controller")
@RefreshScope
public class SystemController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private VersionService versionService;

    @ApiOperation(value = "意见反馈", notes = "(void)意见反馈",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "反馈内容",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1安卓 2iOS 3小程序",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本",required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/feedback")
    @ResponseBody
    public BaseResp feedback(UserParam param) throws IOException {
        try {
            log.info("----》意见反馈《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            if(StringUtils.isEmpty(param.getContent())){
                return toError("反馈信息不能为空！");
            }
            if(param.getType() == null){
                return toError("登录类型不能为空！");
            }
            if(StringUtils.isEmpty(param.getVersion())){
                return toError("系统版本不能为空！");
            }
            systemService.feedback(user.getId(),param.getContent(),param.getType(),param.getVersion());
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "版本更新", notes = "版本更新",response = BaseResp.class)
    @GetMapping(value = "/versionUpdate")
    @ResponseBody
    public BaseResp versionUpdate() throws IOException {
        try {
            log.info("----》版本更新《----");

            AppVersionDto dto= versionService.getVersion();

            return toSuccess(dto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
}
