package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserAuthApply;
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
}