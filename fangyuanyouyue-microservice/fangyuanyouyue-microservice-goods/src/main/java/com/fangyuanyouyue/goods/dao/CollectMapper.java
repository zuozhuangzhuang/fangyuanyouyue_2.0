package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    Collect selectByCollectId(@Param("userId")Integer userId,@Param("collectId") Integer collectId,@Param("type")Integer type);
}