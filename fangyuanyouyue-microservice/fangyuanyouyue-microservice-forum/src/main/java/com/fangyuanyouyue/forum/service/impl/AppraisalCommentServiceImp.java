package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.AppraisalCommentMapper;
import com.fangyuanyouyue.forum.dto.AppraisalCommentDto;
import com.fangyuanyouyue.forum.model.AppraisalComment;
import com.fangyuanyouyue.forum.param.AppraisalParam;
import com.fangyuanyouyue.forum.service.AppraisalCommentService;


@Service(value = "appraisalCommentService")
public class AppraisalCommentServiceImp implements AppraisalCommentService {
	
	@Autowired
	AppraisalCommentMapper appraisalCommentMapper;

	@Override
	public Integer countComment(Integer id) throws ServiceException {

		return appraisalCommentMapper.countById(id);
	}

	@Override
	public List<AppraisalCommentDto> getAppraisalCommentList(Integer appraisalId, Integer start, Integer limit)
			throws ServiceException {
		List<AppraisalComment> list = appraisalCommentMapper.selectByAppraisalId(appraisalId, start, limit);
		return AppraisalCommentDto.toDtoList(list);
	}

	@Override
	public void saveComment(Integer userId,AppraisalParam param) {
		AppraisalComment model = new AppraisalComment();
		model.setAddTime(new Date());
		model.setAppraisalId(param.getAppraisalId());
		model.setContent(param.getContent());
		model.setUserId(userId);
		model.setViewpoint(param.getViewpoint());
		appraisalCommentMapper.insert(model);
	}

	@Override
	public Integer countComment(Integer appraisalId, Integer viewpoint) throws ServiceException {

		return appraisalCommentMapper.countByViewPoint(appraisalId, viewpoint);
	}

   
	
}
