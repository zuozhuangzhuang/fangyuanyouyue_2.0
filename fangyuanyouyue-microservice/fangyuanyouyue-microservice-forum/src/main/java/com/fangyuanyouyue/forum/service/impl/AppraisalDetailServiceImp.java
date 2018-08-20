package com.fangyuanyouyue.forum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.*;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.dto.AppraisalImgDto;
import com.fangyuanyouyue.forum.model.*;
import com.fangyuanyouyue.forum.service.AppraisalDetailService;
import com.fangyuanyouyue.forum.service.SchedualMessageService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.I;


@Service(value = "appraisalDetailService")
public class AppraisalDetailServiceImp implements AppraisalDetailService {

	@Autowired
	private AppraisalDetailMapper appraisalDetailMapper;
	@Autowired
	private AppraisalImgMapper appraisalImgMapper;
	@Autowired
	private AppraisalCommentServiceImp appraisalCommentServiceImp;
	@Autowired
	private AppraisalLikesServiceImp appraisalLikesServiceImp;
	@Autowired
	private AppraisalPvServiceImp appraisalPvServiceImp;
	@Autowired
	private CollectMapper collectMapper;
	@Autowired
	private SchedualUserService schedualUserService;
	@Autowired
	private AppraisalLikesMapper appraisalLikesMapper;
	@Autowired
	AppraisalCommentMapper appraisalCommentMapper;
	@Autowired
	private SchedualMessageService schedualMessageService;


	@Override
	public AppraisalDetailDto getAppraisalDetail(Integer userId,Integer appraisalId) throws ServiceException {
		AppraisalDetail model = appraisalDetailMapper.selectDetailByPrimaryKey(appraisalId);
		AppraisalDetailDto dto = new AppraisalDetailDto(model);
		//评论数量
		Integer commentCount = appraisalCommentServiceImp.countComment(appraisalId);
		dto.setCommentCount(commentCount);
		//点赞数量
		Integer likesCount = appraisalLikesServiceImp.countLikes(appraisalId);
		dto.setLikesCount(likesCount);
		//浏览量
		Integer viewCount = appraisalPvServiceImp.countPv(appraisalId);
		dto.setViewCount(viewCount+model.getPvCount());
		//看真数量
		Integer truthCount = appraisalCommentServiceImp.countComment(appraisalId,StatusEnum.YES.getValue());
		//看假数量
		Integer untruthCount = appraisalCommentServiceImp.countComment(appraisalId,StatusEnum.NO.getValue());
		dto.setTruthCount(truthCount);
		dto.setUntruthCount(untruthCount);

		if(userId!=null) {
			//增加浏览量
			appraisalPvServiceImp.savePv(userId,appraisalId);
			//是否收藏
			Collect collect = collectMapper.selectByCollectIdType(userId, appraisalId, 5);
			if(collect != null){
				dto.setIsCollect(StatusEnum.YES.getValue());
			}
//			//是否关注作者
//			boolean isFans = JSONObject.parseObject(schedualUserService.isFans(userId,model.getUserId())).getBoolean("data");
//			if(isFans){
//				dto.setIsFans(StatusEnum.YES.getValue());
//			}
			//是否点赞
			AppraisalLikes appraisalLikes = appraisalLikesMapper.selectByAppraisalIdUserId(appraisalId, userId);
			if(appraisalLikes != null){
				dto.setIsLikes(StatusEnum.YES.getValue());
			}
			//浏览量
			Integer pvCount = appraisalPvServiceImp.countPv(appraisalId);
			dto.setViewCount(pvCount+model.getPvCount());
			AppraisalComment comment = appraisalCommentMapper.selectByAppraisalIdUserId(userId, appraisalId);
			if(comment != null){
				//用户观点
				dto.setViewpoint(comment.getViewpoint());
				//是否获胜
				dto.setIsWinner(comment.getIsWinner());
			}
		}
		//鉴定图片列表
		List<AppraisalImg> appraisalImgs = appraisalImgMapper.selectListByAppraisal(appraisalId);
		dto.setImgDtos(AppraisalImgDto.toDtoList(appraisalImgs));
		return dto;
	}

	@Override
	public List<AppraisalDetailDto> getAppraisalList(Integer userId,String keyword,Integer start, Integer limit,Integer type) throws ServiceException {
		List<AppraisalDetail> list;
		if(type == null){
			list = appraisalDetailMapper.selectMyList(null, keyword, start, limit);
		}else{
			if(userId == null){
				throw new ServiceException("用户id不能为空！");
			}
			if(type == 1){
				list = appraisalDetailMapper.selectListWithMe(userId, keyword, start, limit);
			}else if(type == 2){
				//userId 不为空：我的全民鉴定列表
				list = appraisalDetailMapper.selectMyList(userId, keyword, start, limit);
			}else{
				throw new ServiceException("类型错误！");
			}
		}
		List<AppraisalDetailDto> dtos = new ArrayList<>();
		//列表不需要返回点赞数、浏览量、评论量
		for(AppraisalDetail model:list) {
			AppraisalDetailDto dto = new AppraisalDetailDto(model);
			//是否收藏
			Collect collect = collectMapper.selectByCollectIdType(userId, model.getId(), 5);
			if(collect != null){
				dto.setIsCollect(StatusEnum.YES.getValue());
			}
			//参与鉴定用户头像列表
			List<String> headImgUrls = new ArrayList<>();
			List<AppraisalComment> appraisalComments = appraisalCommentMapper.selectByAppraisalId(model.getId(), 0, 5);
			if(appraisalComments != null && appraisalComments.size() > 0){
				for(AppraisalComment comment:appraisalComments){
					headImgUrls.add(comment.getHeadImgUrl());
				}
			}
			dto.setHeadImgUrls(headImgUrls);
			//鉴定图片列表
			List<AppraisalImg> appraisalImgs = appraisalImgMapper.selectListByAppraisal(model.getId());
			dto.setImgDtos(AppraisalImgDto.toDtoList(appraisalImgs));
			AppraisalComment comment = appraisalCommentMapper.selectByAppraisalIdUserId(userId,model.getId());
			if(comment != null){
				//用户观点
				dto.setViewpoint(comment.getViewpoint());
				//是否获胜
				dto.setIsWinner(comment.getIsWinner());
			}
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public void addAppraisal(Integer userId, BigDecimal bonus, String title, String content,String[] imgUrls,Integer[] userIds) throws ServiceException {
		AppraisalDetail appraisalDetail = new AppraisalDetail();
		appraisalDetail.setUserId(userId);
		appraisalDetail.setTitle(title);
		if(StringUtils.isNotEmpty(content)){
			appraisalDetail.setContent(content);
		}
		if(bonus != null && bonus.compareTo(new BigDecimal(0)) > 0){
			appraisalDetail.setBonus(bonus);
		}
		appraisalDetail.setStatus(1);//状态 1显示 2隐藏
		//结束时间为7天后
		appraisalDetail.setEndTime(DateUtil.getDateAfterDay(DateStampUtils.getTimesteamp(),7));
		appraisalDetail.setAddTime(DateStampUtils.getTimesteamp());
		appraisalDetail.setPvCount(0);
		appraisalDetailMapper.insert(appraisalDetail);
		for(String imgUrl:imgUrls){
			//存储图片
			AppraisalImg appraisalImg = new AppraisalImg();
			appraisalImg.setImgUrl(imgUrl);
			appraisalImg.setAppraisalId(appraisalDetail.getId());
			appraisalImg.setAddTime(DateStampUtils.getTimesteamp());
			appraisalImgMapper.insert(appraisalImg);
		}
		//邀请我：用户“用户昵称”发起全民鉴定【全名鉴定名称】时邀请了您！点击此处前往查看吧
		UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
		if(userIds != null && userIds.length > 0){
			for(Integer toUserId:userIds){
				schedualMessageService.easemobMessage(toUserId.toString(),
						"用户“"+user.getNickName()+"”发起全民鉴定【"+appraisalDetail.getTitle()+"】时邀请了您！点击此处前往查看吧","7",appraisalDetail.getId().toString());
			}
		}
	}

	@Override
	public void invite(Integer userId,Integer appraisalId, Integer[] userIds) throws ServiceException {
		AppraisalDetail appraisalDetail = appraisalDetailMapper.selectByPrimaryKey(appraisalId);
		//邀请我：用户“用户昵称”发起全民鉴定【全名鉴定名称】时邀请了您！点击此处前往查看吧
		UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(userId)).getString("data")), UserInfo.class);
		if(userIds != null && userIds.length > 0){
			for(Integer toUserId:userIds){
				schedualMessageService.easemobMessage(toUserId.toString(),
						"用户“"+user.getNickName()+"”看到全民鉴定【"+appraisalDetail.getTitle()+"】时邀请了您！点击此处前往查看吧","7",appraisalDetail.getId().toString());
			}
		}
	}
}
