package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserBalanceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    List<UserBalanceDetail> selectByUserIdType(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit,@Param("type")Integer type,@Param("searchDate")String searchDate);

    /**
     * 余额账单详情
     * @param userId
     * @param orderNo
     * @return
     */
    UserBalanceDetail selectByUserIdOrderNo(@Param("userId")Integer userId,@Param("orderNo")String orderNo);

    /**
     *
     * @param userId
     * @param date
     * @return
     */
    List<Map<String,Object>> monthlyBalance(@Param("userId")Integer userId,@Param("startDate")String startDate,@Param("endDate")String endDate);


    /**
     * 分页总条数
     * @param payType
     * @param orderType
     * @param type
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("payType")Integer payType,@Param("orderType")Integer orderType,@Param("type")Integer type,@Param("keyword")String keyword, @Param("status")Integer status, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取用户收支信息
     * @param payType
     * @param orderType
     * @param type
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<UserBalanceDetail> getPage(@Param("payType")Integer payType,@Param("orderType")Integer orderType,@Param("type")Integer type,@Param("start") Integer start, @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("status")Integer status, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);
}