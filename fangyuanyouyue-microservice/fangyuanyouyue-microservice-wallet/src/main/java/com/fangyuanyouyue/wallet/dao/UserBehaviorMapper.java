package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserBehaviorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBehavior record);

    int insertSelective(UserBehavior record);

    UserBehavior selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBehavior record);

    int updateByPrimaryKey(UserBehavior record);

    /**
     * 根据用户id行为类型获取行为信息
     * @param userId
     * @param businessId
     * @param businessType 对象类型 1用户 2商品、抢购 3商品、抢购评论 4帖子、视频 5帖子、视频评论 6全民鉴定 7全民鉴定评论
     * @param type 行为类型 1点赞 2关注用户 3评论 4购买抢购
     * @return
     */
    UserBehavior selectByUserIdType(@Param("userId")Integer userId,@Param("businessId") Integer businessId,@Param("businessType") Integer businessType,@Param("type") Integer type);
}