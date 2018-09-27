package com.fangyuanyouyue.forum.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.service.ForumColumnService;
import com.fangyuanyouyue.forum.service.SchedualRedisService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/forumFeign")
@Api(description = "首页系统外部调用Controller")
@RefreshScope
public class FeignController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private ForumColumnService forumColumnService;



    //是否拥有专栏
    @PostMapping(value = "/isHasColumn")
    @ResponseBody
    public BaseResp isHasColumn(Integer userId) throws IOException{
        try {
            log.info("----》是否拥有专栏《----");
            log.info("参数：userId：" + userId);
            //验证用户
            if(userId == null){
                return toError("用户id不能为空！");
            }
            //获取专栏信息
            boolean isHasColumn = forumColumnService.isHasColumn(userId);
            return toSuccess(isHasColumn);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
}
