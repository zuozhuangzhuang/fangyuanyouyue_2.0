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
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumColumnService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/column")
@Api(description = "专栏Controller")
@RefreshScope
public class ForumColumnController extends BaseController {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private ForumColumnService forumColumnService;

	@ApiOperation(value = "专栏", notes = "获取全部专栏", response = BaseResp.class)
	@PostMapping(value = "/list")
	@ResponseBody
	public BaseResp forumColumn(ForumParam param) throws IOException {
		try {
			log.info("----》获取全部专栏《----");
			log.info("参数：" + param.toString());

			List<ForumColumnTypeDto> dto = forumColumnService.getColumnList(0,1000);

			return toSuccess(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}


	@ApiOperation(value = "精选专栏", notes = "获取精选专栏", response = BaseResp.class)
	@PostMapping(value = "/chosen")
	@ResponseBody
	public BaseResp chosenColumn(ForumParam param) throws IOException {
		try {
			log.info("----》获取精选的专栏《----");
			log.info("参数：" + param.toString());

			List<ForumColumnDto> dto = forumColumnService.getChosenColumnList();

			return toSuccess(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}
}
