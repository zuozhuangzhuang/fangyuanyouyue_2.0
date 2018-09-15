package com.fangyuanyouyue.forum.controller;

import java.io.IOException;
import java.util.List;

import com.fangyuanyouyue.forum.service.ForumInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.model.ForumColumnApply;
import com.fangyuanyouyue.forum.param.ForumParam;
import com.fangyuanyouyue.forum.service.ForumColumnService;
import com.fangyuanyouyue.forum.service.SchedualRedisService;
import com.fangyuanyouyue.forum.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/adminAppraisal")
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


	@ApiOperation(value = "专栏申请列表", notes = "专栏申请列表", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "keyword", value = "关键字搜索",required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "状态 0申请中 1通过 2未通过",required = true, dataType = "int", paramType = "query")
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
			//TODO 专栏申请列表
           // List<ForumColumnApply> forumColumnApplies = forumColumnService.applyList(param.getStart(), param.getLimit(), param.getKeyword(), param.getStatus());

            return toPage(pager);
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
            forumInfoService.dealReport(id,content);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
/*
@Override
    public void dealReport(Integer id, String content) throws ServiceException {
        //1、删除商品 2、发送信息（content）
        Report report = reportMapper.selectByPrimaryKey(id);
        if(report == null){
            throw new ServiceException("未找到举报信息！");
        }
        if(report.getType() == 1){
            //删除商品
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(report.getBusinessId());
            if(goodsInfo == null){
                throw new ServiceException("未找到商品、抢购");
            }
            goodsInfo.setStatus(5);
            goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
            //很抱歉，您的商品/抢购【名称】被多用户举报，并经官方核实。已被删除，删除理由：￥@……#%￥&#%￥……@。点击查看详情
            schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                    "很抱歉，您的"+(goodsInfo.getType()==1?"商品【":"抢购【")+goodsInfo.getName()+"】被多用户举报，并经官方核实。已被删除，删除理由："+content+"。点击查看详情","1","2",goodsInfo.getId().toString());
        }else if(report.getType() == 2 || report.getType() == 3){
            //删除帖子、视频

            //很抱歉，您的帖子/视频/全民鉴定【名称】被多用户举报，并经官方核实。已被删除，删除理由：￥@……#%￥&#%￥……@
            schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                    "很抱歉，您的"+(goodsInfo.getType()==1?"商品【":"抢购【")+goodsInfo.getName()+"】被多用户举报，并经官方核实。已被删除，删除理由："+content+"。点击查看详情","1","2",goodsInfo.getId().toString());
        }else if(report.getType() == 4){
            //删除全民鉴定

            //很抱歉，您的帖子/视频/全民鉴定【名称】被多用户举报，并经官方核实。已被删除，删除理由：￥@……#%￥&#%￥……@
            schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                    "很抱歉，全民鉴定【"+goodsInfo.getName()+"】被多用户举报，并经官方核实。已被删除，删除理由："+content,"1","","");
        }
        report.setStatus(1);
        reportMapper.updateByPrimaryKeySelective(report);
    }
 */
}
