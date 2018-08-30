package com.fangyuanyouyue.timer.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.timer.service.SchedualForumService;
import com.fangyuanyouyue.timer.service.SchedualGoodsService;
import com.fangyuanyouyue.timer.service.SchedualOrderService;
import com.fangyuanyouyue.timer.service.SchedualWalletService;
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

    @Scheduled(cron="0 * *  * * ? ")
    public BaseResp updateLevel() throws IOException {
        try {
            log.info("----》修改用户等级《----");
            schedualWalletService.updateLevel();
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


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

//
//    @Scheduled(cron="0 * *  * * ? ")
//    public BaseResp updateOrderRefundTwoDay() throws IOException {
//        try {
//            log.info("----》仅退款：48小时卖家未处理自动退款《----");
//
//            return toSuccess();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//
//
//    @Scheduled(cron="0 * *  * * ? ")
//    public BaseResp updateOrderRefundThreeDay() throws IOException {
//        try {
//            log.info("----》退货退款：72小时卖家未处理自动交给后台处理《----");
//
//            return toSuccess();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError("系统繁忙，请稍后再试！");
//        }
//    }
//
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

}
