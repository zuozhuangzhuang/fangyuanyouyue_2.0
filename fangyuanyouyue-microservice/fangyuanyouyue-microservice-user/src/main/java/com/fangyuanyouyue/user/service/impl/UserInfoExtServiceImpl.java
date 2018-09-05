package com.fangyuanyouyue.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.dao.*;
import com.fangyuanyouyue.user.model.*;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import com.fangyuanyouyue.user.service.SchedualWalletService;
import io.swagger.annotations.Api;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.service.UserInfoExtService;
import com.fangyuanyouyue.user.service.UserInfoService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
    @Autowired
    private SchedualWalletService schedualWalletService;
    @Autowired
    private UserAuthOrderMapper userAuthOrderMapper;
    @Autowired
    private UserAuthApplyMapper userAuthApplyMapper;

    @Override
    public void certification(String token, String name, String identity, String identityImgCoverUrl, String identityImgBackUrl) throws ServiceException {
        //验证用户
        UserInfo userInfo = userInfoService.getUserByToken(token);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userInfo.getId());
        //根据用户ID获取实名认证申请信息
        IdentityAuthApply identityAuthApply = identityAuthApplyMapper.selectByUserId(userInfo.getId());
        if(identityAuthApply != null){
            //identityAuthApply:已申请过 状态 1申请 2通过 3拒绝(这里不查询拒绝的情况）
            //userInfoExt 实名登记状态 1已实名 2未实名
            if(identityAuthApply.getStatus() == 1 && userInfoExt.getStatus() != null && userInfoExt.getStatus() == 2){
                throw new ServiceException("您已提交过实名认证，请耐心等待！");
            }else if(identityAuthApply.getStatus() == 2 && userInfoExt.getStatus() != null && userInfoExt.getStatus() == 1){
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
            userInfoExt.setStatus(2);
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
        boolean result = MD5Util.verify(MD5Util.MD5(payPwd),userInfoExt.getPayPwd());
        //TODO 支付密码错误次数
        return result;
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
    public Object authType(Integer userId,Integer payType,String payPwd) throws ServiceException {
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
                //下单
                UserAuthOrder authOrder = new UserAuthOrder();
                authOrder.setUserId(userId);
                //订单号
                final IdGenerator idg = IdGenerator.INSTANCE;
                String id = idg.nextId();
                authOrder.setOrderNo(id);
                authOrder.setAmount(new BigDecimal(360));
                authOrder.setStatus(1);
                authOrder.setAddTime(DateStampUtils.getTimesteamp());
                authOrder.setTitle("官方认证支付");
                userAuthOrderMapper.insert(authOrder);

                StringBuffer payInfo = new StringBuffer();
                if(payType.intValue() == 1){//微信,如果回调失败就不做处理，成功就在回调接口中继续生成全民鉴定
                    WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualWalletService.orderPayByWechat(authOrder.getOrderNo(), authOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.auth_wechat_notify.getNotifUrl())).getString("data")), WechatPayDto.class);
                    return wechatPayDto;
                }else if(payType.intValue() == 2){//支付宝,如果回调失败就不做处理，成功就在回调接口中继续生成全民鉴定
                    String info = JSONObject.parseObject(schedualWalletService.orderPayByALi(authOrder.getOrderNo(), authOrder.getAmount(), NotifyUrl.test_notify.getNotifUrl()+NotifyUrl.auth_alipay_notify.getNotifUrl())).getString("data");
                    payInfo.append(info);
                }else if(payType.intValue() == 3) {//余额
                    BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(userId, authOrder.getAmount(), 2)), BaseResp.class);
                    if(baseResp.getCode() == 1){
                        throw new ServiceException(baseResp.getReport().toString());
                    }
                    updateOrder(authOrder.getOrderNo(),null,3);
                    payInfo.append("余额支付成功！");
                }
                return payInfo.toString();
            }
        }
    }

    /**
     * 修改订单状态
     * @param orderNo
     * @throws ServiceException
     */
    public boolean updateOrder(String orderNo,String thirdOrderNo,Integer payType) throws ServiceException {
        UserAuthOrder authOrder = userAuthOrderMapper.selectByOrderNo(orderNo);
        if(authOrder == null){
            throw new ServiceException("订单不存在！");
        }
        //添加申请记录
        //TODO 官方认证添加时间限制
        UserAuthApply userAuthApply = new UserAuthApply();
        userAuthApply.setUserId(authOrder.getUserId());
        userAuthApply.setStatus(1);
        userAuthApply.setAddTime(DateStampUtils.getTimesteamp());
        userAuthApplyMapper.insert(userAuthApply);

        //修改扩展表中信息为申请中
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(authOrder.getUserId());
        userInfoExt.setAuthType(0);
        userInfoExtMapper.updateByPrimaryKey(userInfoExt);
        authOrder.setStatus(2);
        userAuthOrderMapper.updateByPrimaryKeySelective(authOrder);
        //系统消息：您的认证店铺申请已提交，将于5个工作日内完成审核，请注意消息通知
        schedualMessageService.easemobMessage(authOrder.getUserId().toString(),"您的认证店铺申请已提交，将于5个工作日内完成审核，请注意消息通知","1","1","");
        //买家新增余额账单
        schedualWalletService.addUserBalanceDetail(authOrder.getUserId(),authOrder.getAmount(),3,1,authOrder.getOrderNo(),"申请认证店铺",null,authOrder.getUserId(),8);
        return true;
    }

    @Override
    public boolean isFans(Integer userId, Integer toUserId) throws ServiceException {
        UserFans userFans = this.userFans.selectByUserIdToUserId(userId, toUserId);
        return userFans != null;
    }
}
