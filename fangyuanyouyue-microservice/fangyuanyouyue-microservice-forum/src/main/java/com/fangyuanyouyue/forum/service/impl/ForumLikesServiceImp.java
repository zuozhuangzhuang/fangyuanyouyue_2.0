package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.forum.dao.ForumLikesMapper;
import com.fangyuanyouyue.forum.dto.ForumLikesDto;
import com.fangyuanyouyue.forum.model.ForumLikes;
import com.fangyuanyouyue.forum.service.ForumLikesService;
import com.fangyuanyouyue.forum.utils.ServiceException;


@Service(value = "forumLikesService")
public class ForumLikesServiceImp implements ForumLikesService {

    @Autowired
    private ForumLikesMapper forumLikesMapper;
    
	public Integer countLikes(Integer forumId) {
		return forumLikesMapper.countById(forumId);
	}

	@Override
	public List<ForumLikesDto> getLikesList(Integer forumId, Integer start, Integer limit) throws ServiceException {
		List<ForumLikes> list = forumLikesMapper.selectByForumId(forumId, start, limit);
		return ForumLikesDto.toDtoList(list);
	}

	@Override
	public void saveLikes(Integer type, Integer userId, Integer forumId) {
		ForumLikes model = new ForumLikes();
		model.setUserId(userId);
		model.setForumId(forumId);
		model.setAddTime(new Date());
		forumLikesMapper.insert(model);
	}

}
