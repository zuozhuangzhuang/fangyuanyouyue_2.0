package com.fangyuanyouyue.goods.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fangyuanyouyue.goods.dao.CommentLikesMapper;
import com.fangyuanyouyue.goods.dao.GoodsImgMapper;
import com.fangyuanyouyue.goods.dao.GoodsInfoMapper;
import com.fangyuanyouyue.goods.model.CommentLikes;
import com.fangyuanyouyue.goods.model.GoodsImg;
import com.fangyuanyouyue.goods.model.GoodsInfo;
import com.fangyuanyouyue.goods.service.SchedualMessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.goods.dao.GoodsCommentMapper;
import com.fangyuanyouyue.goods.dto.GoodsCommentDto;
import com.fangyuanyouyue.goods.model.GoodsComment;
import com.fangyuanyouyue.goods.param.GoodsParam;
import com.fangyuanyouyue.goods.service.CommentService;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "commentService")
@Transactional(rollbackFor=Exception.class)
public class CommentServiceImpl implements CommentService{
    @Autowired
    private GoodsCommentMapper goodsCommentMapper;
    @Autowired
    private CommentLikesMapper commentLikesMapper;
    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private GoodsImgMapper goodsImgMapper;
    @Autowired
    private SchedualMessageService schedualMessageService;

    @Override
    public Integer addComment(GoodsParam param) throws ServiceException {
        GoodsComment goodsComment = new GoodsComment();
        goodsComment.setAddTime(DateStampUtils.getTimesteamp());
        if(param.getCommentId() != null){
            goodsComment.setCommentId(param.getCommentId());
        }
        goodsComment.setUserId(param.getUserId());
        if(StringUtils.isNotEmpty(param.getImg1Url())){
            goodsComment.setImg1Url(param.getImg1Url());
        }
        if(StringUtils.isNotEmpty(param.getImg2Url())){
            goodsComment.setImg1Url(param.getImg2Url());
        }
        if(StringUtils.isNotEmpty(param.getImg3Url())){
            goodsComment.setImg1Url(param.getImg3Url());
        }
        goodsComment.setStatus(1);//状态 1正常 2隐藏
        if(StringUtils.isNotEmpty(param.getContent())){
            goodsComment.setContent(param.getContent());
        }
        goodsComment.setGoodsId(param.getGoodsId());
        goodsComment.setLikesCount(0);//点赞数初始值为0
        goodsCommentMapper.insert(goodsComment);
        //社交消息：您的商品【商品名称】有新的评论，点击此处前往查看吧
        //社交消息：您的抢购【抢购名称】有新的评论，点击此处前往查看吧
        GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(param.getGoodsId());
        schedualMessageService.easemobMessage(goodsInfo.getUserId().toString(),
                "您的"+(goodsInfo.getType() == 1?"商品【":"抢购【")+goodsInfo.getName()+"】有新的评论，点击此处前往查看吧","8","3",goodsComment.getId().toString());
        return goodsComment.getId();
    }

    @Override
    public void commentLikes(Integer userId,Integer commentId,Integer type) throws ServiceException {
        //获取评论信息
        GoodsComment goodsComment = goodsCommentMapper.selectByPrimaryKey(commentId);
        if(goodsComment == null){
            throw new ServiceException("评论不存在！");
        }else{
            //评论点赞
            CommentLikes commentLikes = commentLikesMapper.selectByUserId(userId,commentId);
            if(type == 1){//点赞
                if(commentLikes != null){
                    throw new ServiceException("您已赞过此评论！");
                }else{
                    commentLikes = new CommentLikes();
                    commentLikes.setUserId(userId);
                    commentLikes.setCommentId(goodsComment.getId());
                    commentLikes.setAddTime(DateStampUtils.getTimesteamp());
                    commentLikesMapper.insert(commentLikes);
                    //更新点赞数
                    goodsComment.setLikesCount(goodsComment.getLikesCount()+1);
                    goodsCommentMapper.updateByPrimaryKey(goodsComment);
                }
            }else if(type == 2){//取消点赞
                if(commentLikes != null){
                    commentLikesMapper.deleteByPrimaryKey(commentLikes.getId());
                    //更新点赞数
                    goodsComment.setLikesCount(goodsComment.getLikesCount()-1);
                    goodsCommentMapper.updateByPrimaryKey(goodsComment);
                }else{
                    throw new ServiceException("您还未赞过此评论！");
                }
            }else{
                throw new ServiceException("类型错误！");
            }
        }
    }

    @Override
    public List<GoodsCommentDto> getComments(Integer userId,Integer goodsId,Integer start,Integer limit) throws ServiceException {
        List<Map<String, Object>> maps = goodsCommentMapper.selectByGoodsId( goodsId,start*limit,limit);
        List<GoodsCommentDto> goodsCommentDtos = GoodsCommentDto.mapToDtoList(maps);
        for(GoodsCommentDto goodsCommentDto:goodsCommentDtos){
            goodsCommentDto.setReplys(selectCommentList(goodsCommentDto.getId(),goodsId));
            //判断评论是否已点赞
            if(userId != null){
                CommentLikes commentLikes = commentLikesMapper.selectByUserId(userId, goodsCommentDto.getId());
                if(commentLikes != null){
                    goodsCommentDto.setIsLike(1);
                }
            }
        }
        return goodsCommentDtos;
    }

    /**
     * 递归获取评论及回复 （扁平化只显示两层，这里通过新增评论时多层级的回复全部按照一级评论作为父级实现，不用修改此处逻辑）
     * @param commentId
     * @param goodsId
     * @return
     */
    private List<GoodsCommentDto> selectCommentList(Integer commentId,Integer goodsId){
        //根据评论的ID和商品的ID获取回复列表
        List<Map<String, Object>> maps = goodsCommentMapper.selectMapByGoodsIdCommentId(commentId, goodsId,null,null);
        List<GoodsCommentDto> goodsCommentDtos = GoodsCommentDto.mapToDtoList(maps);
        if(goodsCommentDtos != null && goodsCommentDtos.size()>0){
            for(GoodsCommentDto goodsCommentDto:goodsCommentDtos){
                //获取被评论人的信息
                Map<String, Object> map = goodsCommentMapper.selectByCommentId(goodsCommentDto.getCommentId());
                if(map != null){
                    goodsCommentDto.setToUserId((Integer)map.get("user_id"));
                    goodsCommentDto.setToUserHeadImgUrl((String)map.get("head_img_url"));
                    goodsCommentDto.setToUserName((String)map.get("nick_name"));
                }
                //重复获取此回复的回复列表，直到没有回复了为止
                goodsCommentDto.setReplys(selectCommentList(goodsCommentDto.getId(),goodsId));
            }
        }
        return goodsCommentDtos;
    }

    @Override
    public List<GoodsCommentDto> myComments(Integer userId, Integer type, Integer start, Integer limit) throws ServiceException {
        //根据用户ID获取所有user_id为userId且status=1的评论，按照时间排序，并根据评论商品ID获取商品信息，根据userId获取用户信息
        List<Map<String, Object>> maps = goodsCommentMapper.selectByUserId(userId, start * limit, limit);
        List<GoodsCommentDto> goodsCommentDtos = GoodsCommentDto.mapToDtoList(maps);
        Iterator<GoodsCommentDto> it = goodsCommentDtos.iterator();
        while(it.hasNext()){
            GoodsCommentDto goodsCommentDto = it.next();
            GoodsInfo goodsInfo = goodsInfoMapper.selectByPrimaryKey(goodsCommentDto.getGoodsId());
            if(goodsInfo == null){
                throw new ServiceException("商品信息异常！");
            }
            if(goodsInfo.getType().intValue() != type.intValue()){
                it.remove();
                continue;
            }
            List<GoodsImg> imgsByGoodsId = goodsImgMapper.getImgsByGoodsId(goodsInfo.getId());
            StringBuffer mainImgUrl = new StringBuffer();
            for(GoodsImg goodsImg:imgsByGoodsId){
                if(goodsImg.getType() == 1){
                    mainImgUrl.append(goodsImg.getImgUrl());
                }
            }
            //获取被评论人的信息
            Map<String, Object> map = goodsCommentMapper.selectByCommentId(goodsCommentDto.getCommentId());
            if(map != null){
                goodsCommentDto.setToUserId((Integer)map.get("user_id"));
                goodsCommentDto.setToUserHeadImgUrl((String)map.get("head_img_url"));
                goodsCommentDto.setToUserName((String)map.get("nick_name"));
            }
            goodsCommentDto.setGoodsName(goodsInfo.getName());
            goodsCommentDto.setMainUrl(mainImgUrl.toString());
            goodsCommentDto.setDescription(goodsInfo.getDescription());
        }
        return goodsCommentDtos;
    }

    @Override
    public void deleteComment(Integer[] commentIds) throws ServiceException {
        for(Integer commentId:commentIds){
            //获取评论信息
            GoodsComment goodsComment = goodsCommentMapper.selectByPrimaryKey(commentId);
            if(goodsComment == null || goodsComment.getStatus() == 2){
                throw new ServiceException("评论不存在！");
            }else{
                goodsComment.setStatus(2);//状态 1正常 2隐藏
                goodsCommentMapper.updateByPrimaryKey(goodsComment);
                List<GoodsComment> replys = goodsCommentMapper.selectCommentByCommentId(commentId);
                for(GoodsComment reply:replys){
                    reply.setStatus(2);
                    goodsCommentMapper.updateByPrimaryKey(reply);
                }
            }
        }
    }
}
