package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserAuthApply;
import com.fangyuanyouyue.user.model.UserAuthOrder;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAuthOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAuthOrder record);

    int insertSelective(UserAuthOrder record);

    UserAuthOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAuthOrder record);

    int updateByPrimaryKey(UserAuthOrder record);

    /**
     *
     * @param orderNo
     * @return
     */
    UserAuthOrder selectByOrderNo(@Param("orderNo")String orderNo);
    
    

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
    List<UserAuthOrder> getPage(@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);
    
    
}