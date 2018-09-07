package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.AppraisalCommentLikesMapper;
import com.fangyuanyouyue.forum.model.AppraisalCommentLikes;
import com.fangyuanyouyue.forum.service.AppraisalCommentLikesService;


@Service(value = "appraisalCommentLikesService")
public class AppraisalCommentLikesServiceImpl implements AppraisalCommentLikesService {

	@Autowired
	private AppraisalCommentLikesMapper appraisalCommentLikesMapper;

	@Override
	public Integer countCommentLikes(Integer commentId) throws ServiceException {
		return appraisalCommentLikesMapper.countById(commentId);
	}

	@Override
	public void saveLikes(Integer userId, Integer commentId,Integer type) throws ServiceException {
        AppraisalCommentLikes appraisalCommentLikes = appraisalCommentLikesMapper.selectByCommentIdUserId(commentId, userId);
        if(type == 1){
            if(appraisalCommentLikes != null){
                throw new ServiceException("您已点赞，请勿重复点赞！");
            }else{
                appraisalCommentLikes = new AppraisalCommentLikes();
                appraisalCommentLikes.setAddTime(new Date());
                appraisalCommentLikes.setCommentId(commentId);
                appraisalCommentLikes.setUserId(userId);

                appraisalCommentLikesMapper.insert(appraisalCommentLikes);
            }
		}else if(type == 2){
            if(appraisalCommentLikes == null){
                throw new ServiceException("未点赞，请先点赞！");
            }else{
                appraisalCommentLikesMapper.deleteByPrimaryKey(appraisalCommentLikes.getId());
            }
		}else{
			throw new ServiceException("类型错误！");
		}

	}

   
	
}
