package com.fangyuanyouyue.timer.service;

import com.fangyuanyouyue.timer.service.impl.SchedualWalletServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "wallet-service",fallback = SchedualWalletServiceImpl.class)
@Component
public interface SchedualWalletService {
    /**
     * 修改余额
     * @param userId 用户ID
     * @param amount 修改金额
     * @param type 1充值 2消费
     * @return
     */
    @RequestMapping(value = "/walletFeign/updateBalance",method = RequestMethod.POST)
    String updateBalance(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "amount") BigDecimal amount, @RequestParam(value = "type") Integer type);


    /**
     * 获取免费鉴定次数
     * @param userId
     * @return
     */
    @RequestMapping(value = "/walletFeign/getAppraisalCount",method = RequestMethod.GET)
    String getAppraisalCount(@RequestParam(value = "userId") Integer userId);

    /**
     * 修改剩余免费鉴定次数
     * @param userId
     * @param count
     * @param type
     * @return
     */
    @RequestMapping(value = "/walletFeign/updateAppraisalCount",method = RequestMethod.POST)
    String updateAppraisalCount(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "count") Integer count, @RequestParam(value = "type") Integer type);

    /**
     * 定时更新用户等级
     * @return
     */
    @RequestMapping(value = "/timer/updateLevel",method = RequestMethod.POST)
    String updateLevel();

     /**
     * 会员自动到期
     * @return
     */
    @RequestMapping(value = "/timer/cancelVip",method = RequestMethod.POST)
    String cancelVip();

    /**
     * 新增用户行为
     * @param userId 用户id
     * @param toUserId 行为对象所属用户id
     * @param businessId 对象id
     * @param businessType 对象类型 1用户 2商品、抢购 3商品、抢购评论 4帖子、视频 5帖子、视频评论 6全民鉴定 7全民鉴定评论
     * @param type 行为类型 1点赞 2关注用户 3评论 4购买抢购
     * @return
     */
    @RequestMapping(value = "/walletFeign/addUserBehavior",method = RequestMethod.POST)
    String addUserBehavior(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "toUserId") Integer toUserId,@RequestParam(value = "businessId") Integer businessId,
                           @RequestParam(value = "businessType") Integer businessType,@RequestParam(value = "type") Integer type);

    /**
     * 送优惠券
     * @return
     */
    @RequestMapping(value = "/timer/sendCoupon",method = RequestMethod.POST)
    String sendCoupon();

}
