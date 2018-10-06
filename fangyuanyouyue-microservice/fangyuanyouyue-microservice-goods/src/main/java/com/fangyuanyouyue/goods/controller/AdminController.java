package com.fangyuanyouyue.goods.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.adminDto.AdminBannerDto;
import com.fangyuanyouyue.goods.dto.adminDto.AdminGoodsDto;
import com.fangyuanyouyue.goods.model.GoodsCategory;
import com.fangyuanyouyue.goods.param.AdminGoodsParam;
import com.fangyuanyouyue.goods.service.AppraisalService;
import com.fangyuanyouyue.goods.service.BannerService;
import com.fangyuanyouyue.goods.service.GoodsInfoService;
import com.fangyuanyouyue.goods.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/adminGoods")
@Api(description = "商品管理系统Controller")
@RefreshScope
public class AdminController  extends BaseController {

    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private BannerService bannerService;
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private AppraisalService appraisalService;

    @ApiOperation(value = "新增首页轮播图", notes = "(BannerIndex)新增首页轮播图",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessId", value = "业务ID：例：商品id、视频id、积分商城商品id...", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "jumpType", value = "跳转类型 1页面 2链接 3图片（businessId为空）", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "businessType", value = "业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏 7积分商品", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1首页主页 2商品详情 3积分商城 4首页专栏", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "描述标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrl", value = "图片地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/addBanner")
    @ResponseBody
    public BaseResp addBanner(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》新增首页轮播图《----");
            log.info("参数："+param.toString());
            
            if(param.getId()!=null&&param.getId()>0) {

                if(param.getBusinessType() != null){
                	 if(param.getBusinessId() == null){
                         return toError("业务ID不能为空！");
                     }
                }
                //修改首页轮播图
                bannerService.updateBanner(param);
            }else { 
                if(param.getJumpType() == null){
                    return toError("跳转类型不能为空！");
                }
                if(param.getBusinessType() != null){
                	 if(param.getBusinessId() == null){
                         return toError("业务ID不能为空！");
                     }
                }
                if(param.getType() == null){
                    return toError("类型不能为空！");
                }
                if(StringUtils.isEmpty(param.getTitle())){
                    return toError("描述标题不能为空！");
                }
                if(StringUtils.isEmpty(param.getImgUrl())){
                    return toError("图片地址不能为空！");
                }
                if(param.getSort() == null){
                    return toError("排序不能为空！");
                }
                //新增首页轮播图
                bannerService.addBanner(param);
            	
            }
           

            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "删除轮播图", notes = "删除轮播图",response = BaseResp.class)
    @PostMapping(value = "/deleteBanner")
    @ResponseBody
    public BaseResp deleteBanner(Integer bannerId) throws IOException{
        try {
            log.info("----》删除轮播图《----");
            log.info("参数：" + bannerId);
            //查看首页轮播图
            if(bannerId == null){
                return toError("id不能为空！");
            }
            bannerService.deleteBanner(bannerId);

            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "查看轮播图列表", notes = "查看首页轮播图列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jumpType", value = "跳转类型 1页面 2链接 3图片（businessId为空）", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "businessType", value = "业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏 7积分商品", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1首页 2商品详情 3积分商城", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/bannerList")
    @ResponseBody
    public BaseResp bannerList(AdminGoodsParam param) throws IOException{
        try {
            log.info("----》查看首页轮播图列表《----");
            log.info("参数：" + param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            //查看首页轮播图
            Pager pager = bannerService.bannerList(param);

            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "增加商品分类", notes = "增加商品分类",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "上级id", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "类目名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrl", value = "图片地址", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通 2热门", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 0正常 1禁用", required = false, dataType = "int", paramType = "query")
    })
    @PutMapping(value = "/addCategory")
    @ResponseBody
    public BaseResp addCategory(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》增加商品分类《----");
            log.info("参数："+param.toString());
            //获取分类列表
            goodsInfoService.addCategory(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改分类信息", notes = "修改分类信息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分类信息id", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "父类id", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "分类名字", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgUrl", value = "图片地址", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1普通 2热门", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 0正常 1禁用 2删除", required = false, dataType = "int", paramType = "query")
    })
    @PutMapping(value = "/updateCategory")
    @ResponseBody
    public BaseResp updateCategory(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》修改分类信息《----");
            log.info("参数："+param.toString());
            if(param.getId()!=null&&param.getId()>0) {
            	 //获取分类列表
                goodsInfoService.updateCategory(param.getId(),param.getParentId(), param.getName(),param.getImgUrl(), param.getSort(),param.getType(),param.getStatus());
              
            }else{

                //获取分类列表
                goodsInfoService.addCategory(param);
            }
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "查看商品分类列表", notes = "查看商品分类列表",response = BaseResp.class)
    @GetMapping(value = "/categoryList")
    @ResponseBody
    public BaseResp categoryList(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》查看商品分类列表《----");
            log.info("参数："+param.toString());
            //获取分类列表
            Pager pager = goodsInfoService.categoryPage(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
    


    @ApiOperation(value = "获取顶级分类列表", notes = "查看顶级分类列表",response = BaseResp.class)
    @GetMapping(value = "/categorys")
    @ResponseBody
    public BaseResp categorys() throws IOException {
        try {
            log.info("----》获取顶级菜单列表列表《----");
            
            //获取分类列表
            List<GoodsCategory> datas = goodsInfoService.getCategory();
            return toSuccess(datas);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    //获取商品列表
    @ApiOperation(value = "获取商品、抢购列表", notes = "获取商品、抢购列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1商品 2抢购", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/goodsList")
    @ResponseBody
    public BaseResp goodsList(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》获取商品、抢购列表《----");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            //获取商品列表
            Pager pager = goodsInfoService.getGoodsPage(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "查看商品详情", notes = "查看商品详情",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/goodsDetail")
    @ResponseBody
    public BaseResp goodsDetail(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》查看商品详情《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("商品id不能为空！");
            }
            //获取商品详情
            AdminGoodsDto dto = goodsInfoService.adminGoodsDetail(param.getId());
            return toSuccess(dto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "后台管理修改商品、抢购", notes = "(void)后台管理修改商品、抢购",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "goodsCategoryIds", value = "商品分类数组（同一商品可多种分类）",allowMultiple = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "商品状态 1出售中 2已售出 3已下架（已结束） 5删除",dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "isAppraisal", value = "是否官方鉴定 1已鉴定 2未鉴定",dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "（置顶、取消）商品排序 1置顶 2默认",dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "处理原因",dataType = "String", paramType = "query")
    })
    @PutMapping(value = "/updateGoods")
    @ResponseBody
    public BaseResp updateGoods(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》后台管理修改商品、抢购《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("id不能为空！");
            }
            goodsInfoService.updateGoods(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //TODO EXCEL 批量上传商品

    //后台处理举报
    @ApiOperation(value = "后台处理举报", notes = "后台处理举报，发送信息给被举报者",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "举报信息id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "处理原因", required = true, dataType = "int", paramType = "query")
    })
    @PutMapping(value = "/dealReport")
    @ResponseBody
    public BaseResp dealReport(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》后台处理举报《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("id不能为空！");
            }
            if(StringUtils.isEmpty(param.getContent())){
                return toError("处理原因不能为空！");
            }
            reportService.dealReport(param.getId(),param.getContent());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //获取举报商品列表
    @ApiOperation(value = "获取举报商品列表", notes = "(void)获取举报商品列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1商品 2抢购", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1已处理 2待处理", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/reportList")
    @ResponseBody
    public BaseResp reportList(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》获取举报商品列表《----");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            Pager pager = reportService.getGoodsReportPage(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "官方鉴定列表", notes = "官方鉴定列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "鉴定类型 1卖家鉴定 2买家鉴定 3我要鉴定", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 0申请 1真 2假 3存疑", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/appraisalList")
    @ResponseBody
    public BaseResp appraisalList(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》官方鉴定列表《----");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            Pager pager = appraisalService.appraisalList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }

    }

    @ApiOperation(value = "后台处理官方鉴定", notes = "后台处理官方鉴定",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "官方鉴定", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "处理原因", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1真 2假 3存疑", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "isShow", value = "是否鉴定展示 1是 2否", required = true, dataType = "int", paramType = "query")
    })
    @PutMapping(value = "/updateAppraisal")
    @ResponseBody
    public BaseResp updateAppraisal(AdminGoodsParam param) throws IOException {
        try {
            log.info("----》后台处理官方鉴定《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("id不能为空！");
            }
            appraisalService.updateAppraisal(param.getId(),param.getStatus(),param.getContent(),param.getIsShow());
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
