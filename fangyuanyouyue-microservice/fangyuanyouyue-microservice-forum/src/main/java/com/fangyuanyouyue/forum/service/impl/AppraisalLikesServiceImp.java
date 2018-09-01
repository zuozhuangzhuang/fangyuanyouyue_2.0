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
	public void saveLikes(Integer userId, Integer appraisalId,Integer type) throws ServiceException {
		AppraisalLikes appraisalLikes = appraisalLikesMapper.selectByAppraisalIdUserId(appraisalId, userId);
		if(type == 1){
			if(appraisalLikes != null){
				throw new ServiceException("您已点赞，请勿重复点赞！");
			}else{
				appraisalLikes = new AppraisalLikes();
				appraisalLikes.setAddTime(new Date());
				appraisalLikes.setUserId(userId);
				appraisalLikes.setAppraisalId(appraisalId);

				appraisalLikesMapper.insert(appraisalLikes);
			}
		}else if(type == 2){
			if(appraisalLikes == null){
				throw new ServiceException("未点赞，请先点赞！");
			}else{
				appraisalLikesMapper.deleteByPrimaryKey(appraisalLikes.getId());
			}
		}else{
			throw new ServiceException("类型错误！");
		}

	}

   
	
}
