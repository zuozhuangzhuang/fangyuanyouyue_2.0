package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.exception.ServiceException;

public interface UserThirdService {

    /**
     * 判断是否可以账号合并
     * @param token
     * @param unionId
     * @param phone
     * @param type
     * @return
     * @throws ServiceException
     */
    boolean judgeMerge(String token,String unionId,String phone,Integer type) throws ServiceException;
}
