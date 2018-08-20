package com.fangyuanyouyue.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.*;
import com.fangyuanyouyue.goods.model.BannerIndex;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.model.OrderInfo;
import com.fangyuanyouyue.goods.model.OrderRefund;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/goods")
@Api(description = "商品系统Controller")
@RefreshScope
public class GoodsController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
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
            @ApiImplicitParam(name = "status", value = "状态 1出售中 2已售出 3（抢购）已结束 5删除", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索词条", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "synthesize", value = "综合 1：综合排序 2：信用排序 3：价格升序 4：价格降序",dataType = "int", paramType = "query"),
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
            if(StringUtils.isNotEmpty(param.getToken())){//我的商品验证用户
                //根据用户token获取userId
                Integer userId = (Integer)schedualRedisService.get(param.getToken());
                String verifyUser = schedualUserService.verifyUserById(userId);
                JSONObject jsonObject = JSONObject.parseObject(verifyUser);
                if((Integer)jsonObject.get("code") != 0){
                    return toError(jsonObject.getString("report"));
                }
                param.setUserId(userId);
            }else{
                if(param.getQuality() != null && param.getQuality() == 4){//我的关注，要求登录授权
                    return toError("未登录，无法获取我的关注！");
                }
            }
            if(param.getQuality() != null && param.getQuality() == 5){
                if(param.getType() != 2){
                    return toError("只有抢购可选已完成！");
                }
            }
            //获取商品列表
            List<GoodsDto> goodsDtos = goodsInfoService.getGoodsInfoList(param);
            return toSuccess(goodsDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "发布商品/抢购", notes = "(void)发布商品/抢购",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsInfoName", value = "商品名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）", required = true,allowMultiple = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "商品描述(详情)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "商品价格", required = true, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "postage", value = "运费", required = true, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "floorPrice", value = "最低价", dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "intervalTime", value = "降价时间间隔，单位：秒", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "markdown", value = "降价幅度", dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通商品 2抢购商品", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "imgUrls", value = "商品图片路径数组", required = true,allowMultiple = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoUrl", value = "视频路径",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoImg", value = "视频截图路径",dataType = "String", paramType = "query"),
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
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            //验证实名认证
            if(JSONObject.parseObject(schedualUserService.isAuth(userId)).getBoolean("data") == false){
                return toError("用户未实名认证！");
            }
            JSONObject user = JSONObject.parseObject(jsonObject.getString("data"));
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
            if(param.getPostage() == null){
                return  toError("商品运费不能为空！");
            }
            if(param.getType() == null){
                return  toError("商品类型不能为空！");
            }
            if(param.getImgUrls() == null || param.getImgUrls().length == 0){
                return toError("请至少上传一张图片！");
            }
            if(param.getType() == 2){//抢购
                if(param.getFloorPrice() == null){
                    return toError("最低价不能为空！");
                }
                if(param.getIntervalTime() == null){
                    return toError("降价时间间隔不能为空！");
                }
                if(param.getMarkdown() == null){
                    return toError("降价幅度不能为空！");
                }
            }
            goodsInfoService.addGoods(userId,user.getString("nickName"),param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
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
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getGoodsIds() != null && param.getGoodsIds().length<1){
                return toError("商品ID不能为空！");
            }
            for(Integer goodsId:param.getGoodsIds()){
                //TODO 1、商品是否存在订单 2、订单是否已完成或已取消 3、订单已退款：是否已完成退款、是否已拒绝退款
                GoodsInfo goodsInfo = goodsInfoService.selectByPrimaryKey(goodsId);
                if(goodsInfo.getStatus() == 2){
                    OrderInfo orderInfo = orderInfoService.selectOrderByGoodsId(userId,goodsId);
                    //1、判断是否在退货 2、判断是否已完成、已取消
                    OrderRefund orderRefund = orderInfoService.seletRefundByOrderId(orderInfo.getId());
                    if((orderInfo.getStatus().intValue() != 4 && orderInfo.getStatus().intValue() != 5) || (orderRefund != null && orderRefund.getStatus() == 1) ){
                        return toError("商品【"+goodsInfo.getName()+"】存在未完成订单，请勿删除！");
                    }
                }
            }

            //TODO 批量删除商品
            goodsInfoService.deleteGoods(userId,param.getGoodsIds());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
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
            @ApiImplicitParam(name = "videoUrl", value = "视频路径",dataType = "String", paramType = "query")
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
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if((Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            goodsInfoService.modifyGoods(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
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
            if(StringUtils.isNotEmpty(param.getToken())){//商品详情验证用户
                //根据用户token获取userId
                Integer userId = (Integer)schedualRedisService.get(param.getToken());
                String verifyUser = schedualUserService.verifyUserById(userId);
                JSONObject jsonObject = JSONObject.parseObject(verifyUser);
                if((Integer)jsonObject.get("code") != 0){
                    return toError(jsonObject.getString("report"));
                }
                param.setUserId(userId);
                //获取用户是否已关注此商品
                goodsDto = goodsInfoService.goodsInfoByToken(param.getGoodsId(),param.getUserId());
            }else{
                //商品详情
                goodsDto = goodsInfoService.goodsInfo(param.getGoodsId());
            }

            return toSuccess(goodsDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
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
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    //获取首页轮播图
    @ApiOperation(value = "获取首页轮播图", notes = "(BannerIndexDto)获取首页轮播图",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1首页 2商城", required = true, dataType = "int", paramType = "query")
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

    //新增首页轮播图
    @ApiOperation(value = "新增首页轮播图", notes = "(BannerIndex)新增首页轮播图",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessId", value = "业务ID:商品ID/用户ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "jumpType", value = "跳转类型 1页面 2链接 3图片（businessId为空）", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "businessType", value = "业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1首页 2商城", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "描述标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrl", value = "图片地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "是否展示，1展示 2不展示", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/addBanner")
    @ResponseBody
    public BaseResp addBanner(GoodsParam param) throws IOException{
        try {
            log.info("----》新增首页轮播图《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getImgUrl())){
                return toError("图片地址不能为空！");
            }
            if(param.getBusinessId() == null){
                return toError("业务ID不能为空！");
            }
            if(param.getType() == null){
                return toError("业务类型不能为空！");
            }
            if(param.getJumpType() == null){
                return toError("跳转类型不能为空！");

            }
            //新增首页轮播图
            BannerIndex banner = goodsInfoService.addBanner(param);

            return toSuccess(banner);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //修改首页轮播图
    @ApiOperation(value = "修改首页轮播图", notes = "(BannerIndex)修改首页轮播图",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bannerIndexId", value = "轮播图ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "businessId", value = "业务ID:商品ID/用户ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "jumpType", value = "跳转类型,0:商品 1：个人", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "业务类型,0:商品 1：个人", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "描述标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrl", value = "图片地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "是否下架，0未下架 1下架", dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/updateBanner")
    @ResponseBody
    public BaseResp updateBanner(GoodsParam param) throws IOException{
        try {
            log.info("----》修改首页轮播图《----");
            log.info("参数：" + param.toString());
            if (param.getBannerIndexId() == null) {
                return toError( "轮播图ID不能为空！");
            }
            if (StringUtils.isEmpty(param.getImgUrl())) {
                return toError( "图片地址不能为空！");
            }
            if (param.getBusinessId() == null) {
                return toError( "业务ID不能为空！");
            }
            if (param.getType() == null) {
                return toError( "业务类型不能为空！");
            }
            if (param.getJumpType() == null) {
                return toError( "跳转类型不能为空！");

            }
            //修改首页轮播图
            BannerIndex banner = goodsInfoService.updateBanner(param);

            return toSuccess(banner);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
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
            return toError(e.getMessage());
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
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    //我拍下的商品
//
//    //我卖出的商品
//    @ApiOperation(value = "我卖出的商品", notes = "我卖出的商品")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "search", value = "搜索内容（标题和内容）", dataType = "string", paramType = "query")
//    })
//    @PostMapping(value = "/sellGoods")
//    @ResponseBody
//    public String sellGoods(GoodsParam param) throws IOException {
//        try {
//            log.info("----》我卖出的商品《----");
//            log.info("参数："+param.toString());
//
//            if(param.getGoodsId()==null || param.getGoodsId().intValue()==0){
//                return toError("商品id不能为空！");
//            }
//            if(param.getCatalogId()==null || param.getCatalogId().intValue()==0){
//                return toError("推荐类型不能为空！");
//            }
//            if(param.getStart()==null){
//                return toError("start不能为空！");
//            }
//            if(param.getLimit()==null){
//                return toError("limit不能为空！");
//            }
//            //我卖出的商品
//
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "获取我卖出的商品成功！");
//            return toResult(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//    //我的收藏
//    @ApiOperation(value = "我的收藏", notes = "我的收藏")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "search", value = "搜索内容（标题和内容）", dataType = "string", paramType = "query")
//    })
//    @PostMapping(value = "/myCollect")
//    @ResponseBody
//    public String myCollect(GoodsParam param) throws IOException {
//        try {
//            log.info("----》我的收藏《----");
//            log.info("参数："+param.toString());
//
//            if(param.getGoodsId()==null || param.getGoodsId().intValue()==0){
//                return toError("商品id不能为空！");
//            }
//            if(param.getCatalogId()==null || param.getCatalogId().intValue()==0){
//                return toError("推荐类型不能为空！");
//            }
//            if(param.getStart()==null){
//                return toError("start不能为空！");
//            }
//            if(param.getLimit()==null){
//                return toError("limit不能为空！");
//            }
//            //我的收藏
//
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "获取我的收藏成功！");
//            return toResult(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//    //他的商品
//    @ApiOperation(value = "他的商品", notes = "他的商品")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "search", value = "搜索内容（标题和内容）", dataType = "string", paramType = "query")
//    })
//    @PostMapping(value = "/otherGoods")
//    @ResponseBody
//    public String otherGoods(GoodsParam param) throws IOException {
//        try {
//            log.info("----》他的商品《----");
//            log.info("参数："+param.toString());
//
//            if(param.getGoodsId()==null || param.getGoodsId().intValue()==0){
//                return toError("商品id不能为空！");
//            }
//            if(param.getCatalogId()==null || param.getCatalogId().intValue()==0){
//                return toError("推荐类型不能为空！");
//            }
//            if(param.getStart()==null){
//                return toError("start不能为空！");
//            }
//            if(param.getLimit()==null){
//                return toError("limit不能为空！");
//            }
//            //他的商品
//
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "获取他的商品成功！");
//            return toResult(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//    //商品鉴定申请
//    @ApiOperation(value = "商品鉴定申请", notes = "商品鉴定申请")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "file", value = "商品图片（6张图片）file1~file6", dataType = "file", paramType = "query"),
//            @ApiImplicitParam(name = "content", value = "分页limit", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "title", value = "搜索内容（标题和内容）", dataType = "string", paramType = "query")
//    })
//    @PostMapping(value = "/authenticate")
//    @ResponseBody
//    public String authenticate(GoodsParam param) throws IOException {
//        try {
//            log.info("----》商品鉴定申请《----");
//            log.info("参数："+param.toString());
//            if(StringUtils.isEmpty(param.getToken())){
//                return toError("用户token不能为空！");
//            }
//            if(StringUtils.isEmpty(param.getTitle())){
//                return toError("商品标题不能为空！");
//            }
////            AUser user=userService.getByToken(param.getToken());
////            if(user==null){
////                return toError("登录超时，请重新登录！");
////            }
////            if(StringUtils.isNotEmpty(user.getStatus()) && "1".equals(user.getStatus())){
////                return toError("您的账号已被冻结，请联系管理员！");
////            }
//            if(StringUtils.isEmpty(param.getContent())){
//                return toError("商品介绍不能为空!");
//            }
//            //商品鉴定申请
//
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "商品鉴定申请成功！");
//            return toResult(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//    //我的鉴定列表
//    @ApiOperation(value = "我的鉴定列表", notes = "我的鉴定列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query")
//    })
//    @PostMapping(value = "/authenticateList")
//    @ResponseBody
//    public String authenticateList(GoodsParam param) throws IOException {
//        try {
//            log.info("----》我的鉴定列表《----");
//            log.info("参数："+param.toString());
//
//            if(param.getGoodsId()==null || param.getGoodsId().intValue()==0){
//                return toError("商品id不能为空！");
//            }
//            if(param.getCatalogId()==null || param.getCatalogId().intValue()==0){
//                return toError("推荐类型不能为空！");
//            }
//            if(param.getStart()==null){
//                return toError("start不能为空！");
//            }
//            if(param.getLimit()==null){
//                return toError("limit不能为空！");
//            }
//            //我的鉴定列表
//
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "获取我的鉴定列表成功！");
//            return toResult(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//
//    //收藏商品/取消收藏
//    @ApiOperation(value = "收藏商品/取消收藏", notes = "收藏商品/取消收藏 ")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "收藏类型 0收藏商品   1取消收藏", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true,  dataType = "int", paramType = "query")
//    })
//    @PostMapping(value = "/collectGoods")
//    @ResponseBody
//    public String collectGoods(GoodsParam param) throws IOException {
//        try {
//            log.info("----》收藏商品/取消收藏《----");
//            log.info("参数："+param.toString());
//            if(StringUtils.isEmpty(param.getToken())){
//                return toError("用户token不能为空！");
//            }
//            if(param.getGoodsId()==null){
//                return toError("商品id不能为空！");
//            }
//            if(StringUtils.isEmpty(param.getType())){
//                return toError("收藏类型不能为空！");
//            }
//
////            ResponseEntity<List> responseEntity = restTemplate.postForObject("http://localhost/user/login");
//
////            AUser user=userService.getByToken(param.getToken());
////            if(user==null){
////                return toError("登录超时，请重新登录！");
////            }
////            if(StringUtils.isNotEmpty(user.getStatus()) && "1".equals(user.getStatus())){
////                return toError("您的账号已被冻结，请联系管理员！");
////            }
////            AGoods goods=goodsService.get(param.getGoodsId());
////            if(goods==null){
////                return toError("此商品不存在,参数有误！");
////            }
//            //收藏商品/取消收藏
//
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "收藏商品/取消收藏成功！");
//            return toResult(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }

}
