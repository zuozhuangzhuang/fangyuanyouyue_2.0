package com.fangyuanyouyue.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.user.constant.StatusEnum;
import com.fangyuanyouyue.user.dao.UserAuthApplyMapper;
import com.fangyuanyouyue.user.model.UserAuthApply;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import com.fangyuanyouyue.user.service.UserAuthApplyService;

@Service(value = "userAuthApplyService")
@Transactional(rollbackFor=Exception.class)
public class UserAuthApplyServiceImpl implements UserAuthApplyService {

    @Autowired
    private UserAuthApplyMapper userAuthApplyMapper;
    
    @Autowired
    private SchedualMessageService schedualMessageService;


	@Override
	public Pager getPage(BasePageReq param) {
		
		Integer total = userAuthApplyMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

		List<UserAuthApply> datas = userAuthApplyMapper.getPage(param.getStart()*param.getLimit(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
		Pager pager = new Pager();
		pager.setTotal(total);
		pager.setDatas(datas);
		return pager;
	}


	@Override
	public void updateAccept(Integer id) {
		UserAuthApply model = userAuthApplyMapper.selectByPrimaryKey(id);
		model.setStatus(StatusEnum.AUTH_ACCEPT.getCode());
		userAuthApplyMapper.updateByPrimaryKey(model);
		//发送消息
        schedualMessageService.easemobMessage(model.getUserId().toString(),
                "恭喜您，您申请的实名认证，已通过官方审核！","1","1","");
	}


	@Override
	public void updateReject(Integer id, String reason) {
		UserAuthApply model = userAuthApplyMapper.selectByPrimaryKey(id);
		model.setStatus(StatusEnum.AUTH_REJECT.getCode());
		model.setReason(reason);
		userAuthApplyMapper.updateByPrimaryKey(model);
		//发送消息
        schedualMessageService.easemobMessage(model.getUserId().toString(),
                "很抱歉，您申请的实名认证，官方审核未通过！可重新提交资料再次申请。","1","1","");
		
	}

	
}
