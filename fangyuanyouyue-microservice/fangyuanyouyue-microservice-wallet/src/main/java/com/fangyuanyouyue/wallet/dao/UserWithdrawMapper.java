package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserBalanceDetail;
import com.fangyuanyouyue.wallet.model.UserWithdraw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserWithdrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserWithdraw record);

    int insertSelective(UserWithdraw record);

    UserWithdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserWithdraw record);

    int updateByPrimaryKey(UserWithdraw record);


    /**
     *
     * @param userId
     * @param date
     * @return
     */
    List<Map<String,Object>> monthlyBalance(@Param("userId")Integer userId, @Param("startDate")String startDate, @Param("endDate")String endDate);


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
    int countPage(@Param("payType")Integer payType,@Param("status")Integer status,@Param("keyword")String keyword, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取用户提现信息
     * @param payType
     * @param status
     * @param start
     * @param limit
     * @param keyword
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<UserWithdraw> getPage(@Param("payType")Integer payType, @Param("status")Integer status, @Param("start") Integer start, @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);
}