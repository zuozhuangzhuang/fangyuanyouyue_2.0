package com.fangyuanyouyue.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dao.SysMenuMapper;
import com.fangyuanyouyue.user.dao.SysRoleMapper;
import com.fangyuanyouyue.user.dao.SysRoleMenuMapper;
import com.fangyuanyouyue.user.dto.admin.AdminRoleDto;
import com.fangyuanyouyue.user.model.SysRole;
import com.fangyuanyouyue.user.model.SysRoleMenu;
import com.fangyuanyouyue.user.param.AdminRoleParam;
import com.fangyuanyouyue.user.service.SysRoleService;

@Service(value = "sysRoleService")
@Transactional(rollbackFor=Exception.class)
public class SysRoleServiceImpl implements SysRoleService{
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
	@Override
	public List<AdminRoleDto> getAllRole() throws ServiceException {
		return AdminRoleDto.toDtoList(sysRoleMapper.selectAll());
	}
	@Override
	public List<AdminRoleDto> getRoleByMenuId(Integer menuId) throws ServiceException {
		List<SysRole> allRole = sysRoleMapper.selectAll();
		List<SysRole> permissionRole = sysRoleMapper.selectByMenuId(menuId);
		List<AdminRoleDto> dtos = new ArrayList<AdminRoleDto>();
		for(SysRole role:allRole) {
			AdminRoleDto dto = new AdminRoleDto(role);
			dto.setPermission(false);
			for(SysRole pRole:permissionRole) {
				if(dto.getId().intValue()==pRole.getId().intValue()) {
					dto.setPermission(true);
				}
			}
			dtos.add(dto);
		}
		
		return dtos;
	}
	@Override
	public List<AdminRoleDto> getRoleByUserId(Integer userId) throws ServiceException {
		List<SysRole> allRole = sysRoleMapper.selectAll();
		List<SysRole> permissionRole = sysRoleMapper.selectByUserId(userId);
		List<AdminRoleDto> dtos = new ArrayList<AdminRoleDto>();
		for(SysRole role:allRole) {
			AdminRoleDto dto = new AdminRoleDto(role);
			dto.setPermission(false);
			for(SysRole pRole:permissionRole) {
				if(dto.getId().intValue()==pRole.getId().intValue()) {
					dto.setPermission(true);
				}
			}
			dtos.add(dto);
		}
		
		return dtos;
	}
	@Override
	public void saveRole(AdminRoleParam param) throws ServiceException {

		SysRole role = null;
		if(param.getRoleId()!=null&&param.getRoleId()>0) {
			role = sysRoleMapper.selectByPrimaryKey(param.getRoleId());
			//role.setup(new Date());
			role.setName(param.getRoleName());
			role.setStatus(0);
			sysRoleMapper.updateByPrimaryKey(role);
			
		}else {
			role = new SysRole();
			role.setAddTime(new Date());
			role.setName(param.getRoleName());
			role.setStatus(0);
			sysRoleMapper.insert(role);
		}
		
		//保存权限
		sysRoleMenuMapper.deleteByRoleId(role.getId());
		String[] menus = param.getRoleAuth().split(",");
		for(String menuId:menus) {
			Integer	id = Integer.parseInt(menuId);
			SysRoleMenu roleMenu = new SysRoleMenu();
			roleMenu.setAddTime(new Date());
			roleMenu.setMenuId(id);
			roleMenu.setRoleId(role.getId());
			sysRoleMenuMapper.insert(roleMenu);
		}
	}
	@Override
	public void deleteRole(Integer roleId) {
		sysRoleMapper.deleteByPrimaryKey(roleId);
		sysRoleMenuMapper.deleteByRoleId(roleId);
		//TODO 删掉用户对应的角色
	}


	
}
