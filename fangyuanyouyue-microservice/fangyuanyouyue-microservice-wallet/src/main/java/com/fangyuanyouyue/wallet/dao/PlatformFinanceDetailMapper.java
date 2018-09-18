package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.PlatformFinanceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlatformFinanceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformFinanceDetail record);

    int insertSelective(PlatformFinanceDetail record);

    PlatformFinanceDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformFinanceDetail record);

    int updateByPrimaryKey(PlatformFinanceDetail record);

    /**
     * 分页总条数
     * @param payType
     * @param orderType
     * @param type
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("payType")Integer payType,@Param("orderType")Integer orderType,@Param("type")Integer type,@Param("keyword")String keyword, @Param("status")Integer status, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取平台收支信息
     * @param payType
     * @param orderType
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
    List<PlatformFinanceDetail> getPage(@Param("payType")Integer payType,@Param("orderType")Integer orderType,@Param("type")Integer type,@Param("start") Integer start, @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("status")Integer status, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);
}