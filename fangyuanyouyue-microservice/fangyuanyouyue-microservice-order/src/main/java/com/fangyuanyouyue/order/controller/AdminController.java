package com.fangyuanyouyue.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.order.dto.CompanyDto;
import com.fangyuanyouyue.order.dto.OrderDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderDto;
import com.fangyuanyouyue.order.dto.adminDto.AdminOrderProcessDto;
import com.fangyuanyouyue.order.param.AdminOrderParam;
import com.fangyuanyouyue.order.param.OrderParam;
import com.fangyuanyouyue.order.service.*;
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
@RequestMapping(value = "/adminOrder")
@Api(description = "订单系统后台管理相关Controller")
@RefreshScope
public class AdminController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualGoodsService schedualGoodsService;
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private RefundService refundService;


    @ApiOperation(value = "查看所有用户订单", notes = "订单管理、退货管理",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1待支付 2待发货 3待收货 4已完成 5已取消 7退货", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/orderList")
    @ResponseBody
    public BaseResp orderList(AdminOrderParam param) throws IOException {
        try {
            log.info("----》查看所有用户订单《----");
            log.info("参数："+param.toString());
            //参数判断
            //验证用户
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            //查看所有用户订单
            Pager pager = orderService.orderList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
    
    

    @ApiOperation(value = "查看退货订单", notes = "订单退货",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1申请退货 2退货成功 3拒绝退货", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/refundList")
    @ResponseBody
    public BaseResp refundList(AdminOrderParam param) throws IOException {
        try {
            log.info("----》查看退货订单《----");
            log.info("参数："+param.toString());
            //参数判断
            //验证用户
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            //查看所有用户订单
            Pager pager = refundService.refundList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "官方处理退货", notes = "官方处理退货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "退货信息id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "处理理由", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 2同意 3拒绝",required = true, dataType = "int", paramType = "query")
    })
    @PutMapping(value = "/platformDealReturns")
    @ResponseBody
    public BaseResp platformDealReturns(OrderParam param) throws IOException {
        try {
            log.info("----》官方处理退货《----");
            log.info("参数："+param.toString());
            if(param.getOrderId() == null){
                return toError("id不能为空！");
            }
            if(param.getStatus() == null){
                return toError("处理状态不能为空！");
            }
            //官方处理退货
            refundService.platformDealReturns(null,param.getOrderId(),param.getReason(),param.getStatus());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "物流公司列表", notes = "物流公司列表")
    @GetMapping(value = "/companyList")
    @ResponseBody
    public BaseResp companyList(AdminOrderParam param) throws IOException {
        try {
            log.info("----》物流公司列表《----");
            log.info("参数："+param.toString());
            Pager pager = orderService.companyList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "增加物流公司", notes = "增加物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "物流公司编号",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "物流公司名",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "参考价格",required = true, dataType = "BigDecimal", paramType = "query")
    })
    @PostMapping(value = "/addCompany")
    @ResponseBody
    public BaseResp addCompany(AdminOrderParam param) throws IOException {
        try {
            log.info("----》增加物流公司《----");
            log.info("参数："+param.toString());
            
            if(param.getNumber() == null){
                return toError("物流公司编号不能为空！");
            }
            if(param.getName() == null){
                return toError("物流公司名不能为空！");
            }

            if(param.getId()!=null&&param.getId()>0) {

                orderService.updateCompany(param.getId(),param.getNumber(),param.getName(),param.getPrice(), param.getStatus());
            }else {
                orderService.addCompany(param.getNumber(),param.getName(),param.getPrice());
            	
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

    @ApiOperation(value = "修改物流公司信息", notes = "修改、删除物流公司信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "物流公司id",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "number", value = "物流公司编号",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "物流公司名",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "参考价格",required = false, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1正常 2删除",required = false, dataType = "int", paramType = "query")
    })
    @PutMapping(value = "/updateCompany")
    @ResponseBody
    public BaseResp updateCompany(AdminOrderParam param) throws IOException {
        try {
            log.info("----》修改物流公司信息《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("物流公司id不能为空！");
            }
           orderService.updateCompany(param.getId(),param.getNumber(),param.getName(),param.getPrice(), param.getStatus());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "后台获取订单详情", notes = "(AdminOrderDto)订单详情",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单ID",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/orderDetail")
    @ResponseBody
    public BaseResp orderDetail(AdminOrderParam param) throws IOException {
        try {
            log.info("----》后台获取订单详情《----");
            log.info("参数："+param.toString());
            //参数判断
            if(param.getId()==null){
                return toError("订单ID不能为空！");
            }
            //订单详情
            AdminOrderDto orderDto = orderService.adminOrderDetail(param.getId());
            return toSuccess(orderDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "后台统计订单", notes = "(AdminOrderProcessDto)后台统计订单",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态 1待支付 2待发货 3待收货 4已完成 5已取消 7退货", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1今日订单 2一周订单 3月订单", required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/orderProcess")
    @ResponseBody
    public BaseResp orderProcess(AdminOrderParam param) throws IOException {
        try {
            log.info("----》后台统计订单《----");
            log.info("参数："+param.toString());
            //参数判断
            if(param.getId()==null){
                return toError("订单ID不能为空！");
            }
            //订单详情
            AdminOrderProcessDto dto = orderService.getOrderProcess(param.getStatus(),param.getStartDate(),param.getEndDate());
            return toSuccess(dto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



}
