package com.fangyuanyouyue.user.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.ShopDto;
import com.fangyuanyouyue.user.dto.UserDto;
import com.fangyuanyouyue.user.dto.UserFansDto;
import com.fangyuanyouyue.user.dto.WaitProcessDto;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.param.UserParam;

/**
 * 用户相关接口
 */
public interface UserInfoService {

    /**
     * 根据用户token获取用户信息
     * @param token
     * @return
     * @throws ServiceException
     */
    UserInfo getUserByToken(String token) throws ServiceException;
    /**
     * 根据ID获取用户
     * @param id
     * @return
     */
    UserInfo selectByPrimaryKey(Integer id);

    /**
     * 根据手机获取用户
     * @param phone
     * @return
     */
    UserInfo getUserByPhone(String phone);

    /**
     * 根据昵称获取用户
     * @param nickName
     * @return
     */
    UserInfo getUserByNickName(String nickName) throws ServiceException;



    /**
     * 用户手机注册
     * @param param
     * @return
     */
    UserDto regist(UserParam param) throws ServiceException;

    /**
     * 用户手机号登录
     * @param phone
     * @param logingPwd
     * @param lastLoginPlatform
     * @return
     */
    UserDto login(String phone,String logingPwd,Integer lastLoginPlatform) throws ServiceException;

    /**
     * 三方登录
     * @param param
     * @return
     * @throws ServiceException
     */
    UserDto thirdLogin(UserParam param) throws ServiceException;

    /**
     * 三方绑定
     * @param token
     * @param unionId
     * @param type
     * @return
     * @throws ServiceException
     */
    UserDto thirdBind(String token,String unionId,Integer type) throws ServiceException;



    /**
     * 完善资料
     * @param param
     * @return
     * @throws ServiceException
     */
    UserDto modify(UserParam param) throws ServiceException;

    /**
     * 找回密码
     * @param phone
     * @param newPwd
     * @throws ServiceException
     */
    void resetPwd(String phone,String newPwd) throws ServiceException;

    /**
     * 修改密码
     * @param token
     * @param newPwd
     * @throws ServiceException
     */
    void updatePwd(String token,String newPwd) throws ServiceException;

    /**
     * 修改绑定手机
     * @param token
     * @param phone
     * @throws ServiceException
     */
    UserDto updatePhone(String token,String phone) throws ServiceException;

    /**
     * 合并账号
     * @param token
     * @param phone
     * @return
     * @throws ServiceException
     */
    UserDto accountMerge(String token,String phone) throws ServiceException;

    /**
     * 小程序登录
     * @param param
     * @return
     * @throws ServiceException
     */
    UserDto miniLogin(UserParam param,String openid,String session_key) throws ServiceException;

    /**
     * 根据unionID获取用户
     * @param unionId
     * @return
     * @throws ServiceException
     */
    UserInfo getUserByUnionId(String unionId,Integer type) throws ServiceException;

    /**
     * 获取个人店铺列表
     * @param type
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<ShopDto> shopList(String nickName,Integer type,Integer start,Integer limit,Integer authType) throws ServiceException;

    /**
     * 获取用户信息
     * @param userId
     * @return
     * @throws ServiceException
     */
    UserDto userInfo(String token,Integer userId) throws ServiceException;

    /**
     * 添加关注/取消关注
     * @param userId
     * @param toUserId
     * @throws ServiceException
     */
    void fansFollow(Integer userId,Integer toUserId,Integer type) throws ServiceException;

    /**
     * 我的粉丝
     * @param userId
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<UserFansDto> myFansOrFollows(Integer userId,Integer type,Integer start,Integer limit) throws ServiceException;

    /**
     * 获取待处理信息
     * @param userId
     * @return
     * @throws ServiceException
     */
    WaitProcessDto myWaitProcess(Integer userId) throws ServiceException;
    
    /**
     * 注册通讯账号
     */
    void registIMUser(UserInfo user) throws ServiceException;
    
}
