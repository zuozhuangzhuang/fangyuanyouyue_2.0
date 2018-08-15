package com.fangyuanyouyue.goods.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.fangyuanyouyue.goods.dto.GoodsCommentDto;
import com.fangyuanyouyue.goods.dto.GoodsDto;
import com.fangyuanyouyue.goods.model.Collect;
import com.fangyuanyouyue.goods.model.GoodsCorrelation;
import com.fangyuanyouyue.goods.model.GoodsImg;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.model.UserInfo;
import com.fangyuanyouyue.goods.service.CollectService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "collectService")
@Transactional(rollbackFor=Exception.class)
public class CollectServiceImpl implements CollectService{
    @Autowired
    private CollectMapper collectMapper;
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
    private SchedualUserService schedualUserService;//调用其他service时用

    @Override
    public void collect(Integer userId, Integer[] collectIds, Integer collectType, Integer type,Integer status) throws ServiceException {
        for(Integer collectId:collectIds){
            Collect collect = collectMapper.selectByCollectId(userId,collectId, type);
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(collectId);
            if(goodsInfo == null){
                throw new ServiceException("商品数据异常！");
            }else{
                if(goodsInfo.getType().intValue() != collectType.intValue()){
                    throw new ServiceException("类型错误！");
                }
                //collectType 关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏
                //type 类型 1关注 2收藏
                if(collect != null){
                    if(status == 1){//关注/收藏
                        if(type == 1){
                            throw new ServiceException("已关注，请勿重新关注！");
                        }else{
                            //批量收藏存在已收藏的跳过！
                            int count = 0;
                            if(collectIds.length > 1){
                                count++;
                                if(collectIds.length == count){
                                    throw new ServiceException("已全部收藏，请勿重新收藏！");
                                }
                                continue;
                            }else{
                                throw new ServiceException("已收藏，请勿重新收藏！");
                            }
                        }
                    }else if(status == 2){//取消关注/收藏
                        collectMapper.deleteByPrimaryKey(collect.getId());
                    }else{
                        throw new ServiceException("状态值错误！");
                    }
                }else{
                    if(status == 2){//取消关注/收藏
                        if(type == 1){
                            throw new ServiceException("未关注，请先关注！");
                        }else{
                            throw new ServiceException("未收藏，请先收藏！");
                        }
                    }else if(status == 1){
                        if(type == 1){
                            if(collectType != 2){
                                throw new ServiceException("关注类型错误！");
                            }
                        }else if(type == 2){
                            if(collectType != 1 && collectType != 2){
                                throw new ServiceException("收藏类型错误！");
                            }
                        }else{
                            throw new ServiceException("类型错误！");
                        }
                        collect = new Collect();
                        collect.setAddTime(DateStampUtils.getTimesteamp());
                        collect.setCollectId(collectId);
                        collect.setCollectType(collectType);
                        collect.setType(type);
                        collect.setUserId(userId);
                        collectMapper.insert(collect);

                    }else{
                        throw new ServiceException("状态值错误！");
                    }
                }
            }
        }

    }

    @Override
    public List<GoodsDto> collectList(Integer userId, Integer collectType, Integer type,Integer start,Integer limit) throws ServiceException {
        //collectType 关注/收藏类型 1商品 2抢购 3视频 4专栏 5鉴赏
        //type 类型 1关注 2收藏
        if(type == 1){
            if(collectType != 2){
                throw new ServiceException("关注类型错误！");
            }
        }else if(type == 2){
            if(collectType != 1 && collectType != 2){
                throw new ServiceException("收藏类型错误！");
            }
        }else{
            throw new ServiceException("类型错误！");
        }
        List<GoodsInfo> goodsInfos = goodsInfoMapper.selectMyCollectGoods(userId, collectType, type,null,start*limit,limit);
        List<GoodsDto> goodsDtos = new ArrayList<>();
        for (GoodsInfo goodsInfo:goodsInfos) {
            goodsDtos.add(setDtoByGoodsInfo(goodsInfo));
        }
        return goodsDtos;
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
            String mainImgUrl = null;
            for(GoodsImg goodsImg:goodsImgs){
                if(goodsImg.getType() == 1){
                    mainImgUrl = goodsImg.getImgUrl();
                }
            }
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
                goodsCommentDto.setMainUrl(mainImgUrl);
                goodsCommentDto.setGoodsName(goodsInfo.getName());
                goodsCommentDto.setDescprition(goodsInfo.getDescription());
            }
            //获取卖家信息
            UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(goodsInfo.getUserId())).getString("data")), UserInfo.class);
            GoodsDto goodsDto = new GoodsDto(user,goodsInfo,goodsImgs,goodsCorrelations,goodsCommentDtos);
            goodsDto.setCommentCount(goodsCommentMapperl.selectCount(goodsInfo.getId()));
            return goodsDto;
        }
    }
}
