package com.fangyuanyouyue.forum.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.dto.ForumColumnTypeDto;
import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.service.ForumColumnService;


@Service(value = "forumColumnService")
public class ForumColumnServiceImp implements ForumColumnService {

    @Autowired
    private ForumColumnMapper forumColumnMapper;

	@Override
	public List<ForumColumnTypeDto> getColumnList(Integer start, Integer limit) throws ServiceException {
		List<ForumColumn> list = forumColumnMapper.selectPage(start, limit);
		List<ForumColumnDto> dtos = ForumColumnDto.toDtoList(list);
		return ForumColumnTypeDto.toDtoList(dtos);
	}

	@Override
	public List<ForumColumnDto> getChosenColumnList() throws ServiceException {
		List<ForumColumn> list = forumColumnMapper.selectChosen(1);
		return ForumColumnDto.toDtoList(list);
	}
    

}
