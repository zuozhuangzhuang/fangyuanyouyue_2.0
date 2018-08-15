package com.fangyuanyouyue.forum.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.param.CollectParam;
import com.fangyuanyouyue.forum.service.*;
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
import java.util.List;

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
            @ApiImplicitParam(name = "collectType", value = "收藏类型 3视频 4专栏 5鉴定", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/collect")
    @ResponseBody
    public BaseResp collect(CollectParam param) throws IOException {
        try {
            log.info("----》收藏或取消《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getCollectType() == null){
                return toError(ReCode.FAILD.getValue(),"关注/收藏类型不能为空！");
            }
            if(param.getCollectId() == null){
                return toError(ReCode.FAILD.getValue(),"对象ID不能为空！");
            }
            if(param.getStatus() == null){
                return toError(ReCode.FAILD.getValue(),"状态不能为空！");
            }
            //收藏或取消
            collectService.collect(userId,param.getCollectId(),param.getCollectType(),param.getStatus());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "我的收藏列表 (视频、专栏、鉴定)", notes = "(Object)获取我收藏的专栏、视频、鉴定",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "collectType", value = "关注/收藏类型  3视频 4专栏 5鉴定", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/collectList")
    @ResponseBody
    public BaseResp collectList(CollectParam param) throws IOException {
        try {
            log.info("----》我的收藏列表 (视频、专栏、鉴定)《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getCollectType() == null){
                return toError(ReCode.FAILD.getValue(),"关注/收藏类型不能为空！");
            }
            if(param.getStart() == null || param.getStart() < 0){
                return toError(ReCode.FAILD.getValue(),"起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError(ReCode.FAILD.getValue(),"每页个数错误！");
            }
            List dtos = collectService.collectList(userId, param.getCollectType(),param.getStart(),param.getLimit());;
            return toSuccess(dtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }
}
