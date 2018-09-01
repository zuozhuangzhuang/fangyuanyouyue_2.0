package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserBalanceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserBalanceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBalanceDetail record);

    int insertSelective(UserBalanceDetail record);

    UserBalanceDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBalanceDetail record);

    int updateByPrimaryKey(UserBalanceDetail record);

    /**
     * 余额账单列表
     * @param userId
     * @param start
     * @param limit
     * @param type
     * @param searchDate
     * @return
     */
    List<UserBalanceDetail> selectByUserIdType(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit,@Param("type")Integer type,@Param("searchDate")Date searchDate);

    /**
     * 余额账单详情
     * @param userId
     * @param orderNo
     * @return
     */
    UserBalanceDetail selectByUserIdOrderNo(@Param("userId")Integer userId,@Param("orderNo")String orderNo);
}