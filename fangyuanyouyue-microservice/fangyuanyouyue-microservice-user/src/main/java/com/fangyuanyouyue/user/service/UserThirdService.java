package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.MergeDto;
import com.fangyuanyouyue.user.dto.UserDto;

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
    MergeDto judgeMerge(String token, String unionId, String phone, Integer type) throws ServiceException;


    /**
     * 合并账号
     * @param token
     * @param phone
     * @param unionId
     * @param type
     * @param loginPwd
     * @return
     * @throws ServiceException
     */
    void accountMerge(String token, String phone, String unionId, Integer type, String loginPwd) throws ServiceException;
}
