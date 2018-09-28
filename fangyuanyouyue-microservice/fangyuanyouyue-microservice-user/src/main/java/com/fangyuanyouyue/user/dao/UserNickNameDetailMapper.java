package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.UserNickNameDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserNickNameDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserNickNameDetail record);

    int insertSelective(UserNickNameDetail record);

    UserNickNameDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserNickNameDetail record);

    int updateByPrimaryKey(UserNickNameDetail record);

    /**
     * 分页总条数
     * @param id
     * @param keyword
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("id")Integer id,@Param("keyword")String keyword,  @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取
     * @param id
     * @param start
     * @param limit
     * @param keyword
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<UserNickNameDetail> getPage(@Param("id")Integer id,@Param("start") Integer start, @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);

}