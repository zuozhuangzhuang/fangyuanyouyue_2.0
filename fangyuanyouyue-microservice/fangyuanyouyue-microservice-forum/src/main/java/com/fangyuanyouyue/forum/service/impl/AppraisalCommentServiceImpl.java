package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.AppraisalCommentLikesMapper;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.model.*;
import com.fangyuanyouyue.forum.service.AppraisalCommentLikesService;
import com.fangyuanyouyue.forum.service.SchedualMessageService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.AppraisalCommentMapper;
import com.fangyuanyouyue.forum.dto.AppraisalCommentDto;
import com.fangyuanyouyue.forum.param.AppraisalParam;
import com.fangyuanyouyue.forum.service.AppraisalCommentService;
import org.springframework.transaction.annotation.Transactional;


@Service(value = "appraisalCommentService")
@Transactional(rollbackFor=Exception.class)
public class AppraisalCommentServiceImpl implements AppraisalCommentService {
	
	@Autowired
	AppraisalCommentMapper appraisalCommentMapper;
	@Autowired
	private SchedualUserService schedualUserService;
	@Autowired
	private AppraisalDetailMapper appraisalDetailMapper;
	@Autowired
	private AppraisalCommentLikesService appraisalCommentLikesService;
	@Autowired
	private AppraisalCommentLikesMapper appraisalCommentLikesMapper;
	@Autowired
	private SchedualMessageService schedualMessageService;


	@Override
	public Integer countComment(Integer id) throws ServiceException {
		return appraisalCommentMapper.countById(id);
	}

	@Override
	public List<AppraisalCommentDto> getAppraisalCommentList(Integer userId,Integer appraisalId, Integer start, Integer limit)
			throws ServiceException {
		List<AppraisalComment> list = appraisalCommentMapper.selectByAppraisalId(appraisalId, start*limit, limit);
		List<AppraisalCommentDto> dtos = AppraisalCommentDto.toDtoList(list);
		for(AppraisalCommentDto dto:dtos){
			//是否点赞
			AppraisalCommentLikes appraisalCommentLikes = appraisalCommentLikesMapper.selectByCommentIdUserId(dto.getCommentId(),userId);
			if(appraisalCommentLikes != null){
				dto.setIsLikes(StatusEnum.YES.getValue());
			}
			//点赞数量
			Integer likesCount = appraisalCommentLikesService.countCommentLikes(dto.getCommentId());
			dto.setLikesCount(likesCount);
		}
		return dtos;
	}

	@Override
	public AppraisalCommentDto saveComment(Integer userId,AppraisalParam param) throws ServiceException{
		AppraisalComment model = appraisalCommentMapper.selectByAppraisalIdUserId(userId,param.getAppraisalId());
		if(model == null){
			model = new AppraisalComment();
			model.setUserId(userId);
			model.setAppraisalId(param.getAppraisalId());
			model.setViewpoint(param.getViewpoint());
			model.setContent(param.getContent());
			model.setAddTime(DateStampUtils.getTimesteamp());
			appraisalCommentMapper.insert(model);
			if(param.getUserIds() != null && param.getUserIds().length > 0){
				//邀请我：用户“用户昵称”参与全民鉴定【全民鉴定名称】时邀请了您！点击此处前往查看吧
				UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
				AppraisalDetail appraisalDetail = appraisalDetailMapper.selectDetailByPrimaryKey(param.getAppraisalId());
				for(Integer toUserId:param.getUserIds()){
					schedualMessageService.easemobMessage(toUserId.toString(),
							"用户“"+user.getNickName()+"”参与全民鉴定【"+appraisalDetail.getTitle()+"】时邀请了您！点击此处前往查看吧", Status.INVITE_MESSAGE.getMessage(),Status.JUMP_TYPE_APPRAISAL.getMessage(),appraisalDetail.getId().toString());
				}
			}
			AppraisalCommentDto dto = new AppraisalCommentDto(model);
			return dto;
		}else{
			throw new ServiceException("您只能发表一次看法！");
		}
	}

	@Override
	public Integer countComment(Integer appraisalId, Integer viewpoint) throws ServiceException {

		return appraisalCommentMapper.countByViewPoint(appraisalId, viewpoint);
	}

   
	
}
