package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.Collect;

@Mapper
public interface CollectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collect record);

    int insertSelective(Collect record);

    Collect selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collect record);

    int updateByPrimaryKey(Collect record);


    /**
     * 根据类型和ID获取收藏记录
     * @param userId
     * @param collectId
     * @param type
     * @return
     */
    Collect selectByCollectId(@Param("userId")Integer userId, @Param("collectId") Integer collectId, @Param("type")Integer type);

    /**
     * 获取收藏信息
     * @param userId 用户id
     * @param collectId 收藏对象id
     * @param collectType 关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏（只有抢购可以关注）
     * @return
     */
    Collect selectByCollectIdType(@Param("userId")Integer userId,@Param("collectId") Integer collectId,@Param("collectType")Integer collectType);
}