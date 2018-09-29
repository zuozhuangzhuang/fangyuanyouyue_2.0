package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.user.service.SchedualForumService;
import org.springframework.stereotype.Component;

@Component
public class SchedualForumServiceImpl implements SchedualForumService {
    @Override
    public String isHasColumn(Integer userId) {
        return "获取是否拥有专栏失败！";
    }

    @Override
    public String processTodayForum() {
        return "失败！";
    }

    @Override
    public String processAllForum() {
        return "失败！";
    }
}
