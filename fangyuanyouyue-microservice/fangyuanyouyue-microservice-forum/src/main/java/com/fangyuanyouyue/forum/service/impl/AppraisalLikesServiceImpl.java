package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.forum.dao.AppraisalDetailMapper;
import com.fangyuanyouyue.forum.dao.AppraisalLikesMapper;
import com.fangyuanyouyue.forum.model.AppraisalDetail;
import com.fangyuanyouyue.forum.model.AppraisalLikes;
import com.fangyuanyouyue.forum.service.AppraisalLikesService;
import com.fangyuanyouyue.forum.service.SchedualWalletService;


@Service(value = "appraisalLikesService")
@Transactional(rollbackFor=Exception.class)
public class AppraisalLikesServiceImpl implements AppraisalLikesService {
	
	@Autowired
	private AppraisalLikesMapper appraisalLikesMapper;
	@Autowired
	private SchedualWalletService schedualWalletService;
	@Autowired
	private AppraisalDetailMapper appraisalDetailMapper;

	@Override
	public Integer countLikes(Integer appraisalId) throws ServiceException {
		return appraisalLikesMapper.countById(appraisalId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	@TxTransaction(isStart=true)
	public void saveLikes(Integer userId, Integer appraisalId,Integer type) throws ServiceException {
		AppraisalLikes appraisalLikes = appraisalLikesMapper.selectByAppraisalIdUserId(appraisalId, userId);
		if(type == 1){
			if(appraisalLikes != null){
				throw new ServiceException("您已点赞，请勿重复点赞！");
			}else{
				appraisalLikes = new AppraisalLikes();
				appraisalLikes.setAddTime(new Date());
				appraisalLikes.setUserId(userId);
				appraisalLikes.setAppraisalId(appraisalId);
				appraisalLikesMapper.insert(appraisalLikes);
				//增加用户行为
				AppraisalDetail detail = appraisalDetailMapper.selectByPrimaryKey(appraisalId);
				BaseResp baseResp = ParseReturnValue.getParseReturnValue(schedualWalletService.addUserBehavior(userId,detail.getUserId(),appraisalId, Status.BUSINESS_TYPE_APPRAISAL.getValue(),Status.BEHAVIOR_TYPE_LIKES.getValue()));
				if(!baseResp.getCode().equals(ReCode.SUCCESS.getValue())){
					throw new ServiceException(baseResp.getCode(),baseResp.getReport());
				}
			}
		}else if(type == 2){
			if(appraisalLikes == null){
				throw new ServiceException("未点赞，请先点赞！");
			}else{
				appraisalLikesMapper.deleteByPrimaryKey(appraisalLikes.getId());
			}
		}else{
			throw new ServiceException("类型错误！");
		}

	}

   
	
}
