package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.enums.Credit;
import com.fangyuanyouyue.base.enums.Score;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.wallet.dao.UserBehaviorMapper;
import com.fangyuanyouyue.wallet.dao.UserInfoMapper;
import com.fangyuanyouyue.wallet.model.UserBehavior;
import com.fangyuanyouyue.wallet.model.UserInfo;
import com.fangyuanyouyue.wallet.service.SchedualMessageService;
import com.fangyuanyouyue.wallet.service.UserBehaviorService;
import com.fangyuanyouyue.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userBehaviorService")
public class UserBehaviorServiceImpl implements UserBehaviorService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserBehaviorMapper userBehaviorMapper;
    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualMessageService schedualMessageService;

    @Override
    public void addUserBehavior(Integer userId, Integer toUserId, Integer businessId, Integer businessType, Integer type) throws ServiceException {
        UserBehavior userBehavior = userBehaviorMapper.selectByUserIdType(userId, businessId, businessType, type);
        if(userBehavior == null){
            //用户首次行为
            userBehavior = new UserBehavior();
            userBehavior.setUserId(userId);
            userBehavior.setBusinessId(businessId);
            userBehavior.setBusinessType(businessType);
            userBehavior.setType(type);
            userBehavior.setToUserId(toUserId);
            userBehavior.setAddTime(DateStampUtils.getTimesteamp());
            userBehaviorMapper.insert(userBehavior);
            //增加积分、信誉度
            if(type.intValue() == Status.BEHAVIOR_TYPE_LIKES.getValue().intValue()){
                walletService.updateScore(userId,Score.LIKES.getScore(), Status.ADD.getValue());
                walletService.updateCredit(toUserId, Credit.LIKES.getCredit(),Status.ADD.getValue());
            }else if(type.intValue() == Status.BEHAVIOR_TYPE_FANS.getValue().intValue()){
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
                walletService.updateCredit(toUserId,Credit.FANS.getCredit(),Status.ADD.getValue());
                schedualMessageService.easemobMessage(toUserId.toString(),
                        "用户“"+userInfo.getNickName()+"”已关注了您！","1","4","");
            }else if(type.intValue() == Status.BEHAVIOR_TYPE_COMMENT.getValue().intValue()){
                //评论
                walletService.updateScore(userId,Score.COMMENT.getScore(),Status.ADD.getValue());
                walletService.updateCredit(toUserId,Credit.COMMENT.getCredit(),Status.ADD.getValue());
            }
        }else{
            //用户非首次行为
            return;
        }
    }
}
