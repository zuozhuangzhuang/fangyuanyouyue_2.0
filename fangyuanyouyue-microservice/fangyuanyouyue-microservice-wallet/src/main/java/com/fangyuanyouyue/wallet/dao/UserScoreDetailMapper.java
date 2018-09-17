package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserScoreDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserScoreDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserScoreDetail record);

    int insertSelective(UserScoreDetail record);

    UserScoreDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserScoreDetail record);

    int updateByPrimaryKey(UserScoreDetail record);
}