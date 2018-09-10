package com.fangyuanyouyue.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMergeMapper {

    /**
     * 合并优惠券
     * @param userId
     * @param toUserId
     */
    void MergeCoupon(@Param("userId")Integer userId,@Param("toUserId")Integer toUserId);

    /**
     * 合并商品评论
     * @param userId
     * @param toUserId
     */
    void MergeGoodsComment(@Param("userId")Integer userId,@Param("toUserId")Integer toUserId);

    /**
     * 合并帖子、视频评论
     * @param userId
     * @param toUserId
     */
    void MergeForumComment(@Param("userId")Integer userId,@Param("toUserId")Integer toUserId);

}
