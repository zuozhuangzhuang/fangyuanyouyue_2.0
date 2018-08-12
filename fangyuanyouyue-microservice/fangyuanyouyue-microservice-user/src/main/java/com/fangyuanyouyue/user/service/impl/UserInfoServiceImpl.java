package com.fangyuanyouyue.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.constant.StatusEnum;
import com.fangyuanyouyue.user.dao.IdentityAuthApplyMapper;
import com.fangyuanyouyue.user.dao.UserFansMapper;
import com.fangyuanyouyue.user.dao.UserInfoExtMapper;
import com.fangyuanyouyue.user.dao.UserInfoMapper;
import com.fangyuanyouyue.user.dao.UserThirdPartyMapper;
import com.fangyuanyouyue.user.dao.UserVipMapper;
import com.fangyuanyouyue.user.dao.UserWalletMapper;
import com.fangyuanyouyue.user.dto.ShopDto;
import com.fangyuanyouyue.user.dto.UserDto;
import com.fangyuanyouyue.user.dto.UserFansDto;
import com.fangyuanyouyue.user.dto.WaitProcessDto;
import com.fangyuanyouyue.user.model.UserFans;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.UserInfoExt;
import com.fangyuanyouyue.user.model.UserThirdParty;
import com.fangyuanyouyue.user.model.UserVip;
import com.fangyuanyouyue.user.model.UserWallet;
import com.fangyuanyouyue.user.param.UserParam;
import com.fangyuanyouyue.user.service.SchedualGoodsService;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import com.fangyuanyouyue.user.service.SchedualOrderService;
import com.fangyuanyouyue.user.service.SchedualRedisService;
import com.fangyuanyouyue.user.service.UserInfoService;

@Service(value = "userInfoService")
@Transactional(rollbackFor=Exception.class)
public class UserInfoServiceImpl implements UserInfoService {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;
    @Autowired
    private IdentityAuthApplyMapper identityAuthApplyMapper;
    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    @Autowired
    private UserVipMapper userVipMapper;
    @Autowired
    private SchedualGoodsService schedualGoodsService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private UserFansMapper userFansMapper;
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private SchedualOrderService schedualOrderService;
    @Autowired
    private SchedualMessageService schedualMessageService;

    @Override
    public UserInfo getUserByToken(String token) throws ServiceException {
        Integer userId = (Integer)schedualRedisService.get(token);
        if(userId!=null) {
            //更新时间
            schedualRedisService.set(token, userId.toString(), 7*24*60*60l);
            schedualRedisService.set(userId.toString(), token, 7*24*60*60l);
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
            return userInfo;
        }
        return null;
    }
    
    /**
     * 设置token
     * @param token
     * @param userId
     */
    private String setToken(String token,Integer userId){
        //生成用户token，存到Redis
        token = 10000+userId+"FY"+DateStampUtils.getGMTUnixTimeByCalendar()+"";
        
        //覆盖原来的
        schedualRedisService.set(token, userId.toString(), 7*24*60*60l);
        schedualRedisService.set(userId.toString(), token, 7*24*60*60l);
        
        //redisTemplate.opsForValue().set(token,userId);
       // redisTemplate.expire(token,7,TimeUnit.DAYS);
        
       // if(redisTemplate.opsForValue().get(userId) != null){
            //更新userId:token的value，同时使旧token失效
       //     redisTemplate.opsForValue().set(redisTemplate.opsForValue().get(userId),null);
       // }
        //存入userId:token，用来在更新token时确保旧token失效，时效7天
       // schedualRedisService.set(userId,token);
       // redisTemplate.expire(userId,7,TimeUnit.DAYS);
        return token;
    }
    

    @Override
    public UserInfo selectByPrimaryKey(Integer id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public UserInfo getUserByPhone(String phone) {
        return userInfoMapper.getUserByPhone(phone);
    }

    @Override
    public UserInfo getUserByNickName(String nickName) throws ServiceException{
        UserInfo userInfo = userInfoMapper.getUserByNickName(nickName);
        return userInfo;
    }

    @Override
    public UserDto regist(UserParam param) throws ServiceException{
        //初始化用户信息
        UserInfo user = new UserInfo();
        //手机号注册必定是APP端
        user.setHeadImgUrl(param.getHeadImgUrl());
        user.setBgImgUrl(param.getBgImgUrl());
        user.setRegType(1);//注册来源 1APP 2微信小程序
        user.setRegPlatform(param.getRegPlatform());
        user.setAddTime(DateStampUtils.getTimesteamp());
        user.setPhone(param.getPhone());
        user.setLoginPwd(MD5Util.generate(MD5Util.MD5(param.getLoginPwd())));
        //TODO 昵称筛选敏感字
        user.setNickName(param.getNickName());
        user.setStatus(1);//状态 1正常 2冻结
        user.setGender(param.getGender());
        userInfoMapper.insert(user);
        //设置用户token到Redis
        String token = setToken("",user.getId());
        //用户扩展信息表
        UserInfoExt userInfoExt = new UserInfoExt();
        userInfoExt.setUserId(user.getId());
        userInfoExt.setStatus(2);//实名登记状态 1已实名 2未实名
        userInfoExt.setAuthType(2);//认证状态 1已认证 2未认证
        //TODO 信誉度待定
        userInfoExt.setCredit(100L);//信誉度
        userInfoExt.setAddTime(DateStampUtils.getTimesteamp());
        userInfoExtMapper.insert(userInfoExt);
        //用户会员系统
        UserVip userVip = new UserVip();
        userVip.setUserId(user.getId());
        userVip.setStatus(2);//会员状态1已开通 2未开通
        userVip.setAddTime(DateStampUtils.getTimesteamp());
        userVipMapper.insert(userVip);
        //TODO 注册通讯账户
        registIMUser(user);
        //TODO 调用钱包系统初始化接口
        UserWallet userWallet = new UserWallet();
        userWallet.setUserId(user.getId());
        userWallet.setBalance(new BigDecimal(0));
        userWallet.setBalanceFrozen(new BigDecimal(0));
        userWallet.setPoint(0L);//剩余积分
        userWallet.setScore(0L);//用户总积分
        userWallet.setAddTime(DateStampUtils.getTimesteamp());
        userWalletMapper.insert(userWallet);
        //初始化用户钱包
        UserDto userDto = setUserDtoByInfo(token,user);
        return userDto;
    }

    @Override
    public UserDto login(String phone,String loginPwd,Integer lastLoginPlatform) throws ServiceException{
        UserInfo user = userInfoMapper.getUserByPhone(phone);
        //账号或密码错误处理
        if(user == null){
            throw new ServiceException("账号不正确！");
        }else{
//            if(!user.getLoginPwd().equals(loginPwd)){
            if(!MD5Util.verify(MD5Util.MD5(loginPwd),user.getLoginPwd())){
                //密码错误次数
                if(user.getPwdErrCount() == null || user.getPwdErrCount() == 0){
                    user.setPwdErrCount(1);
                }else{
                    user.setPwdErrCount(user.getPwdErrCount()+1);
                }
                userInfoMapper.updateByPrimaryKey(user);
                //TODO 根据密码错误次数进行操作
                throw new ServiceException("密码不正确！");
            }else{
                user.setPwdErrCount(0);
                user.setLastLoginTime(DateStampUtils.getTimesteamp());
                user.setLastLoginPlatform(lastLoginPlatform);
                userInfoMapper.updateByPrimaryKey(user);
                //设置用户token到Redis
                String token = setToken("",user.getId());
                //TODO 注册通讯账户
                registIMUser(user);
                
                //TODO 获取用户的相关信息：好友列表
                UserDto userDto = setUserDtoByInfo(token,user);
                return userDto;
            }
        }
    }


    @Override
    public UserDto thirdLogin(UserParam param) throws ServiceException {
        //根据第三方唯一ID和类型获取第三方登录信息
        UserThirdParty userThirdParty = userThirdPartyMapper.getUserByThirdNoType(param.getUnionId(),param.getType());
        if(userThirdParty == null){
            log.info("三方注册");
            //注册
            //初始化用户信息
            UserInfo user = new UserInfo();
            if(StringUtils.isEmpty(param.getThirdNickName())){
                throw new ServiceException("第三方账号昵称不能为空！");
            }else{
                //第三方昵称末尾加随机数
                user.setNickName(param.getThirdNickName()+"-"+((int)(Math.random() * 9000) + 1000));
            }
            user.setHeadImgUrl(param.getThirdHeadImgUrl());
            user.setRegType(param.getRegType());
            user.setRegPlatform(param.getLoginPlatform());
            user.setLastLoginPlatform(param.getLoginPlatform());
            user.setAddTime(DateStampUtils.getTimesteamp());
            user.setStatus(1);//状态 1正常 2冻结
            if(param.getGender() != null){
                user.setGender(param.getGender());
            }
            userInfoMapper.insert(user);
            //设置用户token到Redis
            String token = setToken("",user.getId());
            //初始化用户第三方登录信息
            userThirdParty = new UserThirdParty();
            userThirdParty.setUserId(user.getId());
            userThirdParty.setNickName(param.getThirdNickName());
            userThirdParty.setHeadImgUrl(param.getThirdHeadImgUrl());
            userThirdParty.setUnionId(param.getUnionId());
            userThirdParty.setType(param.getType());
            userThirdParty.setAddTime(DateStampUtils.getTimesteamp());
            userThirdPartyMapper.insert(userThirdParty);
            //用户扩展信息表
            UserInfoExt userInfoExt = new UserInfoExt();
            userInfoExt.setUserId(user.getId());
            userInfoExt.setStatus(2);//实名登记状态 1已实名 2未实名
            userInfoExt.setAuthType(2);//认证状态 1已认证 2未认证
            //TODO 信誉度待定
            userInfoExt.setCredit(100L);//信誉度
            userInfoExt.setAddTime(DateStampUtils.getTimesteamp());
            userInfoExtMapper.insert(userInfoExt);
            //用户会员系统
            UserVip userVip = new UserVip();
            userVip.setUserId(user.getId());
            userVip.setStatus(2);//会员状态1已开通 2未开通
            userVip.setAddTime(DateStampUtils.getTimesteamp());
            userVipMapper.insert(userVip);
            //TODO 注册通讯账户
            registIMUser(user);
            
            //TODO 调用钱包系统初始化接口
            UserWallet userWallet = new UserWallet();
            userWallet.setUserId(user.getId());
            userWallet.setBalance(new BigDecimal(0));
            userWallet.setBalanceFrozen(new BigDecimal(0));
            userWallet.setPoint(0L);
            userWallet.setScore(0L);//用户总积分
            userWallet.setAddTime(DateStampUtils.getTimesteamp());
            userWalletMapper.insert(userWallet);
            UserDto userDto = setUserDtoByInfo(token,user);
            return userDto;
        }else{
            //记录用户登录时间，登录平台，最后一次登录
            log.info("三方登录");
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userThirdParty.getUserId());
            userInfo.setLastLoginTime(DateStampUtils.getTimesteamp());
            userInfo.setLastLoginPlatform(param.getLoginPlatform());
            userInfoMapper.updateByPrimaryKey(userInfo);

            registIMUser(userInfo);
            
            String token = setToken("",userInfo.getId());
            UserDto userDto = setUserDtoByInfo(token,userInfo);
            return userDto;
        }
    }

    @Override
    public UserDto thirdBind(String token,String unionId,Integer type) throws ServiceException {
        //Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        //redisTemplate.expire(token,7,TimeUnit.DAYS);
        //根据用户ID获取用户，生成新的三方登陆信息
        //UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        
    	UserInfo userInfo = getUserByToken(token);
        
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserThirdParty userThirdParty = userThirdPartyMapper.getUserByThirdNoType(unionId,type);
            //校验是否绑定三方账号
            if(userThirdParty != null){
                if(userThirdParty.getUserId() == userInfo.getId()){
                    throw new ServiceException("请勿重复绑定！");
                }else{
                    UserInfo user = userInfoMapper.selectByPrimaryKey(userThirdParty.getUserId());
                    if(StringUtils.isEmpty(user.getPhone())){
                        //TODO 是否合并
                        throw new ServiceException("是否合并用户！");
                    }else{
                        throw new ServiceException("已绑定其他用户！");
                    }
                }
            }else{
                userThirdParty.setUserId(userInfo.getId());
                userThirdParty.setType(type);
                userThirdParty.setUnionId(unionId);
                userThirdParty.setAddTime(DateStampUtils.getTimesteamp());
                userThirdPartyMapper.insert(userThirdParty);
                UserDto userDto = setUserDtoByInfo(token,userInfo);
                return userDto;
            }

        }
    }

    @Override
    public void resetPwd(String phone, String newPwd) throws ServiceException{
        UserInfo userInfo = userInfoMapper.getUserByPhone(phone);
        updatePwd(newPwd, userInfo);
    }

    @Override
    public void updatePwd(String token, String newPwd) throws ServiceException {
        //Integer userId = (Integer)redisTemplate.opsForValue().get(token);
       // redisTemplate.expire(token,7,TimeUnit.DAYS);
       // UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);

    	UserInfo userInfo = getUserByToken(token);
        updatePwd(newPwd, userInfo);
    }

    @Override
    public UserDto modify(UserParam param) throws ServiceException {
       // Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
       // redisTemplate.expire(param.getToken(),7,TimeUnit.DAYS);
        //UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);

    	UserInfo userInfo = getUserByToken(param.getToken());
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            //用户信息
            if(StringUtils.isNotEmpty(param.getPhone())){
                userInfo.setPhone(param.getPhone());
            }
            if(StringUtils.isNotEmpty(param.getEmail())){
                userInfo.setEmail(param.getEmail());
            }
            if(StringUtils.isNotEmpty(param.getNickName())){
                userInfo.setNickName(param.getNickName());
            }
            // 保存头像
            if(StringUtils.isNotEmpty(param.getHeadImgUrl())){
                userInfo.setHeadImgUrl(param.getHeadImgUrl());
            }
            //保存背景图片
            userInfo.setBgImgUrl(param.getBgImgUrl());
            if(param.getGender() != null){
                userInfo.setGender(param.getGender());
            }
            if(StringUtils.isNotEmpty(param.getSignature())){
                userInfo.setSignature(param.getSignature());
            }
            if(StringUtils.isNotEmpty(param.getContact())){
                userInfo.setContact(param.getContact());
            }
            if(StringUtils.isNotEmpty(param.getUserAddress())){
                userInfo.setAddress(param.getUserAddress());
            }
            userInfoMapper.updateByPrimaryKey(userInfo);
            //用户扩展信息表
            UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userInfo.getId());
            if(userInfoExt == null){
                throw new ServiceException("用户扩展信息错误！");
            }
            if(StringUtils.isNotEmpty(param.getIdentity())){
                userInfoExt.setIdentity(param.getIdentity());
            }
            if(StringUtils.isNotEmpty(param.getName())){
                userInfoExt.setName(param.getName());
            }
            if(StringUtils.isNotEmpty(param.getPayPwd())){
                userInfoExt.setPayPwd(MD5Util.generate(MD5Util.MD5(param.getPayPwd())));
            }
            userInfoExtMapper.updateByPrimaryKey(userInfoExt);
            UserDto userDto = setUserDtoByInfo(param.getToken(),userInfo);
            return userDto;
        }
    }


    @Override
    public UserDto updatePhone(String token, String phone) throws ServiceException {
        //Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        //redisTemplate.expire(token,7,TimeUnit.DAYS);
        //UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);

    	UserInfo userInfo = getUserByToken(token);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            userInfo.setPhone(phone);
            userInfoMapper.updateByPrimaryKey(userInfo);
            UserDto userDto = setUserDtoByInfo(token,userInfo);
            return userDto;
        }
    }

    @Override
    public UserDto accountMerge(String token, String phone) throws ServiceException {
        //Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        //redisTemplate.expire(token,7,TimeUnit.DAYS);
        //UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
    	UserInfo userInfo = getUserByToken(token);
        if(userInfo == null){
            throw new ServiceException("第三方用户不存在！");
        }else{
            UserInfo userInfoByPhone = userInfoMapper.getUserByPhone(phone);
            if(userInfoByPhone == null){
                throw new ServiceException("手机号用户不存在！");
            }else{
                //TODO 合并余额、粉丝、关注、收藏、商品、收货地址、好友、会员时间
                //TODO 清除旧token
                //第三方登录账号在未绑定手机号时拥有功能：

                userInfoMapper.updateByPrimaryKey(userInfo);
                UserDto userDto = setUserDtoByInfo(token,userInfo);
                return userDto;
            }
        }
    }

    /**
     * 将userInfo封装到userDto中
     * @param user
     * @return
     * @throws ServiceException
     */
    private UserDto setUserDtoByInfo(String token,UserInfo user) throws ServiceException{
        if(user == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(user.getId());
//            List<UserAddressInfo> userAddressInfos = userAddressInfoMapper.selectAddressByUserId(user.getId());
//            UserThirdParty userThirdParty = userThirdPartyMapper.getUserThirdByUserId(user.getId());
            UserVip userVip = userVipMapper.getUserVipByUserId(user.getId());
//            IdentityAuthApply identityAuthApply = identityAuthApplyMapper.selectByUserId(user.getId());
            UserDto userDto = new UserDto(token,user,userVip,userInfoExt);
            //TODO 单独请求用户等级，还是返回到UserDto中
            UserWallet userWallet = userWalletMapper.selectByUserId(user.getId());
            if(userWallet == null){
                throw new ServiceException("获取钱包失败！");
            }else{
                long score = userWallet.getScore();
                if(0 <= score && score < 500){//Lv1
                    userDto.setLevel(1);
                }else if(500 <= score && score < 3000){//Lv2
                    userDto.setLevel(2);
                }else if(3000 <= score && score < 10000){//Lv3
                    userDto.setLevel(3);
                }else if(10000 <= score && score < 30000){//Lv4
                    userDto.setLevel(4);
                }else if(30000 <= score && score < 80000){//Lv5
                    userDto.setLevel(5);
                }else if(80000 <= score && score < 200000){//Lv6
                    userDto.setLevel(6);
                }else if(200000 <= score && score < 600000){//Lv7
                    userDto.setLevel(7);
                }else if(600000 <= score && score < 1000000){//Lv8
                    userDto.setLevel(8);
                }else if(1000000 <= score){//Lv9
                    userDto.setLevel(9);
                }else{
                    throw new ServiceException("积分错误！");
                }
            }
            return userDto;
        }
    }


    /**
     * 修改密码
     * @param newPwd
     * @param userInfo
     * @throws ServiceException
     */
    private void updatePwd(String newPwd, UserInfo userInfo) throws ServiceException {
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
//            if(MD5Util.getMD5String(newPwd).equals(userInfo.getLoginPwd())){
            if(MD5Util.verify(MD5Util.MD5(newPwd),userInfo.getLoginPwd())){
                throw new ServiceException("不能和旧密码相同！");
            }else{
                userInfo.setLoginPwd(MD5Util.generate(MD5Util.MD5(newPwd)));
                userInfoMapper.updateByPrimaryKey(userInfo);
            }
        }
    }

    @Override
    public UserDto miniLogin(UserParam param,String openid,String session_key) throws ServiceException {
        //用户登录
        //根据unionId和type获取用户第三方登录信息
        UserThirdParty userThirdParty = userThirdPartyMapper.getUserByThirdNoType(param.getUnionId(),1);
        if(userThirdParty == null){
            //如果用户为空，注册
            //初始化用户信息
            UserInfo user = new UserInfo();
            user.setNickName(param.getThirdNickName()+"-"+((int)(Math.random() * 9000) + 1000));
            user.setHeadImgUrl(param.getThirdHeadImgUrl());
            user.setRegType(2);//注册来源 1app 2微信小程序
            user.setRegPlatform(3);//注册平台 1安卓 2iOS 3小程序
            user.setAddTime(DateStampUtils.getTimesteamp());
            user.setStatus(1);//状态 1正常 2冻结
            if(param.getGender() != null){
                user.setGender(param.getGender());
            }
            userInfoMapper.insert(user);
            //设置用户token到Redis
            String token = setToken("",user.getId());
            //初始化用户第三方登录信息
            userThirdParty = new UserThirdParty();
            userThirdParty.setUserId(user.getId());
            userThirdParty.setNickName(param.getThirdNickName());
            userThirdParty.setHeadImgUrl(param.getThirdHeadImgUrl());
            userThirdParty.setUnionId(param.getUnionId());
            userThirdParty.setMiniOpenId(openid);
             userThirdParty.setSessionKey(session_key);
            userThirdParty.setType(1);//类型 1微信 2QQ 3微博
            userThirdParty.setAddTime(DateStampUtils.getTimesteamp());
            userThirdPartyMapper.insert(userThirdParty);
            //用户扩展信息表
            UserInfoExt userInfoExt = new UserInfoExt();
            userInfoExt.setUserId(user.getId());
            userInfoExt.setStatus(2);//实名登记状态 1已实名 2未实名
            userInfoExt.setAuthType(2);//认证状态 1已认证 2未认证
            //TODO 信誉度待定
            userInfoExt.setCredit(100L);//信誉度
            userInfoExt.setAddTime(DateStampUtils.getTimesteamp());
            userInfoExtMapper.insert(userInfoExt);
            //用户会员系统
            UserVip userVip = new UserVip();
            userVip.setUserId(user.getId());
            userVip.setStatus(2);//会员状态 1已开通 2未开通
            userVip.setAddTime(DateStampUtils.getTimesteamp());
            userVipMapper.insert(userVip);
            //TODO 注册通讯账户
            registIMUser(user);
            //TODO 调用钱包系统初始化接口
            UserWallet userWallet = new UserWallet();
            userWallet.setUserId(user.getId());
            userWallet.setBalance(new BigDecimal(0));
            userWallet.setBalanceFrozen(new BigDecimal(0));
            userWallet.setPoint(0L);
            userWallet.setScore(0L);//用户总积分
            userWallet.setAddTime(DateStampUtils.getTimesteamp());
            userWalletMapper.insert(userWallet);
            UserDto userDto = setUserDtoByInfo(token,user);
            return userDto;
        }else{
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userThirdParty.getUserId());
            userInfo.setLastLoginPlatform(3);//最后登录平台 1安卓 2IOS 3小程序
            userInfo.setLastLoginTime(DateStampUtils.getTimesteamp());
            userInfoMapper.updateByPrimaryKey(userInfo);

            registIMUser(userInfo);
            
            String token = setToken("",userInfo.getId());
            userThirdParty.setSessionKey(session_key);
            userThirdPartyMapper.updateByPrimaryKey(userThirdParty);
            UserDto userDto = setUserDtoByInfo(token,userInfo);
            return userDto;
        }
    }

    @Override
    public UserInfo getUserByUnionId(String unionId,Integer type) throws ServiceException {
        UserThirdParty userThirdParty = userThirdPartyMapper.getUserByThirdNoType(unionId,type);
        if(userThirdParty == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userThirdParty.getUserId());
            return userInfo;
        }
    }


    @Override
    public List<ShopDto> shopList(String nickName,Integer type, Integer start, Integer limit,Integer authType) throws ServiceException {
        //分页
        //个人店铺排序：1.会员等级 2.认证店铺 3.信誉度 4.发布商品时间
        List<Map<String, Object>> maps = userInfoMapper.shopList(nickName,start*limit,limit,authType);
        List<ShopDto> shopDtos = ShopDto.toDtoList(maps);
        for(ShopDto shopDto:shopDtos){
            //根据用户ID获取前三个商品
            String goodsLists = schedualGoodsService.goodsList(shopDto.getUserId(), 0, 3);
            System.out.println("goodsLists:"+goodsLists);
            JSONObject jsonObject = JSONObject.parseObject(goodsLists);
            System.out.println("jsonObject:"+jsonObject);
            JSONArray goodsList = JSONArray.parseArray(jsonObject.getString("data"));
            for(int i=0;i<goodsList.size();i++){
                System.out.println("goods:"+goodsList.get(i));
                JSONObject goods = JSONObject.parseObject(goodsList.get(i).toString());
                if(i == 0){
                    shopDto.setImgUrl1(goods.getString("mainUrl"));
                }else if(i == 1){
                    shopDto.setImgUrl2(goods.getString("mainUrl"));
                }else if(i == 2){
                    shopDto.setImgUrl3(goods.getString("mainUrl"));
                }else{
                    throw new ServiceException("");
                }
            }
        }
        return shopDtos;
    }


    @Override
    public UserDto userInfo(String token,Integer userId) throws ServiceException {
        UserInfo userByToken = null;
        if(StringUtils.isNotEmpty(token)){
            userByToken = getUserByToken(token);
        }
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);

        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserDto userDto = setUserDtoByInfo("",userInfo);
            userDto.setFansCount(userFansMapper.fansCount(userId));
            userDto.setCollectCount(userFansMapper.collectCount(userId));
            //判断token用户是否关注userId用户
            if(userByToken != null){
                UserFans userFans = userFansMapper.selectByUserIdToUserId(userByToken.getId(),userId);
                if(userFans != null){
                   userDto.setIsFollow(1);//是否关注 1是 2否
                }
            }
            return userDto;
        }
    }

    @Override
    public void fansFollow(Integer userId, Integer toUserId, Integer type) throws ServiceException {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserInfo toUser = userInfoMapper.selectByPrimaryKey(toUserId);
            if(toUser == null){
                throw new ServiceException("被关注用户不存在！");
            }else{
                UserFans userFans = userFansMapper.selectByUserIdToUserId(userId,toUserId);
                if(type == 0){//关注用户
                    if(userFans != null){
                        throw new ServiceException("已关注，请勿重复关注！");
                    }
                    userFans = new UserFans();
                    userFans.setAddTime(DateStampUtils.getTimesteamp());
                    userFans.setToUserId(toUserId);
                    userFans.setUserId(userId);
                    userFansMapper.insert(userFans);
                    List<UserInfo> list = new ArrayList<>();
                    Iterator<UserInfo> iterator = list.iterator();
                    while(iterator.hasNext()){
                        iterator.remove();
                    }
                }else if(type == 1){//取消关注
                    if(userFans == null){
                        throw new ServiceException("未关注，取消关注失败！");
                    }else{
                        userFansMapper.deleteByPrimaryKey(userFans.getId());
                    }
                }else{
                    throw new ServiceException("状态值错误！");
                }
            }
        }
    }

    @Override
    public List<UserFansDto> myFansOrFollows(Integer userId,Integer type,Integer start,Integer limit) throws ServiceException {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            //分页获取粉丝列表/关注列表
            List<Map<String, Object>> maps = userFansMapper.myFansOrFollows(userId,type,start*limit, limit);
            List<UserFansDto> userFollowsDtos = UserFansDto.toDtoList(maps);
            return userFollowsDtos;
        }
    }

    @Override
    public WaitProcessDto myWaitProcess(Integer userId) throws ServiceException {
        WaitProcessDto waitProcessDto = new WaitProcessDto();
        /**
         * 订单：
         *  我买下的：
         *   待付款+待收货
         *  我卖出的：
         *   待发货+待处理退货
         */
        Integer buy = JSONObject.parseObject(schedualOrderService.getProcess(userId, 1)).getInteger("data");
        waitProcessDto.setBuy(buy);
        Integer sell = JSONObject.parseObject(schedualOrderService.getProcess(userId, 2)).getInteger("data");
        waitProcessDto.setSell(sell);
        /**
         * 市集：
         *  商品：
         *   待处理的议价
         */
        Integer goods = JSONObject.parseObject(schedualGoodsService.getProcess(userId)).getInteger("data");
        waitProcessDto.setGoods(goods);
        return waitProcessDto;
    }

	@Override
	public void registIMUser(UserInfo user) throws ServiceException {
		//判断用户是否已经注册环信
		if(user.getIsRegHx()==null||user.getIsRegHx()==StatusEnum.NO.getCode()) {
	        schedualMessageService.easemobRegist(user.getId().toString(), MD5Util.MD5("xiaofangyuan"+user.getId().toString()));
			user.setIsRegHx(StatusEnum.YES.getCode());
			userInfoMapper.updateByPrimaryKey(user);
		}
	}
}
