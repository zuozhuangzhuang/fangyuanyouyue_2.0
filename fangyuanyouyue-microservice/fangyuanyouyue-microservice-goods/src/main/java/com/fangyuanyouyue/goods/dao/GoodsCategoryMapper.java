package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);

    /**
     * 获取父级分类
     * @param type
     * @return
     */
    List<GoodsCategory> categoryParentList(Integer type);

    /**
     * 根据父级分类获取分类列表
     * @param parentId
     * @return
     */
    List<GoodsCategory> getChildCategoryList(Integer parentId);

    /**
     * 根据ID修改热度
     * @param goodsCategoryIds
     */
    void addSearchCountByCategoryIds(@Param("goodsCategoryIds") Integer[] goodsCategoryIds);

    /**
     * 热门分类列表
     * @return
     */
    List<GoodsCategory> getHotCategaryList();

    /**
     * 根据分类id获取父级分类id
     * @param categoryId
     * @return
     */
    Integer selectParentId(@Param("categoryId")Integer categoryId);
}