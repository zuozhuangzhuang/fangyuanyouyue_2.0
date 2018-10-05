package com.fangyuanyouyue.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.order.model.Company;

@Mapper
public interface CompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Company record);

    int insertSelective(Company record);

    Company selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Company record);

    int updateByPrimaryKey(Company record);

    /**
     * 获取物流公司列表
     * @return
     */
    List<Company> getList();
    
    
    /**
     * 后台分页总数
     * @param keyword
     * @param status
     * @return
     */
    int countPage(@Param("keyword")String keyword,@Param("status")Integer status);
    
    /**
     * 后台分页数据
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param orders
     * @return
     */
    List<Company> getPage(@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,@Param("orders")String orders,@Param("ascType")Integer ascType);

    
}