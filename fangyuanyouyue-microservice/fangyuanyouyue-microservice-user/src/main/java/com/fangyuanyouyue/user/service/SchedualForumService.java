package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.user.service.impl.SchedualForumServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "forum-service",fallback = SchedualForumServiceImpl.class)
@Component
public interface SchedualForumService {
    /**
     * 用户是否拥有专栏
     * @param userId
     * @return
     */
    @RequestMapping(value = "/forumFeign/isHasColumn",method = RequestMethod.POST)
    String isHasColumn(@RequestParam(value = "userId") Integer userId);

}
