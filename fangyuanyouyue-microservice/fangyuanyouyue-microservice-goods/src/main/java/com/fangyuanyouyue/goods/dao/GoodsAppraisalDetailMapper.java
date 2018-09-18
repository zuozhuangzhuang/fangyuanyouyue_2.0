package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsAppraisalDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

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
    List<GoodsAppraisalDetail> selectListByUserId(@Param("userId") Integer userId,@Param("orderId")Integer orderId,@Param("start")Integer start,@Param("limit")Integer limit,@Param("status")Integer status);

    /**
     * 鉴定展示
     * @param start
     * @param limit
     * @return
     */
    List<GoodsAppraisalDetail> getListByStatus(@Param("start")Integer start,@Param("limit")Integer limit);

    /**
     * 根据订单ID、status获取鉴定列表
     * @param orderId
     * @param status
     * @return
     */
    List<GoodsAppraisalDetail> getListByOrderIdStatus(@Param("orderId")Integer orderId,@Param("status")Integer status);
    /**
     * 分页总条数
     * @return
     */
    int countPage(@Param("type") Integer type,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 分页获取官方鉴定列表
     * @param type
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<GoodsAppraisalDetail> getAppraisalPage(@Param("type") Integer type,@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,
                                 @Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);
}