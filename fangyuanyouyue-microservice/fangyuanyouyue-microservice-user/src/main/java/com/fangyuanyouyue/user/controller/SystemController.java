package com.fangyuanyouyue.user.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.AppVersionDto;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.param.UserParam;
import com.fangyuanyouyue.user.service.SchedualRedisService;
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
    @Autowired
    private SchedualRedisService schedualRedisService;

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
                return toError(ReCode.LOGIN_TIME_OUT.getValue(),ReCode.LOGIN_TIME_OUT.getMessage());
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FROZEN.getValue(),ReCode.FROZEN.getMessage());
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
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
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
            return toError(e.getCode(),e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "获取小程序二维码", notes = "获取小程序二维码",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1抢购 2商品 3帖子 4个人店铺",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/getQRCode")
    @ResponseBody
    public BaseResp getQRCode(UserParam param) throws IOException {
        try {
            log.info("----》获取小程序二维码《----");
            Integer userId = null;
            if(StringUtils.isNotEmpty(param.getToken())) {
                userId = (Integer)schedualRedisService.get(param.getToken());
            }
            String qrCode= systemService.getQRCode(userId,param.getId(),param.getType());

            return toSuccess(qrCode);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "获取小程序二维码路径", notes = "获取小程序二维码路径",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1抢购 2商品 3帖子 4个人店铺",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/getQRCodeUrl")
    @ResponseBody
    public BaseResp getQRCodeUrl(UserParam param) throws IOException {
        try {
            log.info("----》获取小程序二维码路径《----");
            Integer userId = null;
            if(StringUtils.isNotEmpty(param.getToken())) {
                userId = (Integer)schedualRedisService.get(param.getToken());
            }
            String qrCode= systemService.getQRCodeUrl(userId,param.getId(),param.getType());

            return toSuccess(qrCode);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "小程序二维码开关", notes = "小程序二维码开关",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "开关 1打开 2关闭",required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/updateQRSwitch")
    @ResponseBody
    public BaseResp updateQRSwitch(UserParam param) throws IOException {
        try {
            log.info("----》小程序二维码开关《----");
            if(param.getStatus() == null){
                return toError("开关状态不能为空！");
            }
            systemService.updateQRSwitch(param.getStatus());

            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "邀请活动开关", notes = "邀请活动开关",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "开关 1打开 2关闭",required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/updateInviteSwitch")
    @ResponseBody
    public BaseResp updateInviteSwitch(UserParam param) throws IOException {
        try {
            log.info("----》邀请活动开关《----");
            if(param.getStatus() == null){
                return toError("开关状态不能为空！");
            }
            systemService.updateInviteSwitch(param.getStatus());

            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

}
