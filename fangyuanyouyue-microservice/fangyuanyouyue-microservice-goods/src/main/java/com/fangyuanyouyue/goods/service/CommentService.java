package com.fangyuanyouyue.goods.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.goods.dto.GoodsCommentDto;
import com.fangyuanyouyue.goods.param.GoodsParam;

public interface CommentService {
    /**
     * 增加评论/回复
     * @param param
     * @throws ServiceException
     */
    Integer addComment(GoodsParam param) throws ServiceException;

    /**
     * 评论点赞
     * @param userId
     * @param commentId
     * @throws ServiceException
     */
    void commentLikes(Integer userId,Integer commentId,Integer type) throws ServiceException;

    /**
     * 查看全部评论
     * @param userId
     * @param goodsId
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<GoodsCommentDto> getComments(Integer userId,Integer goodsId,Integer start,Integer limit) throws ServiceException;

    /**
     * （商品/抢购）我的评论
     * @param userId
     * @param type
     * @param start
     * @param limit
     * @return
     * @throws ServiceException
     */
    List<GoodsCommentDto> myComments(Integer userId,Integer type,Integer start,Integer limit) throws ServiceException;

    /**
     * 删除评论
     * @param commentIds
     * @throws ServiceException
     */
    void deleteComment(Integer[] commentIds) throws ServiceException;
}
