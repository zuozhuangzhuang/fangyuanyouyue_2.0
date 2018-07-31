package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserAddressInfo;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAddressInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAddressInfo record);

    int insertSelective(UserAddressInfo record);

    UserAddressInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAddressInfo record);

    int updateByPrimaryKey(UserAddressInfo record);

    List<UserAddressInfo> selectAddressByUserId(@Param("userId") Integer userId,@Param("addressId")Integer addressId);

    UserAddressInfo selectDefaultAddressByUserId(Integer userId);


}