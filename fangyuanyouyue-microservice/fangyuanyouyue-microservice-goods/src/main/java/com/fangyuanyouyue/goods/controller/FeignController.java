package com.fangyuanyouyue.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.*;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/goodsFeign")
@Api(description = "商品系统外部调用Controller")
@RefreshScope
public class FeignController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private CartService cartService;
    @Autowired
    private BargainService bargainService;

    @GetMapping(value = "/goodsMainImg")
    @ResponseBody
    public BaseResp goodsMainImg(GoodsParam param) throws IOException {
        try {
            log.info("----》获取商品主图《----");
            log.info("参数：" + param.toString());

            if (param.getGoodsId() == null) {
                return toError( "商品id不能为空！");
            }
            //商品详情
            String goodsMainImg = goodsInfoService.goodsMainImg(param.getGoodsId());

            return toSuccess(goodsMainImg);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @GetMapping(value = "/goodsInfo")
    @ResponseBody
    public BaseResp goodsInfo(GoodsParam param) throws IOException {
        try {
            log.info("----》商品详情《----");
            log.info("参数：" + param.toString());

            if (param.getGoodsId() == null) {
                return toError( "商品id不能为空！");
            }
            //商品详情
            GoodsInfo goodsInfo = goodsInfoService.selectByPrimaryKey(param.getGoodsId());

            return toSuccess(goodsInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/updateGoodsStatus")
    @ResponseBody
    public BaseResp updateGoodsStatus(GoodsParam param) throws IOException {
        try {
            log.info("----》修改商品状态《----");
            log.info("参数：" + param.toString());
            if(param.getGoodsId() == null){
                return toError("商品ID不能为空！");
            }
            if(param.getStatus() == null){
                return toError("状态不能为空！");
            }
            //修改商品状态
            goodsInfoService.updateGoodsStatus(param.getGoodsId(),param.getStatus());
            return toSuccess("修改商品状态成功！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //移出购物车
    @PostMapping(value = "/cartRemove")
    @ResponseBody
    public BaseResp cartRemove(GoodsParam param) throws IOException{
        try {
            log.info("----》移出购物车《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(param.getUserId() == null){
                return toError("用户id不能为空！");
            }
            String verifyUser = schedualUserService.verifyUserById(param.getUserId());
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getGoodsIds() == null || param.getGoodsIds().length == 0){
                return toError("商品id不能为空！");
            }
            //移出购物车
            cartService.cartRemoveByIds(param.getUserId(),param.getGoodsIds());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    //获取统计信息
    @PostMapping(value = "/getProcess")
    @ResponseBody
    public BaseResp getProcess(Integer userId) throws IOException{
        try {
            log.info("----》获取统计信息《----");
            log.info("参数：userId：" + userId);
            //验证用户
            if(userId == null){
                return toError("用户id不能为空！");
            }
            //获取统计信息
            Integer count = bargainService.getProcess(userId);
            return toSuccess(count);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
}
