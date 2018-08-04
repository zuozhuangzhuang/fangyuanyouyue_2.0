package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserFans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserFansMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFans record);

    int insertSelective(UserFans record);

    UserFans selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFans record);

    int updateByPrimaryKey(UserFans record);

    /**
     * 根据用户ID和被关注用户ID获取关注信息
     * @param userId
     * @param toUserId
     * @return
     */
    UserFans selectByUserIdToUserId(@Param("userId")Integer userId,@Param("toUserId")Integer toUserId);

    /**
     * 获取粉丝数量
     * @param userId
     * @return
     */
    Integer fansCount(Integer userId);

    /**
     * 获取关注数量
     * @param userId
     * @return
     */
    Integer collectCount(Integer userId);

    /**
     * 我的关注及被关注用户信息
     * @param userId
     * @param start
     * @param limit
     * @return
     */
    List<Map<String,Object>> myFansOrFollows(@Param("userId")Integer userId,@Param("type")Integer type,@Param("start")Integer start,@Param("limit")Integer limit);

    /**
     * 我的粉丝
     * @param userId
     * @param start
     * @param limit
     * @return
     */
//    List<UserFans> myFans(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit);
}