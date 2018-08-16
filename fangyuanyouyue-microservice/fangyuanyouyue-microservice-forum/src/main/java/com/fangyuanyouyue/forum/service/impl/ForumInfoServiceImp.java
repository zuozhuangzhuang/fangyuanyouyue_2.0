package com.fangyuanyouyue.forum.service.impl;

import java.util.List;

import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.forum.dao.ForumColumnMapper;
import com.fangyuanyouyue.forum.model.ForumColumn;
import org.apache.commons.lang.StringUtils;
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
	@Autowired
	private ForumColumnMapper forumColumnMapper;

	@Override
	public ForumInfoDto getForumInfoById(Integer id) throws ServiceException {

		ForumInfo forumInfo = forumInfoMapper.selectDetailByPrimaryKey(id);

		if(forumInfo!=null) {
			ForumInfoDto dto = new ForumInfoDto(forumInfo);
			//计算点赞数
			Integer	 likesCount = forumLikesService.countLikes(id);
			dto.setLikesCount(likesCount);
			//计算评论数
			Integer commentCount = forumCommentService.countComment(id);
			dto.setCommentCount(commentCount);

			//TODO 如果不是视频，需要找到图片

			return dto;
		}

		return null;
	}

	@Override
	public List<ForumInfoDto> getForumList(Integer columnId,Integer userId, Integer type, String keyword, Integer start, Integer limit)
			throws ServiceException {
		List<ForumInfo> list = forumInfoMapper.selectList(columnId,userId,type,keyword, start, limit);

		return ForumInfoDto.toDtoList(list);
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
				//TODO 社交消息：您的专栏【专栏标题】有新的帖子，点击此处前往查看吧
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
			//TODO 发送通知邀请用户 邀请我：用户“用户昵称”上传帖子【帖子名称】时邀请了您！点击此处前往查看吧
			//邀请我：用户“用户昵称”上传视频【视频名称】时邀请了您！点击此处前往查看吧
			for(Integer toUserId:userIds){

			}
		}
	}
}
