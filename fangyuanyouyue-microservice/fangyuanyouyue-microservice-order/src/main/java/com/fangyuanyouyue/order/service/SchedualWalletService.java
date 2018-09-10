package com.fangyuanyouyue.order.service;

import com.fangyuanyouyue.order.service.impl.SchedualWalletServiceImpl;
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
    String updateBalance(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "amount") BigDecimal amount,@RequestParam(value = "type") Integer type);

    /**
     * 修改积分
     * @param userId
     * @param score
     * @param type 1增加 2减少
     * @return
     */
    @RequestMapping(value = "/walletFeign/updateScore",method = RequestMethod.POST)
    String updateScore(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "score") Long score,@RequestParam(value = "type") Integer type);

    /**
     * 修改信誉度
     * @param userId
     * @param credit
     * @param type
     * @return
     */
    @RequestMapping(value = "/walletFeign/updateCredit",method = RequestMethod.POST)
    String updateCredit(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "credit") Long credit,@RequestParam(value = "type") Integer type);


    /**
     * 微信支付
     * @param orderNo
     * @param price
     * @return
     */
    @RequestMapping(value = "/walletFeign/orderPayByWechat",method = RequestMethod.POST)
    String orderPayByWechat(@RequestParam(value = "orderNo") String orderNo,@RequestParam(value = "price") BigDecimal price,@RequestParam(value = "notifyUrl") String notifyUrl);

    /**
     * 小程序支付
     * @param userId
     * @param orderNo
     * @param price
     * @param notifyUrl
     * @return
     */
    @RequestMapping(value = "/walletFeign/orderPayByWechatMini",method = RequestMethod.POST)
    String orderPayByWechatMini(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "orderNo") String orderNo,@RequestParam(value = "price") BigDecimal price,@RequestParam(value = "notifyUrl") String notifyUrl);

    /**
     * 支付宝支付
     * @param orderNo
     * @param price
     * @return
     */
    @RequestMapping(value = "/walletFeign/orderPayByALi",method = RequestMethod.POST)
    String orderPayByALi(@RequestParam(value = "orderNo") String orderNo,@RequestParam(value = "price") BigDecimal price,@RequestParam(value = "notifyUrl") String notifyUrl);


    /**
     * 新增用户收支信息
     * @param userId
     * @param amount
     * @param payType 支付类型 1微信 2支付宝 3余额 4小程序
     * @param type 收支类型 1收入 2支出
     * @param orderNo
     * @param title
     * @param sellerId
     * @param buyerId
     * @param orderType 订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5申请专栏 6充值 7开通、续费会员 8认证店铺
     * @return
     */
    @RequestMapping(value = "/walletFeign/addUserBalanceDetail",method = RequestMethod.POST)
    String addUserBalanceDetail(@RequestParam(value = "userId")Integer userId,@RequestParam(value = "amount")BigDecimal amount,@RequestParam(value = "payType")Integer payType,
                          @RequestParam(value = "type")Integer type,@RequestParam(value = "orderNo") String orderNo,
                          @RequestParam(value = "title")String title,@RequestParam(value = "sellerId")Integer sellerId,
                          @RequestParam(value = "buyerId")Integer buyerId,@RequestParam(value = "orderType")Integer orderType);

    /**
     * 根据优惠券id计算价格
     * @param price
     * @param couponId
     * @return
     */
    @RequestMapping(value = "/walletFeign/getPriceByCoupon",method = RequestMethod.POST)
    String getPriceByCoupon(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "price") BigDecimal price,@RequestParam(value = "couponId") Integer couponId);
}
