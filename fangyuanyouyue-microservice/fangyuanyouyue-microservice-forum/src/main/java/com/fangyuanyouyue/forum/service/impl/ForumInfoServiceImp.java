package com.fangyuanyouyue.forum.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.dao.ForumInfoMapper;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.model.ForumInfo;
import com.fangyuanyouyue.forum.service.ForumCommentService;
import com.fangyuanyouyue.forum.service.ForumInfoService;
import com.fangyuanyouyue.forum.service.ForumLikesService;
import com.fangyuanyouyue.forum.service.ForumPvService;


@Service(value = "forumInfoService")
public class ForumInfoServiceImp implements ForumInfoService {


	@Autowired
	private ForumInfoMapper forumInfoMapper;
	@Autowired
	private ForumCommentService forumCommentService;
	@Autowired
	private ForumLikesService forumLikesService;
	@Autowired
	private ForumColumnMapper forumColumnMapper;
	@Autowired
	private ForumPvService forumPvService;

	@Override
	public ForumInfoDto getForumInfoById(Integer forumId,Integer userId) throws ServiceException {

		ForumInfo forumInfo = forumInfoMapper.selectDetailByPrimaryKey(forumId);

		if(forumInfo!=null) {
			ForumInfoDto dto = new ForumInfoDto(forumInfo);
			//计算点赞数
			Integer	 likesCount = forumLikesService.countLikes(forumId);
			dto.setLikesCount(likesCount);
			
			//计算评论数
			Integer commentCount = forumCommentService.countComment(forumId);
			dto.setCommentCount(commentCount);
			
			//增加浏览记录
			if(userId!=null) {

				forumPvService.savePv(userId, forumId);
			}

			Integer pvCount = forumPvService.countPv(forumId);
			dto.setViewCount(pvCount);
			
			return dto;
		}

		return null;
	}

	@Override
	public List<ForumInfoDto> getForumList(Integer columnId,Integer userId, Integer type, String keyword, Integer start, Integer limit)
			throws ServiceException {
		List<ForumInfo> list = forumInfoMapper.selectList(columnId,userId,type,keyword, start, limit);
		List<ForumInfoDto> dtos = new ArrayList<ForumInfoDto>();
		for(ForumInfo info:list) {
			ForumInfoDto dto = new ForumInfoDto(info);
			//计算点赞数
			Integer	 likesCount = forumLikesService.countLikes(info.getId());
			dto.setLikesCount(likesCount);
			
			//计算评论数
			Integer commentCount = forumCommentService.countComment(info.getId());
			dto.setCommentCount(commentCount);

			//浏览量
			Integer pvCount = forumPvService.countPv(info.getId());
			dto.setViewCount(pvCount);
			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public void addForum(Integer userId, Integer columnId, String title, String content,String videoUrl,Integer videoLength, String videoImg, Integer type,Integer[] userIds) throws ServiceException {
		ForumInfo forumInfo = new ForumInfo();
		forumInfo.setUserId(userId);
		forumInfo.setTitle(title);
		forumInfo.setContent(content);
		forumInfo.setType(type);//帖子类型 1图文 2视频
		forumInfo.setStatus(1);//状态 1显示 2隐藏
		forumInfo.setAddTime(DateStampUtils.getTimesteamp());
		if(type == 1){//帖子
			//获取专栏
			if(columnId == null){
				throw new ServiceException("专栏id为空！");
			}else{
				ForumColumn forumColumn = forumColumnMapper.selectByPrimaryKey(columnId);
				if(forumColumn == null){
					throw new ServiceException("专栏异常！");
				}else{
					forumInfo.setColumnId(columnId);
				}
			}
		}else if(type == 2){//视频
			if(StringUtils.isEmpty(videoUrl) || StringUtils.isEmpty(videoImg) || videoLength == null){
				throw new ServiceException("参数异常！");
			}
			forumInfo.setVideoUrl(videoUrl);
			forumInfo.setVideoLength(videoLength);
			forumInfo.setVideoImg(videoImg);
		}else{
			throw new ServiceException("类型错误！");
		}
		forumInfoMapper.insert(forumInfo);
		if(userIds != null && userIds.length > 0){
			//TODO 发送通知邀请用户
			for(Integer toUserId:userIds){

			}
		}
	}
}
