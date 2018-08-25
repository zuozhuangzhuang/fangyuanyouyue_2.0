package com.fangyuanyouyue.forum.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumColumnService;
import com.fangyuanyouyue.forum.service.SchedualRedisService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
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

@RestController
@RequestMapping(value = "/appraisal")
@Api(description = "首页后台管理Controller")
@RefreshScope
public class AdminController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private ForumColumnService forumColumnService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;


	@ApiOperation(value = "专栏申请列表", notes = "专栏申请列表", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start", value = "用户id",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "专栏申请id",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "keyword", value = "关键字搜索",required = true, dataType = "String", paramType = "query")
	})
	@PostMapping(value = "/applyList")
	@ResponseBody
	public BaseResp applyList(ForumParam param) throws IOException {
		try {
			log.info("----》专栏申请列表《----");
			log.info("参数：" + param.toString());
            if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
                return toError("分页参数错误");
            }
			//专栏申请列表
			forumColumnService.applyList(param.getStart(),param.getLimit(),param.getKeyword());

			return toSuccess();
		} catch (ServiceException e) {
			e.printStackTrace();
			return toError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}

    @ApiOperation(value = "后台处理专栏申请", notes = "后台处理专栏申请", response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "applyId", value = "专栏申请id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 1同意 2拒绝",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coverImgUrl", value = "封面图片地址（同意必填）",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "拒绝理由（拒绝必填）",required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/handle")
    @ResponseBody
    public BaseResp handle(ForumParam param) throws IOException {
        try {
            log.info("----》后台处理专栏申请《----");
            log.info("参数：" + param.toString());
            if(param.getApplyId() == null){
                return toError("申请id不能为空！");
            }
            if(param.getStatus() == null){
                return toError("处理状态不能为空！");
            }
            //处理专栏申请
            forumColumnService.handle(param.getUserId(),param.getApplyId(),param.getStatus(),param.getCoverImgUrl(),param.getReason());

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
