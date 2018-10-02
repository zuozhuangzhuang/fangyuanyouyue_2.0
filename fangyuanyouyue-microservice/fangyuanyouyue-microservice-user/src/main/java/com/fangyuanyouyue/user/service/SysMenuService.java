package com.fangyuanyouyue.user.service;

import java.util.List;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.user.dto.admin.AdminMenuDto;
import com.fangyuanyouyue.user.model.SysMenu;
import com.fangyuanyouyue.user.param.AdminMenuParam;

public interface SysMenuService {
    
	/**
	 * 获取全部菜单
	 * @return
	 * @throws ServiceException
	 */
    List<AdminMenuDto> getAllMenu() throws ServiceException;

	/**
	 * 获取角色菜单
	 * @return
	 * @throws ServiceException
	 */
    List<AdminMenuDto> getMenuByRole(Integer roleId) throws ServiceException;

	/**
	 * 获取用户菜单
	 * @return
	 * @throws ServiceException
	 */
    List<AdminMenuDto> getMenuByUser(Integer userId) throws ServiceException;
    
	/**
	 * 获取用户菜单
	 * @throws ServiceException
	 */
    void saveMenu(AdminMenuParam param) throws ServiceException;
    
    /**
     * 删除菜单
     * @param menuId
     */
    void deleteMenu(Integer menuId) ;
    

}
