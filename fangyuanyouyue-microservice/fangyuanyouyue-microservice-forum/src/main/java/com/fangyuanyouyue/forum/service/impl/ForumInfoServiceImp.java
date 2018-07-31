package com.fangyuanyouyue.forum.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.ForumInfoMapper;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.model.ForumInfo;
import com.fangyuanyouyue.forum.service.ForumCommentService;
import com.fangyuanyouyue.forum.service.ForumInfoService;
import com.fangyuanyouyue.forum.service.ForumLikesService;


@Service(value = "forumInfoService")
public class ForumInfoServiceImp implements ForumInfoService {


    @Autowired
    private ForumInfoMapper forumInfoMapper;
    @Autowired
    private ForumCommentService forumCommentService;
    @Autowired
    private ForumLikesService forumLikesService;
    
	@Override
	public ForumInfoDto getForumInfoById(Integer id) throws ServiceException {
		
		ForumInfo forumInfo = forumInfoMapper.selectDetailByPrimaryKey(id);
		
		if(forumInfo!=null) {
			ForumInfoDto dto = new ForumInfoDto(forumInfo);
			//计算点赞数
			long likesCount = forumLikesService.countLikes(id);
			dto.setLikesCount(likesCount);
			//计算评论数
			long commentCount = forumCommentService.countComment(id);
			dto.setCommentCount(commentCount);
			
			//TODO 如果不是视频，需要找到图片
			
			return dto;
		}
		
		return null;
	}

	@Override
	public List<ForumInfoDto> getForumList(Integer columnId, Integer start, Integer limit)
			throws ServiceException {
		List<ForumInfo> list = forumInfoMapper.selectList(columnId, start, limit);

		return ForumInfoDto.toDtoList(list);
	}


}
