package com.fangyuanyouyue.user.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.IdentityAuthApplyMapper;
import com.fangyuanyouyue.user.dao.UserInfoExtMapper;
import com.fangyuanyouyue.user.model.IdentityAuthApply;
import com.fangyuanyouyue.user.model.UserInfoExt;
import com.fangyuanyouyue.user.service.UserInfoExtService;

@Service(value = "userInfoExtService")
public class UserInfoExtServiceImpl implements UserInfoExtService {

    @Autowired
    private IdentityAuthApplyMapper identityAuthApplyMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    protected RedisTemplate redisTemplate;

    @Override
    public void certification(String token, String name, String identity, String identityImgCoverUrl, String identityImgBackUrl) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7, TimeUnit.DAYS);
        IdentityAuthApply identityAuthApply = identityAuthApplyMapper.selectByUserId(userId);
        if(identityAuthApply != null){
            if(identityAuthApply.getStatus() == 1){
                throw new ServiceException("您已提交过实名认证，请耐心等待！");
            }else if(identityAuthApply.getStatus() == 2){
                throw new ServiceException("您的实名认证已通过，请勿重复提交！");
            }
        }else{
            //用户扩展信息表
            UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
            userInfoExt.setIdentity(identity);
            userInfoExt.setName(name);
            userInfoExt.setUserId(userId);
            userInfoExt.setStatus(2);//实名登记状态 1已实名 2未实名
            userInfoExt.setCredit(100);
            userInfoExt.setScore(0);
            userInfoExtMapper.insert(userInfoExt);

            //实名认证申请表
            identityAuthApply.setAddTime(DateStampUtils.getTimesteamp());
            identityAuthApply.setIdentity(identity);
            identityAuthApply.setName(name);
            identityAuthApply.setStatus(1);//状态 1申请 2通过 3拒绝
            identityAuthApply.setIdentityImgCover(identityImgCoverUrl);
            identityAuthApply.setIdentityImgBack(identityImgBackUrl);
            identityAuthApplyMapper.insert(identityAuthApply);
        }
    }

}
