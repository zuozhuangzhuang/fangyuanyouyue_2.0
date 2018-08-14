package com.fangyuanyouyue.forum.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumInfoService;
import com.fangyuanyouyue.forum.service.SchedualRedisService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/forum")
@Api(description = "帖子/视频Controller")
@RefreshScope
public class ForumController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    
    @Autowired
    private ForumInfoService forumInfoService;
    
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private SchedualUserService schedualUserService;

    @ApiOperation(value = "帖子详情", notes = "根据id获取帖子详情",response = BaseResp.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "forumId", value = "帖子id",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/detail")
    @ResponseBody
    public BaseResp forumDetail(ForumParam param) throws IOException {
        try {
            log.info("----》获取帖子详情《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            //Integer userId = (Integer)schedualRedisService.get(param.getToken());
            
            //if(userId!=null) {
            	//TODO 暂时不需要处理
            //}
            
            
            if(param.getForumId()==null) {
            	return toError("帖子ID不能为空");
            }
            ForumInfoDto dto = forumInfoService.getForumInfoById(param.getForumId());
            
            if(dto==null) {
            	return toError("找不到帖子");
            }
            
            return toSuccess(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "帖子/视频列表", notes = "获取帖子列表大集合",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "columnId", value = "专栏id",required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id",required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字",required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchType", value = "搜索类型1古物圈子 2专栏精选 ",required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "帖子类型1帖子 2视频",required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/list")
    @ResponseBody
    public BaseResp forumList(ForumParam param) throws IOException {
        try {
            log.info("----》获取帖子列表《----");
            log.info("参数：" + param.toString());
            if(param.getStart()==null||param.getLimit()==null) {
            	return toError("分页参数不能为空");
            }
            
            List<ForumInfoDto> dto = forumInfoService.getForumList(param.getColumnId(),param.getUserId(),param.getType(),param.getKeyword(),param.getStart(),param.getLimit());
            
            return toSuccess(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }




    @ApiOperation(value = "发布帖子/视频", notes = "发布帖子/视频",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "columnId", value = "专栏id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoUrl", value = "视频链接",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoLength", value = "视频长度",required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "videoImg", value = "视频封面图",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "帖子类型 1帖子 2视频",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "邀请用户id数组",allowMultiple = true, dataType = "int", paramType = "query"),
    })
    @PostMapping(value = "/addForum")
    @ResponseBody
    public BaseResp addForum(ForumParam param) throws IOException {
        try {
            log.info("----》发布帖子/视频《----");
            log.info("参数：" + param.toString());
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
            //验证实名认证
            if(JSONObject.parseObject(schedualUserService.isAuth(userId)).getBoolean("data") == false){
                return toError(ReCode.FAILD.getValue(),"用户未实名认证！");
            }
            if(param.getType() == null){
                return toError(ReCode.FAILD.getValue(),"类型不能为空！");
            }
            if(StringUtils.isEmpty(param.getTitle())){
                return toError(ReCode.FAILD.getValue(),"标题不能为空！");
            }
            if(StringUtils.isEmpty(param.getContent())){
                return toError(ReCode.FAILD.getValue(),"内容不能为空！");
            }
            //TODO 发布视频、发布帖子
            forumInfoService.addForum(userId,param.getColumnId(),param.getTitle(),param.getContent(),param.getVideoUrl(),param.getVideoLength(),param.getVideoImg(),param.getType(),param.getUserIds());

            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


}
