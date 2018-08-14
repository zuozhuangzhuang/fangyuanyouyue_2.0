package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.AppraisalLikesMapper;
import com.fangyuanyouyue.forum.model.AppraisalLikes;
import com.fangyuanyouyue.forum.service.AppraisalLikesService;


@Service(value = "appraisalLikesService")
public class AppraisalLikesServiceImp implements AppraisalLikesService {
	
	@Autowired
	private AppraisalLikesMapper appraisalLikesMapper;

	@Override
	public Integer countLikes(Integer appraisalId) throws ServiceException {
		return appraisalLikesMapper.countById(appraisalId);
	}

	@Override
	public void saveLikes(Integer userId, Integer appraisalId) throws ServiceException {
		AppraisalLikes model = new AppraisalLikes();
		model.setAddTime(new Date());
		model.setUserId(userId);
		model.setAppraisalId(appraisalId);
		
		appraisalLikesMapper.insert(model);
		
	}

   
	
}
