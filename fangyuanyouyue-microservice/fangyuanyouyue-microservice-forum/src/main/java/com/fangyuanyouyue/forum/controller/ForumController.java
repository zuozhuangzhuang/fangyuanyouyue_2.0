package com.fangyuanyouyue.forum.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.ResultUtil;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/forum")
@Api( tags = {"帖子/视频Controller"})
@RefreshScope
public class ForumController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    
    @Autowired
    private ForumInfoService forumInfoService;

    @ApiOperation(value = "帖子详情", notes = "根据id获取帖子详情",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "forumId", value = "帖子id",required = true, dataType = "Integer", paramType = "query")
    })
    @PostMapping(value = "/detail")
    @ResponseBody
    public BaseResp forumDetail(ForumParam param) throws IOException {
        try {
            log.info("----》获取帖子详情《----");
            log.info("参数：" + param.toString());
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


    @ApiOperation(value = "帖子列表", notes = "根据专栏id获取帖子列表",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "columnId", value = "专栏id",required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "Integer", paramType = "query")
    })
    @PostMapping(value = "/list")
    @ResponseBody
    public BaseResp forumList(ForumParam param) throws IOException {
        try {
            log.info("----》获取帖子详情《----");
            log.info("参数：" + param.toString());
            if(param.getColumnId()==null) {
            	return toError("专栏ID不能为空");
            }
            if(param.getStart()==null||param.getLimit()==null) {
            	return toError("分页参数不能为空");
            }
            
            List<ForumInfoDto> dto = forumInfoService.getForumList(param.getColumnId(),param.getStart(),param.getLimit());
            
            return toSuccess(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }



}
