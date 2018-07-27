package com.fangyuanyouyue.forum.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.service.ForumColumnService;
import com.fangyuanyouyue.forum.utils.ServiceException;


@Service(value = "forumColumnService")
public class ForumColumnServiceImp implements ForumColumnService {

    @Autowired
    private ForumColumnMapper forumColumnMapper;

	@Override
	public List<ForumColumnDto> getColumnList(Integer start, Integer limit) throws ServiceException {
		List<ForumColumn> list = forumColumnMapper.selectPage(start, limit);
		return ForumColumnDto.toDtoList(list);
	}
    

}
