package com.fangyuanyouyue.timer.service;

import com.fangyuanyouyue.timer.service.impl.SchedualGoodsServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "forum-service",fallback = SchedualGoodsServiceImpl.class)
@Component
public interface SchedualForumService {
    /**
     * 全民鉴定结束
     * @return
     */
    @RequestMapping(value = "/timer/appraisalEnd",method = RequestMethod.POST)
    String appraisalEnd();

}
