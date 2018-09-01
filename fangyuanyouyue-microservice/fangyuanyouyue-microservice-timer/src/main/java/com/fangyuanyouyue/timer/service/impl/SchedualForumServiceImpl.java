package com.fangyuanyouyue.timer.service.impl;

import com.fangyuanyouyue.timer.service.SchedualForumService;
import org.springframework.stereotype.Component;

@Component
public class SchedualForumServiceImpl implements SchedualForumService{
    @Override
    public String appraisalEnd() {
        return "全民鉴定结束失败！";
    }
}
