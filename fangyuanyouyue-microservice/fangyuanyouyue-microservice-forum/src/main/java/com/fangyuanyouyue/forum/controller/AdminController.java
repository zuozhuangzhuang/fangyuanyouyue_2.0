package com.fangyuanyouyue.forum.controller;

import java.io.IOException;

import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.forum.param.AdminForumParam;
import com.fangyuanyouyue.forum.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/adminForum")
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
    @Autowired
    private ForumInfoService forumInfoService;
    @Autowired
    private AppraisalDetailService appraisalDetailService;
    @Autowired
    private ReportService reportService;


	@ApiOperation(value = "专栏列表", notes = "专栏列表", response = BaseResp.class)
	@ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
	})
	@GetMapping(value = "/columnList")
	@ResponseBody
	public BaseResp columnList(AdminForumParam param) throws IOException {
		try {
			log.info("----》专栏列表《----");
			log.info("参数：" + param.toString());
            if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
                return toError("分页参数错误");
            }
            Pager pager = forumColumnService.getPage(param);

            return toPage(pager);
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}


	@ApiOperation(value = "帖子列表", notes = "(ForumInfoDto)帖子列表", response = BaseResp.class)
	@ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
	})
	@GetMapping(value = "/forumList")
	@ResponseBody
	public BaseResp forumList(BasePageReq param) throws IOException {
		try {
			log.info("----》帖子列表《----");
			log.info("参数：" + param.toString());
            if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
                return toError("分页参数错误");
            }
            Pager pager = forumInfoService.getPage(param,1);

            return toPage(pager);
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}



	@ApiOperation(value = "视频列表", notes = "(ForumInfoDto)视频列表", response = BaseResp.class)
	@ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
	})
	@GetMapping(value = "/videoList")
	@ResponseBody
	public BaseResp videoList(BasePageReq param) throws IOException {
		try {
			log.info("----》帖子列表《----");
			log.info("参数：" + param.toString());
            if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
                return toError("分页参数错误");
            }
            Pager pager = forumInfoService.getPage(param,2);

            return toPage(pager);
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}




	@ApiOperation(value = "全民鉴定列表", notes = "全民鉴定列表", response = BaseResp.class)
	@ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
	})
	@GetMapping(value = "/appraisalList")
	@ResponseBody
	public BaseResp appraisalList(BasePageReq param) throws IOException {
		try {
			log.info("----》全民鉴定列表《----");
			log.info("参数：" + param.toString());
            if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
                return toError("分页参数错误");
            }
            Pager pager = appraisalDetailService.getPage(param);

            return toPage(pager);
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}



	@ApiOperation(value = "专栏申请列表", notes = "专栏申请列表", response = BaseResp.class)
	@ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
	})
	@GetMapping(value = "/applyList")
	@ResponseBody
	public BaseResp applyList(BasePageReq param) throws IOException {
		try {
			log.info("----》专栏申请列表《----");
			log.info("参数：" + param.toString());
            if(param.getStart()==null || param.getStart() < 0 ||param.getLimit()==null || param.getLimit() < 1) {
                return toError("分页参数错误");
            }
            Pager pager = forumColumnService.getPageApply(param);

            return toPage(pager);
		} catch (Exception e) {
			e.printStackTrace();
			return toError("系统繁忙，请稍后再试！");
		}
	}

    @ApiOperation(value = "后台处理专栏申请", notes = "后台处理专栏申请", response = BaseResp.class)
    @ApiImplicitParams({

            @ApiImplicitParam(name = "id", value = "专栏申请id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id",required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 1同意 2拒绝",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coverImgUrl", value = "封面图片地址（同意必填）",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "拒绝理由（拒绝必填）",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isChosen", value = "是否精选1是 2否",required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/handleApply")
    @ResponseBody
    public BaseResp handle(AdminForumParam param) throws IOException {
        try {
            log.info("----》后台处理专栏申请《----");
            log.info("参数：" + param.toString());
            if(param.getId() == null){
                return toError("申请id不能为空！");
            }
            if(param.getStatus() == null){
                return toError("处理状态不能为空！");
            }
            //处理专栏申请
            forumColumnService.handle(param.getId(),param.getStatus(),param.getCoverImgUrl(),param.getReason());

            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //获取举报商品列表
    @ApiOperation(value = "获取举报列表", notes = "获取举报列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1帖子 2视频 3全民鉴定", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1已处理 2待处理", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/reportList")
    @ResponseBody
    public BaseResp reportList(AdminForumParam param) throws IOException {
        try {
            log.info("----》获取举报列表《----");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            Pager pager;
            if(param.getType().intValue() == 1 || param.getType().intValue() == 2){
                pager = reportService.getForumReportPage(param);
            }else if(param.getType().intValue() == 3){
                pager = reportService.getAppraisalReportPage(param);
            }else{
                return toError("类型错误！");
            }
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    //后台处理举报
    @ApiOperation(value = "后台处理举报", notes = "后台处理举报，发送信息给被举报者",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "举报信息id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "处理原因", required = true, dataType = "int", paramType = "query")
    })
    @PutMapping(value = "/dealReport")
    @ResponseBody
    public BaseResp dealReport(Integer id,String content) throws IOException {
        try {
            log.info("----》后台处理举报《----");
            if(id == null){
                return toError("id不能为空！");
            }
            if(StringUtils.isEmpty(content)){
                return toError("处理原因不能为空！");
            }
            reportService.dealReport(id,content);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改专栏", notes = "修改专栏", response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "专栏id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "专栏名字",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coverImgUrl", value = "封面图片地址",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isChosen", value = "是否精选 1是 2否",required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateColumn")
    @ResponseBody
    public BaseResp updateColumn(AdminForumParam param) throws IOException {
        try {
            log.info("----》修改专栏《----");
            log.info("参数：" + param.toString());
            if(param.getId() == null){
                return toError("专栏id不能为空！");
            }
            forumColumnService.updateColumn(param.getId(),param.getName(), param.getCoverImgUrl(),param.getIsChosen());

            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    @ApiOperation(value = "修改帖子、视频", notes = "精选帖子、置顶视频、删除帖子视频", response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "帖子、视频id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排列优先级 1置顶 2默认排序",required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "isChosen", value = "是否精选 1是 2否",required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1显示 2删除",required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/updateForum")
    @ResponseBody
    public BaseResp updateForum(AdminForumParam param) throws IOException {
        try {
            log.info("----》修改帖子、视频《----");
            log.info("参数：" + param.toString());
            if(param.getId() == null){
                return toError("帖子、视频id不能为空！");
            }
            forumInfoService.updateForum(param.getId(),param.getSort(),param.getIsChosen(),param.getStatus());

            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "编辑浏览量基数", notes = "编辑浏览量基数",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "视频、帖子id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "count", value = "修改数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1增加 2减少", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/updateFansCount")
    @ResponseBody
    public BaseResp updateFansCount(AdminForumParam param) throws IOException {
        try {
            log.info("----》编辑浏览量基数《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("帖子、视频id不能为空！");
            }
            forumInfoService.updatePvCount(param.getId(),param.getCount(),param.getType());
            return toSuccess();
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

}
