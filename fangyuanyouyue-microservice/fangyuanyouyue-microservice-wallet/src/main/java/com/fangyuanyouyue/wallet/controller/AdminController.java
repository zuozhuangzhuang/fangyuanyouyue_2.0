package com.fangyuanyouyue.wallet.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.wallet.param.AdminWalletParam;
import com.fangyuanyouyue.wallet.service.PlatformFinanceService;
import com.fangyuanyouyue.wallet.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping(value = "/adminWallet")
@Api(description = "后台管理系统Controller")
@RefreshScope
public class AdminController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private PlatformFinanceService platformFinanceService;
    @Autowired
    private WalletService walletService;


    @ApiOperation(value = "查看平台收支", notes = "查看平台收支",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "支付类型 1微信 2支付宝 3余额 4小程序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "收支类型 1收入 2支出", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orderType", value = "订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/platformFinance")
    @ResponseBody
    public BaseResp platformFinance(AdminWalletParam param) throws IOException {
        try {
            log.info("查看平台收支");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            Pager pager = platformFinanceService.platformFinance(param);
            return toPage(pager);
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "查看用户收支", notes = "查看用户收支",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "支付类型 1微信 2支付宝 3余额 4小程序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "收支类型 1收入 2支出 3退款", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orderType", value = "订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/userFinance")
    @ResponseBody
    public BaseResp userFinance(AdminWalletParam param) throws IOException {
        try {
            log.info("查看用户收支");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            Pager pager = walletService.userFinance(param);
            return toPage(pager);
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }
    //TODO 提现申请管理
    //TODO 冻结用户余额
    //TODO 修改用户扩展信息
}
