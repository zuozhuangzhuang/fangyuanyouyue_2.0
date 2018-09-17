package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserAuthApply;
import com.fangyuanyouyue.user.model.UserInfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAuthApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAuthApply record);

    int insertSelective(UserAuthApply record);

    UserAuthApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAuthApply record);

    int updateByPrimaryKey(UserAuthApply record);

    /**
     * 获取用户官方认证申请
     * @param userId
     * @param status
     * @return
     */
    UserAuthApply selectByUserIdStatus(@Param("userId")Integer userId,@Param("status")Integer status);
    

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
    List<UserAuthApply> getPage(@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);

    /**
     * 获取超时认证店铺
     * @param endTime
     * @param status
     * @return
     */
    List<UserAuthApply> selectByEndTime(@Param("endTime")String endTime,@Param("status")Integer status);
    
}