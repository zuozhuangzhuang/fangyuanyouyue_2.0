package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.dao.UserFansMapper;
import com.fangyuanyouyue.user.model.UserFans;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.IdentityAuthApplyMapper;
import com.fangyuanyouyue.user.dao.UserInfoExtMapper;
import com.fangyuanyouyue.user.model.IdentityAuthApply;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.UserInfoExt;
import com.fangyuanyouyue.user.service.UserInfoExtService;
import com.fangyuanyouyue.user.service.UserInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userInfoExtService")
@Transactional(rollbackFor=Exception.class)
public class UserInfoExtServiceImpl implements UserInfoExtService {

    @Autowired
    private IdentityAuthApplyMapper identityAuthApplyMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserFansMapper userFans;
    @Autowired
    private SchedualMessageService schedualMessageService;

    @Override
    public void certification(String token, String name, String identity, String identityImgCoverUrl, String identityImgBackUrl) throws ServiceException {
        //验证用户
        UserInfo userInfo = userInfoService.getUserByToken(token);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }
        //根据用户ID获取实名认证申请信息
        IdentityAuthApply identityAuthApply = identityAuthApplyMapper.selectByUserId(userInfo.getId());
        if(identityAuthApply != null){//已申请过 状态 1申请 2通过 3拒绝(这里不查询拒绝的情况）
            if(identityAuthApply.getStatus() == 1){
                throw new ServiceException("您已提交过实名认证，请耐心等待！");
            }else if(identityAuthApply.getStatus() == 2){
                throw new ServiceException("您的实名认证已通过，请勿重复提交！");
            }
        }else{
            //新增实名认证申请记录
            identityAuthApply = new IdentityAuthApply();
            //实名认证申请表
            identityAuthApply.setUserId(userInfo.getId());
            identityAuthApply.setName(name);
            identityAuthApply.setIdentity(identity);
            identityAuthApply.setIdentityImgCover(identityImgCoverUrl);
            identityAuthApply.setIdentityImgBack(identityImgBackUrl);
            identityAuthApply.setStatus(1);//状态 1申请 2通过 3拒绝
            identityAuthApply.setAddTime(DateStampUtils.getTimesteamp());
            identityAuthApplyMapper.insert(identityAuthApply);
        }
        //系统消息：您的实名认证申请已提交，将于1个工作日内完成审核，请注意消息通知
        schedualMessageService.easemobMessage(userInfo.getId().toString(),
                "您的实名认证申请已提交，将于1个工作日内完成审核，请注意消息通知","1","1","");
    }

    @Override
    public boolean userIsAuth(Integer userId) throws ServiceException {
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
        if(userInfoExt == null){
            throw new ServiceException("用户扩展信息错误！");
        }
        return userInfoExt.getAuthType() == 1;
    }

    @Override
    public boolean verifyPayPwd(Integer userId, String payPwd) throws ServiceException {
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
        if(userInfoExt == null){
            throw new ServiceException("用户扩展信息错误！");
        }
        return MD5Util.verify(MD5Util.MD5(payPwd),userInfoExt.getPayPwd());
    }

    @Override
    public boolean isAuth(Integer userId) throws ServiceException {
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
        //根据用户ID获取实名认证申请信息
        IdentityAuthApply identityAuthApply = identityAuthApplyMapper.selectByUserId(userId);
        //已申请过 状态 1申请 2通过 3拒绝
        if(identityAuthApply != null && identityAuthApply.getStatus() == 2 && userInfoExt.getStatus().intValue() == 1) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void authType(Integer userId) throws ServiceException {
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
        if(userInfoExt == null){
            throw new ServiceException("用户扩展信息错误！");
        }else{
            //认证状态 0申请中 1已认证 2未认证
            if(userInfoExt.getAuthType() == 1){
                throw new ServiceException("您的官方认证已通过，请勿重复提交！");
            }else if(userInfoExt.getAuthType() == 0){
                throw new ServiceException("您已提交官方认证，请耐心等待！");
            }else{
                userInfoExt.setAuthType(0);
                userInfoExtMapper.updateByPrimaryKey(userInfoExt);
            }
        }
        //系统消息：您的认证店铺申请已提交，将于5个工作日内完成审核，请注意消息通知
        schedualMessageService.easemobMessage(userId.toString(),"您的认证店铺申请已提交，将于5个工作日内完成审核，请注意消息通知","1","1","");
    }

    @Override
    public boolean isFans(Integer userId, Integer toUserId) throws ServiceException {
        UserFans userFans = this.userFans.selectByUserIdToUserId(userId, toUserId);
        return userFans != null;
    }
}
