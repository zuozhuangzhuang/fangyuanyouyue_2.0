package com.fangyuanyouyue.forum.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fangyuanyouyue.forum.dao.AppraisalPvMapper;
import com.fangyuanyouyue.forum.model.AppraisalPv;
import com.fangyuanyouyue.forum.service.AppraisalPvService;


@Service(value = "appraisalPvService")
public class AppraisalPvServiceImp implements AppraisalPvService {
	
	@Autowired
	private AppraisalPvMapper appraisalPvMapper;

	@Override
	public Integer countPv(Integer appraisalId) {
		return appraisalPvMapper.countById(appraisalId);
	}

	@Override
	public void savePv(Integer userId, Integer appraisalId) {
		AppraisalPv model = new AppraisalPv();
		model.setAddTime(new Date());
		model.setUserId(userId);
		model.setAppraisalId(appraisalId);
		
		appraisalPvMapper.insert(model);
		
	}

   
	
}
