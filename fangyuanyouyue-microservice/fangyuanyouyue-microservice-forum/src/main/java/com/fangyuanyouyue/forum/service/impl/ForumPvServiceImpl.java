package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.forum.dao.ForumPvMapper;
import com.fangyuanyouyue.forum.model.ForumPv;
import com.fangyuanyouyue.forum.service.ForumPvService;
import org.springframework.transaction.annotation.Transactional;


@Service(value = "forumPvService")
@Transactional(rollbackFor=Exception.class)
public class ForumPvServiceImpl implements ForumPvService {
	
	@Autowired
	private ForumPvMapper forumPvsMapper;


	@Override
	public Integer countPv(Integer forumId) {
		return forumPvsMapper.countById(forumId);
	}

	@Override
	public void savePv(Integer userId, Integer forumId,Integer columnId) {
		ForumPv model = forumPvsMapper.selectByUserIdColumnId(userId,forumId);
		if(model == null){
			model = new ForumPv();
			model.setAddTime(new Date());
			model.setUserId(userId);
			model.setForumId(forumId);
			model.setColumnId(columnId);
			forumPvsMapper.insert(model);
		}
	}

   
	
}
