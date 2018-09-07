package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import com.fangyuanyouyue.base.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.forum.dao.ForumCommentLikesMapper;
import com.fangyuanyouyue.forum.model.ForumCommentLikes;
import com.fangyuanyouyue.forum.service.ForumCommentLikesService;


@Service(value = "forumCommentLikesService")
public class ForumCommentLikesServiceImpl implements ForumCommentLikesService {

    @Autowired
    private ForumCommentLikesMapper forumCommentLikesMapper;
    
	@Override
	public Integer countLikes(Integer commentId) {
		return forumCommentLikesMapper.countById(commentId);
	}

	@Override
	public void saveLikes(Integer type, Integer userId, Integer commentId)  throws ServiceException {
		ForumCommentLikes forumCommentLikes = forumCommentLikesMapper.selectByUserIdCommentId(userId, commentId);
		if(type == 1){
			if(forumCommentLikes != null){
				throw new ServiceException("您已点赞，请勿重复点赞！");
			}else{
				forumCommentLikes = new ForumCommentLikes();
				forumCommentLikes.setUserId(userId);
				forumCommentLikes.setCommentId(commentId);
				forumCommentLikes.setAddTime(new Date());
				forumCommentLikesMapper.insert(forumCommentLikes);
			}
		}else if(type == 2){
			if(forumCommentLikes == null){
				throw new ServiceException("未点赞，请先点赞！");
			}else{
				forumCommentLikesMapper.deleteByPrimaryKey(forumCommentLikes.getId());
			}
		}else{
			throw new ServiceException("类型错误！");
		}
	}

}
