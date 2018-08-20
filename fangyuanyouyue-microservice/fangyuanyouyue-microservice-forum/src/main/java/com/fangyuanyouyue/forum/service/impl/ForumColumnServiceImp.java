package com.fangyuanyouyue.forum.service.impl;

import java.util.List;

import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.dao.ForumColumnTypeMapper;
import com.fangyuanyouyue.forum.model.ForumColumnType;
import com.fangyuanyouyue.forum.service.SchedualMessageService;
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
    @Autowired
	private ForumColumnTypeMapper forumColumnTypeMapper;
    @Autowired
	private SchedualMessageService schedualMessageService;

	@Override
	public List<ForumColumnTypeDto> getColumnList(Integer start, Integer limit) throws ServiceException {
		List<ForumColumn> list = forumColumnMapper.selectPage(start*limit, limit);
		List<ForumColumnDto> dtos = ForumColumnDto.toDtoList(list);
		return ForumColumnTypeDto.toDtoList(dtos);
	}

	@Override
	public List<ForumColumnDto> getChosenColumnList(Integer start,Integer limit) throws ServiceException {
		List<ForumColumn> list = forumColumnMapper.selectChosen(start*limit,limit,1);
		return ForumColumnDto.toDtoList(list);
	}


	@Override
	public List<ForumColumnTypeDto> getForumTypeList() throws ServiceException {
		List<ForumColumnType> typeList = forumColumnTypeMapper.getAll();
		return ForumColumnTypeDto.toDtoListByType(typeList);
	}

	@Override
	public void addColumn(Integer userId, Integer typeId,String name) throws ServiceException {
		ForumColumnType forumColumnType = forumColumnTypeMapper.selectByPrimaryKey(typeId);
		if(forumColumnType == null){
			throw new ServiceException("分类异常！");
		}else{
			ForumColumn forumColumn = forumColumnMapper.selectByUserId(userId);
			if(forumColumn != null){
				throw new ServiceException("每位用户只能申请一个专栏！");
			}else{
				forumColumn = new ForumColumn();
				forumColumn.setUserId(userId);
				forumColumn.setName(name);
				forumColumn.setFansCount(0);
				forumColumn.setAddTime(DateStampUtils.getTimesteamp());
				forumColumn.setIsChosen(2);//是否精选1是 2否
				forumColumn.setTypeId(typeId);
				forumColumnMapper.insert(forumColumn);
			}
		}
		//系统消息：您的【专栏名称】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知
		schedualMessageService.easemobMessage(userId.toString(),
				"您的【"+name+"】专栏申请已提交，将于3个工作日内完成审核，请注意消息通知","1","");
	}
}
