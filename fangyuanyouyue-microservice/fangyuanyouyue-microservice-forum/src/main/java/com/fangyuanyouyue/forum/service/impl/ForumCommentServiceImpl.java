package com.fangyuanyouyue.forum.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fangyuanyouyue.forum.dao.ForumInfoMapper;
import com.fangyuanyouyue.forum.dto.MyForumCommentDto;
import com.fangyuanyouyue.forum.model.ForumInfo;
import com.fangyuanyouyue.forum.service.SchedualMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.ForumCommentLikesMapper;
import com.fangyuanyouyue.forum.dao.ForumCommentMapper;
import com.fangyuanyouyue.forum.dto.ForumCommentDto;
import com.fangyuanyouyue.forum.model.ForumComment;
import com.fangyuanyouyue.forum.service.ForumCommentService;
import org.springframework.transaction.annotation.Transactional;


@Service(value = "forumCommentService")
@Transactional(rollbackFor=Exception.class)
public class ForumCommentServiceImpl implements ForumCommentService {

    @Autowired
    private ForumCommentMapper forumCommentMapper;
    @Autowired
    private ForumCommentLikesMapper forumCommentLikesMapper;
    @Autowired
	private SchedualMessageService schedualMessageService;
    @Autowired
	private ForumInfoMapper forumInfoMapper;

	@Override
	public Integer countComment(Integer forumId) {
		return forumCommentMapper.countById(forumId);
	}

	@Override
	public List<ForumCommentDto> getCommentList(Integer userId,Integer forumId, Integer start, Integer limit) throws ServiceException {
		List<ForumComment> list = forumCommentMapper.selectByForumId(forumId, start, limit);
		List<ForumCommentDto> dtos = new ArrayList<>();
		for(ForumComment model:list) {
			ForumCommentDto dto = new ForumCommentDto(model);

			Integer likesCount = forumCommentLikesMapper.countById(dto.getCommentId());
			dto.setLikesCount(likesCount);

			Integer commentCount = forumCommentMapper.countByCommentId(dto.getCommentId());
			dto.setCommentCount(commentCount);

			if(userId!=null) {
				if(forumCommentLikesMapper.countByUserId(dto.getCommentId(), userId)>0) {
					dto.setIsLikes(StatusEnum.YES.getValue());
				}
			}
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public ForumCommentDto saveComment(Integer userId, Integer forumId, String content, Integer commentId) throws ServiceException{
		ForumInfo forumInfo = forumInfoMapper.selectDetailByPrimaryKey(forumId);
		if(forumInfo == null){
			throw new ServiceException("找不到帖子或视频！");
		}
		ForumComment model = new ForumComment();
		model.setUserId(userId);
		model.setForumId(forumId);
		model.setUserId(userId);
		model.setAddTime(new Date());
		model.setContent(content);
		model.setStatus(StatusEnum.STATUS_NORMAL.getValue());
		model.setCommentId(commentId);
		forumCommentMapper.insert(model);
		//社交消息：您的帖子【帖子标题】有新的评论，点击此处前往查看吧
		//社交消息：您的视频【视频标题】有新的评论，点击此处前往查看吧
		if(forumInfo.getType() == 1){
			schedualMessageService.easemobMessage(forumInfo.getUserId().toString(),
					"您的帖子【"+forumInfo.getTitle()+"】有新的评论，点击此处前往查看吧","9","3",forumId.toString());

		}else{
			schedualMessageService.easemobMessage(forumInfo.getUserId().toString(),
					"您的视频【"+forumInfo.getTitle()+"】有新的评论，点击此处前往查看吧","10","3",forumId.toString());
		}
		ForumCommentDto dto = new ForumCommentDto(model);

		Integer likesCount = forumCommentLikesMapper.countById(dto.getCommentId());
		dto.setLikesCount(likesCount);

		if(userId!=null) {
			if(forumCommentLikesMapper.countByUserId(dto.getCommentId(), userId)>0) {
				dto.setIsLikes(StatusEnum.YES.getValue());
			}
		}
		return dto;
	}


	@Override
	public List<ForumCommentDto> getCommentCommentList(Integer userId,Integer commentId, Integer start, Integer limit)
			throws ServiceException {
		
		List<ForumComment> list = forumCommentMapper.selectByCommentId(commentId, start, limit);
		List<ForumCommentDto> dtos = new ArrayList<>();
		for(ForumComment model:list) {
			ForumCommentDto dto = new ForumCommentDto(model);

			Integer likesCount = forumCommentLikesMapper.countById(dto.getCommentId());
			dto.setLikesCount(likesCount);

			if(userId!=null) {
				if(forumCommentLikesMapper.countByUserId(dto.getCommentId(), userId)>0) {
					dto.setIsLikes(StatusEnum.YES.getValue());
				}
			}

			dtos.add(dto);
		}
		return dtos;

	}

	@Override
	public List<MyForumCommentDto> myComments(Integer userId, Integer statr, Integer limit, Integer type) throws ServiceException {
		List<Map> forumComments = forumCommentMapper.selectByUserId(userId, statr * limit, limit, type);
		List<MyForumCommentDto> myForumCommentDtos = MyForumCommentDto.toDtoList(forumComments);
		return myForumCommentDtos;
	}
}
