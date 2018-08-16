package com.fangyuanyouyue.forum.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.forum.dto.ForumCommentDto;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumCommentService;
import com.fangyuanyouyue.forum.service.SchedualRedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value = "/comment")
@Api(description = "论坛评论Controller")
@RefreshScope
public class ForumCommentController extends BaseController {
	protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private SchedualRedisService schedualRedisService;
	@Autowired
	private ForumCommentService forumCommentService;

	@ApiOperation(value = "帖子评论列表", notes = "根据帖子id获取评论列表", response = BaseResp.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "forumId", value = "帖子id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "起始条数", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "int", paramType = "query") })
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

			List<ForumCommentDto> dto = forumCommentService.getCommentList(param.getForumId(), param.getStart(),
					param.getLimit());

			return toSuccess(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}


	@ApiOperation(value = "帖子二级评论列表", notes = "根据评论id获取评论列表", response = BaseResp.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "start", value = "起始条数", required = true, dataType = "int", paramType = "query"),
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

			List<ForumCommentDto> dto = forumCommentService.getCommentCommentList(param.getCommentId(), param.getStart(),
					param.getLimit());

			return toSuccess(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}
	
	@ApiOperation(value = "添加评论", notes = "添加评论", response = BaseResp.class)
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

			if (param.getToken() == null) {
				return toError("token不能为空");
			}
			
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            
            if(userId==null) {
            	//TODO 暂时不需要处理
				return toError("用户ID不能为空");
            }

			if (param.getForumId() == null) {
				return toError("帖子ID不能为空");
			}
			if (param.getContent() == null) {
				return toError("内容不能为空");
			}

			forumCommentService.saveComment(userId, param.getForumId(), param.getContent(), param.getCommentId());

			return toSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}
}
