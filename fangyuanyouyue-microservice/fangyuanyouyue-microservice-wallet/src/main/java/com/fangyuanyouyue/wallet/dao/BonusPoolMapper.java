package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.BonusPool;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BonusPoolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BonusPool record);

    int insertSelective(BonusPool record);

    BonusPool selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BonusPool record);

    int updateByPrimaryKey(BonusPool record);

    /**
     * 获取奖池所有内容
     * @return
     */
    List<BonusPool> selectAll();
}