package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.param.AdminUserParam;

public interface ConfinedUserService {
    /**
     * 获取后台分页数据
     * @param param
     * @return
     */
    Pager getPage(AdminUserParam param);

    /**
     * 设置、取消代理
     * @param id
     * @param type
     * @param code
     * @throws ServiceException
     */
    void updateConfined(Integer id,Integer type,String code) throws ServiceException;
}
