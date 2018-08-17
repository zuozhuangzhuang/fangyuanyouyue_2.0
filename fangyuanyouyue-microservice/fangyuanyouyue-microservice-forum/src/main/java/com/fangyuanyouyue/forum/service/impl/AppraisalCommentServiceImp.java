package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.AppraisalCommentLikesMapper;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.model.*;
import com.fangyuanyouyue.forum.service.AppraisalCommentLikesService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.AppraisalCommentMapper;
import com.fangyuanyouyue.forum.dto.AppraisalCommentDto;
import com.fangyuanyouyue.forum.param.AppraisalParam;
import com.fangyuanyouyue.forum.service.AppraisalCommentService;


@Service(value = "appraisalCommentService")
public class AppraisalCommentServiceImp implements AppraisalCommentService {
	
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
			Integer likesCount = appraisalCommentLikesService.countCommentLikes(appraisalId);
			dto.setLikesCount(likesCount);
		}
		return dtos;
	}

	@Override
	public void saveComment(Integer userId,AppraisalParam param) throws ServiceException{
		AppraisalComment model = appraisalCommentMapper.selectByUserId(userId);
		if(model != null){
			model = new AppraisalComment();
			model.setUserId(userId);
			model.setAppraisalId(param.getAppraisalId());
			model.setViewpoint(param.getViewpoint());
			model.setContent(param.getContent());
			model.setAddTime(new Date());
			appraisalCommentMapper.insert(model);
			if(param.getUserIds() != null && param.getUserIds().length > 0){
				//TODO 邀请我：用户“用户昵称”参与全民鉴定【全民鉴定名称】时邀请了您！点击此处前往查看吧
				UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
				AppraisalDetail appraisalDetail = appraisalDetailMapper.selectByPrimaryKey(param.getAppraisalId());
				for(Integer toUserId:param.getUserIds()){
					UserInfo toUser = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(toUserId)).getString("data")), UserInfo.class);
					System.out.println("用户“"+user.getNickName()+"”参与全民鉴定【"+appraisalDetail.getTitle()+"】时邀请了“"+toUser.getNickName()+"“！");
				}
			}
		}else{
			throw new ServiceException("您只能发表一次看法！");
		}
	}

	@Override
	public Integer countComment(Integer appraisalId, Integer viewpoint) throws ServiceException {

		return appraisalCommentMapper.countByViewPoint(appraisalId, viewpoint);
	}

   
	
}
