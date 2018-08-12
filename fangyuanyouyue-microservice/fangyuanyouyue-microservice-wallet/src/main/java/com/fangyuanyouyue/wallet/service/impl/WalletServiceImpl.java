package com.fangyuanyouyue.wallet.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.wallet.dao.ConfinedUserMapper;
import com.fangyuanyouyue.wallet.dao.UserBalanceDetailMapper;
import com.fangyuanyouyue.wallet.dao.UserInfoExtMapper;
import com.fangyuanyouyue.wallet.dao.UserRechargeDetailMapper;
import com.fangyuanyouyue.wallet.dao.UserVipMapper;
import com.fangyuanyouyue.wallet.dao.UserWalletMapper;
import com.fangyuanyouyue.wallet.dto.WalletDto;
import com.fangyuanyouyue.wallet.model.ConfinedUser;
import com.fangyuanyouyue.wallet.model.UserInfoExt;
import com.fangyuanyouyue.wallet.model.UserRechargeDetail;
import com.fangyuanyouyue.wallet.model.UserVip;
import com.fangyuanyouyue.wallet.model.UserWallet;
import com.fangyuanyouyue.wallet.service.WalletService;

@Service(value = "walletService")
public class WalletServiceImpl implements WalletService{
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private ConfinedUserMapper confinedUserMapper;
    @Autowired
    private UserVipMapper userVipMapper;
    @Autowired
    private UserRechargeDetailMapper userRechargeDetailMapper;
   @Autowired
    private UserBalanceDetailMapper userBalanceDetailMapper;

    @Override
    public void recharge(Integer userId, BigDecimal amount, Integer type) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包失败！");
        }
        if(type.intValue() == 1){
            //微信
        }else if(type.intValue() == 2){
            //支付宝
        }else{
            throw new ServiceException("充值类型错误！");
        }
        //用户充值明细
        UserRechargeDetail userRechargeDetail = new UserRechargeDetail();
        userRechargeDetail.setUserId(userId);
        userRechargeDetail.setAmount(amount);
        userRechargeDetail.setPayType(type);
        userRechargeDetail.setPayNo("");
        userRechargeDetail.setAddTime(DateStampUtils.getTimesteamp());
        userRechargeDetailMapper.insert(userRechargeDetail);
        //扣除余额 1充值 2消费 payType 1微信 2支付宝 3余额
        updateBalance(userId,amount,1);
    }

    @Override
    public void withdrawDeposit(Integer userId, BigDecimal amount, Integer type, String account, String realName, String payPwd) throws ServiceException {
        //获取被限制的用户（代理不可以余额提现）
        ConfinedUser confinedUser = confinedUserMapper.selectByUserIdStatus(userId, 0);
        if(confinedUser != null){
            throw new ServiceException("此用户被限制使用余额提现！");
        }
        UserInfoExt userInfoExt = userInfoExtMapper.selectUserInfoExtByUserId(userId);
        if(type != 1){//提现方式 0支付宝 1微信
            if(StringUtils.isEmpty(payPwd)){
                throw new ServiceException("支付密码为空！");
            }
            if(userInfoExt.getPayPwd()==null){
                throw new ServiceException("请先设置支付密码再提现");
            }
            if(MD5Util.verify(userInfoExt.getPayPwd(),MD5Util.MD5(payPwd)) == false){
                throw new ServiceException("支付密码错误！");
            }
        }
        //TODO 根据用户会员等级扣除不同手续费
        UserVip userVip = userVipMapper.selectByUserId(userId);
        Integer vipLevel = userVip.getVipLevel();//会员等级
        BigDecimal charge;//手续费
        BigDecimal percent;
        if(vipLevel.intValue() == 1){
            //铂金会员
            percent = new BigDecimal(0.008);
        }else if(vipLevel.intValue() == 2){
            //至尊会员
            percent = new BigDecimal(0.006);
        }else{
            //普通用户
            percent = new BigDecimal(0.008);
        }
        charge = amount.multiply(percent);
        amount = amount.add(charge);
        //扣除余额 type 类型 1充值 2消费 payType 支付类型 1微信 2支付宝 3余额
        updateBalance(userId,amount,2);
    }


    @Override
    public WalletDto getWallet(Integer userId) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包失败！");
        }else{
            UserInfoExt userInfoExt = userInfoExtMapper.selectUserInfoExtByUserId(userId);
            if(userInfoExt == null){
                throw new ServiceException("获取用户扩展信息失败！");
            }else{
                UserVip userVip = userVipMapper.selectByUserId(userId);
                WalletDto walletDto = new WalletDto(userWallet,userVip);
                //信誉度
                walletDto.setCredit(userInfoExt.getCredit());
                long credit = userInfoExt.getCredit();
                if(credit < -100){//差
                    walletDto.setCreditLevel(1);
                }else if(-100 <= credit && credit < 1000){//低
                    walletDto.setCreditLevel(2);
                }else if(1000 <= credit && credit < 10000){//中
                    walletDto.setCreditLevel(3);
                }else if(10000 <= credit && credit < 500000){//高
                    walletDto.setCreditLevel(4);
                }else if(500000 <= credit){//优
                    walletDto.setCreditLevel(5);
                }else{
                    throw new ServiceException("信誉度错误！");
                }
                return walletDto;
            }
        }
    }

    @Override
    public void updateScore(Integer userId, Long score,Integer type) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            Long updateScore = userWallet.getPoint()+score;
            if(type == 1){//增加积分 同时增加总积分和积分余额
                userWallet.setScore(userWallet.getScore()+score);
            }else{//减少积分
                //修改积分后的用户积分余额
                //如果修改后的积分余额低于0，就返回积分不足
                if(updateScore < 0){
                    throw new ServiceException("积分不足！");
                }
            }
            userWallet.setPoint(updateScore);
            userWalletMapper.updateByPrimaryKey(userWallet);
        }
    }

    @Override
    public void updateBalance(Integer userId, BigDecimal amount,Integer type) throws ServiceException {
        //获取用户钱包信息
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            if(type.intValue() == 1){//充值
                userWallet.setBalance(userWallet.getBalance().add(amount));
            }else if(type.intValue() == 2){//消费
                //获取被限制的用户（代理不可以余额提现）
                ConfinedUser confinedUser = confinedUserMapper.selectByUserIdStatus(userId, 0);
                if(confinedUser != null){
                    throw new ServiceException("此用户被限制使用余额提现！");
                }
                if(userWallet.getBalance().compareTo(amount) < 0){//余额小于消费金额
                    throw new ServiceException("余额不足！");
                }else{
                    userWallet.setBalance(userWallet.getBalance().subtract(amount));
                }
            }else{
                throw new ServiceException("类型错误！");
            }
            userWalletMapper.updateByPrimaryKey(userWallet);
        }
    }

}
