package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.goods.service.impl.SchedualWalletServiceImpl;
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
     * @return
     */
    @RequestMapping(value = "/walletFeign/updateAppraisalCount",method = RequestMethod.POST)
    String updateAppraisalCount(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "count") Integer count);
}
