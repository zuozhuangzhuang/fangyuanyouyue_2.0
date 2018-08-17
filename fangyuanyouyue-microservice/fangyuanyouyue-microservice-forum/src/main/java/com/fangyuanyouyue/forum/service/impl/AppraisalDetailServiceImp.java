package com.fangyuanyouyue.forum.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.constants.StatusEnum;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.dao.AppraisalImgMapper;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.model.AppraisalImg;
import com.fangyuanyouyue.forum.service.AppraisalDetailService;


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

	@Override
	public AppraisalDetailDto getAppraisalDetail(Integer userId,Integer appraisalId) throws ServiceException {
		AppraisalDetail model = appraisalDetailMapper.selectByPrimaryKey(appraisalId);
		AppraisalDetailDto dto = new AppraisalDetailDto(model);

		Integer commentCount = appraisalCommentServiceImp.countComment(appraisalId);
		dto.setCommentCount(commentCount);

		Integer likesCount = appraisalLikesServiceImp.countLikes(appraisalId);
		dto.setLikesCount(likesCount);

		Integer viewCount = appraisalPvServiceImp.countPv(appraisalId);
		dto.setViewCount(viewCount);

		Integer truthCount = appraisalCommentServiceImp.countComment(appraisalId,StatusEnum.YES.getValue());
		Integer untruthCount = appraisalCommentServiceImp.countComment(appraisalId,StatusEnum.NO.getValue());
		dto.setTruthCount(truthCount);
		dto.setUntruthCount(untruthCount);

		if(userId!=null) {
			//TODO 需要判断
			dto.setIsCollect(StatusEnum.NO.getValue());
			dto.setIsFans(StatusEnum.NO.getValue());
			dto.setIsLikes(StatusEnum.NO.getValue());
		}

		return dto;
	}

	@Override
	public List<AppraisalDetailDto> getAppraisalList(Integer userId,String keyword,Integer start, Integer limit) throws ServiceException {
		List<AppraisalDetail> list = appraisalDetailMapper.selectList(userId, keyword, start, limit);
		List<AppraisalDetailDto> dtos = new ArrayList<AppraisalDetailDto>();
		for(AppraisalDetail model:list) {
			AppraisalDetailDto dto = new AppraisalDetailDto(model);
			Integer commentCount = appraisalCommentServiceImp.countComment(model.getId());
			dto.setCommentCount(commentCount);

			Integer likesCount = appraisalLikesServiceImp.countLikes(model.getId());
			dto.setLikesCount(likesCount);

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
