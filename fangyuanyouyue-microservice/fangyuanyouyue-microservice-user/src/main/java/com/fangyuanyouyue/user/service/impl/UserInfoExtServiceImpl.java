package com.fangyuanyouyue.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.enums.Credit;
import com.fangyuanyouyue.base.enums.NotifyUrl;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.constant.StatusEnum;
import com.fangyuanyouyue.user.dao.*;
import com.fangyuanyouyue.user.dto.admin.AdminIdentityAuthApplyDto;
import com.fangyuanyouyue.user.dto.admin.AdminUserAuthApplyDto;
import com.fangyuanyouyue.user.model.*;
import com.fangyuanyouyue.user.param.AdminUserParam;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import com.fangyuanyouyue.user.service.SchedualWalletService;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.service.UserInfoExtService;
import com.fangyuanyouyue.user.service.UserInfoService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    @Autowired
    private UserInfoMapper userInfoMapper;

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
            if(identityAuthApply.getStatus().equals(StatusEnum.AUTH_APPLY.getCode()) && userInfoExt.getStatus() != null && userInfoExt.getStatus().equals(StatusEnum.AUTH_APPLY.getCode())){
                throw new ServiceException("您已提交过实名认证，请耐心等待！");
            }else if(identityAuthApply.getStatus().equals(StatusEnum.AUTH_ACCEPT.getCode()) && userInfoExt.getStatus() != null && userInfoExt.getStatus().equals(StatusEnum.AUTH_ACCEPT.getCode())){
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
            identityAuthApply.setStatus(StatusEnum.AUTH_APPLY.getCode());//状态 1申请 2通过 3拒绝
            identityAuthApply.setAddTime(DateStampUtils.getTimesteamp());
            identityAuthApplyMapper.insert(identityAuthApply);
            userInfoExt.setStatus(StatusEnum.AUTH_APPLY.getCode());
            userInfoExtMapper.updateByPrimaryKeySelective(userInfoExt);
        }
        //系统消息：您的实名认证申请已提交，将于1个工作日内完成审核，请注意消息通知
        schedualMessageService.easemobMessage(userInfo.getId().toString(),
                "您的实名认证申请已提交，将于1个工作日内完成审核，请注意消息通知",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
    }

    @Override
    public boolean userIsAuth(Integer userId) throws ServiceException {
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
        if(userInfoExt == null){
            throw new ServiceException("用户扩展信息错误！");
        }
        return userInfoExt.getAuthType().equals(StatusEnum.AUTH_ACCEPT.getCode());
    }

    @Override
    public boolean verifyPayPwd(Integer userId, String payPwd) throws ServiceException {
        if(StringUtils.isEmpty(payPwd)){
            throw new ServiceException("支付密码不能为空！");
        }
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
        if(userInfoExt == null){
            throw new ServiceException("用户扩展信息错误！");
        }
        if(StringUtils.isEmpty(userInfoExt.getPayPwd())){
            throw new ServiceException("未设置支付密码！");
        }
        boolean result = MD5Util.verify(MD5Util.MD5(payPwd),userInfoExt.getPayPwd());
        //TODO 支付密码错误次数
        return result;
    }

    @Override
    public boolean isAuth(Integer userId) throws ServiceException {
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
        //已申请过 状态 1申请 2通过 3拒绝
        if(userInfoExt.getStatus().equals(StatusEnum.AUTH_ACCEPT.getCode())) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Object authType(Integer userId,Integer payType,String payPwd) throws ServiceException {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(StringUtils.isEmpty(userInfo.getPhone())){
            throw new ServiceException("未绑定手机号！");
        }
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
        if(userInfoExt == null){
            throw new ServiceException("用户扩展信息错误！");
        }else{
            if(userInfoExt.getAuthType().equals(StatusEnum.AUTH_ACCEPT.getCode())){
                throw new ServiceException("您的官方认证已通过，请勿重复提交！");
            }else if(userInfoExt.getAuthType().equals(StatusEnum.AUTH_APPLY.getCode())){
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
                authOrder.setStatus(StatusEnum.ORDER_UNPAID.getCode());
                authOrder.setAddTime(DateStampUtils.getTimesteamp());
                authOrder.setTitle("官方认证支付");
                userAuthOrderMapper.insert(authOrder);

                StringBuffer payInfo = new StringBuffer();
                if(payType.intValue() == Status.PAY_TYPE_WECHAT.getValue()){
                    WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualWalletService.orderPayByWechat(authOrder.getOrderNo(), authOrder.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.auth_wechat_notify.getNotifUrl())).getString("data")), WechatPayDto.class);
                    return wechatPayDto;
                }else if(payType.intValue() == Status.PAY_TYPE_ALIPAY.getValue()){
                    String info = JSONObject.parseObject(schedualWalletService.orderPayByALi(authOrder.getOrderNo(), authOrder.getAmount(), NotifyUrl.notify.getNotifUrl()+NotifyUrl.auth_alipay_notify.getNotifUrl())).getString("data");
                    payInfo.append(info);
                }else if(payType.intValue() == Status.PAY_TYPE_BALANCE.getValue()) {
                    boolean verifyPayPwd = verifyPayPwd(userId, payPwd);
                    if(!verifyPayPwd){
                        throw new ServiceException(ReCode.PAYMENT_PASSWORD_ERROR.getValue(),ReCode.PAYMENT_PASSWORD_ERROR.getMessage());
                    }
                    BaseResp baseResp = JSONObject.toJavaObject(JSONObject.parseObject(schedualWalletService.updateBalance(userId, authOrder.getAmount(), Status.SUB.getValue())), BaseResp.class);
                    if(baseResp.getCode() == 1){
                        throw new ServiceException(baseResp.getReport().toString());
                    }
                    updateOrder(authOrder.getOrderNo(),null,Status.PAY_TYPE_BALANCE.getValue());
                    payInfo.append("余额支付成功！");
                }else if(payType.intValue() == Status.PAY_TYPE_MINI.getValue()){
                    WechatPayDto wechatPayDto = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualWalletService.orderPayByWechatMini(userId,authOrder.getOrderNo(), authOrder.getAmount(), NotifyUrl.mini_notify.getNotifUrl()+NotifyUrl.auth_wechat_notify.getNotifUrl())).getString("data")), WechatPayDto.class);
                    return wechatPayDto;
                }else{
                    throw new ServiceException("支付方式错误！");
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
    @Override
    public boolean updateOrder(String orderNo, String thirdOrderNo, Integer payType) throws ServiceException {
        UserAuthOrder authOrder = userAuthOrderMapper.selectByOrderNo(orderNo);
        if(authOrder == null){
            throw new ServiceException("订单不存在！");
        }
        //添加申请记录
        UserAuthApply userAuthApply = new UserAuthApply();
        userAuthApply.setUserId(authOrder.getUserId());
        userAuthApply.setStatus(StatusEnum.AUTH_TYPE_APPLY.getCode());
        userAuthApply.setAddTime(DateStampUtils.getTimesteamp());
        userAuthApplyMapper.insert(userAuthApply);

        //修改扩展表中信息为申请中
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(authOrder.getUserId());
        userInfoExt.setAuthType(StatusEnum.AUTH_TYPE_APPLY.getCode());
        userInfoExtMapper.updateByPrimaryKey(userInfoExt);
        authOrder.setStatus(StatusEnum.ORDER_COMPLETE.getCode());
        authOrder.setPayNo(thirdOrderNo);
        userAuthOrderMapper.updateByPrimaryKeySelective(authOrder);
        //系统消息：您的认证店铺申请已提交，将于5个工作日内完成审核，请注意消息通知
        schedualMessageService.easemobMessage(authOrder.getUserId().toString(),"您的认证店铺申请已提交，将于5个工作日内完成审核，请注意消息通知",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_SYSTEM.getMessage(),"");
        //买家新增余额账单
        schedualWalletService.addUserBalanceDetail(authOrder.getUserId(),authOrder.getAmount(),payType,Status.EXPEND.getValue(),orderNo,"申请认证店铺",null,authOrder.getUserId(),Status.SHOP_AUTH.getValue(),thirdOrderNo);
        return true;
    }

    @Override
    public boolean isFans(Integer userId, Integer toUserId) throws ServiceException {
        UserFans userFans = this.userFans.selectByUserIdToUserId(userId, toUserId);
        return userFans != null;
    }

    @Override
    public void updateExtAuth(Integer applyId, Integer status, String content) throws ServiceException {
        IdentityAuthApply apply = identityAuthApplyMapper.selectByPrimaryKey(applyId);
        if(apply == null){
            throw new ServiceException("未找到申请信息！");
        }
        if(apply.getStatus().intValue() == StatusEnum.AUTH_ACCEPT.getCode()){
            throw new ServiceException("此用户已通过实名认证！");
        }
        UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(apply.getUserId());
        apply.setStatus(status);
        identityAuthApplyMapper.updateByPrimaryKeySelective(apply);
        userInfoExt.setStatus(status);
        if(status.intValue() == StatusEnum.AUTH_ACCEPT.getCode()){
            //通过
            userInfoExt.setIdentity(apply.getIdentity());
            userInfoExt.setName(apply.getName());
            userInfoExt.setCredit(userInfoExt.getCredit()+Credit.EXTAPPLY.getCredit());
//            schedualWalletService.updateCredit(apply.getUserId(), Credit.EXTAPPLY.getCredit(), Status.ADD.getValue());
            schedualMessageService.easemobMessage(apply.getUserId().toString(),
                    "恭喜您，您申请的实名认证，已通过官方审核！",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_EXT_AGREE.getMessage(),"");
        }else{
            //拒绝
            schedualMessageService.easemobMessage(apply.getUserId().toString(),
                    "很抱歉，您申请的实名认证，官方审核未通过！可重新提交资料再次申请。",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_EXT_REFUSE.getMessage(),"");
        }
        userInfoExtMapper.updateByPrimaryKeySelective(userInfoExt);
    }

    @Override
    public Pager getExtAuthPage(BasePageReq param) {

        Integer total = identityAuthApplyMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

        List<IdentityAuthApply> datas = identityAuthApplyMapper.getPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(AdminIdentityAuthApplyDto.toDtoList(datas));
        return pager;
    }

    @Override
    public Pager getShopAuthPage(BasePageReq param) {

        Integer total = userAuthApplyMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

        List<UserAuthApply> datas = userAuthApplyMapper.getPage(param.getStart(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
        Pager pager = new Pager();
        pager.setTotal(total);
        pager.setDatas(AdminUserAuthApplyDto.toDtoList(datas));
        return pager;
    }

    @Override
    public void updateShopAuth(Integer applyId, Integer status, String content) throws ServiceException {

        UserAuthApply model = userAuthApplyMapper.selectByPrimaryKey(applyId);
        if(model == null){
            throw new ServiceException("未找到申请信息！");
        }
        model.setStatus(status);
        UserInfoExt ext = userInfoExtMapper.selectByUserId(model.getUserId());
        ext.setAuthType(status);
        userInfoExtMapper.updateByPrimaryKey(ext);
        if(status.intValue() == StatusEnum.AUTH_TYPE_ACCEPT.getCode()){
            //通过
            model.setStartTime(DateStampUtils.getTimesteamp());
            model.setEndTime(DateUtil.getDateAfterYear(DateStampUtils.getTimesteamp(),1));
            schedualMessageService.easemobMessage(model.getUserId().toString(),
                    "恭喜您，您申请的认证店铺已通过官方审核！您的店铺已添加认证店铺专属标识，快拉您的好友来尽情购买吧！",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_AUTH_TYPE_AGREE.getMessage(),"");
        }else{
            model.setReason(content);
            schedualWalletService.updateBalance(ext.getUserId(),new BigDecimal(360),Status.ADD.getValue());
            //买家新增余额账单
			//订单号
			final IdGenerator idg = IdGenerator.INSTANCE;
			String orderNo = idg.nextId();
            schedualWalletService.addUserBalanceDetail(model.getUserId(),new BigDecimal(360),Status.PAY_TYPE_BALANCE.getValue(),Status.REFUND.getValue(),orderNo,"认证店铺审核未通过",null,model.getUserId(),Status.SHOP_AUTH.getValue(),orderNo);
            //拒绝
            schedualMessageService.easemobMessage(model.getUserId().toString(),
                    "很抱歉，您申请的认证店铺未通过官方审核，可联系客服咨询详情。",Status.SYSTEM_MESSAGE.getMessage(),Status.JUMP_TYPE_AUTH_TYPE_REFUSE.getMessage(),"");
        }
        userAuthApplyMapper.updateByPrimaryKey(model);
    }

}
