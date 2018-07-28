package com.fangyuanyouyue.user.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.dao.IdentityAuthApplyMapper;
import com.fangyuanyouyue.user.dao.UserAddressInfoMapper;
import com.fangyuanyouyue.user.dao.UserFansMapper;
import com.fangyuanyouyue.user.dao.UserInfoExtMapper;
import com.fangyuanyouyue.user.dao.UserInfoMapper;
import com.fangyuanyouyue.user.dao.UserThirdPartyMapper;
import com.fangyuanyouyue.user.dao.UserVipMapper;
import com.fangyuanyouyue.user.dto.ShopDto;
import com.fangyuanyouyue.user.dto.UserDto;
import com.fangyuanyouyue.user.model.IdentityAuthApply;
import com.fangyuanyouyue.user.model.UserFans;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.UserInfoExt;
import com.fangyuanyouyue.user.model.UserThirdParty;
import com.fangyuanyouyue.user.model.UserVip;
import com.fangyuanyouyue.user.param.UserParam;
import com.fangyuanyouyue.user.service.SchedualGoodsService;
import com.fangyuanyouyue.user.service.UserInfoService;

@Service(value = "userInfoService")
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
    private UserAddressInfoMapper userAddressInfoMapper;
    @Autowired
    private UserVipMapper userVipMapper;
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    private SchedualGoodsService schedualGoodsService;
    @Autowired
    private UserFansMapper userFansMapper;

    @Override
    public UserInfo getUserByToken(String token) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        return userInfo;
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
        //TODO 信誉度待定
        userInfoExt.setCredit(100);//信誉度
        userInfoExt.setScore(0);//用户积分
        userInfoExt.setAuthType(2);//认证状态 1已认证 2未认证
        userInfoExt.setAddTime(DateStampUtils.getTimesteamp());
        userInfoExtMapper.insert(userInfoExt);
        //用户会员系统
        UserVip userVip = new UserVip();
        userVip.setUserId(user.getId());
        userVip.setStatus(2);//会员状态1已开通 2未开通
        userVip.setAddTime(DateStampUtils.getTimesteamp());
        userVipMapper.insert(userVip);
        //TODO 注册通讯账户
        //TODO 调用钱包系统初始化接口
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
            //TODO 信誉度待定
            userInfoExt.setCredit(100);
            userInfoExt.setScore(0);
            userInfoExt.setAddTime(DateStampUtils.getTimesteamp());
            userInfoExtMapper.insert(userInfoExt);
            //用户会员系统
            UserVip userVip = new UserVip();
            userVip.setUserId(user.getId());
            userVip.setStatus(2);//会员状态1已开通 2未开通
            userVip.setAddTime(DateStampUtils.getTimesteamp());
            userVipMapper.insert(userVip);
            //TODO 注册通讯账户
            //TODO 调用钱包系统初始化接口

            UserDto userDto = setUserDtoByInfo(token,user);
            return userDto;
        }else{
            //记录用户登录时间，登录平台，最后一次登录
            log.info("三方登录");
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userThirdParty.getUserId());
            userInfo.setLastLoginTime(DateStampUtils.getTimesteamp());
            userInfo.setLastLoginPlatform(param.getLoginPlatform());
            userInfoMapper.updateByPrimaryKey(userInfo);
            String token = setToken("",userInfo.getId());
            UserDto userDto = setUserDtoByInfo(token,userInfo);
            return userDto;
        }
    }

    @Override
    public UserDto thirdBind(String token,String unionId,Integer type) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        //根据用户ID获取用户，生成新的三方登陆信息
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserThirdParty userThirdParty = userThirdPartyMapper.getUserByThirdNoType(unionId,type);
            //校验是否绑定三方账号
            if(userThirdParty != null){
                if(userThirdParty.getUserId() == userId){
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
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        updatePwd(newPwd, userInfo);
    }

    @Override
    public UserDto modify(UserParam param) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(param.getToken());
        redisTemplate.expire(param.getToken(),7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
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
            UserInfoExt userInfoExt = userInfoExtMapper.selectByUserId(userId);
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
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
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
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
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
            IdentityAuthApply identityAuthApply = identityAuthApplyMapper.selectByUserId(user.getId());
            UserDto userDto = new UserDto(token,user,userVip,userInfoExt,identityAuthApply);
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
            //TODO 信誉度待定
            userInfoExt.setCredit(100);
            userInfoExt.setScore(0);
            userInfoExt.setAddTime(DateStampUtils.getTimesteamp());
            userInfoExtMapper.insert(userInfoExt);
            //用户会员系统
            UserVip userVip = new UserVip();
            userVip.setUserId(user.getId());
            userVip.setStatus(2);//会员状态 1已开通 2未开通
            userVip.setAddTime(DateStampUtils.getTimesteamp());
            userVipMapper.insert(userVip);
            //TODO 注册通讯账户
            //TODO 调用钱包系统初始化接口
            UserDto userDto = setUserDtoByInfo(token,user);
            return userDto;
        }else{
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userThirdParty.getUserId());
            userInfo.setLastLoginPlatform(3);//最后登录平台 1安卓 2IOS 3小程序
            userInfo.setLastLoginTime(DateStampUtils.getTimesteamp());
            userInfoMapper.updateByPrimaryKey(userInfo);
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

    /**
     * 设置token
     * @param token
     * @param userId
     */
    private String setToken(String token,Integer userId){
        //生成用户token，存到Redis
        token = 10000+userId+"FY"+DateStampUtils.getGMTUnixTimeByCalendar()+"";
        redisTemplate.opsForValue().set(token,userId);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        if(redisTemplate.opsForValue().get(userId) != null){
            //更新userId:token的value，同时使旧token失效
            redisTemplate.opsForValue().set(redisTemplate.opsForValue().get(userId),null);
        }
        //存入userId:token，用来在更新token时确保旧token失效，时效7天
        redisTemplate.opsForValue().set(userId,token);
        redisTemplate.expire(userId,7,TimeUnit.DAYS);
        return token;
    }

    @Override
    public List<ShopDto> shopList(String nickName,Integer type, Integer start, Integer limit) throws ServiceException {
        //分页
        //个人店铺排序：1.会员等级 2.认证店铺 3.信誉度 4.发布商品时间
        List<Map<String, Object>> maps = userInfoMapper.shopList(nickName,start*limit,limit);
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

//    public static void main(String[] args) {
//        SchedualGoodsService schedualGoodsService = new SchedualGoodsServiceImpl();
//        String goodsLists = schedualGoodsService.goodsList(16, 0, 3);
//        System.out.println("goodsLists:"+goodsLists);
//        JSONObject jsonObject = JSONObject.parseObject(goodsLists);
//        System.out.println("jsonObject:"+jsonObject);
//        JSONObject goodsList = JSONArray.parseObject(jsonObject.getString("data"));
//        System.out.println("goodsList:"+goodsList);
//    }

    @Override
    public UserDto userInfo(String token,Integer userId) throws ServiceException {
        Integer userIdByToken = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserDto userDto = setUserDtoByInfo("",userInfo);
            userDto.setFansCount(userFansMapper.fansCount(userId));
            userDto.setCollectCount(userFansMapper.collectCount(userId));
            //判断token用户是否关注userId用户
            UserFans userFans = userFansMapper.selectByUserIdToUserId(userIdByToken, userId);
            if(userFans != null){
               userDto.setIsFollow(1);//是否关注 1是 2否
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


}
