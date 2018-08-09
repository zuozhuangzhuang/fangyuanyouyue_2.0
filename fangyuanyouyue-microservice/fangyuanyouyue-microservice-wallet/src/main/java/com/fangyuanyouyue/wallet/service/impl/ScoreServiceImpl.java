package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dao.BonusPoolMapper;
import com.fangyuanyouyue.wallet.dao.UserInfoExtMapper;
import com.fangyuanyouyue.wallet.dao.UserWalletMapper;
import com.fangyuanyouyue.wallet.dto.BonusPoolDto;
import com.fangyuanyouyue.wallet.model.BonusPool;
import com.fangyuanyouyue.wallet.model.UserInfoExt;
import com.fangyuanyouyue.wallet.model.UserWallet;
import com.fangyuanyouyue.wallet.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "scoreService")
public class ScoreServiceImpl implements ScoreService{
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private BonusPoolMapper bonusPoolMapper;

    @Override
    public List<BonusPoolDto> getBonusPool() throws ServiceException {
        List<BonusPool> bonusPools = bonusPoolMapper.selectAll();
        List<BonusPoolDto> bonusPoolDtos = BonusPoolDto.toDtoList(bonusPools);
        return bonusPoolDtos;
    }

    @Override
    public String lottery(Integer userId) throws ServiceException {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包失败！");
        }else {
            UserInfoExt userInfoExt = userInfoExtMapper.selectUserInfoExtByUserId(userId);
            if (userInfoExt == null) {
                throw new ServiceException("获取用户扩展信息失败！");
            } else {

            }
        }
        return null;
    }
}
