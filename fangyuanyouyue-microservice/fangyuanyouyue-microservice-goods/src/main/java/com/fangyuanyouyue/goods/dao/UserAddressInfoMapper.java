package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.UserAddressInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAddressInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAddressInfo record);

    int insertSelective(UserAddressInfo record);

    UserAddressInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAddressInfo record);

    int updateByPrimaryKey(UserAddressInfo record);
}