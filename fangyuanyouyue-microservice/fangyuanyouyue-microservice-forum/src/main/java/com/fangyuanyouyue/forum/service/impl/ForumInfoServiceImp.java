package com.fangyuanyouyue.forum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.CollectMapper;
import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.dao.ForumInfoMapper;
import com.fangyuanyouyue.forum.dao.ForumLikesMapper;
import com.fangyuanyouyue.forum.dto.ForumInfoDto;
import com.fangyuanyouyue.forum.dto.ForumLikesDto;
import com.fangyuanyouyue.forum.model.*;
import com.fangyuanyouyue.forum.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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
	@Autowired
	private SchedualUserService schedualUserService;
	@Autowired
	private CollectMapper collectMapper;
	@Autowired
	private ForumLikesMapper forumLikesMapper;
	@Autowired
	private SchedualMessageService schedualMessageService;

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
				forumPvService.savePv(userId, forumId,forumInfo.getColumnId());
				//是否点赞
				ForumLikes forumLikes = forumLikesMapper.selectByForumIdUserId(dto.getForumId(), userId);
				if(forumLikes != null){
					dto.setIsLikes(StatusEnum.YES.getValue());
				}
				//是否收藏
				Collect collect = collectMapper.selectByCollectIdType(userId, forumId, forumInfo.getType() == 2?3:4);
				if(collect != null){
					dto.setIsCollect(StatusEnum.YES.getValue());
				}
				//是否关注作者
				boolean isFans = JSONObject.parseObject(schedualUserService.isFans(userId,forumInfo.getUserId())).getBoolean("data");
				if(isFans){
					dto.setIsFans(StatusEnum.YES.getValue());
				}
			}
			//浏览量
			Integer pvCount = forumPvService.countPv(forumId);
			dto.setViewCount(pvCount+forumInfo.getPvCount());
			return dto;
		}else{
			throw new ServiceException("获取信息失败！");
		}
	}

	@Override
	public List<ForumInfoDto> getForumList(Integer columnId,Integer userId, Integer type, String keyword, Integer start, Integer limit,Integer listType,Integer searchType) throws ServiceException {
		List<ForumInfo> list;
		if(listType.intValue() == 1){
			//普通列表，需要判断是否点赞/收藏
			list = forumInfoMapper.selectList(columnId,null,type,keyword, start*limit, limit,searchType);
		}else if(listType.intValue() == 2){
//			if(userId == null){
//				throw new ServiceException("用户信息为空！");
//			}
			//我的xx列表
			list = forumInfoMapper.selectList(columnId,userId,type,keyword, start*limit, limit,searchType);
		}else{
			throw new ServiceException("列表类型错误！");
		}
		List<ForumInfoDto> dtos = new ArrayList<>();
		for(ForumInfo info:list) {
			ForumInfoDto dto = new ForumInfoDto(info);
			//计算点赞数
			Integer likesCount = forumLikesService.countLikes(info.getId());
			dto.setLikesCount(likesCount);
			//计算评论数
			Integer commentCount = forumCommentService.countComment(info.getId());
			dto.setCommentCount(commentCount);
			if(userId != null){
				//是否收藏
				Collect collect = collectMapper.selectByCollectIdType(userId, info.getId(), info.getType() == 2?3:4);
				if(collect != null){
					dto.setIsCollect(StatusEnum.YES.getValue());
				}
				//是否点赞
				ForumLikes forumLikes = forumLikesMapper.selectByForumIdUserId(dto.getForumId(), userId);
				if(forumLikes != null){
					dto.setIsLikes(StatusEnum.YES.getValue());
				}
			}
			//浏览量
			Integer pvCount = forumPvService.countPv(info.getId());
			dto.setViewCount(pvCount+info.getPvCount());
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
				forumInfo.setPvCount(0);
				forumInfoMapper.insert(forumInfo);
				//社交消息：您的专栏【专栏标题】有新的帖子，点击此处前往查看吧
				schedualMessageService.easemobMessage(forumColumn.getUserId().toString(),
						"您的专栏【"+forumColumn.getName()+"】有新的帖子，点击此处前往查看吧","5","3",forumInfo.getId().toString());
			}
		}else if(type == 2){//视频
			if(StringUtils.isEmpty(videoUrl) || StringUtils.isEmpty(videoImg) || videoLength == null){
				throw new ServiceException("参数异常！");
			}
			forumInfo.setVideoUrl(videoUrl);
			forumInfo.setVideoLength(videoLength);
			forumInfo.setVideoImg(videoImg);
			forumInfo.setPvCount(0);
			forumInfoMapper.insert(forumInfo);
		}else{
			throw new ServiceException("类型错误！");
		}
		if(userIds != null && userIds.length > 0){
			//邀请我：用户“用户昵称”上传帖子【帖子名称】时邀请了您！点击此处前往查看吧
			//邀请我：用户“用户昵称”上传视频【视频名称】时邀请了您！点击此处前往查看吧
			UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
			for(Integer toUserId:userIds){
				schedualMessageService.easemobMessage(toUserId.toString(),
						"用户“"+user.getNickName()+"”上传"+(forumInfo.getType()==1?"帖子【":"视频【")+forumInfo.getTitle()+"】时邀请了您！点击此处前往查看吧",forumInfo.getType()==1?"5":"4","5",forumInfo.getId().toString());
			}
		}
	}
}
