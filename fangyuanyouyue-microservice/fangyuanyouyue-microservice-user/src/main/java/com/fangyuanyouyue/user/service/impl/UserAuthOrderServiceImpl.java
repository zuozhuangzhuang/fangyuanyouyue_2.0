package com.fangyuanyouyue.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.BasePageReq;
import com.fangyuanyouyue.base.Pager;
import com.fangyuanyouyue.user.constant.StatusEnum;
import com.fangyuanyouyue.user.dao.UserAuthOrderMapper;
import com.fangyuanyouyue.user.dao.UserInfoExtMapper;
import com.fangyuanyouyue.user.model.UserAuthOrder;
import com.fangyuanyouyue.user.model.UserInfoExt;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import com.fangyuanyouyue.user.service.UserAuthOrderService;

@Service(value = "userAuthOrderService")
@Transactional(rollbackFor=Exception.class)
public class UserAuthOrderServiceImpl implements UserAuthOrderService {

    @Autowired
    private UserAuthOrderMapper userAuthOrderMapper;

    @Autowired
    private UserInfoExtMapper userInfoExtMapper;
    
    @Autowired
    private SchedualMessageService schedualMessageService;

	@Override
	public Pager getPage(BasePageReq param) {
		
		Integer total = userAuthOrderMapper.countPage(param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate());

		List<UserAuthOrder> datas = userAuthOrderMapper.getPage(param.getStart()*param.getLimit(),param.getLimit(),param.getKeyword(),param.getStatus(),param.getStartDate(),param.getEndDate(),param.getOrders(),param.getAscType());
		Pager pager = new Pager();
		pager.setTotal(total);
		pager.setDatas(datas);
		return pager;
	}


	@Override
	public void updateAccept(Integer id) {
		UserAuthOrder model = userAuthOrderMapper.selectByPrimaryKey(id);
		UserInfoExt ext = userInfoExtMapper.selectByUserId(model.getUserId());
		ext.setAuthType(1);
		userInfoExtMapper.updateByPrimaryKey(ext);
		
		//发送消息
        schedualMessageService.easemobMessage(model.getUserId().toString(),
                "恭喜您，您申请的认证店铺已通过官方审核！您的店铺已添加认证店铺专属标识，快拉您的好友来尽情购买吧！","1","1","");
	}


	@Override
	public void updateReject(Integer id, String reason) {
		//TODO 拒绝要退回余额
		UserAuthOrder model = userAuthOrderMapper.selectByPrimaryKey(id);
		UserInfoExt ext = userInfoExtMapper.selectByUserId(model.getUserId());
		ext.setAuthType(2);
		userInfoExtMapper.updateByPrimaryKey(ext);
		//发送消息
        schedualMessageService.easemobMessage(model.getUserId().toString(),
                "很抱歉，您申请的认证店铺未通过官方审核，可联系客服咨询详情。","1","1","");
		
	}

	
}
