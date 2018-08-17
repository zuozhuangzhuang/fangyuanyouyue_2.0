package com.fangyuanyouyue.forum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.dao.AppraisalImgMapper;
import com.fangyuanyouyue.forum.dao.AppraisalLikesMapper;
import com.fangyuanyouyue.forum.dao.CollectMapper;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.model.AppraisalImg;
import com.fangyuanyouyue.forum.model.AppraisalLikes;
import com.fangyuanyouyue.forum.model.Collect;
import com.fangyuanyouyue.forum.service.AppraisalDetailService;
import com.fangyuanyouyue.forum.service.SchedualUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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


	@Override
	public AppraisalDetailDto getAppraisalDetail(Integer userId,Integer appraisalId) throws ServiceException {
		AppraisalDetail model = appraisalDetailMapper.selectByPrimaryKey(appraisalId);
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
		}

		return dto;
	}

	@Override
	public List<AppraisalDetailDto> getAppraisalList(Integer userId,String keyword,Integer start, Integer limit) throws ServiceException {
		List<AppraisalDetail> list = appraisalDetailMapper.selectList(userId, keyword, start, limit);
		List<AppraisalDetailDto> dtos = new ArrayList<>();
		//列表不需要返回点赞数、浏览量、评论量
		for(AppraisalDetail model:list) {
			AppraisalDetailDto dto = new AppraisalDetailDto(model);
			//是否收藏
			Collect collect = collectMapper.selectByCollectIdType(userId, model.getId(), 5);
			if(collect != null){
				dto.setIsCollect(StatusEnum.YES.getValue());
			}
			//TODO 参与鉴定用户头像列表
			List<String> headImgUrls = new ArrayList<>();

			dto.setHeadImgUrls(headImgUrls);
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
		//TODO 邀请我：用户“用户昵称”发起全民鉴定【全名鉴定名称】时邀请了您！点击此处前往查看吧
		for(Integer toUserId:userIds){

		}
	}
	
}
