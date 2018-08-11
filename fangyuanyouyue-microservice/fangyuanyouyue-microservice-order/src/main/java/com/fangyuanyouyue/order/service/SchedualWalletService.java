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
}
