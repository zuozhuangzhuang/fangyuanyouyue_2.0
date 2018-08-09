package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dao.UserVipMapper;
import com.fangyuanyouyue.wallet.model.UserVip;
import com.fangyuanyouyue.wallet.service.UserVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userVipService")
public class UserVipServiceImpl implements UserVipService{

    @Autowired
    private UserVipMapper userVipMapper;

    @Override
    public void openMebber(Integer userId, Integer vipLevel, Integer vipType) throws ServiceException {
        UserVip userVip = userVipMapper.selectByUserId(userId);
        if(userVip == null){
            throw new ServiceException("获取用户会员信息失败！");
        }
    }


}
