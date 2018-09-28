package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.param.AdminUserParam;

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
     * @param payType
     * @param payPwd
     * @throws ServiceException
     */
    Object authType(Integer userId,Integer payType,String payPwd) throws ServiceException;

    /**
     * A是否关注用户B
     * @param userId
     * @param toUserId
     * @return
     * @throws ServiceException
     */
    boolean isFans(Integer userId,Integer toUserId) throws ServiceException;

    /**
     * 修改订单状态
     * @param orderNo
     * @param thirdOrderNo
     * @param payType
     * @return
     * @throws ServiceException
     */
    boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException;

    /**
     * 实名认证列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager getExtAuthPage(BasePageReq param) throws ServiceException;

    /**
     * 认证店铺列表
     * @param param
     * @return
     * @throws ServiceException
     */
    Pager getShopAuthPage(BasePageReq param) throws ServiceException;

    /**
     * 修改实名认证状态
     * @param id
     * @param status
     * @param content
     * @throws ServiceException
     */
    void updateExtAuth(Integer id,Integer status,String content) throws ServiceException;

    /**
     * 修改认证店铺状态
     * @param id
     * @param status
     * @param content
     * @throws ServiceException
     */
    void updateShopAuth(Integer id,Integer status,String content) throws ServiceException;


}
