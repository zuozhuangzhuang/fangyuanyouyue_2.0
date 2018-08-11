package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.Company;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}