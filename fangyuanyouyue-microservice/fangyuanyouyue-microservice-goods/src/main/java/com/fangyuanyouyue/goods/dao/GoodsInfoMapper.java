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

    /**
     * 商品详情（包括用户信息）
     * @param id
     * @return
     */
    GoodsInfo selectByPrimaryKeyDetail(Integer id);

    int updateByPrimaryKeySelective(GoodsInfo record);

    int updateByPrimaryKeyWithBLOBs(GoodsInfo record);

    int updateByPrimaryKey(GoodsInfo record);


    /**
     * 获取商品列表
     * @param myId
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
    List<GoodsInfo> getGoodsList(@Param("myId") Integer myId,@Param("userId") Integer userId, @Param("status") Integer status, @Param("search") String search,
                                 @Param("priceMin") BigDecimal priceMin, @Param("priceMax") BigDecimal priceMax,
                                 @Param("synthesize")Integer synthesize, @Param("quality")Integer quality,
                                 @Param("start") Integer start, @Param("limit") Integer limit,@Param("type")Integer type,
                                 @Param("goodsCategoryIds")Integer[] goodsCategoryIds);

    /**
     * 获取抢购列表
     * @param myId
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
    List<GoodsInfo> getAuctionList(@Param("myId") Integer myId,@Param("userId") Integer userId, @Param("status") Integer status, @Param("search") String search,
                                   @Param("priceMin") BigDecimal priceMin, @Param("priceMax") BigDecimal priceMax,
                                   @Param("synthesize")Integer synthesize, @Param("quality")Integer quality,
                                   @Param("start") Integer start, @Param("limit") Integer limit,
                                   @Param("goodsCategoryIds")Integer[] goodsCategoryIds);


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
     * 根据类型和状态获取列表
     * @param type
     * @param status
     * @return
     */
    List<GoodsInfo> selectListByTypeStatus(@Param("type")Integer type,@Param("status")Integer status);

    /**
     * 分页总条数
     * @return
     */
    int countPage(@Param("type")Integer type,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 分页获取商品\抢购列表
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
    List<GoodsInfo> getGoodsPage(@Param("type")Integer type,@Param("start") Integer start,
                            @Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,
                            @Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);

    /**
     * 获取今日上传商品、抢购数量
     * @return
     */
    Integer getTodayGoodsCount();

    /**
     * 获取全部商品、抢购数量
     * @return
     */
    Integer getAllGoodsCount();

    /**
     * 获取本月上传商品、抢购数量
     * @return
     */
//    Integer getMonthGoodsCount();

    /**
     * 获取分享页面店铺内商品列表
     * @param shopId
     * @return
     */
    List<GoodsInfo> getShareGoodsImgs(@Param("shopId")Integer shopId);

    /**
     * 用户上传的商品
     * @param userId
     * @return
     */
    int goodsCount(@Param("userId")Integer userId);

}