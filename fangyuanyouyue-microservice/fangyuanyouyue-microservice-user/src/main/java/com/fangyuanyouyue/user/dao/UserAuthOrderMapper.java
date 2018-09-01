package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserAuthOrder;
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
}