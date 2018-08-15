package com.fangyuanyouyue.forum.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.dao.AppraisalImgMapper;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.model.AppraisalImg;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dto.AppraisalDetailDto;
import com.fangyuanyouyue.forum.service.AppraisalDetailService;


@Service(value = "appraisalDetailService")
public class AppraisalDetailServiceImp implements AppraisalDetailService {

	@Autowired
	private AppraisalDetailMapper appraisalDetailMapper;
	@Autowired
	private AppraisalImgMapper appraisalImgMapper;

	@Override
	public AppraisalDetailDto getAppraisalDetail(Integer id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AppraisalDetailDto> getAppraisalList(Integer start, Integer limit) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAppraisal(Integer userId, BigDecimal bonus, String title, String content,String[] imgUrls) throws ServiceException {
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
	}
	
}
