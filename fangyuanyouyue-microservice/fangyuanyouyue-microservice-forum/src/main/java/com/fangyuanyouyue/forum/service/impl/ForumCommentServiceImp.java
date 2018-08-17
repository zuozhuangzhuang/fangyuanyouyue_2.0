package com.fangyuanyouyue.forum.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.ForumCommentLikesMapper;
import com.fangyuanyouyue.forum.dao.ForumCommentMapper;
import com.fangyuanyouyue.forum.dto.ForumCommentDto;
import com.fangyuanyouyue.forum.model.ForumComment;
import com.fangyuanyouyue.forum.service.ForumCommentService;


@Service(value = "forumCommentService")
public class ForumCommentServiceImp implements ForumCommentService {

    @Autowired
    private ForumCommentMapper forumCommentMapper;
    @Autowired
    private ForumCommentLikesMapper forumCommentLikesMapper;

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
	public void saveComment(Integer userId, Integer forumId, String content, Integer commentId) throws ServiceException{
		ForumComment model = new ForumComment();
		model.setUserId(userId);
		model.setForumId(forumId);
		model.setUserId(userId);
		model.setAddTime(new Date());
		model.setContent(content);
		model.setStatus(StatusEnum.STATUS_NORMAL.getValue());
		model.setCommentId(commentId);
		forumCommentMapper.insert(model);
		//TODO 社交消息：您的帖子【帖子标题】有新的评论，点击此处前往查看吧
		//社交消息：您的视频【视频标题】有新的评论，点击此处前往查看吧
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

}
