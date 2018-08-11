package com.fangyuanyouyue.wallet.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.SchedualRedisService;
import com.fangyuanyouyue.wallet.service.SchedualUserService;
import com.fangyuanyouyue.wallet.service.WalletService;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping(value = "/walletFeign")
@Api(description = "外部调用系统Controller",hidden = true)
@RefreshScope
public class FeignController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;

    @PostMapping(value = "/updateScore")
    @ResponseBody
    public BaseResp updateScore(WalletParam param) throws IOException {
        try {
            log.info("----》修改积分《----");
            log.info("参数："+param.toString());
            if(param.getUserId() == null){
                return toError("用户ID不能为空！");
            }
            if(param.getScore() == null){
                return toError("积分错误！");
            }
            if(param.getType() == null){
                return toError("类型错误！");
            }
            //修改积分
            walletService.updateScore(param.getUserId(),param.getScore(),param.getType());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/updateBalance")
    @ResponseBody
    public BaseResp updateBalance(WalletParam param) throws IOException {
        try {
            log.info("----》修改余额《----");
            log.info("参数："+param.toString());
            if(param.getUserId() == null){
                return toError("用户ID不能为空！");
            }
            if(param.getAmount() == null){
                return toError("修改金额错误！");
            }
            if(param.getType() == null){
                return toError("类型错误！");
            }
            //修改余额
            walletService.updateBalance(param.getUserId(),param.getAmount(),param.getType());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @GetMapping(value = "/getAppraisalCount")
    @ResponseBody
    public BaseResp getAppraisalCount(Integer userId) throws IOException {
        try {
            log.info("----》获取免费鉴定次数《----");
            log.info("参数：userId："+userId);
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            //获取免费鉴定次数
            Integer count = walletService.getAppraisalCount(userId);
            return toSuccess(count);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }



    @PostMapping(value = "/updateAppraisalCount")
    @ResponseBody
    public BaseResp updateAppraisalCount(Integer userId,Integer count) throws IOException {
        try {
            log.info("----》修改剩余免费鉴定次数《----");
            log.info("参数：userId："+userId);
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            if(count == null){
                return toError("修改数值不能为空！");
            }
            //修改剩余免费鉴定次数
            walletService.updateAppraisalCount(userId,count);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }
}
