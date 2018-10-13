package com.fangyuanyouyue.wallet.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.admin.AdminWithdrawDto;
import com.fangyuanyouyue.wallet.param.AdminWalletParam;
import com.fangyuanyouyue.wallet.service.PlatformFinanceService;
import com.fangyuanyouyue.wallet.service.UserVipService;
import com.fangyuanyouyue.wallet.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

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
    @Autowired
    private UserVipService userVipService;


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
            @ApiImplicitParam(name = "orderType", value = "订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "计算方式 1总账 2平台纯收支", required = true, dataType = "int", paramType = "query")
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
            if(param.getStatus() == null){
                return toError("计算方式不能为空！");
            }
            Pager pager = platformFinanceService.platformFinance(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
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
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "提现申请管理", notes = "提现申请管理",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "提现方式 1微信 2支付宝", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "申请状态 1申请中 2提现成功 3申请拒绝", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/withdrawList")
    @ResponseBody
    public BaseResp withdrawList(AdminWalletParam param) throws IOException {
        try {
            log.info("提现申请管理");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            Pager pager = walletService.withdrawList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改提现申请", notes = "修改提现申请",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "申请信息id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 2同意 3拒绝", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "处理原因", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateWithdraw")
    @ResponseBody
    public BaseResp updateWithdraw(AdminWalletParam param) throws IOException {
        try {
            log.info("修改提现申请");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("申请信息id不能为空！");
            }
            if(param.getStatus().equals(Status.WITHDRAW_REFUSE.getValue())){
                if(StringUtils.isEmpty(param.getContent())){
                    return toError("拒绝原因不能为空！");
                }
            }
            walletService.updateWithdraw(param.getId(),param.getStatus(),param.getContent());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "编辑用户余额", notes = "编辑用户余额",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1增加 2减少", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "金额", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateUserBalance")
    @ResponseBody
    public BaseResp updateUserBalance(AdminWalletParam param) throws IOException {
        try {
            log.info("----》编辑用户余额《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("用户id不能为空！");
            }
            if(param.getType()==null){
                return toError("操作类型不能为空！");
            }
            if(param.getAmount()==null){
                return toError("金额不能为空");
            }

            walletService.updateUserBalance(param.getId(),param.getType(),param.getAmount());

            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }


    @ApiOperation(value = "修改冻结用户余额", notes = "修改冻结用户余额",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1冻结余额 2解除冻结金额", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "金额", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateUserConfined")
    @ResponseBody
    public BaseResp updateUserConfined(AdminWalletParam param) throws IOException {
        try {
            log.info("----》修改冻结用户余额《----");
            log.info("参数："+param.toString());
            if(param.getId() == null){
                return toError("用户id不能为空！");
            }
            if(param.getType()==null){
                return toError("操作类型不能为空！");
            }
            if(param.getAmount()==null){
                return toError("金额不能为空");
            }

            walletService.updateUserFrozen(param.getId(),param.getType(),param.getAmount());

            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统错误！");
        }
    }

    @ApiOperation(value = "后台设置限制余额用户", notes = "后台设置限制余额用户",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 1限制 2解除限制",  required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/confinedUser")
    @ResponseBody
    public BaseResp confinedUser(AdminWalletParam param) throws IOException {
        try {
            log.info("----》后台设置限制用户使用余额《----");
            log.info(param.toString());
            if(param.getId() == null){
                return toError("用户id不能为空！");
            }
            if(param.getType() == null){
                return toError("状态不能为空！");
            }
            walletService.confinedUser(param.getId(), param.getStatus());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改用户会员信息", notes = "修改用户会员信息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vipLevel", value = "会员等级 1铂金会员 2至尊会员",  required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "vipType", value = "会员类型 1一个月 2三个月 3一年会员",  required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1开通 2续费 3取消",required = true, dataType = "int", paramType = "query"),
    })
    @PostMapping(value = "/updateUserVip")
    @ResponseBody
    public BaseResp updateUserVip(AdminWalletParam param) throws IOException {
        try {
            log.info("----》修改用户会员信息《----");
            log.info(param.toString());
            if(param.getId() == null){
                return toError("用户id不能为空！");
            }
            if(param.getVipLevel()==null){
                return toError("会员等级不能为空！");
            }
            if(param.getVipType() == null){
                return toError("会员类型不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            userVipService.updateUserVip(param.getId(), param.getVipLevel(),param.getVipType(),param.getType());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "会员列表", notes = "会员列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "搜索词条", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序规则", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ascType", value = "排序类型 1升序 2降序", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "vipLevel", value = "会员等级 1铂金会员 2至尊会员", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "vipType", value = "会员类型 1一个月 2三个月 3一年会员", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "isSendMessage", value = "是否发送7天后到期信息 1是 2否", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/vipList")
    @ResponseBody
    public BaseResp vipList(AdminWalletParam param) throws IOException {
        try {
            log.info("----》会员列表《----");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }

            Pager pager = userVipService.vipList(param);
            return toPage(pager);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }





}
