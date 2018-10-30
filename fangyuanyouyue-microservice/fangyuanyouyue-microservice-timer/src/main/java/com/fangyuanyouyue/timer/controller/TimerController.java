package com.fangyuanyouyue.timer.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.timer.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RefreshScope
public class TimerController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private SchedualGoodsService schedualGoodsService;
    @Autowired
    private SchedualOrderService schedualOrderService;
    @Autowired
    private SchedualWalletService schedualWalletService;
    @Autowired
    private SchedualForumService schedualForumService;
    @Autowired
    private SchedualUserService schedualUserService;

    @Scheduled(cron="0 * *  * * ? ")
    public BaseResp depreciate() throws IOException {
        try {
            log.info("----》抢购降价《----");
            schedualGoodsService.depreciate();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @Scheduled(cron="0 * *  * * ? ")
    public BaseResp refuseBargain() throws IOException {
        try {
            log.info("----》压价处理《----");
            schedualGoodsService.refuseBargain();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @Scheduled(cron="0 * *  * * ? ")
    public BaseResp cancelOrder() throws IOException {
        try {
            log.info("----》取消订单《----");
            schedualOrderService.cancelOrder();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

//    @Scheduled(cron="0 * *  * * ? ")
//    public BaseResp updateLevel() throws IOException {
//        try {
//            log.info("----》修改用户等级《----");
//            schedualWalletService.updateLevel();
//            return toSuccess();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }


    @Scheduled(cron="0 * *  * * ? ")
    public BaseResp saveReceiptGoods() throws IOException {
        try {
            log.info("----》12天自动收货《----");
            schedualOrderService.saveReceiptGoods();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @Scheduled(cron="0 * *  * * ? ")
    public BaseResp updateOrderRefund() throws IOException {
        try {
            log.info("----》自动处理退货《----");
            schedualOrderService.updateOrderRefund();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @Scheduled(cron="0 * *  * * ? ")
    public BaseResp cancelVip() throws IOException {
        try {
            log.info("----》会员到期《----");
            schedualWalletService.cancelVip();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @Scheduled(cron="0 * *  * * ? ")
    public BaseResp appraisalEnd() throws IOException {
        try {
            log.info("----》全民鉴定结束《----");
            schedualForumService.appraisalEnd();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @Scheduled(cron="0 0 0  * * ? ")
    public BaseResp shopAuthTimeOut() throws IOException {
        try {
            log.info("----》官方认证自动过期《----");
            schedualUserService.shopAuthTimeOut();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    @Scheduled(cron="0 0 8 * * ? ")
    public BaseResp dailyWage() throws IOException {
        try {
            log.info("----》专栏返利《----");
            schedualForumService.dailyWage();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @Scheduled(cron="0 * * * * ? ")
    public BaseResp sendCoupon() throws IOException {
        try {
            //年会员定时每个月送优惠券
            log.info("----》送优惠券《----");
            schedualWalletService.sendCoupon();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
    //TODO 优惠券自动过期


    @Scheduled(cron="0 0 2 * * ? ")
    public BaseResp dailyStatistics() throws IOException {
        try {
            log.info("----》每日统计数据《----");
            schedualUserService.dailyStatistics();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @Scheduled(cron="0 0 0 * * ? ")
    public BaseResp resetFreeTopCount() throws IOException {
        try {
            log.info("----》每天00:00重置免费置顶次数《----");
            schedualWalletService.resetFreeTopCount();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
}
