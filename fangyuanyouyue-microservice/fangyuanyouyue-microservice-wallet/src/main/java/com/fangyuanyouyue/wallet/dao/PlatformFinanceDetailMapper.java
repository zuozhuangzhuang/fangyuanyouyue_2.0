package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.PlatformFinanceDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlatformFinanceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformFinanceDetail record);

    int insertSelective(PlatformFinanceDetail record);

    PlatformFinanceDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformFinanceDetail record);

    int updateByPrimaryKey(PlatformFinanceDetail record);
}