package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dao.UserThirdPartyMapper;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.UserThirdParty;
import com.fangyuanyouyue.user.service.UserInfoService;
import com.fangyuanyouyue.user.service.UserThirdService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userThirdService")
public class UserThirdServiceImpl implements UserThirdService {
    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Override
    public boolean judgeMerge(String token, String unionId, String phone,Integer type) throws ServiceException {
        //当前用户
        UserInfo user = userInfoService.getUserByToken(token);
        UserThirdParty userThird = userThirdPartyMapper.getUserThirdByUserId(user.getId());
        if(unionId == null){
            //绑定手机
            //手机用户
            UserInfo phoneUser = userInfoService.getUserByPhone(phone);
            UserThirdParty phoneThird = userThirdPartyMapper.getUserThirdByUserId(phoneUser.getId());
            if(StringUtils.isEmpty(user.getPhone()) && phoneThird == null){
                return true;
            }
        }else if(phone == null){
            //三方绑定
            //三方用户
            UserInfo unionIdThird = userInfoService.getUserByUnionId(unionId, type);
            if(userThird == null && StringUtils.isEmpty(unionIdThird.getPhone())){
                return true;
            }
        }else{
            return false;
        }
        return false;
    }
}
