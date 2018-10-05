package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.enums.Score;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.base.util.IdGenerator;
import com.fangyuanyouyue.wallet.dao.*;
import com.fangyuanyouyue.wallet.dto.BonusPoolDto;
import com.fangyuanyouyue.wallet.model.*;
import com.fangyuanyouyue.wallet.service.ScoreService;
import com.fangyuanyouyue.wallet.service.UserCouponService;
import com.fangyuanyouyue.wallet.service.UserVipService;
import com.fangyuanyouyue.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service(value = "scoreService")
@Transactional(rollbackFor=Exception.class)
public class ScoreServiceImpl implements ScoreService{
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private BonusPoolMapper bonusPoolMapper;
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserScoreDetailMapper userScoreDetailMapper;
    @Autowired
    private UserVipMapper userVipMapper;

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
                updateScore(userId,10000L,2);
                //增加用户积分、优惠券
                if(bonus.getType() == 1){
                    //积分
                    updateScore(userId,bonus.getScore(),1);
                }else if(bonus.getType() == 2){
                    //优惠券
                    //保存用户优惠券
                    userCouponService.insertUserCoupon(userId,bonus.getCouponId());
                }
                return bonus.getBonusName();
            }
        }
    }

    @Override
    public void shareHtml(Integer userId) throws ServiceException {
        updateScore(userId, Score.SHARE.getScore(), Status.ADD.getValue());
    }

    public static void main(String[] args) {
        String date = DateUtil.getFormatDate(new Date(),DateUtil.DATE_FORMT_YEAR);
        System.out.println(date);
    }
    @Override
    public void updateScore(Integer userId, Long score,Integer type) throws ServiceException {
        //每个用户每天可增加500积分 增加一张用户积分记录表，记录用户积分增加历史，按天筛选，不可超过500分
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        if(userWallet == null){
            throw new ServiceException("获取钱包信息失败！");
        }else{
            if(type.equals(Status.ADD.getValue())){
                //判断是否满500积分
                Long userScore = userScoreDetailMapper.getUserScoreByDay(userId);
                //铂金2倍 至尊4倍
                UserVip userVip = userVipMapper.selectByUserId(userId);

                //大于等于500不再加分，小于500：判断要增加的积分与500的差值，如果今日收益+要增加的积分>500，则增加500-今日收益的积分，否则增加原积分
                if(userScore != null){
                    if(userVip.getStatus().intValue() == Status.IS_VIP.getValue()){
                        if(userVip.getVipLevel().intValue() == Status.VIP_LEVEL_LOW.getValue()){
                            score = score * 2;
                            if(userScore >= 1000L) {
                                return;
                            }else{
                                if(userScore + score > 1000L){
                                    score = 1000L - userScore;
                                }
                            }

                        }else{
                            score = score * 4;
                            if(userScore >= 2000L) {
                                return;
                            }else{
                                if(userScore + score > 2000L){
                                    score = 2000L - userScore;
                                }
                            }

                        }
                    }else{
                        if(userScore >= 500L) {
                            return;
                        }else{
                            if(userScore + score > 500L){
                                score = 500L - userScore;
                            }
                        }
                    }

                }
                userWallet.setScore(userWallet.getScore()+score);
                userWallet.setPoint(userWallet.getPoint()+score);
                //修改用户等级
                UserInfo info = userInfoMapper.selectByPrimaryKey(userId);
                //积分等级，计算总积分
                WalletServiceImpl.setUserLevel(userWallet.getScore(), info);
                userInfoMapper.updateByPrimaryKeySelective(info);
            }else{
                //如果修改后的积分余额低于0，就返回积分不足
                Long updateScore = userWallet.getPoint()-score;
                if(updateScore < 0){
                    throw new ServiceException("积分不足！");
                }
                userWallet.setPoint(updateScore);
            }
            UserScoreDetail userScoreDetail = new UserScoreDetail();
            userScoreDetail.setUserId(userId);
            userScoreDetail.setAddTime(DateStampUtils.getTimesteamp());
            //订单号
            final IdGenerator idg = IdGenerator.INSTANCE;
            String orderNo = idg.nextId();
            userScoreDetail.setOrderNo(orderNo);
            userScoreDetail.setScore(score);
            userScoreDetail.setType(type);
            userScoreDetailMapper.insert(userScoreDetail);
            userWalletMapper.updateByPrimaryKeySelective(userWallet);
        }
    }

}
