//package com.fangyuanyouyue.forum.controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.fangyuanyouyue.base.BaseController;
//import com.fangyuanyouyue.base.BaseResp;
//import com.fangyuanyouyue.base.enums.ReCode;
//import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
//import com.fangyuanyouyue.forum.param.AppraisalParam;
//import com.fangyuanyouyue.forum.param.ForumParam;
//import com.fangyuanyouyue.forum.service.AppraisalCommentLikesService;
//import com.fangyuanyouyue.forum.service.AppraisalCommentService;
//import com.fangyuanyouyue.forum.service.AppraisalDetailService;
//import com.fangyuanyouyue.forum.service.SchedualRedisService;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(value = "/appraisal")
//@Api(description = "全民鉴定Controller")
//@RefreshScope
//public class AppraisalController extends BaseController {
//    protected Logger log = Logger.getLogger(this.getClass());
//
//    @Autowired
//    private AppraisalDetailService appraisalDetailService;
//    @Autowired
//    private AppraisalCommentService appraisalCommentService;
//    @Autowired
//    private AppraisalCommentLikesService appraisalCommentLikesService;
//
//    @Autowired
//    private SchedualRedisService schedualRedisService;
//
//    @ApiOperation(value = "全民鉴定详情", notes = "根据id获取全民鉴定详情",response = BaseResp.class)
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
//        @ApiImplicitParam(name = "forumId", value = "帖子id",required = true, dataType = "int", paramType = "query")
//    })
//    @PostMapping(value = "/detail")
//    @ResponseBody
//    public BaseResp appraisalDetail(AppraisalParam param) throws IOException {
//        try {
//            log.info("----》获取全民鉴定详情《----");
//            log.info("参数：" + param.toString());
//            //验证用户
//            if(StringUtils.isEmpty(param.getToken())){
//                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
//            }
//            //Integer userId = (Integer)schedualRedisService.get(param.getToken());
//
//            //if(userId!=null) {
//            	//TODO 暂时不需要处理
//            //}
//
//
//            if(param.getAppraisalId()==null) {
//            	return toError("鉴定ID不能为空");
//            }
//            AppraisalDetailDto dto = appraisalDetailService.getAppraisalDetail(param.getAppraisalId());
//
//            if(dto==null) {
//            	return toError("找不到该鉴定");
//            }
//
//            return toSuccess(dto);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
//        }
//    }
//
//
//    @ApiOperation(value = "全民鉴定列表", notes = "获取全民鉴定列表大集合",response = BaseResp.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户id",required = false, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "keyword", value = "搜索关键字",required = false, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query")
//    })
//    @PostMapping(value = "/list")
//    @ResponseBody
//    public BaseResp forumList(ForumParam param) throws IOException {
//        try {
//            log.info("----》获取全民鉴定列表《----");
//            log.info("参数：" + param.toString());
//            if(param.getStart()==null||param.getLimit()==null) {
//            	return toError("分页参数不能为空");
//            }
//
//            List<AppraisalDetailDto> dto = appraisalDetailService.getAppraisalList(param.getStart(), param.getLimit());
//
//            return toSuccess(dto);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
//        }
//    }
//
//
//
//}
