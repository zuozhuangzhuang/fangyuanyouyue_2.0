package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.ForumLikesMapper;
import com.fangyuanyouyue.forum.dto.ForumLikesDto;
import com.fangyuanyouyue.forum.model.ForumLikes;
import com.fangyuanyouyue.forum.service.ForumLikesService;


@Service(value = "forumLikesService")
public class ForumLikesServiceImpl implements ForumLikesService {

    @Autowired
    private ForumLikesMapper forumLikesMapper;
    
	@Override
	public Integer countLikes(Integer forumId) {
		return forumLikesMapper.countById(forumId);
	}

	@Override
	public List<ForumLikesDto> getLikesList(Integer forumId, Integer start, Integer limit) throws ServiceException {
		List<ForumLikes> list = forumLikesMapper.selectByForumId(forumId, start*limit, limit);
		return ForumLikesDto.toDtoList(list);
	}

	@Override
	public void saveLikes(Integer type, Integer userId, Integer forumId) throws ServiceException{
		ForumLikes model = forumLikesMapper.selectByForumIdUserId(forumId,userId);
		if(type == 1){
			if(model != null){
				throw new ServiceException("您已点赞，请勿重复点赞！");
			}else{
				model = new ForumLikes();
				model.setUserId(userId);
				model.setForumId(forumId);
				model.setAddTime(new Date());
				forumLikesMapper.insert(model);
			}
		}else if(type == 2){
			if(model == null){
				throw new ServiceException("未点赞，请先点赞！");
			}else{
				forumLikesMapper.deleteByPrimaryKey(model.getId());
			}
		}else{
			throw new ServiceException("类型错误！");
		}

	}

}
