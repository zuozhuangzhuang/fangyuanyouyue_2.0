package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.CartInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartInfo record);

    int insertSelective(CartInfo record);

    CartInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CartInfo record);

    int updateByPrimaryKey(CartInfo record);

    CartInfo selectByUserId(Integer userId);
}