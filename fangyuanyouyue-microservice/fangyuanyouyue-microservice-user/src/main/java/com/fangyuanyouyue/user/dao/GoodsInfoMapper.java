package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsInfo record);

    int insertSelective(GoodsInfo record);

    GoodsInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsInfo record);

    int updateByPrimaryKeyWithBLOBs(GoodsInfo record);

    int updateByPrimaryKey(GoodsInfo record);

    /**
     * 获取个人店铺列表的商品图片
     * @param userId
     * @return
     */
    List<String> selectShopGoodsImg(@Param("userId")Integer userId);
}