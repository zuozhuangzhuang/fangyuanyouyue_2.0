 package com.fangyuanyouyue.forum.controller;

 import com.fangyuanyouyue.base.BaseController;
 import com.fangyuanyouyue.base.BaseResp;
 import com.fangyuanyouyue.base.enums.ReCode;
 import com.fangyuanyouyue.forum.dto.ForumColumnDto;
 import com.fangyuanyouyue.forum.param.ForumParam;
 import com.fangyuanyouyue.forum.service.ForumColumnService;
 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiImplicitParam;
 import io.swagger.annotations.ApiImplicitParams;
 import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value = "/column")
@Api(description = "专栏Controller")
@RefreshScope
public class ForumColumnController extends BaseController {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private ForumColumnService forumColumnService;

	@ApiOperation(value = "专栏", notes = "获取专栏列表", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start", value = "起始条数", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "int", paramType = "query") })
	@PostMapping(value = "/list")
	@ResponseBody
	public BaseResp forumColumn(ForumParam param) throws IOException {
		try {
			log.info("----》获取专栏列表《----");
			log.info("参数：" + param.toString());
			if (param.getStart() == null || param.getLimit() == null) {
				return toError("分页参数不能为空");
			}

			List<ForumColumnDto> dto = forumColumnService.getColumnList(param.getStart(), param.getLimit());

			return toSuccess(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}

	
}
