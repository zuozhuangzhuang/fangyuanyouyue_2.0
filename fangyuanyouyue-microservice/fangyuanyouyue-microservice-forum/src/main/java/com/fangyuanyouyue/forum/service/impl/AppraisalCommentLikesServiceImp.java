package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.AppraisalCommentLikesMapper;
import com.fangyuanyouyue.forum.model.AppraisalCommentLikes;
import com.fangyuanyouyue.forum.service.AppraisalCommentLikesService;


@Service(value = "appraisalCommentLikesService")
public class AppraisalCommentLikesServiceImp implements AppraisalCommentLikesService {

	@Autowired
	private AppraisalCommentLikesMapper appraisalCommentLikesMapper;

	@Override
	public Integer countCommentLikes(Integer commentId) throws ServiceException {
		return appraisalCommentLikesMapper.countById(commentId);
	}

	@Override
	public void saveLikes(Integer userId, Integer commentId) throws ServiceException {
		
		AppraisalCommentLikes model = new AppraisalCommentLikes();
		model.setAddTime(new Date());
		model.setCommentId(commentId);
		model.setUserId(userId);
		
		appraisalCommentLikesMapper.insert(model);
		
	}

   
	
}
