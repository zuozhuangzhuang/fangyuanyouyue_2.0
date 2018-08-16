package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo getUserByPhone(String phone);

    UserInfo getUserByNickName(String nickName);

    /**
     * 根据状态获取个人店铺列表
     * @param nickName
     * @param start
     * @param limit
     * @return
     */
    List<Map<String,Object>> shopList(@Param("nickName") String nickName,@Param("start") Integer start,@Param("limit") Integer limit,@Param("authType")Integer authType);

    /**
     * 根据用户名获取用户列表
     * @param nickName
     * @param start
     * @param limit
     * @return
     */
    List<Map<String,Object>> getUserByName(@Param("nickName") String nickName,@Param("start") Integer start,@Param("limit") Integer limit);
}