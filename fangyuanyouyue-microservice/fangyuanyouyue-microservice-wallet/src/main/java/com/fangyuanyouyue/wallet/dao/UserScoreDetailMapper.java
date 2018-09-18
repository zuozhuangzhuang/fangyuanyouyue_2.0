package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserScoreDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserScoreDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserScoreDetail record);

    int insertSelective(UserScoreDetail record);

    UserScoreDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserScoreDetail record);

    int updateByPrimaryKey(UserScoreDetail record);

    /**
     * 获取用户当天积分收入
     * @param userId
     * @return
     */
    Long getUserScoreByDay(@Param("userId")Integer userId);

    /**
     * 获取当天用户积分账单
     * @param userId
     * @return
     */
    List<UserScoreDetail> selectByUserId(@Param("userId")Integer userId);
}