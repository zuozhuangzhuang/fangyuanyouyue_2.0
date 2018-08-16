package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.forum.dao.ForumCommentLikesMapper;
import com.fangyuanyouyue.forum.model.ForumCommentLikes;
import com.fangyuanyouyue.forum.service.ForumCommentLikesService;


@Service(value = "forumCommentLikesService")
public class ForumCommentLikesServiceImp implements ForumCommentLikesService {

    @Autowired
    private ForumCommentLikesMapper forumCommentLikesMapper;
    
	public Integer countLikes(Integer commentId) {
		return forumCommentLikesMapper.countById(commentId);
	}

	@Override
	public void saveLikes(Integer type, Integer userId, Integer commentId) {
		ForumCommentLikes model = new ForumCommentLikes();
		model.setUserId(userId);
		model.setCommentId(commentId);
		model.setAddTime(new Date());
		forumCommentLikesMapper.insert(model);
	}

}
