package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserInfo;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * 分页总条数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 分页获取
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @return
     */
    List<UserInfo> getPage(@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);
    
    /**
     * 根据用户名获取用户列表
     * @param nickName
     * @param start
     * @param limit
     * @return
     */
    List<Map<String,Object>> getUserByName(@Param("nickName") String nickName,@Param("start") Integer start,@Param("limit") Integer limit);

    /**
     * 获取所有注册环信用户
     * @return
     */
    List<UserInfo> findAllHxUser();
}