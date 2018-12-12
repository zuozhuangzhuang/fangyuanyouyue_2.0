package com.fangyuanyouyue.forum.controller;

import java.io.IOException;
import java.util.List;

import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.util.ParseReturnValue;
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
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumCommentDto;
import com.fangyuanyouyue.forum.dto.MyForumCommentDto;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumCommentLikesService;
import com.fangyuanyouyue.forum.service.ForumCommentService;
import com.fangyuanyouyue.forum.service.SchedualRedisService;
import com.fangyuanyouyue.forum.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/comment")
@Api(description = "论坛评论Controller")
@RefreshScope
public class ForumCommentController extends BaseController {
	protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private SchedualRedisService schedualRedisService;
	@Autowired
	private ForumCommentService forumCommentService;
	@Autowired
	private SchedualUserService schedualUserService;
	@Autowired
	private ForumCommentLikesService forumCommentLikesService;

	@ApiOperation(value = "帖子评论列表", notes = "（ForumCommentDto）根据帖子id获取评论列表", response = BaseResp.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "forumId", value = "帖子id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "int", paramType = "query")
	})
	@PostMapping(value = "/commentList")
	@ResponseBody
	public BaseResp forumComment(ForumParam param) throws IOException {
		try {
			log.info("----》获取帖子评论《----");
			log.info("参数：" + param.toString());
			if (param.getForumId() == null) {
				return toError("帖子ID不能为空");
			}
			if (param.getStart() == null || param.getLimit() == null) {
				return toError("分页参数不能为空");
			}
			Integer userId = null;
            if(param.getToken()!=null) {
				userId = (Integer)schedualRedisService.get(param.getToken());
            }

			List<ForumCommentDto> dto = forumCommentService.getCommentList(userId,param.getForumId(), param.getStart(),
					param.getLimit());

			return toSuccess(dto);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getCode(),e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError( "系统繁忙，请稍后再试！");
		}
	}


	@ApiOperation(value = "帖子二级评论列表", notes = "（ForumCommentDto）根据评论id获取评论列表", response = BaseResp.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "int", paramType = "query")
	})
	@PostMapping(value = "/replyList")
	@ResponseBody
	public BaseResp forumCommentComment(ForumParam param) throws IOException {
		try {
			log.info("----》获取帖子二级评论《----");
			log.info("参数：" + param.toString());
			if (param.getCommentId() == null) {
				return toError("评论ID不能为空");
			}
			if (param.getStart() == null || param.getLimit() == null) {
				return toError("分页参数不能为空");
			}
			Integer userId = null;
            if(param.getToken()!=null) {
				userId = (Integer)schedualRedisService.get(param.getToken());
            }

			List<ForumCommentDto> dto = forumCommentService.getCommentCommentList(userId,param.getCommentId(), param.getStart(),
					param.getLimit());

			return toSuccess(dto);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getCode(),e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError( "系统繁忙，请稍后再试！");
		}
	}
	
	@ApiOperation(value = "添加评论", notes = "(ForumCommentDto)添加评论", response = BaseResp.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "forumId", value = "帖子id", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "content", value = "评论内容", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "commentId", value = "被回复的评论id", required = false, dataType = "int", paramType = "query") })
	@PostMapping(value = "/add")
	@ResponseBody
	public BaseResp saveComment(ForumParam param) throws IOException {
		try {
			log.info("----》添加评论《----");
			log.info("参数：" + param.toString());

			//验证用户
			if(StringUtils.isEmpty(param.getToken())){
				return toError("用户token不能为空！");
			}
			Integer userId = (Integer)schedualRedisService.get(param.getToken());
			BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
			if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
				return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
			}

			if (param.getForumId() == null) {
				return toError("帖子ID不能为空");
			}
			if (param.getContent() == null) {
				return toError("内容不能为空");
			}

			ForumCommentDto forumCommentDto = forumCommentService.saveComment(userId, param.getForumId(), param.getContent(), param.getCommentId());

			return toSuccess(forumCommentDto);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getCode(),e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError( "系统繁忙，请稍后再试！");
		}
	}


	@ApiOperation(value = "帖子评论点赞", notes = "对帖子评论点赞",response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "commentId", value = "评论id",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型 1点赞 2取消点赞",required = true, dataType = "int", paramType = "query")
	})
	@PostMapping(value = "/comment/likes")
	@ResponseBody
	public BaseResp commentLikes(ForumParam param) throws IOException {
		try {
			log.info("----》对帖子评论点赞《----");
			log.info("参数：" + param.toString());

			//验证用户
			if(StringUtils.isEmpty(param.getToken())){
				return toError("用户token不能为空！");
			}
			Integer userId = (Integer)schedualRedisService.get(param.getToken());
			BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
			if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
				return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
			}
			if(param.getCommentId() == null){
				return toError("评论id不能为空");
			}
			if(param.getType() == null){
				return toError("类型不能为空");
			}
			forumCommentLikesService.saveLikes(param.getType(),userId ,param.getCommentId());
			return toSuccess();
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getCode(),e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}


	@ApiOperation(value = "我的帖子、视频评论列表", notes = "（MyForumCommentDto）我的帖子、视频评论列表", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "类型 1帖子 2视频", required = true, dataType = "int", paramType = "query")
	})
	@PostMapping(value = "/myComments")
	@ResponseBody
	public BaseResp myComments(ForumParam param) throws IOException {
		try {
			log.info("----》我的帖子、视频评论列表《----");
			log.info("参数：" + param.toString());

			//验证用户
			if(StringUtils.isEmpty(param.getToken())){
				return toError("用户token不能为空！");
			}
			Integer userId = (Integer)schedualRedisService.get(param.getToken());
			BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
			if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
				return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
			}
			if (param.getStart() == null || param.getLimit() == null) {
				return toError("分页参数不能为空");
			}
			if(param.getType() == null){
				return toError("类型不能为空");
			}

			List<MyForumCommentDto> dtos = forumCommentService.myComments(userId, param.getStart(),param.getLimit(),param.getType());

			return toSuccess(dtos);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getCode(),e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError( "系统繁忙，请稍后再试！");
		}
	}



	@ApiOperation(value = "删除帖子/视频评论", notes = "删除帖子/视频评论",response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "ids", value = "评论id数组",required = true,allowMultiple = true,dataType = "int", paramType = "query")
	})
	@PostMapping(value = "/deleteForumComment")
	@ResponseBody
	public BaseResp deleteForumComment(ForumParam param) throws IOException {
		try {
			log.info("----》删除帖子/视频评论《----");
			log.info("参数：" + param.toString());
			//验证用户
			if(StringUtils.isEmpty(param.getToken())){
				return toError("用户token不能为空！");
			}
			Integer userId = (Integer)schedualRedisService.get(param.getToken());
			BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
			if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
				return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
			}
			if(param.getIds() == null || param.getIds().length <1){
				return toError("id数组不能为空！");
			}
			//删除视频、帖子评论
			forumCommentService.deleteForumComment(userId,param.getIds());

			return toSuccess();
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getCode(),e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}


}
