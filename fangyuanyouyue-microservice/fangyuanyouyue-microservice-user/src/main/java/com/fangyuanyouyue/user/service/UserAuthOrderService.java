package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.Pager;

/**
 * 用户相关接口
 */
public interface UserAuthOrderService {


    /**
     * 获取后台分页数据
     * @param param
     * @return
     */
    Pager getPage(BasePageReq param);


    /**
     * 同意认证
     * @param id
     */
    void updateAccept(Integer id);

    /**
     * 拒绝认证
     * @param id
     */
    void updateReject(Integer id,String reason);
    
}
