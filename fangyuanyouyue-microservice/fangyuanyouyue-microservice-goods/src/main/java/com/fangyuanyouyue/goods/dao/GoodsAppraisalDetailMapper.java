package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsAppraisalDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsAppraisalDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAppraisalDetail record);

    int insertSelective(GoodsAppraisalDetail record);

    GoodsAppraisalDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsAppraisalDetail record);

    int updateByPrimaryKeyWithBLOBs(GoodsAppraisalDetail record);

    int updateByPrimaryKey(GoodsAppraisalDetail record);

    /**
     * 根据用户ID，商品ID获取鉴定详情
     * @param userId
     * @param goodsId
     * @return
     */
    GoodsAppraisalDetail selectByUserIdGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);

    /**
     * 根据用户ID获取鉴定详情列表
     * @param userId
     * @return
     */
    List<GoodsAppraisalDetail> selectListByUserId(@Param("userId") Integer userId,@Param("orderId")Integer orderId,@Param("start")Integer start,@Param("limit")Integer limit);

    /**
     * 鉴定展示
     * @param start
     * @param limit
     * @return
     */
    List<GoodsAppraisalDetail> getListByStatus(@Param("start")Integer start,@Param("limit")Integer limit);

}