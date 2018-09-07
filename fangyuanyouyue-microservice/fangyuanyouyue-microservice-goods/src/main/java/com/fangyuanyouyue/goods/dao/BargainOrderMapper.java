package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.BargainOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BargainOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BargainOrder record);

    int insertSelective(BargainOrder record);

    BargainOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BargainOrder record);

    int updateByPrimaryKey(BargainOrder record);

    /**
     * 根据订单号查询议价详情
     * @param orderNo
     * @return
     */
    BargainOrder selectByOrderNo(@Param("orderNo")String orderNo);
}