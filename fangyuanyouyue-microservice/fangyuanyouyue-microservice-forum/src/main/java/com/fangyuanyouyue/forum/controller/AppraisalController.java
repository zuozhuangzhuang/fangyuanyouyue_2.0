package com.fangyuanyouyue.forum.controller;

import java.io.IOException;
import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.forum.dto.AppraisalCommentDto;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.param.AppraisalParam;
import com.fangyuanyouyue.forum.service.AppraisalCommentLikesService;
import com.fangyuanyouyue.forum.service.AppraisalCommentService;
import com.fangyuanyouyue.forum.service.AppraisalDetailService;
import com.fangyuanyouyue.forum.service.AppraisalLikesService;
import com.fangyuanyouyue.forum.service.SchedualRedisService;
import com.fangyuanyouyue.forum.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/appraisal")
@Api(description = "全民鉴定Controller")
@RefreshScope
public class AppraisalController extends BaseController {
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

    @ApiOperation(value = "全民鉴定详情", notes = "（AppraisalDetailDto）根据id获取全民鉴定详情",response = BaseResp.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "appraisalId", value = "帖子id",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/detail")
    @ResponseBody
    public BaseResp appraisalDetail(AppraisalParam param) throws IOException {
        try {
            log.info("----》获取全民鉴定详情《----");
            log.info("参数：" + param.toString());
			Integer userId = null;
            if(param.getToken()!=null) {
                userId = (Integer)schedualRedisService.get(param.getToken());
                String verifyUser = schedualUserService.verifyUserById(userId);
                JSONObject jsonObject = JSONObject.parseObject(verifyUser);
                if((Integer)jsonObject.get("code") != 0){
                    return toError(jsonObject.getString("report"));
                }
            }
            if(param.getAppraisalId()==null) {
            	return toError("鉴定ID不能为空");
            }
            AppraisalDetailDto dto = appraisalDetailService.getAppraisalDetail(userId,param.getAppraisalId());
            
            if(dto==null) {
            	return toError("找不到该鉴定");
            }
            
            return toSuccess(dto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "全民鉴定列表", notes = "（AppraisalDetailDto）获取全民鉴定列表大集合",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字",required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1我参与的 2我发起的(为空则是普通列表)",required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/list")
    @ResponseBody
    public BaseResp appraisalList(AppraisalParam param) throws IOException {
        try {
            log.info("----》获取全民鉴定列表《----");
            log.info("参数：" + param.toString());
            if(param.getStart()==null||param.getLimit()==null) {
            	return toError("分页参数不能为空");
            }
            Integer userId = null;
            if(param.getToken()!=null) {
                userId = (Integer)schedualRedisService.get(param.getToken());
                String verifyUser = schedualUserService.verifyUserById(userId);
                JSONObject jsonObject = JSONObject.parseObject(verifyUser);
                if((Integer)jsonObject.get("code") != 0){
                    return toError(jsonObject.getString("report"));
                }
            }
            List<AppraisalDetailDto> dto = appraisalDetailService.getAppraisalList(userId,param.getKeyword(),param.getStart(), param.getLimit(),param.getType());
            
            return toSuccess(dto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    

    @ApiOperation(value = "全民鉴定评论列表", notes = "（AppraisalCommentDto）获取全民鉴定评论列表",response = BaseResp.class)
    @ApiImplicitParams({ 
    		@ApiImplicitParam(name = "token", value = "用户token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appraisalId", value = "鉴定id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/comment/list")
    @ResponseBody
    public BaseResp commentList(AppraisalParam param) throws IOException {
        try {
            log.info("----》获取全民鉴定评论列表《----");
            log.info("参数：" + param.toString());

            if(param.getAppraisalId()==null) {
            	return toError("鉴定ID不能为空");
            }
            
            if(param.getStart()==null||param.getLimit()==null) {
            	return toError("分页参数不能为空");
            }
            Integer userId = null;
            if(StringUtils.isNotEmpty(param.getToken())) {
                userId = (Integer)schedualRedisService.get(param.getToken());
                String verifyUser = schedualUserService.verifyUserById(userId);
                JSONObject jsonObject = JSONObject.parseObject(verifyUser);
                if((Integer)jsonObject.get("code") != 0){
                    return toError(jsonObject.getString("report"));
                }

            }
            List<AppraisalCommentDto> dtos = appraisalCommentService.getAppraisalCommentList(userId,param.getAppraisalId(), param.getStart(), param.getLimit());
            
            return toSuccess(dtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    @ApiOperation(value = "全民鉴定点赞", notes = "对全民鉴定点赞",response = BaseResp.class)
    @ApiImplicitParams({ 
    		@ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appraisalId", value = "鉴定id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1点赞 2取消点赞",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/likes")
    @ResponseBody
    public BaseResp likes(AppraisalParam param) throws IOException {
        try {
            log.info("----》对全民鉴定点赞《----");
            log.info("参数：" + param.toString());

            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
			
            if(param.getAppraisalId()==null) {
            	return toError("鉴定ID不能为空");
            }
            
            appraisalLikesService.saveLikes(userId, param.getAppraisalId(),param.getType());
            
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }




    @ApiOperation(value = "全民鉴定评论点赞", notes = "对全民鉴定评论点赞",response = BaseResp.class)
    @ApiImplicitParams({ 
    		@ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "commentId", value = "评论id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1点赞 2取消点赞",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/comment/likes")
    @ResponseBody
    public BaseResp commentLikes(AppraisalParam param) throws IOException {
        try {
            log.info("----》对全民鉴定评论点赞《----");
            log.info("参数：" + param.toString());

			if (param.getToken() == null) {
				return toError("token不能为空");
			}

            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
			
            if(param.getCommentId()==null) {
            	return toError("评论ID不能为空");
            }
            
            appraisalCommentLikesService.saveLikes(userId, param.getCommentId(),param.getType());
            
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "发起鉴定", notes = "发起鉴定",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bonus", value = "鉴定赏金",required = false, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "商品描述", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrls", value = "图片数组", allowMultiple = true,required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "邀请用户id数组", allowMultiple = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/addAppraisal")
    @ResponseBody
    public BaseResp addAppraisal(AppraisalParam param) throws IOException {
        try {
            log.info("----》发起鉴定《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            //验证实名认证
            if(JSONObject.parseObject(schedualUserService.isAuth(userId)).getBoolean("data") == false){
                return toError("用户未实名认证！");
            }
            if(StringUtils.isEmpty(param.getTitle())){
                return toError("标题不能为空！");
            }
            if(param.getImgUrls() == null || param.getImgUrls().length < 1){
                return toError("请至少包含一张图片！");
            }
            //发起鉴定
            appraisalDetailService.addAppraisal(userId,param.getBonus(),param.getTitle(),param.getContent(),param.getImgUrls(),param.getUserIds());

            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    @ApiOperation(value = "添加评论", notes = "添加评论",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appraisalId", value = "鉴定id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "viewpoint", value = "评论观点 1看真 2看假",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "我的看法", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "邀请用户数组", allowMultiple = true,required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/saveComment")
    @ResponseBody
    public BaseResp saveComment(AppraisalParam param) throws IOException {
        try {
            log.info("----》添加评论《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getAppraisalId() == null){
                return toError("鉴定id不能为空！");
            }
            if(param.getViewpoint() == null){
                return toError("评论观点不能为空！");
            }
            if(StringUtils.isEmpty(param.getContent())){
                return toError("看法不能为空！");
            }
            //添加评论
            appraisalCommentService.saveComment(userId,param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "邀请好友", notes = "邀请好友",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appraisalId", value = "鉴定id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "邀请用户数组", allowMultiple = true,required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/invite")
    @ResponseBody
    public BaseResp invite(AppraisalParam param) throws IOException {
        try {
            log.info("----》邀请好友《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getAppraisalId() == null){
                return toError("鉴定id不能为空！");
            }
            if(param.getUserIds() == null || param.getUserIds().length < 1){
                return toError("邀请用户不能为空！");
            }
            //邀请好友
            appraisalDetailService.invite(userId,param.getAppraisalId(),param.getUserIds());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


}
