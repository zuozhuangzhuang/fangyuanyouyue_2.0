package com.fangyuanyouyue.goods.service.impl;

import java.util.*;

import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.goods.dao.*;
import com.fangyuanyouyue.goods.dto.GoodsCommentDto;
import com.fangyuanyouyue.goods.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dto.CartDetailDto;
import com.fangyuanyouyue.goods.dto.CartShopDto;
import com.fangyuanyouyue.goods.dto.GoodsDto;
import com.fangyuanyouyue.goods.service.CartService;
import com.fangyuanyouyue.goods.service.SchedualUserService;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "cartService")
@Transactional(rollbackFor=Exception.class)
public class CartServiceImpl implements CartService {
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private CartDetailMapper cartDetailMapper;
    @Autowired
    private CartInfoMapper cartInfoMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private SchedualUserService schedualUserService;//调用其他service时用
    @Autowired
    private GoodsCorrelationMapper goodsCorrelationMapper;
    @Autowired
    private GoodsCommentMapper goodsCommentMapperl;
    @Autowired
    private GoodsBargainMapper goodsBargainMapper;




    @Override
    public void addGoodsToCart(Integer userId, Integer goodsId) throws ServiceException {
        //获取购物车信息
        CartInfo cartInfo = cartInfoMapper.selectByUserId(userId);
        if (cartInfo == null) {
            //购物车不存在就新增
            cartInfo = new CartInfo();
            cartInfo.setUserId(userId);
            cartInfo.setAddTime(DateStampUtils.getTimesteamp());
            cartInfoMapper.insert(cartInfo);
        } else {
            //购物车存在就修改update_time
            cartInfoMapper.updateByPrimaryKey(cartInfo);

            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsId);
            //判断商品还在不
            if (goodsInfo == null || goodsInfo.getStatus().intValue() == 3 || goodsInfo.getStatus().intValue() == 5) {
                throw new ServiceException("商品不存在或已下架！");
            } else {
                if(goodsInfo.getStatus().intValue() != 1){//1出售中
                    throw new ServiceException("商品状态异常！");
                }
                if(goodsInfo.getUserId().intValue() == userId.intValue()){
                    throw new ServiceException("不可以把自己的商品加入到购物车！");
                }
                if(goodsInfo.getType().equals(Status.AUCTION.getValue())){
                    throw new ServiceException("抢购商品无法加入购物车！");
                }
                CartDetail cartDetail = cartDetailMapper.selectByCartIdGoodsId(cartInfo.getId(), goodsId);
                //判断购物车是否已经有这个商品了
                if (cartDetail == null) {
                    cartDetail = new CartDetail();
                    cartDetail.setAddTime(DateStampUtils.getTimesteamp());
                    cartDetail.setCartId(cartInfo.getId());
                    cartDetail.setGoodsId(goodsId);
                    cartDetail.setPrice(goodsInfo.getPrice());
                    cartDetail.setUserId(goodsInfo.getUserId());
                    cartDetailMapper.insert(cartDetail);
                } else {
                    throw new ServiceException("商品已存在购物车中，请勿重复添加！");
                }
            }
        }
    }

    @Override
    public List<CartShopDto> getCart(Integer userId) throws ServiceException {
        CartInfo cart = cartInfoMapper.selectByUserId(userId);
        //购物车是否存在
        if (cart == null) {
            return null;
        } else {
            List<CartShopDto> cartShopDtos = new ArrayList<>();
            List<CartDetail> cartDetails = cartDetailMapper.selectByCartId(cart.getId());
            if (cartDetails != null) {
                for (CartDetail cartDetail : cartDetails) {
                    //是否官方认证
                    Map<String, Object> goodsUserInfoExtAndVip = goodsInfoMapper.getGoodsUserInfoExtAndVip(cartDetail.getGoodsId());
                    BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(cartDetail.getUserId()));
                    if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                        throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
                    }
                    //获取卖家信息
                    UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()), UserInfo.class);
                    CartShopDto cartShopDto = new CartShopDto();
                    cartShopDto.setUserId(cartDetail.getUserId());
                    cartShopDto.setHeadImgUrl(user.getHeadImgUrl());
                    cartShopDto.setNickName(user.getNickName());

                    List<CartDetailDto> cartDetailDtos = CartDetailDto.toDtoList(cartDetailMapper.selectByCartIdUserId(cart.getId(), cartDetail.getUserId()));
                    if(cartDetailDtos == null){
                        continue;
                    }
                    for (CartDetailDto cartDetailDto : cartDetailDtos) {
                        //是否官方认证
                        if(goodsUserInfoExtAndVip != null) {
                            cartDetailDto.setAuthType((Integer) goodsUserInfoExtAndVip.get("auth_type") == 2 ? 1 : 2);
                        }
                        List<GoodsImg> imgsByGoodsId = goodsImgMapper.getImgsByGoodsId(cartDetailDto.getGoodsId());
                        for (GoodsImg goodsImg : imgsByGoodsId) {
                            if (goodsImg.getType() == 1) {//主图
                                cartDetailDto.setMainUrl(goodsImg.getImgUrl());
                            }
                        }
                        //传递商品状态
                        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(cartDetailDto.getGoodsId());
                        cartDetailDto.setStatus(goodsInfo.getStatus());
                        cartDetailDto.setUserId(cartDetail.getUserId());
                        //用户是否存在申请中议价
                        List<GoodsBargain> goodsBargains = goodsBargainMapper.selectByUserIdGoodsId(userId, cartDetailDto.getGoodsId(), 1);
                        if(goodsBargains != null && goodsBargains.size() > 0){
                            cartDetailDto.setBargainId(goodsBargains.get(0).getId());
                        }
                    }
                    cartShopDto.setCartDetail(cartDetailDtos);
                    cartShopDtos.add(cartShopDto);
                }
                return cartShopDtos;
            } else {
                throw new ServiceException("获取购物车详情失败！");
            }
        }
    }

    @Override
    public void cartRemove(Integer[] cartDetailIds) throws ServiceException {
        for (Integer cartDetailId : cartDetailIds) {
            CartDetail cartDetail = cartDetailMapper.selectByPrimaryKey(cartDetailId);
            if (cartDetail == null) {
                throw new ServiceException("购物车详情数据异常！");
            } else {
                cartDetailMapper.deleteByPrimaryKey(cartDetail.getId());
            }
        }
    }

    @Override
    public List<GoodsDto> choice(Integer userId, Integer start, Integer limit) throws ServiceException {
        //1、根据商品分类 2、根据会员等级排序
        CartInfo cartInfo = cartInfoMapper.selectByUserId(userId);
        List<Integer> goodsCategoryIds = new ArrayList<>();
        if(cartInfo != null){
            List<CartDetail> cartDetails = cartDetailMapper.selectByCartId(cartInfo.getId());
            //获取购物车内所有商品分类并去重
            Set<Integer> set = new HashSet<>();
            for(CartDetail detail:cartDetails){
                List<Integer> integers = goodsCorrelationMapper.selectCategoryIdByGoodsId(detail.getGoodsId());
                set.addAll(integers);
            }
            goodsCategoryIds.addAll(set);
        }
        List<GoodsInfo> goodsInfos = goodsInfoMapper.selectByCategoryIds(goodsCategoryIds, start * limit, limit);
        List<GoodsDto> goodsDtos = new ArrayList<>();
        for (GoodsInfo goodsInfo : goodsInfos) {
            goodsDtos.add(setDtoByGoodsInfo(goodsInfo));
        }
        return goodsDtos;
    }

    /**
     * 给GoodsDto赋值
     *
     * @param goodsInfo
     * @return
     * @throws ServiceException
     */
    private GoodsDto setDtoByGoodsInfo(GoodsInfo goodsInfo) throws ServiceException {
        if(goodsInfo == null){
            throw new ServiceException("商品不存在或已下架！");
        } else {
            List<GoodsImg> goodsImgs = goodsImgMapper.getImgsByGoodsId(goodsInfo.getId());
            String mainImgUrl = null;
            for(GoodsImg goodsImg:goodsImgs){
                if(goodsImg.getType() == 1){
                    mainImgUrl = goodsImg.getImgUrl();
                }
            }
            List<GoodsCorrelation> goodsCorrelations = goodsCorrelationMapper.getCorrelationsByGoodsId(goodsInfo.getId());
            //按照先后顺序获取评论
            List<Map<String, Object>> maps = goodsCommentMapperl.selectMapByGoodsIdCommentId(null, goodsInfo.getId(), 0, 3);
            List<GoodsCommentDto> goodsCommentDtos = GoodsCommentDto.mapToDtoList(maps);
            for (GoodsCommentDto goodsCommentDto : goodsCommentDtos) {
                Map<String, Object> map = goodsCommentMapperl.selectByCommentId(goodsCommentDto.getCommentId());
                if (map != null) {
                    goodsCommentDto.setToUserId((Integer) map.get("user_id"));
                    goodsCommentDto.setToUserHeadImgUrl((String) map.get("head_img_url"));
                    goodsCommentDto.setToUserName((String) map.get("nick_name"));
                }
                goodsCommentDto.setGoodsName(goodsInfo.getName());
                goodsCommentDto.setDescription(goodsInfo.getDescription());
                goodsCommentDto.setMainUrl(mainImgUrl);
            }

            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(goodsInfo.getUserId()));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                throw new ServiceException(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            //获取卖家信息
            UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(parseReturnValue.getData().toString()), UserInfo.class);
            GoodsDto goodsDto = new GoodsDto(user, goodsInfo, goodsImgs, goodsCorrelations, goodsCommentDtos);
            goodsDto.setCommentCount(goodsCommentMapperl.selectCount(goodsInfo.getId()));
            return goodsDto;
        }
    }

    @Override
    public void cartRemoveByIds(Integer userId,Integer[] goodsIds) throws ServiceException {
        //根据商品ID数组删除购物车内信息
        CartInfo cart = cartInfoMapper.selectByUserId(userId);
        if (cart == null) {
            throw new ServiceException("购物车异常！");
        } else {
            for(Integer goodsId : goodsIds){
                CartDetail cartDetail = cartDetailMapper.selectByCartIdGoodsId(cart.getId(),goodsId);
                if (cartDetail == null) {
                    throw new ServiceException("购物车详情数据异常！");
                } else {
                    cartDetailMapper.deleteByPrimaryKey(cartDetail.getId());
                }
            }
        }
    }
}