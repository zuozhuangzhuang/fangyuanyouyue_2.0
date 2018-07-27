package com.fangyuanyouyue.goods.controller;

import java.io.IOException;
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
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.CartService;
import com.fangyuanyouyue.goods.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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

    @ApiOperation(value = "申请鉴定", notes = "申请鉴定分为四种情况：1.卖家对自己商品进行鉴定，可显示到商品详情中 " +
            "2.买家对别人的商品进行鉴定，只能自己看到 3.用户上传图片鉴定图片中的物品(这个是全民鉴定还是官方鉴定) " +
            "4.官方认证店铺中的所有商品都是已鉴定",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsIds", value = "商品ID数组", allowMultiple = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "鉴定标题", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "鉴定赏金",  dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "描述",  dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrl", value = "图片地址",  dataType = "String", paramType = "query")
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
            Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            redisTemplate.expire(param.getToken(),7, TimeUnit.DAYS);
            if(StringUtils.isEmpty(param.getTitle())){
                return toError(ReCode.FAILD.getValue(),"鉴定标题不能为空！");
            }
            //申请鉴定
            appraisalService.addAppraisal(userId,param.getGoodsIds(),param.getTitle(), param.getDescription(),param.getPrice(),param.getImgUrl());
            return toSuccess( "申请鉴定成功！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    //我的鉴定
//    @ApiOperation(value = "我的鉴定", notes = "我的鉴定",response = ResultUtil.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
//    })
//    @PostMapping(value = "/getAppraisal")
//    @ResponseBody
//    public String getAppraisal(GoodsParam param) throws IOException{
//        try {
//            log.info("----》我的鉴定《----");
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
//            //我的鉴定
//            List<CartShopDto> cart = cartService.getCart(userId);
//            return toSuccess( cart,"获取我的鉴定成功！");
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            return toError(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
//        }
//    }




}
