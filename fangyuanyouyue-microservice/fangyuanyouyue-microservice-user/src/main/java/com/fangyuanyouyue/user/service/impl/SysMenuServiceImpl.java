package com.fangyuanyouyue.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dao.SysMenuMapper;
import com.fangyuanyouyue.user.dao.SysRoleMenuMapper;
import com.fangyuanyouyue.user.dto.admin.AdminMenuDto;
import com.fangyuanyouyue.user.model.SysMenu;
import com.fangyuanyouyue.user.param.AdminMenuParam;
import com.fangyuanyouyue.user.service.SysMenuService;

@Service(value = "sysMenuService")
@Transactional(rollbackFor=Exception.class)
public class SysMenuServiceImpl implements SysMenuService{
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

	@Override
	public List<AdminMenuDto> getAllMenu() throws ServiceException {
		return getChildren(0);
	}
	
	public List<AdminMenuDto> getChildren(Integer parentId){
		List<SysMenu> menus = sysMenuMapper.selectChildren(parentId);
		List<AdminMenuDto> dtos = new ArrayList<AdminMenuDto>();
		for(SysMenu menu:menus) {
			AdminMenuDto dto = new AdminMenuDto(menu);
			dto.setChildren(getChildren(menu.getId()));
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<AdminMenuDto> getMenuByRole(Integer roleId) throws ServiceException {

		List<SysMenu> menus = sysMenuMapper.selectAll();
		//获取全部菜单，然后获取角色菜单，角色有的则勾选
		
		
		return null;
	}

	@Override
	public List<AdminMenuDto> getMenuByUser(Integer userId) throws ServiceException {

		List<SysMenu> menus = sysMenuMapper.selectAll();
		return AdminMenuDto.toDtoList(menus);
	}

	@Override
	public void saveMenu(AdminMenuParam param) throws ServiceException {
		SysMenu menu = new SysMenu();
		menu.setAddTime(new Date());
		menu.setIcon(param.getIcon());
		menu.setName(param.getText());
		menu.setUrl(param.getUrl());
		sysMenuMapper.insert(menu);
		
		//权限
		for(HashMap<String,String> map:param.getAuth()) {
			
		}
		
	}

	@Override
	public void deleteMenu(Integer menuId) {
		sysMenuMapper.deleteByPrimaryKey(menuId);
		sysRoleMenuMapper.deleteByMenuId(menuId);
	}

	
}
