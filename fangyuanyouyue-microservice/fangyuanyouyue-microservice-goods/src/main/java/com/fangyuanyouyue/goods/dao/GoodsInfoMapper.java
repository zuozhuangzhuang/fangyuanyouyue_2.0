package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 分页获取商品/抢购列表
     * @param userId
     * @param status
     * @param search
     * @param priceMin
     * @param priceMax
     * @param synthesize
     * @param quality
     * @param start
     * @param limit
     * @param type
     * @param goodsCategoryIds
     * @return
     */
    List<GoodsInfo> getGoodsList(@Param("userId") Integer userId, @Param("status") Integer status, @Param("search") String search,
                                 @Param("priceMin") BigDecimal priceMin, @Param("priceMax") BigDecimal priceMax,
                                 @Param("synthesize")Integer synthesize, @Param("quality")Integer quality,
                                 @Param("start") Integer start, @Param("limit") Integer limit, @Param("type")Integer type,
                                 @Param("goodsCategoryIds")Integer[] goodsCategoryIds);

    /**
     * 根据商品ID集合获取商品
     * @param goodsIds
     * @return
     */
//    List<GoodsInfo> getGoodsByGoodsIds(@Param("goodsIds") Set<Integer> goodsIds, int pageNum, int pageSize);

    /**
     * 根据当前时间获取需要降价的抢购列表
     * @param nowTime
     * @return
     */
    List<GoodsInfo> selectGoodsByIntervalTime(@Param("nowTime") Long nowTime);

    /**
     * 获取我收藏/关注的商品、抢购列表
     * @param userId
     * @param collectType
     * @param type
     * @param goodsId
     * @param start
     * @param limit
     * @param search
     * @return
     */
    List<GoodsInfo> selectMyCollectGoods(@Param("userId")Integer userId,@Param("collectType")Integer collectType,@Param("type")Integer type,@Param("goodsId")Integer goodsId,@Param("start")Integer start,@Param("limit")Integer limit,@Param("search")String search);

    /**
     * 根据商品所属分类列表获取商品列表
     * @param goodsCategoryIds
     * @param start
     * @param limit
     * @return
     */
    List<GoodsInfo> selectByCategoryIds(@Param("goodsCategoryIds")List<Integer> goodsCategoryIds,@Param("start")Integer start,@Param("limit")Integer limit);

    /**
     * 获取商品所在店铺是否官方认证
     * @param goodsId
     * @return
     */
    Map<String,Object> getGoodsUserInfoExtAndVip(@Param("goodsId")Integer goodsId);

    /**
     * 获取卖家粉丝数
     * @param goodsId
     * @return
     */
    Integer getGoodsUserFansCount(Integer goodsId);

    /**
     * 获取卖家关注数
     * @param goodsId
     * @return
     */
    Integer getGoodsUserCollectCount(Integer goodsId);

    /**
     * 根据用户ID获取商品列表
     * @param userId
     * @param start
     * @param limit
     * @return
     */
    List<GoodsInfo> selectGoodsByUserId(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit);

    /**
     * 根据类型和状态获取列表
     * @param type
     * @param status
     * @return
     */
    List<GoodsInfo> selectListByTypeStatus(@Param("type")Integer type,@Param("status")Integer status);
}