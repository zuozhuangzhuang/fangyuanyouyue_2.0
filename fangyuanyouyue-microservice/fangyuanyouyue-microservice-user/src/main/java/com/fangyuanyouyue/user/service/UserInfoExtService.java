package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.exception.ServiceException;

/**
 * 实名认证相关接口
 */
public interface UserInfoExtService {
    /**
     * 实名认证
     * @param token
     * @param name
     * @param identity
     * @param identityImgCoverUrl
     * @param identityImgBackUrl
     * @throws ServiceException
     */
    void certification(String token, String name, String identity, String identityImgCoverUrl, String identityImgBackUrl) throws ServiceException;

    /**
     * 用户是否官方认证
     * @param userId
     * @return
     * @throws ServiceException
     */
    boolean userIsAuth(Integer userId) throws ServiceException;

    /**
     * 验证支付密码
     * @param userId
     * @param payPwd
     * @return
     * @throws ServiceException
     */
    boolean verifyPayPwd(Integer userId,String payPwd) throws ServiceException;

    /**
     * 是否实名认证
     * @param userId
     * @return
     * @throws ServiceException
     */
    boolean isAuth(Integer userId) throws ServiceException;

    /**
     * 申请官方认证
     * @param userId
     * @throws ServiceException
     */
    void authType(Integer userId) throws ServiceException;

    /**
     * A是否关注用户B
     * @param userId
     * @param toUserId
     * @return
     * @throws ServiceException
     */
    boolean isFans(Integer userId,Integer toUserId) throws ServiceException;
}
