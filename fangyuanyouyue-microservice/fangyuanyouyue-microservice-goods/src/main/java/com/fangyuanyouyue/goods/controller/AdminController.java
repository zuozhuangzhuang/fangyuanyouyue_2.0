package com.fangyuanyouyue.goods.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.model.BannerIndex;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.GoodsInfoService;
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
@RequestMapping(value = "/admin")
@Api(description = "商品管理系统Controller")
@RefreshScope
public class AdminController  extends BaseController {

    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private GoodsInfoService goodsInfoService;


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
    public BaseResp addBanner(GoodsParam param) throws IOException {
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

}
