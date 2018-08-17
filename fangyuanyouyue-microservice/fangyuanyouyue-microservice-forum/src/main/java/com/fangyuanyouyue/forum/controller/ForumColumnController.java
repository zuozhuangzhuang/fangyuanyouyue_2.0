 package com.fangyuanyouyue.forum.controller;

 import com.alibaba.fastjson.JSONObject;
 import com.fangyuanyouyue.base.BaseController;
 import com.fangyuanyouyue.base.BaseResp;
 import com.fangyuanyouyue.base.enums.ReCode;
 import com.fangyuanyouyue.base.exception.ServiceException;
 import com.fangyuanyouyue.forum.dto.ForumColumnDto;
 import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;
 import com.fangyuanyouyue.forum.param.ForumParam;
 import com.fangyuanyouyue.forum.service.ForumColumnService;
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
@RequestMapping(value = "/column")
@Api(description = "专栏Controller")
@RefreshScope
public class ForumColumnController extends BaseController {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private ForumColumnService forumColumnService;
	@Autowired
	private SchedualUserService schedualUserService;
	@Autowired
	private SchedualRedisService schedualRedisService;

	@ApiOperation(value = "专栏", notes = "获取全部专栏", response = BaseResp.class)
	@PostMapping(value = "/list")
	@ResponseBody
	public BaseResp forumColumn(ForumParam param) throws IOException {
		try {
			log.info("----》获取全部专栏《----");
			log.info("参数：" + param.toString());

			List<ForumColumnTypeDto> dto = forumColumnService.getColumnList(0,1000);

			return toSuccess(dto);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
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
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}


	@ApiOperation(value = "获取专栏分类列表", notes = "获取专栏分类列表", response = BaseResp.class)
	@PostMapping(value = "/getForumTypeList")
	@ResponseBody
	public BaseResp getForumTypeList() throws IOException {
		try {
			log.info("----》获取专栏分类列表《----");
			//获取专栏分类列表
			List<ForumColumnTypeDto> typeList = forumColumnService.getForumTypeList();
			return toSuccess(typeList);
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}

	@ApiOperation(value = "申请专栏", notes = "申请专栏", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "用户token",required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "typeId", value = "专栏分类id",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "专栏名称",required = true, dataType = "String", paramType = "query")
	})
	@PostMapping(value = "/addColumn")
	@ResponseBody
	public BaseResp addColumn(ForumParam param) throws IOException {
		try {
			log.info("----》申请专栏《----");
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
			if(param.getTypeId() == null){
				return toError(ReCode.FAILD.getValue(),"专栏分类id不能为空！");
			}
			if(StringUtils.isEmpty(param.getName())){
				return toError(ReCode.FAILD.getValue(),"专栏名称不能为空！");
            }
			//TODO 申请专栏
			forumColumnService.addColumn(userId,param.getTypeId(),param.getName());

			return toSuccess();
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError(ReCode.FAILD.getValue(), "系统繁忙，请稍后再试！");
		}
	}
}
