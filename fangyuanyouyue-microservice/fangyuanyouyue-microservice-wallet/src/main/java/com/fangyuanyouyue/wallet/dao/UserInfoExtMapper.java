package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserInfoExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoExtMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfoExt record);

    int insertSelective(UserInfoExt record);

    UserInfoExt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfoExt record);

    int updateByPrimaryKey(UserInfoExt record);

    /**
     * 根据用户信息获取商品扩展表信息
     * @param userId
     * @return
     */
    UserInfoExt selectUserInfoExtByUserId(@Param("userId")Integer userId);
}