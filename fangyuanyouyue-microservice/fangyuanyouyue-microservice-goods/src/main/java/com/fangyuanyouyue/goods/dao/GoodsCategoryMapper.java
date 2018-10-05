package com.fangyuanyouyue.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.goods.model.GoodsCategory;

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
    * 根据父级分类获取分类列表
    * @param parentId
    * @return
    */
   List<GoodsCategory> getTopCategory();

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

    /**
     * 分页总条数
     * @return
     */
    int countPage(@Param("keyword")String keyword,@Param("status")Integer status,@Param("type")Integer type,@Param("parentId")Integer parentId);
    
    List<GoodsCategory> getPage(@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,@Param("type")Integer type,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType,@Param("parentId")Integer parentId);
    
    

}