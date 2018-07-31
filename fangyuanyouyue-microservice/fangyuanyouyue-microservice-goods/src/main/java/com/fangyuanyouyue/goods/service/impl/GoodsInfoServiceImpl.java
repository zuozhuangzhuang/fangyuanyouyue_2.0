package com.fangyuanyouyue.goods.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.BannerIndexMapper;
import com.fangyuanyouyue.goods.dao.CollectMapper;
import com.fangyuanyouyue.goods.dao.GoodsCategoryMapper;
import com.fangyuanyouyue.goods.dao.GoodsCommentMapper;
import com.fangyuanyouyue.goods.dao.GoodsCorrelationMapper;
import com.fangyuanyouyue.goods.dao.GoodsImgMapper;
import com.fangyuanyouyue.goods.dao.GoodsInfoMapper;
import com.fangyuanyouyue.goods.dao.GoodsQuickSearchMapper;
import com.fangyuanyouyue.goods.dao.HotSearchMapper;
import com.fangyuanyouyue.goods.dao.ReportGoodsMapper;
import com.fangyuanyouyue.goods.dto.BannerIndexDto;
import com.fangyuanyouyue.goods.dto.GoodsCategoryDto;
import com.fangyuanyouyue.goods.dto.GoodsCommentDto;
import com.fangyuanyouyue.goods.dto.GoodsDto;
import com.fangyuanyouyue.goods.dto.GoodsQuickSearchDto;
import com.fangyuanyouyue.goods.dto.SearchDto;
import com.fangyuanyouyue.goods.model.BannerIndex;
import com.fangyuanyouyue.goods.model.Collect;
import com.fangyuanyouyue.goods.model.GoodsCategory;
import com.fangyuanyouyue.goods.model.GoodsCorrelation;
import com.fangyuanyouyue.goods.model.GoodsImg;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.model.GoodsQuickSearch;
import com.fangyuanyouyue.goods.model.HotSearch;
import com.fangyuanyouyue.goods.model.ReportGoods;
import com.fangyuanyouyue.goods.model.UserInfo;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.GoodsInfoService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import com.github.pagehelper.PageHelper;

@Service(value = "goodsInfoService")
public class GoodsInfoServiceImpl implements GoodsInfoService{
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private GoodsCorrelationMapper goodsCorrelationMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Autowired
    private GoodsCommentMapper goodsCommentMapperl;
    @Autowired
    private HotSearchMapper hotSearchMapper;
    @Autowired
    private BannerIndexMapper bannerIndexMapper;
    @Autowired
    private GoodsQuickSearchMapper goodsQuickSearchMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private ReportGoodsMapper reportGoodsMapper;
    @Override
    public GoodsInfo selectByPrimaryKey(Integer id) {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(id);
        if(goodsInfo == null){
            throw new SecurityException("获取商品失败！");
        }
        return goodsInfo;
    }


    @Override
    public String goodsMainImg(Integer goodsId) throws ServiceException {
        List<GoodsImg> imgsByGoodsId = goodsImgMapper.getImgsByGoodsId(goodsId);
        if(imgsByGoodsId != null){
            for(GoodsImg goodsImg:imgsByGoodsId){
                if(goodsImg.getType() == 1){
                    return goodsImg.getImgUrl();
                }
            }
        }
        throw new ServiceException("获取商品主图失败！");
    }

    @Override
    public List<GoodsDto> getGoodsInfoList(GoodsParam param) throws ServiceException{
        if(StringUtils.isNotEmpty(param.getSearch())){
            HotSearch hotSearch = hotSearchMapper.selectByName(param.getSearch());
            if(hotSearch == null){
                hotSearch = new HotSearch();
                hotSearch.setAddTime(DateStampUtils.getTimesteamp());
                hotSearch.setName(param.getSearch());
                hotSearch.setCount(1);
                hotSearchMapper.insert(hotSearch);
            }else{
                hotSearch.setCount(hotSearch.getCount()+1);
                hotSearchMapper.updateByPrimaryKey(hotSearch);
            }
        }
        //分页
//        PageHelper.startPage(param.getStart(), param.getLimit());
        List<GoodsInfo> goodsInfos =goodsInfoMapper.getGoodsList(param.getUserId(),param.getStatus(),param.getSearch(),
                param.getPriceMin(),param.getPriceMax(),param.getSynthesize(),param.getQuality(),param.getStart()*param.getLimit(),param.getLimit(),param.getType(),param.getGoodsCategoryIds());
//        PageResults list = new PageResults();
//        list.setPageSize(param.getStart());
//        list.setResults(goodsInfos);
//        list.setCurrentPage(param.getStart());
        //分类热度加一
        if(param.getGoodsCategoryIds() != null && param.getGoodsCategoryIds().length>0){
            goodsCategoryMapper.addSearchCountByCategoryIds(param.getGoodsCategoryIds());
        }
        List<GoodsDto> goodsDtos = new ArrayList<>();
        for (GoodsInfo goodsInfo:goodsInfos) {
            goodsDtos.add(setDtoByGoodsInfo(goodsInfo));
        }
        return goodsDtos;
    }

    @Override
    public GoodsDto addGoods(Integer userId,String nickName,GoodsParam param) throws ServiceException {
        //商品表 goods_info
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setUserId(userId);
        goodsInfo.setName(param.getGoodsInfoName());
        goodsInfo.setDescription(param.getDescription());
        goodsInfo.setPrice(param.getPrice());
        goodsInfo.setPostage(param.getPostage());
        //排序：是否置顶
//        goodsInfo.setSort(param.getSort());
        if(StringUtils.isNotEmpty(param.getLabel())){
            goodsInfo.setLabel(param.getLabel());
        }
        goodsInfo.setType(param.getType());
        goodsInfo.setStatus(1);//状态 1出售中 2 已售出 5删除
        goodsInfo.setAddTime(DateStampUtils.getTimesteamp());
        if(param.getType() == 2){
            goodsInfo.setFloorPrice(param.getFloorPrice());
            goodsInfo.setIntervalTime(param.getIntervalTime());
            goodsInfo.setMarkdown(param.getMarkdown());
        }
        goodsInfoMapper.insert(goodsInfo);

        //初始化商品分类关联表
        for(int i=0;i<param.getGoodsCategoryIds().length;i++){
            GoodsCorrelation goodsCorrelation = new GoodsCorrelation();
            goodsCorrelation.setGoodsId(goodsInfo.getId());
            goodsCorrelation.setAddTime(DateStampUtils.getTimesteamp());
            goodsCorrelation.setGoodsCategoryId(param.getGoodsCategoryIds()[i]);
            goodsCorrelationMapper.insert(goodsCorrelation);
        }
        //商品图片表 goods_img
        //每个图片储存一条商品图片表信息
        for(int i=0;i<param.getImgUrls().length;i++){
            if(i == 0){
                saveGoodsPicOne(goodsInfo.getId(),param.getImgUrls()[i],param.getType(),1);
            }else{
                saveGoodsPicOne(goodsInfo.getId(),param.getImgUrls()[i],param.getType(),2);
            }
        }
        return setDtoByGoodsInfo(goodsInfo);
    }

    /**
     * 给GoodsDto赋值
     * @param goodsInfo
     * @return
     * @throws ServiceException
     */
    private GoodsDto setDtoByGoodsInfo(GoodsInfo goodsInfo) throws ServiceException{
        if(goodsInfo == null){
            throw new ServiceException("获取商品失败！");
        }else{
            List<GoodsImg> goodsImgs = goodsImgMapper.getImgsByGoodsId(goodsInfo.getId());
            List<GoodsCorrelation> goodsCorrelations = goodsCorrelationMapper.getCorrelationsByGoodsId(goodsInfo.getId());
            //按照先后顺序获取评论
            List<Map<String, Object>> maps = goodsCommentMapperl.selectMapByGoodsIdCommentId(null,goodsInfo.getId(), 0, 3);
            List<GoodsCommentDto> goodsCommentDtos = GoodsCommentDto.mapToDtoList(maps);
            for(GoodsCommentDto goodsCommentDto:goodsCommentDtos){
                Map<String, Object> map = goodsCommentMapperl.selectByCommentId(goodsCommentDto.getCommentId());
                if(map != null){
                    goodsCommentDto.setToUserId((Integer)map.get("user_id"));
                    goodsCommentDto.setToUserHeadImgUrl((String)map.get("head_img_url"));
                    goodsCommentDto.setToUserName((String)map.get("nick_name"));
                }
            }

            //获取卖家信息
            UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(goodsInfo.getUserId())).getString("data")), UserInfo.class);
            GoodsDto goodsDto = new GoodsDto(user,goodsInfo,goodsImgs,goodsCorrelations,goodsCommentDtos);
            goodsDto.setCommentCount(goodsCommentMapperl.selectCount(goodsInfo.getId()));
            return goodsDto;
        }
    }

    /**
     * 添加商品图片路径
     * @param goodsId
     * @param type
     * @param sort
     * @throws ServiceException
     */
    private void saveGoodsPicOne(Integer goodsId, String imgUrl, Integer type,Integer sort) throws ServiceException{
        GoodsImg goodsImg = new GoodsImg();
        goodsImg.setAddTime(DateStampUtils.getTimesteamp());
        goodsImg.setGoodsId(goodsId);
        goodsImg.setType(type);//类型 1主图 2次图
        goodsImg.setSort(sort);
        goodsImg.setImgUrl(imgUrl);
        goodsImgMapper.insert(goodsImg);
    }

    @Override
    public void deleteGoods(Integer[] goodsIds) throws ServiceException {
        //TODO 如果商品存在用户议价，取消所有议价并返还用户余额？
        //TODO 删除商品所有评论
        //批量修改商品状态为删除
        for(Integer goodsId:goodsIds){
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            if(goodsInfo == null){
                throw new ServiceException("商品状态错误！");
            }else{
                goodsInfo.setStatus(5);//状态 普通商品 1出售中 2已售出 5删除
                goodsInfoMapper.updateByPrimaryKey(goodsInfo);
            }
        }
    }

    @Override
    public void modifyGoods(GoodsParam param) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(param.getGoodsId());
        if(goodsInfo == null){
            throw new ServiceException("商品状态错误！");
        }else{
            if(goodsInfo.getStatus() != 1){
                throw new ServiceException("商品已下架或已售出！");
            }
            //修改商品信息
            if(StringUtils.isNotEmpty(param.getGoodsInfoName())){
                goodsInfo.setName(param.getGoodsInfoName());
            }
            if(StringUtils.isNotEmpty(param.getDescription())){
                goodsInfo.setDescription(param.getDescription());
            }
            if(param.getPrice() != null){
                goodsInfo.setPrice(param.getPrice());
            }
            if(param.getPostage() != null){
                goodsInfo.setPostage(param.getPostage());
            }
            if(StringUtils.isNotEmpty(param.getLabel())){
                goodsInfo.setLabel(param.getLabel());
            }
//            goodsInfo.setSort(param.getSort());
            if(param.getFloorPrice() != null){
                goodsInfo.setFloorPrice(param.getFloorPrice());
            }
            if(param.getIntervalTime() != null){
                goodsInfo.setIntervalTime(param.getIntervalTime());
            }
            if(param.getMarkdown() != null){
                goodsInfo.setMarkdown(param.getMarkdown());
            }
            if(StringUtils.isNotEmpty(param.getVideoUrl())){
                goodsInfo.setVideoUrl(param.getVideoUrl());
            }

            //删除旧商品图片信息
            goodsImgMapper.deleteByGoodsId(goodsInfo.getId());
            //新增商品图片信息
            //每个图片储存一条商品图片表信息
            for(int i=0;i<param.getImgUrls().length;i++){
                if(i == 0){
                    saveGoodsPicOne(goodsInfo.getId(),param.getImgUrls()[i],param.getType(),1);
                }else{
                    saveGoodsPicOne(goodsInfo.getId(),param.getImgUrls()[i],param.getType(),2);
                }
            }
        }
    }

    @Override
    public List<GoodsCategoryDto> categoryList() throws ServiceException{
        List<GoodsCategory> goodsCategories = goodsCategoryMapper.categoryParentList(null);
        List<GoodsCategoryDto> categoryDtos = GoodsCategoryDto.toDtoList(goodsCategories);
        for(GoodsCategoryDto categoryDto:categoryDtos){
            categoryDto.setChildList(GoodsCategoryDto.toDtoList(goodsCategoryMapper.getChildCategoryList(categoryDto.getCategoryId())));
        }
        return categoryDtos;
    }

    @Override
    public GoodsDto goodsInfoByToken(Integer goodsId, Integer userId) throws ServiceException {
        List<GoodsInfo> goodsInfos = goodsInfoMapper.selectMyCollectGoods(userId, null, null, goodsId);
        GoodsInfo goodsInfo;
        GoodsDto goodsDto;
        //是否收藏/关注 1未关注未收藏（商品/抢购） 2已关注未收藏(抢购) 3未关注已收藏（商品/抢购） 4已关注已收藏(抢购)
        if(goodsInfos != null && goodsInfos.size() > 0){
            goodsDto = setDtoByGoodsInfo(goodsInfos.get(0));
            if(goodsInfos.size()>1){
                goodsDto.setIsCollect(4);
            }else{
                //判断是收藏还是关注
                Collect collect = collectMapper.selectByGoodsId(goodsId, null);
                if(collect.getType() == 1){//类型 1关注 2收藏
                    goodsDto.setIsCollect(2);
                }else{
                    goodsDto.setIsCollect(3);
                }
            }
        }else{
            goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            goodsDto = setDtoByGoodsInfo(goodsInfo);
            goodsDto.setIsCollect(1);
        }
        //是否官方认证
        Map<String, Object> goodsUserInfoExtAndVip = goodsInfoMapper.getGoodsUserInfoExtAndVip(goodsId);
        goodsDto.setAuthType((Integer)goodsUserInfoExtAndVip.get("auth_type"));
        goodsDto.setVipLevel((Integer)goodsUserInfoExtAndVip.get("vip_level"));
        goodsDto.setCredit((Integer)goodsUserInfoExtAndVip.get("credit"));
        //卖家信息
        //粉丝数
        goodsDto.setFansCount(goodsInfoMapper.getGoodsUserFansCount(goodsId));
        //关注数
        goodsDto.setCollectCount(goodsInfoMapper.getGoodsUserCollectCount(goodsId));


        return goodsDto;
    }

    @Override
    public GoodsDto goodsInfo(Integer goodsId) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        if(goodsInfo == null){
            throw new ServiceException("获取商品失败！");
        }
        GoodsDto goodsDto = setDtoByGoodsInfo(goodsInfo);
        //是否官方认证
        Map<String, Object> goodsUserInfoExtAndVip = goodsInfoMapper.getGoodsUserInfoExtAndVip(goodsId);
        if(goodsUserInfoExtAndVip != null){
            goodsDto.setAuthType((Integer)goodsUserInfoExtAndVip.get("auth_type"));
            goodsDto.setVipLevel((Integer)goodsUserInfoExtAndVip.get("vip_level"));
            goodsDto.setCredit((Integer)goodsUserInfoExtAndVip.get("credit"));
        }
        //卖家信息
        //粉丝数
        goodsDto.setFansCount(goodsInfoMapper.getGoodsUserFansCount(goodsId));
        //关注数
        goodsDto.setCollectCount(goodsInfoMapper.getGoodsUserCollectCount(goodsId));
        return goodsDto;
    }

    @Override
    public List<GoodsDto> similarGoods(Integer goodsId,Integer pageNum, Integer pageSize) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        //根据商品
        if(goodsInfo == null){
            throw new ServiceException("获取商品失败！");
        }else{
            //根据商品获取此商品的所属的分类列表
            //按照会员和认证店铺进行排序
            List<Integer> goodsCategoryIds = goodsCorrelationMapper.selectCategoryIdByGoodsId(goodsId);
            //根据分类列表获取商品的列表
            List<GoodsInfo> goodsInfos = goodsInfoMapper.selectByCategoryIds(goodsCategoryIds,pageNum*pageSize,pageSize);
            //获取商品的分类集合
            List<GoodsDto> goodsDtos = new ArrayList<>();
            for(GoodsInfo model:goodsInfos){
                goodsDtos.add(setDtoByGoodsInfo(model));
            }
            return goodsDtos;
        }
    }

    @Override
    public List<BannerIndexDto> getBanner(Integer type) throws ServiceException {
        List<BannerIndex> banners = bannerIndexMapper.getBanner(type);
        List<BannerIndexDto> bannerIndexDtos = BannerIndexDto.toDtoList(banners);
        return bannerIndexDtos;
    }

    @Override
    public BannerIndex addBanner(GoodsParam param) throws ServiceException {
        BannerIndex bannerIndex = new BannerIndex();
        bannerIndex.setAddTime(DateStampUtils.getTimesteamp());
        bannerIndex.setType(param.getType());
        bannerIndex.setBusinessId(param.getBusinessId());
        bannerIndex.setImgUrl(param.getImgUrl());
        bannerIndex.setJumpType(param.getJumpType());
        if(StringUtils.isNotEmpty(param.getTitle())){
            bannerIndex.setTitle(param.getTitle());
        }
        if(param.getSort() != null){
            bannerIndex.setSort(param.getSort());
        }
        bannerIndex.setStatus(0);//是否下架，0未下架 1下架
        bannerIndexMapper.insert(bannerIndex);
        return bannerIndex;
    }

    @Override
    public BannerIndex updateBanner(GoodsParam param) throws ServiceException {
        BannerIndex bannerIndex = bannerIndexMapper.selectByPrimaryKey(param.getBannerIndexId());
        if(bannerIndex == null){
            throw new ServiceException("获取轮播图失败！");
        }else{
            bannerIndex.setType(param.getType());
            bannerIndex.setBusinessId(param.getBusinessId());
            bannerIndex.setImgUrl(param.getImgUrl());
            bannerIndex.setJumpType(param.getJumpType());
            if(StringUtils.isNotEmpty(param.getTitle())){
                bannerIndex.setTitle(param.getTitle());
            }
            if(param.getSort() != null){
                bannerIndex.setSort(param.getSort());
            }
            if(param.getStatus() != null){
                bannerIndex.setStatus(param.getStatus());
            }
            bannerIndexMapper.updateByPrimaryKey(bannerIndex);
            return bannerIndex;
        }
    }

    @Override
    public List<SearchDto> hotSearch() throws ServiceException {
        PageHelper.startPage(0,10);
        List<HotSearch> hotSearchList = hotSearchMapper.getHotSearchList();
        List<SearchDto> searchDtos = SearchDto.toDtoList(hotSearchList);
        if(searchDtos == null || searchDtos.size()==0){
            throw new ServiceException("获取热门查询失败！");
        }else{
            return searchDtos;
        }
    }

    @Override
    public List<GoodsCategoryDto> hotCategary() throws ServiceException {
        PageHelper.startPage(0,10);
        List<GoodsCategory> hotCategaryList = goodsCategoryMapper.getHotCategaryList();
        List<GoodsCategoryDto> goodsCategoryDtos = GoodsCategoryDto.toDtoList(hotCategaryList);
        if(goodsCategoryDtos == null || goodsCategoryDtos.size()==0){
            throw new ServiceException("获取热门分类失败！");
        }else{
            return goodsCategoryDtos;
        }
    }

    @Override
    public List<GoodsQuickSearchDto> quickSearch() throws ServiceException {
        List<GoodsQuickSearch> quickSearchList = goodsQuickSearchMapper.getQuickSearchList();
        List<GoodsQuickSearchDto> goodsQuickSearchDtos = GoodsQuickSearchDto.toDtoList(quickSearchList);
        if(goodsQuickSearchDtos == null || goodsQuickSearchDtos.size()==0){
            throw new ServiceException("获取快速查询条件列表失败！");
        }else{
            return goodsQuickSearchDtos;
        }
    }

    @Override
    public void updateGoodsStatus(Integer goodsId,Integer status) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        if(goodsInfo == null){
            throw new ServiceException("获取商品失败！");
        }else{
            goodsInfo.setStatus(status);
            goodsInfoMapper.updateByPrimaryKey(goodsInfo);
        }
    }

    @Override
    public void reportGoods(Integer userId, Integer goodsId, String reason) throws ServiceException {
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
        if(goodsInfo == null){
            throw new ServiceException("获取商品失败！");
        }else{
            //判断商品状态
            ReportGoods reportGoods = reportGoodsMapper.selectByUserIdGoodsId(userId,goodsId);
            if(reportGoods != null){
                throw new ServiceException("您已举报过此商品！");
            }else{
                reportGoods = new ReportGoods();
                reportGoods.setAddTime(DateStampUtils.getTimesteamp());
                reportGoods.setGoodsId(goodsId);
                reportGoods.setReason(reason);
                reportGoods.setUserId(userId);
                reportGoodsMapper.insert(reportGoods);
            }
        }
    }
}
