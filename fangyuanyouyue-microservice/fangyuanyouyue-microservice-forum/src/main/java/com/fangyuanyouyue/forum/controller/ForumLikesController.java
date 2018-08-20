package com.fangyuanyouyue.forum.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.ForumLikesDto;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumLikesService;
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
@RequestMapping(value = "/likes")
@Api(description = "帖子点赞Controller")
@RefreshScope
public class ForumLikesController extends BaseController {
	protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private SchedualRedisService schedualRedisService;
	@Autowired
	private ForumLikesService forumLikesService;
	@Autowired
    private SchedualUserService schedualUserService;

	@ApiOperation(value = "帖子点赞列表", notes = "（ForumLikesDto）根据id获取帖子点赞列表", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "forumId", value = "帖子id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "start", value = "起始条数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "int", paramType = "query")
	})
	@PostMapping(value = "/list")
	@ResponseBody
	public BaseResp forumLikes(ForumParam param) throws IOException {
		try {
			log.info("----》获取帖子点赞《----");
			log.info("参数：" + param.toString());
			if (param.getForumId() == null) {
				return toError("帖子ID不能为空");
			}
			if (param.getStart() == null || param.getStart() < 0 || param.getLimit() == null || param.getLimit() < 1) {
				return toError("分页参数不能为空");
			}

			List<ForumLikesDto> dto = forumLikesService.getLikesList(param.getForumId(), param.getStart(),param.getLimit());

			return toSuccess(dto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}

	@ApiOperation(value = "帖子点赞", notes = "添加帖子点赞", response = BaseResp.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "forumId", value = "帖子id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1点赞 2取消点赞",required = true, dataType = "int", paramType = "query")
			})
	@PostMapping(value = "/add")
	@ResponseBody
	public BaseResp saveLikes(ForumParam param) throws IOException {
		try {
            log.info("----》帖子点赞《----");
            log.info("参数：" + param.toString());

            //验证用户
            if (StringUtils.isEmpty(param.getToken())) {
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer) schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if ((Integer) jsonObject.get("code") != 0) {
                return toError(jsonObject.getString("report"));
            }

            if (param.getForumId() == null) {
                return toError("帖子ID不能为空");
            }
            if(param.getType() == null){
                return toError("类型不能为空");
            }
            forumLikesService.saveLikes(param.getType(), userId, param.getForumId());

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
