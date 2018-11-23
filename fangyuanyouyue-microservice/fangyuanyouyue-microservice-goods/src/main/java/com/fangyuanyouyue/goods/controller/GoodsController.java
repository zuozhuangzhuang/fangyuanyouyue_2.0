package com.fangyuanyouyue.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.model.WxPayResult;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.base.util.WechatUtil.WXPayUtil;
import com.fangyuanyouyue.base.util.alipay.util.AlipayNotify;
import com.fangyuanyouyue.goods.dto.*;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.model.UserInfo;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/goods")
@Api(description = "商品系统Controller")
@RefreshScope
public class GoodsController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());

    protected Logger wechatLog = Logger.getLogger(this.getClass());
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private SchedualOrderService schedualOrderService;

    @ApiOperation(value = "获取商品/抢购列表", notes = "(GoodsDto)根据start和limit获取分页后的商品/抢购，根据用户token获取买家相关商品/抢购列表，" +
            "根据userId获取卖家相关商品/抢购列表，根据search、synthesizer、priceMin、priceMax、quality、goodsCategoryIds对列表进行筛选，根据type进行区分商品和抢购",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token，不为空则为：我的商品", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "卖家id，不为空则为：他的商品", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1出售中 2已售出  3已下架（已结束） 5删除", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索词条", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "synthesize", value = "综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序 5：时间升序 6：时间降序",dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "priceMin", value = "最小价格", dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "priceMax", value = "最大价格", dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "quality", value = "品质 1：认证店铺 2：官方保真 3：高信誉度 4：我的关注 5：（抢购）已完成", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）",allowMultiple = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/goodsList")
    @ResponseBody
    public BaseResp goodsList(GoodsParam param) throws IOException {
        try {
            log.info("----》获取商品列表《----");
            log.info("参数：" + param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            Integer myId = null;
            if(StringUtils.isNotEmpty(param.getToken())){//我的商品验证用户
                //根据用户token获取userId
                 myId = (Integer)schedualRedisService.get(param.getToken());
                BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(myId));
                if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                    return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
                }
            }else{
                if(param.getQuality() != null && param.getQuality() == 4){//我的关注，要求登录授权
                    return toError("未登录，无法获取我的关注！");
                }
            }
            if(param.getQuality() != null && param.getQuality() == 5){
                if(!Status.AUCTION.getValue().equals(param.getType())){
                    return toError("只有抢购可选已完成！");
                }
            }
            //获取商品列表
            List<GoodsDto> goodsDtos = goodsInfoService.getGoodsInfoList(myId,param);
            return toSuccess(goodsDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
//    @ApiOperation(value = "获取抢购列表", notes = "(GoodsDto)根据start和limit获取分页后的商品/抢购，根据用户token获取买家相关商品/抢购列表，" +
//            "根据userId获取卖家相关商品/抢购列表，根据search、synthesizer、priceMin、priceMax、quality、goodsCategoryIds对列表进行筛选，根据type进行区分商品和抢购",response = BaseResp.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token，不为空则为：我的商品", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "userId", value = "卖家id，不为空则为：他的商品", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "status", value = "状态 1出售中 2已售出  3已下架（已结束） 5删除", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "search", value = "搜索词条", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "synthesize", value = "综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序",dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "priceMin", value = "最小价格", dataType = "BigDecimal", paramType = "query"),
//            @ApiImplicitParam(name = "priceMax", value = "最大价格", dataType = "BigDecimal", paramType = "query"),
//            @ApiImplicitParam(name = "quality", value = "品质 1：认证店铺 2：官方保真 3：高信誉度 4：我的关注 5：（抢购）已完成", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品",required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）",allowMultiple = true, dataType = "int", paramType = "query")
//    })
//    @PostMapping(value = "/auctionList")
//    @ResponseBody
//    public BaseResp auctionList(GoodsParam param) throws IOException {
//        try {
//            log.info("----》获取商品列表《----");
//            log.info("参数：" + param.toString());
//            if(param.getStart() == null || param.getStart() < 0){
//                return toError("起始页数错误！");
//            }
//            if(param.getLimit() == null || param.getLimit() < 1){
//                return toError("每页个数错误！");
//            }
//            if(StringUtils.isNotEmpty(param.getToken())){//我的商品验证用户
//                //根据用户token获取userId
//                Integer userId = (Integer)schedualRedisService.get(param.getToken());
//                BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
//                if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
//                    return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
//                }
//                param.setUserId(userId);
//            }else{
//                if(param.getQuality() != null && param.getQuality() == 4){//我的关注，要求登录授权
//                    return toError("未登录，无法获取我的关注！");
//                }
//            }
//            if(param.getQuality() != null && param.getQuality() == 5){
//                if(param.getType() != 2){
//                    return toError("只有抢购可选已完成！");
//                }
//            }
//            //获取商品列表
//            List<GoodsDto> goodsDtos = goodsInfoService.getGoodsInfoList(param);
//            return toSuccess(goodsDtos);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            return toError(e.getCode(),e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//
//    @ApiOperation(value = "获取个人店铺商品列表", notes = "(GoodsDto)根据start和limit获取分页后的商品/抢购，根据用户token获取买家相关商品/抢购列表，" +
//            "根据userId获取卖家相关商品/抢购列表，根据search、synthesizer、priceMin、priceMax、quality、goodsCategoryIds对列表进行筛选，根据type进行区分商品和抢购",response = BaseResp.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token，不为空则为：我的商品", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "userId", value = "卖家id，不为空则为：他的商品", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "status", value = "状态 1出售中 2已售出  3已下架（已结束） 5删除", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "search", value = "搜索词条", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "synthesize", value = "综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序",dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "priceMin", value = "最小价格", dataType = "BigDecimal", paramType = "query"),
//            @ApiImplicitParam(name = "priceMax", value = "最大价格", dataType = "BigDecimal", paramType = "query"),
//            @ApiImplicitParam(name = "quality", value = "品质 1：认证店铺 2：官方保真 3：高信誉度 4：我的关注 5：（抢购）已完成", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品",required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）",allowMultiple = true, dataType = "int", paramType = "query")
//    })
//    @PostMapping(value = "/shopGoodsList")
//    @ResponseBody
//    public BaseResp shopGoodsList(GoodsParam param) throws IOException {
//        try {
//            log.info("----》获取商品列表《----");
//            log.info("参数：" + param.toString());
//            if(param.getStart() == null || param.getStart() < 0){
//                return toError("起始页数错误！");
//            }
//            if(param.getLimit() == null || param.getLimit() < 1){
//                return toError("每页个数错误！");
//            }
//            if(StringUtils.isNotEmpty(param.getToken())){//我的商品验证用户
//                //根据用户token获取userId
//                Integer userId = (Integer)schedualRedisService.get(param.getToken());
//                BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
//                if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
//                    return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
//                }
//                param.setUserId(userId);
//            }else{
//                if(param.getQuality() != null && param.getQuality() == 4){//我的关注，要求登录授权
//                    return toError("未登录，无法获取我的关注！");
//                }
//            }
//            if(param.getQuality() != null && param.getQuality() == 5){
//                if(param.getType() != 2){
//                    return toError("只有抢购可选已完成！");
//                }
//            }
//            //获取商品列表
//            List<GoodsDto> goodsDtos = goodsInfoService.getGoodsInfoList(param);
//            return toSuccess(goodsDtos);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            return toError(e.getCode(),e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//
//    @ApiOperation(value = "获取个人店铺抢购列表", notes = "(GoodsDto)根据start和limit获取分页后的商品/抢购，根据用户token获取买家相关商品/抢购列表，" +
//            "根据userId获取卖家相关商品/抢购列表，根据search、synthesizer、priceMin、priceMax、quality、goodsCategoryIds对列表进行筛选，根据type进行区分商品和抢购",response = BaseResp.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token，不为空则为：我的商品", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "userId", value = "卖家id，不为空则为：他的商品", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "status", value = "状态 1出售中 2已售出  3已下架（已结束） 5删除", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "search", value = "搜索词条", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "synthesize", value = "综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序",dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "priceMin", value = "最小价格", dataType = "BigDecimal", paramType = "query"),
//            @ApiImplicitParam(name = "priceMax", value = "最大价格", dataType = "BigDecimal", paramType = "query"),
//            @ApiImplicitParam(name = "quality", value = "品质 1：认证店铺 2：官方保真 3：高信誉度 4：我的关注 5：（抢购）已完成", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品",required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）",allowMultiple = true, dataType = "int", paramType = "query")
//    })
//    @PostMapping(value = "/shopAuctionList")
//    @ResponseBody
//    public BaseResp shopAuctionList(GoodsParam param) throws IOException {
//        try {
//            log.info("----》获取商品列表《----");
//            log.info("参数：" + param.toString());
//            if(param.getStart() == null || param.getStart() < 0){
//                return toError("起始页数错误！");
//            }
//            if(param.getLimit() == null || param.getLimit() < 1){
//                return toError("每页个数错误！");
//            }
//            if(StringUtils.isNotEmpty(param.getToken())){//我的商品验证用户
//                //根据用户token获取userId
//                Integer userId = (Integer)schedualRedisService.get(param.getToken());
//                BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
//                if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
//                    return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
//                }
//                param.setUserId(userId);
//            }else{
//                if(param.getQuality() != null && param.getQuality() == 4){//我的关注，要求登录授权
//                    return toError("未登录，无法获取我的关注！");
//                }
//            }
//            if(param.getQuality() != null && param.getQuality() == 5){
//                if(param.getType() != 2){
//                    return toError("只有抢购可选已完成！");
//                }
//            }
//            //获取商品列表
//            List<GoodsDto> goodsDtos = goodsInfoService.getGoodsInfoList(param);
//            return toSuccess(goodsDtos);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            return toError(e.getCode(),e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }

    @ApiOperation(value = "发布商品/抢购", notes = "(void)发布商品/抢购",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsInfoName", value = "商品名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）", required = true,allowMultiple = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "商品描述(详情)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "商品价格", required = true, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "postage", value = "运费", required = false, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "floorPrice", value = "最低价", dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "intervalTime", value = "降价时间间隔，单位：秒", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "markdown", value = "降价幅度", dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "imgUrls", value = "商品图片路径数组", required = true,allowMultiple = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoUrl", value = "视频路径",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoImg", value = "视频截图路径",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoLength", value = "视频长度",dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "邀请用户id数组",allowMultiple = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/addGoods")
    @ResponseBody
    public BaseResp addGoods(GoodsParam param) throws IOException {
        try {
            log.info("----》发布商品/抢购《----");
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
            UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()),UserInfo.class);
            //验证实名认证
            if(JSONObject.parseObject(schedualUserService.isAuth(userId)).getBoolean("data") == false){
                return toError("用户未实名认证！");
            }
            if(StringUtils.isEmpty(param.getGoodsInfoName())){
                return toError("标题不能为空！");
            }
            if(param.getGoodsCategoryIds().length<1){
                return toError("所属分类不能为空！");
            }
            if(StringUtils.isEmpty(param.getDescription())){
                return toError("商品描述不能为空！");
            }
            if(param.getPrice()==null){
                return toError("价格不能为空！");
            }
            if(param.getPrice().compareTo(new BigDecimal(0))<0){
                return toError("价格不能低于0元！");
            }
            if(param.getType() == null){
                return  toError("商品类型不能为空！");
            }
            if(param.getImgUrls() == null || param.getImgUrls().length < 1){
                return toError("请至少上传一张图片！");
            }
            if(param.getType() == 2){//抢购
                if(param.getFloorPrice() == null){
                    return toError("最低价不能为空！");
                }
                if(param.getFloorPrice().compareTo(param.getPrice())>0){
                    return toError("最低价不能大于原价！");
                }
                if(param.getIntervalTime() == null){
                    return toError("降价时间间隔不能为空！");
                }
                if(param.getIntervalTime() < 60){
                    return toError("降价时间间隔不得低于60秒！");
                }
                if(param.getMarkdown() == null){
                    return toError("降价幅度不能为空！");
                }
                if(param.getMarkdown().compareTo(param.getPrice()) > 0){
                    return toError("降价幅度不能大于原价！");
                }
            }
            goodsInfoService.addGoods(user,param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "批量删除商品", notes = "(void)批量删除商品",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", dataType = "String", paramType = "query",example = ""),
            @ApiImplicitParam(name = "goodsIds", value = "商品id数组", required = true, allowMultiple = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/deleteGoods")
    @ResponseBody
    public BaseResp deleteGoods(GoodsParam param) throws IOException {
        try {
            log.info("----》批量删除商品《----");
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
            if(param.getGoodsIds() == null || param.getGoodsIds().length<1){
                return toError("商品ID不能为空！");
            }
            for(Integer goodsId:param.getGoodsIds()){
                //1、商品是否存在订单 2、订单是否已完成或已取消 3、订单已退款：是否已完成退款、是否已拒绝退款
                GoodsInfo goodsInfo = goodsInfoService.selectByPrimaryKey(goodsId);
                if(goodsInfo.getStatus().equals(Status.GOODS_SOLD.getValue())){
                    if(orderInfoService.ifHasOrder(userId,goodsId)){
                        return toError("商品【"+goodsInfo.getName()+"】存在未完成订单，请勿删除！");
                    }
                }
            }

            //批量删除商品
            goodsInfoService.deleteGoods(userId,param.getGoodsIds());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "编辑商品/抢购", notes = "(void)对已发布的商品或抢购进行修改",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "goodsInfoName", value = "商品名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）",allowMultiple = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "商品描述(详情)",  dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "商品价格",  dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "postage", value = "运费",  dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "floorPrice", value = "最低价", dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "intervalTime", value = "降价时间间隔", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "markdown", value = "降价幅度", dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "imgUrls", value = "商品图片路径数组",allowMultiple = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoUrl", value = "视频路径",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoImg", value = "视频截图路径",dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "/modifyGoods")
    @ResponseBody
    public BaseResp modifyGoods(GoodsParam param) throws IOException {
        try {
            log.info("----》编辑商品/抢购《----");
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
            goodsInfoService.modifyGoods(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //商品详情
    @ApiOperation(value = "商品详情", notes = "(GoodsDto)商品详情",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/goodsInfo")
    @ResponseBody
    public BaseResp goodsInfo(GoodsParam param) throws IOException{
        try {
            log.info("----》商品详情《----");
            log.info("参数："+param.toString());

            if(param.getGoodsId() == null){
                return toError("商品id不能为空！");
            }
            GoodsDto goodsDto;
            Integer userId = null;
            if(StringUtils.isNotEmpty(param.getToken())) {//商品详情验证用户
                //根据用户token获取userId
                userId = (Integer)schedualRedisService.get(param.getToken());
            }
            if(userId == null){
                //商品详情
                goodsDto = goodsInfoService.goodsInfo(param.getGoodsId());
            }else{
                //获取用户是否已关注此商品
                goodsDto = goodsInfoService.goodsInfoByToken(param.getGoodsId(),userId);
            }

            return toSuccess(goodsDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //分类列表
    @ApiOperation(value = "获取分类列表", notes = "(GoodsCategoryDto)获取分类列表",response = BaseResp.class)
    @GetMapping(value = "/categoryList")
    @ResponseBody
    public BaseResp categoryList() throws IOException {
        try {
            log.info("----》获取分类列表《----");
            //获取分类列表
            List<GoodsCategoryDto> categoryDtos = goodsInfoService.categoryList();
            return toSuccess(categoryDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //同类推荐
    @ApiOperation(value = "同类推荐", notes = "(GoodsDto)同类推荐",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/similarGoods")
    @ResponseBody
    public BaseResp similarGoods(GoodsParam param) throws IOException {
        try {
            log.info("----》同类推荐《----");
            log.info("参数："+param.toString());
            if(param.getGoodsId() == null){
                return toError("商品id不能为空！");
            }
            if(param.getStart() == null || param.getLimit() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            //同类推荐
            List<GoodsDto> goodsDtos = goodsInfoService.similarGoods(param.getGoodsId(),param.getStart(),param.getLimit());

            return toSuccess(goodsDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    //获取首页轮播图
    @ApiOperation(value = "获取首页轮播图", notes = "(BannerIndexDto)获取首页轮播图",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1首页主页 2商品详情 3积分商城 4首页专栏", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/getBanner")
    @ResponseBody
    public BaseResp getBanner(GoodsParam param) throws IOException{
        try {
            log.info("----》获取首页轮播图《----");
            //获取首页轮播图
            if(param.getType() == null){
                return toError("轮播图类型不能为空！");
            }
            List<BannerIndexDto> banners = goodsInfoService.getBanner(param.getType());

            return toSuccess(banners);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    //热门搜索
    @ApiOperation(value = "热门搜索", notes = "(SearchDto)热门搜索",response = BaseResp.class)
    @GetMapping(value = "/hotSearch")
    @ResponseBody
    public BaseResp hotSearch() throws IOException{
        try {
            log.info("----》热门搜索《----");
            //热门搜索
            List<SearchDto> searchDtos = goodsInfoService.hotSearch();
            return toSuccess(searchDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    //热门分类
    @ApiOperation(value = "热门分类", notes = "(GoodsCategoryDto)热门分类",response = BaseResp.class)
    @GetMapping(value = "/hotCategary")
    @ResponseBody
    public BaseResp hotCategary() throws IOException{
        try {
            log.info("----》热门分类《----");
            //热门分类
            List<GoodsCategoryDto> goodsCategoryDtos = goodsInfoService.hotCategary();
            return toSuccess(goodsCategoryDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    //获取快速查询条件
    @ApiOperation(value = "获取快速查询条件", notes = "(GoodsQuickSearchDto)获取快速查询条件",response = BaseResp.class)
    @GetMapping(value = "/quickSearch")
    @ResponseBody
    public BaseResp quickSearch() throws IOException{
        try {
            log.info("----》获取快速查询条件《----");
            //获取快速查询条件
            List<GoodsQuickSearchDto> goodsQuickSearchDtos = goodsInfoService.quickSearch();
            return toSuccess(goodsQuickSearchDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "置顶功能", notes = "(void)用户对自己的商品置顶，需要支付金额，会员每日限免3/8次 ",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID",required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "支付方式 1微信 2支付宝 3余额 4小程序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码",required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/setGoodsTop")
    @ResponseBody
    public BaseResp setGoodsTop(GoodsParam param) throws IOException {
        try {
            log.info("----》置顶功能《----");
            log.info("参数：" + param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getGoodsId() == null){
                return toError("商品ID不能为空！");
            }
            //申请商品置顶
            Object payInfo = goodsInfoService.setGoodsTop(userId, param.getGoodsId(), param.getPayType(), param.getPayPwd());
            return toSuccess(payInfo);
        } catch (ServiceException e) {
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "申请专栏回调接口", notes = "申请专栏", response = BaseResp.class)
    @PostMapping(value = "/notify/wechat")
    @ResponseBody
    public String notify(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String resXml = "";

        try{
            //把如下代码贴到的你的处理回调的servlet 或者.do 中即可明白回调操作
            wechatLog.info("微信支付小方圆回调数据开始");


            //示例报文
            //		String xml = "<xml><appid><![CDATA[wxb4dc385f953b356e]]></appid><bank_type><![CDATA[CCB_CREDIT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1228442802]]></mch_id><nonce_str><![CDATA[1002477130]]></nonce_str><openid><![CDATA[o-HREuJzRr3moMvv990VdfnQ8x4k]]></openid><out_trade_no><![CDATA[1000000000051249]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[1269E03E43F2B8C388A414EDAE185CEE]]></sign><time_end><![CDATA[20150324100405]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1009530574201503240036299496]]></transaction_id></xml>";
            String inputLine;
            String notityXml = "";
            try {
                while ((inputLine = request.getReader().readLine()) != null) {
                    notityXml += inputLine;
                }
                request.getReader().close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            wechatLog.info("接收到的报文：" + notityXml);


//			Map m = parseXmlToList2(notityXml);
            Map m = WXPayUtil.xmlToMap(notityXml);
            WxPayResult wpr = new WxPayResult();
            if(m != null){
                wpr.setAppid(m.get("appid").toString());
                wpr.setBankType(m.get("bank_type").toString());
                wpr.setCashFee(m.get("cash_fee").toString());
                //wpr.setFeeType(m.get("fee_type").toString());
                //wpr.setIsSubscribe(m.get("is_subscribe").toString());
                wpr.setMchId(m.get("mch_id").toString());
                wpr.setNonceStr(m.get("nonce_str").toString());
                wpr.setOpenid(m.get("openid").toString());
                wpr.setOutTradeNo(m.get("out_trade_no").toString());
                wpr.setResultCode(m.get("result_code").toString());
                wpr.setReturnCode(m.get("return_code")==null?"":m.get("return_code").toString());
                wpr.setSign(m.get("sign").toString());
                wpr.setTimeEnd(m.get("time_end").toString());
                wpr.setTotalFee(m.get("total_fee").toString());
                wpr.setTradeType(m.get("trade_type").toString());
                wpr.setTransactionId(m.get("transaction_id").toString());
            }
            wechatLog.info("返回信息："+wpr.toString());
            if("SUCCESS".equals(wpr.getResultCode())){
                //支付成功
                boolean result = goodsInfoService.updateOrder(wpr.getOutTradeNo(), wpr.getTransactionId(),1);

                wechatLog.info("支付成功！");
                if(result){
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                    wechatLog.info("处理成功！");
                }else{
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[FAILD]]></return_msg>" + "</xml> ";

                    wechatLog.info("处理失败！");
                }


            }else{
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                wechatLog.info("支付失败！");
            }

            wechatLog.info("微信支付回调结束");


        } catch (Exception e) {
            e.printStackTrace();
            wechatLog.error("微信通知后台处理系统出错", e);
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return resXml;
    }

    //支付宝回调
    @ApiOperation(value = "申请官方鉴赏支付宝回调接口", notes = "支付宝回调", response = BaseResp.class,hidden = true)
    @RequestMapping(value = "/notify/alipay", method = RequestMethod.POST)
    @ResponseBody
    public String orderNotify(HttpServletRequest request) throws IOException {

        log.info("-----------支付宝后台通知-----------");
        //HttpServletRequest request = getRequest();
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        String response = "";
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);

        }
        for (String key : params.keySet()) {
            response += key + "=" + params.get(key) + ",";
        }
        if (response.equals("")) {
            log.warn("无数据返回");
            return "";
        }
        log.warn("支付宝响应报文[订单号:" + params.get("out_trade_no") + "]：" + response);
        String ret = "";
        try {
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            // 商户订单号
            String out_trade_no = new String(request.getParameter(
                    "out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            log.info("商户订单号：" + out_trade_no);
            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no")
                    .getBytes("ISO-8859-1"), "UTF-8");
            log.info("支付宝交易号：" + trade_no);
            // 交易状态
            String trade_status = new String(request.getParameter(
                    "trade_status").getBytes("ISO-8859-1"), "UTF-8");

            log.info("交易状态：" + trade_status);
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            log.info("支付响应报文开始验证");
            if (AlipayNotify.verify(params)) {// 验证成功

                log.info("支付宝支付验证成功[订单号:" + trade_no + "]");
                // ////////////////////////////////////////////////////////////////////////////////////////
                // 请在这里加上商户的业务逻辑程序代码

                // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

                if (trade_status.equals("TRADE_FINISHED")) {

                    log.info("支付宝支付完成！TRADE_FINISHED");
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序
//                    boolean result = orderService.saveNotify(out_trade_no, trade_no,Type.PAYTYPE_ALIPAY.getValue());

                    boolean result = goodsInfoService.updateOrder(out_trade_no, trade_no,2);
                    if(result){
                        ret = "success"; // 请不要修改或删除
                    }else{
                        ret = "fail";
                    }
                    // 注意：
                    // 该种交易状态只在两种情况下出现
                    // 1、开通了普通即时到账，买家付款成功后。
                    // 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
                } else if (trade_status.equals("TRADE_SUCCESS")) {

                    log.info("支付宝支付完成！TRADE_SUCCESS");

                    // service.doUpdate(out_trade_no);
                    boolean result = goodsInfoService.updateOrder(out_trade_no, trade_no,2);
                    if(result){
                        ret = "success"; // 请不要修改或删除
                    }else{
                        ret = "fail";
                    }
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序

                    // 注意：
                    // 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
                }else{
                    ret = "fail";
                }

                // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——


                // ////////////////////////////////////////////////////////////////////////////////////////
            } else {// 验证失败
                log.error("支付宝支付验证失败！");
                ret = "fail";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付宝通知后台处理系统出错", e);
            ret = "fail";
        }

        return ret;

    }

}
