package com.fangyuanyouyue.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.wallet.dto.BonusPoolDto;
import com.fangyuanyouyue.wallet.dto.PointGoodsDto;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/score")
@Api(description = "积分系统Controller")
@RefreshScope
public class ScoreController extends BaseController{

    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private PointGoodsService pointGoodsService;
    @Autowired
    private PointOrderService pointOrderService;

    @ApiOperation(value = "获取奖池信息", notes = "(WalletDto)获取奖池信息",response = BaseResp.class)
    @GetMapping(value = "/getBonusPool")
    @ResponseBody
    public BaseResp getBonusPool() throws IOException {
        try {
            log.info("----》获取奖池信息《----");
            //获取奖池信息
            List<BonusPoolDto> bonusPool = scoreService.getBonusPool();
            return toSuccess(bonusPool);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "抽奖", notes = "(WalletDto)获取奖池信息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/lottery")
    @ResponseBody
    public BaseResp lottery(WalletParam param) throws IOException {
        try {
            log.info("----》抽奖《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            //抽奖
            String message = scoreService.lottery(userId);
            return toSuccess(message);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "积分商品列表", notes = "(PointGoodsDto)获取积分商城列表",response = BaseResp.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "start", value = "起始条数",required = true, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "每页条数",required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/goods/list")
    @ResponseBody
    public BaseResp goodsList(WalletParam param) throws IOException {
        try {
            log.info("----》积分商品列表《----");
            log.info("参数："+param.toString());
            
            List<PointGoodsDto> list = pointGoodsService.getPointGoods(param.getStart(), param.getLimit());
            
            return toSuccess(list);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
    
    @ApiOperation(value = "兑换商品", notes = "积分兑换商品",response = BaseResp.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "goodsId", value = "商城ID",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/goods/buy")
    @ResponseBody
    public BaseResp goodsBuy(WalletParam param) throws IOException {
        try {
            log.info("----》积分兑换《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            pointOrderService.saveOrder(userId, param.getGoodsId());
            
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "分享增加积分", notes = "用户分享增加积分")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/shareHtml")
    @ResponseBody
    public BaseResp shareHtml(WalletParam param) throws IOException {
        try {
            log.info("----》分享增加积分《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }

            //用户分享增加积分
            scoreService.shareHtml(userId);
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
