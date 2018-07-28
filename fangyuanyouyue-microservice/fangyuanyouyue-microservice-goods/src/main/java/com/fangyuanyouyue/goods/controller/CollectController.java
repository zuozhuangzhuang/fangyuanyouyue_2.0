package com.fangyuanyouyue.goods.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.ResultUtil;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.GoodsDto;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.CollectService;
import com.fangyuanyouyue.goods.service.SchedualUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/collect")
@Api(description = "商品收藏系统Controller")
@RefreshScope
public class CollectController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CollectService collectService;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用

    @ApiOperation(value = "收藏/关注或取消", notes = "(void)收藏/关注或取消",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "collectId", value = "收藏对象ID", allowMultiple = true,required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1关注 2收藏", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1关注/收藏 2取消关注/收藏", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "collectType", value = "关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏（只有抢购可以关注）", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/collectGoods")
    @ResponseBody
    public BaseResp collectGoods(GoodsParam param) throws IOException {
        try {
            log.info("----》收藏/关注或取消《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
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
            if(param.getType() == null){
                return toError(ReCode.FAILD.getValue(),"类型不能为空！");
            }
            if(param.getCollectType() == null){
                return toError(ReCode.FAILD.getValue(),"关注/收藏类型不能为空！");
            }
            if(param.getCollectId() == null || param.getCollectId().length < 1){
                return toError(ReCode.FAILD.getValue(),"对象ID不能为空！");
            }
            if(param.getStatus() == null){
                return toError(ReCode.FAILD.getValue(),"状态不能为空！");
            }
            //收藏/关注或取消
            collectService.collectGoods(userId,param.getCollectId(),param.getCollectType(),param.getType(),param.getStatus());
            if(param.getStatus() == 1){
                return toSuccess("收藏/关注成功");
            }else{
                return toSuccess("取消收藏/关注成功");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "获取我的收藏/关注", notes = "(GoodsDto)获取我的收藏/关注的商品或抢购",response = ResultUtil.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1关注 2收藏", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "collectType", value = "关注/收藏类型 1商品 2抢购（只有抢购可以关注）", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/collectList")
    @ResponseBody
    public BaseResp collectList(GoodsParam param) throws IOException {
        try {
            log.info("----》获取我的收藏/关注《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
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
            if(param.getType() == null){
                return toError(ReCode.FAILD.getValue(),"类型不能为空！");
            }
            if(param.getCollectType() == null){
                return toError(ReCode.FAILD.getValue(),"关注/收藏类型不能为空！");
            }
            List<GoodsDto> goodsDtos = collectService.collectList(userId, param.getCollectType(), param.getType());
            return toSuccess(goodsDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }
}
