package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.user.param.AdminUserParam;

public interface ConfinedUserService {
    /**
     * 获取后台分页数据
     * @param param
     * @return
     */
    Pager getPage(AdminUserParam param);
}
