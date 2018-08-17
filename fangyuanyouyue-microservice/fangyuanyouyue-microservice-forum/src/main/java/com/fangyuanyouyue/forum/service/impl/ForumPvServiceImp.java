package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.forum.dao.ForumPvMapper;
import com.fangyuanyouyue.forum.model.ForumPv;
import com.fangyuanyouyue.forum.service.ForumPvService;


@Service(value = "forumPvService")
public class ForumPvServiceImp implements ForumPvService {
	
	@Autowired
	private ForumPvMapper ForumPvsMapper;

	@Override
	public Integer countPv(Integer forumId) {
		return ForumPvsMapper.countById(forumId);
	}

	@Override
	public void savePv(Integer userId, Integer forumId) {
		ForumPv model = new ForumPv();
		model.setAddTime(new Date());
		model.setUserId(userId);
		model.setForumId(forumId);
		
		ForumPvsMapper.insert(model);
		
	}

   
	
}
