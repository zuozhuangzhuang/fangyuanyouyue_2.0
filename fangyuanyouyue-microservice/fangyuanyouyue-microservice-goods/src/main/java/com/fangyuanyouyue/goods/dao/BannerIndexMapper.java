package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.BannerIndex;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BannerIndexMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BannerIndex record);

    int insertSelective(BannerIndex record);

    BannerIndex selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BannerIndex record);

    int updateByPrimaryKey(BannerIndex record);

    /**
     * 获取首页轮播图列表
     * @return
     */
    List<BannerIndex> getBanner(Integer type);

    /**
     * 分页总条数
     * @return
     */
    int countPage(@Param("keyword")String keyword, @Param("status")Integer status, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取轮播图列表
     * @param type
     * @param jumpType
     * @param businessType
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
    List<BannerIndex> getPage(@Param("type")Integer type,@Param("jumpType")Integer jumpType,@Param("businessType")Integer businessType, @Param("start") Integer start,
                                 @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("status")Integer status,
                                 @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);
}