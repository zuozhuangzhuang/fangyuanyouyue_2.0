package com.fangyuanyouyue.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.ReportService;
import com.fangyuanyouyue.goods.service.SchedualRedisService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
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

@RestController
@RequestMapping(value = "/report")
@Api(description = "举报系统Controller")
@RefreshScope
public class ReportController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ReportService reportService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;


    //举报
    @ApiOperation(value = "举报", notes = "(void)举报",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "businessId", value = "举报对象id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "举报原因", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "举报类型 1商品、抢购 2视频 3帖子 4全民鉴定 5用户", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/reportGoods")
    @ResponseBody
    public BaseResp reportGoods(GoodsParam param) throws IOException {
        try {
            log.info("----》举报《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getBusinessId() == null){
                return toError("举报对象id不能为空！");
            }
            if(StringUtils.isEmpty(param.getReason())){
                return toError("举报原因不能为空！");
            }
            if(param.getType() == null){
                return toError("举报类型不能为空！");
            }
            //举报
            reportService.report(userId,param.getBusinessId(),param.getReason(),param.getType());
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
