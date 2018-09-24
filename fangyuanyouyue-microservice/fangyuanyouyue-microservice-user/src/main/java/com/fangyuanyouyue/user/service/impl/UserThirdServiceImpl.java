package com.fangyuanyouyue.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.dao.*;
import com.fangyuanyouyue.user.dto.MergeDto;
import com.fangyuanyouyue.user.model.UserFans;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.UserThirdParty;
import com.fangyuanyouyue.user.model.UserWallet;
import com.fangyuanyouyue.user.service.SchedualRedisService;
import com.fangyuanyouyue.user.service.UserInfoService;
import com.fangyuanyouyue.user.service.UserThirdService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "userThirdService")
@Transactional(rollbackFor=Exception.class)
public class UserThirdServiceImpl implements UserThirdService {
    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private UserFansMapper userFansMapper;
    @Autowired
    private UserMergeMapper userMergeMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public MergeDto judgeMerge(String token, String unionId, String phone,Integer type) throws ServiceException {
        //当前用户
        UserInfo user = userInfoService.getUserByToken(token);
        UserThirdParty userThird = userThirdPartyMapper.getUserThirdByUserId(user.getId(),type);
        MergeDto mergeDto = new MergeDto();
        mergeDto.setFromUserId(user.getId());
        mergeDto.setFromUserNickName(user.getNickName());
        mergeDto.setFromUserHeadImgUrl(user.getHeadImgUrl());
        if(unionId == null){
            //绑定手机
            //手机用户
            UserInfo phoneUser = userInfoService.getUserByPhone(phone);
            UserThirdParty phoneThird = userThirdPartyMapper.getUserThirdByUserId(phoneUser.getId(),type);
            if(StringUtils.isEmpty(user.getPhone()) && phoneThird == null){
                mergeDto.setToUserId(phoneUser.getId());
                mergeDto.setToUserNickName(phoneUser.getNickName());
                mergeDto.setToUserHeadImgUrl(phoneUser.getHeadImgUrl());
                return mergeDto;
            }
        }/*else if(phone == null){
            //三方绑定
            //三方用户
            UserInfo unionIdThird = userInfoService.getUserByUnionId(unionId, type);
            if(userThird == null && StringUtils.isEmpty(unionIdThird.getPhone())){
                mergeDto.setToUserId(unionIdThird.getId());
                mergeDto.setToUserNickName(unionIdThird.getNickName());
                mergeDto.setToUserHeadImgUrl(unionIdThird.getHeadImgUrl());
                return mergeDto;
            }
        }*/else{
            return null;
        }
        return null;
    }


    //合并账号
    @Override
    public void accountMerge(String token, String phone, String unionId, Integer type, String loginPwd) throws ServiceException {
        //1、修改手机用户的密码 2、修改三方用户的userId 3、修改数据表中userId（钱包表：合并、会员表：合并、） 4、清除双方token，让用户重新登录
        //当前用户
        UserInfo user = userInfoService.getUserByToken(token);
        UserThirdParty userThird = userThirdPartyMapper.getUserThirdByUserId(user.getId(),type);
        if(unionId == null){
            //三方用户发起+绑定手机用户 = 新·手机用户

            UserInfo phoneUser = userInfoService.getUserByPhone(phone);
            UserThirdParty phoneThird = userThirdPartyMapper.getUserThirdByUserId(phoneUser.getId(),type);
            if(StringUtils.isEmpty(user.getPhone()) && phoneThird == null){
                //修改手机用户的密码
                phoneUser.setLoginPwd(MD5Util.generate(MD5Util.MD5(loginPwd)));
                userInfoMapper.updateByPrimaryKeySelective(phoneUser);
                //将三方改到手机号账号
                userThird.setUserId(phoneUser.getId());
                userThirdPartyMapper.updateByPrimaryKeySelective(userThird);
                //1、userWallet：合并
                UserWallet userWallet = userWalletMapper.selectByUserId(user.getId());
                UserWallet phoneUserWallet = userWalletMapper.selectByUserId(phoneUser.getId());
                userWallet.setBalance(userWallet.getBalance().add(phoneUserWallet.getBalance()));
                userWallet.setBalanceFrozen(userWallet.getBalanceFrozen().add(phoneUserWallet.getBalanceFrozen()));
                userWallet.setPoint(userWallet.getPoint()+phoneUserWallet.getPoint());
                userWallet.setScore(userWallet.getScore()+phoneUserWallet.getScore());
                userWalletMapper.updateByPrimaryKeySelective(userWallet);
                //2、userFans：如果to_user_id == 三方用户id，如果user_id != 手机用户，修改to_user_id 为 手机用户id，如果user_id == 手机用户，删除此条记录
                List<UserFans> userFansList = userFansMapper.userFans(user.getId(), null, null);
                if(userFansList != null && userFansList.size() >0){
                    for(UserFans fans:userFansList){
                        if(fans.getUserId().intValue() == phoneUser.getId().intValue()){
                            userFansMapper.deleteByPrimaryKey(fans.getId());
                        }else{
                            fans.setToUserId(phoneUser.getId());
                            userFansMapper.updateByPrimaryKeySelective(fans);
                        }
                    }
                }
                //3、userCoupon：修改userId
                userMergeMapper.MergeCoupon(user.getId(),phoneUser.getId());
                //4、goodsComment：修改userId
                userMergeMapper.MergeGoodsComment(user.getId(),phoneUser.getId());
                //5、forumComment：修改userId
                userMergeMapper.MergeForumComment(user.getId(),phoneUser.getId());


                //删除三方账号
                user.setStatus(3);
                userInfoMapper.updateByPrimaryKeySelective(user);
                //清除旧token
                //1、设置三方用户token失效 2、设置手机用户token失效
                //设置旧token失效
                String userToken = JSONObject.parseObject(schedualRedisService.getToken(user.getId().toString())).getString("data");
                if(StringUtils.isNotEmpty(userToken)){
                    schedualRedisService.set(userToken,null,null);
                }
                String phoneUserToken = JSONObject.parseObject(schedualRedisService.getToken(phoneUser.getId().toString())).getString("data");
                if(StringUtils.isNotEmpty(phoneUserToken)){
                    schedualRedisService.set(phoneUserToken,null,null);
                }
            }
        }/*else if(phone == null){
            //手机用户发起 + 绑定三方用户 = 新·手机用户

            UserInfo unionIdThird = userInfoService.getUserByUnionId(unionId, type);
            if(userThird == null && StringUtils.isEmpty(unionIdThird.getPhone())){

                //最后将三方改到手机号账号
                UserThirdParty userByThirdNoType = userThirdPartyMapper.getUserByThirdNoType(unionId, type);
                userByThirdNoType.setUserId(user.getId());
                //删除三方账号
                unionIdThird.setStatus(3);
                //清除旧token
                return new UserDto();
            }
        }*/
    }

}
