package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.forum.dao.AppraisalPvMapper;
import com.fangyuanyouyue.forum.model.AppraisalPv;
import com.fangyuanyouyue.forum.service.AppraisalPvService;


@Service(value = "appraisalPvService")
@Transactional(rollbackFor=Exception.class)
public class AppraisalPvServiceImpl implements AppraisalPvService {
	
	@Autowired
	private AppraisalPvMapper appraisalPvMapper;

	@Override
	public Integer countPv(Integer appraisalId) {
		return appraisalPvMapper.countById(appraisalId);
	}

	@Override
	public void savePv(Integer userId, Integer appraisalId) {
		AppraisalPv appraisalPv = appraisalPvMapper.selectByUserIdAppraisalId(userId, appraisalId);
		if(appraisalPv == null){
			appraisalPv = new AppraisalPv();
			appraisalPv.setAddTime(new Date());
			appraisalPv.setUserId(userId);
			appraisalPv.setAppraisalId(appraisalId);
			appraisalPvMapper.insert(appraisalPv);
		}


	}

   
	
}
