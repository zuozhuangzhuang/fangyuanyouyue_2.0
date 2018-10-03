package com.fangyuanyouyue.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dao.SysOperatorMapper;
import com.fangyuanyouyue.user.dao.SysRoleMenuMapper;
import com.fangyuanyouyue.user.dto.admin.AdminMenuDto;
import com.fangyuanyouyue.user.dto.admin.AdminOperatorDto;
import com.fangyuanyouyue.user.model.SysMenu;
import com.fangyuanyouyue.user.model.SysRoleMenu;
import com.fangyuanyouyue.user.param.AdminMenuParam;
import com.fangyuanyouyue.user.param.AdminMenuSaveParam;
import com.fangyuanyouyue.user.param.AdminRoleParam;
import com.fangyuanyouyue.user.service.SysOperatorService;

@Service(value = "sysOperatorService")
@Transactional(rollbackFor=Exception.class)
public class SysOperatorServiceImpl implements SysOperatorService{
    @Autowired
    private SysOperatorMapper sysOperatorMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
	@Override
	public List<AdminOperatorDto> getAllOperator() throws ServiceException {
		return AdminOperatorDto.toDtoList(sysOperatorMapper.selectAll());
	}
	@Override
	public List<AdminOperatorDto> getAllOperator(Integer roleId) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void deleteOperator(Integer operatorId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AdminOperatorDto login(String loginCode, String loginPwd) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}


	
}
