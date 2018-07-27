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
import com.fangyuanyouyue.forum.dto.ForumCommentDto;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumCommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/comment")
@Api(tags = "论坛评论Controller")
@RefreshScope
public class ForumCommentController extends BaseController {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private ForumCommentService forumCommentService;

	@ApiOperation(value = "帖子评论", notes = "根据id获取论坛详情", response = ResultUtil.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "forumId", value = "帖子id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "起始条数", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "Integer", paramType = "query") })
	@PostMapping(value = "/list")
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

	@ApiOperation(value = "添加评论", notes = "添加评论", response = ResultUtil.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "forumId", value = "帖子id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "content", value = "评论内容", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "commentId", value = "被回复的评论id", required = false, dataType = "Integer", paramType = "query") })
	@PostMapping(value = "/add")
	@ResponseBody
	public BaseResp saveComment(ForumParam param) throws IOException {
		try {
			log.info("----》获取帖子评论《----");
			log.info("参数：" + param.toString());
			if (param.getForumId() == null) {
				return toError("帖子ID不能为空");
			}
			if (param.getContent() == null) {
				return toError("内容不能为空");
			}

			forumCommentService.saveComment(param.getUserId(), param.getForumId(), param.getContent(), param.getCommentId());

			return toSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}
}
