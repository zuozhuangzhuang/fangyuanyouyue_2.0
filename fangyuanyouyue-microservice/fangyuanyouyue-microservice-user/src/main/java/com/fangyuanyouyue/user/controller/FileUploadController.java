package com.fangyuanyouyue.user.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.param.UserParam;
import com.fangyuanyouyue.user.service.FileUploadService;
import com.fangyuanyouyue.user.service.SchedualRedisService;
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
@RequestMapping(value = "/file")
@Api(description = "文件上传系统Controller")
@RefreshScope
public class FileUploadController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private SchedualRedisService schedualRedisService;

    @ApiOperation(value = "图片上传", notes = "(String)图片上传",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1头像 2商品、抢购 3视频、帖子 4全民鉴定 5官方鉴定",dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "imgFile", value = "图片，格式为：jpeg，png，jpg",dataType = "file", paramType = "form")
    })
    @PostMapping(value = "/uploadPic")
    @ResponseBody
    public BaseResp uploadPic(UserParam param) throws IOException {
        try {
            log.info("----》图片上传《----");
            log.info("参数：" + param.toString());
            if(param.getImgFile() == null){
                return toError("图片为空");
            }
            String name = param.getImgFile().getOriginalFilename().toLowerCase();
            if(!(name.endsWith("jpeg") || name.endsWith("png")
                    || name.endsWith("jpg"))){
                return toError("请上传JPEG/PNG/JPG格式化图片！");
            }
            Integer userId = null;
            if(StringUtils.isNotEmpty(param.getToken())) {
                userId = (Integer)schedualRedisService.get(param.getToken());
            }
            //图片上传
            String url= fileUploadService.uploadPic(userId,param.getType(),param.getImgFile());
            return toSuccess(url);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "视频上传", notes = "(String)视频上传",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoFile", value = "视频",dataType = "file", paramType = "form")
    })
    @PostMapping(value = "/uploadVideo")
    @ResponseBody
    public BaseResp uploadVideo(UserParam param) throws IOException {
        try {
            log.info("----》视频上传《----");
            log.info("参数：" + param.toString());
            if(param.getVideoFile() == null){
                return toError("文件为空");
            }
            //视频上传
            String url= fileUploadService.uploadVideo(param.getVideoFile());
            return toSuccess(url);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
}
