package com.fangyuanyouyue.goods.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.ResultUtil;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.BargainDto;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.BargainService;
import com.fangyuanyouyue.goods.service.CartService;
import com.fangyuanyouyue.goods.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/bargain")
@Api(description = "压价系统Controller")
@RefreshScope
public class BargainController extends BaseController{
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
    private BargainService bargainService;

    @ApiOperation(value = "商品压价申请", notes = "(void)用户发起对商品的议价，直接扣除用户余额 ",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID",required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "出价钱", required = true, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "议价理由", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/addBargain")
    @ResponseBody
    public BaseResp addBargain(GoodsParam param) throws IOException {
        try {
            log.info("----》商品压价申请《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
            if(param.getGoodsId() == null){
                return toError(ReCode.FAILD.getValue(),"商品ID不能为空！");
            }
            if(param.getPrice() == null){
                return toError(ReCode.FAILD.getValue(),"出价钱不能为空！");
            }
            if(StringUtils.isEmpty(param.getReason())){
                return toError(ReCode.FAILD.getValue(),"议价理由不能为空！");
            }
            //申请商品压价
            bargainService.addBargain(userId,param.getGoodsId(),param.getPrice(),param.getReason());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "处理压价", notes = "(void)处理压价，包括：取消压价返还余额；卖家同意压价，生成订单；卖家拒绝压价，返还余额",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID",required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "bargainId", value = "压价详情ID",required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 2同意 3拒绝 4取消",required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/updateBargain")
    @ResponseBody
    public BaseResp updateBargain(GoodsParam param) throws IOException {
        try {
            log.info("----》处理压价《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
            if(param.getGoodsId() == null){
                return toError(ReCode.FAILD.getValue(),"商品ID不能为空！");
            }
            if(param.getBargainId() == null){
                return toError(ReCode.FAILD.getValue(),"压价详情ID不能为空！");
            }
            if(param.getStatus() == null){
                return toError(ReCode.FAILD.getValue(),"状态不能为空！");
            }
            //处理压价
            bargainService.updateBargain(userId,param.getGoodsId(),param.getBargainId(),param.getStatus());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "我的压价列表", notes = "(BargainDto)买家获取自己所压价商品的列表",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/bargainList")
    @ResponseBody
    public BaseResp bargainList(GoodsParam param) throws IOException {
        try {
            log.info("----》我的压价列表《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
            //TODO 我的压价列表
            List<BargainDto> bargainDtos = bargainService.bargainList(userId);
            return toSuccess( bargainDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


//    @ApiOperation(value = "压价详情", notes = "",response = ResultUtil.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "goodsId", value = "商品ID",required = true,dataType = "int", paramType = "query")
//    })
//    @PostMapping(value = "/bargainDetail")
//    @ResponseBody
//    public String bargainDetail(GoodsParam param) throws IOException {
//        try {
//            log.info("----》压价详情《----");
//            log.info("参数：" + param.toString());
//            //验证用户
//            if(StringUtils.isEmpty(param.getToken())){
//                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
//            }
//            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
//            String verifyUser = schedualUserService.verifyUserById(userId);
//            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
//            if((Integer)jsonObject.get("code") != 0){
//                return toError(jsonObject.getString("report"));
//            }
//            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
//            if(param.getGoodsId() == null){
//                return toError(ReCode.FAILD.getValue(),"商品ID不能为空！");
//            }
//            //TODO 压价详情
//            GoodsBargain goodsBargain = bargainService.bargainDetail(userId, param.getBargainId(), param.getGoodsId());
////            bargainService.addBargain(userId,param.getGoodsId(),param.getPrice(),param.getReason());
//            return toSuccess( "获取压价详情成功！");
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            return toError(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
//        }
//    }

}
