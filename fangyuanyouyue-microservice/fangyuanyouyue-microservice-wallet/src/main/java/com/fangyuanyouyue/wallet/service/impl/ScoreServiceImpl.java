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
import com.fangyuanyouyue.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service(value = "scoreService")
public class ScoreServiceImpl implements ScoreService{
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private BonusPoolMapper bonusPoolMapper;
    @Autowired
    private WalletService walletService;

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
                BonusPool bonus = new BonusPool();
                //TODO 抽奖
                //总的概率区间
                float totalPro = 0f;
                //存储每个奖品新的概率区间
                List<Float> proSection = new ArrayList<>();
                proSection.add(0f);
                //遍历每个奖品，设置概率区间，总的概率区间为每个概率区间的总和
                List<BonusPool> bonusPools = bonusPoolMapper.selectAll();
                for (BonusPool bonusPool : bonusPools) {
                    //每个概率区间为奖品概率乘以1000（把三位小数转换为整）再乘以剩余奖品数量
                    totalPro += bonusPool.getProbability() * 10;
                    proSection.add(totalPro);
                }
                //获取总的概率区间中的随机数
                Random random = new Random();
                float randomPro = (float)random.nextInt((int)totalPro);
                //判断取到的随机数在哪个奖品的概率区间中
                for (int i = 0,size = proSection.size(); i < size; i++) {
                    if(randomPro >= proSection.get(i) && randomPro < proSection.get(i + 1)){
                        bonus = bonusPools.get(i);
                    }
                }
                //扣除积分余额
                walletService.updateScore(userId,10000L,2);
                return bonus.getBonusName();
            }
        }
    }
}
