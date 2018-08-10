package com.fangyuanyouyue.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.AppraisalDetailDto;
import com.fangyuanyouyue.goods.dto.AppraisalOrderInfoDto;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.param.OrderParam;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.CartService;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/appraisal")
@Api(description = "鉴赏系统Controller")
@RefreshScope
public class AppraisalController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    private CartService cartService;
    @Autowired
    private AppraisalService appraisalService;
    @Autowired
    private SchedualRedisService schedualRedisService;


    @ApiOperation(value = "申请鉴定", notes = "(AppraisalOrderInfoDto)申请鉴定分为四种情况：1.卖家对自己商品进行鉴定，可显示到商品详情中 " +
            "2.买家对别人的商品进行鉴定，只能自己看到 3.用户上传图片鉴定图片中的物品 " +
            "4.官方认证店铺中的所有商品都是已鉴定",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsIds", value = "商品ID数组", allowMultiple = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "描述",  dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrls", value = "图片地址数组",allowMultiple = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoUrl", value = "视频路径",dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/addAppraisal")
    @ResponseBody
    public BaseResp addAppraisal(GoodsParam param) throws IOException {
        try {
            log.info("----》申请鉴定《----");
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
            if(param.getGoodsIds() == null || param.getGoodsIds().length < 1){
                if(param.getImgUrls() == null && param.getVideoUrl() == null){
                    toError(ReCode.FAILD.getValue(),"至少包含一张图片或一段视频！");
                }
            }
            //申请鉴定，需要生成订单并返回订单信息
            AppraisalOrderInfoDto appraisalOrderInfoDto = appraisalService.addAppraisal(userId, param.getGoodsIds(), param.getTitle(), param.getDescription(), param.getImgUrls(),param.getVideoUrl());
            return toSuccess(appraisalOrderInfoDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "取消鉴定", notes = "(void)取消鉴定",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID",required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/cancelAppraisal")
    @ResponseBody
    public BaseResp cancelAppraisal(OrderParam param) throws IOException {
        try {
            log.info("----》取消鉴定《----");
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
            if(param.getOrderId() == null){
                toError(ReCode.FAILD.getValue(),"订单ID不能为空！");
            }
            //取消鉴定，删除订单及详情
            appraisalService.cancelAppraisal(userId,param.getOrderId());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }



    //鉴定查询
    @ApiOperation(value = "鉴定查询", notes = "(AppraisalDetailDto)鉴定查询",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/getAppraisal")
    @ResponseBody
    public BaseResp getAppraisal(GoodsParam param) throws IOException{
        try {
            log.info("----》鉴定查询《----");
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
            if(param.getStart() == null || param.getStart() < 0){
                return toError(ReCode.FAILD.getValue(),"起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError(ReCode.FAILD.getValue(),"每页个数错误！");
            }
            //鉴定查询
            List<AppraisalDetailDto> appraisals = appraisalService.getAppraisal(userId,param.getStart(),param.getLimit());
            return toSuccess(appraisals);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    //鉴定结果
    @ApiOperation(value = "鉴定结果", notes = "(AppraisalDetailDto)鉴定结果",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "detailId", value = "鉴定详情ID", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/appraisalDetail")
    @ResponseBody
    public BaseResp appraisalDetail(GoodsParam param) throws IOException{
        try {
            log.info("----》鉴定结果《----");
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
            if(param.getDetailId() == null){
                return toError(ReCode.FAILD.getValue(),"鉴定详情ID不能为空！");
            }
            //鉴定结果
            AppraisalDetailDto appraisal = appraisalService.appraisalDetail(userId,param.getDetailId());
            return toSuccess(appraisal);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }



    @ApiOperation(value = "鉴定支付", notes = "(String)鉴定支付",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID",required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码",required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "支付方式 1支付宝 2微信 3余额支付", required = true, dataType = "int", paramType = "query"),

    })
    @PostMapping(value = "/payAppraisal")
    @ResponseBody
    public BaseResp payAppraisal(OrderParam param) throws IOException {
        try {
            log.info("----》鉴定支付《----");
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
            if(param.getOrderId() == null){
                toError(ReCode.FAILD.getValue(),"订单ID不能为空！");
            }
            if(StringUtils.isEmpty(param.getPayPwd())){
                toError(ReCode.FAILD.getValue(),"支付密码不能为空！");
            }
            if(param.getType()==null){
                return toError("支付类型不能为空！");
            }
            //鉴定支付
            String payInfo = appraisalService.payAppraisal(userId, param.getOrderId(), param.getType(), param.getPayPwd());
            return toSuccess(payInfo);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }



    //鉴定查询
    @ApiOperation(value = "鉴定展示", notes = "(AppraisalDetailDto)鉴定展示",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/getAllAppraisal")
    @ResponseBody
    public BaseResp getAllAppraisal(GoodsParam param) throws IOException{
        try {
            log.info("----》鉴定展示《----");
            log.info("参数：" + param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError(ReCode.FAILD.getValue(),"起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError(ReCode.FAILD.getValue(),"每页个数错误！");
            }
            //鉴定展示
            List<AppraisalDetailDto> appraisals = appraisalService.getAllAppraisal(param.getStart(),param.getLimit());
            return toSuccess(appraisals);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

}
