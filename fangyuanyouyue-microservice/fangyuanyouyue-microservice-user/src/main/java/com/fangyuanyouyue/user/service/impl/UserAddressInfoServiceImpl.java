package com.fangyuanyouyue.user.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.user.dao.UserAddressInfoMapper;
import com.fangyuanyouyue.user.dao.UserInfoMapper;
import com.fangyuanyouyue.user.dto.UserAddressDto;
import com.fangyuanyouyue.user.model.UserAddressInfo;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.service.UserAddressInfoService;

@Service(value = "userAddressInfoService")
public class UserAddressInfoServiceImpl implements UserAddressInfoService{
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserAddressInfoMapper userAddressInfoMapper;
    @Autowired
    protected RedisTemplate redisTemplate;

    @Override
    public List<UserAddressDto> addAddress(String token, String receiverName, String receiverPhone, String province, String city, String area, String address, String postCode, Integer type) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7, TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserAddressInfo userAddressInfo = new UserAddressInfo();
            userAddressInfo.setUserId(userId);
            userAddressInfo.setAddTime(DateStampUtils.getTimesteamp());
            userAddressInfo.setReceiverName(receiverName);
            userAddressInfo.setReceiverPhone(receiverPhone);
            userAddressInfo.setProvince(province);
            userAddressInfo.setCity(city);
            userAddressInfo.setArea(area);
            userAddressInfo.setAddress(address);
            if(type != null){
                userAddressInfo.setType(type);
            }
            userAddressInfoMapper.insert(userAddressInfo);
            List<UserAddressInfo> userAddressInfos = userAddressInfoMapper.selectAddressByUserId(userId,null);
            List<UserAddressDto> userAddressDtos = UserAddressDto.toDtoList(userAddressInfos);
            return userAddressDtos;
        }
    }

    @Override
    public UserAddressDto updateAddress(String token, Integer addressId, String receiverName, String receiverPhone, String province, String city, String area, String address, String postCode, Integer type) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("此用户不存在！");
        }else{
            UserAddressInfo userAddressInfo = userAddressInfoMapper.selectByPrimaryKey(addressId);
            if(userAddressInfo == null){
                throw new ServiceException("收货地址有误！");
            }else{
                userAddressInfo.setReceiverName(receiverName);
                userAddressInfo.setReceiverPhone(receiverPhone);
                userAddressInfo.setProvince(province);
                userAddressInfo.setCity(city);
                userAddressInfo.setArea(area);
                userAddressInfo.setAddress(address);
                userAddressInfo.setPostCode(postCode);
                userAddressInfo.setType(type);
                userAddressInfoMapper.updateByPrimaryKey(userAddressInfo);
                return new UserAddressDto(userAddressInfo);
            }
        }
    }

    @Override
    public List<UserAddressDto> deleteAddress(String token, Integer addressId) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("此用户不存在！");
        }else{
            UserAddressInfo userAddressInfo = userAddressInfoMapper.selectByPrimaryKey(addressId);
            if(userAddressInfo == null){
                throw new ServiceException("收货地址有误！");
            }else{
                userAddressInfoMapper.deleteByPrimaryKey(addressId);
                List<UserAddressInfo> userAddressInfos = userAddressInfoMapper.selectAddressByUserId(userId,null);
                List<UserAddressDto> userAddressDtos = UserAddressDto.toDtoList(userAddressInfos);
                return userAddressDtos;
            }
        }
    }

    @Override
    public void defaultAddress(String token, Integer addressId) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("此用户不存在！");
        }else{
            //取消旧默认地址
            UserAddressInfo defaultAddress = userAddressInfoMapper.selectDefaultAddressByUserId(userId);
            if(defaultAddress != null){
                defaultAddress.setType(Integer.valueOf(Status.OTHER.getValue()));
                userAddressInfoMapper.updateByPrimaryKey(defaultAddress);
            }
            //设置新默认地址
            UserAddressInfo userAddressInfo = userAddressInfoMapper.selectByPrimaryKey(addressId);
            if(userAddressInfo == null){
                throw new ServiceException("参数错误！");
            }
            userAddressInfo.setType(Integer.valueOf(Status.ISDEFAULT.getValue()));
            userAddressInfoMapper.updateByPrimaryKey(userAddressInfo);
        }
    }

    @Override
    public List<UserAddressDto> getAddressList(String token,Integer addressId) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            List<UserAddressInfo> userAddressInfos = userAddressInfoMapper.selectAddressByUserId(userId,addressId);
            if(addressId != null && userAddressInfos.size() == 0){
                throw new ServiceException("收货地址有误！");
            }
            return UserAddressDto.toDtoList(userAddressInfos);
        }
    }

    @Override
    public UserAddressDto getDefaultAddress(String token) throws ServiceException {
        Integer userId = (Integer)redisTemplate.opsForValue().get(token);
        redisTemplate.expire(token,7,TimeUnit.DAYS);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo == null){
            throw new ServiceException("用户不存在！");
        }else{
            UserAddressInfo defaultAddress = userAddressInfoMapper.selectDefaultAddressByUserId(userId);
            if(defaultAddress == null){
                throw new ServiceException("未设置默认地址！");
            }
            return new UserAddressDto(defaultAddress);
        }
    }
}
