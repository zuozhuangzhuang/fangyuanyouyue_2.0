package com.fangyuanyouyue.forum.controller;

import java.io.IOException;
import java.util.List;

import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.param.CollectParam;
import com.fangyuanyouyue.forum.service.AppraisalCommentLikesService;
import com.fangyuanyouyue.forum.service.AppraisalCommentService;
import com.fangyuanyouyue.forum.service.AppraisalDetailService;
import com.fangyuanyouyue.forum.service.AppraisalLikesService;
import com.fangyuanyouyue.forum.service.CollectService;
import com.fangyuanyouyue.forum.service.SchedualRedisService;
import com.fangyuanyouyue.forum.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/collect")
@Api(description = "收藏系统Controller")
@RefreshScope
public class CollectController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private AppraisalDetailService appraisalDetailService;
    @Autowired
    private AppraisalCommentService appraisalCommentService;
    @Autowired
    private AppraisalCommentLikesService appraisalCommentLikesService;
    @Autowired
    private AppraisalLikesService appraisalLikesService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private CollectService collectService;

    @ApiOperation(value = "收藏或取消", notes = "(void)收藏或取消",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "collectId", value = "收藏对象ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1收藏 2取消收藏", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "collectType", value = "收藏类型 3视频 4帖子 5鉴定", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/collect")
    @ResponseBody
    public BaseResp collect(CollectParam param) throws IOException {
        try {
            log.info("----》收藏或取消《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getCollectType() == null){
                return toError("关注/收藏类型不能为空！");
            }
            if(param.getCollectId() == null){
                return toError("对象ID不能为空！");
            }
            if(param.getStatus() == null){
                return toError("状态不能为空！");
            }
            //收藏或取消
            collectService.collect(userId,param.getCollectId(),param.getCollectType(),param.getStatus());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "我的收藏列表 (视频、专栏、鉴定)", notes = "(Object)获取我收藏的专栏、视频、鉴定",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "collectType", value = "关注/收藏类型  3视频 4帖子 5鉴定", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索内容", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/collectList")
    @ResponseBody
    public BaseResp collectList(CollectParam param) throws IOException {
        try {
            log.info("----》我的收藏列表 (视频、帖子、鉴定)《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getCollectType() == null){
                return toError("关注/收藏类型不能为空！");
            }
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            List dtos = collectService.collectList(userId, param.getCollectType(),param.getStart(),param.getLimit(),param.getSearch());
            return toSuccess(dtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


}
