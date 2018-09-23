package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserBehaviorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBehavior record);

    int insertSelective(UserBehavior record);

    UserBehavior selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBehavior record);

    int updateByPrimaryKey(UserBehavior record);

    /**
     * 根据用户id、类型获取用户行为
     * @param userId
     * @param type
     * @return
     */
    List<UserBehavior> selectByUserIdType(@Param("userId")Integer userId, @Param("type")Integer type);
}