package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.wallet.dao.*;
import com.fangyuanyouyue.wallet.dto.BonusPoolDto;
import com.fangyuanyouyue.wallet.dto.WalletDto;
import com.fangyuanyouyue.wallet.model.*;
import com.fangyuanyouyue.wallet.service.WalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    @Override
    public void recharge(Integer userId, BigDecimal price, Integer type) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包失败！");
        }
        //扣除余额 1充值 2消费
        updateBalance(userId,price,1);
    }

    @Override
    public void withdrawDeposit(Integer userId, BigDecimal price, Integer type, String account, String realName, String payPwd) throws ServiceException {
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
        //扣除余额 1充值 2消费
        updateBalance(userId,price,2);
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
                //积分等级，计算总积分
//                long score = userWallet.getScore();
//                if(0 <= score && score < 500){//Lv1
//                    walletDto.setLevel(1);
//                    walletDto.setLevelDesc("升级还需"+(500-score)+"积分");
//                }else if(500 <= score && score < 3000){//Lv2
//                    walletDto.setLevel(2);
//                    walletDto.setLevelDesc("升级还需"+(3000-score)+"积分");
//                }else if(3000 <= score && score < 10000){//Lv3
//                    walletDto.setLevel(3);
//                    walletDto.setLevelDesc("升级还需"+(10000-score)+"积分");
//                }else if(10000 <= score && score < 30000){//Lv4
//                    walletDto.setLevel(4);
//                    walletDto.setLevelDesc("升级还需"+(30000-score)+"积分");
//                }else if(30000 <= score && score < 80000){//Lv5
//                    walletDto.setLevel(5);
//                    walletDto.setLevelDesc("升级还需"+(80000-score)+"积分");
//                }else if(80000 <= score && score < 200000){//Lv6
//                    walletDto.setLevel(6);
//                    walletDto.setLevelDesc("升级还需"+(200000-score)+"积分");
//                }else if(200000 <= score && score < 600000){//Lv7
//                    walletDto.setLevel(7);
//                    walletDto.setLevelDesc("升级还需"+(600000-score)+"积分");
//                }else if(600000 <= score && score < 1000000){//Lv8
//                    walletDto.setLevel(8);
//                    walletDto.setLevelDesc("升级还需"+(1000000-score)+"积分");
//                }else if(1000000 <= score){//Lv9
//                    walletDto.setLevel(9);
//                    walletDto.setLevelDesc("您已升至满级！");
//                }else{
//                    throw new ServiceException("积分错误！");
//                }
                return walletDto;
            }
        }
    }

    @Override
    public void updateScore(Integer userId, Long score) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            //修改积分后的用户积分余额
            Long updateScore = userWallet.getPoint()+score;
            if(score>=0){//增加积分，同时增加总积分和积分余额
                userWallet.setScore(userWallet.getScore()+score);
            }
            //如果修改后的积分余额低于0，就返回积分不足
            if(updateScore < 0){
                throw new ServiceException("积分不足！");
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
