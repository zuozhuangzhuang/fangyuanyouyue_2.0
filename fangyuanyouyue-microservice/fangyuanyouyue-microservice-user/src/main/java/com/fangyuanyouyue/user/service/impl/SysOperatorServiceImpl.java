package com.fangyuanyouyue.user.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.dao.SysMenuMapper;
import com.fangyuanyouyue.user.dao.SysOperatorMapper;
import com.fangyuanyouyue.user.dao.SysUserRoleMapper;
import com.fangyuanyouyue.user.dto.admin.AdminMenuDto;
import com.fangyuanyouyue.user.dto.admin.AdminOperatorDto;
import com.fangyuanyouyue.user.model.SysOperator;
import com.fangyuanyouyue.user.model.SysUserRole;
import com.fangyuanyouyue.user.param.AdminOperatorParam;
import com.fangyuanyouyue.user.service.SysOperatorService;

@Service(value = "sysOperatorService")
@Transactional(rollbackFor=Exception.class)
public class SysOperatorServiceImpl implements SysOperatorService{
    @Autowired
    private SysOperatorMapper sysOperatorMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysMenuServiceImpl sysMenuServiceImpl;
	@Override
	public List<AdminOperatorDto> getAllOperator() throws ServiceException {
		return AdminOperatorDto.toDtoList(sysOperatorMapper.selectAll());
	}
	@Override
	public List<AdminOperatorDto> getAllOperator(Integer roleId) throws ServiceException {
		return AdminOperatorDto.toDtoList(sysOperatorMapper.selectByRoleId(roleId));
	}
	@Override
	public void deleteOperator(Integer operatorId) {
		sysOperatorMapper.deleteByPrimaryKey(operatorId);
		//删除角色
		sysUserRoleMapper.deleteByUserId(operatorId);
		
	}
	@Override
	public AdminOperatorDto login(String loginCode, String loginPwd) throws ServiceException {
		SysOperator operator = sysOperatorMapper.selectLogin(loginCode, loginPwd);
		
		if(operator==null) {
            return null;
        }
		
		AdminOperatorDto dto = new AdminOperatorDto(operator);
		
		dto.setLoginPwd(null);
		
		operator.setLastLoginTime(new Date());
		
		sysOperatorMapper.updateByPrimaryKey(operator);

		//获取权限菜单
		List<AdminMenuDto> menus = sysMenuServiceImpl.getMenuByUser(operator.getId());

		//处理菜单
		dto.setMenu(menus);
		
		return dto;
	}
	
	@Override
	public void saveOperator(AdminOperatorParam param) throws ServiceException {
		if(param.getUserId()!=null&&param.getUserId()>0) {
			SysOperator oper = sysOperatorMapper.selectByPrimaryKey(param.getUserId());
			//oper.setUserCode(param.getUserCode());
			//oper.setUserName(param.getUserCode());
			if(param.getPassword()!=null&&param.getPassword().trim().length()>0) {
                oper.setLoginPwd(MD5Util.getMD5String(param.getPassword()));
            }
			oper.setStatus(0);
			if("FORBIDDEN".equals(param.getState())) {
				oper.setStatus(1);
			}
			sysOperatorMapper.updateByPrimaryKeySelective(oper);
			
			sysUserRoleMapper.deleteByUserId(param.getUserId());
		}else {
			
			//用户名是否重复了
			if(sysOperatorMapper.countUser(param.getUserCode())>0) {
				throw new ServiceException("登录名不能重复");
			}
			if(param.getPassword()==null||param.getPassword().trim().length()<=0) {
				throw new ServiceException("登录密码不能为空");
			}
			SysOperator oper = new SysOperator();
			oper.setAddTime(new Date());
			oper.setUserCode(param.getUserCode());
			oper.setLoginPwd(MD5Util.getMD5String(param.getPassword()));
			oper.setUserName(param.getUserCode());
			oper.setStatus(0);
			if("FORBIDDEN".equals(param.getState())) {
				oper.setStatus(1);
			}
			sysOperatorMapper.insert(oper);
			param.setUserId(oper.getId());
		}
		for(Integer roleId:param.getRoleIds()) {
			SysUserRole userRole = new SysUserRole();
			userRole.setAddTime(new Date());
			userRole.setOperatorId(param.getUserId());
			userRole.setRoleId(roleId);
			sysUserRoleMapper.insert(userRole);
		}
		
		
	}


}
