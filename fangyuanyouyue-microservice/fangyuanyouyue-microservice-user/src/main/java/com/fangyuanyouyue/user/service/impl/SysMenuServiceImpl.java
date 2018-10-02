package com.fangyuanyouyue.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dao.SysMenuMapper;
import com.fangyuanyouyue.user.dao.SysRoleMenuMapper;
import com.fangyuanyouyue.user.dto.admin.AdminMenuDto;
import com.fangyuanyouyue.user.model.SysMenu;
import com.fangyuanyouyue.user.model.SysRoleMenu;
import com.fangyuanyouyue.user.param.AdminMenuParam;
import com.fangyuanyouyue.user.param.AdminMenuSaveParam;
import com.fangyuanyouyue.user.param.AdminRoleParam;
import com.fangyuanyouyue.user.service.SysMenuService;
import com.google.gson.JsonArray;

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

		List<AdminMenuDto> menus = getAllMenu();
		//获取全部菜单，然后获取角色菜单，角色有的则勾选
		List<SysMenu> roleMenus = sysMenuMapper.selectByRoleId(roleId);
		for(AdminMenuDto menu:menus) {
			for(SysMenu roleMenu:roleMenus) {
				if(menu.getId().intValue()==roleMenu.getId().intValue()) {
					menu.getState().setSelected(true);
				}
			}
		}
		
		return menus;
	}

	@Override
	public List<AdminMenuDto> getMenuByUser(Integer userId) throws ServiceException {

		List<SysMenu> menus = sysMenuMapper.selectAll();
		return AdminMenuDto.toDtoList(menus);
	}

	@Override
	public void saveMenu(AdminMenuParam param) throws ServiceException {
		//字符串转Json
		AdminMenuSaveParam menu = JSONObject.parseObject(param.getMenu(),AdminMenuSaveParam.class);
		updateChildren(menu,0);
	}

	@Override
	public void deleteMenu(Integer menuId) {
		sysMenuMapper.deleteByPrimaryKey(menuId);
		
		sysMenuMapper.deleteByParentId(menuId);
		
		sysRoleMenuMapper.deleteByMenuId(menuId);
	}
	
	/**
	 * 	更新子节点
	 * @param menu
	 */
	private void updateChildren(AdminMenuSaveParam menu,Integer parentId) {
		
		if(menu==null)return ;
		
		if(menu.getId()==null||menu.getId()==0) {
			SysMenu parent = new SysMenu();
			parent.setName(menu.getText());
			parent.setUrl(menu.getUrl());
			parent.setIcon(menu.getIcon());
			parent.setAddTime(new Date());
			parent.setParentId(parentId);
			sysMenuMapper.insert(parent);
			menu.setId(parent.getId());
		}else {
			SysMenu parent = sysMenuMapper.selectByPrimaryKey(menu.getId());
			parent.setName(menu.getText());
			parent.setUrl(menu.getUrl());
			parent.setIcon(menu.getIcon());
			parent.setParentId(parentId);
			sysMenuMapper.updateByPrimaryKey(parent);
		}

		//权限
		sysRoleMenuMapper.deleteByMenuId(menu.getId());
		if(menu.getAuth()!=null) {
			System.out.println("size====="+menu.getAuth().size());
			for(AdminRoleParam role:menu.getAuth()) {
				SysRoleMenu roleMenu = new SysRoleMenu();
				roleMenu.setMenuId(menu.getId());
				roleMenu.setAddTime(new Date());
				roleMenu.setRoleId(role.getId());
				sysRoleMenuMapper.insert(roleMenu);
			}
		}
		
		
		if(menu.getChildren()!=null&&menu.getChildren().size()>0) {
			for(AdminMenuSaveParam dto:menu.getChildren()) {
				updateChildren(dto,menu.getId());
			}
		}
	}

	
}
