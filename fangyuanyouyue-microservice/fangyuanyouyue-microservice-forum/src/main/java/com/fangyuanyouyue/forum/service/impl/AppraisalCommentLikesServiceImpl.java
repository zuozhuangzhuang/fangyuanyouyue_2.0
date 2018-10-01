package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.forum.dao.AppraisalCommentMapper;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.model.AppraisalComment;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.AppraisalCommentLikesMapper;
import com.fangyuanyouyue.forum.model.AppraisalCommentLikes;
import com.fangyuanyouyue.forum.service.AppraisalCommentLikesService;


@Service(value = "appraisalCommentLikesService")
@Transactional(rollbackFor=Exception.class)
public class AppraisalCommentLikesServiceImpl implements AppraisalCommentLikesService {

	@Autowired
	private AppraisalCommentLikesMapper appraisalCommentLikesMapper;
	@Autowired
    private AppraisalCommentMapper appraisalCommentMapper;
	@Autowired
    private AppraisalDetailMapper appraisalDetailMapper;

	@Override
	public Integer countCommentLikes(Integer commentId) throws ServiceException {
		return appraisalCommentLikesMapper.countById(commentId);
	}

	@Override
	public void saveLikes(Integer userId, Integer commentId,Integer type) throws ServiceException {
        AppraisalComment comment = appraisalCommentMapper.selectByPrimaryKey(commentId);
        if(comment == null || comment.getStatus().equals(Status.HIDE.getValue())){
            throw new ServiceException("未找到评论！");
        }
        AppraisalDetail detail = appraisalDetailMapper.selectByPrimaryKey(comment.getAppraisalId());
        if(detail == null || detail.getStatus().equals(Status.DELETE.getValue())){
            throw new ServiceException("未找到全民鉴定！");
        }
        if(detail.getStatus().equals(Status.END.getValue())){
            throw new ServiceException("全民鉴定已结束！");
        }
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
