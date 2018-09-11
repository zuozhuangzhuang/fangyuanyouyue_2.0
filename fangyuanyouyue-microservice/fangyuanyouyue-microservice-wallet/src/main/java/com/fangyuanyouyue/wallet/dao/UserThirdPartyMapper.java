package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserThirdParty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserThirdPartyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserThirdParty record);

    int insertSelective(UserThirdParty record);

    UserThirdParty selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserThirdParty record);

    int updateByPrimaryKey(UserThirdParty record);

    /**
     * 根据用户ID获取用户第三方信息
     * @return
     * @param id
     */
    UserThirdParty getUserThirdByUserId(@Param("userId") Integer userId, @Param("type")Integer type);
}